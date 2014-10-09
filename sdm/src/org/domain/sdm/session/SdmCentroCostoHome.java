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

@Name("sdmCentroCostoHome")
public class SdmCentroCostoHome extends EntityHome<SdmCentroCosto> {

	@In
	private EntityManager entityManager;
	
	public void setSdmCentroCostoCodigo(String id) {
		setId(id);
	}

	public String getSdmCentroCostoCodigo() {
		return (String) getId();
	}

	@Override
	protected SdmCentroCosto createInstance() {
		SdmCentroCosto sdmCentroCosto = new SdmCentroCosto();
		return sdmCentroCosto;
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

	public SdmCentroCosto getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<SdmEmpleado> getSdmEmpleados() {
		return getInstance() == null ? null : new ArrayList<SdmEmpleado>(
				getInstance().getSdmEmpleados());
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
	
	
	public ArrayList<SdmCentroCosto> listarCentroCostos() {
		Query query =  entityManager.createQuery("select d from SdmCentroCosto d ");
		return (ArrayList<SdmCentroCosto>)query.getResultList();		
	}
	
	/**
	 * Retorna la divición con dicho código o nulo en caso no se encuentre
	 * @param codigo
	 * @return
	 */
	public SdmCentroCosto buscarSdmCentroCostoXCodigo(String codigo){
		try{
			Query query =  entityManager.createQuery("From SdmCentroCosto d where d.codigo=:codigo)");
			return (SdmCentroCosto)query.setParameter("codigo",codigo).getSingleResult();
		}catch(NoResultException nre){
			return null;
		}
	}
	
	public ArrayList<String> listaCodigos(){
		Query query =  entityManager.createQuery("select e.codigo from SdmCentroCosto e ");
		return (ArrayList<String>)query.getResultList();	
	}
	
	public SdmCentroCosto modificarCentroCosto(SdmCentroCosto sdmCentroCosto){
		String nombre = sdmCentroCosto.getNombre();
		sdmCentroCosto = this.buscarSdmCentroCostoXCodigo(sdmCentroCosto.getCodigo());
		sdmCentroCosto.setNombre(nombre);
		entityManager.persist(sdmCentroCosto);
		return sdmCentroCosto;
	}
	
	public SdmCentroCosto crearCentroCosto(SdmCentroCosto sdmCentroCosto){
		entityManager.persist(sdmCentroCosto);
		return sdmCentroCosto;
	}
	
	public void eliminarCentroCosto(SdmCentroCosto  sdmCentroCosto){
		sdmCentroCosto = this.buscarSdmCentroCostoXCodigo(sdmCentroCosto.getCodigo());
		entityManager.remove(sdmCentroCosto);
	}

}
