package org.vendor.composite.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class ImplContentProvider implements ITreeContentProvider {

	private static final Object[] EMPTY_ARRAY = new Object[0];
	
	private List<ILaunchConfiguration> configurations;

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] getElements(Object inputElement) {
		configurations = (List<ILaunchConfiguration>) inputElement;
		ArrayList<ILaunchConfiguration> list = new ArrayList<ILaunchConfiguration>();
		if(configurations.size() > 0){
			for (ILaunchConfiguration config : configurations)			
				list.add(config);	
			return list.toArray();
		}
		return EMPTY_ARRAY;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
			return null;
	}

	@Override
	public Object getParent(Object element) {
			return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return false;
	}
}

