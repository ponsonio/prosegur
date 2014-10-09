package org.domain.sdm.vista.bean;

import java.math.BigDecimal;

public class VistaReporteTotalCentroCosto {
	
	private String delegacion_divicion_cc;
	
	private BigDecimal totalCentroCosto;


	

	public String getDelegacion_divicion_cc() {
		return delegacion_divicion_cc;
	}

	public void setDelegacion_divicion_cc(String delegacion_divicion_cc) {
		this.delegacion_divicion_cc = delegacion_divicion_cc;
	}

	public BigDecimal getTotalCentroCosto() {
		return totalCentroCosto;
	}

	public void setTotalCentroCosto(BigDecimal totalCentroCosto) {
		this.totalCentroCosto = totalCentroCosto;
	}

	@Override
	public String toString() {
		return "VistaReporteTotalCentroCosto [nombreCentro=" + delegacion_divicion_cc
				+ ", totalCentroCosto=" + totalCentroCosto + "]";
	}
	
	

}
