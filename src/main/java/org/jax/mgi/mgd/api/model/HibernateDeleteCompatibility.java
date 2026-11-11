
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

public class HibernateDeleteCompatibility {
    private static final Logger log = Logger.getLogger(HibernateDeleteCompatibility.class);


	public static void removeReferences(SessionImplementor session, Object deletedEntity) {
	    if (deletedEntity == null) return;
	    Class<?> deletedClass = getEntityClass(deletedEntity);
	    Iterator<Object> iterator = session.getPersistenceContextInternal().managedEntitiesIterator();
	    List<Object> managedEntities = new ArrayList<>();
	    while (iterator.hasNext()) managedEntities.add(iterator.next());
	    for (Object owner : managedEntities) {
	        if (owner == deletedEntity) continue;
	        Class<?> ownerClass = Hibernate.getClass(owner);
	        while (ownerClass != null && ownerClass != Object.class) {
		        log.info("Check class: " + ownerClass.getSimpleName());
	            for (Field field : ownerClass.getDeclaredFields()) {
	                if (!field.isAnnotationPresent(OneToMany.class)) continue;
	                OneToMany oneToMany = field.getAnnotation(OneToMany.class);
	                Class<?> targetClass = oneToMany.targetEntity();
	                if (targetClass == void.class) {
	                    if (!(field.getGenericType() instanceof ParameterizedType type)) continue;
	                    if (!(type.getActualTypeArguments()[0] instanceof Class<?> genericClass)) continue;
	                    targetClass = genericClass;
	                }
	                if (!targetClass.isAssignableFrom(deletedClass)) continue;
	                JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
	                if (joinColumn == null) continue;
	                String foreignKey = joinColumn.name();
	                String referencedKey = joinColumn.referencedColumnName();
	                if (foreignKey.isEmpty() || referencedKey.isEmpty()) continue;
	                log.info("  owner " + ownerClass.getSimpleName() + ": " + deletedClass.getSimpleName());
	                try {
	                    Field deletedKeyField = findColumnField(deletedClass, foreignKey);
	                    Field ownerKeyField = findColumnField(ownerClass, referencedKey);
	                    if (!deletedKeyField.trySetAccessible() || !ownerKeyField.trySetAccessible()) continue;
	                    if (!Objects.equals(deletedKeyField.get(deletedEntity), ownerKeyField.get(owner))) continue;
	                    if (!field.trySetAccessible()) continue;
	                    Object value = field.get(owner);
	                    if (value == null) continue;
	                    Hibernate.initialize(value);
	                    log.info("  deletedKeyField: " + deletedKeyField.getName());
	                    log.info("  ownerKeyField: " + ownerKeyField.getName());
	                    if (value instanceof Collection<?> collection) {
	                        Iterator<?> elements = collection.iterator();
	                        while (elements.hasNext()) {
	                            Object element = elements.next();
	                            if (sameEntity(session, element, deletedEntity)) {
	                                log.infof("  Removing %s from %s.%s", deletedClass.getSimpleName(), ownerClass.getSimpleName(), field.getName());
	                                elements.remove();
	                            }
	                        }
	                    }
	                } catch (IllegalAccessException e) {
	                    throw new IllegalStateException("Cannot inspect association: " + field.getName(), e);
	                }
	            }
	            ownerClass = ownerClass.getSuperclass();
	        }
	    }
	}
	
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

    
    
	public static void removeReferences2(SessionImplementor session, Object deletedEntity) {
	    Iterator<Object> iterator = session.getPersistenceContextInternal().managedEntitiesIterator();
	    List<Object> managedEntities = new ArrayList<>();
	    while (iterator.hasNext()) {
	        Object entity = iterator.next();
	        if (entity != deletedEntity) managedEntities.add(entity);
	    }
	    for (Object owner : managedEntities) {
	        if (!Hibernate.isInitialized(owner)) Hibernate.initialize(owner);
	        Class<?> currentClass = Hibernate.getClass(owner);
	        log.info("Check class: " + currentClass.getSimpleName());
//	        if (!"Antibody".equals(currentClass.getSimpleName())) continue;
	        while (currentClass != null && currentClass != Object.class) {
	            for (Field field : currentClass.getDeclaredFields()) {
	                if (!field.isAnnotationPresent(OneToMany.class)) continue;
	                log.info("  Check field: " + field.getName());
//	                if (!"markers".equals(field.getName())) continue;
	                try {
	                    if (!field.trySetAccessible()) throw new IllegalStateException("Cannot access association: " + currentClass.getName() + "." + field.getName());
	                    Object value = field.get(owner);
	                    if (value == null || !Hibernate.isInitialized(value)) continue;
//	                    if (value == null) continue;
//	                    Hibernate.initialize(value);
	                    if (value instanceof Collection<?> collection) {
	                        Iterator<?> elements = collection.iterator();
	                        while (elements.hasNext()) {
	                            Object element = elements.next();
	                            if (sameEntity(session, element, deletedEntity)) {
	                                log.infof("    Removing %s from %s.%s (owner: %s)", getEntityClass(element).getSimpleName(), currentClass.getSimpleName(), field.getName(), owner);
	                                elements.remove();
	                            }
	                        }
	                    } else if (value instanceof Map<?, ?> map) {
	                        Iterator<?> entries = map.entrySet().iterator();
	                        while (entries.hasNext()) {
	                            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) entries.next();
	                            if (sameEntity(session, entry.getValue(), deletedEntity)) {
	                                log.infof("    Removing %s from %s.%s (owner: %s)", getEntityClass(entry.getValue()).getSimpleName(), currentClass.getSimpleName(), field.getName(), owner);
	                                entries.remove();
	                            }
	                        }
	                    }
	                } catch (IllegalAccessException e) {
	                    throw new IllegalStateException("Cannot inspect association: " + field.getName(), e);
	                }
	            }
	            currentClass = currentClass.getSuperclass();
	        }
	    }
	}

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

    private static Class<?> getEntityClass(Object entity) {
        return entity instanceof HibernateProxy
                ? ((HibernateProxy) entity).getHibernateLazyInitializer().getPersistentClass()
                : entity.getClass();
    }
}
