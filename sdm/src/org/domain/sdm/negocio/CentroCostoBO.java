package org.domain.sdm.negocio;

import java.io.Serializable;
import java.util.ArrayList;

import org.domain.sdm.entity.SdmCentroCosto;
import org.domain.sdm.entity.SdmInformeViaticos;
import org.domain.sdm.session.SdmEmpleadoHome;
import org.domain.sdm.session.SdmCentroCostoHome;
import org.domain.sdm.session.SdmInformeViaticosHome;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.international.StatusMessages;

@Name("centroCostoBO")
@Scope(ScopeType.SESSION)
public class CentroCostoBO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 787937008071175854L;
	


    @In 
    StatusMessages statusMessages;
	
	
	String nombreCentroCostoNueva ; 
	
	String codigoCentroCostoNueva ;

	SdmCentroCosto sdmCentroCostoSelect = new SdmCentroCosto();
	
	@In(create=true)
	SdmCentroCostoHome sdmCentroCostoHome;
	
	@In(create=true)
	SdmInformeViaticosHome sdmInformeViaticosHome;
	
	@In(create=true)
	SdmEmpleadoHome sdmEmpleadoHome ;
	
	String tabSelect;
	
	public ArrayList<SdmCentroCosto> arrayListSdmCentroCosto() {
		return sdmCentroCostoHome.listarCentroCostos();
	}
	
	public String selecionar(String codigoCentroCosto){
		this.sdmCentroCostoSelect = this.sdmCentroCostoHome.buscarSdmCentroCostoXCodigo(codigoCentroCosto);
		this.tabSelect="tabModificar";
		return "/centroCosto.xhtml";
	}
	
	public String  modificarCentroCosto(){
		if (this.sdmCentroCostoSelect.getCodigo() == null 
				|| this.sdmCentroCostoSelect.getCodigo().isEmpty()){
			statusMessages.add(Severity.INFO,"Seleccione un centro de costo primero ");
			return "/centroCosto.xhtml";
		}
		this.sdmCentroCostoSelect.setNombre(this.sdmCentroCostoSelect.getNombre().trim());
			
		if (this.sdmCentroCostoSelect.getNombre().isEmpty()){
			statusMessages.add(Severity.INFO,"Ingrese un nombre valido");
			return "/centroCosto.xhtml";
		}

		sdmCentroCostoHome.modificarCentroCosto(this.sdmCentroCostoSelect);
		statusMessages.add(Severity.INFO,"Se modificó el centro de costo ");
		this.sdmCentroCostoSelect = new SdmCentroCosto();
		return "/centroCosto.xhtml";
	}
	
	public String eliminarCentroCosto(){
		
		if (this.sdmCentroCostoSelect.getCodigo() == null ||
				this.sdmCentroCostoSelect.getCodigo().isEmpty()){
			statusMessages.add(Severity.INFO,"Seleccione un centro costo primero ");
			return "/centroCosto.xhtml";
		}	
		if (sdmEmpleadoHome.buscarEmpleadosCentroCosto(this.sdmCentroCostoSelect.getCodigo()).size() > 0 ) {
			statusMessages.add(Severity.ERROR, "Existen empleados con dicho centro de costo asignado");
			return "/centroCosto.xhtml";
		}
		
		if (sdmInformeViaticosHome.buscarInformesXCentroCosto(this.sdmCentroCostoSelect.getCodigo()).size() > 0 ) {
			statusMessages.add(Severity.ERROR, "Existen liquidaciones con dicho centro de costo asignado");
			return "/centroCosto.xhtml";
		}
		sdmCentroCostoHome.eliminarCentroCosto(this.sdmCentroCostoSelect);
		statusMessages.add(Severity.INFO,"Se eliminó el centro de costo " + this.sdmCentroCostoSelect.getNombre());
		this.sdmCentroCostoSelect = new SdmCentroCosto();
		return "/centroCosto.xhtml";
	}
	
	public String crearCentroCosto(){
		this.codigoCentroCostoNueva = this.codigoCentroCostoNueva.trim().toUpperCase();
		
		if (this.codigoCentroCostoNueva.length() < 5){
			statusMessages.add(Severity.ERROR, "Ingrese un código válido");
			return "/centroCosto.xhtml";
		}
		
		this.nombreCentroCostoNueva = this.nombreCentroCostoNueva.trim();
		
		if (this.nombreCentroCostoNueva.isEmpty()){
			statusMessages.add(Severity.ERROR, "Ingrese un nombre válido");
			return "/centroCosto.xhtml";
		}
		
		if ( sdmCentroCostoHome.buscarSdmCentroCostoXCodigo(this.codigoCentroCostoNueva) != null){
			statusMessages.add(Severity.ERROR, "Ya existe un centro de costo con dicho código");
			return "/centroCosto.xhtml";
		}
		
		SdmCentroCosto sdmCentroCosto = new SdmCentroCosto(this.codigoCentroCostoNueva , this.nombreCentroCostoNueva);
		sdmCentroCostoHome.crearCentroCosto(sdmCentroCosto);
		statusMessages.add(Severity.INFO,"Se creó el centro de costo");
		return "/centroCosto.xhtml";
	}


	

	public String getNombreCentroCostoNueva() {
		return nombreCentroCostoNueva;
	}

	public void setNombreCentroCostoNueva(String nombreCentroCostoNueva) {
		this.nombreCentroCostoNueva = nombreCentroCostoNueva;
	}

	public String getCodigoCentroCostoNueva() {
		return codigoCentroCostoNueva;
	}

	public void setCodigoCentroCostoNueva(String codigoCentroCostoNueva) {
		this.codigoCentroCostoNueva = codigoCentroCostoNueva;
	}

	public SdmCentroCosto getSdmCentroCostoSelect() {
		return sdmCentroCostoSelect;
	}

	public void setSdmCentroCostoSelect(SdmCentroCosto sdmCentroCostoSelect) {
		this.sdmCentroCostoSelect = sdmCentroCostoSelect;
	}

	public SdmCentroCostoHome getSdmCentroCostoHome() {
		return sdmCentroCostoHome;
	}

	public void setSdmCentroCostoHome(SdmCentroCostoHome sdmCentroCostoHome) {
		this.sdmCentroCostoHome = sdmCentroCostoHome;
	}

	public String getTabSelect() {
		return tabSelect;
	}

	public void setTabSelect(String tabSelect) {
		this.tabSelect = tabSelect;
	}


	
	
	
	
	
}
