package org.domain.sdm.negocio;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.domain.sdm.entity.SdmLiquidacionInformeViaticos;
import org.domain.sdm.entity.SdmLog;
import org.domain.sdm.session.SdmDivicionHome;
import org.domain.sdm.session.SdmLogHome;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("loggerBO")
@Scope(ScopeType.SESSION)
public class LoggerBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5089767333432157257L;

	@In(create=true)
	SdmLogHome sdmLogHome;
	
	//@In("remoteAddr")  
	String remoteAddress;
	
	public SdmLog ingresarLog(SdmLog sdmLog){
		Calendar cal = Calendar.getInstance();
		
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest)context.getRequest();
		String ip = request.getRemoteAddr();
		sdmLog.setIp(ip);
		System.out.print("ip"+ ip);

		Date d = new Date();
		
		sdmLog.setFecha(new Timestamp(d.getTime()) );
		sdmLogHome.crear(sdmLog);
		return sdmLog; 
	}
	
}
