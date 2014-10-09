package org.domain.sdm.session;

import org.domain.sdm.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("sdmEmpresaList")
public class SdmEmpresaList extends EntityQuery<SdmEmpresa> {

	private static final String EJBQL = "select sdmEmpresa from SdmEmpresa sdmEmpresa";

	private static final String[] RESTRICTIONS = {
			"lower(sdmEmpresa.codigo) like lower(concat(#{sdmEmpresaList.sdmEmpresa.codigo},'%'))",
			"lower(sdmEmpresa.nombre) like lower(concat(#{sdmEmpresaList.sdmEmpresa.nombre},'%'))", };

	private SdmEmpresa sdmEmpresa = new SdmEmpresa();

	public SdmEmpresaList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public SdmEmpresa getSdmEmpresa() {
		return sdmEmpresa;
	}
}
