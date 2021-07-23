package gui.qGang;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;

import gui.Board;
import orgObjects.Question;

public class QMedia extends Qbox {

	
	
	private String fileName;
	private String fullPath;
	private String fileExtension;
	private Board grandparent;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public QMedia(Composite parent, int style,Question q) {
		super(parent, style, q);
		Board grandparent=(Board) parent.getParent().getParent().getParent();
		FileDialog chooser=new FileDialog(parent.getShell());
	
		
		if(q.getTypeDetails().contains("P")) {
			fileExtension=".jpg";
		}else if(q.getTypeDetails().contains("S")){
			fileExtension=".mp3";
		}else {
			fileExtension=".*";
			System.out.println("Something is not right, I'm rending a QMedia for a Q with type details: "+q.getTypeDetails());
		}
		chooser.setFilterExtensions(new String[]{"*"+fileExtension});
		
		qDD.setGrayed(false);
		
		qAnswer.setEditable(true);
		
		openButton.setVisible(true);
		openButton.setText("Select Media File");
		
		GridData openLayoutDetails=new GridData(GridData.FILL,GridData.FILL,true,false,3,1);
		openLayoutDetails.widthHint=this.width;
		openButton.setLayoutData(openLayoutDetails);
		this.pack();
		
		qEdit.setEditable(true);
		GridData qEditLayoutDetails=new GridData(GridData.FILL,GridData.FILL,false,false,3,1);
		qEditLayoutDetails.heightHint=qEdit.getBounds().height-(openButton.getBounds().height+vSpace);
		qEditLayoutDetails.widthHint=width;
		qEdit.setLayoutData(qEditLayoutDetails);
		
		this.layout(true);
		this.pack();
		openButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				try {
				chooser.open();
				if(chooser.getFileName().length()>1) {
					fileName=chooser.getFileName().substring(0,(chooser.getFileName().lastIndexOf('.')));
					fullPath=chooser.getFilterPath()+"\\"+chooser.getFileName();
					qEdit.setText(fullPath);
					if(grandparent.useFileNames) {
						setAnswerToFileName();
					}
				}
			
			}catch(Exception err){
				//I don't think I actually want to do anything...just like...exist
				System.out.print("Ope that didn't work");
			}

		}});

	}
	
	public void setAnswerToFileName() {
		if(fileName!=null) {
			qAnswer.setText(fileName);
		}else if(qEdit.getText().contains(fileExtension)){
			int lastIndex=qEdit.getText().lastIndexOf('.')-1;
			int firstIndex=qEdit.getText().lastIndexOf('\\')+1;
			qAnswer.setText(qEdit.getText(firstIndex,lastIndex));
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
}
