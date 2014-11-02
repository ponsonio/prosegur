package org.domain.sdm.session;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.domain.sdm.entity.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("sdmParametroHome")
public class SdmParametroHome extends EntityHome<SdmParametro> {
	
	
	@In
	private EntityManager entityManager;

	public void setSdmParametroNombre(String id) {
		setId(id);
	}

	public String getSdmParametroNombre() {
		return (String) getId();
	}

	@Override
	protected SdmParametro createInstance() {
		SdmParametro sdmParametro = new SdmParametro();
		return sdmParametro;
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

	public SdmParametro getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	
	public List<SdmParametro> getParametro(String nombreParametro){
		Query query =  entityManager.createQuery("From SdmParametro p where p.nombre = :nombreParametro");
		return (List<SdmParametro>)query.setParameter("nombreParametro",nombreParametro).getResultList();
	}

}
