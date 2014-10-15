package org.domain.sdm.negocio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;


import org.domain.sdm.entity.SdmEmpleado;
import org.domain.sdm.entity.SdmInformeViaticos;
import org.domain.sdm.entity.SdmLiquidacionInformeViaticos;
import org.domain.sdm.entity.SdmUsuario;
import org.domain.sdm.session.SdmEmpleadoHome;
import org.domain.sdm.session.SdmInformeViaticosHome;
import org.domain.sdm.session.SdmLiquidacionInformeViaticosHome;
import org.domain.sdm.vista.bean.VistaSdmInformeViaticos;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;

@Name("anulacionInformeBO")
@Scope(ScopeType.SESSION)
/**
 * Clase encargada de los metodos de negocio de la anulación de un informe
 * @author jcabrera
 *
 */
public class AnulacionInformeBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8499272177185240449L;

	String anulacionInforme = "Anulación Informe";

	@In
	SdmEmpleado sdmEmpleado;

	@In
	StatusMessages statusMessages;

	@In(create = true)
	LoggerBO loggerBO;

	@In(create = true)
	SdmLiquidacionInformeViaticosHome sdmLiquidacionInformeViaticosHome;

	@Logger
	private Log log;

	SdmEmpleado sdmEmpleadoSelect = new SdmEmpleado();

	String codigoBusqueda;

	private String tabSelect;

	SdmUsuario sdmUsuarioSelect = new SdmUsuario();

	private Date fechaDesde = new Date();

	private Date fechaHasta = new Date();

	@DataModel
	List<VistaSdmInformeViaticos> listvistaSdmInformeViaticos;

	@In(create = true)
	SdmEmpleadoHome sdmEmpleadoHome;

	@In(create = true)
	SdmInformeViaticosHome sdmInformeViaticosHome;

	@Factory("arrayListSdmEmpleadoRolEmisor")
	public List<SdmEmpleado> buscarEmpleadosEmisores() throws Exception {
		try {
			return sdmEmpleadoHome.listaEmpleadosEmisores();
		} catch (Exception e) {
			loggerBO.ingresarRegistroError(this.getClass().getCanonicalName(),
					e.getMessage(), "Busqueda empleado", null);
			throw e;
		}
	}

	/**
	 * Busca las liquidaciones por correlativo o genera un mensaje de error
	 * 
	 * @return url de página de anulación
	 */
	public String buscarLiquidacionXCorrelativo() throws Exception {
		listvistaSdmInformeViaticos = new ArrayList<VistaSdmInformeViaticos>();
		List<SdmInformeViaticos> listRSdmInformeViaticos = new ArrayList<SdmInformeViaticos>();
		this.codigoBusqueda = this.codigoBusqueda.trim();
		SdmInformeViaticos informe;
		// sdmEmpleadoSelect = new SdmEmpleado();
		try {

			informe = sdmInformeViaticosHome.buscarInformeXCorrelativo(Long
					.parseLong(this.codigoBusqueda));
			if (informe.isAnulado()) {
				log.info("Infome ya anulado" + this, codigoBusqueda);
				statusMessages
						.add(Severity.ERROR, "El informe ya esta anulado");
				return "/anulacionInforme.xhtml";
			}
		} catch (NoResultException nre) {
			log.info("Infome no encontrado" + this, codigoBusqueda);
			statusMessages.add(Severity.ERROR, "No se encontro el informe");
			return "/anulacionInforme.xhtml";
		} catch (NonUniqueResultException nue) {
			statusMessages
					.add(Severity.ERROR,
							"Se encontró más de un informe, por favor comunicarse con el área de soporte");
			log.error("Mas de un registro de informe" + this, codigoBusqueda);
			return "/anulacionInforme.xhtml";
		} catch (NumberFormatException e) {
			statusMessages
					.add(Severity.ERROR, "Debe ingresar un número valido");
			return "/anulacionInforme.xhtml";
			// TODO: handle exception
		}

		try {
			SdmLiquidacionInformeViaticos liquidacion = sdmLiquidacionInformeViaticosHome
					.buscarLiquidacionInforme(informe.getId());
			if (liquidacion != null) {
				statusMessages.add(Severity.ERROR,
						"El informe ya fue liquidado");
				return "/anulacionInforme.xhtml";
			}
			// Agrego el informe para mostrar
			listRSdmInformeViaticos.add(informe);
			mostrarResultados(listRSdmInformeViaticos);
		} catch (NonUniqueResultException nue) {
			statusMessages
					.add(Severity.ERROR,
							"Se encontro más de una liquidación, por favor comunicarse con el área de soporte");
			log.error("Mas de un registro de informe" + this, codigoBusqueda);
			return "/anulacionInforme.xhtml";
		}
		return "/anulacionInforme.xhtml";
	}

	/**
	 * Busca las lquidaciones por emisor y rango de fechas, soporta la opción
	 * 'Todos'
	 * 
	 * @return página de navegación correspondiente
	 * @throws Exception
	 */
	public String buscarLiquidacionesXRango() throws Exception {
		try {
			List<SdmInformeViaticos> listRSdmInformeViaticos;

			if (this.sdmEmpleadoSelect.getId() == -1) {
				listRSdmInformeViaticos = sdmInformeViaticosHome
						.buscarInformesActivosXLiquidarXFechas(this.fechaDesde,
								this.fechaHasta);
			} else {
				listRSdmInformeViaticos = sdmInformeViaticosHome
						.buscarInformesActivosXLiquidarXEmpleadoFechas(
								this.sdmEmpleadoSelect.getId(),
								this.fechaDesde, this.fechaHasta);
			}

			if (listRSdmInformeViaticos.size() == 0)
				statusMessages.add(Severity.INFO, "No se encontraron datos");
			mostrarResultados(listRSdmInformeViaticos);
		} catch (Exception e) {

			loggerBO.ingresarRegistroError(this.getClass().getCanonicalName(),
					e.getMessage(), this.anulacionInforme, null);

			throw e;
		}
		return "/anulacionInforme.xhtml";
	}

	/**
	 * Función de servicio para mostrar los resultados
	 * 
	 * @param listRSdmInformeViaticos
	 */
	public void mostrarResultados(
			List<SdmInformeViaticos> listRSdmInformeViaticos) throws Exception {
		try {
			Iterator<SdmInformeViaticos> it = listRSdmInformeViaticos
					.iterator();
			listvistaSdmInformeViaticos = new ArrayList<VistaSdmInformeViaticos>();
			while (it.hasNext()) {
				SdmInformeViaticos sViaticos = it.next();
				VistaSdmInformeViaticos vista = new VistaSdmInformeViaticos();
				vista.setId(sViaticos.getId());
				vista.setAnulado(sViaticos.isAnulado());
				vista.setCodigoCentroCosto(sViaticos.getSdmCentroCosto()
						.getCodigo());
				vista.setCodigoDelegacion(sViaticos.getSdmDelegacion()
						.getCodigo());
				vista.setCodigoDivicion(sViaticos.getSdmDivicion().getCodigo());
				vista.setCodigoEmpresa(sViaticos.getSdmEmpresa().getCodigo());
				vista.setCorrelativo(String.valueOf(sViaticos.getId()));
				vista.setFecha(sViaticos.getFecha());
				vista.setMontoTotal(sViaticos.getMontoTotal());
				vista.setSdmEmpleado(sViaticos.getSdmEmpleado());
				// vista.setProcesado(sViaticos.isProcesado());
				vista.setSdmInformeViaticos(sViaticos);
				listvistaSdmInformeViaticos.add(vista);
			}

		} catch (Exception e) {

			loggerBO.ingresarRegistroError(this.getClass().getCanonicalName(),
					e.getMessage(), this.anulacionInforme, null);

			throw e;
		}

	}

	/**
	 * Anula los informes selecionados
	 * 
	 * @return
	 */
	public String anular() throws Exception {
		try {

			Iterator<VistaSdmInformeViaticos> it = listvistaSdmInformeViaticos
					.iterator();
			VistaSdmInformeViaticos vista;
			while (it.hasNext()) {
				vista = it.next();
				if (vista.selecionado) {
					log.info("vista.getSdmInformeViaticos().getId()"
							+ vista.getSdmInformeViaticos().getId());
					log.info("vista.getFechaLiquidacion()"
							+ vista.getFechaLiquidacion());
					SdmInformeViaticos sdmInformeViaticos = sdmInformeViaticosHome
							.buscarInformeActivosXCorrelativo(vista.getId());
					sdmInformeViaticosHome
							.anularInformeViaticos(sdmInformeViaticos);
					statusMessages.add("Se anuló el informe : "
							+ sdmInformeViaticos.getId());

					loggerBO.ingresarRegistroEvento(this.getClass()
							.getCanonicalName(), "Anulación Informe",
							this.anulacionInforme, String
									.valueOf(sdmInformeViaticos.getId()));

				}
			}
			listvistaSdmInformeViaticos = new ArrayList<VistaSdmInformeViaticos>();
			this.sdmEmpleadoSelect = new SdmEmpleado();
			return "/anulacionInforme.xhtml";

		} catch (Exception e) {

			loggerBO.ingresarRegistroError(this.getClass().getCanonicalName(),
					e.getMessage(), this.anulacionInforme, null);

			throw e;
		}

	}

	public String getTabSelect() {
		return tabSelect;
	}

	public void setTabSelect(String tabSelect) {
		this.tabSelect = tabSelect;
	}

	public String getCodigoBusqueda() {
		return codigoBusqueda;
	}

	public void setCodigoBusqueda(String codigoBusqueda) {
		this.codigoBusqueda = codigoBusqueda;
	}

	public SdmUsuario getSdmUsuarioSelect() {
		return sdmUsuarioSelect;
	}

	public void setSdmUsuarioSelect(SdmUsuario sdmUsuarioSelect) {
		this.sdmUsuarioSelect = sdmUsuarioSelect;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public SdmEmpleado getSdmEmpleadoSelect() {
		return sdmEmpleadoSelect;
	}

	public void setSdmEmpleadoSelect(SdmEmpleado sdmEmpleadoSelect) {
		this.sdmEmpleadoSelect = sdmEmpleadoSelect;
	}

}
