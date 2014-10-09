package org.domain.sdm.session;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.domain.sdm.entity.SdmEmpleado;
import org.domain.sdm.entity.SdmLog;
import org.domain.sdm.entity.SdmRolXUsuario;
import org.domain.sdm.entity.SdmUsuario;
import org.domain.sdm.negocio.LoggerBO;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;

@Name("authenticator")
@Scope(ScopeType.SESSION)
public class Authenticator implements Serializable {

	private static final long serialVersionUID = 7573483855386410083L;

	@Logger
	private Log log;
	
	@In(create= true)
	LoggerBO loggerBO ;

	@In
	StatusMessages statusMessages;

	@In
	Identity identity;

	@In
	Credentials credentials;

	@In(create = true)
	@Out
	SdmUsuario sdmUsuario;

	@In(create = true)
	@Out
	SdmEmpleado sdmEmpleado;

	@In(create = true)
	SdmUsuarioHome sdmUsuarioHome;

	public boolean authenticate() {
		// obtengo el usuario
		SdmUsuario usr;
		try {
			usr = sdmUsuarioHome.obtenerSdmUsuario(credentials.getUsername());
			sdmUsuario = usr;
		} catch (NoResultException nre) {
			// log.info("Usuario no encontrado");
			statusMessages
					.add(Severity.ERROR,
							"No se encontro su usario, por favor comunicarse con el área de soporte");
			return false;
		} catch (NonUniqueResultException nue) {
			// TODO: handle exception
			log.error("Mas de un registro de usuario"
					+ credentials.getUsername());
			throw nue;
		}

		// Valido contraseña
		if (usr != null
				&& usr.getContrasena().equals(credentials.getPassword())) {

			sdmEmpleado = usr.getSdmEmpleado(); //
			log.info("usuario:" + credentials.getUsername()); //no quitar o se pudre todo!
			log.info("sdmEmpleado"+ sdmEmpleado.getNombre()); //no quitar o se pudre todo!
			Set<SdmRolXUsuario> lst_rol_x_usr = (Set<SdmRolXUsuario>) usr
					.getSdmRolXUsuarios();
			//Si no tiene roles
			if (lst_rol_x_usr.size() == 0) {
				log.info("Usuario sin rol" + credentials.getUsername());
				statusMessages
						.add(Severity.ERROR,
								"No se encontraron roles para su usario, por favor comunicarse con área de soporte");
				return false;
			}
			// cargo los roles
			Iterator<SdmRolXUsuario> it = lst_rol_x_usr.iterator();
			SdmRolXUsuario sdmRol_x_usr;
			while (it.hasNext()) {
				sdmRol_x_usr = it.next();
				identity.addRole(sdmRol_x_usr.getSdmRol().getEtiqueta());
			}
			SdmLog sdmLog = new SdmLog();
			sdmLog.setMensaje("Ingreso al sistema");
			sdmLog.setNombreClase(this.getClass().getCanonicalName());
			sdmLog.setReferencia(credentials.getUsername());
			sdmLog.setUsuario(credentials.getUsername());
			sdmLog.setTipo("Evento");
			sdmLog.setOperacion("Login");
			loggerBO.ingresarLog(sdmLog);
			return true;
		}
		statusMessages.add(Severity.ERROR, "Contraseña Errada");
		return false;
	}

}
