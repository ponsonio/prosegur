package org.domain.sdm.session;

import org.domain.sdm.entity.*;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("sdmTipoServicioHome")
public class SdmTipoServicioHome extends EntityHome<SdmTipoServicio> {
	
	
	@In
	private EntityManager entityManager;

	public void setSdmTipoServicioId(Long id) {
		setId(id);
	}

	public Long getSdmTipoServicioId() {
		return (Long) getId();
	}

	@Override
	protected SdmTipoServicio createInstance() {
		SdmTipoServicio sdmTipoServicio = new SdmTipoServicio();
		return sdmTipoServicio;
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

	public SdmTipoServicio getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<SdmInformeViaticoDetalle> getSdmInformeViaticoDetalles() {
		return getInstance() == null ? null
				: new ArrayList<SdmInformeViaticoDetalle>(getInstance()
						.getSdmInformeViaticoDetalles());
	}
	
	
	
	/***
	 * Retorna los tipos de servicios activos
	 * @return
	 */
    @SuppressWarnings("unchecked")
	public ArrayList<SdmTipoServicio>  obtenerListaSdmTipoServicioActivo(){
		Query query =  entityManager.createQuery("From SdmTipoServicio t where t.activo = :activo");
		return (ArrayList<SdmTipoServicio> )query.setParameter("activo",true).getResultList();
    } 
    
    /**
     * Obtiene el tipo de servicio por Id, asi no este activo
     * @param id
     * @return
	 * @exception NonUniqueResultException , NoResultException nre 
     */
    public SdmTipoServicio getSdmTipoServicioxId(long id){
		Query query =  entityManager.createQuery("From SdmTipoServicio t where t.id = :id");
		return (SdmTipoServicio)query.setParameter("id",id).getSingleResult();
    }

    /**
     * Retorna todos los tipos de servicios (áctivos o no)
     * @return
     */
    public ArrayList<SdmTipoServicio> obtenerListaSDMTipoServicio()	{
		Query query =  entityManager.createQuery("From SdmTipoServicio t ");
		return (ArrayList<SdmTipoServicio>)query.getResultList();
	}
    /**
     * Activa el Tipo de Servicio enviado
     * @param sdmTipoServicio
     * @return
     */
    public SdmTipoServicio activar(SdmTipoServicio sdmTipoServicio){
    	sdmTipoServicio.setActivo(true);
		entityManager.merge(sdmTipoServicio);
		return sdmTipoServicio;
    }
    
    /**
     * Desactiva el Tipo de Servicio enviado
     * @param sdmTipoServicio
     * @return
     */
    public SdmTipoServicio desactivar(SdmTipoServicio sdmTipoServicio){
    	sdmTipoServicio.setActivo(false);
		entityManager.merge(sdmTipoServicio);
		return sdmTipoServicio;
    }
    
    public SdmTipoServicio crear(SdmTipoServicio sdmTipoServicio){
		entityManager.persist(sdmTipoServicio);
    	return sdmTipoServicio;
    }
}
