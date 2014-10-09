package org.domain.sdm.session;

import org.domain.sdm.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("sdmDivicionList")
public class SdmDivicionList extends EntityQuery<SdmDivicion> {

	private static final String EJBQL = "select sdmDivicion from SdmDivicion sdmDivicion";

	private static final String[] RESTRICTIONS = {
			"lower(sdmDivicion.codigo) like lower(concat(#{sdmDivicionList.sdmDivicion.codigo},'%'))",
			"lower(sdmDivicion.nombre) like lower(concat(#{sdmDivicionList.sdmDivicion.nombre},'%'))", };

	private SdmDivicion sdmDivicion = new SdmDivicion();

	public SdmDivicionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public SdmDivicion getSdmDivicion() {
		return sdmDivicion;
	}
}
