package org.domain.sdm.session;

import org.domain.sdm.entity.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("sdmRolXUsuarioHome")
public class SdmRolXUsuarioHome extends EntityHome<SdmRolXUsuario> {

	@In(create = true)
	SdmUsuarioHome sdmUsuarioHome;
	@In(create = true)
	SdmRolHome sdmRolHome;

	public void setSdmRolXUsuarioId(SdmRolXUsuarioId id) {
		setId(id);
	}

	public SdmRolXUsuarioId getSdmRolXUsuarioId() {
		return (SdmRolXUsuarioId) getId();
	}

	public SdmRolXUsuarioHome() {
		setSdmRolXUsuarioId(new SdmRolXUsuarioId());
	}

	@Override
	public boolean isIdDefined() {
		if (getSdmRolXUsuarioId().getIdRol() == 0)
			return false;
		if (getSdmRolXUsuarioId().getIdUsuario() == 0)
			return false;
		return true;
	}

	@Override
	protected SdmRolXUsuario createInstance() {
		SdmRolXUsuario sdmRolXUsuario = new SdmRolXUsuario();
		sdmRolXUsuario.setId(new SdmRolXUsuarioId());
		return sdmRolXUsuario;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		SdmUsuario sdmUsuario = sdmUsuarioHome.getDefinedInstance();
		if (sdmUsuario != null) {
			getInstance().setSdmUsuario(sdmUsuario);
		}
		SdmRol sdmRol = sdmRolHome.getDefinedInstance();
		if (sdmRol != null) {
			getInstance().setSdmRol(sdmRol);
		}
	}

	public boolean isWired() {
		if (getInstance().getSdmUsuario() == null)
			return false;
		if (getInstance().getSdmRol() == null)
			return false;
		return true;
	}

	public SdmRolXUsuario getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
