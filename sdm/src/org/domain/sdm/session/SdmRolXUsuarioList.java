package org.domain.sdm.session;

import org.domain.sdm.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("sdmRolXUsuarioList")
public class SdmRolXUsuarioList extends EntityQuery<SdmRolXUsuario> {

	private static final String EJBQL = "select sdmRolXUsuario from SdmRolXUsuario sdmRolXUsuario";

	private static final String[] RESTRICTIONS = {};

	private SdmRolXUsuario sdmRolXUsuario;

	public SdmRolXUsuarioList() {
		sdmRolXUsuario = new SdmRolXUsuario();
		sdmRolXUsuario.setId(new SdmRolXUsuarioId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public SdmRolXUsuario getSdmRolXUsuario() {
		return sdmRolXUsuario;
	}
}
