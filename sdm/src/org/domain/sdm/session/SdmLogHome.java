package org.domain.sdm.session;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.domain.sdm.entity.*;
import org.hibernate.Transaction;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
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
		org.hibernate.Session hibernateSession = ((org.hibernate.Session)entityManager.getDelegate()).getSessionFactory().openSession();
		//System.out.println("Abrir Sesión para el log");
		//Transaction tx = null;
		try {
			//tx = hibernateSession.beginTransaction();
			hibernateSession.persist(sdmLog);
			hibernateSession.flush();
			//tx.commit();
			//System.out.println("Escribo el log");
		}finally{
			//System.out.println("Cerrar Sesión para el log");
			hibernateSession.close();
		}
		
		
		//entityManager.persist(sdmLog);
		return sdmLog;

	
	}
	
	
	public List<SdmLog> buscarLogFechas(Date fechaIni , Date fechaHasta) {
		
		Query query =  entityManager.createQuery("From SdmLog i  where  i.fecha >= :fechaIni and i.fecha <= :fechaHasta ");
		return (List<SdmLog>)query.setParameter("fechaIni",fechaIni).setParameter("fechaHasta",fechaHasta).getResultList();

	}
}
