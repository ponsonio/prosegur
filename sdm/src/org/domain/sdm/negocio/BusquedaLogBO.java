package org.domain.sdm.negocio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.domain.sdm.entity.SdmLog;
import org.domain.sdm.session.SdmLogHome;
import org.domain.sdm.session.SdmLogList;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.StatusMessages;


@Name("busquedaLogBO")
@Scope(ScopeType.SESSION)
public class BusquedaLogBO implements  Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5163265087147726000L;

	@In 
	StatusMessages statusMessages;

	@In(create= true)
	LoggerBO loggerBO ;
	
	@In(create = true)
	SdmLogHome sdmLogHome ;
	
	private Date fechaDesde = new Date();

	private Date fechaHasta = new Date();
	
	String usuarioBusqueda ; 
	
	String mensajeBusqueda  ;
	
	String referenciaBusqueda  ;
	
	String tipoEventoBusqueda  ; 
	
	String operacionBusqueda  ;
	
	String ipBusqueda  ;
	
	public List<SdmLog> getListLog() {
		return listLog;
	}



	public void setListLog(List<SdmLog> listLog) {
		this.listLog = listLog;
	}

	public List<SdmLog> listLog = new ArrayList<SdmLog>();
	
	
	public void trim(){
		if (tipoEventoBusqueda!=null) tipoEventoBusqueda =  tipoEventoBusqueda.trim() ;		
		if (usuarioBusqueda!=null) usuarioBusqueda = usuarioBusqueda.trim() ; 
		if (mensajeBusqueda!=null) mensajeBusqueda = mensajeBusqueda.trim();
		if (referenciaBusqueda!=null) referenciaBusqueda = referenciaBusqueda.trim() ;
		if (tipoEventoBusqueda!=null) tipoEventoBusqueda = tipoEventoBusqueda.trim(); 
		if (operacionBusqueda!=null) operacionBusqueda = operacionBusqueda.trim();
	}
	
	public String buscarLog() throws Exception{
		try{
			trim();
			String usrAux =   ( usuarioBusqueda.isEmpty())?   null : usuarioBusqueda  ;
			String mensajeAux =   ( mensajeBusqueda.isEmpty())?   null : mensajeBusqueda  ;
			String referenciaAux =   ( referenciaBusqueda.isEmpty())?   null : referenciaBusqueda  ;
			String operacionAux =   ( operacionBusqueda.isEmpty())?   null : operacionBusqueda  ;
			String stringTipoEventoAux = this.tipoEventoBusqueda ;
			
			if (stringTipoEventoAux.equals("Todos")) stringTipoEventoAux = null ;

					listLog = (ArrayList<SdmLog>)sdmLogHome.buscarLogFechas(this.fechaDesde , this.fechaHasta, usrAux
							, mensajeAux, referenciaAux, operacionAux,stringTipoEventoAux);
			
					
			//System.out.print("listLog.size()"+ listLog.size());
		}catch(Exception e){
			loggerBO.ingresarRegistroError(this.getClass().getCanonicalName(),
					e.getMessage(), "Busqueda sobre el log", null);
			throw e;
	
		}
		return "/busquedaLog.xhtml";
	}
	
	

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}



	public String getUsuarioBusqueda() {
		return usuarioBusqueda;
	}



	public void setUsuarioBusqueda(String usuarioBusqueda) {
		this.usuarioBusqueda = usuarioBusqueda;
	}



	public String getMensajeBusqueda() {
		return mensajeBusqueda;
	}



	public void setMensajeBusqueda(String mensajeBusqueda) {
		this.mensajeBusqueda = mensajeBusqueda;
	}



	public String getReferenciaBusqueda() {
		return referenciaBusqueda;
	}



	public void setReferenciaBusqueda(String referenciaBusqueda) {
		this.referenciaBusqueda = referenciaBusqueda;
	}



	public String getTipoEventoBusqueda() {
		return tipoEventoBusqueda;
	}



	public void setTipoEventoBusqueda(String tipoEventoBusqueda) {
		this.tipoEventoBusqueda = tipoEventoBusqueda;
	}



	public String getOperacionBusqueda() {
		return operacionBusqueda;
	}



	public void setOperacionBusqueda(String operacionBusqueda) {
		this.operacionBusqueda = operacionBusqueda;
	}



	public String getIpBusqueda() {
		return ipBusqueda;
	}



	public void setIpBusqueda(String ipBusqueda) {
		this.ipBusqueda = ipBusqueda;
	}
	
	
	
}
