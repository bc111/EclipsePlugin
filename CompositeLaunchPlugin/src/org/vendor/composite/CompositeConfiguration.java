package org.vendor.composite;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;


/**
* This composite launch provides a tool to launch several configurations 'at once'.
* 
* Please note:
* Modes 'run' and 'debug' are supported.
* A composite-launch configuration itself (= a configuration of that type) cannot be selected. 
* 
* @author  Barbara C
* @version 0.9
* @since   2017-01-19 
*/

public class CompositeConfiguration extends LaunchConfigurationDelegate {
	
	public final static String ATTRIBUTE_CONFIGS_TO_LAUNCH = "configurationsToLaunch";	
	
	/* The String is hard-coded to present the value of the 'id' value of
	 * point="org.eclipse.debug.core.launchConfigurationTypes"
	 * in the file plugin.xml.
	 * Configurations of this implemented composite-type are not allowed to be included
	 * into a composite-launch (circular reference) - and are checked against that value.
	 */
	public final static String PLUGIN_XML_ID = "org.vendor.CompositeLaunchType";

	
	/* A list of configuration-names (Strings) is transferred to the launch-method;
	 * the list was transfered as 'attribute'.
	 * (assumption: its name is unique/identifier for a launch-configuration in the Eclipse IDE).
	 * In the launch method, from all launch-configurations currently existing in the IDE, 
	 * where the (transferred) names match, those configurations are launched.
	 */
    @Override
    public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
                    throws CoreException {
    	      	
    	List<String> configNames = configuration.getAttribute(ATTRIBUTE_CONFIGS_TO_LAUNCH, Collections.<String> emptyList()); 
          
    	ILaunchConfiguration[] configs = DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurations();
		
    	HashMap<String, ILaunchConfiguration> map = new HashMap<String, ILaunchConfiguration>();    	
    	for(ILaunchConfiguration c : configs)
    		map.put(c.getName(), c);
    	
    	try{
    		for(String cn : configNames){    		
    			if(map.containsKey(cn)){    			    				
    				if(!monitor.isCanceled()){
    					((ILaunchConfiguration)map.get(cn)).launch(mode, null);       			
    				}
    			}
    		}    		
       	} finally {
    		monitor.done();
    	}      
    }     

}
