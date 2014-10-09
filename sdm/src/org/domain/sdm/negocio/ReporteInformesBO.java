package org.domain.sdm.negocio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.domain.sdm.entity.SdmCentroCosto;
import org.domain.sdm.entity.SdmInformeViaticoDetalle;
import org.domain.sdm.entity.SdmInformeViaticos;
import org.domain.sdm.entity.SdmLiquidacionInformeViaticos;
import org.domain.sdm.entity.SdmTipoServicio;
import org.domain.sdm.session.SdmCentroCostoHome;
import org.domain.sdm.session.SdmInformeViaticoDetalleHome;
import org.domain.sdm.session.SdmInformeViaticosHome;
import org.domain.sdm.session.SdmLiquidacionInformeViaticosHome;
import org.domain.sdm.session.SdmTipoServicioHome;
import org.domain.sdm.vista.bean.VistaReporteCCTipoServicio;
import org.domain.sdm.vista.bean.VistaReporteLiquidaciones;
import org.domain.sdm.vista.bean.VistaReporteTotalCentroCosto;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;



@Name("reporteInformesBO")
@Scope(ScopeType.CONVERSATION)
public class ReporteInformesBO implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -760128394313886983L;

	List<SdmInformeViaticos> arrayInformeViaticos = new ArrayList<SdmInformeViaticos>() ;
	
	List<VistaReporteLiquidaciones> arrayVistaReporteLiquidaciones = new ArrayList<VistaReporteLiquidaciones>();
	
	
	List<VistaReporteCCTipoServicio> arrayVistaReporteCCTipoServicio = new ArrayList<VistaReporteCCTipoServicio>();
	
	
	private String tabSelect;
	
	Date fechaDesde = new Date();
	
	Date fechaHasta = new Date();
	
	@In(create=true)
	SdmInformeViaticosHome sdmInformeViaticosHome ;
	
	@In(create=true)
	SdmInformeViaticoDetalleHome sdmInformeViaticoDetalleHome;
	
	@In(create=true)
	SdmLiquidacionInformeViaticosHome sdmLiquidacionInformeViaticosHome;
	
	@In(create=true)
	SdmCentroCostoHome sdmCentroCostoHome;
	
	@In(create=true)
	SdmTipoServicioHome sdmTipoServicioHome;
	
	@Logger private Log log;
	
	public String generarReporte(){
		this.arrayInformeViaticos = sdmInformeViaticosHome.buscarInformesLiquidacionesActivosXFechas(this.fechaDesde, this.fechaHasta);
		mostrarDetalle();
		return "/reporteInformes_xls.xhtml";
	}
	
	public void mostrarDetalle(){
		this.arrayVistaReporteLiquidaciones = new ArrayList<VistaReporteLiquidaciones>();
		Iterator<SdmInformeViaticos> it1 = this.arrayInformeViaticos.iterator();
		while (it1.hasNext()){
			SdmInformeViaticos informe =  it1.next();
			SdmLiquidacionInformeViaticos liquidacion = informe.getSdmLiquidacionInformeViaticos();
			Iterator<SdmInformeViaticoDetalle> it2 = informe.getSdmInformeViaticoDetalles().iterator();		
			while (it2.hasNext()){
				VistaReporteLiquidaciones vista = new VistaReporteLiquidaciones();
				vista.setSdmInformeViaticos(informe);
				vista.setSdmLiquidacionInformeViaticos(liquidacion);
				vista.setSdmInformeViaticoDetalle(it2.next());
				arrayVistaReporteLiquidaciones.add(vista);
			}		
		}
		
	}

	public String generarReporteCentroCostoTipoServicio(){
		
		List<VistaReporteCCTipoServicio> arrayVistaReporteCCTipoServicio = new ArrayList<VistaReporteCCTipoServicio>();
		ArrayList<SdmCentroCosto> arraySdmCentroCostos = this.sdmCentroCostoHome.listarCentroCostos();
		ArrayList<SdmTipoServicio> arrayTipoServicios  = this.sdmTipoServicioHome.obtenerListaSDMTipoServicio();
		
		//log.error("centro de costos:"+arraySdmCentroCostos.size());
		//log.error("centro de tipo serv:"+arrayTipoServicios.size());
		
		//Itero los centros de costo
		Iterator<SdmCentroCosto> it1 = arraySdmCentroCostos.iterator();
		while (it1.hasNext()){
			SdmCentroCosto sdmCentroCosto = it1.next();
			//Itero los tipos de serv
			Iterator<SdmTipoServicio> it2 = arrayTipoServicios.iterator();
			while (it2.hasNext()){
				SdmTipoServicio sdmTipoServicio = it2.next();
				VistaReporteCCTipoServicio vista = new VistaReporteCCTipoServicio();
				vista.setSdmCentroCosto(sdmCentroCosto);
				vista.setSdmTipoServicio(sdmTipoServicio);
				vista.setBigDecimalTotal(
						sdmInformeViaticoDetalleHome
						.obtenerTotalDetalleXCCXTipoActivosFechas(sdmCentroCosto.getCodigo(), sdmTipoServicio.getId(), 
								this.fechaDesde, this.fechaHasta));
				this.arrayVistaReporteCCTipoServicio.add(vista);
			}
			
		}
		
		return "reporteInformesCCTipoServicio.xhtml";
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

	public SdmInformeViaticosHome getSdmInformeViaticosHome() {
		return sdmInformeViaticosHome;
	}

	public void setSdmInformeViaticosHome(
			SdmInformeViaticosHome sdmInformeViaticosHome) {
		this.sdmInformeViaticosHome = sdmInformeViaticosHome;
	}

	public String getTabSelect() {
		return tabSelect;
	}

	public void setTabSelect(String tabSelect) {
		this.tabSelect = tabSelect;
	}

	public List<SdmInformeViaticos> getArrayInformeViaticos() {
		return arrayInformeViaticos;
	}

	public void setArrayInformeViaticos(
			List<SdmInformeViaticos> arrayInformeViaticos) {
		this.arrayInformeViaticos = arrayInformeViaticos;
	}

	public List<VistaReporteLiquidaciones> getArrayVistaReporteLiquidaciones() {
		return arrayVistaReporteLiquidaciones;
	}

	public void setArrayVistaReporteLiquidaciones(
			List<VistaReporteLiquidaciones> arrayVistaReporteLiquidaciones) {
		this.arrayVistaReporteLiquidaciones = arrayVistaReporteLiquidaciones;
	}

	public List<VistaReporteCCTipoServicio> getArrayVistaReporteCCTipoServicio() {
		return arrayVistaReporteCCTipoServicio;
	}

	public void setArrayVistaReporteCCTipoServicio(
			List<VistaReporteCCTipoServicio> arrayVistaReporteCCTipoServicio) {
		this.arrayVistaReporteCCTipoServicio = arrayVistaReporteCCTipoServicio;
	}
		
	
}
