package org.domain.sdm.vista.bean;

import java.math.BigDecimal;

import org.domain.sdm.entity.SdmCentroCosto;
import org.domain.sdm.entity.SdmTipoServicio;

public class VistaReporteCCTipoServicio {
	
	SdmTipoServicio sdmTipoServicio;
	
	SdmCentroCosto sdmCentroCosto ;
	
	BigDecimal bigDecimalTotal = new BigDecimal(0);

	public SdmTipoServicio getSdmTipoServicio() {
		return sdmTipoServicio;
	}

	public void setSdmTipoServicio(SdmTipoServicio sdmTipoServicio) {
		this.sdmTipoServicio = sdmTipoServicio;
	}

	public SdmCentroCosto getSdmCentroCosto() {
		return sdmCentroCosto;
	}

	public void setSdmCentroCosto(SdmCentroCosto sdmCentroCosto) {
		this.sdmCentroCosto = sdmCentroCosto;
	}

	public BigDecimal getBigDecimalTotal() {
		return bigDecimalTotal;
	}

	public void setBigDecimalTotal(BigDecimal bigDecimalTotal) {
		this.bigDecimalTotal = bigDecimalTotal;
	}

	
}
