
package org.jax.mgi.mgd.api.model;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.hibernate.Hibernate;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.proxy.HibernateProxy;
import org.jboss.logging.Logger;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

/**
 * Provides compatibility cleanup for Hibernate entity deletion.
 *
 * <p>When a child entity is deleted, Hibernate may leave the deleted entity
 * in a parent's in-memory OneToMany collection. This helper removes the
 * deleted entity from collections belonging to other managed entities in
 * the current Hibernate persistence context.</p>
 *
 * <p>This restores the previous collection-cleanup behavior expected by
 * the application and prevents stale references from remaining in managed
 * parent objects.</p>
 * 
 * see: https://discourse.hibernate.org/t/instance-save-transient-before/10293?utm_source=chatgpt.com
 *
 * <p>Strategy:</p>
 * <ul>
 *   <li>Uses OneToMany and JoinColumn mapping metadata to identify
 *       matching parent-child relationships.</li>
 * </ul>
 *
 * <p>NOTE: This is an in-memory persistence-context cleanup mechanism.
 * It does not replace database foreign-key handling, cascading deletes,
 * or Hibernate entity removal.</p>
 */
public class HibernateDeleteCompatibility {
    private static final Logger log = Logger.getLogger(HibernateDeleteCompatibility.class);

    /**
     * Removes a deleted child entity from matching OneToMany parent collections
     * in the current Hibernate persistence context.
     *
     * This method uses OneToMany and JoinColumn mapping metadata to identify
     * parent-child relationships, compares foreign-key values, and removes the
     * deleted child from the corresponding in-memory collection.
     *
     * @param session the current Hibernate session
     * @param deletedEntity the child entity being deleted
     */
    public static void removeReferences(SessionImplementor session, Object deletedEntity) {
        if (deletedEntity == null) return;

        // Resolve the actual entity class, including when the deleted entity is represented by a Hibernate proxy.
        Class<?> deletedClass = getEntityClass(deletedEntity);
        // Get all entities currently managed by this Hibernate persistence context.
        Iterator<Object> iterator = session.getPersistenceContextInternal().managedEntitiesIterator();
        // Copy managed entities into a separate list before processing to avoid modifying the persistence-context iterator.
        List<Object> managedEntities = new ArrayList<>();
        while (iterator.hasNext()) managedEntities.add(iterator.next());
        
        // Examine every managed entity as a potential parent of the deleted entity.
        for (Object owner : managedEntities) {
            // Do not inspect the deleted entity itself as a parent.
            if (owner == deletedEntity) continue;
            // Get the actual class of the parent, resolving Hibernate proxies.
            Class<?> ownerClass = Hibernate.getClass(owner);
            // Walk through the parent class hierarchy so inherited @OneToMany associations are also included.
            while (ownerClass != null && ownerClass != Object.class) {
                log.info("Check class: " + ownerClass.getSimpleName());
                // Inspect all fields declared in the current class.
                for (Field field : ownerClass.getDeclaredFields()) {
                    // Only process fields explicitly mapped as @OneToMany.
                    if (!field.isAnnotationPresent(OneToMany.class)) continue;
                    OneToMany oneToMany = field.getAnnotation(OneToMany.class);
                    // Determine the child entity class from targetEntity.
                    Class<?> targetClass = oneToMany.targetEntity();
                    // If targetEntity was not explicitly specified, determine the child type from the collection's generic type.
                    if (targetClass == void.class) {
                        if (!(field.getGenericType() instanceof ParameterizedType type)) continue;
                        if (!(type.getActualTypeArguments()[0] instanceof Class<?> genericClass)) continue;
                        targetClass = genericClass;
                    }
                    // Skip associations whose child type does not match the deleted entity class, including subclasses.
                    if (!targetClass.isAssignableFrom(deletedClass)) continue;
                    // Get the @JoinColumn mapping that defines the child foreign key.
                    JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
                    // Cannot determine the relationship without @JoinColumn.
                    if (joinColumn == null) continue;
                    // foreignKey: column in the child table referencing the parent.
                    // referencedKey: column in the parent table referenced by the child.
                    String foreignKey = joinColumn.name();
                    String referencedKey = joinColumn.referencedColumnName();
                    // Both column names must be explicitly available for comparison.
                    if (foreignKey.isEmpty() || referencedKey.isEmpty()) continue;
                    log.info("  owner " + ownerClass.getSimpleName() + ": " + deletedClass.getSimpleName());
                    try {
                        // Find the Java fields corresponding to the database columns, including inherited fields.
                        Field deletedKeyField = findColumnField(deletedClass, foreignKey);
                        Field ownerKeyField = findColumnField(ownerClass, referencedKey);
                        // Make the fields accessible for reflection. Skip if either field cannot be accessed.
                        if (!deletedKeyField.trySetAccessible() || !ownerKeyField.trySetAccessible()) continue;
                        // Compare the child's foreign-key value with the parent's referenced-key value.
                        // If they differ, this parent does not own the deleted child.
                        if (!Objects.equals(deletedKeyField.get(deletedEntity), ownerKeyField.get(owner))) continue;
                        // Allow reflection access to the parent collection field.
                        if (!field.trySetAccessible()) continue;
                        // Get the actual collection from the parent entity.
                        Object value = field.get(owner);
                        // Nothing to clean up if the collection is null.
                        if (value == null) continue;
                        // Initialize the Hibernate collection if it is lazy-loaded so its elements can be inspected.
                        Hibernate.initialize(value);
                        log.info("  deletedKeyField: " + deletedKeyField.getName());
                        log.info("  ownerKeyField: " + ownerKeyField.getName());
                        // Handle collection-based @OneToMany associations.
                        if (value instanceof Collection<?> collection) {
                            // Iterate through the collection so matching elements can be removed safely.
                            Iterator<?> elements = collection.iterator();
                            while (elements.hasNext()) {
                                Object element = elements.next();
                                // Compare entity type and persistent identifier, handling different objects representing the same database entity.
                                if (sameEntity(session, element, deletedEntity)) {
                                    log.infof("  Removing %s from %s.%s", deletedClass.getSimpleName(), ownerClass.getSimpleName(), field.getName());
                                    // Remove the deleted child from the parent's in-memory collection.
                                    elements.remove();
                                }
                            }
                        }
                    } catch (IllegalAccessException e) {
                        // Reflection failed while accessing an entity field.
                        throw new IllegalStateException("Cannot inspect association: " + field.getName(), e);
                    }
                }
                // Continue checking the superclass for inherited associations.
                ownerClass = ownerClass.getSuperclass();
            }
        }
    }
	
	/**
	 * Determines whether two objects represent the same persistent entity.
	 *
	 * <p>First compares Java object identity, then compares Hibernate entity
	 * types and persistent identifiers.</p>
	 *
	 * @param session the current Hibernate session
	 * @param first first entity
	 * @param second second entity
	 * @return true if both objects represent the same persistent entity
	 */	
	private static Field findColumnField(Class<?> entityClass, String columnName) {
	    while (entityClass != null && entityClass != Object.class) {
	        for (Field field : entityClass.getDeclaredFields()) {
	            Column column = field.getAnnotation(Column.class);
	            String name = column != null && !column.name().isEmpty() ? column.name() : field.getName();
	            if (name.equalsIgnoreCase(columnName)) return field;
	        }
	        entityClass = entityClass.getSuperclass();
	    }
	    throw new IllegalStateException("Cannot find column: " + columnName);
	}

	/**
	 * Finds the Java field corresponding to a database column.
	 *
	 * <p>Searches the specified entity class and its superclasses.
	 * Uses Column.name when available; otherwise uses the Java field name.</p>
	 *
	 * @param entityClass entity class to inspect
	 * @param columnName database column name
	 * @return matching Java field
	 * @throws IllegalStateException if no matching field is found
	 */	
	private static boolean sameEntity(SessionImplementor session, Object first, Object second) {
	    if (first == second) return true;
	    if (first == null || second == null) return false;

	    Class<?> firstClass = Hibernate.getClass(first);
	    Class<?> secondClass = Hibernate.getClass(second);
	    if (!firstClass.equals(secondClass)) return false;

	    EntityPersister persister = session.getEntityPersister(null, first);
	    Object firstId = persister.getIdentifier(first, session);
	    Object secondId = persister.getIdentifier(second, session);

	    return firstId != null && Objects.equals(firstId, secondId);
	}

	/**
	 * Returns the underlying entity class, resolving Hibernate proxies
	 * when necessary.
	 *
	 * @param entity entity or Hibernate proxy
	 * @return underlying persistent entity class
	 */	
    private static Class<?> getEntityClass(Object entity) {
        return entity instanceof HibernateProxy
                ? ((HibernateProxy) entity).getHibernateLazyInitializer().getPersistentClass()
                : entity.getClass();
    }
}
