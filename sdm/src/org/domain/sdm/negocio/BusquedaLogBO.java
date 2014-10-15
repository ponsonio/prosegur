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
	
	
	public List<SdmLog> getListLog() {
		return listLog;
	}



	public void setListLog(List<SdmLog> listLog) {
		this.listLog = listLog;
	}

	public List<SdmLog> listLog = new ArrayList<SdmLog>();
	
	
	public String buscarLog() throws Exception{
		try{
			listLog = (ArrayList<SdmLog>)sdmLogHome.buscarLogFechas(this.fechaDesde, this.fechaHasta);
			//System.out.print("listLog.size()"+ listLog.size());
		}catch(Exception e){
			loggerBO.ingresarRegistroError(this.getClass().getCanonicalName(),
					e.getMessage(), "Busqueda empleado", null);
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
	
	
	
}
