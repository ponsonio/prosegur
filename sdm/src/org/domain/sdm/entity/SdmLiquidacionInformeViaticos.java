package org.domain.sdm.entity;

// Generated Jul 16, 2014 12:05:59 PM by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.NotNull;

/**
 * SdmLiquidacionInformeViaticos generated by hbm2java
 */
@Entity
@Table(name = "SDM_liquidacion_informe_viaticos")
public class SdmLiquidacionInformeViaticos implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1090183167886724344L;
	private long id;
	private SdmInformeViaticos sdmInformeViaticos;
	private SdmEmpleado sdmEmpleado;
	private Date fecha;
	private BigDecimal monto;
	private boolean anulado;

	public SdmLiquidacionInformeViaticos() {
	}

	public SdmLiquidacionInformeViaticos(long id,
			SdmInformeViaticos sdmInformeViaticos, SdmEmpleado sdmEmpleado,
			Date fecha, BigDecimal monto, boolean anulado) {
		this.id = id;
		this.sdmInformeViaticos = sdmInformeViaticos;
		this.sdmEmpleado = sdmEmpleado;
		this.fecha = fecha;
		this.monto = monto;
		this.anulado = anulado;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_informe_viaticos", nullable = false)
	@NotNull
	public SdmInformeViaticos getSdmInformeViaticos() {
		return this.sdmInformeViaticos;
	}

	public void setSdmInformeViaticos(SdmInformeViaticos sdmInformeViaticos) {
		this.sdmInformeViaticos = sdmInformeViaticos;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_empleado_liquida", nullable = false)
	@NotNull
	public SdmEmpleado getSdmEmpleado() {
		return this.sdmEmpleado;
	}

	public void setSdmEmpleado(SdmEmpleado sdmEmpleado) {
		this.sdmEmpleado = sdmEmpleado;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "fecha", nullable = false, length = 10)
	@NotNull
	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@Column(name = "monto", nullable = false, precision = 8)
	@NotNull
	public BigDecimal getMonto() {
		return this.monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	@Column(name = "anulado", nullable = false)
	public boolean isAnulado() {
		return this.anulado;
	}

	public void setAnulado(boolean anulado) {
		this.anulado = anulado;
	}

}
