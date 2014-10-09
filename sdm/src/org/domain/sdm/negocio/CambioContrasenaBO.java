package org.domain.sdm.negocio;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.domain.sdm.entity.SdmEmpleado;
import org.domain.sdm.entity.SdmUsuario;
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

	@In 
    StatusMessages statusMessages;

	@In
	SdmEmpleado sdmEmpleado;
	
	@In
	@Out
	SdmUsuario sdmUsuario;
	
	@In(create=true)
	SdmUsuarioHome sdmUsuarioHome;

	public String cambiarContrasena(){
		sdmUsuario.setContrasena(sdmUsuario.getContrasena().trim());
		if (sdmUsuario.getContrasena().isEmpty()) {
			statusMessages.add("Ingrese una contraseña no vacia");
			return "/cambioContrasena.xhtml";
		}
		sdmUsuarioHome.actualizarUsuario(sdmUsuario);
		statusMessages.add("Contraseña cambiada");
		return "/home.xhtml";
	}

}