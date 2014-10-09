package org.domain.sdm.negocio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.domain.sdm.entity.SdmEmpleado;
import org.domain.sdm.entity.SdmLiquidacionInformeViaticos;
import org.domain.sdm.entity.SdmRol;
import org.domain.sdm.entity.SdmRolXUsuario;
import org.domain.sdm.entity.SdmTipoServicio;
import org.domain.sdm.entity.SdmUsuario;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;


@Name("asignacionRolesBO")
@Scope(ScopeType.SESSION)
public class AsignacionRolesBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4320533824412478306L;

	@Logger
	private Log log;
	
    @In 
    StatusMessages statusMessages;
    
    
	@In
	private EntityManager entityManager;
	
	public long[] longIdRolesSelecionados;
	

	ArrayList<SdmRol> arrayListSdmRolUsuarioSelect = new ArrayList<SdmRol>();
	

	SdmUsuario sdmUsuarioSelect = new SdmUsuario();
	
	//private SdmEmpleado sdmEmpleadoNuevoUsuario = new SdmEmpleado();
	
	private String tabSelect;
	
	private String nombreBuscar;
	
	@DataModel
	ArrayList<SdmEmpleado> arraylistSdmEmpleado;

	@DataModelSelection("arraylistSdmEmpleado")
	SdmEmpleado sdmEmpleadoBusquedaNombre;
	
	
	@Factory("arrayListUsuariosRol")
	public ArrayList<SdmUsuario>  obtenerUsuarios(){
			Query query =  entityManager.createQuery("From SdmUsuario s");
			return  (ArrayList<SdmUsuario>)query.getResultList();
	}
	
	
	@Factory("arrayListRol")
	public  ArrayList<SdmRol> obtenerRoles(){
			Query query =  entityManager.createQuery("From SdmRol s");
			return  (ArrayList<SdmRol>)query.getResultList();
	}





	public SdmUsuario getSdmUsuarioSelect() {
		return sdmUsuarioSelect;
	}


	public void setSdmUsuarioSelect(SdmUsuario sdmUsuarioSelect) {
		this.sdmUsuarioSelect = sdmUsuarioSelect;
	}

	/**
	 * Carga la información de roles y usuario del Usuario elegido
	 * @return
	 */
	public String cargarInfo(){
		arrayListSdmRolUsuarioSelect = obtenerRolXUsuario(this.sdmUsuarioSelect.getId());
		this.sdmUsuarioSelect = obtenerUsuarioXId(this.sdmUsuarioSelect.getId());
		return "/asignacionRoles.xhtml";
	}

	/**
	 * Obtiene los roles de un usuario y los muestra
	 * @param idUsuario id del usuario
	 * @return
	 */
	public ArrayList<SdmRol> obtenerRolXUsuario(long idUsuario){
		//log.info("id usuario"+idUsuario);
		ArrayList<SdmRol> arraSdmRols = new ArrayList<SdmRol>();
		Query query =  entityManager.createQuery("select u.sdmRolXUsuarios From  SdmUsuario u where u.id = :idUsuario");  
		ArrayList<SdmRolXUsuario>  rolxusuario = (ArrayList<SdmRolXUsuario>)query.setParameter("idUsuario", idUsuario).getResultList();
		Iterator<SdmRolXUsuario> it = rolxusuario.iterator();
		longIdRolesSelecionados = new long[rolxusuario.size()];
		int i = 0 ;
		while (it.hasNext()){
			SdmRol rol = it.next().getSdmRol();
			//arraSdmRols.add(rol);
			longIdRolesSelecionados[i] =  rol.getId();
			log.info("rol de usuario:" + rol.getEtiqueta());
			i++;
		}
		return arraSdmRols;
	}

	public boolean verificarRolUSuario(long idRol){
		Iterator<SdmRol> it = this.arrayListSdmRolUsuarioSelect.iterator();
		while (it.hasNext()){
			if ( idRol == it.next().getId()) return true;
		}
		return false;
	}
	
	public ArrayList<SdmRol> getArrayListSdmRolUsuarioSelect() {
		return arrayListSdmRolUsuarioSelect;
	}


	public void setArrayListSdmRolUsuarioSelect(
			ArrayList<SdmRol> arrayListSdmRolUsuarioSelect) {
		this.arrayListSdmRolUsuarioSelect = arrayListSdmRolUsuarioSelect;
	}



	/**
	 * Obtiene el usuario 
	 * @param idUsuario
	 * @return
	 */
	public SdmUsuario obtenerUsuarioXId(long idUsuario){
		Query query =  entityManager.createQuery("select u From  SdmUsuario u where u.id = :idUsuario");  
		return (SdmUsuario)query.setParameter("idUsuario",idUsuario).getSingleResult();
	}

	/**
	 * Carga los datos del empleado selecionado
	 * @return
	 */
	public String cargarEmpleadoSelect(){
		SdmUsuario usuarioAux = this.obtenerUsuarioxIdEmpleado(sdmEmpleadoBusquedaNombre.getId());
		
		if  (usuarioAux != null ) {
			statusMessages.add(Severity.ERROR,"El empleado ya tiene usuario");
			return "/asignacionRoles.xhtml";
		}
		this.sdmUsuarioSelect = new SdmUsuario();
		this.sdmUsuarioSelect.setSdmEmpleado(sdmEmpleadoBusquedaNombre);
		this.sdmUsuarioSelect.setActivo(true);
		this.arraylistSdmEmpleado = new  ArrayList<SdmEmpleado>();
		return "/asignacionRoles.xhtml";
	}
	
	public String getTabSelect() {
		return tabSelect;
	}


	public void setTabSelect(String tabSelect) {
		this.tabSelect = tabSelect;
	}
	
	/**
	 * Busqueda de empelados activos por nombre
	 * @return
	 */
	public String buscarSdmEmpleadoActivoPorNombre(){
   		String nombre = this.nombreBuscar;
   		Query query =  entityManager.createQuery("From SdmEmpleado e where e.activo = true and lower(e.nombre) like lower('%"+nombre+"%')");
   		arraylistSdmEmpleado = (ArrayList<SdmEmpleado>)query.getResultList();
   		this.tabSelect="tabNombre";
		return "/asignacionRoles.xhtml";
   }


	/**
	 * Obtiene el usuario con el id de empleado enviado
	 * @param idEmpleado
	 * @return
	 */
	public SdmUsuario  obtenerUsuarioxIdEmpleado(long idEmpleado){
		try{
	  		Query query =  entityManager.createQuery("From SdmUsuario u where u.sdmEmpleado.id = :idEmpleado");
	  		 return (SdmUsuario)query.setParameter("idEmpleado",idEmpleado).getSingleResult();
		}catch (NoResultException nre) {
			return null;
		}catch (NonUniqueResultException nue) {
			log.error(Severity.ERROR,"Más de un registro de usuario id:Empleado:"+ idEmpleado);
			throw nue;
		}

 	}


	public String getNombreBuscar() {
		return nombreBuscar;
	}


	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}
	
	/**
	 * Valida y graba los datos
	 * @return
	 */
	public String grabar(){
		
		if (sdmUsuarioSelect.getSdmEmpleado() == null ||
				sdmUsuarioSelect.getSdmEmpleado().getId() <= 0){
			statusMessages.add(Severity.ERROR , "Selecione un usuario o empleado primero");
			return "/asignacionRoles.xhtml";
		}
		
		sdmUsuarioSelect.setContrasena(sdmUsuarioSelect.getContrasena().trim());
		if (sdmUsuarioSelect.getContrasena().isEmpty()){
			statusMessages.add(Severity.ERROR , "La contraseña no es valida");
			return "/asignacionRoles.xhtml";
		}
		
		sdmUsuarioSelect.setCorreo(sdmUsuarioSelect.getCorreo().trim());
		if (sdmUsuarioSelect.getCorreo().isEmpty()){
			statusMessages.add(Severity.ERROR , "La contraseña no es valida");
			return "/asignacionRoles.xhtml";
		}
		

		
		Iterator<SdmRolXUsuario> iterator  = sdmUsuarioSelect.getSdmRolXUsuarios().iterator();
		
		//quito los que no estan selecionados
		while (iterator.hasNext()){
			SdmRolXUsuario sdmRolXUsuario = iterator.next();
			boolean selecionado = false;	
			for (int i = 0 ; i < longIdRolesSelecionados.length ; i ++){
				if (longIdRolesSelecionados[i] == sdmRolXUsuario.getSdmRol().getId()) 
						selecionado = true; 	
			}
			if (!selecionado) entityManager.remove(sdmRolXUsuario);
		}
		
		//creo los que no estan
		for (int i = 0 ; i < longIdRolesSelecionados.length ; i ++){
			
			Iterator<SdmRolXUsuario> iterator2  = sdmUsuarioSelect.getSdmRolXUsuarios().iterator();
			boolean asigando = false;
			while (iterator2.hasNext()){
				SdmRolXUsuario sdmRolXUsuario = iterator2.next();
				if (longIdRolesSelecionados[i] == sdmRolXUsuario.getSdmRol().getId())
					asigando = true;
			}
			if (!asigando){
				SdmRolXUsuario sdmRolXUsuario = new SdmRolXUsuario();
				SdmRol sdmrol = new SdmRol();
				sdmrol.setId(longIdRolesSelecionados[i]);
				sdmRolXUsuario.setSdmRol(sdmrol);
				sdmRolXUsuario.setSdmUsuario(sdmUsuarioSelect);
				entityManager.merge(sdmRolXUsuario);
			}
		}

		
		statusMessages.add(Severity.INFO , "Se guardaron los cambios");
		return "/asignacionRoles.xhtml";	
	}


	public long[] getLongIdRolesSelecionados() {
		return longIdRolesSelecionados;
	}


	public void setLongIdRolesSelecionados(long[] longIdRolesSelecionados) {
		this.longIdRolesSelecionados = longIdRolesSelecionados;
	}
	
	
	
	
}
