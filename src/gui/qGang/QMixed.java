package gui.qGang;

import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.wb.swt.SWTResourceManager;

import gui.Board;
import orgObjects.Question;

public class QMixed extends QMedia {

	private String fileName;
	private String fullPath;
	private Board grandparent;
	private String qType;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public QMixed(Composite parent, int style, Question q) {
		super(parent, style, q);
		Board grandparent = (Board) parent.getParent().getParent().getParent();
		//chooser = new FileDialog(parent.getShell());
		if (qType == null && q.getTypeDetails().length() > 0) {
			switch (q.getTypeDetails().charAt(0)) {
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
		} else if (qType == null && q.getTypeDetails().length() < 1) {
			qType = "text";
		}

		// Category types
		typeSelect.setVisible(true);
		typeSelect.setItems(Arrays.copyOfRange(Board.typeNames, 0, 3));
		typeSelect.setText(qType);
		typeSelect.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 3, 1));
		((GridData) typeSelect.getLayoutData()).exclude = false;
		typeSelect.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				Combo w = (Combo) e.widget;
				QMixed parent = (QMixed) w.getParent();
				parent.qType = w.getText();
				parent.q.setType(w.getText());
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
			viewButton.setImage(SWTResourceManager.getImage(Qbox.class, "Zoom16.gif"));
			openButton.setVisible(true);
			viewButton.setVisible(true);
			((GridData) openButton.getLayoutData()).exclude = false;
			((GridData) viewButton.getLayoutData()).exclude = false;
			fileExtension = new String[] { "*.jpg;*.png;*.gif;*.jpeg","*.jpg","*.png","*.gif","*.jpeg"};
			openButton.setText("Select Picture");
			
			break;

		case "audio":
			viewButton.setImage(SWTResourceManager.getImage(Qbox.class, "Volume16.gif"));
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
			((GridData) qEdit.getLayoutData()).heightHint = editHeightDefault
					- (openButton.getBounds().height + typeSelect.getBounds().height +vSpace)/2;

			// SIZE ANSWER BOX TO ACCOMIDATE TYPE SELECT
			if(!isMC) {
			((GridData) qAnswer.getLayoutData()).heightHint = openAnsHeightDefault
					- (openButton.getBounds().height + typeSelect.getBounds().height +vSpace)/2;
			}

		
		} else if (qType.contains("text")) {
			// SIZE QUESTION BOX TO ACCOMIDATE TYPE SELECT
			((GridData) qEdit.getLayoutData()).heightHint = editHeightDefault
					- (typeSelect.getBounds().height/2+vSpace);

			// SIZE ANSWER BOX TO DEFAULT
			((GridData) qAnswer.getLayoutData()).heightHint = openAnsHeightDefault- (typeSelect.getBounds().height/2);
			
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
	
}
