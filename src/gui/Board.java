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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;

import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import gui.qGang.QEdit;
import gui.qGang.QMedia;
import gui.qGang.Qbox;
import orgObjects.Category;
import orgObjects.Question;

public class Board extends Composite {

	private final int qIndexInGroup=2; //Use this for noting what index questions start at in category Group
	public boolean useFileNames;
	private Group[] catGroups;
	private Text[] titles;
	private String[] typeNames=new String[]{"text","picture","audio"};
	private Text fQtext;
	private Text fQanswer;
	public int boxW;
	public int boxH;
	public File currentOpenDoc;
	public final static Color audioBG= SWTResourceManager.getColor(255, 229, 249);//light pink
	public final static Color picBG= SWTResourceManager.getColor(230, 249, 255);//light blue
	//public final static Color lilac= SWTResourceManager.getColor(226, 213, 255);
	public final static Color lilac= SWTResourceManager.getColor(238, 230, 255);
	public final static Color bgColor= SWTResourceManager.getColor(243, 233, 210);
	//public final static Color bgColor= SWTResourceManager.getColor(249, 238, 210);
	public final static Color darkerLilac= SWTResourceManager.getColor(148, 130, 201);
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
	public Board(Composite parent, int style,Category[] catObjs,File source) {
		super(parent, style);
		currentOpenDoc=source;
		populateBoard(catObjs);
	}
	

	private void populateBoard(Category[] catObjs) {
		setBackgroundMode(SWT.INHERIT_DEFAULT);
		
		//setBackground(bgColor);
		setLayout(new GridLayout());
		
		Label title=new Label(this,SWT.NONE);
		
		//title.setForeground(SWTResourceManager.getColor(204, 153, 255));
		title.setLayoutData(new GridData(GridData.FILL,GridData.FILL,true,false));
		title.setText("The Fancy Question Editor");
		title.setAlignment(SWT.CENTER);
		title.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.BOLD));
		
		
		
		//create group Top Header that buttons live in
		Group topHeader= new Group(this, SWT.NONE);
		topHeader.setLayoutData(new GridData(GridData.FILL_HORIZONTAL|GridData.CENTER));
		
		//now set the actual layout that Top header is employing
		RowLayout headerInnerLayout= new RowLayout();
		//headerInnerLayout.justify=true;
		headerInnerLayout.spacing=10;
		headerInnerLayout.center=true;
		topHeader.setLayout(headerInnerLayout);
		//topHeader.setText("Options");
		
		
		//add stuff to topHeader Group
		Label headerInstructions=new Label(topHeader,SWT.NONE);
		headerInstructions.setLayoutData(new RowData(SWT.DEFAULT,50));
		headerInstructions.setText("Options:");
		headerInstructions.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
		
		Button ufnButton=new Button(topHeader,SWT.CHECK|SWT.WRAP);
		ufnButton.setLayoutData(new RowData(SWT.DEFAULT,50));
		//ufnButton.setBounds(0, 0, 120, 50);
		ufnButton.setText("Use file names as answers?");
		ufnButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if(ufnButton.getSelection()) { //true= selected & want to use file name
					//INCOMPLETE
					useFileNames=true;
					setAllMediaAnswersToFileName();
				}
				
			}
		});
		
		if(currentOpenDoc != null) {
			title.setText(title.getText()+" - "+currentOpenDoc.getName());
			Button save= new Button(topHeader,SWT.PUSH);
			save.setLayoutData(new RowData(SWT.DEFAULT,40));
			save.setText("SAVE - "+currentOpenDoc.getName());
			save.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
				exportToFile(currentOpenDoc);
				}
			});
		}
		
		//Save Button
		Button saveAS= new Button(topHeader,SWT.PUSH);
		saveAS.setLayoutData(new RowData(SWT.DEFAULT,40));
		saveAS.setText("SAVE AS...");
		saveAS.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				FileDialog chooser= new FileDialog(((Control) e.widget).getShell(),SWT.SAVE);
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
						
					}
				}catch(Exception err) {
					//no forreal I don't want to do anything
					err.printStackTrace();
				}
				
			}

		});
		
		//Open Button
		Button open= new Button(topHeader,SWT.PUSH);
		//open.setBounds(100, 0, 120, 50);
		open.setLayoutData(new RowData(SWT.DEFAULT,40));
		open.setText("OPEN");
		open.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				FileDialog chooser= new FileDialog(((Control) e.widget).getShell());
				try {
				chooser.setFilterExtensions(new String[] {"*.txt"});
				chooser.open();
				
					if(chooser.getFileName().length()>1) {
						File source= new File(chooser.getFilterPath()+"\\"+chooser.getFileName());
						ViewBoard newWindow= new ViewBoard();
						newWindow.openFile(source);
					}
				}catch(Exception err) {
					//nuthin
					err.printStackTrace();
				}
			}
		});
		
		//New button
		Button newBoard= new Button(topHeader,SWT.PUSH);
		//new.setBounds(100, 0, 120, 50);
		newBoard.setLayoutData(new RowData(SWT.DEFAULT,40));
		newBoard.setText("NEW BOARD");
		newBoard.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					// 
					ViewBoard newWindow= new ViewBoard();
					newWindow.openBlank();
				}
			});
		
		//fINAL QUESTION SECTION
		Label sep = new Label(topHeader,SWT.SEPARATOR|SWT.VERTICAL);
		Label finalQSecText=new Label(topHeader,SWT.NONE);
		finalQSecText.setLayoutData(new RowData(SWT.DEFAULT,50));
		finalQSecText.setText("Final Question");
		finalQSecText.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));

		//Group finalQSection= new Group(topHeader, SWT.NONE);
		//finalQSection.setLayout(new RowLayout(SWT.VERTICAL));
		//finalQSection.setText("Final Question");
		Question finalQ=catObjs[6].getQuestions()[0];
		Group finalQTextSec= new Group(topHeader, SWT.NONE);
		finalQTextSec.setText("Question");
		finalQTextSec.setLayout(new RowLayout());
		fQtext= new Text(finalQTextSec,SWT.MULTI);
		fQtext.setText(finalQ.getQuestion());
		fQtext.setLayoutData(new RowData(200,40));
		
		Group finalQAnsSec= new Group(topHeader, SWT.NONE);
		finalQAnsSec.setText("Answer");
		finalQAnsSec.setLayout(new RowLayout());
		fQanswer= new Text(finalQAnsSec,SWT.MULTI);
		fQanswer.setText(finalQ.getAnswer());
		fQanswer.setLayoutData(new RowData(200,40));
		
		
		
		
		//START THE MAIN SECTION OF THE WINDOW
		ScrolledComposite scrollContainer= new ScrolledComposite(this,SWT.V_SCROLL|SWT.H_SCROLL| SWT.BORDER);
		scrollContainer.setLayoutData(new GridData(GridData.FILL,GridData.FILL,true,true));
		
		
		Composite dummyContainer = new Composite(scrollContainer,SWT.NONE);
		dummyContainer.setLayout(new GridLayout(6,true));
		dummyContainer.setBackground(darkerLilac);
		
		scrollContainer.setContent(dummyContainer);
		catGroups = new Group[6];
		
		titles= new Text[6];
		Composite[] qGroup; //start with qGroup as a group of qEdits, but potentially they will become qMedias
		Combo[] catType= new Combo[6];
		
		
		
		
		
		
		//BEGIN PUTTING THINGS IN CAT GROUPS
		for(int i =0;i<catObjs.length-1;i++) {
			catGroups[i]= new Group(dummyContainer, SWT.SHADOW_ETCHED_IN);
			catGroups[i].setText("Category "+(i+1));
			catGroups[i].setLayoutData(new GridData(GridData.BEGINNING));
			
			switch(catObjs[i].getType()) {
			case "audio": catGroups[i].setBackground(audioBG);
			case "picture":catGroups[i].setBackground(picBG);
			case "text":catGroups[i].setBackground(lilac);
			}
			catGroups[i].setBackgroundMode(SWT.INHERIT_DEFAULT);
		
			RowLayout catLayout=new RowLayout(SWT.HORIZONTAL|SWT.WRAP);
			//catLayout.spacing=1;
			catLayout.justify=true;
			//catLayout.center=true;
			catLayout.marginWidth=5;
			catLayout.fill=true;
			catGroups[i].setLayout(catLayout);
			 
			
			
			
			//Category titles
			titles[i]= new Text(catGroups[i], SWT.MULTI|SWT.WRAP|SWT.BORDER|SWT.V_SCROLL);
			titles[i].setFont(SWTResourceManager.getFont("Segoe UI", 13, SWT.NORMAL));
			titles[i].setText(catObjs[i].getName().toUpperCase());
			
			//WE SET TILE SIZE LATER AFTER GRABBING THE BOXES
			
			
			//Category types
			catType[i]= new Combo(catGroups[i],SWT.DROP_DOWN|SWT.READ_ONLY);
			catType[i].setItems(typeNames);
			//catType[i].setSize(boxW, 15);
			//catType[i].setLayoutData(new RowData(boxW, 30));
			//catType[i].setLayoutData(new RowData(SWT.DEFAULT, 30));
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
			
			//add all the questions
			Question[] qs=catObjs[i].getQuestions();
			Qbox[] boxes=makeQuestionGroup(catGroups[i],qs);	
			titles[i].setLayoutData(new RowData(boxes[0].width, (catType[i].getBounds().height)*2));
		}
		
		dummyContainer.layout();
		dummyContainer.pack();
		scrollContainer.pack();
		
		pack();
		
		
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
				//qGroup[j].setSize(boxW, boxH);//adding extra boxH to the starting position because of title
				//qGroup[j].setLayoutData(new RowData(boxW+20, boxH));
			}
		}else if((newType.contains("audio")) || (newType.contains("picture"))){
			qGroup= new QMedia[5];
			if(newType.contains("picture")){
				parentCatGroup.setBackground(picBG);
			}else {parentCatGroup.setBackground(audioBG);}
			
			for(int j =0;j<qs.length;j++) {
				qGroup[j]= new QMedia(parentCatGroup, SWT.NONE,qs[j]);
				//qGroup[j].setSize(boxW, boxH);//adding extra boxH to the starting position because of title
				//qGroup[j].setLayoutData(new RowData(boxW+20, boxH));
			}
			
		}
		parentCatGroup.layout();
		parentCatGroup.pack();
		parentCatGroup.update();
		return qGroup;
	}
	
	
	
	protected void setType(Composite catGroupParent,Combo typeSelect) {
		
		int index= Integer.parseInt(((Group)catGroupParent).getText().substring(9))-1;
		Group test=catGroups[index];
		Qbox[] qBoxGroup= new Qbox[5];
		Question[] qs=new Question[5];
	
		String newType=typeSelect.getText();
		Control[]kids=catGroupParent.getChildren();//these kids are all the things in Category group:title,chooser, q1,q2...q5
		for(int i=0;i<qBoxGroup.length;i++) { 
			qBoxGroup[i]= (Qbox) kids[i+qIndexInGroup];//start at 2 because 0 and 1 are title + chooser
			qs[i]= qBoxGroup[i].getQobject();//also grab the question and add it to an array
			qBoxGroup[i].dispose();
		}
		
		Category c=qs[0].getCategory();
		c.changeType(newType);//change the type at category level
		makeQuestionGroup(catGroupParent,qs);
		catGroupParent.layout();
		//catGroupParent.pack();
		
	}


	
	
	protected void exportToFile(File output) {
		int ddCount=0;
		char outputDD;
		try {
			PrintWriter fileOut= new PrintWriter(output);
			
			for(int i =0;i<catGroups.length;i++) {
				
				Control[] children= catGroups[i].getChildren();
				Text title=(Text) children[0];
				fileOut.println(title.getText()+" ");
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
					
					
					fileOut.println(qEd.getText()+" ");
					fileOut.println(" "+qEd.getAnswer()+"^^^^");
					fileOut.println(outputDD);
					fileOut.println(qEd.getTypeDetails());
					}
			}
			//after all the loops are done at the very end add the FINAL QUESTION
			fileOut.println(fQtext.getText()+" ");
			fileOut.println(" "+fQanswer.getText()+"^^^^");
			
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
		Question originalQ =originalBox.getQobject();
		int oldLevel=originalQ.getLvlIndex();
		
		//get qBoxchildren in parent group so we can get questions
		Qbox newBox=(Qbox) originalBox.getParent().getChildren()[qIndexInGroup+newLevel]; //get the specific sibling at the desired level
		
		//grab question object at newLevel
		Question newQ= newBox.getQobject();

		//set qBox at newLevel to OG question
		newBox.setNewQObject(originalQ);
		//set OG qBox to new Question
		originalBox.setNewQObject(newQ);
		
	}
	
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}


/* Not sure I need this method anymore?
 * Put it directly in selection listener
protected void openClick(SelectionEvent e) {
	// TODO Auto-generated method stub
	FileDialog chooser= new FileDialog(this.getShell());
	chooser.open();
	File source= new File(chooser.getFilterPath()+"\\"+chooser.getFileName());
	
	ViewBoard newWindow= new ViewBoard();
	newWindow.openNewFile(source);
	
	
}
*/

