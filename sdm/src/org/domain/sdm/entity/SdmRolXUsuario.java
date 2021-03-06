package org.domain.sdm.entity;

// Generated Jul 16, 2014 12:05:59 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.validator.NotNull;

/**
 * SdmRolXUsuario generated by hbm2java
 */
@Entity
@Table(name = "SDM_rol_x_usuario")
public class SdmRolXUsuario implements java.io.Serializable {

	
	private SdmRolXUsuarioId id;
	private SdmUsuario sdmUsuario;
	private SdmRol sdmRol;

	public SdmRolXUsuario() {
	}

	public SdmRolXUsuario(SdmRolXUsuarioId id, SdmUsuario sdmUsuario,
			SdmRol sdmRol) {
		this.id = id;
		this.sdmUsuario = sdmUsuario;
		this.sdmRol = sdmRol;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "idRol", column = @Column(name = "id_rol", nullable = false)),
			@AttributeOverride(name = "idUsuario", column = @Column(name = "id_usuario", nullable = false)) })
	@NotNull
	public SdmRolXUsuarioId getId() {
		return this.id;
	}

	public void setId(SdmRolXUsuarioId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_usuario", nullable = false, insertable = false, updatable = false)
	@NotNull
	public SdmUsuario getSdmUsuario() {
		return this.sdmUsuario;
	}

	public void setSdmUsuario(SdmUsuario sdmUsuario) {
		this.sdmUsuario = sdmUsuario;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_rol", nullable = false, insertable = false, updatable = false)
	@NotNull
	public SdmRol getSdmRol() {
		return this.sdmRol;
	}

	public void setSdmRol(SdmRol sdmRol) {
		this.sdmRol = sdmRol;
	}

}
