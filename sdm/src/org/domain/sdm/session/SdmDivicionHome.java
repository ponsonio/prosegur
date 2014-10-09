package org.domain.sdm.session;

import org.domain.sdm.entity.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("sdmDivicionHome")
public class SdmDivicionHome extends EntityHome<SdmDivicion> {

	@In
	private EntityManager entityManager;
	
	public void setSdmDivicionCodigo(String id) {
		setId(id);
	}

	public String getSdmDivicionCodigo() {
		return (String) getId();
	}

	@Override
	protected SdmDivicion createInstance() {
		SdmDivicion sdmDivicion = new SdmDivicion();
		return sdmDivicion;
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

	public SdmDivicion getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<SdmEmpleado> getSdmEmpleados() {
		return getInstance() == null ? null : new ArrayList<SdmEmpleado>(
				getInstance().getSdmEmpleados());
	}

	public List<SdmInformeViaticoDetalle> getSdmInformeViaticoDetalles() {
		return getInstance() == null ? null
				: new ArrayList<SdmInformeViaticoDetalle>(getInstance()
						.getSdmInformeViaticoDetalles());
	}

	public List<SdmInformeViaticos> getSdmInformeViaticoses() {
		return getInstance() == null ? null
				: new ArrayList<SdmInformeViaticos>(getInstance()
						.getSdmInformeViaticoses());
	}
	
	
	public ArrayList<SdmDivicion> listarDiviciones() {
		Query query =  entityManager.createQuery("select d from SdmDivicion d ");
		return (ArrayList<SdmDivicion>)query.getResultList();		
	}
	
	/**
	 * Retorna la divición con dicho código o nulo en caso no se encuentre
	 * @param codigo
	 * @return
	 */
	public SdmDivicion buscarSdmDivicionXCodigo(String codigo){
		try{
			Query query =  entityManager.createQuery("From SdmDivicion d where d.codigo=:codigo)");
			return (SdmDivicion)query.setParameter("codigo",codigo).getSingleResult();
		}catch(NoResultException nre){
			return null;
		}
	}
	
	public SdmDivicion modificarDivicion(SdmDivicion sdmDivicion){
		String nombre = sdmDivicion.getNombre();
		sdmDivicion = this.buscarSdmDivicionXCodigo(sdmDivicion.getCodigo());
		sdmDivicion.setNombre(nombre);
		entityManager.persist(sdmDivicion);
		return sdmDivicion;
	}
	
	public SdmDivicion crearDivicion(SdmDivicion sdmDivicion){
		entityManager.persist(sdmDivicion);
		return sdmDivicion;
	}
	

	public void eliminarDivicion(SdmDivicion  sdmDivicion){
		sdmDivicion = this.buscarSdmDivicionXCodigo(sdmDivicion.getCodigo());
		entityManager.remove(sdmDivicion);
	}

	public ArrayList<String> listaCodigos(){
		Query query =  entityManager.createQuery("select e.codigo from SdmDivicion e ");
		return (ArrayList<String>)query.getResultList();	
	}
}
