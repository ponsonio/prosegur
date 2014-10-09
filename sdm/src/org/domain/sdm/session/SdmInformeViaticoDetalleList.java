package org.domain.sdm.session;

import org.domain.sdm.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("sdmInformeViaticoDetalleList")
public class SdmInformeViaticoDetalleList extends
		EntityQuery<SdmInformeViaticoDetalle> {

	private static final String EJBQL = "select sdmInformeViaticoDetalle from SdmInformeViaticoDetalle sdmInformeViaticoDetalle";

	private static final String[] RESTRICTIONS = {
			"lower(sdmInformeViaticoDetalle.descripcion) like lower(concat(#{sdmInformeViaticoDetalleList.sdmInformeViaticoDetalle.descripcion},'%'))",
			"lower(sdmInformeViaticoDetalle.destino) like lower(concat(#{sdmInformeViaticoDetalleList.sdmInformeViaticoDetalle.destino},'%'))", };

	private SdmInformeViaticoDetalle sdmInformeViaticoDetalle = new SdmInformeViaticoDetalle();

	public SdmInformeViaticoDetalleList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public SdmInformeViaticoDetalle getSdmInformeViaticoDetalle() {
		return sdmInformeViaticoDetalle;
	}
}
