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

	String Login = "Login";
	
	
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
							"No se encontro su usuario, por favor comunicarse con el área de soporte");
			
			loggerBO.ingresarLogUsr(this.getClass().getCanonicalName(), 
					"No se encontro su usuario", this.Login, credentials.getUsername(), LoggerBO.EVENTO, credentials.getUsername());
			
			return false;
		} catch (NonUniqueResultException nue) {
			// TODO: handle exception
			loggerBO.ingresarLogUsr(this.getClass().getCanonicalName(), 
					"No se encontro su usuario", this.Login, credentials.getUsername(), LoggerBO.EVENTO, credentials.getUsername());
			throw nue;
		}

		
		// Valido contraseña
		if (usr != null
				&& usr.getContrasena().equals(credentials.getPassword())) {

			sdmEmpleado = usr.getSdmEmpleado(); //
			System.out.println("usuario:" + credentials.getUsername()); //no quitar 
			System.out.println("sdmEmpleado"+ sdmEmpleado.getNombre()); //no quitar 
			Set<SdmRolXUsuario> lst_rol_x_usr = (Set<SdmRolXUsuario>) usr
					.getSdmRolXUsuarios();
			//Si no tiene roles
			if (lst_rol_x_usr.size() == 0) {
				loggerBO.ingresarLogUsr(this.getClass().getCanonicalName(), 
						"Usuario sin Rol Asignado", this.Login, credentials.getUsername(), LoggerBO.ERROR, credentials.getUsername());
				//log.info("Usuario sin rol" + credentials.getUsername());
				statusMessages
						.add(Severity.ERROR,
								"No se encontraron roles para su usuario, por favor comunicarse con área de soporte");
				return false;
			}
			// cargo los roles
			Iterator<SdmRolXUsuario> it = lst_rol_x_usr.iterator();
			SdmRolXUsuario sdmRol_x_usr;
			while (it.hasNext()) {
				sdmRol_x_usr = it.next();
				identity.addRole(sdmRol_x_usr.getSdmRol().getEtiqueta());
			}
			loggerBO.ingresarLogUsr(this.getClass().getCanonicalName(), 
					"Inicio Sesión", this.Login, credentials.getUsername(), LoggerBO.EVENTO, credentials.getUsername());
			return true;
		}
		loggerBO.ingresarLogUsr(this.getClass().getCanonicalName(), 
				"Contraseña Errada", this.Login, credentials.getUsername(), LoggerBO.ERROR, credentials.getUsername());
		statusMessages.add(Severity.ERROR, "Contraseña Errada");
		return false;
	}
	


}
