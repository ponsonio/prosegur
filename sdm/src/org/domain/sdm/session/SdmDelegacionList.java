package org.domain.sdm.session;

import org.domain.sdm.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("sdmDelegacionList")
public class SdmDelegacionList extends EntityQuery<SdmDelegacion> {

	private static final String EJBQL = "select sdmDelegacion from SdmDelegacion sdmDelegacion";

	private static final String[] RESTRICTIONS = {
			"lower(sdmDelegacion.codigo) like lower(concat(#{sdmDelegacionList.sdmDelegacion.codigo},'%'))",
			"lower(sdmDelegacion.nombre) like lower(concat(#{sdmDelegacionList.sdmDelegacion.nombre},'%'))", };

	private SdmDelegacion sdmDelegacion = new SdmDelegacion();

	public SdmDelegacionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public SdmDelegacion getSdmDelegacion() {
		return sdmDelegacion;
	}
}
