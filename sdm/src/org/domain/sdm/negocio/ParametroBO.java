package org.domain.sdm.negocio;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Query;

import org.domain.sdm.entity.SdmParametro;
import org.domain.sdm.entity.SdmTipoServicio;
import org.domain.sdm.session.SdmParametroHome;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.StatusMessages;

@Name("parametroBO")
@Scope(ScopeType.SESSION)
public class ParametroBO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4631994377105850405L;

	@In(create= true)
	LoggerBO loggerBO ;

    @In 
    StatusMessages statusMessages;
	
    @In(create = true )
    SdmParametroHome sdmParametroHome; 
   
    /**
     * 
     * @return
     * @throws Exception
     */
    public int obtenerNumeroDiasExpiraContrasena() throws Exception{
    	try{
    		ArrayList<SdmParametro> arrayList = (ArrayList<SdmParametro>)sdmParametroHome.getParametro("dias_expira_contrasena") ;
    		
    		if (arrayList.size() > 1){
    			loggerBO.ingresarRegistroError(this.getClass().getCanonicalName(),
    					"mas de un parámetro de días expira contraseña", "Error obteniedo parámetro días de expira contraseña" , null);
    		}
    		
    		SdmParametro sdmParametro = arrayList.get(0) ;
    		return  Integer.parseInt(sdmParametro.getValor()) ;
    		
    	}catch(Exception e) {
			loggerBO.ingresarRegistroError(this.getClass().getCanonicalName(),
					e.getMessage(), "Error obteniedo parámetro días de expira contraseña" , null);
			throw e;
    	}
    }
    


}
