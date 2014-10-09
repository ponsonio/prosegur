package org.domain.sdm.session;

import org.domain.sdm.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("sdmEmpleadoList")
public class SdmEmpleadoList extends EntityQuery<SdmEmpleado> {

	private static final String EJBQL = "select sdmEmpleado from SdmEmpleado sdmEmpleado";

	private static final String[] RESTRICTIONS = {
			"lower(sdmEmpleado.nombre) like lower(concat(#{sdmEmpleadoList.sdmEmpleado.nombre},'%'))",
			"lower(sdmEmpleado.codigo) like lower(concat(#{sdmEmpleadoList.sdmEmpleado.codigo},'%'))", };

	private SdmEmpleado sdmEmpleado = new SdmEmpleado();

	public SdmEmpleadoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public SdmEmpleado getSdmEmpleado() {
		return sdmEmpleado;
	}
}
