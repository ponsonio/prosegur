package org.domain.sdm.session;

import org.domain.sdm.entity.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("sdmRolHome")
public class SdmRolHome extends EntityHome<SdmRol> {

	
	@In
	private EntityManager entityManager;
	
	public void setSdmRolId(Long id) {
		setId(id);
	}

	public Long getSdmRolId() {
		return (Long) getId();
	}

	@Override
	protected SdmRol createInstance() {
		SdmRol sdmRol = new SdmRol();
		return sdmRol;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public SdmRol getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<SdmRolXUsuario> getSdmRolXUsuarios() {
		return getInstance() == null ? null : new ArrayList<SdmRolXUsuario>(
				getInstance().getSdmRolXUsuarios());
	}

	public  ArrayList<SdmRol> obtenerRoles(){
		Query query =  entityManager.createQuery("From SdmRol r");
		return  (ArrayList<SdmRol>)query.getResultList();
	}


	
	
}
