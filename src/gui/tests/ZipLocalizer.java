package gui.tests;

import java.io.File;

import gui.AppBoard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import orgObjects.Category;

public class ZipLocalizer {

	protected Shell zipWindow;
	protected File source;
	protected File destination;
	private Text sourceZipText;
	private Text destinationText;
	protected String pathPrefix;

	public ZipLocalizer() {
		zipWindow=new Shell();
		zipWindow.open();
		zipWindow.setLayout(new GridLayout(2, false));
		
		Label title = new Label(zipWindow, SWT.NONE);
		title.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		title.setText("Zip Localizer");
		title.setAlignment(SWT.CENTER);
		title.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.BOLD));
		
		Button selectZip = new Button(zipWindow, SWT.PUSH);
		selectZip.setLayoutData(new GridData(SWT.FILL, SWT.FILL,true,false));
		selectZip.setText("Select .zip file");
		selectZip.addSelectionListener(fileSelect);
		
		sourceZipText = new Text(zipWindow,SWT.SINGLE);
		sourceZipText.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		
		
		Button selectDestination = new Button(zipWindow, SWT.PUSH);
		selectDestination.setLayoutData(new GridData(SWT.FILL, SWT.FILL,true,false));
		selectDestination.setText("Select Destination Folder");
		selectDestination.addSelectionListener(destinationSelect);
		
		destinationText = new Text(zipWindow,SWT.SINGLE);
		destinationText.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		
		
		Button localizeButton = new Button(zipWindow, SWT.PUSH);
		localizeButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL,true,false,2,1));
		localizeButton.setText("Localize");
		localizeButton.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				localize();
			}
		});
		
		zipWindow.layout();
	}
	

	public SelectionListener fileSelect = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			FileDialog zipchooser = new FileDialog(zipWindow, SWT.OPEN);
			try {
				zipchooser.setFilterExtensions(new String[] { "*.zip" });
				String path=zipchooser.open();

				if (zipchooser.getFileName().length() > 1) {
					source = new File(path);
					sourceZipText.setText(source.getPath());
					destinationText.setText(source.getParentFile().getPath());
				}
			} catch (Exception err) {
				// nuthin
				err.printStackTrace();
			}
		}
		
		
	};
	
	public SelectionListener destinationSelect = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			DirectoryDialog destChooser = new DirectoryDialog(zipWindow, SWT.OPEN);
			try {
				String dest=destChooser.open();

				if (dest.length() > 1) {
					destination = new File(dest);
					destinationText.setText(destination.getPath());
				}
			} catch (Exception err) {
				// nuthin
				err.printStackTrace();

			}
		}
		
		
	};


	protected void localize() {
		//to localize we should either open the board and do the path stuff we already have, or...>.> not
		//first we def gotta unzip and move files tho
		this.pathPrefix=destinationText.getText();
		File unzippedBoard=unzip();
		Category[] localizedCats=fixPaths(unzippedBoard);
	}
	protected File unzip() {

		return null;
	}
	
	protected Category[] fixPaths(File board) {
		Category[] initialCats= AppBoard.parseBoard(board);
		//TODO: check if the end of this path and the start of a question file path are the same...

		for (Category c : initialCats) {
			if(!c.isFinal()){
				c.addQPrefix(this.pathPrefix, true);
			}
		}
		
		return initialCats;
	}
	
	protected void openLocalizedBoard(Category[] boardToOpen) {
		zipWindow.dispose();
		//boardToOpen
		//AppBoard.openGeneratedBoard();
		
	}

}
