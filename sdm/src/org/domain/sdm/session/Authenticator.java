package org.domain.sdm.session;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.domain.sdm.entity.SdmEmpleado;
import org.domain.sdm.entity.SdmLog;
import org.domain.sdm.entity.SdmRolXUsuario;
import org.domain.sdm.entity.SdmUsuario;
import org.domain.sdm.negocio.LoggerBO;
import org.domain.sdm.negocio.ParametroBO;
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
	
	boolean contrasenaExpirada = false;
	
	int intentos = 0 ;
	
	int diasExpira = 0;
	
	String login_ant = null;
	
	@In(create= true)
	LoggerBO loggerBO ;
	
	@In(create= true)
	ParametroBO parametroBO;

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

	public boolean authenticate() throws Exception {
		// obtengo el usuario
		SdmUsuario usr;
		try {
			ArrayList<SdmUsuario> arrayLisSdmUsuarios = (ArrayList<SdmUsuario>)sdmUsuarioHome.obtenerSdmUsuario(credentials.getUsername());
			
			if(arrayLisSdmUsuarios.size() == 0) {
				loggerBO.ingresarLogUsr(this.getClass().getCanonicalName(), 
						"Sin usuario", this.Login, credentials.getUsername(), LoggerBO.ERROR, credentials.getUsername());
				statusMessages
						.add(Severity.ERROR,
								"No se encontró su usuario");
				return false;
			}
			
			if(arrayLisSdmUsuarios.size() > 1) {
				loggerBO.ingresarLogUsr(this.getClass().getCanonicalName(), 
						"Se encontró mas de un usuario", this.Login, credentials.getUsername(), LoggerBO.ERROR, credentials.getUsername());
				statusMessages
						.add(Severity.ERROR,
								"Se encontró mas de un usuario");
				return false;
			}
			
			usr = arrayLisSdmUsuarios.get(0) ;
			sdmUsuario = usr;

		} catch (Exception e) {
			// TODO: handle exception
			loggerBO.ingresarLogUsr(this.getClass().getCanonicalName(), 
					"Error obteniendo usuario", this.Login, credentials.getUsername(), LoggerBO.EVENTO, credentials.getUsername());
			throw e;
		}

		diasExpira = parametroBO.obtenerNumeroDiasExpiraContrasena();
		String password = new  String(usr.getContrasena(),"UTF-8");
		//System.out.print("password:"+password);
		System.out.print(" parametro diasExpira:"+diasExpira);
		//System.out.print("intentos:"+this.intentos);
		// Valido contraseña
		if (password.equals(credentials.getPassword())) {

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
			this.contrasenaExpirada = validaExpira();
			System.out.println("contrasenaExpirada"+contrasenaExpirada);
			if (this.contrasenaExpirada) {
				statusMessages.add(Severity.ERROR , "Su contraseña expiró, por favor cambiarla");
				loggerBO.ingresarLogUsr(this.getClass().getCanonicalName(), 
						"Contraseña Expirada", this.Login, credentials.getUsername(), LoggerBO.EVENTO, credentials.getUsername());
			}
			
			loggerBO.ingresarLogUsr(this.getClass().getCanonicalName(), 
					"Inicio Sesión", this.Login, credentials.getUsername(), LoggerBO.EVENTO, credentials.getUsername());
			return true;
		}
		
		if (credentials.getUsername().equals(this.login_ant) ){
			this.intentos ++;
		}
		
		loggerBO.ingresarLogUsr(this.getClass().getCanonicalName(), 
				"Contraseña Errada", this.Login, credentials.getUsername(), LoggerBO.ERROR, credentials.getUsername());
		statusMessages.add(Severity.ERROR, "Contraseña Errada");
		return false;
	}

	
	



	public boolean isContrasenaExpirada() {
		return contrasenaExpirada;
	}






	public void setContrasenaExpirada(boolean contrasenaExpirada) {
		this.contrasenaExpirada = contrasenaExpirada;
	}






	public int getIntentos() {
		return intentos;
	}



	public void setIntentos(int intentos) {
		this.intentos = intentos;
	}



	public String getLogin_ant() {
		return login_ant;
	}



	public void setLogin_ant(String login_ant) {
		this.login_ant = login_ant;
	}
	

	public boolean validaExpira(){
		Calendar c = Calendar.getInstance(); 
		c.setTime(this.sdmUsuario.getFechaModContrasena()); 
		c.add(Calendar.DATE, diasExpira);
		Date fechaExpira = c.getTime();
		Date hoy = new Date();
		System.out.println("fechaExpira:" +fechaExpira.toLocaleString());
		System.out.println("hoy:"+ hoy.toLocaleString());
		if (fechaExpira.before(hoy) ){
			System.out.println(" contraseña expirada");
			return true;	
		}
		System.out.println(" contraseña No expirada");
		return false;
	}
	
	
	public void logout() throws Exception{
		try{
			loggerBO.ingresarLogUsr(this.getClass().getCanonicalName(), 
				"Cierre de Sesión", this.Login, credentials.getUsername(), LoggerBO.EVENTO, credentials.getUsername());
		
			identity.logout();
		}catch(Exception e){
			// TODO: handle exception
			loggerBO.ingresarLogUsr(this.getClass().getCanonicalName(), 
					"Error en logout", this.Login, credentials.getUsername(), LoggerBO.EVENTO, credentials.getUsername());
			throw e;
		}
	}
	
	
	
	
	
}
