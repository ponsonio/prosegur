package org.domain.sdm.session;

import org.domain.sdm.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("sdmRolList")
public class SdmRolList extends EntityQuery<SdmRol> {

	private static final String EJBQL = "select sdmRol from SdmRol sdmRol";

	private static final String[] RESTRICTIONS = { "lower(sdmRol.etiqueta) like lower(concat(#{sdmRolList.sdmRol.etiqueta},'%'))", };

	private SdmRol sdmRol = new SdmRol();

	public SdmRolList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public SdmRol getSdmRol() {
		return sdmRol;
	}
}
