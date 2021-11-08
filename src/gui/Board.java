package gui;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
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
	private Composite parent;
	private boolean mcToggle;
	private Group[] catGroups;
	private Text[] titles;
	public final static String[] typeNames=new String[]{"text","picture","audio","mixed"};
	private Text fQtext;
	private Text fQanswer;
	public int boxW;
	public int boxH;
	public File currentOpenDoc;

	public final static Color audioBG= SWTResourceManager.getColor(255, 229, 249);//light pink
	public final static Color picBG= SWTResourceManager.getColor(230, 249, 255);//light blue
	public final static Color mixedBG= SWTResourceManager.getColor(255, 248, 212);//yellow
	public final static Color lilac= SWTResourceManager.getColor(238, 230, 255);
	public final static Color bgColor= SWTResourceManager.getColor(243, 233, 210);
	public final static Color darkerLilac= SWTResourceManager.getColor(148, 130, 201);
	
	
	private SelectionListener newBoardAdapter=new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			// 
			AppBoard newWindow= new AppBoard();
			newWindow.openBlank();
		}
	};
	
	private SelectionListener openBoardAdapter=new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			// 
			FileDialog chooser= new FileDialog(((Button) e.widget).getShell(),SWT.SAVE);
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
	private SelectionListener saveASAdapter=new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			FileDialog chooser= new FileDialog(((Button)e.widget).getShell(),SWT.SAVE);
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
	
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 * @param categories
	 * @wbp.parser.constructor
	 */
	public Board(Composite parent, int style,Category[] catObjs) {
		super(parent, style);
		
		populateBoard(catObjs);
		
	}
	
	//CONSTRUCTOR FOR OPENING FILES
	public Board(Composite parent, int style,Category[] catObjs,File source,boolean isMC) {
		super(parent, style);
		this.mcToggle=isMC;
		currentOpenDoc=source;
		populateBoard(catObjs);
		
	}
	

	private void populateBoard(Category[] catObjs) {
		setBackgroundMode(SWT.INHERIT_DEFAULT);
		
		//setBackground(bgColor);
		setLayout(new GridLayout(3,false));
		
		
		
		Label title=new Label(this,SWT.NONE);
		
		//title.setForeground(SWTResourceManager.getColor(204, 153, 255));
		title.setLayoutData(new GridData(GridData.FILL,GridData.FILL,true,false,3,1));
		title.setText("The Fancy Question Editor");
		title.setAlignment(SWT.CENTER);
		title.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.BOLD));
		
		
		
		//create group Top Header that BUTTONS+warning (only) live in
		Group buttonHeader= new Group(this, SWT.NONE);
		buttonHeader.setLayoutData(new GridData(GridData.FILL,GridData.FILL,false,false));
		
		
		//now set the actual layout that Top header is employing
		GridLayout headerInnerLayout= new GridLayout(5,false);
		//headerInnerLayout.justify=true;
//		headerInnerLayout.spacing=10;
//		headerInnerLayout.center=true;
//		headerInnerLayout.marginWidth=2;
		buttonHeader.setLayout(headerInnerLayout);
		buttonHeader.setText("Options");
		
		
		//add stuff to topHeader Group
//		Label headerInstructions=new Label(buttonHeader,SWT.NONE);
//		//headerInstructions.setLayoutData(new RowData(SWT.DEFAULT,50));
//		headerInstructions.setLayoutData(new GridData());
//		headerInstructions.setText("Options:");
//		headerInstructions.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
//		
		Button ufnButton=new Button(buttonHeader,SWT.CHECK|SWT.WRAP);
		//ufnButton.setLayoutData(new RowData(SWT.DEFAULT,50));
		
		ufnButton.setText("Use file names as answers?");
		ufnButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				useFileNameAction((Button)e.widget);
				
			}
		});
		
		if(currentOpenDoc != null) {
			title.setText(title.getText()+" - "+currentOpenDoc.getName());
			Button save= new Button(buttonHeader,SWT.PUSH);
			//save.setLayoutData(new RowData(SWT.DEFAULT,40));
			save.setText("SAVE - "+currentOpenDoc.getName());
			save.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
				exportToFile(currentOpenDoc);
				
				}
			});
		}
		
		//Save Button
		Button saveAS= new Button(buttonHeader,SWT.PUSH);
		saveAS.setText("SAVE AS...");
		saveAS.addSelectionListener(saveASAdapter);
		
		//Open Button
		Button open= new Button(buttonHeader,SWT.PUSH);
		open.setText("OPEN");
		open.addSelectionListener(openBoardAdapter);
		
		//New button
		Button newBoard= new Button(buttonHeader,SWT.PUSH);
		newBoard.setText("NEW BOARD");
		newBoard.addSelectionListener(newBoardAdapter);
		
		
		
		
		Label reminder=new Label(buttonHeader,SWT.WRAP|SWT.CENTER);
		//reminder.setLayoutData(new RowData(SWT.DEFAULT,50));
		GridData reminderLayData=new GridData(SWT.FILL,SWT.FILL,false,true,5,1);
		reminderLayData.widthHint=reminder.computeSize(SWT.DEFAULT,SWT.DEFAULT).x/2;
		reminder.setLayoutData(reminderLayData);
		
		reminder.setText("REMINDER: \nPlease always open boards in Trivia Board Pro Editor and SAVE before playing, just in case");
		reminder.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		reminder.setBackground(mixedBG);
		buttonHeader.pack();
		
		//fINAL QUESTION SECTION
			Group finalQHeader= new Group(this, SWT.NONE);
			finalQHeader.setLayoutData(new GridData(GridData.FILL,GridData.FILL,true,false,2,1));
			GridLayout fqSectionLayout= new GridLayout(2,false);
			finalQHeader.setLayout(fqSectionLayout);	
				
				
		Label finalQSecText=new Label(finalQHeader,SWT.NONE);
		finalQSecText.setText("Final Question");
		finalQSecText.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
		finalQSecText.setLayoutData(new GridData(GridData.CENTER,GridData.BEGINNING,false,false,2,1));
		
		

		Question finalQ=catObjs[6].getQuestions()[0];
		Group finalQTextSec= new Group(finalQHeader, SWT.NONE);
		finalQTextSec.setText("Question");
		finalQTextSec.setLayout(new GridLayout());
		fQtext= new Text(finalQTextSec,SWT.MULTI|SWT.WRAP);
		fQtext.setText(finalQ.getQuestion());
		fQtext.setLayoutData(new GridData(GridData.FILL,GridData.FILL,true,false));
		((GridData) fQtext.getLayoutData()).heightHint=fQtext.getLineHeight()*3;
		finalQTextSec.setLayoutData(new GridData(GridData.FILL,GridData.FILL,true,true));
		
		Group finalQAnsSec= new Group(finalQHeader, SWT.NONE);
		finalQAnsSec.setText("Answer");
		finalQAnsSec.setLayout(new GridLayout());
		fQanswer= new Text(finalQAnsSec,SWT.MULTI|SWT.WRAP);
		fQanswer.setText(finalQ.getAnswer());
		finalQAnsSec.setLayoutData(new GridData(GridData.FILL,GridData.FILL,true,true));
		fQanswer.setLayoutData(new GridData(GridData.FILL,GridData.FILL,true,false));
		((GridData) fQanswer.getLayoutData()).heightHint=fQtext.getLineHeight()*3;
		finalQHeader.pack();
		
		
		
		//START THE MAIN SECTION OF THE WINDOW
		ScrolledComposite scrollContainer= new ScrolledComposite(this,SWT.V_SCROLL|SWT.H_SCROLL| SWT.BORDER);
		scrollContainer.setLayoutData(new GridData(GridData.FILL,GridData.FILL,true,true,3,1));
		scrollContainer.setExpandHorizontal(true);
		
		Composite dummyContainer = new Composite(scrollContainer,SWT.NONE);
		//Group dummyContainer = new Group(scrollContainer,SWT.NONE);
		
		GridLayout dummyLay= new GridLayout(6,true);
		dummyLay.horizontalSpacing=10;
		dummyContainer.setLayout(dummyLay);
		dummyContainer.setLayoutData(new GridData(GridData.FILL,GridData.FILL,true,false));
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
			catGroups[i].setLayoutData(new GridData(GridData.FILL,GridData.FILL,true,false));
			
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
			catType[i].setLayoutData(new GridData(GridData.FILL,GridData.CENTER,true,false));
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
			GridData titleData= new GridData(GridData.FILL,GridData.FILL,true,false,2,1);
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
	

	protected void useFileNameAction(Button ufnButton) {
		if(ufnButton.getSelection()) { //true= selected & want to use file name
			useFileNames=true;
			setAllMediaAnswersToFileName();
		}else {
			useFileNames=false;
		}
		
	}

	protected void setAllMediaAnswersToFileName() {
		// TODO Auto-generated method stub
		Control[] kids;
		//go through all groups
		for(Group g:catGroups) {
			kids=g.getChildren();
			//check IF they're a media category
			if(!((Combo) kids[1]).getText().contains("text")) {
				//get every question in each group
				for(int i=qIndexInGroup;i<kids.length;i++) { 
					// then set the name to the file
					((QMedia) kids[i]).setAnswerToFileName();
			}
			
			}
		}
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
			q.setLayoutData(new GridData(GridData.FILL,GridData.FILL,true,true,2,1));
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
					fileOut.println(" "+qEd.getAnswer().replaceAll("\\n", " ")+"^^^^");
					fileOut.println(outputDD);
					fileOut.println(qEd.getTypeDetails());
					}
			}
			//after all the loops are done at the very end add the FINAL QUESTION
			fileOut.println(fQtext.getText().replaceAll("\\n", " ")+" ");
			fileOut.println(" "+fQanswer.getText().replaceAll("\\n", " ")+"^^^^");
			
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

