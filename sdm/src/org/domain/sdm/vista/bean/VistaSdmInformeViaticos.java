package org.domain.sdm.vista.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.domain.sdm.entity.SdmEmpleado;
import org.domain.sdm.entity.SdmInformeViaticos;

public class VistaSdmInformeViaticos  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7557907172139356421L;
	
	private long id;
	private Date fecha;
	private BigDecimal montoTotal;
	private String correlativo;
	private boolean procesado;
	private SdmEmpleado sdmEmpleado;
	private boolean anulado;
	private String codigoCentroCosto;
	private String codigoEmpresa;
	private String codigoDelegacion;
	private String codigoDivicion;
	
	private Date fechaLiquidacion ;
	
	private SdmInformeViaticos sdmInformeViaticos ;
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public BigDecimal getMontoTotal() {
		return montoTotal;
	}

	public void setMontoTotal(BigDecimal montoTotal) {
		this.montoTotal = montoTotal;
	}

	public String getCorrelativo() {
		return correlativo;
	}

	public void setCorrelativo(String correlativo) {
		this.correlativo = correlativo;
	}

	public boolean isProcesado() {
		return procesado;
	}

	public void setProcesado(boolean procesado) {
		this.procesado = procesado;
	}

	public SdmEmpleado getSdmEmpleado() {
		return sdmEmpleado;
	}

	public void setSdmEmpleado(SdmEmpleado sdmEmpleado) {
		this.sdmEmpleado = sdmEmpleado;
	}

	public boolean isAnulado() {
		return anulado;
	}

	public void setAnulado(boolean anulado) {
		this.anulado = anulado;
	}

	public String getCodigoCentroCosto() {
		return codigoCentroCosto;
	}

	public void setCodigoCentroCosto(String codigoCentroCosto) {
		this.codigoCentroCosto = codigoCentroCosto;
	}

	public String getCodigoEmpresa() {
		return codigoEmpresa;
	}

	public void setCodigoEmpresa(String codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}

	public String getCodigoDelegacion() {
		return codigoDelegacion;
	}

	public void setCodigoDelegacion(String codigoDelegacion) {
		this.codigoDelegacion = codigoDelegacion;
	}

	public String getCodigoDivicion() {
		return codigoDivicion;
	}

	public void setCodigoDivicion(String codigoDivicion) {
		this.codigoDivicion = codigoDivicion;
	}

	public boolean selecionado ;

	public boolean isSelecionado() {
		return selecionado;
	}

	public void setSelecionado(boolean seleciondo) {
		this.selecionado = seleciondo;
	}

	public Date getFechaLiquidacion() {
		return fechaLiquidacion;
	}

	public void setFechaLiquidacion(Date fechaLiquidacion) {
		this.fechaLiquidacion = fechaLiquidacion;
	}

	public SdmInformeViaticos getSdmInformeViaticos() {
		return sdmInformeViaticos;
	}

	public void setSdmInformeViaticos(SdmInformeViaticos sdmInformeViaticos) {
		this.sdmInformeViaticos = sdmInformeViaticos;
	} 
	
	
	

}
