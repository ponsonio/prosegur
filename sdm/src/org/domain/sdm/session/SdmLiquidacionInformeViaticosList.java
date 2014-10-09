package org.domain.sdm.session;

import org.domain.sdm.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("sdmLiquidacionInformeViaticosList")
public class SdmLiquidacionInformeViaticosList extends
		EntityQuery<SdmLiquidacionInformeViaticos> {

	private static final String EJBQL = "select sdmLiquidacionInformeViaticos from SdmLiquidacionInformeViaticos sdmLiquidacionInformeViaticos";

	private static final String[] RESTRICTIONS = {};

	private SdmLiquidacionInformeViaticos sdmLiquidacionInformeViaticos = new SdmLiquidacionInformeViaticos();

	public SdmLiquidacionInformeViaticosList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public SdmLiquidacionInformeViaticos getSdmLiquidacionInformeViaticos() {
		return sdmLiquidacionInformeViaticos;
	}
}
