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
import org.domain.sdm.entity.SdmUsuario;
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


@Name("busquedaInformeBO")
@Scope(ScopeType.SESSION)
public class BusquedaInformeBO implements Serializable{





	/**
	 * 
	 */
	private static final long serialVersionUID = -6143927137155773095L;


	
    @In 
    StatusMessages statusMessages;
	
	@In 
	private EntityManager entityManager;
	
	@Logger private Log log;
	
	SdmEmpleado sdmEmpleadoSelect = new SdmEmpleado();
	
	String codigoBusqueda ;

	private String tabSelect;
	
	SdmUsuario sdmUsuarioSelect = new SdmUsuario();
	
	private Date fechaDesde = new Date();
	
	private Date fechaHasta = new Date();
	
	
	@DataModel
	List<VistaSdmInformeViaticos> listvistaSdmInformeViaticos;

	
	
	
	 public String buscarLiquidacionXCorrelativo(){
		 listvistaSdmInformeViaticos = new ArrayList<VistaSdmInformeViaticos>();
		 List <SdmInformeViaticos> listRSdmInformeViaticos = new  ArrayList<SdmInformeViaticos>();
		 this.codigoBusqueda = this.codigoBusqueda.trim();
		 SdmInformeViaticos informe ;
		 //sdmEmpleadoSelect = new SdmEmpleado();
		 try {
			 informe =  buscarInformesXLiquidarXCorrelativo(Long.parseLong(this.codigoBusqueda)) ;
			 if (informe.isAnulado()) {
					log.info("Infome ya anulado"+ this,codigoBusqueda);
					statusMessages.add(Severity.ERROR ,"El informe esta anulado");
					return "/busquedaInforme.xhtml";
			 }
			}catch (NoResultException nre) {
				log.info("Infome no encontrado"+ this,codigoBusqueda);
				statusMessages.add(Severity.ERROR ,"No se encontro Informe");
				return "/busquedaInforme.xhtml";
			}catch (NonUniqueResultException nue) {
				statusMessages.add(Severity.ERROR ,"Se encontró más de un informe, por favor comunicarse con el área de soporte");
				log.error("Mas de un registro de informe"+ this,codigoBusqueda);
				return "/busquedaInforme.xhtml";
			}catch (NumberFormatException e) {
				statusMessages.add(Severity.ERROR ,"Debe ingresar un número valido");
				return "/busquedaInforme.xhtml";
				// TODO: handle exception
			}

		 
		 /**
		  * Se deben de poder buscar los liq tambien
		 try {
			 buscarLiquidacionInforme(informe.getId());
			 statusMessages.add(Severity.ERROR ,"El informe ya fue liquidado");
			 return "/busquedaInforme.xhtml";
		 }catch (NoResultException nre) {
			 //Agrego el informe para mostrar
			 listRSdmInformeViaticos.add(informe);
		     mostrarResultados(listRSdmInformeViaticos);
		 }catch (NonUniqueResultException nue) {
				statusMessages.add(Severity.ERROR ,"Se encontró más de una liquidación,por favor comunicarse con el área de soporte");
				log.error("Mas de un registro de informe"+ this,codigoBusqueda);
				return "/busquedaInforme.xhtml";
		}
		**/
		 listRSdmInformeViaticos.add(informe);
	     mostrarResultados(listRSdmInformeViaticos);
		 return "/busquedaInforme.xhtml";
	 }
	 
	 
	 public String buscarLiquidacionesXRango(){
		 List <SdmInformeViaticos>  listRSdmInformeViaticos ;
		 if (this.sdmEmpleadoSelect.getId() == -1){
			 listRSdmInformeViaticos = buscarInformesActivosXFechas(this.fechaDesde , this.fechaHasta);
		 }else{
			 listRSdmInformeViaticos = buscarInformesActivosXEmpleadoFechas(this.sdmEmpleadoSelect.getId() , this.fechaDesde , this.fechaHasta);
		 }
		  
		 if (listRSdmInformeViaticos.size() == 0) statusMessages.add(Severity.INFO , "No se encontraron datos");
		 mostrarResultados(listRSdmInformeViaticos);
		 return "/busquedaInforme.xhtml";
	 }

	    public void mostrarResultados(List<SdmInformeViaticos> listRSdmInformeViaticos){
			  Iterator<SdmInformeViaticos> it = listRSdmInformeViaticos.iterator();
			  listvistaSdmInformeViaticos = new ArrayList<VistaSdmInformeViaticos>();
			  while (it.hasNext()){
				  SdmInformeViaticos sViaticos = it.next();
				  VistaSdmInformeViaticos vista = new  VistaSdmInformeViaticos();
				  SdmLiquidacionInformeViaticos liquidacion = this.buscarLiquidacionInforme(sViaticos.getId());
				  
				  if (liquidacion != null) vista.setFechaLiquidacion(liquidacion.getFecha());
				  
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
				  //vista.setProcesado(sViaticos.isProcesado());
				  vista.setSdmInformeViaticos(sViaticos);
				  listvistaSdmInformeViaticos.add(vista);
				  
			  }

	    }
	    
	    public String verInforme(Long i){
	    	System.out.print("ver informe :"+i);
	    	return "/busquedaInforme.xhtml";
	    }
	    
	    
	    public SdmEmpleado  buscarSdmEmpleadoActivoPorCodigo(String codigo){
			Query query =  entityManager.createQuery("From SdmEmpleado e where e.activo = true and e.codigo = :codigo");
			return (SdmEmpleado)query.setParameter("codigo",codigo).getSingleResult();
	    }
	    


	    
	    public List<SdmInformeViaticos>  buscarInformesActivosXLiquidarXEmpleadoFechas(long idEmpleado, Date fechaIni , Date fechaHasta ){
			Query query =  entityManager.createQuery("From SdmInformeViaticos i  where i.anulado=false and i.sdmEmpleado.id = :idEmpleado  and i.fecha >= :fechaIni and i.fecha <= :fechaHasta and i not in (select  l.sdmInformeViaticos.id From SdmLiquidacionInformeViaticos l where l.anulado =false)");
			return (List<SdmInformeViaticos>)query.setParameter("idEmpleado",idEmpleado).setParameter("fechaIni",fechaIni).setParameter("fechaHasta",fechaHasta).getResultList();
	    }

	    public List<SdmInformeViaticos>  buscarInformesActivosXEmpleadoFechas(long idEmpleado, Date fechaIni , Date fechaHasta ){
			Query query =  entityManager.createQuery("From SdmInformeViaticos i  where i.anulado=false and i.sdmEmpleado.id = :idEmpleado  and i.fecha >= :fechaIni and i.fecha <= :fechaHasta");
			return (List<SdmInformeViaticos>)query.setParameter("idEmpleado",idEmpleado).setParameter("fechaIni",fechaIni).setParameter("fechaHasta",fechaHasta).getResultList();
	    }
	    
	    public List<SdmInformeViaticos>  buscarInformesActivosXFechas( Date fechaIni , Date fechaHasta ){
			Query query =  entityManager.createQuery("From SdmInformeViaticos i  where i.anulado=false  and i.fecha >= :fechaIni and i.fecha <= :fechaHasta");
			return (List<SdmInformeViaticos>)query.setParameter("fechaIni",fechaIni).setParameter("fechaHasta",fechaHasta).getResultList();
	    }
	    

	    
	    public SdmInformeViaticos  buscarInformesXLiquidarXCorrelativo(long correlativo){
			Query query =  entityManager.createQuery("From SdmInformeViaticos i where i.id= :correlativo)");
			return (SdmInformeViaticos)query.setParameter("correlativo",correlativo).getSingleResult();
	    }
	    
	    public SdmInformeViaticos  buscarInformesXCorrelativo(long correlativo){
			Query query =  entityManager.createQuery("From SdmInformeViaticos i where i.id= :correlativo)");
			return (SdmInformeViaticos)query.setParameter("correlativo",correlativo).getSingleResult();
	    }
	    
	    public SdmLiquidacionInformeViaticos buscarLiquidacionInforme(long idInforme){
	    	try{
				Query query =  entityManager.createQuery("From SdmLiquidacionInformeViaticos i where i.sdmInformeViaticos.id = :idInforme and i.anulado=false)");
				return (SdmLiquidacionInformeViaticos)query.setParameter("idInforme",idInforme).getSingleResult();
	    	}catch(NoResultException nre){
	    		return null;
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
