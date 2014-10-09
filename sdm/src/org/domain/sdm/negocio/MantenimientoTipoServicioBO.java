package org.domain.sdm.negocio;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.domain.sdm.entity.SdmTipoServicio;
import org.domain.sdm.session.SdmTipoServicioHome;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;


@Scope(ScopeType.SESSION)
@Name("mantenimientoTipoServicioBO")
public class MantenimientoTipoServicioBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7360835165849520535L;
	
	@Logger
	private Log log;
	
    @In 
    StatusMessages statusMessages;
    
    public SdmTipoServicio sdmTipoServicioNuevo = new SdmTipoServicio();
	
	@DataModel
	ArrayList<SdmTipoServicio> arraylistSdmTipoServicioEdit;

	@DataModelSelection("arraylistSdmTipoServicioEdit")
	SdmTipoServicio sdmTipoServicioSelect;
	
	@In(create=true)
	SdmTipoServicioHome sdmTipoServicioHome;
	
	@Factory("arraylistSdmTipoServicioEdit")
	public void obtenerListaSDMTipoServicio()	{
		arraylistSdmTipoServicioEdit = sdmTipoServicioHome.obtenerListaSDMTipoServicio();
	}

	
	public String activar(){
		sdmTipoServicioSelect = sdmTipoServicioHome.activar(sdmTipoServicioSelect);
		return "/tipoServicio.xhtml";
	}
	
	public String desactivar(){
		sdmTipoServicioSelect = sdmTipoServicioHome.desactivar(sdmTipoServicioSelect);
		return "/tipoServicio.xhtml";
	}
	
	public String crearTipoServicio(){
		sdmTipoServicioNuevo.setDescripcion(sdmTipoServicioNuevo.getDescripcion().trim());
		if (sdmTipoServicioNuevo.getDescripcion().isEmpty() == true) {
			statusMessages.add(Severity.ERROR , "Ingrese un nombre");
			return "/tipoServicio.xhtml";
		}
		sdmTipoServicioNuevo.setActivo(true);
		sdmTipoServicioHome.crear(sdmTipoServicioNuevo);
		obtenerListaSDMTipoServicio();
		this.sdmTipoServicioNuevo = new SdmTipoServicio();
		this.statusMessages.add(Severity.INFO,"Se creó el tipo de servicio ");
		return "/tipoServicio.xhtml";
	}


	public SdmTipoServicio getSdmTipoServicioNuevo() {
		return sdmTipoServicioNuevo;
	}


	public void setSdmTipoServicioNuevo(SdmTipoServicio sdmTipoServicioNuevo) {
		this.sdmTipoServicioNuevo = sdmTipoServicioNuevo;
	}


	public SdmTipoServicio getSdmTipoServicioSelect() {
		return sdmTipoServicioSelect;
	}


	public void setSdmTipoServicioSelect(SdmTipoServicio sdmTipoServicioSelect) {
		this.sdmTipoServicioSelect = sdmTipoServicioSelect;
	}



}
