package org.domain.sdm.session;

import org.domain.sdm.entity.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.framework.EntityHome;

@Name("sdmInformeViaticosHome")
public class SdmInformeViaticosHome extends EntityHome<SdmInformeViaticos> {

	@In(create = true)
	SdmEmpresaHome sdmEmpresaHome;
	@In(create = true)
	SdmEmpleadoHome sdmEmpleadoHome;
	@In(create = true)
	SdmDivicionHome sdmDivicionHome;
	@In(create = true)
	SdmCentroCostoHome sdmCentroCostoHome;
	@In(create = true)
	SdmDelegacionHome sdmDelegacionHome;

	
	
	@In
	private EntityManager entityManager;

	public void setSdmInformeViaticosId(Long id) {
		setId(id);
	}

	public Long getSdmInformeViaticosId() {
		return (Long) getId();
	}

	@Override
	protected SdmInformeViaticos createInstance() {
		SdmInformeViaticos sdmInformeViaticos = new SdmInformeViaticos();
		return sdmInformeViaticos;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		SdmEmpresa sdmEmpresa = sdmEmpresaHome.getDefinedInstance();
		if (sdmEmpresa != null) {
			getInstance().setSdmEmpresa(sdmEmpresa);
		}
		SdmEmpleado sdmEmpleado = sdmEmpleadoHome.getDefinedInstance();
		if (sdmEmpleado != null) {
			getInstance().setSdmEmpleado(sdmEmpleado);
		}
		SdmDivicion sdmDivicion = sdmDivicionHome.getDefinedInstance();
		if (sdmDivicion != null) {
			getInstance().setSdmDivicion(sdmDivicion);
		}
		SdmCentroCosto sdmCentroCosto = sdmCentroCostoHome.getDefinedInstance();
		if (sdmCentroCosto != null) {
			getInstance().setSdmCentroCosto(sdmCentroCosto);
		}
		SdmDelegacion sdmDelegacion = sdmDelegacionHome.getDefinedInstance();
		if (sdmDelegacion != null) {
			getInstance().setSdmDelegacion(sdmDelegacion);
		}
	}

	public boolean isWired() {
		if (getInstance().getSdmEmpresa() == null)
			return false;
		if (getInstance().getSdmEmpleado() == null)
			return false;
		if (getInstance().getSdmDivicion() == null)
			return false;
		if (getInstance().getSdmCentroCosto() == null)
			return false;
		if (getInstance().getSdmDelegacion() == null)
			return false;
		return true;
	}

	public SdmInformeViaticos getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<SdmInformeViaticoDetalle> getSdmInformeViaticoDetalles() {
		return getInstance() == null ? null
				: new ArrayList<SdmInformeViaticoDetalle>(getInstance()
						.getSdmInformeViaticoDetalles());
	}
	
	
	/**
	 * Persiste el informe (y su lista de detalles) enviados como parámetros
	 * @param sdmInformeViaticos
	 * @return
	 */
	@Transactional(TransactionPropagationType.REQUIRED)
	public SdmInformeViaticos grabarInformeViaticos(SdmInformeViaticos sdmInformeViaticos){
		entityManager.persist(sdmInformeViaticos);
		Iterator<SdmInformeViaticoDetalle> i = sdmInformeViaticos
				.getSdmInformeViaticoDetalles().iterator();
		
		//int l = 0;
		while (i.hasNext()) {
			SdmInformeViaticoDetalle detalle = i.next();
			//detalle.setCorelativoInforme(l);
			//l++;
			entityManager.persist(detalle);
		}
		//entityManager.flush();		
		return sdmInformeViaticos;
	}
	
	/**
	 * Obtiene el informe de viatico por id
	 * @param idSdmInformeViaticos
	 * @return
	 */
    public SdmInformeViaticos  obtenerInformeViaticos(long idSdmInformeViaticos){
		Query query =  entityManager.createQuery("From SdmInformeViaticos i where i.id = :id");
		SdmInformeViaticos sdmInformeViaticos =  (SdmInformeViaticos)query.setParameter("id",idSdmInformeViaticos).getSingleResult();
		return sdmInformeViaticos;
    }
    
    /**
     * Anula el informe enviado 
     * @param sdmInformeViaticos
     * @return
     */
    public SdmInformeViaticos  anularInformeViaticos(SdmInformeViaticos sdmInformeViaticos){
    	sdmInformeViaticos.setAnulado(true);
    	entityManager.persist(sdmInformeViaticos);
    	return sdmInformeViaticos ;
    }
    	
    /**
     * Obtiene un informe de viatico activo por correlativo
     * @param correlativo
     * @return
     */
    public SdmInformeViaticos  buscarInformeActivosXCorrelativo(long correlativo){
		Query query =  entityManager.createQuery("From SdmInformeViaticos i where i.id= :correlativo and i.anulado=false)");
		return (SdmInformeViaticos)query.setParameter("correlativo",correlativo).getSingleResult();
    }
    
    /**
     * Obtiene un informe de viatico activo por correlativo
     * @param correlativo
     * @return
     */
    public SdmInformeViaticos  buscarInformeXCorrelativo(long correlativo){
		Query query =  entityManager.createQuery("From SdmInformeViaticos i where i.id= :correlativo)");
		return (SdmInformeViaticos)query.setParameter("correlativo",correlativo).getSingleResult();
    }    
    /**
     * 
     * @param idEmpleado
     * @return
     */
    public List<SdmInformeViaticos>  buscarInformesActivosXLiquidarXEmpleado(long idEmpleado){
		Query query =  entityManager.createQuery("From SdmInformeViaticos i where i.anulado=false and i.sdmEmpleado.id = :idEmpleado and i not in (select  l.sdmInformeViaticos.id From SdmLiquidacionInformeViaticos l where l.anulado =false)");
		return (List<SdmInformeViaticos>)query.setParameter("idEmpleado",idEmpleado).getResultList();
    }

    /**
     * 
     * @param idEmpleado
     * @param fechaIni
     * @param fechaHasta
     * @return
     */
    public List<SdmInformeViaticos>  buscarInformesActivosXLiquidarXEmpleadoFechas(long idEmpleado, Date fechaIni , Date fechaHasta ){
		Query query =  entityManager.createQuery("From SdmInformeViaticos i  where i.anulado=false and i.sdmEmpleado.id = :idEmpleado  and i.fecha >= :fechaIni and i.fecha <= :fechaHasta and i not in (select l.sdmInformeViaticos.id From SdmLiquidacionInformeViaticos l where l.anulado =false)");
		return (List<SdmInformeViaticos>)query.setParameter("idEmpleado",idEmpleado).setParameter("fechaIni",fechaIni).setParameter("fechaHasta",fechaHasta).getResultList();
    }
    
    /**
     * 
     * @param idEmpleado
     * @param fechaIni
     * @param fechaHasta
     * @return
     */
    public List<SdmInformeViaticos>  buscarInformesActivosXLiquidarXFechas(long idEmpleado, Date fechaIni , Date fechaHasta ){
		Query query =  entityManager.createQuery("From SdmInformeViaticos i  where i.anulado=false  and i.fecha >= :fechaIni and i.fecha <= :fechaHasta and i not in (select l.sdmInformeViaticos.id From SdmLiquidacionInformeViaticos l where l.anulado =false)");
		return (List<SdmInformeViaticos>)query.setParameter("fechaIni",fechaIni).setParameter("fechaHasta",fechaHasta).getResultList();
    }
    
    public List<SdmInformeViaticos>  buscarInformesActivosXLiquidarXFechas( Date fechaIni , Date fechaHasta ){
		Query query =  entityManager.createQuery("From SdmInformeViaticos i  where i.anulado=false and i.fecha >= :fechaIni and i.fecha <= :fechaHasta and i not in (select l.sdmInformeViaticos.id From SdmLiquidacionInformeViaticos l where l.anulado =false)");
		return (List<SdmInformeViaticos>)query.setParameter("fechaIni",fechaIni).setParameter("fechaHasta",fechaHasta).getResultList();
    }

    public List<SdmInformeViaticos>  buscarInformesActivosLiquidadosXFechas(Date fechaIni , Date fechaHasta ){
		Query query =  entityManager.createQuery("From SdmInformeViaticos i  where i.anulado=false  and i.fecha >= :fechaIni and i.fecha <= :fechaHasta and i in (select l.sdmInformeViaticos.id From SdmLiquidacionInformeViaticos l where l.anulado =false)");
		return (List<SdmInformeViaticos>)query.setParameter("fechaIni",fechaIni).setParameter("fechaHasta",fechaHasta).getResultList();
    }
    
    public List<SdmInformeViaticos>  buscarInformesActivosXFechas(Date fechaIni , Date fechaHasta ){
		Query query =  entityManager.createQuery("From SdmInformeViaticos i  where i.anulado=false  and i.fecha >= :fechaIni and i.fecha <= :fechaHasta");
		return (List<SdmInformeViaticos>)query.setParameter("fechaIni",fechaIni).setParameter("fechaHasta",fechaHasta).getResultList();
    }
    
    public List<SdmInformeViaticos>  buscarInformesLiquidacionesActivosXFechas( Date fechaIni , Date fechaHasta ){
		Query query =  entityManager.createQuery(" select i , l from SdmInformeViaticos i left join i.sdmLiquidacionInformeViaticoses l where i.anulado=false  and i.fecha >= :fechaIni and i.fecha <= :fechaHasta");
		ArrayList<SdmInformeViaticos> array =  new ArrayList<SdmInformeViaticos>();
		
		ArrayList<Object[]> resutl = (ArrayList<Object[]>) query.setParameter("fechaIni",fechaIni).setParameter("fechaHasta",fechaHasta).getResultList();
		Iterator<Object[]> it = resutl.iterator();
		while (it.hasNext()){
			Object[] row = (Object[])it.next();
			SdmInformeViaticos informe = (SdmInformeViaticos)row[0];
			SdmLiquidacionInformeViaticos liquidacion = (SdmLiquidacionInformeViaticos)row[1];
			if (liquidacion != null && liquidacion.isAnulado() == false) informe.setSdmLiquidacionInformeViaticos(liquidacion);
			array.add(informe);
		}
		return array;
    }
    
    public List<SdmInformeViaticos>  buscarInformesXEmpresa(String codigoEmpresa){
		Query query =  entityManager.createQuery("From SdmInformeViaticos i  where i.sdmEmpresa.codigo=:codigo)");
		return (List<SdmInformeViaticos>)query.setParameter("codigo",codigoEmpresa).getResultList();
    }
    
    public List<SdmInformeViaticos>  buscarInformesXCentroCosto(String codigoCentroCosto){
		Query query =  entityManager.createQuery("From SdmInformeViaticos i  where i.sdmCentroCosto.codigo=:codigo)");
		return (List<SdmInformeViaticos>)query.setParameter("codigo",codigoCentroCosto).getResultList();
    }  
    
    public List<SdmInformeViaticos>  buscarInformesXDivicion(String codigoDivicion){
		Query query =  entityManager.createQuery("From SdmInformeViaticos i  where i.sdmDivicion.codigo=:codigo)");
		return (List<SdmInformeViaticos>)query.setParameter("codigo",codigoDivicion).getResultList();
    }
    
    public List<SdmInformeViaticos>  buscarInformesXDelegacion(String codigoDelegacion){
		Query query =  entityManager.createQuery("From SdmInformeViaticos i  where i.sdmDelegacion.codigo=:codigo)");
		return (List<SdmInformeViaticos>)query.setParameter("codigo",codigoDelegacion).getResultList();
    } 
}
