package gui.qGang;

import org.eclipse.swt.widgets.Composite;

import orgObjects.Question;

public class QEdit extends Qbox {

	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public QEdit(Composite parent, int style,Question q) {
		super(parent, style,q);
		
		
		qDD.setGrayed(false);
		
		qEdit.setEditable(true);
		
		
		
		//this.layout();
		//this.pack();

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	
}
