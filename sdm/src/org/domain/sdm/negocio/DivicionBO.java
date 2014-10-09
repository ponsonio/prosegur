package org.domain.sdm.negocio;

import java.io.Serializable;
import java.util.ArrayList;

import org.domain.sdm.entity.SdmDivicion;
import org.domain.sdm.entity.SdmInformeViaticos;
import org.domain.sdm.session.SdmEmpleadoHome;
import org.domain.sdm.session.SdmDivicionHome;
import org.domain.sdm.session.SdmInformeViaticosHome;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.international.StatusMessages;

@Name("divicionBO")
@Scope(ScopeType.SESSION)
public class DivicionBO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 787937008071175854L;
	


    @In 
    StatusMessages statusMessages;
	
	
	String nombreDivicionNueva ; 
	
	String codigoDivicionNueva ;

	SdmDivicion sdmDivicionSelect = new SdmDivicion();
	
	@In(create=true)
	SdmDivicionHome sdmDivicionHome;
	
	@In(create=true)
	SdmInformeViaticosHome sdmInformeViaticosHome;
	
	@In(create=true)
	SdmEmpleadoHome sdmEmpleadoHome ;
	
	String tabSelect;
	
	public ArrayList<SdmDivicion> arrayListSdmDivicion() {
		return sdmDivicionHome.listarDiviciones();
	}
	
	public String selecionar(String codigoDivicion){
		this.sdmDivicionSelect = this.sdmDivicionHome.buscarSdmDivicionXCodigo(codigoDivicion);
		this.tabSelect="tabModificar";
		return "/division.xhtml";
	}
	
	public String  modificarDivicion(){
		if (this.sdmDivicionSelect.getCodigo() == null 
				|| this.sdmDivicionSelect.getCodigo().isEmpty()){
			statusMessages.add(Severity.INFO,"Seleccione una división primero ");
			return "/division.xhtml";
		}
		this.sdmDivicionSelect.setNombre(this.sdmDivicionSelect.getNombre().trim());
		
		if (this.sdmDivicionSelect.getNombre().isEmpty()){
			statusMessages.add(Severity.INFO,"Ingrese un nombre válido");
			return "/division.xhtml";
		}
		
		sdmDivicionHome.modificarDivicion(this.sdmDivicionSelect);
		statusMessages.add(Severity.INFO,"Se modificó la división ");
		this.sdmDivicionSelect = new SdmDivicion();
		return "/division.xhtml";
	}
	
	public String eliminarDivicion(){
		
		if (this.sdmDivicionSelect.getCodigo() == null ||
				this.sdmDivicionSelect.getCodigo().isEmpty()){
			statusMessages.add(Severity.INFO,"Seleccione una división primero ");
			return "/division.xhtml";
		}	
		if (sdmEmpleadoHome.buscarEmpleadosDivicion(this.sdmDivicionSelect.getCodigo()).size() > 0 ) {
			statusMessages.add(Severity.ERROR, "Existen empleados con dicha división asignada");
			return "/division.xhtml";
		}
		
		if (sdmInformeViaticosHome.buscarInformesXDivicion(this.sdmDivicionSelect.getCodigo()).size() > 0 ) {
			statusMessages.add(Severity.ERROR, "Existen liquidaciones con dicha división asignada");
			return "/division.xhtml";
		}
		sdmDivicionHome.eliminarDivicion(this.sdmDivicionSelect);
		statusMessages.add(Severity.INFO,"Se eliminó la división " + this.sdmDivicionSelect.getNombre());
		this.sdmDivicionSelect = new SdmDivicion();
		return "/division.xhtml";
	}
	
	public String crearDivicion(){
		
		this.codigoDivicionNueva = this.codigoDivicionNueva.trim().toUpperCase();
		
		if (this.codigoDivicionNueva.length() < 4){
			statusMessages.add(Severity.ERROR, "Ingrese un código válido");
			return "/division.xhtml";
		}
		
		this.nombreDivicionNueva = this.nombreDivicionNueva.trim();
		
		if (this.nombreDivicionNueva.isEmpty()){
			statusMessages.add(Severity.ERROR, "Ingrese un nombre válido");
			return "/division.xhtml";
		}
		
		if ( sdmDivicionHome.buscarSdmDivicionXCodigo(this.codigoDivicionNueva) != null){
			statusMessages.add(Severity.ERROR, "Ya existe una división con dicho código");
			return "/division.xhtml";
		}
		SdmDivicion sdmDivicion = new SdmDivicion(this.codigoDivicionNueva , this.nombreDivicionNueva);
		sdmDivicionHome.crearDivicion(sdmDivicion);
		statusMessages.add(Severity.INFO,"Se creó la nueva división");
		return "/division.xhtml";
	}


	

	public String getNombreDivicionNueva() {
		return nombreDivicionNueva;
	}

	public void setNombreDivicionNueva(String nombreDivicionNueva) {
		this.nombreDivicionNueva = nombreDivicionNueva;
	}

	public String getCodigoDivicionNueva() {
		return codigoDivicionNueva;
	}

	public void setCodigoDivicionNueva(String codigoDivicionNueva) {
		this.codigoDivicionNueva = codigoDivicionNueva;
	}

	public SdmDivicion getSdmDivicionSelect() {
		return sdmDivicionSelect;
	}

	public void setSdmDivicionSelect(SdmDivicion sdmDivicionSelect) {
		this.sdmDivicionSelect = sdmDivicionSelect;
	}

	public SdmDivicionHome getSdmDivicionHome() {
		return sdmDivicionHome;
	}

	public void setSdmDivicionHome(SdmDivicionHome sdmDivicionHome) {
		this.sdmDivicionHome = sdmDivicionHome;
	}

	public String getTabSelect() {
		return tabSelect;
	}

	public void setTabSelect(String tabSelect) {
		this.tabSelect = tabSelect;
	}
	
	
}
