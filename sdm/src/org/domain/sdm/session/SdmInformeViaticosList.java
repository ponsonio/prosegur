package org.domain.sdm.session;

import org.domain.sdm.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("sdmInformeViaticosList")
public class SdmInformeViaticosList extends EntityQuery<SdmInformeViaticos> {

	private static final String EJBQL = "select sdmInformeViaticos from SdmInformeViaticos sdmInformeViaticos";

	private static final String[] RESTRICTIONS = {};

	private SdmInformeViaticos sdmInformeViaticos = new SdmInformeViaticos();

	public SdmInformeViaticosList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public SdmInformeViaticos getSdmInformeViaticos() {
		return sdmInformeViaticos;
	}
}
