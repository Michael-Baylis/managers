package dev.galasa.common.zos3270.internal.properties;

import dev.galasa.common.zos3270.Zos3270ManagerException;
import dev.galasa.framework.spi.cps.CpsProperties;

/**
 * zOS3270 Apply Confidential Text Filtering to screen records
 * <p>
 * This property indicates that all logs and screen recordings are to be passed through 
 * the Confidential Text Filtering services, to hide text like passwords 
 * </p><p>
 * The property is:-<br><br>
 * zos3270.apply.ctf=true
 * </p><p>
 * default value is true
 * </p>
 * 
 * @author Michael Baylis
 *
 */
public class ApplyConfidentialTextFiltering extends CpsProperties {
	
	public static boolean get() throws Zos3270ManagerException {
		return Boolean.parseBoolean(getStringWithDefault(Zos3270PropertiesSingleton.cps(), 
				               "true",
				               "apply", 
				               "ctf"));
	}

}
