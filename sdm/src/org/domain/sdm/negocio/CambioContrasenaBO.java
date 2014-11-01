package org.domain.sdm.negocio;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Date;

import javax.persistence.EntityManager;

import org.domain.sdm.entity.SdmEmpleado;
import org.domain.sdm.entity.SdmUsuario;
import org.domain.sdm.session.Authenticator;
import org.domain.sdm.session.SdmUsuarioHome;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.StatusMessages;

@Scope(ScopeType.SESSION)
@Name("cambioContrasenaBO")
public class CambioContrasenaBO  implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String cambiarContrasena = "Cambiar contraseña ";
	
	@In(create= true)
	LoggerBO loggerBO ;
	
	
	@In 
    StatusMessages statusMessages;
	
	@In
	Authenticator authenticator;

	@In
	SdmEmpleado sdmEmpleado;
	
	@In
	@Out
	SdmUsuario sdmUsuario;
	
	@In(create=true)
	SdmUsuarioHome sdmUsuarioHome;

	String stringContrasena = "";
	
	
	




	public String getStringContrasena() {
		return stringContrasena;
	}







	public void setStringContrasena(String stringContrasena) {
		this.stringContrasena = stringContrasena;
	}







	/**
	 *	Cambio de contraseña 
	 * @return
	 */
	public String cambiarContrasena() throws Exception{
		try{
			this.stringContrasena = this.stringContrasena.trim();
			if (stringContrasena.isEmpty()) {
				statusMessages.add("Ingrese una contraseña no vacia");
				return "/cambioContrasena.xhtml";
			}
		
			sdmUsuario =  sdmUsuarioHome.obtenerUsuarioXid(sdmUsuario.getId());
			
			sdmUsuario.setFechaModContrasena(new Date());
			Charset UTF8_CHARSET = Charset.forName("UTF-8");
			
			//if (UTF8_CHARSET == null) System.out.print("charset null");
			
			sdmUsuario.setContrasena(this.stringContrasena.getBytes(UTF8_CHARSET));
			sdmUsuarioHome.actualizarUsuario(sdmUsuario);
			
			loggerBO.ingresarRegistroEvento(this.getClass().getCanonicalName(), 
						"Se cambio la contraseña " , this.cambiarContrasena , String.valueOf(sdmUsuario.getId()) );
	
			authenticator.setContrasenaExpirada(false);
			statusMessages.add("Contraseña cambiada");
			return "/home.xhtml";
		 }catch (Exception e){
			 statusMessages.add("Ocurrió un error Cambiando la Contraseña");
			 loggerBO.ingresarRegistroError(this.getClass().getCanonicalName(), 
						e.getMessage(),"Cambio contraseña", null);
			 throw e ;
		 }	
	}

}