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
			, String referencia , String operacion , String tipo) {

		String stringQuery = " Select i From SdmLog i  where  i.fecha >= :fechaIni  and  i.fecha <= :fechaHasta " ;

		if (usuario != null){
			stringQuery = stringQuery + " and i.id in (Select i.id From SdmLog i  where  i.fecha >= :fechaIni  and  i.fecha <= :fechaHasta and i.usuario like :usuario ) " ;
		}
		
		
		if (mensaje != null){
			stringQuery = stringQuery + " and i.id in (Select i.id From SdmLog i  where  i.fecha >= :fechaIni  and  i.fecha <= :fechaHasta and i.mensaje like :mensaje ) " ;
		}
		
		if (referencia != null){
			stringQuery = stringQuery + " and i.id in (Select i.id From SdmLog i  where  i.fecha >= :fechaIni  and  i.fecha <= :fechaHasta and i.referencia like :referencia ) " ;
		}
		
		if (operacion != null){
			stringQuery = stringQuery + " and i.id in (Select i.id From SdmLog i  where  i.fecha >= :fechaIni  and  i.fecha <= :fechaHasta and i.operacion like :operacion ) " ;
		}
		
		if (tipo != null){
			stringQuery = stringQuery + " and i.id in (Select i.id From SdmLog i  where  i.fecha >= :fechaIni  and  i.fecha <= :fechaHasta and i.tipo = :tipo ) " ;
		}
		
		
		System.out.println ("stringQuery:"+stringQuery) ;
		
		Query query =  entityManager.createQuery(stringQuery);
		query.setParameter("fechaIni",fechaIni).setParameter("fechaHasta",fechaHasta) ;
		
		if (usuario != null) query.setParameter("usuario", "%"+usuario+"%");
		if (mensaje != null) query.setParameter("mensaje", "%"+mensaje+"%");
		if (referencia != null) query.setParameter("referencia", "%"+referencia+"%");
		if (operacion != null) query.setParameter("operacion", "%"+operacion+"%");
		if (tipo != null) query.setParameter("tipo", tipo);
		
		return (List<SdmLog>)query.getResultList();

	}
	
	

	
}
