package org.domain.sdm.session;

import javax.persistence.EntityManager;

import org.domain.sdm.entity.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("sdmLogHome")
public class SdmLogHome extends EntityHome<SdmLog> {
	
	@In
	private EntityManager entityManager;

	public void setSdmLogId(Long id) {
		setId(id);
	}

	public Long getSdmLogId() {
		return (Long) getId();
	}

	@Override
	protected SdmLog createInstance() {
		SdmLog sdmLog = new SdmLog();
		return sdmLog;
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

	public SdmLog getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	
	public SdmLog crear(SdmLog sdmLog ){
		entityManager.persist(sdmLog);
		return sdmLog;
	}
}
