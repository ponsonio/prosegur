package org.domain.sdm.session;

import org.domain.sdm.entity.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("sdmUsuarioHome")
public class SdmUsuarioHome extends EntityHome<SdmUsuario> {

	@In(create = true)
	SdmEmpleadoHome sdmEmpleadoHome;
	
	@In
	private EntityManager entityManager;

	public void setSdmUsuarioId(Long id) {
		setId(id);
	}

	public Long getSdmUsuarioId() {
		return (Long) getId();
	}

	@Override
	protected SdmUsuario createInstance() {
		SdmUsuario sdmUsuario = new SdmUsuario();
		return sdmUsuario;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		SdmEmpleado sdmEmpleado = sdmEmpleadoHome.getDefinedInstance();
		if (sdmEmpleado != null) {
			getInstance().setSdmEmpleado(sdmEmpleado);
		}
	}

	public boolean isWired() {
		if (getInstance().getSdmEmpleado() == null)
			return false;
		return true;
	}

	public SdmUsuario getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<SdmRolXUsuario> getSdmRolXUsuarios() {
		return getInstance() == null ? null : new ArrayList<SdmRolXUsuario>(
				getInstance().getSdmRolXUsuarios());
	}

	
	/**
	 * Retorna un el usuario , de el empleado cuyo codigo es enviado
	 * @param usr
	 * @return
	 */
	public SdmUsuario obtenerSdmUsuario(String usr){
		Query query =  entityManager.createQuery("select u From SdmEmpleado e , SdmUsuario u where e.id = u.sdmEmpleado.id and e.codigo = :usr and e.activo= true");
		return (SdmUsuario)query.setParameter("usr",usr).getSingleResult();
	}

	
	/**
	 * Retorna un el usuario , de el empleado cuyo codigo es enviado
	 * @param usr
	 * @return
	 */
	public SdmUsuario obtenerSdmUsuarioXCodigo(String usr){
		try{
			Query query =  entityManager.createQuery("select u From SdmEmpleado e , SdmUsuario u where e.id = u.sdmEmpleado.id and e.codigo = :usr");
			return (SdmUsuario)query.setParameter("usr",usr).getSingleResult();
		}catch(NoResultException nre){
			return null;
		}
	}
	
	
	public SdmUsuario actualizarUsuario(SdmUsuario sdmUsuario) {
		entityManager.persist(sdmUsuario);
		return sdmUsuario;
	}	

	
}
