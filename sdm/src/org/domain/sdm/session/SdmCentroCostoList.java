package org.domain.sdm.session;

import org.domain.sdm.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("sdmCentroCostoList")
public class SdmCentroCostoList extends EntityQuery<SdmCentroCosto> {

	private static final String EJBQL = "select sdmCentroCosto from SdmCentroCosto sdmCentroCosto";

	private static final String[] RESTRICTIONS = {
			"lower(sdmCentroCosto.codigo) like lower(concat(#{sdmCentroCostoList.sdmCentroCosto.codigo},'%'))",
			"lower(sdmCentroCosto.nombre) like lower(concat(#{sdmCentroCostoList.sdmCentroCosto.nombre},'%'))", };

	private SdmCentroCosto sdmCentroCosto = new SdmCentroCosto();

	public SdmCentroCostoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public SdmCentroCosto getSdmCentroCosto() {
		return sdmCentroCosto;
	}
}
