package org.domain.sdm.session;

import org.domain.sdm.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("sdmLogList")
public class SdmLogList extends EntityQuery<SdmLog> {

	private static final String EJBQL = "select sdmLog from SdmLog sdmLog";

	private static final String[] RESTRICTIONS = {
			"lower(sdmLog.ip) like lower(concat(#{sdmLogList.sdmLog.ip},'%'))",
			"lower(sdmLog.usuario) like lower(concat(#{sdmLogList.sdmLog.usuario},'%'))",
			"lower(sdmLog.operacion) like lower(concat(#{sdmLogList.sdmLog.operacion},'%'))",
			"lower(sdmLog.mensaje) like lower(concat(#{sdmLogList.sdmLog.mensaje},'%'))",
			"lower(sdmLog.tipo) like lower(concat(#{sdmLogList.sdmLog.tipo},'%'))",
			"lower(sdmLog.nombreClase) like lower(concat(#{sdmLogList.sdmLog.nombreClase},'%'))",
			"lower(sdmLog.referencia) like lower(concat(#{sdmLogList.sdmLog.referencia},'%'))", };

	private SdmLog sdmLog = new SdmLog();

	public SdmLogList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public SdmLog getSdmLog() {
		return sdmLog;
	}
}
