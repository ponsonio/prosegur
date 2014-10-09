package org.domain.sdm.session;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.domain.sdm.entity.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("sdmInformeViaticoDetalleHome")
public class SdmInformeViaticoDetalleHome extends
		EntityHome<SdmInformeViaticoDetalle> {

	@In(create = true)
	SdmEmpresaHome sdmEmpresaHome;
	@In(create = true)
	SdmInformeViaticosHome sdmInformeViaticosHome;
	@In(create = true)
	SdmTipoServicioHome sdmTipoServicioHome;
	@In(create = true)
	SdmEmpleadoHome sdmEmpleadoHome;
	@In(create = true)
	SdmDivicionHome sdmDivicionHome;
	@In(create = true)
	SdmCentroCostoHome sdmCentroCostoHome;
	@In(create = true)
	SdmDelegacionHome sdmDelegacionHome;

	@In
	private EntityManager entityManager;
	
	public void setSdmInformeViaticoDetalleId(Long id) {
		setId(id);
	}

	public Long getSdmInformeViaticoDetalleId() {
		return (Long) getId();
	}

	@Override
	protected SdmInformeViaticoDetalle createInstance() {
		SdmInformeViaticoDetalle sdmInformeViaticoDetalle = new SdmInformeViaticoDetalle();
		return sdmInformeViaticoDetalle;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		SdmEmpresa sdmEmpresa = sdmEmpresaHome.getDefinedInstance();
		if (sdmEmpresa != null) {
			getInstance().setSdmEmpresa(sdmEmpresa);
		}
		SdmInformeViaticos sdmInformeViaticos = sdmInformeViaticosHome
				.getDefinedInstance();
		if (sdmInformeViaticos != null) {
			getInstance().setSdmInformeViaticos(sdmInformeViaticos);
		}
		SdmTipoServicio sdmTipoServicio = sdmTipoServicioHome
				.getDefinedInstance();
		if (sdmTipoServicio != null) {
			getInstance().setSdmTipoServicio(sdmTipoServicio);
		}
		SdmEmpleado sdmEmpleado = sdmEmpleadoHome.getDefinedInstance();
		if (sdmEmpleado != null) {
			getInstance().setSdmEmpleado(sdmEmpleado);
		}
		SdmDivicion sdmDivicion = sdmDivicionHome.getDefinedInstance();
		if (sdmDivicion != null) {
			getInstance().setSdmDivicion(sdmDivicion);
		}
		SdmCentroCosto sdmCentroCosto = sdmCentroCostoHome.getDefinedInstance();
		if (sdmCentroCosto != null) {
			getInstance().setSdmCentroCosto(sdmCentroCosto);
		}
		SdmDelegacion sdmDelegacion = sdmDelegacionHome.getDefinedInstance();
		if (sdmDelegacion != null) {
			getInstance().setSdmDelegacion(sdmDelegacion);
		}
	}

	public boolean isWired() {
		if (getInstance().getSdmEmpresa() == null)
			return false;
		if (getInstance().getSdmInformeViaticos() == null)
			return false;
		if (getInstance().getSdmTipoServicio() == null)
			return false;
		if (getInstance().getSdmEmpleado() == null)
			return false;
		if (getInstance().getSdmDivicion() == null)
			return false;
		if (getInstance().getSdmCentroCosto() == null)
			return false;
		if (getInstance().getSdmDelegacion() == null)
			return false;
		return true;
	}

	public SdmInformeViaticoDetalle getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public BigDecimal obtenerTotalDetalleXCCXTipoActivosFechas(String CodCC , long idTipoServicio, Date fechaIni , Date fechaHasta){
		Query query =  entityManager.createQuery("select sum(d.monto) from SdmInformeViaticoDetalle d where d.sdmCentroCosto.codigo =:CodCC and d.sdmTipoServicio.id =:idTipoServicio and d.sdmInformeViaticos.anulado = false and d.fecha >= :fechaIni and d.fecha <= :fechaHasta ");
		return (BigDecimal)query.setParameter("CodCC",CodCC)
				.setParameter("idTipoServicio", idTipoServicio)
				.setParameter("fechaIni",fechaIni)
				.setParameter("fechaHasta",fechaHasta).getSingleResult();
	}
	
}
