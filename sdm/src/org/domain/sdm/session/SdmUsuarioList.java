package org.domain.sdm.session;

import org.domain.sdm.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("sdmUsuarioList")
public class SdmUsuarioList extends EntityQuery<SdmUsuario> {

	private static final String EJBQL = "select sdmUsuario from SdmUsuario sdmUsuario";

	private static final String[] RESTRICTIONS = {
			"lower(sdmUsuario.contrasena) like lower(concat(#{sdmUsuarioList.sdmUsuario.contrasena},'%'))",
			"lower(sdmUsuario.correo) like lower(concat(#{sdmUsuarioList.sdmUsuario.correo},'%'))", };

	private SdmUsuario sdmUsuario = new SdmUsuario();

	public SdmUsuarioList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public SdmUsuario getSdmUsuario() {
		return sdmUsuario;
	}
}
