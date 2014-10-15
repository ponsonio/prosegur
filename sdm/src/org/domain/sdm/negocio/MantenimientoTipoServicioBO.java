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
	
	String stringCrearTipoServicio = "Crear Tipo Servicio";
	String stringActivarTipoServicio = "Activar Tipo Servicio";
	String stringDesactivarTipoServicio = "Desactivar Tipo Servicio";
	
	@In(create= true)
	LoggerBO loggerBO ;
	
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
	public void obtenerListaSDMTipoServicio()	throws Exception{
		try {
			arraylistSdmTipoServicioEdit = sdmTipoServicioHome.obtenerListaSDMTipoServicio();
		} catch (Exception e) {
			loggerBO.ingresarRegistroError(this.getClass().getCanonicalName(),
					e.getMessage(), "Listar Tipo Servicio ", null);
			throw e;
		}
	}

	/**
	 * Activa un tipo de servicio
	 * @return
	 * @throws Exception
	 */
	public String activar() throws Exception{
		try {
			sdmTipoServicioSelect = sdmTipoServicioHome.activar(sdmTipoServicioSelect);
			
			 loggerBO.ingresarRegistroEvento(this.getClass().getCanonicalName(), 
						"Se activó el tipo de servicio " , this.stringActivarTipoServicio , String.valueOf( sdmTipoServicioSelect.getId()));		
			 
			return "/tipoServicio.xhtml";
		} catch (Exception e) {
			loggerBO.ingresarRegistroError(this.getClass().getCanonicalName(),
					e.getMessage(), "Activar Tipo Servicio ", null);
			throw e;
		}
			
	}
	
	/**
	 * Desactivar tipo de servicio
	 * @return
	 * @throws Exception
	 */
	public String desactivar() throws Exception{
		try {
			sdmTipoServicioSelect = sdmTipoServicioHome.desactivar(sdmTipoServicioSelect);
			
			 loggerBO.ingresarRegistroEvento(this.getClass().getCanonicalName(), 
						"Se desactivó el tipo de servicio " , this.stringDesactivarTipoServicio , String.valueOf( sdmTipoServicioSelect.getId()));		
			return "/tipoServicio.xhtml";
		} catch (Exception e) {
			loggerBO.ingresarRegistroError(this.getClass().getCanonicalName(),
					e.getMessage(), "Desactivar Tipo Servicio ", null);
			throw e;
		}

	}
	
	/**
	 * Crear tipo de servicio
	 * @return
	 * @throws Exception
	 */
	public String crearTipoServicio() throws Exception{
		try {
			sdmTipoServicioNuevo.setDescripcion(sdmTipoServicioNuevo.getDescripcion().trim());
			if (sdmTipoServicioNuevo.getDescripcion().isEmpty() == true) {
				statusMessages.add(Severity.ERROR , "Ingrese un nombre");
				return "/tipoServicio.xhtml";
			}
			sdmTipoServicioNuevo.setActivo(true);
			sdmTipoServicioHome.crear(sdmTipoServicioNuevo);
			obtenerListaSDMTipoServicio();
	
			
			 loggerBO.ingresarRegistroEvento(this.getClass().getCanonicalName(), 
						"Se creó el tipo de servicio " , this.stringCrearTipoServicio , String.valueOf( sdmTipoServicioNuevo.getId()));		
	
			
			this.sdmTipoServicioNuevo = new SdmTipoServicio();
			this.statusMessages.add(Severity.INFO,"Se creó el tipo de servicio ");
				
			return "/tipoServicio.xhtml";
			
		} catch (Exception e) {
			loggerBO.ingresarRegistroError(this.getClass().getCanonicalName(),
					e.getMessage(), "Crear Tipo Servicio ", null);
			throw e;
		}
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
