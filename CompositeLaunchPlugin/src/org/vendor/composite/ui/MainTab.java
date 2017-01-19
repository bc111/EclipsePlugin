package org.vendor.composite.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.TreeSet;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import org.vendor.composite.CompositeConfiguration; // final static variables defined


public class MainTab extends AbstractLaunchConfigurationTab {
	        	
	private List<ILaunchConfiguration> configsAvailable;
	private List<ILaunchConfiguration> configsToLaunch;
		
	/* decision for tree: supposed to show better performance than table */		
	TreeViewer tvLeft_configsAvailable;
	TreeViewer tvRight_configsToLaunch;
		
	ILaunchConfiguration selectedItem = null;
	private boolean isModeImplemented = true;
    	    	
	private String mode /*= ILaunchManager.RUN_MODE, DEBUG_MODE*/;

	public MainTab(String mode){
		this.mode = mode;
    }
    	
	private void init(){
		configsAvailable = getAllowedConfigs();
		configsToLaunch = new ArrayList<ILaunchConfiguration>();
    		
		isModeImplemented = (mode.equals(ILaunchManager.RUN_MODE) || mode.equals(ILaunchManager.DEBUG_MODE));
    }
    	
	@Override
    public void createControl(Composite parent) {  
		init();
        	
        Composite main = new Composite(parent, SWT.NONE);  	                       	             
        GridLayout layout = new GridLayout(2, true);
        layout.marginHeight = layout.marginWidth = 20;
        main.setLayout(layout);
                                  
        tvLeft_configsAvailable = new TreeViewer(main, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE);
        setLayout(tvLeft_configsAvailable);         	
        tvLeft_configsAvailable.setContentProvider(new ImplContentProvider());
        tvLeft_configsAvailable.setLabelProvider(new ImplLabelProvider());              	
        tvLeft_configsAvailable.setInput(configsAvailable);
          	          	            	
        tvRight_configsToLaunch = new TreeViewer(main, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE);
        setLayout(tvRight_configsToLaunch);        	
        tvRight_configsToLaunch.setContentProvider(new ImplContentProvider());
        tvRight_configsToLaunch.setLabelProvider(new ImplLabelProvider());           	         	
        tvRight_configsToLaunch.setInput(configsToLaunch);
         	         	                     	
        tvLeft_configsAvailable.addDoubleClickListener(new IDoubleClickListener() {
        	@Override
            public void doubleClick(DoubleClickEvent event) {
        		TreeSelection selection = (TreeSelection) event.getSelection();                       
                selectedItem = (ILaunchConfiguration)selection.getFirstElement();
                addToSelectedConfigsDblClickAction();
            }
        });
        		         		    	         		
        tvRight_configsToLaunch.addDoubleClickListener(new IDoubleClickListener() {
        	@Override
            public void doubleClick(DoubleClickEvent event) {
        		TreeSelection selection = (TreeSelection) event.getSelection();
                selectedItem = (ILaunchConfiguration)selection.getFirstElement();
                removeFromSelectedConfigsDblClickAction();
            }
        });            	         	
         	
       	setControl(main);
	}
        
    private void updateMethod(){        	        	
    	tvLeft_configsAvailable.setInput(configsAvailable);           
        tvLeft_configsAvailable.refresh();
             
        tvRight_configsToLaunch.setInput(configsToLaunch);
        tvRight_configsToLaunch.refresh();
  			       			
        updateLaunchConfigurationDialog();	
    }
        
    private void addToSelectedConfigsDblClickAction(){        	
    	if(selectedItem != null){
    		configsAvailable = removeFromAvailableConfigs(selectedItem);
        	configsToLaunch = addtoToLaunchConfigs(selectedItem);                          
            selectedItem = null;
        }             
        updateMethod();        	
    }
        
    private void removeFromSelectedConfigsDblClickAction(){        	
    	if(selectedItem != null){        	
    		configsToLaunch = removeFromToLaunchConfigs(selectedItem);
        	configsAvailable = addToAvailableConfigs(selectedItem);        	 
            selectedItem = null;
        }
        updateMethod();              
    }
        
    private void setLayout(TreeViewer tv){
    	final GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalAlignment = GridData.FILL;        		
        tv.getTree().setLayoutData(gridData);
    }
        
     // --------- operations on lists of configurations -------------   
        
    /* Assumption: user prefers to see the configs to select in a sorting order:
     * sorted by type and name
     * >> TreeSet, with comparator please see below: LcTreeSetComparator
     */                
    private List<ILaunchConfiguration> removeFromAvailableConfigs(ILaunchConfiguration lc){        	
    	TreeSet<ILaunchConfiguration> set = new TreeSet<ILaunchConfiguration>(new LcTreeSetComparator());
        set.addAll(configsAvailable); 
        set.remove(lc);
        return new ArrayList<ILaunchConfiguration>(set);   
    }
    /* Assumption: user prefers to see the configs to select in a sorting order:
     * sorted by type and name
     * >> TreeSet, with comparator please see below: LcTreeSetComparator
     */ 
    private List<ILaunchConfiguration> addToAvailableConfigs(ILaunchConfiguration lc){        	
    	TreeSet<ILaunchConfiguration> set = new TreeSet<ILaunchConfiguration>(new LcTreeSetComparator());
        set.addAll(configsAvailable); 
        set.add(lc);
        return new ArrayList<ILaunchConfiguration>(set);   
    }
        
    /* Assumption: user prefers to see the selected configs in the order as they were selected:
     * selection (list insertion) order
     * >> LinkedHashSet
     */
    private List<ILaunchConfiguration> addtoToLaunchConfigs(ILaunchConfiguration lc){ 
    	LinkedHashSet<ILaunchConfiguration> set = new LinkedHashSet<ILaunchConfiguration>(configsToLaunch);
        set.add(lc);
        return new ArrayList<ILaunchConfiguration>(set);   
    }
        
    /* Assumption: user prefers to see the selected configs in the order as they were selected:
     * selection (list insertion) order
     * >> LinkedHashSet
     */
    private List<ILaunchConfiguration> removeFromToLaunchConfigs(ILaunchConfiguration lc){ 
    	LinkedHashSet<ILaunchConfiguration> set = new LinkedHashSet<ILaunchConfiguration>(configsToLaunch);
        set.remove(lc);
        return new ArrayList<ILaunchConfiguration>(set);   
    }
        
        
    // --------- allowed configurations -----------------
        
    /* Allowed configurations:
     * - supports the current mode of run or debug
     * - is not of type composite (= of this plugin) itself
     */
    private List<ILaunchConfiguration> getAllowedConfigs(){
    	if(!isModeImplemented) return new ArrayList<ILaunchConfiguration>();
        	
        List<ILaunchConfiguration> allConfigs = getAllConfigsInIDE();        	
        TreeSet<ILaunchConfiguration> set = new TreeSet<ILaunchConfiguration>(new LcTreeSetComparator());
        for(ILaunchConfiguration lc : allConfigs){
        	if(isConfigurationAllowed(lc))
        		set.add(lc);
        }
        return new ArrayList<ILaunchConfiguration>(set);        	
    }
        
    /* Allowed configuration:
     * - supports the current mode of run or debug
     * - is not of type composite (= of this plugin) itself
     */
    private boolean isConfigurationAllowed(ILaunchConfiguration lc){          	
    	try{        		
    		if(!lc.supportsMode(mode)) return false;
        	if(lc.getType().getIdentifier().equals(CompositeConfiguration.PLUGIN_XML_ID)) return false;
            	    		
        }catch(CoreException e){
        	//includes: mode == null, lc == null
        	e.printStackTrace();
        	return false;
        }
		return true;        	
    }
                
    private List<ILaunchConfiguration> getAllConfigsInIDE(){
    	List<ILaunchConfiguration> allConfigs = null;
        	
		try { allConfigs = Arrays.asList(DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurations());
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return allConfigs;
    }
                   
    @Override
    public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
    }

    @Override
    public void initializeFrom(ILaunchConfiguration configuration) {
    	configsAvailable = getAllowedConfigs();
	}        
        
    /* Attributes are set, these attributes are 'transfered' to the launch-method
     * (please see class CompositeConfiguration).
     * Attributes are transfered inform of a list of Strings,
     * that list consists of the names of the configurations selected for launch.
     */
    @Override
    public void performApply(ILaunchConfigurationWorkingCopy configuration) {        	
    	configuration.removeAttribute(CompositeConfiguration.ATTRIBUTE_CONFIGS_TO_LAUNCH);
        	
        List<String> attribute = new ArrayList<String>();
        	
        for(ILaunchConfiguration config : configsToLaunch)
        	attribute.add(config.getName());
        configuration.setAttribute(CompositeConfiguration.ATTRIBUTE_CONFIGS_TO_LAUNCH, attribute);            
    }

    @Override
    public String getName() {
    	return "Configurations to launch (please select/unselect by dbl-click):";
    }
        
    /* Comparator to provide a sorting order of type and name,
     * for the available-configuration-list - please see above        
     */
    class LcTreeSetComparator implements Comparator<ILaunchConfiguration>{        	 
    	@Override
        public int compare(ILaunchConfiguration lc1, ILaunchConfiguration lc2) {
    		try{
    			if(lc1.getType().equals(lc2.getType()))
    				return lc1.getName().compareTo(lc2.getName());
            	else
            		return lc1.getType().getName().compareTo(lc2.getType().getName());
            }catch(CoreException e){
            	return 0;
            }
        }             
    }
          
}

