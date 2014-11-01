package org.domain.sdm.negocio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletContext;

import org.domain.sdm.entity.SdmEmpleado;
import org.domain.sdm.entity.SdmInformeViaticoDetalle;
import org.domain.sdm.entity.SdmInformeViaticos;
import org.domain.sdm.entity.SdmLiquidacionInformeViaticos;
import org.domain.sdm.entity.SdmTipoServicio;
import org.domain.sdm.entity.SdmUsuario;
import org.domain.sdm.session.SdmInformeViaticosHome;
import org.domain.sdm.vista.bean.VistaReporteTotalCentroCosto;
import org.hibernate.annotations.OrderBy;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.core.ResourceLoader;
import org.jboss.seam.faces.Renderer;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;
import org.jboss.seam.web.ServletContexts;




@Name("informeLiquidacionPDFBO")
@Scope(ScopeType.SESSION)
@AutoCreate
public class InformeLiquidacionPDFBO implements  Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5759529596468420489L;
	
	@In(create= true)
	LoggerBO loggerBO ;

	
	String stringGenerarReporte = "Generar Reporte";
	
	String stringEnviarReporte = "Enviar Reporte Email";
	
	public List<SdmInformeViaticoDetalle> listSdmInformeViaticoDetalle ;
	
	
	@In(create=true)
	private Renderer renderer;

    @In 
    StatusMessages statusMessages;

	@In
	SdmEmpleado sdmEmpleado;
	
	@In
	SdmUsuario sdmUsuario;
	
	HashMap<String ,BigDecimal> hashTotalCentroCosto ;
	
	ArrayList<VistaReporteTotalCentroCosto> arrTotalCentroCostos ;
	
	@In(create=true)
	SdmInformeViaticosHome sdmInformeViaticosHome;

	boolean enviarCorreo = false;
	
	long idInforme ;
	
	
	public List<SdmInformeViaticoDetalle> getListSdmInformeViaticoDetalle() {
		return listSdmInformeViaticoDetalle;
	}


	public void setListSdmInformeViaticoDetalle(
			List<SdmInformeViaticoDetalle> listSdmInformeViaticoDetalle) {
		this.listSdmInformeViaticoDetalle = listSdmInformeViaticoDetalle;
	}


	public SdmInformeViaticos sdmInformeViaticos;
	


	
	@Logger private Log log;
	
	@In(create = true)
	IngresoViaticoBO ingresoViaticoBO;
	
	@In 
	private EntityManager entityManager;
	
	
	public String generarReporteXidSinCorreo(long id) throws Exception{
		this.idInforme = id;
		this.enviarCorreo = false;
		this.generarReporte();
	    return "/informePDF.xhtml";
	}
	
	
	
	
	public void generarReporte()  throws Exception {
		try{
			
			this.sdmInformeViaticos = sdmInformeViaticosHome.obtenerInformeViaticos(this.idInforme);
			
			if (sdmInformeViaticos.isAnulado()){
				statusMessages.add(Severity.ERROR, "El informe se encuentra anulado y no se puede imprimir");
				log.error("El informe se encuentra anulado y no se puede imprimir");
				return;
			}
			Iterator<SdmInformeViaticoDetalle> it =  sdmInformeViaticos.getSdmInformeViaticoDetalles().iterator();
			listSdmInformeViaticoDetalle = new ArrayList<SdmInformeViaticoDetalle>();
			
			this.hashTotalCentroCosto = new HashMap<String ,BigDecimal>();
			
			while(it.hasNext()){
				SdmInformeViaticoDetalle sdmInformeViaticoDetalle = it.next();
				listSdmInformeViaticoDetalle.add(sdmInformeViaticoDetalle);
				
				String delegacion_divicion_cc = sdmInformeViaticoDetalle.getSdmDelegacion().getCodigo() +"/"+sdmInformeViaticoDetalle.getSdmDivicion().getCodigo()+"/" +sdmInformeViaticoDetalle.getSdmCentroCosto().getCodigo() ;
				BigDecimal subTotalCC ;
				
				subTotalCC = hashTotalCentroCosto.get(delegacion_divicion_cc) ;

				if (subTotalCC == null){
					hashTotalCentroCosto.put(delegacion_divicion_cc, sdmInformeViaticoDetalle.getMonto());				
				}else{
					hashTotalCentroCosto.put(delegacion_divicion_cc, sdmInformeViaticoDetalle.getMonto().add(subTotalCC));
				}
			}
			
			Iterator<Map.Entry<String,BigDecimal>> itr = this.hashTotalCentroCosto.entrySet().iterator();
			
			this.arrTotalCentroCostos = new ArrayList<VistaReporteTotalCentroCosto>();
			
			while (itr.hasNext()){
				 Map.Entry<String, BigDecimal> entry = itr.next();
				 VistaReporteTotalCentroCosto vista =  new VistaReporteTotalCentroCosto();
				 vista.setDelegacion_divicion_cc(entry.getKey());
				 vista.setTotalCentroCosto(entry.getValue());
				 this.arrTotalCentroCostos.add(vista);
			}

			 loggerBO.ingresarRegistroEvento(this.getClass().getCanonicalName(), 
						"Se generó el reporte (pdf) " , this.stringGenerarReporte ,String.valueOf(sdmInformeViaticos.getId()));		

			
			if (this.enviarCorreo){
				try {
					enviarEmail();
				}catch (Exception e){
					   statusMessages.add(Severity.ERROR ,"Ocurrió un error al enviar el correo : " + e.getMessage());
					   log.error(e.getStackTrace());

					   loggerBO.ingresarRegistroError(this.getClass().getCanonicalName(),
								e.getMessage(), "Enviando Email ", String.valueOf(this.idInforme));

				}
			}

		} catch (Exception e) {
			loggerBO.ingresarRegistroError(this.getClass().getCanonicalName(),
					e.getMessage(), "Generado  para el informe de liquidación ", String.valueOf(this.idInforme));
			throw e;
		}
			
	}
	

	
	
    public String getReportURL() {
        return "/sdm/informePDF.seam";
    }
     

	/**
	 * Envia email con la liquidación
	 * @return
	 * @throws Exception
	 */
	public String enviarEmail() throws Exception{
			
		  try {
			  renderer.render("/emailInformeLiquidacion.xhtml");
				 loggerBO.ingresarRegistroEvento(this.getClass().getCanonicalName(), 
							"Se envio email " , this.stringEnviarReporte ,String.valueOf(sdmInformeViaticos.getId()));
				 
		   } catch (Exception e) {
			   statusMessages.add(Severity.ERROR ,"Ocurrió un error al enviar el correo: " + e.getMessage());
			   
				 loggerBO.ingresarRegistroError(this.getClass().getCanonicalName(), 
							"Ocurrió un error al enviar email " + e.getMessage()  , this.stringEnviarReporte ,String.valueOf(sdmInformeViaticos.getId()));		

			   log.error(e.getStackTrace());
			   throw e;
		   }
		    return "/impresionInforme.xhtml";
	}


	

	public SdmInformeViaticos getSdmInformeViaticos() {
		return sdmInformeViaticos;
	}


	public void setSdmInformeViaticos(SdmInformeViaticos sdmInformeViaticos) {
		this.sdmInformeViaticos = sdmInformeViaticos;
	}


	public boolean isEnviarCorreo() {
		return enviarCorreo;
	}


	public void setEnviarCorreo(boolean enviarCorreo) {
		this.enviarCorreo = enviarCorreo;
	}


	public long getIdInforme() {
		return idInforme;
	}


	public void setIdInforme(long idInforme) {
		this.idInforme = idInforme;
	}


	public HashMap<String, BigDecimal> getHashTotalCentroCosto() {
		return hashTotalCentroCosto;
	}


	public void setHashTotalCentroCosto(
			HashMap<String, BigDecimal> hashTotalCentroCosto) {
		this.hashTotalCentroCosto = hashTotalCentroCosto;
	}


	public ArrayList<VistaReporteTotalCentroCosto> getArrTotalCentroCostos() {
		return arrTotalCentroCostos;
	}


	public void setArrTotalCentroCostos(
			ArrayList<VistaReporteTotalCentroCosto> arrTotalCentroCostos) {
		this.arrTotalCentroCostos = arrTotalCentroCostos;
	}

	
	
}
