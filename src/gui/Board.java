package gui;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.FontDialog;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;

import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.wb.swt.SWTResourceManager;

import gui.qGang.QEdit;
import gui.qGang.QMedia;
import gui.qGang.QMixed;
import gui.qGang.Qbox;
import orgObjects.Category;
import orgObjects.Question;

public class Board extends Composite {

	private final int qIndexInGroup=3; //Use this for noting what index questions start at in category Group
	public boolean useFileNames;
	public boolean useRelativePaths;
	private Composite parent;
	private boolean mcToggle;
	private Group[] catGroups;
	private Text[] titles;
	public final static String[] typeNames=new String[]{"text","picture","audio","mixed"};
	private Text fQtextBox;
	private Text fQanswerBox;
	public int boxW;
	public int boxH;
	public File currentOpenDoc;

	private String titleText="Fancy Question Editor";
	private String version = "version 22.4.18";
	public String homeFolder;
	public String pathToHome;
	public String textStyle;
	public FontData fontInfo;
	
	public final static int charLimit=275;


	public final static Color audioBG= new Color(255, 229, 249);//light pink
	public final static Color picBG= new Color(230, 249, 255);//light blue
	public final static Color mixedBG= new Color(255, 248, 212);//yellow
	public final static Color lilac= new Color(238, 230, 255);
	public final static Color ddBG= new Color(0, 0, 0,100);
	public final static Color bgColor= new Color(243, 233, 210);
	public final static Color darkerLilac= new Color(148, 130, 201);
	public final static Font bigButtons=SWTResourceManager.getFont("Calibri Light", 11, SWT.BOLD);
	
	public SelectionListener newBoardAdapter=new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			// 
			AppBoard newWindow= new AppBoard();
			newWindow.openBlank();
		}
	};
	
	public SelectionListener openBoardAdapter=new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			// 
			FileDialog chooser= new FileDialog((Shell) e.widget.getData(),SWT.OPEN);
			try {
			chooser.setFilterExtensions(new String[] {"*.txt"});
			chooser.open();
			
				if(chooser.getFileName().length()>1) {
					File source= new File(chooser.getFilterPath()+"\\"+chooser.getFileName());
					AppBoard newWindow= new AppBoard();
					newWindow.openFile(source);
				}
			}catch(Exception err) {
				//nuthin
				err.printStackTrace();
			}
		}
	};
	public SelectionListener saveASAdapter=new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			FileDialog chooser= new FileDialog((Shell) e.widget.getData(),SWT.SAVE);
			try {
				chooser.setFilterExtensions(new String[] {"*.txt"});
				chooser.open();
				String fileName=chooser.getFileName();
				
				if(fileName.length()>1) {
					//if it doesn't end with the extension, then add the extension
					if(!fileName.endsWith(chooser.getFilterExtensions()[0].substring(1))) {
						fileName=fileName+chooser.getFilterExtensions()[0].substring(1);
					}
					File output= new File(chooser.getFilterPath()+"\\"+fileName);
					exportToFile(output);
					Shell shee=getShell();
					
					new Board(shee,SWT.NONE,AppBoard.parseBoard(output),output,mcToggle);
					dispose();
				}
			}catch(Exception err) {
				//no forreal I don't want to do anything
				err.printStackTrace();
			}
			
		}

	};

	public ModifyListener textChanged=new ModifyListener() {
		@Override
		public void modifyText(ModifyEvent e) {
			if(((String) e.widget.getData()).contains("home")) {
				homeFolder=((Text) e.widget).getText();
			}else if(((String) e.widget.getData()).contains("path")) {
				pathToHome=((Text) e.widget).getText();
			}
			
		}
		
	};
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 * @param categories
	 * @wbp.parser.constructor
	 */
	public Board(Composite parent, int style,Category[] catObjs) {
		super(parent, style);
		homeFolder="";
		pathToHome="";
		useRelativePaths=false;
		textStyle="#MS Gothic#28#True#False#16645837#";
		populateBoard(catObjs);
	}
	
	//CONSTRUCTOR FOR OPENING FILES
	public Board(Composite parent, int style,Category[] catObjs,File source,boolean isMC) {
		super(parent, style);
		homeFolder=source.separatorChar+source.getParentFile().getName()+source.separatorChar;
		pathToHome=source.getParentFile().getParent();
		this.mcToggle=isMC;
		currentOpenDoc=source;
		textStyle=catObjs[0].getQuestions()[0].getTypeDetails().substring(1);
		populateBoard(catObjs);
	}
	

	private void populateBoard(Category[] catObjs) {
		setBackgroundMode(SWT.INHERIT_DEFAULT);
		//setBackground(bgColor);
		setLayout(new GridLayout(5,false));
		Composite top=new Composite(this,SWT.NONE);
		top.setLayout(new GridLayout(3,true));
		top.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,5,1));
		//EXPERIMENTING WITH COMPOSITE FOR BUTTONS
		Composite buttons=new Composite(top,SWT.NONE);
		buttons.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		//GridData butts=new GridData(SWT.FILL,SWT.FILL,false,false,1,1);
		//butts.heightHint=100;
		//buttons.setLayoutData(butts);
		GridLayout buttonsLayout= new GridLayout(4,false);
		buttons.setLayout(buttonsLayout);
		if(currentOpenDoc != null) {
			titleText=titleText+currentOpenDoc.getName();
			Button save= new Button(buttons,SWT.PUSH);
			//save.setLayoutData(new RowData(SWT.DEFAULT,40));
			save.setText("SAVE - "+currentOpenDoc.getName());
			save.setFont(bigButtons);
			save.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
				exportToFile(currentOpenDoc);
				
				}
			});
		}
		
		//Save Button
		Button saveAS= new Button(buttons,SWT.PUSH);
		saveAS.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		saveAS.setText("SAVE AS...");
		saveAS.setData(this.getShell());
		saveAS.addSelectionListener(saveASAdapter);
		saveAS.setFont(bigButtons);
		
		//Open Button
		Button open= new Button(buttons,SWT.PUSH);
		open.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		open.setText("OPEN");
		open.setFont(bigButtons);
		open.setData(this.getShell());
		open.addSelectionListener(openBoardAdapter);
		
		//New button
		Button newBoard= new Button(buttons,SWT.PUSH);
		newBoard.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		newBoard.setText("NEW BOARD");
		newBoard.setFont(bigButtons);
		newBoard.addSelectionListener(newBoardAdapter);
		
		
		
		
		Label title=new Label(top,SWT.NONE);
		
		//title.setForeground(SWTResourceManager.getColor(204, 153, 255));
		title.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,1,1));
		title.setText(titleText);
		title.setAlignment(SWT.CENTER);
		title.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.BOLD));
		
		Label ver=new Label(top,SWT.NONE);
		ver.setLayoutData(new GridData(SWT.END,GridData.FILL,false,false,1,1));
		//((GridData)ver.getLayoutData()).
		ver.setText(version);
		ver.setAlignment(SWT.LEFT);
		ver.setFont(SWTResourceManager.getFont("Verdana", 9,SWT.NORMAL));
		//((GridData)title.getLayoutData()).horizontalIndent=ver.getBounds().width;
		//((GridData)title.getLayoutData()).
		this.layout();
	
		//create group Top Header that BUTTONS+warning (only) live in
		Group optionsHeader= new Group(this, SWT.NONE);
		optionsHeader.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,false,1,1));
		GridLayout headerInnerLayout= new GridLayout(2,false);
		headerInnerLayout.horizontalSpacing=10;
//		headerInnerLayout.center=true;
//		headerInnerLayout.marginWidth=2;
		optionsHeader.setLayout(headerInnerLayout);
		optionsHeader.setText("Options");
		
		Button ufnButton=new Button(optionsHeader,SWT.CHECK|SWT.WRAP);
		//ufnButton.setLayoutData(new RowData(SWT.DEFAULT,50));
		
		ufnButton.setText("File names as answers?");
		ufnButton.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,false,2,1));
		ufnButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				useFileNameAction((Button)e.widget);
				
			}
		});
		
		
		
		//Text options
		Label fontFaceLabel=new Label(optionsHeader,SWT.WRAP|SWT.CENTER);
		Label fontSizeLabel=new Label(optionsHeader,SWT.WRAP|SWT.CENTER);
		
		fontFaceLabel.setText("Font: "+textStyle.substring(1,textStyle.indexOf('#',1)));
		int hashIndex=textStyle.indexOf('#', textStyle.indexOf('#',1));
		fontSizeLabel.setText("Size: "+textStyle.substring(1+hashIndex,textStyle.indexOf('#', hashIndex+1)));
		Button selectFont= new Button(optionsHeader,SWT.PUSH);
		selectFont.setText("Customize");
		selectFont.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FontDialog choose=new FontDialog(((Control) e.widget).getShell());
				choose.setFontList(new FontData[]{fontInfo});
				choose.setEffectsVisible(false);
				try {
					fontInfo=choose.open();
					textStyle="#"+fontInfo.getName()+"#"+fontInfo.getHeight()+"#True#False#16645837#";
					fontFaceLabel.setText("Font: "+fontInfo.getName());
					fontSizeLabel.setText("Size: "+fontInfo.getHeight());
					fontInfo.setHeight(10);
					fontFaceLabel.setFont(new Font(lilac.getDevice(),fontInfo));
					
					fontFaceLabel.redraw();
					fontFaceLabel.update();
					layout();
				}catch(Exception e1) {
					e1.printStackTrace();
				}
				
			
			}
		});
		
		Label reminder=new Label(optionsHeader,SWT.WRAP|SWT.CENTER);
		//reminder.setLayoutData(new RowData(SWT.DEFAULT,50));
		GridData reminderLayData=new GridData(SWT.CENTER,SWT.CENTER,true,false,2,1);
		reminder.setText("REMINDER: \nCheck boards in Trivia Board Pro Editor BEFORE playing them");
		reminder.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		reminderLayData.widthHint=reminder.computeSize(SWT.DEFAULT,SWT.DEFAULT).x*2/3;
		reminder.setLayoutData(reminderLayData);
		reminder.setBackground(mixedBG);
		
		
		
		optionsHeader.pack();
		
		
		
		
		
		
		//fINAL QUESTION SECTION
			Group finalQHeader= new Group(this, SWT.NONE);

			finalQHeader.setLayoutData(new GridData(GridData.FILL,GridData.FILL,true,false,2,1));
			GridLayout fqSectionLayout= new GridLayout(2,false);
			finalQHeader.setLayout(fqSectionLayout);	
				
				
		Label finalQSecText=new Label(finalQHeader,SWT.NONE);
		finalQSecText.setText("Final Question");
		finalQSecText.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
		finalQSecText.setLayoutData(new GridData(GridData.CENTER,GridData.BEGINNING,false,false,2,1));

			/*
			 * Label finalQSecText=new Label(finalQHeader,SWT.NONE);
			 * finalQSecText.setText("Final Question");
			 * finalQSecText.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
			 * finalQSecText.setLayoutData(new
			 * GridData(GridData.CENTER,GridData.BEGINNING,false,false));
			 */
		Label fQtextLabel=new Label(finalQHeader,SWT.NONE);	
		fQtextLabel.setText("QUESTION:");
		fQtextLabel.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.NORMAL));


		Question finalQ=catObjs[6].getQuestions()[0];
		//Group finalQTextSec= new Group(finalQHeader, SWT.NONE);
		//finalQTextSec.setText("Question");
		//finalQTextSec.setLayout(new GridLayout());
		//fQtext= new Text(finalQTextSec,SWT.MULTI|SWT.WRAP);
		fQtextBox= new Text(finalQHeader,SWT.MULTI|SWT.WRAP);
		fQtextBox.setText(finalQ.getQuestion());
		fQtextBox.setMessage("Type the text of Final Question here");
		fQtextBox.setLayoutData(new GridData(GridData.FILL,GridData.FILL,true,false));
		((GridData) fQtextBox.getLayoutData()).heightHint=fQtextBox.getLineHeight()*2+5;
		//finalQTextSec.setLayoutData(new GridData(GridData.FILL,GridData.FILL,true,true));
		
		
		Label fQanswerLabel=new Label(finalQHeader,SWT.NONE);
		fQanswerLabel.setText("ANSWER:");
		fQanswerLabel.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.NORMAL));
		//Group finalQAnsSec= new Group(finalQHeader, SWT.NONE);
		//finalQAnsSec.setText("Answer");
		//finalQAnsSec.setLayout(new GridLayout());
		//fQanswer= new Text(finalQAnsSec,SWT.MULTI|SWT.WRAP);
		fQanswerBox= new Text(finalQHeader,SWT.MULTI|SWT.WRAP);
		fQanswerBox.setText(finalQ.getAnswer());
		fQanswerBox.setMessage("Type the answer to the Final Question here");
		fQanswerBox.setToolTipText("Type the answer to the Final Question here");
		//finalQAnsSec.setLayoutData(new GridData(GridData.FILL,GridData.FILL,true,true));
		fQanswerBox.setLayoutData(new GridData(GridData.FILL,GridData.FILL,true,false));
		((GridData) fQanswerBox.getLayoutData()).heightHint=fQtextBox.getLineHeight()*1+5;
		finalQHeader.pack();
		
		
		
		
		
		//new section
				Group pathingHeader= new Group(this, SWT.NONE);
				pathingHeader.setLayoutData(new GridData(GridData.FILL,GridData.FILL,true,false,1,1));
				GridLayout pathingSectionLayout= new GridLayout(3,false);
				pathingSectionLayout.marginHeight=5;
				pathingSectionLayout.marginWidth=10;
				pathingHeader.setLayout(pathingSectionLayout);	
				pathingHeader.setText("Pathing Options");
				//CHILD 0
				Button rpButton=new Button(pathingHeader,SWT.TOGGLE|SWT.WRAP);
				rpButton.setLayoutData(new GridData(GridData.FILL,GridData.FILL,true,false,3,1));
				rpButton.setSelection(useRelativePaths);
				rpButton.setText("Show relative paths?");
				rpButton.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						useRelativePaths= !useRelativePaths;
						Button w=(Button)e.widget;
						togglePathViewType((Group)w.getParent());
						if(useRelativePaths)rpButton.setText("Show full paths?");
						else rpButton.setText("Show relative paths?");
						
					}
				});
				//CHILD 1
				Label hfInputLabel=new Label(pathingHeader,SWT.NONE);
				hfInputLabel.setText("Common Folder:");
				//CHILD 2
				Text homeFolderInput=new Text(pathingHeader,SWT.SINGLE|SWT.BORDER);
				homeFolderInput.setLayoutData(new GridData(GridData.FILL,GridData.FILL,true,false,2,1));
				homeFolderInput.setText(homeFolder);
				homeFolderInput.setData("homeInput");
				homeFolderInput.addModifyListener(textChanged);
				//CHILD 3
				Label pthfInputLabel=new Label(pathingHeader,SWT.NONE);
				pthfInputLabel.setText("New Path To Common Folder:");
				//CHILD 4
				Text pathToInput=new Text(pathingHeader,SWT.SINGLE|SWT.BORDER);
				pathToInput.setLayoutData(new GridData(GridData.FILL,GridData.FILL,true,false,2,1));
				pathToInput.setText(pathToHome);
				pathToInput.setData("pathInput");
				//CHILD 5
				Button replacePathsButton=new Button(pathingHeader,SWT.PUSH);
				replacePathsButton.setLayoutData(new GridData(GridData.FILL,GridData.FILL,false,false,2,1));
				replacePathsButton.setText("Replace Path to Common Folder");
				replacePathsButton.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						replacePath(pathToInput.getText());
					}
				});
				if(!AppBoard.userPrefs.getBoolean("advancedPathing", false)) {
					((GridData)pathingHeader.getLayoutData()).exclude=true;
					((GridData)finalQHeader.getLayoutData()).horizontalSpan=2;
				}
				 AppBoard.userPrefs.addPreferenceChangeListener(new PreferenceChangeListener() {

					@Override
					public void preferenceChange(PreferenceChangeEvent evt) {
						if(!AppBoard.advancedPathing) {
							((GridData)pathingHeader.getLayoutData()).exclude=true;
							((GridData)finalQHeader.getLayoutData()).horizontalSpan=2;
						}else {
							((GridData)pathingHeader.getLayoutData()).exclude=false;
							((GridData)finalQHeader.getLayoutData()).horizontalSpan=1;
						}
						
					}
				});
			
		
		
		
		
		
		//START THE MAIN SECTION OF THE WINDOW
		ScrolledComposite scrollContainer= new ScrolledComposite(this,SWT.V_SCROLL|SWT.H_SCROLL| SWT.BORDER);
		scrollContainer.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,5,1));
		scrollContainer.setExpandHorizontal(true);
		
		Composite dummyContainer = new Composite(scrollContainer,SWT.NONE);
		//Group dummyContainer = new Group(scrollContainer,SWT.NONE);
		
		GridLayout dummyLay= new GridLayout(6,true);
		dummyLay.horizontalSpacing=10;
		dummyContainer.setLayout(dummyLay);
		dummyContainer.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false));
		dummyContainer.setBackground(darkerLilac);
		
		scrollContainer.setContent(dummyContainer);
		catGroups = new Group[6];
		
		titles= new Text[6];
		Composite[] qGroup; //start with qGroup as a group of qEdits, but potentially they will become qMedias
		Combo[] catType= new Combo[6];
		Button[] clear=new Button[6];
		
		
		
		
		
		//BEGIN PUTTING THINGS IN CAT GROUPS
		for(int i =0;i<catObjs.length-1;i++) {
			catGroups[i]= new Group(dummyContainer, SWT.SHADOW_ETCHED_IN);
			catGroups[i].setText("Category "+(i+1));
			catGroups[i].setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
			
			switch(catObjs[i].getType()) {
			case "audio": catGroups[i].setBackground(audioBG);
			case "picture":catGroups[i].setBackground(picBG);
			case "mixed":catGroups[i].setBackground(mixedBG);
			case "text":catGroups[i].setBackground(lilac);
			}
			catGroups[i].setBackgroundMode(SWT.INHERIT_DEFAULT);	
			catGroups[i].setLayout(new GridLayout(2,false));
			Question[] qs=catObjs[i].getQuestions(); 
			
			
			
			
			//Category titles
			titles[i]= new Text(catGroups[i], SWT.MULTI|SWT.WRAP|SWT.BORDER|SWT.V_SCROLL);
			titles[i].setFont(SWTResourceManager.getFont("Segoe UI", 13, SWT.NORMAL));
			titles[i].setText(catObjs[i].getName().toUpperCase());
			
			//WE SET TILE SIZE LATER AFTER GRABBING THE BOXES
			
			
			//Category types
			catType[i]= new Combo(catGroups[i],SWT.DROP_DOWN|SWT.READ_ONLY);
			catType[i].setItems(typeNames);
			catType[i].setLayoutData(new GridData(SWT.FILL,GridData.CENTER,true,false));
			catType[i].setText(catObjs[i].getType());
			catType[i].addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					// TODO Auto-generated method stub
					Combo w=(Combo)e.widget;
					Composite parent=w.getParent();
					setType(parent,w);
				
					
				}
				
			});
			
			
			clear[i]=new Button(catGroups[i],SWT.PUSH);
			clear[i].setImage(SWTResourceManager.getImage(Board.class, "Delete16.gif"));
			clear[i].setText("Clear");
			
			
			
			//add all the questions
			
			Qbox[] boxes=makeQuestionGroup(catGroups[i],qs);	
			GridData titleData= new GridData(SWT.FILL,SWT.FILL,true,false,2,1);
			titleData.widthHint=boxes[0].width;
			titleData.heightHint=titles[i].getLineHeight()*2;
			titles[i].setLayoutData(titleData);
			((GridData)catGroups[i].getLayoutData()).minimumWidth=catGroups[i].computeSize(SWT.DEFAULT,SWT.DEFAULT).x;
			
			clear[i].addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					// 
					for(Qbox q:boxes) {
						q.clear();
					}
					
				}
			});
			
		}
		
		dummyContainer.layout();
		dummyContainer.pack();
		scrollContainer.pack();
		scrollContainer.setMinWidth(dummyContainer.getSize().x);
		pack();
		
		
	}
	

	protected void replacePath(String newPath) {
		pathToHome=newPath;
		ArrayList<QMedia> qs=getAllMediaQs();
		for(QMedia q:qs) {
			q.setRelativePath(homeFolder);
			q.swapPathFront(pathToHome);
		}
		
	}

	protected void useFileNameAction(Button ufnButton) {
		if(ufnButton.getSelection()) { //true= selected & want to use file name
			useFileNames=true;
			getAllMediaQs().forEach(q->{
				q.setAnswerToFileName();
			});
		}else {
			useFileNames=false;
		}
		
	}
	
	protected void togglePathViewType(Group header) {
		if(useRelativePaths) {
			getAllMediaQs().forEach(q->{
				q.viewRelativePath();
			});
		
		}else {
			getAllMediaQs().forEach(q->{
			q.viewFullPath();
		});
		}
		
		
		
		
		
	}

	protected ArrayList<QMedia> getAllMediaQs() {
		ArrayList<QMedia> allMediaQuestions= new ArrayList<QMedia>();
		Control[] kids;
		//go through all groups
		for(Group g:catGroups) {
			kids=g.getChildren();
			//check IF they're a media category
			if(!((Combo) kids[1]).getText().contains("text")) {
				//get every question in each group
				for(int i=qIndexInGroup;i<kids.length;i++) { 
					allMediaQuestions.add((QMedia) kids[i]);
					/*
					 * if(type.contains("answers")) { // then set the answer to the file name
					 * ((QMedia) kids[i]).setAnswerToFileName(); } else
					 * if(type.contains("questions") && useRelativePaths) { // then set the question
					 * to relative pathing ((QMedia) kids[i]).setPathToRelative(homeFolder); }else {
					 * ((QMedia) kids[i]).setPathToFull(); }
					 */
			}
			
			}
		}
		
		return allMediaQuestions;
	}


	private Qbox[] makeQuestionGroup(Composite parentCatGroup,Question[]qs) {
		Qbox[] qGroup=null;
		String newType=qs[0].getCategory().getType();
		if(newType.contains("text")) {
			qGroup= new QEdit[5];
			parentCatGroup.setBackground(lilac);
			for(int j =0;j<qs.length;j++) {
				qGroup[j]= new QEdit(parentCatGroup, SWT.NONE,qs[j]);
			}
		}else if((newType.contains("audio")) || (newType.contains("picture")) ){
			qGroup= new QMedia[5];
			if(newType.contains("picture")){
				parentCatGroup.setBackground(picBG);
			}else if(newType.contains("mixed")){
				parentCatGroup.setBackground(mixedBG);
			}
			else {parentCatGroup.setBackground(audioBG);}
			
			for(int j =0;j<qs.length;j++) {
				qGroup[j]= new QMedia(parentCatGroup, SWT.NONE,qs[j]);
			}
			
		}else if((newType.contains("mixed"))) {
			qGroup= new QMixed[5];
			parentCatGroup.setBackground(mixedBG);
			for(int j =0;j<qs.length;j++) {
				qGroup[j]= new QMixed(parentCatGroup, SWT.NONE,qs[j]);
			}
		}
		
		for(Qbox q:qGroup) {
			q.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,2,1));
		}
		
		parentCatGroup.layout();
		parentCatGroup.pack();
		return qGroup;
	}
	
	
	
	protected void setType(Composite catGroupParent,Combo typeSelect) {
		
		int index= Integer.parseInt(((Group)catGroupParent).getText().substring(9))-1;
		Qbox[] qBoxGroup= new Qbox[5];
		Question[] qs=new Question[5];
	
		String newType=typeSelect.getText();
		Control[]kids=catGroupParent.getChildren();//these kids are all the things in Category group:title,chooser, q1,q2...q5
		for(int i=0;i<qBoxGroup.length;i++) { 
			qBoxGroup[i]= (Qbox) kids[i+qIndexInGroup];//start at 3 because 0-2 are title + chooser + clear
			qs[i]= qBoxGroup[i].getQobject(true);//also grab the question and add it to an array
			qBoxGroup[i].dispose();
		}
		
		Category c=qs[0].getCategory();
		c.changeType(newType);//change the type at category level
		makeQuestionGroup(catGroupParent,qs);
		catGroupParent.getParent().pack();
		
		
	}


	
	
	protected void exportToFile(File output) {
		int ddCount=0;
		char outputDD;
		try {
			PrintWriter fileOut= new PrintWriter(output);
			
			for(int i =0;i<catGroups.length;i++) {
				
				Control[] children= catGroups[i].getChildren();
				String title=((Text)children[0]).getText().replaceAll("\\n", " ");
				fileOut.println(title+" ");
				//BEFORE ANYTHING I COUNT THE DDs
				for(int j =qIndexInGroup;j<children.length;j++) { //start at 2 because skip the category title+type
					Qbox qEd=(Qbox)children[j];
					outputDD=qEd.getDD();
					
					//doing this to make sure there's at least 2 daily doubles
					if(outputDD=='Y') {
						ddCount ++;
					}else if(i==5) { //if its the last category (6)
						if((j-ddCount==3+qIndexInGroup) && (j>2+qIndexInGroup)) {//and we don't have enough DDs
							outputDD='Y';
							ddCount++;
						}
					}
					
					
					fileOut.println(qEd.getText().replaceAll("\\n|\\r", " ")+" ");
					fileOut.println(" "+qEd.exportAnswer().replaceAll("\\n", " "));
					fileOut.println(outputDD);
					if(qEd.getTypeDetails().contains("T")) {
						fileOut.println(qEd.getTypeDetails()+textStyle);
					}else {
						fileOut.println(qEd.getTypeDetails());
					}
					
					}
			}
			//after all the loops are done at the very end add the FINAL QUESTION
			fileOut.println(fQtextBox.getText().replaceAll("\\n", " ")+" ");
			fileOut.println(" "+fQanswerBox.getText().replaceAll("\\n", " ")+"^^^^");
			
			fileOut.close();
			if(ddCount<2) {
				System.out.println("You fucked up the DDs bro");
			}
			
		} catch (IOException err) {
			// TODO Auto-generated catch block
			err.printStackTrace();
		}
	}
	
	
	


	public void swapQuestions(Qbox originalBox, int newLevel) {
		//figure out qBox's current level && initialize OG question object 
		Question originalQ =originalBox.getQobject(true);
		int oldLevel=originalQ.getLvlIndex();
		
		//get qBoxchildren in parent group so we can get questions
		Qbox newBox=(Qbox) originalBox.getParent().getChildren()[qIndexInGroup+newLevel]; //get the specific sibling at the desired level
		
		//grab question object at newLevel
		Question newQ= newBox.getQobject(true);

		//set qBox at newLevel to OG question
		newBox.setNewQObject(originalQ);
		//set OG qBox to new Question
		originalBox.setNewQObject(newQ);
		
	}
	
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public boolean isMc() {
		return mcToggle;
	}
}

