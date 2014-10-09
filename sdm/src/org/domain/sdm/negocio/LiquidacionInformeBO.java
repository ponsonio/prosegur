package org.domain.sdm.negocio;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.domain.sdm.entity.SdmEmpleado;
import org.domain.sdm.entity.SdmInformeViaticos;
import org.domain.sdm.entity.SdmLiquidacionInformeViaticos;
import org.domain.sdm.session.SdmEmpleadoHome;
import org.domain.sdm.session.SdmInformeViaticosHome;
import org.domain.sdm.session.SdmLiquidacionInformeViaticosHome;
import org.domain.sdm.vista.bean.VistaSdmInformeViaticos;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

@Name("liquidacionInformeBO")
@Scope(ScopeType.SESSION)
public class LiquidacionInformeBO implements Serializable {

	@In
	SdmEmpleado sdmEmpleado;

	@In 
	StatusMessages statusMessages;



	@Logger private Log log;

	SdmEmpleado sdmEmpleadoSelect = new SdmEmpleado();

	String codigoBusqueda ;

	@DataModel
	List<VistaSdmInformeViaticos> listvistaSdmInformeViaticos;


	private Date fechaDesde = new Date();

	private Date fechaHasta = new Date();

	private String tabSelect;

	@In(create=true)
	SdmEmpleadoHome sdmEmpleadoHome ;

	@In(create=true)
	SdmInformeViaticosHome sdmInformeViaticosHome;

	@In(create=true)
	SdmLiquidacionInformeViaticosHome sdmLiquidacionInformeViaticosHome;

	public SdmEmpleado getSdmEmpleadoSelect() {
		return sdmEmpleadoSelect;
	}

	public void setSdmEmpleadoSelect(SdmEmpleado empleadoSelect) {
		this.sdmEmpleadoSelect = empleadoSelect;
	}


	public String buscarXCodigoEmpleado(){
		
		listvistaSdmInformeViaticos = new ArrayList<VistaSdmInformeViaticos>();
		List <SdmInformeViaticos> listRSdmInformeViaticos;
		try{
			if (this.sdmEmpleadoSelect.getId() == -1){
				listRSdmInformeViaticos = sdmInformeViaticosHome.buscarInformesActivosXLiquidarXFechas( this.fechaDesde, this.fechaHasta);
			}else{
				listRSdmInformeViaticos = sdmInformeViaticosHome.buscarInformesActivosXLiquidarXEmpleadoFechas(sdmEmpleadoSelect.getId(), this.fechaDesde, this.fechaHasta);
			}

			if (listRSdmInformeViaticos.size() == 0) { statusMessages.add(Severity.ERROR , "No se encontraron resultados");};
			mostrarResultados(listRSdmInformeViaticos);
		}catch (NoResultException nre) {
			statusMessages.add(Severity.ERROR , "No se encontraron datos de el empleado");
		}catch (NonUniqueResultException e) {
			statusMessages.add(Severity.ERROR , "Mas de un empleado con dicho código, por favor comunicarse con soporte");
		}
		return "/liquidacionInforme.xhtml";
	}


	public String buscarLiquidacionXCorrelativo(){
		listvistaSdmInformeViaticos = new ArrayList<VistaSdmInformeViaticos>();
		List <SdmInformeViaticos> listRSdmInformeViaticos = new  ArrayList<SdmInformeViaticos>();
		this.codigoBusqueda = this.codigoBusqueda.trim();
		SdmInformeViaticos informe ;
		sdmEmpleadoSelect = new SdmEmpleado();
		try {
			informe =   sdmInformeViaticosHome.buscarInformeXCorrelativo(Long.parseLong(this.codigoBusqueda)) ;
			if (informe.isAnulado()){
				statusMessages.add(Severity.ERROR ,"El informe se encuentra anulado");
				return "/liquidacionInforme.xhtml";
			}
		}catch (NoResultException nre) {
			log.info("Infome no encontrado"+ this.codigoBusqueda);
			statusMessages.add(Severity.ERROR ,"No se encontro Informe");
			return "/liquidacionInforme.xhtml";
		}catch (NonUniqueResultException nue) {
			statusMessages.add(Severity.ERROR ,"Se encontró más de un informe, por favor comunicarse con el área de soporte");
			log.error("Mas de un registro de informe"+ this.codigoBusqueda);
			return "/liquidacionInforme.xhtml";
		}catch (NumberFormatException e) {
			statusMessages.add(Severity.ERROR ,"Debe ingresar un número valido");
			return "/liquidacionInforme.xhtml";
			// TODO: handle exception
		}


		try {
			SdmLiquidacionInformeViaticos liquidacion =  sdmLiquidacionInformeViaticosHome.buscarLiquidacionInforme(informe.getId());
			if (liquidacion != null){
				statusMessages.add(Severity.ERROR ,"La solicitud ya fue liquidada");
				return "/liquidacionInforme.xhtml";
			}
			listRSdmInformeViaticos.add(informe);
			mostrarResultados(listRSdmInformeViaticos);
		}catch (NonUniqueResultException nue) {
			statusMessages.add(Severity.ERROR ,"Se encontro más de una liquidación, por favor comunicarse con el área de soporte");
			log.error("Mas de un registro de informe"+ this.codigoBusqueda);
			return "/liquidacionInforme.xhtml";
		}
		return "/liquidacionInforme.xhtml";
	}

	public String liquidar(){
		Iterator<VistaSdmInformeViaticos> it = listvistaSdmInformeViaticos.iterator();
		VistaSdmInformeViaticos vista ;
		while(it.hasNext()){
			vista = it.next();
			if (vista.selecionado){
				log.info("vista.getSdmInformeViaticos().getId()"+vista.getSdmInformeViaticos().getId());
				log.info("vista.getFechaLiquidacion()"+vista.getFechaLiquidacion());

				if (vista.getFechaLiquidacion() == null){
					statusMessages.add("Debe de ingresar una fecha de liquidación para los datos selecionados");
					//obtenerInformesUsuario();
					return "/liquidacionInforme.xhtml";
				}

				SdmLiquidacionInformeViaticos sdmLiquidacionInformeViaticos = new SdmLiquidacionInformeViaticos();
				sdmLiquidacionInformeViaticos.setAnulado(false);
				sdmLiquidacionInformeViaticos.setFecha(vista.getFechaLiquidacion());
				sdmLiquidacionInformeViaticos.setSdmInformeViaticos(vista.getSdmInformeViaticos());
				sdmLiquidacionInformeViaticos.setMonto(vista.getMontoTotal());
				sdmLiquidacionInformeViaticos.setSdmEmpleado(sdmEmpleado);
				sdmLiquidacionInformeViaticos.setSdmInformeViaticos(vista.getSdmInformeViaticos());
				sdmLiquidacionInformeViaticosHome.grabarInforme(sdmLiquidacionInformeViaticos);
				statusMessages.add("Se liquidó el informe : "+sdmLiquidacionInformeViaticos.getSdmInformeViaticos().getId());
			}
		}
		listvistaSdmInformeViaticos = new ArrayList<VistaSdmInformeViaticos>();
		this.sdmEmpleadoSelect = new SdmEmpleado();
		return "/liquidacionInforme.xhtml";
	}   


	public void mostrarResultados(List<SdmInformeViaticos> listRSdmInformeViaticos){
		Iterator<SdmInformeViaticos> it = listRSdmInformeViaticos.iterator();
		listvistaSdmInformeViaticos = new ArrayList<VistaSdmInformeViaticos>();
		while (it.hasNext()){
			SdmInformeViaticos sViaticos = it.next();
			VistaSdmInformeViaticos vista = new  VistaSdmInformeViaticos();
			vista.setId(sViaticos.getId());
			vista.setAnulado(sViaticos.isAnulado());
			vista.setCodigoCentroCosto(sViaticos.getSdmCentroCosto().getCodigo());
			vista.setCodigoDelegacion(sViaticos.getSdmDelegacion().getCodigo());
			vista.setCodigoDivicion(sViaticos.getSdmDivicion().getCodigo());
			vista.setCodigoEmpresa(sViaticos.getSdmEmpresa().getCodigo());
			vista.setCorrelativo(String.valueOf(sViaticos.getId()));
			vista.setFecha(sViaticos.getFecha());
			vista.setMontoTotal(sViaticos.getMontoTotal());
			vista.setSdmEmpleado(sViaticos.getSdmEmpleado());
			vista.setFechaLiquidacion(new Date());
			//vista.setProcesado(sViaticos.isProcesado());
			vista.setSdmInformeViaticos(sViaticos);
			listvistaSdmInformeViaticos.add(vista);

		}

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



}
