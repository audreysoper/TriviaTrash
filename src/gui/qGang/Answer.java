package gui.qGang;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

<<<<<<< Updated upstream:src/gui/answers/Answer.java
import gui.AppBoard;
=======
import gui.Board;
>>>>>>> Stashed changes:src/gui/qGang/Answer.java
import gui.qGang.Qbox;

public class Answer extends Composite {
	public final boolean multipleChoice;
	private Text[] answerBoxes;
	private Button[] correctSelector;
	private String correctAnswer;
	private String answerFormatted;
<<<<<<< Updated upstream:src/gui/answers/Answer.java
	
	
	
	public Answer(Qbox parent,boolean multipleChoice) {
		super(parent,SWT.NONE);
		
		this.multipleChoice=multipleChoice;
=======
	private Qbox parent;


	public Answer(Qbox p, boolean multipleChoice) {
		super(p, SWT.NONE);
		parent = p;
		this.multipleChoice = multipleChoice;
>>>>>>> Stashed changes:src/gui/qGang/Answer.java
		try {
			answerFormatted=parent.getQobject(false).getAnswer();
			
			/*
			 * if(answerFormatted==null) { answerFormatted="^^^^";}
			 */
		}
			catch(Exception e) {
				e.printStackTrace();
				correctAnswer="";
				answerFormatted="";}
		GridLayout lay=new GridLayout(2,false);
		lay.marginHeight=0;
		lay.marginWidth=0;
		setLayout(lay);
		
		if(multipleChoice) {
			//first make the text boxes & buttons
			answerBoxes= new Text[4];
			correctSelector= new Button[4];
			for(int i=0;i<answerBoxes.length;i++) {
				answerBoxes[i]= new Text(this,SWT.SINGLE|SWT.BORDER);
				answerBoxes[i].setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true)); //grab vertical space because you'll match parent
				//((GridData)answerBoxes[i].getLayoutData()).widthHint=parent.width;
				
				correctSelector[i]=new Button(this,SWT.RADIO);
				correctSelector[i].setData(answerBoxes[i]);
				correctSelector[i].setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,false)); //don't want it grabbing extra horizontal
				correctSelector[i].addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						Button correct=(Button) e.widget;
						correctAnswer=((Text)correct.getData()).getText();
						if(correctAnswer == null) {
							correctAnswer=" ";
						}
					
					}
				});
			}
			//then add content if we are importing an actual answer
			if(!answerFormatted.contains("^^^^")&&answerFormatted.length()>2) { //check if we ARE importing an answer
			parseMCAnsString(answerFormatted);
			
			}
<<<<<<< Updated upstream:src/gui/answers/Answer.java
			
			
		}else {
			correctAnswer=answerFormatted;
			answerBoxes=new Text[]{new Text(this, SWT.MULTI|SWT.WRAP|SWT.V_SCROLL |SWT.BORDER)};
			GridData qAnswerLayoutDetails=new GridData(GridData.FILL,GridData.FILL,true,true,2,1);
			//qAnswerLayoutDetails.heightHint=answerBoxes[0].getLineHeight()*3;
			answerBoxes[0].setMessage("Answer");
			answerBoxes[0].setLayoutData(qAnswerLayoutDetails);
			answerBoxes[0].setText(correctAnswer);
			//((GridData)answerBoxes[0].getLayoutData()).widthHint=parent.width;
			
			
=======

		}
		else {
			correctAnswer = answerFormatted;
			answerBoxes = new Text[] { new Text(this, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER) };
			GridData qAnswerLayoutDetails = new GridData(GridData.FILL, GridData.FILL, true, true, 2, 1);
			// qAnswerLayoutDetails.heightHint=answerBoxes[0].getLineHeight()*3;
			answerBoxes[0].setMessage("Answer");
			answerBoxes[0].setLayoutData(qAnswerLayoutDetails);
			answerBoxes[0].setText(correctAnswer);
			answerBoxes[0].addTraverseListener(parent.boardFather.tabLister);
			// ((GridData)answerBoxes[0].getLayoutData()).widthHint=parent.width;

>>>>>>> Stashed changes:src/gui/qGang/Answer.java
		}
		//pack();
		layout();
		//System.out.println("AnswerBox ACTUAL Height: "+answerBoxes[0].getSize());
	}
	
	private void parseMCAnsString(String importedAnsText) {
		boolean answerFound=false;
		String[] answers=importedAnsText.split("\\^");
		System.out.println(importedAnsText);
<<<<<<< Updated upstream:src/gui/answers/Answer.java
		correctAnswer=answers[0].trim();
		for(int i=0;(i<answerBoxes.length)&&(i+1<answers.length);i++) {
			answerBoxes[i].setText(answers[i+1].trim());
			if(answers[i+1].trim().matches(answers[0].trim())&& (!answerFound)) {
=======
		correctAnswer = answers[0].trim();
		for (int i = 0; (i < answerBoxes.length) && (i + 1 < answers.length); i++) {
			answerBoxes[i].setText(answers[i + 1].trim());
			if (answers[i + 1].trim().contentEquals(answers[0].trim()) && (!answerFound)) {
>>>>>>> Stashed changes:src/gui/qGang/Answer.java
				correctSelector[i].setSelection(true);
				answerFound=true;
			}
		}
		
		
	}

	public void clear() {
		for(int i=0;i<answerBoxes.length;i++) {
			answerBoxes[i].setText("");
			if(multipleChoice) {
				correctSelector[i].setSelection(false);
			}
		}
		correctAnswer="";
		answerFormatted="";
		
	}
	
	public String ansExport(){
		correctAnswer=answerBoxes[0].getText().trim();
		
		
		if(multipleChoice) {
			answerFormatted="^ "+correctAnswer;
			for(Text a:answerBoxes) {
				answerFormatted +=("^ "+a.getText());
			}
			answerFormatted+="^^^^";
		}else {
			answerFormatted = correctAnswer+" ^^^^";
		}
		
		return answerFormatted;
	}

	public void changeQ(String answer) {
		if(multipleChoice) {
			parseMCAnsString(answer);
		}else {
			correctAnswer=answer;
			answerBoxes[0].setText(correctAnswer);
		}
		
	}
	public String getAnswer(){
		correctAnswer=answerBoxes[0].getText().trim();
		
		
		if(multipleChoice) {
			answerFormatted="^ "+correctAnswer;
			for(Text a:answerBoxes) {
				answerFormatted +=("^ "+a.getText());
			}
			return answerFormatted;
		}else {
			 return correctAnswer;
		}
		
	}

}
