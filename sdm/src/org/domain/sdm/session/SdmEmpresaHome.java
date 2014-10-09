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

@Name("sdmEmpresaHome")
public class SdmEmpresaHome extends EntityHome<SdmEmpresa> {

	@In
	private EntityManager entityManager;
	
	
	public void setSdmEmpresaCodigo(String id) {
		setId(id);
	}

	public String getSdmEmpresaCodigo() {
		return (String) getId();
	}

	@Override
	protected SdmEmpresa createInstance() {
		SdmEmpresa sdmEmpresa = new SdmEmpresa();
		return sdmEmpresa;
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

	public SdmEmpresa getDefinedInstance() {
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
	
	public ArrayList<SdmEmpresa> listarEmpresas() {
		Query query =  entityManager.createQuery("select e from SdmEmpresa e ");
		return (ArrayList<SdmEmpresa>)query.getResultList();		
	}
	
	/**
	 * Retorna la empresa con dicho código o nulo en caso no se encuentre
	 * @param codigo
	 * @return
	 */
	public SdmEmpresa buscarSdmEmpresaXCodigo(String codigo){
		try{
			Query query =  entityManager.createQuery("From SdmEmpresa e where e.codigo=:codigo)");
			return (SdmEmpresa)query.setParameter("codigo",codigo).getSingleResult();
		}catch(NoResultException nre){
			return null;
		}
	}
	
	public SdmEmpresa modificarEmpresa(SdmEmpresa sdmEmpresa){
		String nombre = sdmEmpresa.getNombre();
		sdmEmpresa = this.buscarSdmEmpresaXCodigo(sdmEmpresa.getCodigo());
		sdmEmpresa.setNombre(nombre);
		entityManager.persist(sdmEmpresa);
		return sdmEmpresa;
	}
	
	public SdmEmpresa crearEmpresa(SdmEmpresa sdmEmpresa){
		entityManager.persist(sdmEmpresa);
		return sdmEmpresa;
	}

	public void eliminarEmpresa(SdmEmpresa sdmEmpresa){
		sdmEmpresa = this.buscarSdmEmpresaXCodigo(sdmEmpresa.getCodigo());
		entityManager.remove(sdmEmpresa);
	}
	
	public ArrayList<String> listaCodigos(){
		Query query =  entityManager.createQuery("select e.codigo from SdmEmpresa e ");
		return (ArrayList<String>)query.getResultList();	
	}

}
