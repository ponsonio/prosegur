package org.domain.sdm.negocio;


import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.domain.sdm.entity.SdmCentroCosto;
import org.domain.sdm.entity.SdmDelegacion;
import org.domain.sdm.entity.SdmDivicion;
import org.domain.sdm.entity.SdmEmpleado;
import org.domain.sdm.entity.SdmEmpresa;
import org.domain.sdm.session.SdmCentroCostoHome;
import org.domain.sdm.session.SdmDelegacionHome;
import org.domain.sdm.session.SdmDivicionHome;
import org.domain.sdm.session.SdmEmpleadoHome;
import org.domain.sdm.session.SdmEmpresaHome;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;


@Name("empleadoMasivoBO")
@Scope(ScopeType.SESSION)
public class EmpleadoMasivoBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7276137792492138227L;

	@In(create = true)
	LoggerBO loggerBO;
	
    @In 
    StatusMessages statusMessages;
    
    @In(create=true)
    SdmEmpleadoHome sdmEmpleadoHome;
    
    @In(create=true)
    SdmEmpresaHome sdmEmpresaHome;
    
    @In(create=true)
    SdmCentroCostoHome sdmCentroCostoHome;
    
    @In(create=true)
    SdmDelegacionHome sdmDelegacionHome;
    
    @In(create=true)
    SdmDivicionHome sdmDivicionHome;
    
	@Logger private Log log;
	
	ArrayList<String> codigosEmpresas ;
	ArrayList<String> codigosCentroCosto ;
	ArrayList<String> codigosDelegacion ;
	ArrayList<String> codigosDivicion ;
	
	ArrayList<String> erroresCarga = new ArrayList<String>();
	
	ArrayList<String> resultado = new ArrayList<String>();
	
	ArrayList<SdmEmpleado> arrayListEmpleadosCargar = new ArrayList<SdmEmpleado>();
	
	ArrayList<SdmEmpleado> arrayListEmpleadosActualizar = new ArrayList<SdmEmpleado>();
	
	ArrayList<SdmEmpleado> arrayListEmpleadosDesactivar = new ArrayList<SdmEmpleado>();
	
	InputStream file ;

	public InputStream getFile() {
		return file;
	}

	public void setFile(InputStream file) {
		this.file = file;
	}
	

	
	
	public void cargarSdmEmpleadosFile(){
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(file);
			HSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			 			
			int fila = 0 ;
		    //itero  las filas
		    while(rowIterator.hasNext()) {
		    	Row row = rowIterator.next();
		    	if (fila > 0){
		    		try{
			    		Double d = row.getCell(0).getNumericCellValue() ;
				    	String codigoEmp =  String.valueOf(d.intValue()) ;
				    	
				    	String nombre = row.getCell(1).getStringCellValue().trim().toUpperCase();
				    	String codEmpresa = row.getCell(2).toString().trim();
				    	String codDelegacion = row.getCell(3).toString().trim();
				    	String codDivicion = row.getCell(4).toString().trim();
				    	String codCentroCosto = row.getCell(5).toString().trim();
				    	
						SdmEmpleado empleado = new SdmEmpleado();
						empleado.setCodigo(codigoEmp);
						empleado.setNombre(nombre);
						
						SdmCentroCosto sdmCentroCosto = new SdmCentroCosto();
						sdmCentroCosto.setCodigo(codCentroCosto);
						empleado.setSdmCentroCosto(sdmCentroCosto);
						
						SdmEmpresa sdmEmpresa = new SdmEmpresa();
						sdmEmpresa.setCodigo(codEmpresa);
						empleado.setSdmEmpresa(sdmEmpresa);

						SdmDivicion sdmDivicion = new SdmDivicion();
						sdmDivicion.setCodigo(codDivicion);
						empleado.setSdmDivicion(sdmDivicion);

						SdmDelegacion sdmDelegacion = new SdmDelegacion();
						sdmDelegacion.setCodigo(codDelegacion);
						empleado.setSdmDelegacion(sdmDelegacion);

						empleado.setActivo(true);
						empleado.setExterno(false);

						if (validarEmpleado(empleado)) {
							boolean actualizar = (sdmEmpleadoHome.buscarSdmEmpleadoXCodigo(empleado.getCodigo()) != null) ;
							if (actualizar)  this.arrayListEmpleadosActualizar.add(empleado) ; else this.arrayListEmpleadosCargar.add(empleado);
						}
		    		}catch(Exception e){
		    			//log.error(Severity.ERROR, "Ocurrió un error leyendo la hoja 1 , fila "+(row.getRowNum()+1));
		    			loggerBO.ingresarRegistroError(this.getClass().getCanonicalName(),
		    					"Ocurrió un error leyendo la hoja 1 , fila "+(row.getRowNum()+1)+ e.getMessage(), "Carga masiva empleados", null);
		    			
		    			this.erroresCarga.add("Ocurrió un error leyendo la hoja 1 , fila "+(row.getRowNum()+1));
		    			e.printStackTrace();
		    		}
						
		    	}
		    	fila++;
		    }
		    //Empleados a Desactivar
		    if (workbook.getNumberOfSheets() > 1){
			    HSSFSheet  sheet2 = workbook.getSheetAt(1);
			    Iterator<Row> rowIterator2 = sheet2.iterator();
				fila = 0 ;
			    //itero  las filas
			    while(rowIterator2.hasNext()) {
			    	Row row = rowIterator2.next();
			    	if (fila > 0){
			    		try{
				    		Double d = row.getCell(0).getNumericCellValue() ;
					    	String codigoEmp =  String.valueOf(d.intValue()) ;
					    	SdmEmpleado empleadoEliminar = sdmEmpleadoHome.buscarSdmEmpleadoXCodigo(codigoEmp);
					    	if (empleadoEliminar == null) {
					    		this.erroresCarga.add("No se encontró el empleado a eliminar con código:" + codigoEmp);
					    	}else{
					    		empleadoEliminar.setActivo(false);
					    		this.arrayListEmpleadosDesactivar.add(empleadoEliminar);
					    	}
			    		}catch(Exception e){
			    			//log.error(Severity.ERROR, "Ocurrió un error leyendo la hoja 2 , fila"+ (row.getRowNum()+1));

			    			loggerBO.ingresarRegistroError(this.getClass().getCanonicalName(),
			    					"Ocurrió un error leyendo la hoja 2 , fila"+ (row.getRowNum()+1) +e.getMessage(),"Carga masiva empleados", null);
			    			
			    			this.erroresCarga.add("Ocurrió un error leyendo la hoja 2 , fila "+(row.getRowNum()+1));
			    			e.printStackTrace();
			    			
			    		}
			    	}
			    	fila++;
			    }
		    }
		}catch(Exception e){
			statusMessages.add("Ocurrió un error al abrir el archivo, por favor verifique el formato");

			loggerBO.ingresarRegistroError(this.getClass().getCanonicalName(),
					"Ocurrió un error al abrir el archivo, por favor verifique el formato" +e.getMessage(),"Carga masiva empleados", null);

		
			e.printStackTrace();
			log.error("Ocurrió un error leyendo el archivo");
			log.error(e.getMessage());
		}finally{
			try{
				file.close();
			}catch(Exception e){
				log.error("Ocurrió un error cerrando el archivo de carga masiva"+ e.getMessage());

				loggerBO.ingresarRegistroError(this.getClass().getCanonicalName(),
						"Ocurrió un error cerrando el archivo de carga masiva" +e.getMessage(),"Carga masiva empleados", null);
			}
		}
		

	}
	
	
	public String procesarArchivo() {
			limpiarMensajes();
			this.cargarListas();
			this.limpiarListasEmpleados();
			this.cargarSdmEmpleadosFile();
		return "/altaEmpleadosMasiva.xhtml";
	}
	
	public boolean validarEmpleado(SdmEmpleado sdmEmpleado){
		boolean flag = true;
		
		if (sdmEmpleado.getCodigo().length() != 7 || sdmEmpleado.getCodigo().matches("[a-zA-Z]+")){ this.erroresCarga.add("Código incorrecto :" +sdmEmpleado.getCodigo()) ; flag = false ;}
		if ( (sdmEmpleado.getNombre()==null) || sdmEmpleado.getNombre().isEmpty()) { this.erroresCarga.add("Nombre incorrecto :"+sdmEmpleado.getCodigo()) ; flag = false ;}
		if (!this.codigosEmpresas.contains(sdmEmpleado.getSdmEmpresa().getCodigo())) { this.erroresCarga.add("No existe la empresa: "+sdmEmpleado.getSdmEmpresa().getCodigo()) ; flag = false ;}
		if (!this.codigosCentroCosto.contains(sdmEmpleado.getSdmCentroCosto().getCodigo())) { this.erroresCarga.add("No existe el centro de costo: "+sdmEmpleado.getSdmCentroCosto().getCodigo()) ; flag = false ;}
		if (!this.codigosDivicion.contains(sdmEmpleado.getSdmDivicion().getCodigo())) { this.erroresCarga.add("No existe la división: "+sdmEmpleado.getSdmDivicion().getCodigo()) ; flag = false ;}
		if (!this.codigosDelegacion.contains(sdmEmpleado.getSdmDelegacion().getCodigo())) { this.erroresCarga.add("No existe la delegación: "+sdmEmpleado.getSdmDelegacion().getCodigo()) ; flag = false ;}				
		return flag;
	}

	
	
	public ArrayList<String> getErroresCarga() {
		return erroresCarga;
	}

	public void setErroresCarga(ArrayList<String> erroresCarga) {
		this.erroresCarga = erroresCarga;
	}

	public ArrayList<SdmEmpleado> getArrayListEmpleadosCargar() {
		return arrayListEmpleadosCargar;
	}

	public void setArrayListEmpleadosCargar(
			ArrayList<SdmEmpleado> arrayListEmpleadosCargar) {
		this.arrayListEmpleadosCargar = arrayListEmpleadosCargar;
	}
	
	void cargarListas(){
		this.codigosEmpresas = this.sdmEmpresaHome.listaCodigos() ;
		this.codigosDivicion = this.sdmDivicionHome.listaCodigos() ;
		this.codigosDelegacion = this.sdmDelegacionHome.listaCodigos() ;
		this.codigosCentroCosto = this.sdmCentroCostoHome.listaCodigos() ;

	}
	
	public void limpiarListasEmpleados(){
		arrayListEmpleadosCargar = new ArrayList<SdmEmpleado>();
		arrayListEmpleadosActualizar = new ArrayList<SdmEmpleado>();
		arrayListEmpleadosDesactivar = new ArrayList<SdmEmpleado>();
	}
	
	/**
	 * Graba los cambios
	 * @return
	 */
	public String grabarEmpleados() throws Exception {
		try {
			Iterator<SdmEmpleado> it = this.arrayListEmpleadosCargar.iterator();
			//boolean flag = true;
			while (it.hasNext()){
				SdmEmpleado empleado = it.next();
				try{
				if (sdmEmpleadoHome.buscarSdmEmpleadoXCodigo(empleado.getCodigo()) != null) throw new Exception("Ya existe ese código de empleado");
					sdmEmpleadoHome.crearEmpleado(empleado) ;
					this.resultado.add("Se creo el empleado :" + empleado.getCodigo());
					
					loggerBO.ingresarRegistroEvento(this.getClass().getCanonicalName(),
							"Se creo el empleado :" + empleado.getCodigo(),"Carga masiva empleados", null);
					
				}catch(Exception e){
					//flag=false;
					this.resultado.add("Ocurrió un error y no se creó el empleado :"+ empleado.getCodigo());
					log.error("Ocurrió un error y no se creo el empleado :"+ empleado.getCodigo());
					log.error("Error creación masiva :"+ e.getMessage());
	
					loggerBO.ingresarRegistroError(this.getClass().getCanonicalName(),
							"Error creación masiva :"+ e.getMessage(),"Carga masiva empleados", empleado.getCodigo());
				
				}
			}
			Iterator<SdmEmpleado> it2 = this.arrayListEmpleadosActualizar.iterator();
			while (it2.hasNext()){
				SdmEmpleado empleado = it2.next();
				try{
					sdmEmpleadoHome.actualizarEmpleado(empleado) ;
					this.resultado.add("Se actualizó el empleado :" + empleado.getCodigo());
	
					loggerBO.ingresarRegistroEvento(this.getClass().getCanonicalName(),
							"Se actualizó el empleado :" + empleado.getCodigo(),"Carga masiva empleados", empleado.getCodigo());
					
				}catch(Exception e){
					//flag=false;
					this.resultado.add("Ocurrió un error y no se actualizó el empleado :"+ empleado.getCodigo());
					log.error("Ocurrió un error y no se actualizó el empleado :"+ empleado.getCodigo());
					log.error("Error actualización masiva :"+ e.getMessage());
	
					loggerBO.ingresarRegistroEvento(this.getClass().getCanonicalName(),
							"Error actualización masiva :"+ e.getMessage(),"Carga masiva empleados", empleado.getCodigo());
				}
			}
			
			Iterator<SdmEmpleado> it3 = this.arrayListEmpleadosDesactivar.iterator();
			while (it3.hasNext()){
				SdmEmpleado empleado = it3.next();
				try{
					sdmEmpleadoHome.actualizarEmpleado(empleado) ;
					this.resultado.add("Se desactivó el empleado :" + empleado.getCodigo());
	
					loggerBO.ingresarRegistroEvento(this.getClass().getCanonicalName(),
							"Se desactivó el empleado :" + empleado.getCodigo(),"Carga masiva empleados", empleado.getCodigo());
				
				}catch(Exception e){
					//flag=false;
					this.resultado.add("Ocurrió un error y no se desactivó el empleado :"+ empleado.getCodigo());
					log.error("Ocurrió un error y no se desactivó el empleado :"+ empleado.getCodigo());
					log.error("Error creación desactivación :"+ e.getMessage());
	
					loggerBO.ingresarRegistroError(this.getClass().getCanonicalName(),
							"Error creación desactivación :"+ e.getMessage(),"Carga masiva empleados", empleado.getCodigo());
				
				}
			}
			
			//if (flag) this.statusMessages.add("Operación Realizada");
			this.file = null;
			erroresCarga = new ArrayList<String>();
			//resultado  = new ArrayList<String>();
			limpiarListasEmpleados();
			limpiarListas();
			return "/altaEmpleadosMasiva.xhtml";
		}catch(Exception e){
			loggerBO.ingresarRegistroError(this.getClass().getCanonicalName(),
					"Error al grabar empleados :"+ e.getMessage(),"Carga masiva empleados", null);
			throw e;
		}	
	}
	
	public String limpiarNuevos(){
		limpiarListas();
		arrayListEmpleadosCargar = new ArrayList<SdmEmpleado>();
		return "/altaEmpleadosMasiva.xhtml";
	}
	
	public String limpiarActualizacion(){
		limpiarListas();
		arrayListEmpleadosActualizar = new ArrayList<SdmEmpleado>();
		return "/altaEmpleadosMasiva.xhtml";
		
	}
	
	public String limpiarDesactivacion(){
		limpiarListas();
		arrayListEmpleadosDesactivar = new ArrayList<SdmEmpleado>();
		return "/altaEmpleadosMasiva.xhtml";
		
	}
	
	public String cancelar(){
		limpiarMensajes();
		arrayListEmpleadosCargar = new ArrayList<SdmEmpleado>();
		arrayListEmpleadosActualizar = new ArrayList<SdmEmpleado>();
		arrayListEmpleadosDesactivar = new ArrayList<SdmEmpleado>();
		limpiarListas();
		return "/altaEmpleadosMasiva.xhtml";
	}
	
	public void limpiarMensajes(){
		erroresCarga = new ArrayList<String>();
		resultado  = new ArrayList<String>();
	}
	
	void limpiarListas(){
		this.codigosEmpresas = null ;
		this.codigosDivicion = null ;
		this.codigosDelegacion = null ;
		this.codigosCentroCosto = null;
	}

	public ArrayList<SdmEmpleado> getArrayListEmpleadosActualizar() {
		return arrayListEmpleadosActualizar;
	}

	public void setArrayListEmpleadosActualizar(
			ArrayList<SdmEmpleado> arrayListEmpleadosActualizar) {
		this.arrayListEmpleadosActualizar = arrayListEmpleadosActualizar;
	}

	public ArrayList<SdmEmpleado> getArrayListEmpleadosDesactivar() {
		return arrayListEmpleadosDesactivar;
	}

	public void setArrayListEmpleadosDesactivar(
			ArrayList<SdmEmpleado> arrayListEmpleadosDesactivar) {
		this.arrayListEmpleadosDesactivar = arrayListEmpleadosDesactivar;
	}

	public ArrayList<String> getResultado() {
		return resultado;
	}

	public void setResultado(ArrayList<String> resultado) {
		this.resultado = resultado;
	}
	
	
	
}