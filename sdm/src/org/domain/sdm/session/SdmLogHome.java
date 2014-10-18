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
	
	/**
	 * 
	 * @param fechaIni
	 * @param fechaHasta
	 * @param usuario
	 * @param mensaje
	 * @param referencia
	 * @param operacion
	 * @return
	 */
	public List<SdmLog> buscarLogFechas(Date fechaIni , Date fechaHasta, String usuario , String mensaje
			, String referencia , String operacion) {
		
		Query query =  entityManager.createQuery("From SdmLog i  where  i.fecha >= :fechaIni  and  i.fecha <= :fechaHasta and i.id in "
				+ "( select i.id From SdmLog i  where  i.usuario like :usr or  i.mensaje like :mensaje or i.referencia like :referencia or i.operacion like :operacion  )");
		
		return (List<SdmLog>)query.setParameter("usr",usuario).setParameter("mensaje",mensaje)
				.setParameter("referencia",referencia).setParameter("operacion",operacion);
		
		//return (List<SdmLog>)query.setParameter("fechaIni",fechaIni).setParameter("fechaHasta",fechaHasta).getResultList();

	}
}
