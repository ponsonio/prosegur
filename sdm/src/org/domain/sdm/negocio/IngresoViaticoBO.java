package org.domain.sdm.negocio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.ejb.Remove;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.domain.sdm.entity.SdmCentroCosto;
import org.domain.sdm.entity.SdmDelegacion;
import org.domain.sdm.entity.SdmDivicion;
import org.domain.sdm.entity.SdmEmpleado;
import org.domain.sdm.entity.SdmEmpresa;
import org.domain.sdm.entity.SdmInformeViaticoDetalle;
import org.domain.sdm.entity.SdmInformeViaticos;
import org.domain.sdm.entity.SdmTipoServicio;
import org.domain.sdm.session.SdmEmpleadoHome;
import org.domain.sdm.session.SdmInformeViaticosHome;
import org.domain.sdm.session.SdmTipoServicioHome;
import org.domain.sdm.vista.bean.Adelanto;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;



@Name("ingresoViaticoBO")
@Scope(ScopeType.SESSION)
public class IngresoViaticoBO implements Serializable {


	private static final long serialVersionUID = -3941348397071561579L;

	@Logger
	private Log log;
	
	@In(create= true)
	LoggerBO loggerBO ;
	
	String stringCrearInforme = "Crear Informe";
	
    @In 
    StatusMessages statusMessages;

    @In
    InformeLiquidacionPDFBO informeLiquidacionPDFBO;
	
	private String codigoBuscar;
	
	private String nombreBuscar;
	
	private String tabSelect;

	private int correlativo = 0 ;
	
	SdmTipoServicio sdmTipoServicioSelect = new SdmTipoServicio();

	SdmEmpleado sdmEmpleadoSelect = new SdmEmpleado();

	@DataModel
	ArrayList<Adelanto> arraylistAdelanto;

	@DataModelSelection("arraylistAdelanto")
	Adelanto adelantoSelect;

	@DataModel
	ArrayList<SdmEmpleado> arraylistSdmEmpleado;

	@DataModelSelection("arraylistSdmEmpleado")
	SdmEmpleado sdmEmpleadoBusquedaNombre;
	
	public BigDecimal bigDecimalTotal = new BigDecimal(00.00);
	

	SdmInformeViaticos sdmInformeViaticos;


	Adelanto adelantoNuevo = new Adelanto();

	@In
	SdmEmpleado sdmEmpleado;

	@In(create=true)
	SdmTipoServicioHome sdmTipoServicioHome ;

	@In(create=true)
	SdmEmpleadoHome sdmEmpleadoHome;
	
	@In(create=true)
	SdmInformeViaticosHome sdmInformeViaticosHome;

	@Factory("arrayListSdmTipoServicio")
	public ArrayList<SdmTipoServicio> obtenerTiposGasto() {
		return sdmTipoServicioHome.obtenerListaSdmTipoServicioActivo();
	}

	public String buscarSdmEmpleadoActivoPorNombre(){
   		arraylistSdmEmpleado = sdmEmpleadoHome.buscarEmpeladoActivoXNombre(this.nombreBuscar) ;
   		this.tabSelect="tabNombre";
		return "/ingresoViaticos.xhtml";
   }

	public String cargarEmpleadoSelect(){
		this.sdmEmpleadoSelect = sdmEmpleadoBusquedaNombre;
		this.arraylistSdmEmpleado = new  ArrayList<SdmEmpleado>();
		return "/ingresoViaticos.xhtml";
	}
	
	
	public String buscarSdmEmpleado() throws Exception {
		//String codigoEmpleado = sdmEmpleadoSelect.getCodigo();
		this.sdmEmpleadoSelect = new SdmEmpleado();
		try {
			this.sdmEmpleadoSelect = sdmEmpleadoHome.buscarSdmEmpleadoActivoPorCodigo(this.codigoBuscar);
		}catch (NoResultException nre) {
			log.info("Usuario no encontrado"+this.codigoBuscar);
			statusMessages.add(Severity.ERROR,"No se encontró su usuario, por favor comunicarse con el área de soporte");
		}catch (NonUniqueResultException nue) {
			log.error(Severity.ERROR,"Más de un registro de usuario"+this.codigoBuscar);
			throw nue;
		}
		this.tabSelect="tabCodigo";
		return "/ingresoViaticos.xhtml";
	}
	
	
	/**
	 * Agrega un adelanto
	 * @return
	 * @throws Exception
	 */
	public String agregarAdelanto() throws Exception{
		try {
			if (adelantoNuevo.getMonto().compareTo(BigDecimal.ZERO) < 1){
				statusMessages.add(Severity.ERROR,"Ingrese un monto superior a 0.00") ;
				return "/ingresoViaticos.xhtml";
			}
			if (this.sdmEmpleadoSelect.getId() <= 0 ){
				statusMessages.add(Severity.ERROR,"Selecione un empleado primero") ;
				return "/ingresoViaticos.xhtml";
			}
			if (arraylistAdelanto == null) {
				arraylistAdelanto = new ArrayList<Adelanto>();
			}
			sdmTipoServicioSelect =  sdmTipoServicioHome.getSdmTipoServicioxId(sdmTipoServicioSelect
					.getId());
	
			adelantoNuevo.setMonto(adelantoNuevo.getMonto().setScale(2, RoundingMode.CEILING));
	
			Adelanto adelantoArray = new Adelanto();
			adelantoArray.setCorrelativo(this.correlativo);
			adelantoArray.setMonto(adelantoNuevo.getMonto());
			adelantoArray.setDescripcion(adelantoNuevo.getDescripcion());
			adelantoArray.setDestino(adelantoNuevo.getDestino());
			adelantoArray.setSdmTipoServicio(new SdmTipoServicio(sdmTipoServicioSelect));
			adelantoArray.setCodigoCentroCosto(sdmEmpleadoSelect.getSdmCentroCosto().getCodigo());
			adelantoArray.setCodigoDelegacion(sdmEmpleadoSelect.getSdmDelegacion().getCodigo());
			adelantoArray.setCodigoDivicion(sdmEmpleadoSelect.getSdmDivicion().getCodigo());
			adelantoArray.setCodigoEmpresa(sdmEmpleadoSelect.getSdmEmpresa().getCodigo());
			adelantoArray.setSdmEmpleado(new SdmEmpleado(sdmEmpleadoSelect));
			adelantoArray.setDateFechaAdelanto(new Date());
			arraylistAdelanto.add(adelantoArray);
	
			this.bigDecimalTotal = this.bigDecimalTotal.add(adelantoNuevo.getMonto());
			adelantoNuevo = new Adelanto();
			this.correlativo++;
			
			return "/ingresoViaticos.xhtml";
			
		} catch (Exception e) {
			loggerBO.ingresarRegistroError(this.getClass().getCanonicalName(),
					e.getMessage(), "Agregando Adelanto", null);
			throw e;
		}

	}

	
	/**
	 * Graba un informe
	 * @return
	 * @throws Exception
	 */
	public String grabarInforme()  throws Exception{
		try {
			if (arraylistAdelanto == null ||  arraylistAdelanto.size() == 0){
				statusMessages.add(Severity.ERROR,"Debe añadir al memos un adelanto") ;
				return "/ingresoViaticos.xhtml";
			}
	
			sdmInformeViaticos = new SdmInformeViaticos();
			sdmInformeViaticos.setAnulado(false);
			sdmInformeViaticos.setFecha(new Date());		
			sdmInformeViaticos.setSdmEmpleado(sdmEmpleado);
			sdmInformeViaticos.setSdmCentroCosto(sdmEmpleado
					.getSdmCentroCosto());
			sdmInformeViaticos.setSdmDelegacion(sdmEmpleado
					.getSdmDelegacion());
			sdmInformeViaticos.setSdmDivicion(sdmEmpleado.getSdmDivicion());
			sdmInformeViaticos.setSdmEmpresa(sdmEmpleado.getSdmEmpresa());
			sdmInformeViaticos.setMontoTotal(this.bigDecimalTotal);
			
			SdmInformeViaticoDetalle sdmInformeViaticoDetalle;
			Iterator<Adelanto> it = arraylistAdelanto.iterator();
			while (it.hasNext()) {
				Adelanto adelanto = it.next();
				sdmInformeViaticoDetalle = new SdmInformeViaticoDetalle();
				sdmInformeViaticoDetalle.setSdmInformeViaticos(sdmInformeViaticos);
				sdmInformeViaticoDetalle.setCorrelativoInforme(adelanto.getCorrelativo());
				
				SdmCentroCosto sdmCentroCosto = new SdmCentroCosto();
				sdmCentroCosto.setCodigo(adelanto
						.getCodigoCentroCosto());
				sdmInformeViaticoDetalle.setSdmCentroCosto(sdmCentroCosto);
				
				SdmDelegacion sdmDelegacion = new SdmDelegacion();
				sdmDelegacion.setCodigo(adelanto
						.getCodigoDelegacion());
				
				sdmInformeViaticoDetalle.setSdmDelegacion(sdmDelegacion);
				
				SdmDivicion sdmDivicion = new SdmDivicion();
				sdmDivicion.setCodigo(adelanto
						.getCodigoDivicion());
				
				sdmInformeViaticoDetalle.setSdmDivicion(sdmDivicion);
				
				SdmEmpresa sdmEmpresa = new SdmEmpresa();
				sdmEmpresa.setCodigo(adelanto
						.getCodigoEmpresa());
				
				sdmInformeViaticoDetalle.setSdmEmpresa(sdmEmpresa);
				sdmInformeViaticoDetalle.setSdmTipoServicio(adelanto
						.getSdmTipoServicio());
				sdmInformeViaticoDetalle.setDescripcion(adelanto.getDescripcion());
				sdmInformeViaticoDetalle.setDestino(adelanto.getDestino());
				sdmInformeViaticoDetalle.setSdmEmpleado(adelanto.getSdmEmpleado());
				sdmInformeViaticoDetalle.setFecha(adelanto.getDateFechaAdelanto());
				sdmInformeViaticoDetalle.setMonto(adelanto.getMonto());
				
				
				sdmInformeViaticos.getSdmInformeViaticoDetalles().add(
						sdmInformeViaticoDetalle);
			}
			
			this.sdmInformeViaticos = sdmInformeViaticosHome.grabarInformeViaticos(this.sdmInformeViaticos);
	
			
			 loggerBO.ingresarRegistroEvento(this.getClass().getCanonicalName(), 
						"Se grabó el informe " , this.stringCrearInforme ,String.valueOf(sdmInformeViaticos.getId()));		
			
			informeLiquidacionPDFBO.setEnviarCorreo(true);
			informeLiquidacionPDFBO.setIdInforme(this.sdmInformeViaticos.getId());
			
			arraylistAdelanto = new ArrayList<Adelanto>();
			this.bigDecimalTotal = new BigDecimal(0.0);
			return "/impresionInforme.xhtml";
		
		} catch (Exception e) {
			loggerBO.ingresarRegistroError(this.getClass().getCanonicalName(),
					e.getMessage(), "Grabar Solicitud", null);
			throw e;
		}

	}

	
	
	
	public void delete() {
		bigDecimalTotal = bigDecimalTotal.subtract(adelantoSelect.getMonto());
		arraylistAdelanto.remove(adelantoSelect);
	}



	public SdmInformeViaticos getInformeViaticos() {
		return sdmInformeViaticos;
	}

	public void setInformeViaticos(SdmInformeViaticos sdmInformeViaticos) {
		this.sdmInformeViaticos = sdmInformeViaticos;
	}

	public SdmEmpleado getSdmEmpleadoSelect() {
		return sdmEmpleadoSelect;
	}

	public void setSdmEmpleadoSelect(SdmEmpleado empleadoSelect) {
		this.sdmEmpleadoSelect = empleadoSelect;
	}

	public SdmTipoServicio getSdmTipoServicioSelect() {
		return sdmTipoServicioSelect;
	}

	public void setSdmTipoServicioSelect(SdmTipoServicio tipoServicioSelect) {
		this.sdmTipoServicioSelect = tipoServicioSelect;
	}

	@Remove
	public void destroy() {
	}
	

	public String getCodigoBuscar() {
		return codigoBuscar;
	}

	public void setCodigoBuscar(String codigoBuscar) {
		this.codigoBuscar = codigoBuscar;
	}

	public String getNombreBuscar() {
		return nombreBuscar;
	}

	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	public String getTabSelect() {
		return tabSelect;
	}

	public void setTabSelect(String tabSelect) {
		this.tabSelect = tabSelect;
	}



	public BigDecimal getBigDecimalTotal() {
		return bigDecimalTotal;
	}

	public void setBigDecimalTotal(BigDecimal bigDecimalTotal) {
		this.bigDecimalTotal = bigDecimalTotal;
	}

	public Adelanto getAdelantoNuevo() {
		return adelantoNuevo;
	}

	public void setAdelantoNuevo(Adelanto adelantoNuevo) {
		this.adelantoNuevo = adelantoNuevo;
	}

	public SdmInformeViaticos getSdmInformeViaticos() {
		return sdmInformeViaticos;
	}

	public void setSdmInformeViaticos(SdmInformeViaticos sdmInformeViaticos) {
		this.sdmInformeViaticos = sdmInformeViaticos;
	}
    
	
}