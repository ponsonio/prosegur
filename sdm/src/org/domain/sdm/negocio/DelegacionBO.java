package org.domain.sdm.negocio;

import java.io.Serializable;
import java.util.ArrayList;

import org.domain.sdm.entity.SdmDelegacion;
import org.domain.sdm.entity.SdmInformeViaticos;
import org.domain.sdm.session.SdmEmpleadoHome;
import org.domain.sdm.session.SdmDelegacionHome;
import org.domain.sdm.session.SdmInformeViaticosHome;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

@Name("delegacionBO")
@Scope(ScopeType.SESSION)
public class DelegacionBO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3047272031277780091L;

	@Logger private Log log;

	@In 
    StatusMessages statusMessages;
	
	
	String nombreDelegacionNueva ; 
	
	String codigoDelegacionNueva ;

	SdmDelegacion sdmDelegacionSelect = new SdmDelegacion();
	
	@In(create=true)
	SdmDelegacionHome sdmDelegacionHome;
	
	@In(create=true)
	SdmInformeViaticosHome sdmInformeViaticosHome;
	
	@In(create=true)
	SdmEmpleadoHome sdmEmpleadoHome ;
	
	String tabSelect;
	
	public ArrayList<SdmDelegacion> arrayListSdmDelegacion() {
		return sdmDelegacionHome.listarDelegaciones();
	}
	
	public String selecionar(String codigoDelegacion){
		this.sdmDelegacionSelect = this.sdmDelegacionHome.buscarSdmDelegacionXCodigo(codigoDelegacion);
		this.tabSelect="tabModificar";
		return "/delegacion.xhtml";
	}
	
	public String  modificarDelegacion(){
		if (this.sdmDelegacionSelect.getCodigo() == null 
				|| this.sdmDelegacionSelect.getCodigo().isEmpty()){
			statusMessages.add(Severity.INFO,"Seleccione una delegación primero ");
			return "/delegacion.xhtml";
		}
		this.sdmDelegacionSelect.setNombre(this.sdmDelegacionSelect.getNombre().trim());
		
		if (this.sdmDelegacionSelect.getNombre().isEmpty()){
			statusMessages.add(Severity.INFO,"Ingrese un nombre válido ");
			return "/delegacion.xhtml";
		}
		
		sdmDelegacionHome.modificarDelegacion(this.sdmDelegacionSelect);
		statusMessages.add(Severity.INFO,"Se modificó la delegación ");
		this.sdmDelegacionSelect = new SdmDelegacion();
		return "/delegacion.xhtml";
	}
	
	public String eliminarDelegacion(){
		
		if (this.sdmDelegacionSelect.getCodigo() == null ||
				this.sdmDelegacionSelect.getCodigo().isEmpty()){
			statusMessages.add(Severity.INFO,"Seleccione una delegación primero ");
			return "/delegacion.xhtml";
		}	
		if (sdmEmpleadoHome.buscarEmpleadosDelegacion(this.sdmDelegacionSelect.getCodigo()).size() > 0 ) {
			statusMessages.add(Severity.ERROR, "Existen empleados con dicha delegación asignada");
			return "/delegacion.xhtml";
		}
		
		if (sdmInformeViaticosHome.buscarInformesXDelegacion(this.sdmDelegacionSelect.getCodigo()).size() > 0 ) {
			statusMessages.add(Severity.ERROR, "Existen liquidaciones con dicha delegación asignada");
			return "/delegacion.xhtml";
		}
		sdmDelegacionHome.eliminarDelegacion(this.sdmDelegacionSelect);
		statusMessages.add(Severity.INFO,"Se eliminó la delegación " + this.sdmDelegacionSelect.getNombre());
		this.sdmDelegacionSelect = new SdmDelegacion();
		return "/delegacion.xhtml";
	}
	
	public String crearDelegacion(){

		this.codigoDelegacionNueva = this.codigoDelegacionNueva.trim().toUpperCase();
		
		if (this.codigoDelegacionNueva.length() < 5){
			statusMessages.add(Severity.ERROR, "Ingrese un código válido");
			return "/delegacion.xhtml";
		}

		
		this.nombreDelegacionNueva = this.nombreDelegacionNueva.trim();
		
		if (this.nombreDelegacionNueva.isEmpty()){
			statusMessages.add(Severity.ERROR, "Ingrese un nombre válido");
			return "/delegacion.xhtml";
		}
		
		this.nombreDelegacionNueva = this.nombreDelegacionNueva.trim();
		if (this.nombreDelegacionNueva.isEmpty()){
			statusMessages.add(Severity.ERROR, "Ingreso un nombre válido");
			return "/delegacion.xhtml";
		}
		
		if ( sdmDelegacionHome.buscarSdmDelegacionXCodigo(this.codigoDelegacionNueva) != null){
			statusMessages.add(Severity.ERROR, "Ya existe una delegación con dicho código");
			return "/delegacion.xhtml";
		}
		SdmDelegacion sdmDelegacion = new SdmDelegacion(this.codigoDelegacionNueva , this.nombreDelegacionNueva);
		sdmDelegacionHome.crearDelegacion(sdmDelegacion);
		statusMessages.add(Severity.INFO,"Se creó la nueva delegación");
		return "/delegacion.xhtml";
	}


	

	public String getNombreDelegacionNueva() {
		return nombreDelegacionNueva;
	}

	public void setNombreDelegacionNueva(String nombreDelegacionNueva) {
		this.nombreDelegacionNueva = nombreDelegacionNueva;
	}

	public String getCodigoDelegacionNueva() {
		return codigoDelegacionNueva;
	}

	public void setCodigoDelegacionNueva(String codigoDelegacionNueva) {
		this.codigoDelegacionNueva = codigoDelegacionNueva;
	}

	public SdmDelegacion getSdmDelegacionSelect() {
		return sdmDelegacionSelect;
	}

	public void setSdmDelegacionSelect(SdmDelegacion sdmDelegacionSelect) {
		this.sdmDelegacionSelect = sdmDelegacionSelect;
	}

	public SdmDelegacionHome getSdmDelegacionHome() {
		return sdmDelegacionHome;
	}

	public void setSdmDelegacionHome(SdmDelegacionHome sdmDelegacionHome) {
		this.sdmDelegacionHome = sdmDelegacionHome;
	}

	public String getTabSelect() {
		return tabSelect;
	}

	public void setTabSelect(String tabSelect) {
		this.tabSelect = tabSelect;
	}
	
}
