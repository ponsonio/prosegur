package org.domain.sdm.vista.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.domain.sdm.entity.SdmEmpleado;
import org.domain.sdm.entity.SdmTipoServicio;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;


@Name("adelanto")
@AutoCreate
@Scope(ScopeType.SESSION)
public class Adelanto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7989162204636266895L;
	
	private Date dateFechaAdelanto;
	private SdmTipoServicio  sdmTipoServicio ;
	private BigDecimal monto = new BigDecimal ("00.00");
	private String descripcion;
	private String destino;
	
	private SdmEmpleado sdmEmpleado;

	private int correlativo ;
	
	
   


	public int getCorrelativo() {
		return correlativo;
	}


	public void setCorrelativo(int correlativo) {
		this.correlativo = correlativo;
	}


	public SdmTipoServicio getSdmTipoServicio() {
		return sdmTipoServicio;
	}


	public void setSdmTipoServicio(SdmTipoServicio sdmTipoServicio) {
		this.sdmTipoServicio = sdmTipoServicio;
	}


	public SdmEmpleado getSdmEmpleado() {
		return sdmEmpleado;
	}


	public void setSdmEmpleado(SdmEmpleado sdmEmpleado) {
		this.sdmEmpleado = sdmEmpleado;
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



	private String codigoCentroCosto;
    private String codigoEmpresa;
    private String codigoDelegacion;
    private String codigoDivicion;
	
	public Adelanto(){
		
	}
	
	
	public Date getDateFechaAdelanto() {
		return dateFechaAdelanto;
	}
	public void setDateFechaAdelanto(Date dateFechaAdelanto) {
		this.dateFechaAdelanto = dateFechaAdelanto;
	}


	public BigDecimal getMonto() {
		return monto;
	}
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getDestino() {
		return destino;
	}
	public void setDestino(String destino) {
		this.destino = destino;
	}


	@Override
	public String toString() {
		return "Adelanto [dateFechaAdelanto=" + dateFechaAdelanto
				+ ", sdmTipoServicio=" + sdmTipoServicio.toString() + ", monto=" + monto
				+ ", descripcion=" + descripcion + ", destino=" + destino
				+ ", sdmEmpleado=" + sdmEmpleado.toString() + ", codigoCentroCosto="
				+ codigoCentroCosto + ", codigoEmpresa=" + codigoEmpresa
				+ ", codigoDelegacion=" + codigoDelegacion
				+ ", codigoDivicion=" + codigoDivicion + "]";
	}


}
