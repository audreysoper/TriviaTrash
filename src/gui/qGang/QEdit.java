package gui.qGang;

import gui.cGang.CGroup;
import org.eclipse.swt.widgets.Composite;

import orgObjects.Question;

public class QEdit extends Qbox {

	
	/**
	 * Create the composite.
	 * @param parent
	 */
	public QEdit(CGroup parent, Question q, int index) {
		super(parent,q,index);
		
	
		
		qEdit.setEditable(true);
		
		
		
		this.layout();
		//this.pack();

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	
}
