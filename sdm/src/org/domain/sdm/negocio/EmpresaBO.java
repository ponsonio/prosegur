package org.domain.sdm.negocio;

import java.io.Serializable;
import java.util.ArrayList;

import org.domain.sdm.entity.SdmEmpresa;
import org.domain.sdm.session.SdmEmpleadoHome;
import org.domain.sdm.session.SdmEmpresaHome;
import org.domain.sdm.session.SdmInformeViaticosHome;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;


@Name("empresaBO")
@Scope(ScopeType.SESSION)
public class EmpresaBO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 787937008071175854L;
	
	@Logger private Log log;

    @In 
    StatusMessages statusMessages;
	
	
	String nombreEmpresaNueva ; 
	
	String codigoEmpresaNueva ;

	SdmEmpresa sdmEmpresaSelect = new SdmEmpresa();
	
	@In(create=true)
	SdmEmpresaHome sdmEmpresaHome;
	
	@In(create=true)
	SdmInformeViaticosHome sdmInformeViaticosHome;
	
	@In(create=true)
	SdmEmpleadoHome sdmEmpleadoHome ;
	
	String tabSelect;
	
	public ArrayList<SdmEmpresa> arrayListSdmEmpresa() {
		return sdmEmpresaHome.listarEmpresas();
	}
	
	public String selecionar(String codigoEmpresa){
		this.sdmEmpresaSelect = this.sdmEmpresaHome.buscarSdmEmpresaXCodigo(codigoEmpresa);
		this.tabSelect="tabModificar";
		return "/empresa.xhtml";
	}
	
	public String  modificarEmpresa(){
		if (this.sdmEmpresaSelect.getCodigo() == null 
				|| this.sdmEmpresaSelect.getCodigo().isEmpty()){
			statusMessages.add(Severity.INFO,"Seleccione una empresa primero ");
			return "/empresa.xhtml";
		}
		this.sdmEmpresaSelect.setNombre(this.sdmEmpresaSelect.getNombre().trim());
		if (this.sdmEmpresaSelect.getNombre().isEmpty()){
			statusMessages.add(Severity.INFO,"Ingrese nombre valido");
			return "/empresa.xhtml";
		}
		
		sdmEmpresaHome.modificarEmpresa(this.sdmEmpresaSelect);
		statusMessages.add(Severity.INFO,"Se modificó la empresa ");
		this.sdmEmpresaSelect = new SdmEmpresa();
		return "/empresa.xhtml";
	}
	
	public String eliminarEmpresa(){
		
		if (this.sdmEmpresaSelect.getCodigo() == null ||
				this.sdmEmpresaSelect.getCodigo().isEmpty()){
			statusMessages.add(Severity.INFO,"Seleccione una empresa primero ");
			return "/empresa.xhtml";
		}	
		if (sdmEmpleadoHome.buscarEmpleadosEmpresa(this.sdmEmpresaSelect.getCodigo()).size() > 0 ) {
			statusMessages.add(Severity.ERROR, "Existen empleados con dicha empresa asignada");
			return "/empresa.xhtml";
		}
		
		if (sdmInformeViaticosHome.buscarInformesXEmpresa(this.sdmEmpresaSelect.getCodigo()).size() > 0 ) {
			statusMessages.add(Severity.ERROR, "Existen liquidaciones con dicha empresa asignada");
			return "/empresa.xhtml";
		}
		sdmEmpresaHome.eliminarEmpresa(this.sdmEmpresaSelect);
		statusMessages.add(Severity.INFO,"Se eliminó la empresa " + this.sdmEmpresaSelect.getNombre());
		
		this.sdmEmpresaSelect = new SdmEmpresa();
		return "/empresa.xhtml";
	}
	
	public String crearEmpresa(){

		this.codigoEmpresaNueva = this.codigoEmpresaNueva.trim().toUpperCase();
		
		if (this.codigoEmpresaNueva.length() < 3){
			statusMessages.add(Severity.ERROR, "Ingrese un código  válido");
			return "/empresa.xhtml";
		}
		
		this.nombreEmpresaNueva = this.nombreEmpresaNueva.trim();
		
		if (this.nombreEmpresaNueva.isEmpty()){
			statusMessages.add(Severity.ERROR, "Ingreso un nombre  válido");
			return "/empresa.xhtml";
		}
		
		if ( sdmEmpresaHome.buscarSdmEmpresaXCodigo(this.codigoEmpresaNueva) != null){
			statusMessages.add(Severity.ERROR, "Ya existe una empresa con dicho código");
			return "/empresa.xhtml";
		}
		SdmEmpresa sdmEmpresa = new SdmEmpresa(this.codigoEmpresaNueva , this.nombreEmpresaNueva);
		sdmEmpresaHome.crearEmpresa(sdmEmpresa);
		statusMessages.add(Severity.INFO,"Se creó la nueva empresa");
		return "/empresa.xhtml";
	}


	

	public String getNombreEmpresaNueva() {
		return nombreEmpresaNueva;
	}

	public void setNombreEmpresaNueva(String nombreEmpresaNueva) {
		this.nombreEmpresaNueva = nombreEmpresaNueva;
	}

	public String getCodigoEmpresaNueva() {
		return codigoEmpresaNueva;
	}

	public void setCodigoEmpresaNueva(String codigoEmpresaNueva) {
		this.codigoEmpresaNueva = codigoEmpresaNueva;
	}

	public SdmEmpresa getSdmEmpresaSelect() {
		return sdmEmpresaSelect;
	}

	public void setSdmEmpresaSelect(SdmEmpresa sdmEmpresaSelect) {
		this.sdmEmpresaSelect = sdmEmpresaSelect;
	}

	public SdmEmpresaHome getSdmEmpresaHome() {
		return sdmEmpresaHome;
	}

	public void setSdmEmpresaHome(SdmEmpresaHome sdmEmpresaHome) {
		this.sdmEmpresaHome = sdmEmpresaHome;
	}

	public String getTabSelect() {
		return tabSelect;
	}

	public void setTabSelect(String tabSelect) {
		this.tabSelect = tabSelect;
	}


	
	
	
	
	
}
