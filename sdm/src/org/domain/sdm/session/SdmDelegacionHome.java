package org.domain.sdm.session;

import org.domain.sdm.entity.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

@Name("sdmDelegacionHome")
public class SdmDelegacionHome extends EntityHome<SdmDelegacion> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3187677826218431006L;
	@In
	private EntityManager entityManager;
	
	@Logger private Log log;

	public void setSdmDelegacionCodigo(String id) {
		setId(id);
	}

	public String getSdmDelegacionCodigo() {
		return (String) getId();
	}

	@Override
	protected SdmDelegacion createInstance() {
		SdmDelegacion sdmDelegacion = new SdmDelegacion();
		return sdmDelegacion;
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

	public SdmDelegacion getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<SdmInformeViaticos> getSdmInformeViaticoses() {
		return getInstance() == null ? null
				: new ArrayList<SdmInformeViaticos>(getInstance()
						.getSdmInformeViaticoses());
	}

	public List<SdmInformeViaticoDetalle> getSdmInformeViaticoDetalles() {
		return getInstance() == null ? null
				: new ArrayList<SdmInformeViaticoDetalle>(getInstance()
						.getSdmInformeViaticoDetalles());
	}

	public List<SdmEmpleado> getSdmEmpleados() {
		return getInstance() == null ? null : new ArrayList<SdmEmpleado>(
				getInstance().getSdmEmpleados());
	}
	
	
	public ArrayList<SdmDelegacion> listarDelegaciones() {
		Query query =  entityManager.createQuery("select d from SdmDelegacion d ");
		return (ArrayList<SdmDelegacion>)query.getResultList();		
	}
	
	/**
	 * Retorna la divición con dicho código o nulo en caso no se encuentre
	 * @param codigo
	 * @return
	 */
	public SdmDelegacion buscarSdmDelegacionXCodigo(String codigo){
		try{
			Query query =  entityManager.createQuery("From SdmDelegacion d where d.codigo=:codigo)");
			return (SdmDelegacion)query.setParameter("codigo",codigo).getSingleResult();
		}catch(NoResultException nre){
			return null;
		}
	}
	
	public SdmDelegacion modificarDelegacion(SdmDelegacion sdmDelegacion){
		String nombre = sdmDelegacion.getNombre();
		sdmDelegacion = this.buscarSdmDelegacionXCodigo(sdmDelegacion.getCodigo());
		sdmDelegacion.setNombre(nombre);
		entityManager.persist(sdmDelegacion);
		return sdmDelegacion;
	}
	
	public SdmDelegacion crearDelegacion(SdmDelegacion sdmDelegacion){
		entityManager.persist(sdmDelegacion);
		return sdmDelegacion;
	}
	

	public void eliminarDelegacion(SdmDelegacion  sdmDelegacion){
		sdmDelegacion = this.buscarSdmDelegacionXCodigo(sdmDelegacion.getCodigo());
		entityManager.remove(sdmDelegacion);
	}
	
	public ArrayList<String> listaCodigos(){
		Query query =  entityManager.createQuery("select e.codigo from SdmDelegacion e ");
		return (ArrayList<String>)query.getResultList();	
	}

}
