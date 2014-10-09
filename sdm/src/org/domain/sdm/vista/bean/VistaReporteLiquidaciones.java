package org.domain.sdm.vista.bean;

import java.util.Date;

import org.domain.sdm.entity.SdmInformeViaticoDetalle;
import org.domain.sdm.entity.SdmInformeViaticos;
import org.domain.sdm.entity.SdmLiquidacionInformeViaticos;

public class VistaReporteLiquidaciones {
	
	SdmInformeViaticos sdmInformeViaticos ;
	
	SdmInformeViaticoDetalle sdmInformeViaticoDetalle ;
	
	SdmLiquidacionInformeViaticos sdmLiquidacionInformeViaticos;

	String nombreEmpleadoLiquidador = "";
	
	String codigoEmpleadoLiquidador ="";
	
	Date fechaLiquidacion  ;
	
	
	
	public SdmInformeViaticos getSdmInformeViaticos() {
		return sdmInformeViaticos;
	}

	public void setSdmInformeViaticos(SdmInformeViaticos sdmInformeViaticos) {
		this.sdmInformeViaticos = sdmInformeViaticos;
	}

	public SdmInformeViaticoDetalle getSdmInformeViaticoDetalle() {
		return sdmInformeViaticoDetalle;
	}

	public void setSdmInformeViaticoDetalle(
			SdmInformeViaticoDetalle sdmInformeViaticoDetalle) {
		this.sdmInformeViaticoDetalle = sdmInformeViaticoDetalle;
	}

	public SdmLiquidacionInformeViaticos getSdmLiquidacionInformeViaticos() {
		return sdmLiquidacionInformeViaticos;
	}

	public void setSdmLiquidacionInformeViaticos(
			SdmLiquidacionInformeViaticos sdmLiquidacionInformeViaticos) {
		this.sdmLiquidacionInformeViaticos = sdmLiquidacionInformeViaticos;
	}

	public String getNombreEmpleadoLiquidador() {
		if (this.sdmLiquidacionInformeViaticos != null) this.nombreEmpleadoLiquidador = this.sdmLiquidacionInformeViaticos.getSdmEmpleado().getNombre();
		return this.nombreEmpleadoLiquidador;
	}

	public void setNombreEmpleadoLiquidador(String nombreEmpleadoLiquidador) {
		this.nombreEmpleadoLiquidador = nombreEmpleadoLiquidador;
	}

	public Date getFechaLiquidacion() {
		if (this.sdmLiquidacionInformeViaticos != null) this.fechaLiquidacion = this.sdmLiquidacionInformeViaticos.getFecha();
		return this.fechaLiquidacion;
	}

	public void setFechaLiquidacion(Date fechaLiquidacion) {
		this.fechaLiquidacion = fechaLiquidacion;
	}

	public String getCodigoEmpleadoLiquidador() {
		if (this.sdmLiquidacionInformeViaticos != null) this.codigoEmpleadoLiquidador = this.sdmLiquidacionInformeViaticos.getSdmEmpleado().getCodigo();
		return codigoEmpleadoLiquidador;
	}

	public void setCodigoEmpleadoLiquidador(String codigoEmpleadoLiquidador) {
		this.codigoEmpleadoLiquidador = codigoEmpleadoLiquidador;
	}

	
	
}
