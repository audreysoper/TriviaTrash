package gui;

import gui.cGang.CGroup;
import gui.tests.ZipLocalizer;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.FontDialog;

import java.io.File;
import java.io.PrintWriter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import orgObjects.Category;
import orgObjects.Question;
import static Resources.Colors.*;


public class Board extends Composite {

	public boolean useFileNames;
	public boolean useRelativePaths;
	public boolean newBoardIsMC;
	public boolean pathingIsCatDependent;
	private boolean mcToggle;
	private CGroup[] catGroups;
	private Text[] titles;

	private Text fQtextBox;
	private Text fQanswerBox;
	public File currentOpenDoc;

	private String titleText = "Fancy Question Editor";
	private final String version = "version 2023.10.25";
	public String homeFolder;
	public String pathToHome;
	public String textStyle;
	public FontData fontInfo;
	public int qBoxEditHeightDefault;
	public int qBoxAnsHeightDefault;
	public int qBoxMinWidth;

	public final static int charLimit = 275;

	public SelectionListener saveASAdapter;

	{
		saveASAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog chooser = new FileDialog((Shell) e.widget.getData(), SWT.SAVE);
				try {
					chooser.setFilterExtensions(new String[]{"*.txt"});
					chooser.open();
					String fileName = chooser.getFileName();

					if (fileName.length() > 1) {
						// if it doesn't end with the extension, then add the extension
						if (!fileName.endsWith(chooser.getFilterExtensions()[0].substring(1))) {
							fileName = fileName + chooser.getFilterExtensions()[0].substring(1);
						}
						File output = new File(chooser.getFilterPath() + "\\" + fileName);
						exportToFile(output);
						Shell shee = getShell();

						new Board(shee, SWT.NONE, AppBoard.parseBoard(output), output, mcToggle);
						dispose();
					}
				} catch (Exception err) {
					// no forreal I don't want to do anything
					err.printStackTrace();
				}

			}

		};
	}

	public ModifyListener textChanged = new ModifyListener() {
		@Override
		public void modifyText(ModifyEvent e) {
			if (((String) e.widget.getData()).contains("home")) {
				homeFolder = ((Text) e.widget).getText();
			} else if (((String) e.widget.getData()).contains("path")) {
				pathToHome = ((Text) e.widget).getText();
			}

		}

	};

	public TraverseListener tabLister= e -> {
		if (e.detail == SWT.TRAVERSE_TAB_NEXT || e.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
			e.doit = true;
		}
	};



	/**
	 * Create the composite.
	 *
	 * @param parent
	 * @param style
	 * @param catObjs
	 */
	public Board(Composite parent, int style, Category[] catObjs) {
		super(parent, style);
		homeFolder = "";
		pathToHome = "";
		useRelativePaths = false;
		textStyle = "#MS Gothic#28#True#False#16645837#";
		populateBoard(catObjs);
	}

	// CONSTRUCTOR FOR OPENING FILES
	public Board(Composite parent, int style, Category[] catObjs, File source, boolean isMC) {
		super(parent, style);
		homeFolder = File.separatorChar + source.getParentFile().getName() + File.separatorChar;
		pathToHome = source.getParentFile().getParent();
		this.mcToggle = isMC;
		currentOpenDoc = source;
		textStyle = catObjs[catObjs.length - 1].getQuestions()[0].getFormat();

		populateBoard(catObjs);
	}

	// CONSTRUCTOR FOR GENERATING FILES FROM CATS
	public Board(Composite parent, int style, Category[] catObjs, boolean isMC) {
		super(parent, style);
		homeFolder = "";
		pathToHome = "";
		this.mcToggle = isMC;
		textStyle = catObjs[catObjs.length - 1].getQuestions()[0].getFormat();
		if (textStyle.length() < 2) {
			textStyle = "#MS Gothic#28#True#False#16645837#";
		}

		populateBoard(catObjs);
	}

	private void getDefaults() {
		Text defaultTester= new Text(this,SWT.MULTI);
		GC gc = new GC(defaultTester);
		FontMetrics fm = gc.getFontMetrics();
		qBoxMinWidth = (int) (fm.getAverageCharacterWidth()*15);
		gc.dispose();
		int lineHeight=defaultTester.getLineHeight();
		if(mcToggle) {
			qBoxEditHeightDefault=lineHeight*3;
			qBoxAnsHeightDefault =lineHeight*5+lineHeight/2;
		}else{
			qBoxEditHeightDefault=lineHeight*4;
			qBoxAnsHeightDefault =lineHeight*3+lineHeight/2;

		}

		defaultTester.dispose();

	}
	private void populateBoard(Category[] catObjs) {
		getDefaults();
		entireHeaders(catObjs);

		// START THE MAIN SECTION OF THE WINDOW
		ScrolledComposite scrollContainer = new ScrolledComposite(this, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		scrollContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1));
		scrollContainer.setExpandHorizontal(true);


		Composite dummyContainer = new Composite(scrollContainer, SWT.NONE);
		GridLayout dummyLay = new GridLayout(6, true);
		dummyLay.horizontalSpacing = 10;
		dummyContainer.setLayout(dummyLay);
		dummyContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		dummyContainer.setBackground(darkerLilac);

		scrollContainer.setContent(dummyContainer);
		catGroups = new CGroup[6];
		for (int i=0;i<catGroups.length;i++) {
			catGroups[i]=new CGroup(dummyContainer,i+1,this,catObjs[i]);
		}


		dummyContainer.layout();
		dummyContainer.pack();
		scrollContainer.pack();
		this.layout();
		this.pack();

	}



	protected void useFileNameAction(Button ufnButton) {
		if (ufnButton.getSelection()) { // true= selected & want to use file name
			useFileNames = true;
			for (CGroup c:catGroups) {
				c.answersAreFileName();
			}
		} else {
			useFileNames = false;
		}

	}



	protected void exportToFile(File output) {
		int ddCount = 0;
		try {
			PrintWriter fileOut = new PrintWriter(output);

			for (CGroup catGroup : catGroups) {
				catGroup.printCat(fileOut);

			}
			// after all the loops are done at the very end add the FINAL QUESTION
			fileOut.println(fQtextBox.getText().replaceAll("\\n", " ") + " ");
			fileOut.println(" " + fQanswerBox.getText().replaceAll("\\n", " ") + "^^^^");

			fileOut.close();

		} catch (Exception err) {
			// TODO Auto-generated catch block
			err.printStackTrace();
		}
	}


	public boolean isMc() {
		return mcToggle;
	}


	//METHODS TO SLIM DOWN 'POPULATE BOARD'
	private Button makeButton(Composite parent, String text, SelectionListener listener){
		Button butt = new Button(parent, SWT.PUSH);
		butt.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		butt.setText(text);
		butt.setData(this.getShell());
		butt.addSelectionListener(listener);
		butt.setFont(bigButtons);
		return butt;
	}

	private Group optionsGroup(Group optionsHeader, Category[] catObjs){
		optionsHeader.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		GridLayout optionsInnerLayout = new GridLayout(3, false);
		optionsInnerLayout.horizontalSpacing = 10;
		optionsInnerLayout.verticalSpacing = 10;
		optionsHeader.setLayout(optionsInnerLayout);
		optionsHeader.setText("Options");

		Button ufnButton = new Button(optionsHeader, SWT.CHECK | SWT.WRAP);
		// ufnButton.setLayoutData(new RowData(SWT.DEFAULT,50));

		ufnButton.setText("File names as answers?");
		ufnButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1));
		ufnButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				useFileNameAction((Button) e.widget);

			}
		});

		Button catsButton = new Button(optionsHeader, SWT.PUSH);
		// ufnButton.setLayoutData(new RowData(SWT.DEFAULT,50));

		catsButton.setText("Import Categories");
		catsButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1));
		catsButton.setData(this.getShell());
		SelectionListener categoryChangerAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//
				FileDialog chooser = new FileDialog((Shell) e.widget.getData(), SWT.OPEN);
				try {
					chooser.setFilterExtensions(new String[] { "*.txt" });
					chooser.open();

					if (chooser.getFileName().length() > 1) {
						File source = new File(chooser.getFilterPath() + "\\" + chooser.getFileName());
						new CategoryChooser(catObjs, AppBoard.parseBoard(source));
					}
				} catch (Exception err) {
					// nuthin
					err.printStackTrace();
				}
			}
		};
		catsButton.addSelectionListener(categoryChangerAdapter);

		// Text options
		Label fontFaceLabel = new Label(optionsHeader, SWT.WRAP | SWT.CENTER);
		Label fontSizeLabel = new Label(optionsHeader, SWT.WRAP | SWT.CENTER);

		if(!textStyle.contains("#")) {
			textStyle="#MS Gothic#28#True#False#16645837#";
		}
		Button selectFont = new Button(optionsHeader, SWT.PUSH);
		selectFont.setText("Customize");
		selectFont.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FontDialog choose = new FontDialog(((Control) e.widget).getShell());
				choose.setFontList(new FontData[] { fontInfo });
				choose.setEffectsVisible(false);
				try {
					fontInfo = choose.open();
					textStyle = "#" + fontInfo.getName() + "#" + fontInfo.getHeight() + "#True#False#16645837#";
					fontFaceLabel.setText("Font: " + fontInfo.getName());
					fontSizeLabel.setText("Size: " + fontInfo.getHeight());
					fontInfo.setHeight(10);
					fontFaceLabel.setFont(new Font(lilac.getDevice(), fontInfo));

					fontFaceLabel.redraw();
					fontFaceLabel.update();
					layout();
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});
	return optionsHeader;
	}

	private Composite saveToggle(Composite parent){
		Composite newBoardToggle = new Composite(parent, SWT.NONE);
		newBoardToggle.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,3,1));
		newBoardToggle.setLayout(new GridLayout(3, false));
		Label boardToggleL=new Label(newBoardToggle,SWT.NONE);
		boardToggleL.setText("New board type:");

		Button newAsMC = new Button(newBoardToggle, SWT.TOGGLE);
		newAsMC.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		newAsMC.setText("Multiple Choice");
		newAsMC.setData(true);
		newAsMC.setSelection(mcToggle);

		Button newAsOA = new Button(newBoardToggle, SWT.TOGGLE);
		newAsOA.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		newAsOA.setText("Open Answer");
		newAsOA.setData(false);
		newAsOA.setSelection(!mcToggle);
		if(newAsOA.getSelection()) {
			newAsOA.setBackground(lilac);
		}else {
			newAsMC.setBackground(lilac);
		}


		SelectionAdapter newBoardToggleListener=new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button selected=(Button)e.widget;
				selected.setSelection(true);
				//selected.setFont(bigButtons);
				selected.setBackground(lilac);

				newBoardIsMC=(boolean) selected.getData();
				if(newBoardIsMC) {
					newAsOA.setSelection(false);
					newAsOA.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
					newAsOA.setFont(null);
				}else {
					newAsMC.setSelection(false);
					newAsMC.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
					newAsMC.setFont(null);
				}

				newBoardToggle.layout();

			}
		};


		newAsMC.addSelectionListener(newBoardToggleListener);
		newAsOA.addSelectionListener(newBoardToggleListener);
return newBoardToggle;
	}

	private void finalQSection(Composite parent, Category[] catObjs){
		Group finalQHeader = new Group(parent, SWT.NONE);

		finalQHeader.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 2, 1));
		GridLayout fqSectionLayout = new GridLayout(2, false);
		finalQHeader.setLayout(fqSectionLayout);

		Label finalQSecText = new Label(finalQHeader, SWT.NONE);
		finalQSecText.setText("Final Question");
		finalQSecText.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
		finalQSecText.setLayoutData(new GridData(GridData.CENTER, GridData.BEGINNING, false, false, 2, 1));

		Label fQtextLabel = new Label(finalQHeader, SWT.NONE);
		fQtextLabel.setText("QUESTION:");
		fQtextLabel.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.NORMAL));

		Question finalQ = catObjs[6].getQuestions()[0];
		fQtextBox = new Text(finalQHeader, SWT.MULTI | SWT.WRAP);
		fQtextBox.setText(finalQ.getQuestion());
		fQtextBox.setMessage("Type the text of Final Question here");
		fQtextBox.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		fQtextBox.addTraverseListener(tabLister);
		((GridData) fQtextBox.getLayoutData()).heightHint = fQtextBox.getLineHeight() * 2 + 5;

		Label fQanswerLabel = new Label(finalQHeader, SWT.NONE);
		fQanswerLabel.setText("ANSWER:");
		fQanswerLabel.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.NORMAL));
		fQanswerBox = new Text(finalQHeader, SWT.MULTI | SWT.WRAP);
		fQanswerBox.setText(finalQ.getAnswer());
		fQanswerBox.setMessage("Type the answer to the Final Question here");
		fQanswerBox.setToolTipText("Type the answer to the Final Question here");
		fQanswerBox.addTraverseListener(tabLister);
		fQanswerBox.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		((GridData) fQanswerBox.getLayoutData()).heightHint = fQtextBox.getLineHeight() + 5;
	}

	private Group pathHead(Board board) {
		Group pathingHeader = new Group(board, SWT.NONE);
		pathingHeader.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 1, 1));
		GridLayout pathingSectionLayout = new GridLayout(3, false);
		pathingSectionLayout.marginHeight = 5;
		pathingSectionLayout.marginWidth = 10;
		pathingHeader.setLayout(pathingSectionLayout);
		pathingHeader.setText("Pathing Options");
		// CHILD 0
		Button rpButton = new Button(pathingHeader, SWT.TOGGLE | SWT.WRAP);
		rpButton.setLayoutData(new GridData(GridData.FILL, GridData.FILL,false,true,2,1));
		rpButton.setSelection(useRelativePaths);
		rpButton.setText("Show relative paths?");
		rpButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				useRelativePaths = !useRelativePaths;
				for (CGroup c:catGroups) {
					c.togglePathViewType();
				}

				if (useRelativePaths)
					rpButton.setText("Show full paths?");
				else
					rpButton.setText("Show relative paths?");

			}
		});

		Button catDependent = new Button(pathingHeader, SWT.CHECK | SWT.WRAP);
		catDependent.setText("Category Dependent Home Folders");
		catDependent.setLayoutData(new GridData(GridData.END, GridData.FILL, true, false));
		catDependent.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button w=(Button)e.widget;
				togglePathingIsCatDependent(w.getSelection());
			}
		});
		// CHILD 1
		Label hfInputLabel = new Label(pathingHeader, SWT.NONE);
		hfInputLabel.setText("Common Folder:");
		// CHILD 2
		Text homeFolderInput = new Text(pathingHeader, SWT.SINGLE | SWT.BORDER);
		homeFolderInput.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 2, 1));
		homeFolderInput.setText(homeFolder);
		homeFolderInput.setData("homeInput");
		homeFolderInput.addModifyListener(textChanged);
		// CHILD 3
		Label pthfInputLabel = new Label(pathingHeader, SWT.NONE);
		pthfInputLabel.setText("New Path To Common Folder:");
		// CHILD 4
		Text pathToInput = new Text(pathingHeader, SWT.SINGLE | SWT.BORDER);
		pathToInput.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 2, 1));
		pathToInput.setText(pathToHome);
		pathToInput.setData("pathInput");
		// CHILD 5
		Button replacePathsButton = new Button(pathingHeader, SWT.PUSH);
		replacePathsButton.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 3, 1));
		replacePathsButton.setText("Replace Path to Common Folder");
		replacePathsButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				pathToHome = pathToInput.getText();
				for (CGroup c:catGroups) {
					c.swapPathFronts();

				}
			}
		});
		return pathingHeader;
	}

	private void togglePathingIsCatDependent(boolean value) {
		pathingIsCatDependent=value;
		for (CGroup c:catGroups) {
			c.toggleCommonsView();
			c.setHomeFolder();
			c.layout();
		}
		getShell().layout();
		getShell().pack();
	}

	private void entireHeaders(Category[] catObjs){
		setBackgroundMode(SWT.INHERIT_DEFAULT);
		setLayout(new GridLayout(5, false));
		Composite top = new Composite(this, SWT.NONE);
		top.setLayout(new GridLayout(3, true));
		top.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 5, 1));

		// EXPERIMENTING WITH COMPOSITE FOR BUTTONS
		Composite buttons = new Composite(top, SWT.NONE);
		buttons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		GridLayout buttonsLayout = new GridLayout(4, false);
		buttons.setLayout(buttonsLayout);
		if (currentOpenDoc != null) {
			titleText = titleText + currentOpenDoc.getName();
			Button save = new Button(buttons, SWT.PUSH);
			// save.setLayoutData(new RowData(SWT.DEFAULT,40));
			save.setText("SAVE - " + currentOpenDoc.getName());
			save.setFont(bigButtons);
			save.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					exportToFile(currentOpenDoc);

				}
			});
		}

		// Save Button
		Button saveAS = makeButton(buttons,"SAVE AS...",saveASAdapter);

		// Open Button
		Button open =makeButton(buttons,"OPEN",AppBoard.openBoardAdapter);

		// New button
		Button newBoard =makeButton(buttons,"NEW BOARD",AppBoard.newBoardAdapter);


		// New button

		Composite newBoardToggle =saveToggle(buttons);

		if (!AppBoard.userPrefs.getBoolean("mcEnabled", false)) {
			((GridData) newBoardToggle.getLayoutData()).exclude = true;
		}


		//TITLE - which doesn't have it's own layout....it's just hangin
		Label title = new Label(top, SWT.NONE);
		// title.setForeground(SWTResourceManager.getColor(204, 153, 255));
		title.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		title.setText(titleText);
		title.setAlignment(SWT.CENTER);
		title.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.BOLD));


		//RIGHT SIDE HEADER:
		Composite rightHeader = new Composite(top, SWT.NONE);
		rightHeader.setLayoutData(new GridData(SWT.END, SWT.FILL, false, false, 1, 1));
		GridLayout rightHeaderLayout = new GridLayout(1, false);
		rightHeader.setLayout(rightHeaderLayout);

		Label ver = new Label(rightHeader, SWT.NONE);
		ver.setLayoutData(new GridData(SWT.END, GridData.FILL, false, false, 1, 1));
		// ((GridData)ver.getLayoutData()).
		ver.setText(version);
		ver.setAlignment(SWT.LEFT);
		ver.setFont(SWTResourceManager.getFont("Verdana", 9, SWT.NORMAL));

		//IMPORT FROM ZIP
		Button localizeZip = new Button(rightHeader, SWT.PUSH);
		localizeZip.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		localizeZip.setText("LOCALIZE ZIP FROM EXCESS EDITOR");
		localizeZip.setFont(bigButtons);
		localizeZip.setVisible(AppBoard.userPrefs.getBoolean("localize", false));





		SelectionListener localizeAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new ZipLocalizer();
			}
		};
		localizeZip.addSelectionListener(localizeAdapter);



		// create group OPTIONS HEADER that (only) live in
		Group optionsHeader = new Group(this, SWT.NONE);
		optionsHeader=optionsGroup(optionsHeader, catObjs);

		/*
		 * Label reminder = new Label(optionsHeader, SWT.WRAP | SWT.CENTER); //
		 * reminder.setLayoutData(new RowData(SWT.DEFAULT,50)); GridData reminderLayData
		 * = new GridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1); reminder.
		 * setText("REMINDER: \nCheck boards in Trivia Board Pro Editor BEFORE playing them"
		 * ); reminder.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		 * reminderLayData.widthHint = reminder.computeSize(SWT.DEFAULT, SWT.DEFAULT).x
		 * * 2 / 3; reminder.setLayoutData(reminderLayData);
		 * reminder.setBackground(mixedBG);
		 */


		// fINAL QUESTION SECTION
		finalQSection(this, catObjs);

		// PATHING section
		Group pathingHeader=pathHead(this);


		//Preference listener shit

		if (!AppBoard.userPrefs.getBoolean("advancedPathing", false)) {
			((GridData) pathingHeader.getLayoutData()).exclude = true;
			((GridData) optionsHeader.getLayoutData()).horizontalSpan = 2;
		}
		AppBoard.userPrefs.addPreferenceChangeListener(evt -> {
			System.out.println();
			Display.getDefault().asyncExec(() -> {
				if (evt.getKey().contains("advancedPathing")) {
					((GridData) pathingHeader.getLayoutData()).exclude = evt.getNewValue().contains("false");
				}
				else if(evt.getKey().contains("mcEnabled")) {
					((GridData) newBoardToggle.getLayoutData()).exclude = evt.getNewValue().contains("false");
				}
				else if(evt.getKey().contains("localize")){
					localizeZip.setVisible(evt.getNewValue().contains("true"));
				}
				layout();
			});

		});
	}
}
