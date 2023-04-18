package gui;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import orgObjects.Category;

public class CategoryChooser {
	private int numberSelected;

	public SelectionListener toggleButtonAdapter = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			Button clickedButton = (Button) e.widget;
			if (clickedButton.getSelection()) {
				if (numberSelected < 6) {
					clickedButton.setBackground(Board.lilac);
					numberSelected++;
				} else {
					// if they tried to select, but we have enough categories, then UNselect it
					clickedButton.setSelection(false);
				}
			} else {// if we AREN'T trying to select, fuckin dope, that should always be allowed
				clickedButton.setBackground(null);
				numberSelected--;
			}
		}
	};

	public CategoryChooser(Category[] starterCats, Category[] importCats) {
		Button[] catListSelect = new Button[12];
		Shell theWindow = new Shell();
		theWindow.open();
		theWindow.setLayout(new GridLayout(2, false));
		numberSelected = 6;
		Label title = new Label(theWindow, SWT.NONE);
		title.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		title.setText("Category Importer");
		title.setAlignment(SWT.CENTER);
		title.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.BOLD));
		Button clear = new Button(theWindow, SWT.PUSH);
		clear.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		clear.setText("Clear Selected");
		clear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for (Button b : catListSelect) {
					b.setSelection(false);
					b.setBackground(null);
				}
				numberSelected = 0;
			}
		});

		Group catsLeft = new Group(theWindow, SWT.NONE);
		catsLeft.setLayout(new RowLayout(SWT.VERTICAL));
		((RowLayout)catsLeft.getLayout()).fill=true;
		Label catsLeftLabel = new Label(catsLeft, SWT.NONE);
		catsLeftLabel.setText("Current Categories");

		Group catsRight = new Group(theWindow, SWT.NONE);
		catsRight.setLayout(new RowLayout(SWT.VERTICAL));
		((RowLayout)catsRight.getLayout()).fill=true;
		Label catsRightLabel = new Label(catsRight, SWT.NONE);
		catsRightLabel.setText("Importable Categories");
		Category c;
		for (int i = 0; i < 12; i++) {
			if (i % 2 == 0) {
				c=starterCats[i/2];
				catListSelect[i] = new Button(catsLeft, SWT.TOGGLE);
				catListSelect[i].setSelection(true);
				catListSelect[i].setBackground(Board.lilac);
			} else {
				c=importCats[i/2];
				catListSelect[i] = new Button(catsRight, SWT.TOGGLE);
				
			}
			catListSelect[i].setText(c.getName());
			catListSelect[i].setData(c);
			catListSelect[i].addSelectionListener(toggleButtonAdapter);

		}
		
		
		Button submit = new Button(theWindow, SWT.PUSH);
		submit.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		submit.setText("Generate This Board");
		submit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Category[] catsToGen=new Category[7];
				int toGenIndex=0;
				for(Button b:catListSelect) {
					if(b.getSelection()) {
						catsToGen[toGenIndex]=(Category) b.getData();
						toGenIndex++;
					}
				}
				catsToGen[6]=starterCats[6];
				theWindow.dispose();
				AppBoard.openGeneratedBoard(catsToGen);
				
			}
		});
		theWindow.layout();
		theWindow.pack();
	}

}
