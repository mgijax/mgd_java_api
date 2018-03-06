package org.jax.mgi.mgd.api.model.voc.dao;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.entities.Vocabulary;

@ApplicationScoped
public class VocabularyDAO extends PostgresSQLDAO<Vocabulary> {

	protected VocabularyDAO() {
		super(Vocabulary.class);
	}
	
	public Vocabulary getByName(String vocabName) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", vocabName);
		if(search(map).total_count > 0) {
			return search(map).items.get(0);
		}
		return null;
	}

}
