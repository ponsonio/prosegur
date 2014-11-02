package org.domain.sdm.session;

import org.domain.sdm.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("sdmParametroList")
public class SdmParametroList extends EntityQuery<SdmParametro> {

	private static final String EJBQL = "select sdmParametro from SdmParametro sdmParametro";

	private static final String[] RESTRICTIONS = {
			"lower(sdmParametro.nombre) like lower(concat(#{sdmParametroList.sdmParametro.nombre},'%'))",
			"lower(sdmParametro.valor) like lower(concat(#{sdmParametroList.sdmParametro.valor},'%'))", };

	private SdmParametro sdmParametro = new SdmParametro();

	public SdmParametroList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public SdmParametro getSdmParametro() {
		return sdmParametro;
	}
}
