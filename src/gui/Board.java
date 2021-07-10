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
	public int boxW;
	public int boxH;
	public File currentOpenDoc;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 * @param categories
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
		
		boxW=(getShell().getDisplay().getBounds().width)/25;
		boxH=(getShell().getDisplay().getBounds().height)/7;
		int approxBWidth=boxW*6;
		setLayout(new GridLayout());
		
		Label title=new Label(this,SWT.NONE);
		title.setLayoutData(new GridData(GridData.FILL,GridData.FILL,true,false));
		title.setText("Audrey's Fancy Question Editor");
		title.setAlignment(SWT.CENTER);
		title.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.BOLD));
		
		
		
		//create group Top Header that buttons live in
		Group topHeader= new Group(this, SWT.BORDER);
		GridData topHeaderLayout= new GridData(GridData.FILL_HORIZONTAL|GridData.CENTER);
		topHeader.setLayoutData(topHeaderLayout);
		
		//now set the actual layout that Top header is employing
		RowLayout headerInnerLayout= new RowLayout();
		//headerInnerLayout.justify=true;
		headerInnerLayout.spacing=10;
		headerInnerLayout.center=true;
		topHeader.setLayout(headerInnerLayout);
		
		
		
		//add stuff to topHeader Group
		Label headerInstructions=new Label(topHeader,SWT.NONE);
		headerInstructions.setLayoutData(new RowData(approxBWidth/5,50));
		headerInstructions.setText("Options:");
		headerInstructions.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
		
		Button ufnButton=new Button(topHeader,SWT.CHECK|SWT.WRAP);
		ufnButton.setLayoutData(new RowData(approxBWidth/5,50));
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
			save.setLayoutData(new RowData(approxBWidth/7,40));
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
		saveAS.setLayoutData(new RowData(approxBWidth/7,40));
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
		open.setLayoutData(new RowData(approxBWidth/7,40));
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
		newBoard.setLayoutData(new RowData(approxBWidth/7,40));
		newBoard.setText("NEW BOARD");
		newBoard.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					
					ViewBoard newWindow= new ViewBoard();
					newWindow.openBlank();
				}

			});
		
		
		
		
		
		ScrolledComposite scrollContainer= new ScrolledComposite(this,SWT.V_SCROLL| SWT.BORDER);
		scrollContainer.setLayoutData(new GridData(GridData.FILL,GridData.FILL,true,true));
		GridLayout boardLayout=new GridLayout(6,true);
		
		Composite dummyContainer = new Composite(scrollContainer,SWT.NONE);
		dummyContainer.setLayout(boardLayout);
		
		
		scrollContainer.setContent(dummyContainer);
		catGroups = new Group[6];
		
		titles= new Text[6];
		Composite[] qGroup; //start with qGroup as a group of qEdits, but potentially they will become qMedias
		Combo[] catType= new Combo[6];
		
		
		
		
		
		
		//BEGIN PUTTING THINGS IN CAT GROUPS
		for(int i =0;i<catObjs.length;i++) {
			catGroups[i]= new Group(dummyContainer, SWT.NONE);
			catGroups[i].setText("Category "+(i+1));
			catGroups[i].setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			RowLayout catLayout=new RowLayout(SWT.HORIZONTAL|SWT.WRAP);
			//catLayout.spacing=1;
			catLayout.justify=true;
			catLayout.center=true;
			catLayout.marginWidth=10;
			catLayout.fill=true;
			catGroups[i].setLayout(catLayout);
			catGroups[i].setSize(boxW, boxH*5+40); //6 is because 5 questions + category title box
			
			
			
			//Category titles
			titles[i]= new Text(catGroups[i], SWT.MULTI|SWT.WRAP|SWT.BORDER|SWT.V_SCROLL);
			titles[i].setFont(SWTResourceManager.getFont("Segoe UI", 13, SWT.NORMAL));
			titles[i].setText(catObjs[i].getName().toUpperCase());
			titles[i].setLayoutData(new RowData(boxW, 50));
			
			
			//Category types
			catType[i]= new Combo(catGroups[i],SWT.DROP_DOWN|SWT.READ_ONLY);
			catType[i].setItems(typeNames);
			//catType[i].setSize(boxW, 15);
			catType[i].setLayoutData(new RowData(boxW, 30));
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
			makeQuestionGroup(catGroups[i],qs);	
			
		}
		
		
		dummyContainer.pack();
	
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


	private void makeQuestionGroup(Composite parentCatGroup,Question[]qs) {
		Qbox[] qGroup;
		if(qs[0].getCategory().getType().contains("text")) {
			qGroup= new QEdit[5];
			for(int j =0;j<qs.length;j++) {
				qGroup[j]= new QEdit(parentCatGroup, SWT.NONE,qs[j]);
				//qGroup[j].setSize(boxW, boxH);//adding extra boxH to the starting position because of title
				qGroup[j].setLayoutData(new RowData(boxW+20, boxH));
			}
		}else if((qs[0].getCategory().getType().contains("audio")) || (qs[0].getCategory().getType().contains("picture"))){
			qGroup= new QMedia[5];
			for(int j =0;j<qs.length;j++) {
				qGroup[j]= new QMedia(parentCatGroup, SWT.NONE,qs[j]);
				//qGroup[j].setSize(boxW, boxH);//adding extra boxH to the starting position because of title
				qGroup[j].setLayoutData(new RowData(boxW+20, boxH));
			}
			
		}
		
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
		
	}


	
	
	protected void exportToFile(File output) {
		
		try {
			PrintWriter fileOut= new PrintWriter(output);
			
			for(int i =0;i<catGroups.length;i++) {
				
				Control[] children= catGroups[i].getChildren();
				Text title=(Text) children[0];
				fileOut.println(title.getText());
				for(int j =qIndexInGroup;j<children.length;j++) { //start at 2 because skip the category title+type
					Qbox qEd=(Qbox)children[j];
					fileOut.println(qEd.getText());
					fileOut.println(qEd.getAnswer()+"^^^^");
					fileOut.println(qEd.getDD());
					fileOut.println(qEd.getTypeDetails());
					}
			}
			fileOut.close();
			
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

