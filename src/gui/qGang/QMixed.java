package gui.qGang;

import java.util.Arrays;

import Resources.Colors;
import gui.cGang.CGroup;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.wb.swt.SWTResourceManager;

import orgObjects.Question;

public class QMixed extends QMedia {
	private String qType;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 */
	public QMixed(CGroup parent, Question q, int index) {
		super(parent, q,index);
		//chooser = new FileDialog(parent.getShell());
		if (qType == null && q.getFormat().length() > 0) {
			switch (q.getFormat().charAt(0)) {
			case 'P':
				qType = "picture";
				break;
			case 'S':
				qType = "audio";
				break;
			case 'T':
				qType = "text";
				break;
			}
		} else if (qType == null && q.getFormat().length() < 1) {
			qType = "text";
		}

		// Category types
		typeSelect.setVisible(true);
		typeSelect.setItems(Arrays.copyOfRange(catDad.typeNames, 0, 3));
		typeSelect.setText(qType);
		typeSelect.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 3, 1));
		((GridData) typeSelect.getLayoutData()).exclude = false;
		typeSelect.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				Combo w = (Combo) e.widget;
				QMixed parent = (QMixed) w.getParent();
				parent.qType = w.getText();;
				setupForType();

			}

		});
		setupForType();

		/*
		 * qDD.setGrayed(false); qEdit.setEditable(true); qAnswer.setEditable(true);
		 * 
		 * 
		 * openButton.addSelectionListener(new SelectionAdapter() {
		 * 
		 * @Override public void widgetSelected(SelectionEvent e) { // TODO
		 * Auto-generated method stub try { chooser.open(); if
		 * (chooser.getFileName().length() > 1) { fileName =
		 * chooser.getFileName().substring(0, (chooser.getFileName().lastIndexOf('.')));
		 * fullPath = chooser.getFilterPath() + "\\" + chooser.getFileName();
		 * qEdit.setText(fullPath); if (grandparent.useFileNames) {
		 * setAnswerToFileName(); } }
		 * 
		 * } catch (Exception err) { // I don't think I actually want to do
		 * anything...just like...exist
		 * System.out.print("Ope I couldn't open the chooser"); }
		 * 
		 * } });
		 */

	}

	public void setupForType() {
		switch (qType) {
		case "picture":
			viewButton.setImage(SWTResourceManager.getImage(Colors.class, "Zoom16.gif"));
			openButton.setVisible(true);
			viewButton.setVisible(true);
			((GridData) openButton.getLayoutData()).exclude = false;
			((GridData) viewButton.getLayoutData()).exclude = false;
			fileExtension = new String[] { "*.jpg;*.png;*.gif;*.jpeg","*.jpg","*.png","*.gif","*.jpeg"};
			openButton.setText("Select Picture");
			
			break;

		case "audio":
			viewButton.setImage(SWTResourceManager.getImage(Colors.class, "Volume16.gif"));
			openButton.setVisible(true);
			viewButton.setVisible(true);
			((GridData) openButton.getLayoutData()).exclude = false;
			((GridData) viewButton.getLayoutData()).exclude = false;
			fileExtension = new String[] {"*.mp3"};
			openButton.setText("Select Audio");
			
			break;
		case "text":
			openButton.setVisible(false);
			((GridData) openButton.getLayoutData()).exclude = true;
			viewButton.setVisible(false);
			((GridData) viewButton.getLayoutData()).exclude = true;
			break;
		}
		chooser.setFilterExtensions(fileExtension);
		//this.layout(true);
		//this.pack();
		if (qType.contains("audio") || qType.contains("pic")) {

			// SIZE QUESTION BOX TO ACCOMIDATE MEDIA BUTTON
			((GridData) qEdit.getLayoutData()).heightHint = boardFather.qBoxEditHeightDefault
					- (openButton.getBounds().height + typeSelect.getBounds().height +vSpace)/2;

			// SIZE ANSWER BOX TO ACCOMIDATE TYPE SELECT
			if(!isMC) {
			((GridData) qAnswer.getLayoutData()).heightHint = boardFather.qBoxAnsHeightDefault
					- (openButton.getBounds().height + typeSelect.getBounds().height +vSpace)/2;
			}

		
		} else if (qType.contains("text")) {
			// SIZE QUESTION BOX TO ACCOMIDATE TYPE SELECT
			((GridData) qEdit.getLayoutData()).heightHint = boardFather.qBoxEditHeightDefault
					- (typeSelect.getBounds().height/2+vSpace);

			// SIZE ANSWER BOX TO DEFAULT
			((GridData) qAnswer.getLayoutData()).heightHint = boardFather.qBoxAnsHeightDefault- (typeSelect.getBounds().height/2);
			
		}
		
		this.layout(true);
		//qAnswer.pack();
		//qEdit.pack();
		//this.pack();

	}



	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}


	
	@Override
	public void setNewQObject(Question newQ) {
		super.setNewQObject(newQ);
		  switch (newQ.getFormat().charAt(0)) {
		  case 'P': qType = "picture";
		  break; 
		  case 'S': qType = "audio"; 
		  break; 
		  case 'T': qType = "text"; 
		  break; }
		 
		typeSelect.setText(qType);
		setupForType();
	}
	
	@Override
	public void setRelativePath(String homeFolder) {
		if(!qType.contains("text")){
			super.setRelativePath(homeFolder);
		}
	}
	
	@Override
	public void togglePathView(boolean viewFull){
		if(!qType.contains("text")){
			if (viewFull){
				qEdit.setText(fullPath);
			}else{
				setRelativePath(boardFather.homeFolder);
				qEdit.setText(relativePath);
			}
		}
	}
	
	@Override
	public void swapPathFront(String pathToHome) {
		if(!qType.contains("text")){
			super.swapPathFront(pathToHome);
		}
	}

	@Override
	public boolean hasPath(){
		if (qType.contains("text")) return false;
		else return true;
	}

}
