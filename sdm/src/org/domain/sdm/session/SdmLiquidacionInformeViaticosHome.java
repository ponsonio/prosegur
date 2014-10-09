package org.domain.sdm.session;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.domain.sdm.entity.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("sdmLiquidacionInformeViaticosHome")
public class SdmLiquidacionInformeViaticosHome extends
		EntityHome<SdmLiquidacionInformeViaticos> {

	@In(create = true)
	SdmEmpleadoHome sdmEmpleadoHome;
	@In(create = true)
	SdmInformeViaticosHome sdmInformeViaticosHome;
	
	@In
	private EntityManager entityManager;

	public void setSdmLiquidacionInformeViaticosId(Long id) {
		setId(id);
	}

	public Long getSdmLiquidacionInformeViaticosId() {
		return (Long) getId();
	}

	@Override
	protected SdmLiquidacionInformeViaticos createInstance() {
		SdmLiquidacionInformeViaticos sdmLiquidacionInformeViaticos = new SdmLiquidacionInformeViaticos();
		return sdmLiquidacionInformeViaticos;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		SdmInformeViaticos sdmInformeViaticos = sdmInformeViaticosHome
				.getDefinedInstance();
		if (sdmInformeViaticos != null) {
			getInstance().setSdmInformeViaticos(sdmInformeViaticos);
		}
		SdmEmpleado sdmEmpleado = sdmEmpleadoHome.getDefinedInstance();
		if (sdmEmpleado != null) {
			getInstance().setSdmEmpleado(sdmEmpleado);
		}
	}

	public boolean isWired() {
		if (getInstance().getSdmInformeViaticos() == null)
			return false;
		if (getInstance().getSdmEmpleado() == null)
			return false;
		return true;
	}

	public SdmLiquidacionInformeViaticos getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	
    /**
     * Obtiene el informe de liquidacion valido para el informe o null si no existe
     * @param idInforme
     * @return SdmLiquidacionInformeViaticos ó null si no existe
     */
	public SdmLiquidacionInformeViaticos buscarLiquidacionInforme(long idInforme){
		try{
			Query query =  entityManager.createQuery("From SdmLiquidacionInformeViaticos i where i.sdmInformeViaticos.id= :idInforme and i.anulado=false)");
			return (SdmLiquidacionInformeViaticos)query.setParameter("idInforme",idInforme).getSingleResult();
		}catch (NoResultException nre){
			return null;
		}
    }	   

    public SdmLiquidacionInformeViaticos grabarInforme(SdmLiquidacionInformeViaticos sdmLiquidacionInformeViaticos){
    	 entityManager.persist(sdmLiquidacionInformeViaticos);
    	 return sdmLiquidacionInformeViaticos;
    }
}
