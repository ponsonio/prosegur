package org.domain.sdm.negocio;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.domain.sdm.entity.SdmEmpleado;
import org.domain.sdm.entity.SdmLiquidacionInformeViaticos;
import org.domain.sdm.entity.SdmLog;
import org.domain.sdm.entity.SdmUsuario;
import org.domain.sdm.session.SdmDivicionHome;
import org.domain.sdm.session.SdmLogHome;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.international.StatusMessage.Severity;

@Name("loggerBO")
@Scope(ScopeType.SESSION)
public class LoggerBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5089767333432157257L;
	
	public static String ERROR = "ERROR";
	
	public static String INFO = "INFO";
	
	public static String EVENTO = "EVENTO";
	
	public static String AVISO = "AVISO";
	

	@Logger
	private Log log;
	
	@In(create=true)
	SdmLogHome sdmLogHome;
	
	@In(create = true)
	SdmUsuario sdmUsuario;
	
	
	@In(create = true)
	SdmEmpleado sdmEmpleado;
	
	
	@In
	StatusMessages statusMessages;
	
	
	//@In("remoteAddr")  
	String remoteAddress;
	
	public String obtenerIp(){
		if (this.remoteAddress == null){
			ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
			HttpServletRequest request = (HttpServletRequest)context.getRequest();
			remoteAddress = request.getRemoteAddr();
		}
		return  remoteAddress;
	}

	
	public SdmLog ingresarLog(SdmLog sdmLog){
		try{
			sdmLog.setIp(this.obtenerIp());
			Date d = new Date();
			sdmLog.setFecha(new Timestamp(d.getTime()) );
			sdmLogHome.crear(sdmLog);
			//return sdmLog; 
		}catch(Exception e){
			log.error("Ocurrio un error al grabar el Log " + sdmLog.toString());
			statusMessages.add(Severity.ERROR,"Ocurrio un error al grabar el archivo de auditoría, por favor informar a Soporte");
			log.error(e.getLocalizedMessage());
		}
		return sdmLog;
	}
	
	public SdmLog ingresarLog(String nombreClase , String mensaje , String operacion , String referencia , String tipo){
		SdmLog sdmLog = new SdmLog();
		try{

			 
			sdmLog.setIp(this.obtenerIp());
			sdmLog.setNombreClase(nombreClase);
			sdmLog.setUsuario(sdmEmpleado.getCodigo());
			Date d = new Date();
			sdmLog.setFecha(new Timestamp(d.getTime()) );
			sdmLog.setIp(this.obtenerIp());
			sdmLog.setOperacion(operacion);
			sdmLog.setMensaje(mensaje);
			sdmLog.setReferencia(referencia);
			sdmLog.setTipo(tipo);
			sdmLogHome.crear(sdmLog);
		}catch(Exception e){
			log.error("Ocurrio un error al grabar el Log " + sdmLog.toString());
			log.error(e.getLocalizedMessage());
			statusMessages.add(Severity.ERROR,"Ocurrio un error al grabar el archivo de auditoría, por favor informar a Soporte");

		}
		return sdmLog;
	}
	
	public SdmLog ingresarLogUsr(String nombreClase , String mensaje , String operacion , String referencia , String tipo, String usr){
		SdmLog sdmLog = new SdmLog();
		try{
			sdmLog.setNombreClase(nombreClase);
			sdmLog.setIp(this.obtenerIp());
			sdmLog.setUsuario(usr);
			Date d = new Date();
			sdmLog.setFecha(new Timestamp(d.getTime()) );
			sdmLog.setIp(this.obtenerIp());
			sdmLog.setOperacion(operacion);
			sdmLog.setTipo(tipo);
			sdmLog.setMensaje(mensaje);
			sdmLog.setReferencia(referencia);
			sdmLogHome.crear(sdmLog);
		}catch(Exception e){
			log.error("Ocurrio un error al grabar el Log " + sdmLog.toString());
			log.error(e.getLocalizedMessage());
			statusMessages.add(Severity.ERROR,"Ocurrio un error al grabar el archivo de auditoría, por favor informar a Soporte");
			System.out.println("aca toy");
		}
		return sdmLog;
	}

	
	public void ingresarRegistroEvento(String nombreClase , String mensaje , String operacion , String referencia){
		this.ingresarLog(nombreClase, mensaje, operacion, referencia, LoggerBO.EVENTO);
	}

	public void ingresarRegistroError(String nombreClase , String mensaje , String operacion , String referencia){
		this.ingresarLog(nombreClase, mensaje, operacion, referencia, LoggerBO.ERROR);
	}
	
	public void ingresarRegistroAviso(String nombreClase , String mensaje , String operacion , String referencia){
		this.ingresarLog(nombreClase, mensaje, operacion, referencia, LoggerBO.AVISO);
	}
	
	public void ingresarRegistroInfo(String nombreClase , String mensaje , String operacion , String referencia){
		this.ingresarLog(nombreClase, mensaje, operacion, referencia, LoggerBO.INFO);
	}

	
	
}
