package org.domain.sdm.negocio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.domain.sdm.entity.SdmCentroCosto;
import org.domain.sdm.entity.SdmDelegacion;
import org.domain.sdm.entity.SdmDivicion;
import org.domain.sdm.entity.SdmEmpleado;
import org.domain.sdm.entity.SdmEmpresa;
import org.domain.sdm.entity.SdmRol;
import org.domain.sdm.entity.SdmRolXUsuario;
import org.domain.sdm.entity.SdmUsuario;
import org.domain.sdm.session.SdmCentroCostoHome;
import org.domain.sdm.session.SdmDelegacionHome;
import org.domain.sdm.session.SdmDivicionHome;
import org.domain.sdm.session.SdmEmpleadoHome;
import org.domain.sdm.session.SdmEmpresaHome;
import org.domain.sdm.session.SdmRolHome;
import org.domain.sdm.session.SdmUsuarioHome;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

@Name("empleadoBO")
@Scope(ScopeType.SESSION)
public class EmpleadoBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7276137792492138227L;

	
    @In 
    StatusMessages statusMessages;
    
    @In(create=true)
    SdmEmpleadoHome sdmEmpleadoHome;
    
    @In(create=true)
    SdmUsuarioHome sdmUsuarioHome;
    
    @In(create=true)
    SdmEmpresaHome sdmEmpresaHome;
    
    @In(create=true)
    SdmCentroCostoHome sdmCentroCostoHome;
    
    @In(create=true)
    SdmDelegacionHome sdmDelegacionHome;
    
    @In(create=true)
    SdmDivicionHome sdmDivicionHome;
    
    @In(create=true)
    SdmRolHome sdmRolHome;
	
    
    @Logger private Log log;
	
	SdmEmpleado sdmEmpleadoNuevo = new SdmEmpleado();
	
	SdmUsuario sdmUsuarioNuevo = new SdmUsuario();
	
	SdmEmpresa sdmEmpresa = new SdmEmpresa();
	
	SdmCentroCosto sdmCentroCosto = new SdmCentroCosto();
	
	SdmDelegacion sdmDelegacion = new SdmDelegacion();
	
	SdmDivicion sdmDivicion = new SdmDivicion();
	
	
	SdmEmpleado sdmEmpleadoSelect = null;
	
	SdmUsuario sdmUsuarioSelect = null;
	
	ArrayList<SdmRol> sdmRolesSelect = null;
	
	
	private String tabSelect;
	
	private String codigoBuscar ;
	
	private String nombreBuscar ;
	
	@DataModel
	ArrayList<SdmEmpleado> arraylistSdmEmpleadoBusqueda;

	@DataModelSelection("arraylistSdmEmpleadoBusqueda")
	SdmEmpleado sdmEmpleadoBusqueda;
	
	
	boolean empleadoNuevoUsuario = false;
	
	boolean empleadoSelectUsuario = false;
	
	public long[] longIdRolesSelecionados;
	
	public long[] longRolesUsuarioSelect;
	
	
	public  ArrayList<SdmRol> obtenerSdmRoles(){
		return  sdmRolHome.obtenerRoles();
	}


	public ArrayList<SdmEmpresa> obtenerSdmEmpresas(){
		return sdmEmpresaHome.listarEmpresas();
	}
	
	public ArrayList<SdmCentroCosto> obtenerSdmCentroCosto(){
		return sdmCentroCostoHome.listarCentroCostos();
	}

	public ArrayList<SdmDivicion> obtenerSdmDiviciones(){
		return sdmDivicionHome.listarDiviciones();
	}
	
	
	public ArrayList<SdmDelegacion> obtenerSdmDelegaciones(){
		return sdmDelegacionHome.listarDelegaciones();
	}
	

	public String incluirUsuarioNuevo(){
		if (this.empleadoNuevoUsuario) this.empleadoNuevoUsuario=false ; else this.empleadoNuevoUsuario=true;  
		return "/asignacionRoles.xhtml";
	}
	
	
	public String incluirUsuarioSelect(){
		if (this.empleadoSelectUsuario) this.empleadoSelectUsuario=false ; else this.empleadoSelectUsuario=true;
		if (this.sdmUsuarioSelect == null) this.sdmUsuarioSelect = new SdmUsuario();  
		return "/asignacionRoles.xhtml";
	}
	
	public boolean validarEmpleado(SdmEmpleado sdmEmpleado){
		boolean flag = true;
		//if (sdmEmpleado.getCodigo() == null) sdmEmpleado.setCodigo("");
		//if (sdmEmpleado.getNombre() == null) sdmEmpleado.setNombre("");
		sdmEmpleado.setCodigo(sdmEmpleado.getCodigo().trim().toUpperCase());
		sdmEmpleado.setNombre(sdmEmpleado.getNombre().trim().toUpperCase());
		if (sdmEmpleado.getCodigo().length() != 7 || sdmEmpleado.getCodigo().matches("[a-zA-Z]+")){ this.statusMessages.add(Severity.ERROR, "Código incorrecto :" +sdmEmpleado.getCodigo()) ; flag = false ;}
		if ( (sdmEmpleado.getNombre()==null) || sdmEmpleado.getNombre().isEmpty()) { this.statusMessages.add(Severity.ERROR,"Nombre incorrecto :"+sdmEmpleado.getNombre()) ; flag = false ;}
		if (sdmEmpleadoHome.buscarSdmEmpleadoXCodigo(sdmEmpleado.getCodigo()) != null ) { this.statusMessages.add(Severity.ERROR, "Ya existe un empleado con dicho código :" +sdmEmpleado.getCodigo()) ; flag = false ;}
		return flag;
	}
	
	
	
	public boolean validarEmpleadoModificar(SdmEmpleado sdmEmpleado){
		boolean flag = true;
		//if (sdmEmpleado.getCodigo() == null) sdmEmpleado.setCodigo("");
		//if (sdmEmpleado.getNombre() == null) sdmEmpleado.setNombre("");
		//sdmEmpleado.setCodigo(sdmEmpleado.getCodigo().trim().toUpperCase());
		sdmEmpleado.setNombre(sdmEmpleado.getNombre().trim().toUpperCase());
		//if (sdmEmpleado.getCodigo().length() != 7 || sdmEmpleado.getCodigo().matches("[a-zA-Z]+")){ this.statusMessages.add(Severity.ERROR, "Código incorrecto :" +sdmEmpleado.getCodigo()) ; flag = false ;}
		if ( (sdmEmpleado.getNombre()==null) || sdmEmpleado.getNombre().isEmpty()) { this.statusMessages.add(Severity.ERROR,"Nombre incorrecto :"+sdmEmpleado.getNombre()) ; flag = false ;}
		//if (sdmEmpleadoHome.buscarSdmEmpleadoXCodigo(sdmEmpleado.getCodigo()) != null ) { this.statusMessages.add(Severity.ERROR, "Ya existe un empleado con dicho código :" +sdmEmpleado.getCodigo()) ; flag = false ;}
		return flag;
	}
	
	public boolean validarUsuario(SdmUsuario sdmUsuario){
		boolean flag = true;
		sdmUsuario.setContrasena(sdmUsuario.getContrasena().trim());
		sdmUsuario.setCorreo(sdmUsuario.getCorreo().trim());
		
		if (sdmUsuario.getContrasena().isEmpty()) { this.statusMessages.add(Severity.ERROR,"Contraseña vacia :"+sdmUsuario.getContrasena()) ; flag = false ;}
		if (sdmUsuario.getCorreo().isEmpty()) { this.statusMessages.add(Severity.ERROR,"Correo vacio :"+sdmUsuario.getCorreo()) ; flag = false ;}

		return flag;
	}
	
	public String crearEmpleado(){
		ArrayList<SdmRol> arraSdmRols = null;
		if (this.validarEmpleado(this.sdmEmpleadoNuevo)){
			if (this.empleadoNuevoUsuario){
				if (this.validarUsuario(this.sdmUsuarioNuevo) == false)  return "/asignacionRoles.xhtml";
				this.sdmUsuarioNuevo.setActivo(true);
				arraSdmRols = new ArrayList<SdmRol>();
				for (int i = 0 ; i < longIdRolesSelecionados.length ; i ++){
					SdmRol sdmRol = new SdmRol();
					sdmRol.setId(longIdRolesSelecionados[i]);
					arraSdmRols.add(sdmRol);
				}
			}else{
				this.sdmUsuarioNuevo = null;
			}
			this.sdmEmpleadoNuevo.setSdmEmpresa(this.sdmEmpresa);
			this.sdmEmpleadoNuevo.setSdmCentroCosto(this.sdmCentroCosto);
			this.sdmEmpleadoNuevo.setSdmDelegacion(this.sdmDelegacion);
			this.sdmEmpleadoNuevo.setSdmDivicion(this.sdmDivicion);
			sdmEmpleadoHome.crearEmpleadoUsuario(this.sdmEmpleadoNuevo, this.sdmUsuarioNuevo,arraSdmRols);
			statusMessages.add(Severity.INFO,"El usuario se creó correctamente");
			this.sdmEmpleadoNuevo = new SdmEmpleado();
			this.sdmUsuarioNuevo = new SdmUsuario();
		}
		return "/asignacionRoles.xhtml";
	}
	
	
	public String seleccionarEmpleado(){
		this.sdmEmpleadoSelect = sdmEmpleadoBusqueda;
		this.sdmUsuarioSelect = sdmUsuarioHome.obtenerSdmUsuarioXCodigo(sdmEmpleadoSelect.getCodigo()) ;
		//Cargo los roles 
		if (this.sdmUsuarioSelect != null){
			this.empleadoSelectUsuario = true;
			Iterator<SdmRolXUsuario> it = sdmUsuarioSelect.getSdmRolXUsuarios().iterator();
			this.longRolesUsuarioSelect = new long[sdmUsuarioSelect.getSdmRolXUsuarios().size()];
			int i = 0;
			while (it.hasNext()){
				longRolesUsuarioSelect[i] = it.next().getId().getIdRol();
				i++;
			}
					
		}else{
			this.empleadoSelectUsuario = false;
			this.longRolesUsuarioSelect = null;
		}
		this.arraylistSdmEmpleadoBusqueda = new  ArrayList<SdmEmpleado>();
		return "/asignacionRoles.xhtml";
	}
	
	public String modificarEmpleado(){
		ArrayList<SdmRol> arraSdmRols = null;
		if (this.validarEmpleadoModificar(this.sdmEmpleadoSelect)){
			
			if (this.empleadoSelectUsuario) {
				
				if (this.validarUsuario(this.sdmUsuarioSelect) == false) return  "/asignacionRoles.xhtml";
				this.sdmUsuarioSelect.setActivo(true);
				arraSdmRols = new ArrayList<SdmRol>();
				for (int i = 0 ; i < longRolesUsuarioSelect.length ; i ++){
					SdmRol sdmRol = new SdmRol();
					sdmRol.setId(longRolesUsuarioSelect[i]);
					arraSdmRols.add(sdmRol);
				}
				
			}else{
				this.sdmUsuarioSelect = null;
				this.longRolesUsuarioSelect = null;
			}
			this.sdmEmpleadoHome.actualizarEmpleado(this.sdmEmpleadoSelect , this.sdmUsuarioSelect , arraSdmRols);
			statusMessages.add(Severity.INFO,"Se modificó el empleado correctamente");
		}
		//Si el flag es false no grabo el usuario
		return "/asignacionRoles.xhtml";
	}
	
	
	public String  buscarEmpleadoXCodigo(){
		this.sdmEmpleadoSelect = new SdmEmpleado();
		this.sdmUsuarioSelect = new SdmUsuario();
		this.arraylistSdmEmpleadoBusqueda = new  ArrayList<SdmEmpleado>();
		try {
			this.sdmEmpleadoSelect = sdmEmpleadoHome.buscarSdmEmpleadoXCodigo(this.codigoBuscar);
			this.sdmUsuarioSelect = sdmUsuarioHome.obtenerSdmUsuarioXCodigo(sdmEmpleadoSelect.getCodigo()) ;
			//Cargo los roles 
			if (this.sdmUsuarioSelect != null){
				this.empleadoSelectUsuario = true;
				Iterator<SdmRolXUsuario> it = sdmUsuarioSelect.getSdmRolXUsuarios().iterator();
				this.longRolesUsuarioSelect = new long[sdmUsuarioSelect.getSdmRolXUsuarios().size()];
				int i = 0;
				while (it.hasNext()){
					longRolesUsuarioSelect[i] = it.next().getId().getIdRol();
					i++;
				}
						
			}else{
				this.empleadoSelectUsuario = false;
				this.longRolesUsuarioSelect = null;
			}
			
		}catch (NoResultException nre) {
			log.info("Usuario no encontrado: "+this.codigoBuscar);
			statusMessages.add(Severity.ERROR,"No se encontró su usuario, por favor comunicarse con el área de soporte");
		}catch (NonUniqueResultException nue) {
			log.error(Severity.ERROR,"Más de un registro de usuario"+this.codigoBuscar);
			throw nue;
		}
		return "/asignacionRoles.xhtml";
	}
	
	
	public String  buscarEmpleadoXNombre(){
		arraylistSdmEmpleadoBusqueda = sdmEmpleadoHome.buscarEmpeladoXNombre(this.nombreBuscar) ;
		return "/asignacionRoles.xhtml";
	}
	

	
	public SdmEmpleado getSdmEmpleadoNuevo() {
		return sdmEmpleadoNuevo;
	}

	public void setSdmEmpleadoNuevo(SdmEmpleado sdmEmpleadoNuevo) {
		this.sdmEmpleadoNuevo = sdmEmpleadoNuevo;
	}




	public SdmEmpleado getSdmEmpleadoSelect() {
		return sdmEmpleadoSelect;
	}




	public void setSdmEmpleadoSelect(SdmEmpleado sdmEmpleadoSelect) {
		this.sdmEmpleadoSelect = sdmEmpleadoSelect;
	}


	public String getTabSelect() {
		return tabSelect;
	}


	public void setTabSelect(String tabSelect) {
		this.tabSelect = tabSelect;
	}


	public boolean isEmpleadoNuevoUsuario() {
		return empleadoNuevoUsuario;
	}


	public void setEmpleadoNuevoUsuario(boolean empleadoNuevoUsuario) {
		this.empleadoNuevoUsuario = empleadoNuevoUsuario;
	}


	public SdmUsuario getSdmUsuarioNuevo() {
		return sdmUsuarioNuevo;
	}


	public void setSdmUsuarioNuevo(SdmUsuario sdmUsuarioNuevo) {
		this.sdmUsuarioNuevo = sdmUsuarioNuevo;
	}


	public SdmEmpresa getSdmEmpresa() {
		return sdmEmpresa;
	}


	public void setSdmEmpresa(SdmEmpresa sdmEmpresa) {
		this.sdmEmpresa = sdmEmpresa;
	}


	public SdmCentroCosto getSdmCentroCosto() {
		return sdmCentroCosto;
	}


	public void setSdmCentroCosto(SdmCentroCosto sdmCentroCosto) {
		this.sdmCentroCosto = sdmCentroCosto;
	}


	public SdmDelegacion getSdmDelegacion() {
		return sdmDelegacion;
	}


	public void setSdmDelegacion(SdmDelegacion sdmDelegacion) {
		this.sdmDelegacion = sdmDelegacion;
	}


	public SdmDivicion getSdmDivicion() {
		return sdmDivicion;
	}


	public void setSdmDivicion(SdmDivicion sdmDivicion) {
		this.sdmDivicion = sdmDivicion;
	}


	public long[] getLongIdRolesSelecionados() {
		return longIdRolesSelecionados;
	}


	public void setLongIdRolesSelecionados(long[] longIdRolesSelecionados) {
		this.longIdRolesSelecionados = longIdRolesSelecionados;
	}


	public String getCodigoBuscar() {
		return codigoBuscar;
	}


	public void setCodigoBuscar(String codigoBuscar) {
		this.codigoBuscar = codigoBuscar;
	}


	public String getNombreBuscar() {
		return nombreBuscar;
	}


	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}


	public ArrayList<SdmEmpleado> getArraylistSdmEmpleadoBusqueda() {
		return arraylistSdmEmpleadoBusqueda;
	}


	public void setArraylistSdmEmpleadoBusqueda(
			ArrayList<SdmEmpleado> arraylistSdmEmpleadoBusqueda) {
		this.arraylistSdmEmpleadoBusqueda = arraylistSdmEmpleadoBusqueda;
	}


	public SdmEmpleado getSdmEmpleadoBusqueda() {
		return sdmEmpleadoBusqueda;
	}


	public void setSdmEmpleadoBusqueda(SdmEmpleado sdmEmpleadoBusqueda) {
		this.sdmEmpleadoBusqueda = sdmEmpleadoBusqueda;
	}


	public SdmUsuario getSdmUsuarioSelect() {
		return sdmUsuarioSelect;
	}


	public void setSdmUsuarioSelect(SdmUsuario sdmUsuarioSelect) {
		this.sdmUsuarioSelect = sdmUsuarioSelect;
	}


	public ArrayList<SdmRol> getSdmRolesSelect() {
		return sdmRolesSelect;
	}


	public void setSdmRolesSelect(ArrayList<SdmRol> sdmRolesSelect) {
		this.sdmRolesSelect = sdmRolesSelect;
	}


	public boolean isEmpleadoSelectUsuario() {
		return empleadoSelectUsuario;
	}


	public void setEmpleadoSelectUsuario(boolean empleadoSelectUsuario) {
		this.empleadoSelectUsuario = empleadoSelectUsuario;
	}


	public long[] getLongRolesUsuarioSelect() {
		return longRolesUsuarioSelect;
	}


	public void setLongRolesUsuarioSelect(long[] longRolesUsuarioSelect) {
		this.longRolesUsuarioSelect = longRolesUsuarioSelect;
	}
	
	
}
