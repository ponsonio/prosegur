package org.domain.sdm.session;

import org.domain.sdm.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("sdmTipoServicioList")
public class SdmTipoServicioList extends EntityQuery<SdmTipoServicio> {

	private static final String EJBQL = "select sdmTipoServicio from SdmTipoServicio sdmTipoServicio";

	private static final String[] RESTRICTIONS = { "lower(sdmTipoServicio.descripcion) like lower(concat(#{sdmTipoServicioList.sdmTipoServicio.descripcion},'%'))", };

	private SdmTipoServicio sdmTipoServicio = new SdmTipoServicio();

	public SdmTipoServicioList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public SdmTipoServicio getSdmTipoServicio() {
		return sdmTipoServicio;
	}
}
