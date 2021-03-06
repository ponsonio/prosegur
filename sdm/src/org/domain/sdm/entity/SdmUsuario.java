package org.domain.sdm.entity;

// Generated Jul 16, 2014 12:05:59 PM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TemporalType;


import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 * SdmUsuario generated by hbm2java
 */
@Entity
@Table(name = "SDM_usuario")
@Name("sdmUsuario")
@Scope(ScopeType.SESSION)
public class SdmUsuario implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8706192874178723813L;
	private long id;
	private SdmEmpleado sdmEmpleado;
	private byte[] contrasena;
	private String correo;
	private boolean activo ;
	private Date fechaModContrasena;
	private Set<SdmRolXUsuario> sdmRolXUsuarios = new HashSet<SdmRolXUsuario>(0);

	public SdmUsuario() {
	}

	public SdmUsuario(long id, SdmEmpleado sdmEmpleado, byte[] contrasena,
			String correo, boolean activo , Date fechaModContrasena) {
		this.id = id;
		this.sdmEmpleado = sdmEmpleado;
		this.contrasena = contrasena;
		this.correo = correo;
		this.activo = activo;
	}

	public SdmUsuario(long id, SdmEmpleado sdmEmpleado, byte[] contrasena,
			String correo, boolean activo, Set<SdmRolXUsuario> sdmRolXUsuarios) {
		this.id = id;
		this.sdmEmpleado = sdmEmpleado;
		this.contrasena = contrasena;
		this.correo = correo;
		this.activo = activo;
		this.sdmRolXUsuarios = sdmRolXUsuarios;
		this.fechaModContrasena = fechaModContrasena;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_empleado", nullable = false)
	@NotNull
	public SdmEmpleado getSdmEmpleado() {
		return this.sdmEmpleado;
	}

	public void setSdmEmpleado(SdmEmpleado sdmEmpleado) {
		this.sdmEmpleado = sdmEmpleado;
	}


	@Column(name = "contrasena", nullable = false)
	@NotNull
	public byte[] getContrasena() {
		return this.contrasena;
	}

	public void setContrasena(byte[] contrasena) {
		this.contrasena = contrasena;
	}
	
	

	@Column(name = "correo", nullable = false, length = 50)
	@NotNull
	@Length(max = 50)
	public String getCorreo() {
		return this.correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	@Column(name = "activo", nullable = false)
	public boolean isActivo() {
		return this.activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	@javax.persistence.Temporal(TemporalType.DATE)
	@Column(name = "fecha_mod_contrasena", nullable = false, length = 10)
	@NotNull
	public Date getFechaModContrasena() {
		return this.fechaModContrasena;
	}

	public void setFechaModContrasena(Date fechaModContrasena) {
		this.fechaModContrasena = fechaModContrasena;
	}
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "sdmUsuario")
	public Set<SdmRolXUsuario> getSdmRolXUsuarios() {
		return this.sdmRolXUsuarios;
	}

	public void setSdmRolXUsuarios(Set<SdmRolXUsuario> sdmRolXUsuarios) {
		this.sdmRolXUsuarios = sdmRolXUsuarios;
	}

}
