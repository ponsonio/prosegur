package org.domain.sdm.session;

import org.domain.sdm.entity.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

@Name("sdmEmpleadoHome")
public class SdmEmpleadoHome extends EntityHome<SdmEmpleado> {

	@In
	private EntityManager entityManager;
	
	@In(create = true)
	SdmEmpresaHome sdmEmpresaHome;
	
	@In(create = true)
	SdmUsuarioHome sdmUsuarioHome;
	
	
	@In(create = true)
	SdmDivicionHome sdmDivicionHome;
	@In(create = true)
	SdmCentroCostoHome sdmCentroCostoHome;
	@In(create = true)
	SdmDelegacionHome sdmDelegacionHome;
	
    @Logger private Log log;

	public void setSdmEmpleadoId(Long id) {
		setId(id);
	}

	public Long getSdmEmpleadoId() {
		return (Long) getId();
	}

	@Override
	protected SdmEmpleado createInstance() {
		SdmEmpleado sdmEmpleado = new SdmEmpleado();
		return sdmEmpleado;
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
		if (getInstance().getSdmDivicion() == null)
			return false;
		if (getInstance().getSdmCentroCosto() == null)
			return false;
		if (getInstance().getSdmDelegacion() == null)
			return false;
		return true;
	}

	public SdmEmpleado getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	
	public List<SdmUsuario> getSdmUsuarios() {
		return getInstance() == null ? null : new ArrayList<SdmUsuario>(
				getInstance().getSdmUsuarios());
	}

	public List<SdmInformeViaticos> getSdmInformeViaticoses() {
		return getInstance() == null ? null
				: new ArrayList<SdmInformeViaticos>(getInstance()
						.getSdmInformeViaticoses());
	}

	public List<SdmLiquidacionInformeViaticos> getSdmLiquidacionInformeViaticoses() {
		return getInstance() == null ? null
				: new ArrayList<SdmLiquidacionInformeViaticos>(getInstance()
						.getSdmLiquidacionInformeViaticoses());
	}

	public List<SdmInformeViaticoDetalle> getSdmInformeViaticoDetalles() {
		return getInstance() == null ? null
				: new ArrayList<SdmInformeViaticoDetalle>(getInstance()
						.getSdmInformeViaticoDetalles());
	}
	
	
	/**
	 * Retorna los empleados cuyo nombre tenga la cadena enviada
	 * @param nombre 
	 * @return
	 */
	public ArrayList<SdmEmpleado> buscarEmpeladoActivoXNombre(String nombre){
		Query query =  entityManager.createQuery("From SdmEmpleado e where e.activo = true and lower(e.nombre) like lower('%"+nombre+"%')");
		return (ArrayList<SdmEmpleado>)query.getResultList();
	}

	
	/**
	 * Retorna los empleados cuyo nombre tenga la cadena enviada
	 * @param nombre 
	 * @return
	 */
	public ArrayList<SdmEmpleado> buscarEmpeladoXNombre(String nombre){
		Query query =  entityManager.createQuery("From SdmEmpleado e where lower(e.nombre) like lower('%"+nombre+"%')");
		return (ArrayList<SdmEmpleado>)query.getResultList();
	}
	/**
	 * Retorna el empleado cuyo código es enviado como parámetro
	 * @param codigo
	 * @return
	 * @exception NonUniqueResultException , NoResultException nre 
	 */
    public SdmEmpleado  buscarSdmEmpleadoActivoPorCodigo(String codigo){
		Query query =  entityManager.createQuery("From SdmEmpleado e where e.activo = true and e.codigo = :codigo");
		return (SdmEmpleado)query.setParameter("codigo",codigo).getSingleResult();
    }
    
	/**
	 * Retorna el empleado cuyo código es enviado como parámetro
	 * @param codigo
	 * @return null, si no lo encuentra
	 * @exception NonUniqueResultException , NoResultException nre 
	 */
    public SdmEmpleado  buscarSdmEmpleadoXCodigo(String codigo){
    	try {
    		Query query =  entityManager.createQuery("From SdmEmpleado e where e.codigo = :codigo");
    		return (SdmEmpleado)query.setParameter("codigo",codigo).getSingleResult();
    	}catch (NoResultException nre ){
    		return null;
    	}
    }
    
    /**
     * Retorna todos los empleados que tienen el rol de Emisor
     * @return
     */
    public List<SdmEmpleado> listaEmpleadosEmisores(){
		Query query =  entityManager.createQuery("select u.sdmEmpleado from SdmRolXUsuario rxu , SdmUsuario u where rxu.sdmRol.etiqueta = 'Emisor' and u.id = rxu.sdmUsuario.id order by u.sdmEmpleado.nombre");
		return (List<SdmEmpleado>)query.getResultList();
    }
    
    
    public List<SdmEmpleado> buscarEmpleadosEmpresa(String codigoEmpresa){
		Query query =  entityManager.createQuery("select e from SdmEmpleado e where e.sdmEmpresa.codigo=:codigo ");
		return (List<SdmEmpleado>)query.setParameter("codigo",codigoEmpresa).getResultList();
    }
    
    
    public List<SdmEmpleado> buscarEmpleadosDivicion(String codigoDivicion){
		Query query =  entityManager.createQuery("select e from SdmEmpleado e where e.sdmDivicion.codigo=:codigo ");
		return (List<SdmEmpleado>)query.setParameter("codigo",codigoDivicion).getResultList();
    }
    
    
    
    public List<SdmEmpleado> buscarEmpleadosDelegacion(String codigoDelegacion){
		Query query =  entityManager.createQuery("select e from SdmEmpleado e where e.sdmDelegacion.codigo=:codigo ");
		return (List<SdmEmpleado>)query.setParameter("codigo",codigoDelegacion).getResultList();
    }

    public List<SdmEmpleado> buscarEmpleadosCentroCosto(String codigoCentroCosto){
		Query query =  entityManager.createQuery("select e from SdmEmpleado e where e.sdmCentroCosto.codigo=:codigo ");
		return (List<SdmEmpleado>)query.setParameter("codigo",codigoCentroCosto).getResultList();
    }
    
    public SdmEmpleado crearEmpleado(SdmEmpleado e){
    	entityManager.persist(e);
    	return e;
    }
    
    
    @Transactional
    public SdmEmpleado crearEmpleadoUsuario(SdmEmpleado e, SdmUsuario u , ArrayList<SdmRol> arraSdmRols){
    	entityManager.persist(e);
    	
 
    	if (u != null) { u.setSdmEmpleado(e);  entityManager.persist(u) ;}
    	
    	if (u != null && arraSdmRols != null){
	    	Iterator<SdmRol> it =  arraSdmRols.iterator();
	    	while (it.hasNext()){
	    		SdmRol rol = it.next();
	    		SdmRolXUsuarioId id = new SdmRolXUsuarioId();
	    		id.setIdRol(rol.getId());
	    		id.setIdUsuario(u.getId());
	    		SdmRolXUsuario rolxu = new SdmRolXUsuario(id, u ,rol);
	    		entityManager.persist(rolxu);
	    	}
    	}	
    	return e;
    }
    
    public SdmEmpleado actualizarEmpleado(SdmEmpleado e){
    	SdmEmpleado empAux = this.buscarSdmEmpleadoXCodigo(e.getCodigo());
    	empAux.setExterno(e.isExterno());
    	empAux.setActivo(e.isActivo());
    	empAux.setNombre(e.getNombre());
    	empAux.setSdmCentroCosto(e.getSdmCentroCosto());
    	empAux.setSdmDelegacion(e.getSdmDelegacion());
    	empAux.setSdmDivicion(e.getSdmDivicion());
    	empAux.setSdmEmpresa(e.getSdmEmpresa());
    	entityManager.persist(empAux);
    	return empAux;
    }
    
    
    @Transactional
    public SdmEmpleado actualizarEmpleado(SdmEmpleado e , SdmUsuario u , ArrayList<SdmRol> arraSdmRols){

    	
    	
    	SdmEmpleado empAux = this.buscarSdmEmpleadoXCodigo(e.getCodigo());
    	SdmUsuario usrAux = this.sdmUsuarioHome.obtenerSdmUsuarioXCodigo(empAux.getCodigo());
    	
    	empAux.setCodigo(e.getCodigo());
    	empAux.setExterno(e.isExterno());
    	empAux.setActivo(e.isActivo());
    	empAux.setNombre(e.getNombre());
    	empAux.setSdmCentroCosto(e.getSdmCentroCosto());
    	empAux.setSdmDelegacion(e.getSdmDelegacion());
    	empAux.setSdmDivicion(e.getSdmDivicion());
    	empAux.setSdmEmpresa(e.getSdmEmpresa());
    	entityManager.persist(empAux);
    	
    	//Si no tiene usuario y hay que crear uno
    	
    	if(u != null && usrAux == null) {
    		SdmUsuario u2 = new SdmUsuario();
    		u2.setActivo(u.isActivo());
    		u2.setContrasena(u.getContrasena());
    		u2.setCorreo(u.getCorreo());
    		u2.setSdmEmpleado(empAux);
    		u2.setFechaModContrasena(u.getFechaModContrasena());
    		entityManager.persist(u2);
    		u = u2;
    	}
    	

    	//Elimino los roles que tiene
    	if (usrAux !=  null) {
    		Iterator<SdmRolXUsuario> rxue = usrAux.getSdmRolXUsuarios().iterator();
    		while (rxue.hasNext()){
    			entityManager.remove(rxue.next());
    		}
    		if (u == null) {
        		log.error("remove usrAux pq mando null");
        		entityManager.remove(usrAux);
    		}
    	}
    	
    	
    	//Grabo los roles
    	if (u != null && arraSdmRols != null){
	    	Iterator<SdmRol> it =  arraSdmRols.iterator();
	    	while (it.hasNext()){
	    		SdmRol rol = it.next();
	    		SdmRolXUsuarioId id = new SdmRolXUsuarioId();
	    		id.setIdRol(rol.getId());
	    		id.setIdUsuario(u.getId());
	    		SdmRolXUsuario rolxu = new SdmRolXUsuario(id, u ,rol);
	    		entityManager.persist(rolxu);
	    	}
    	}
    	return e;
    }
    
    
}
