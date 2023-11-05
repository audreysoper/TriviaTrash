package gui.qGang;

import Resources.Colors;
import gui.cGang.CGroup;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.wb.swt.SWTResourceManager;

import gui.Board;
import orgObjects.Question;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;

public class Qbox extends Composite {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 * @param question
	 */
	public int qIndex; //starts at 0
 	public boolean isMC;
	protected Board boardFather;
	protected CGroup catDad;
	protected Text qEdit;
	protected Answer qAnswer;
	protected Button qDD;
	protected Button openButton;
	protected Button viewButton;
	protected Combo typeSelect;
	protected Combo swapPosition;
	protected final int vSpace=3;
	SelectionAdapter qSwapListener=new SelectionAdapter() {

		@Override
		public void widgetSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			Combo swapPCombo=(Combo) e.widget;
			catDad.swapQuestions((Qbox)swapPCombo.getData(),Integer.parseInt(swapPCombo.getText())-1);
		}
	};

	public Qbox(CGroup parent, Question q, int index) {
		super(parent, SWT.NONE);
		boardFather=((Board)parent.getParent().getParent().getParent());
		isMC=boardFather.isMc();
		catDad=parent;

		
		
		this.qIndex =index;
		//this.setBackground(ViewBoard.listBG);
		GridLayout gridLayout = new GridLayout(3,false);
		gridLayout.marginWidth=0;
		gridLayout.verticalSpacing=vSpace;
		setLayout(gridLayout);
		//setBackground(Board.lilac);
		
		Label qNumLbl = new Label(this, SWT.NONE);
		qNumLbl.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		qNumLbl.setAlignment(SWT.CENTER);
		qNumLbl.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		qNumLbl.setText("Question ");
		//qNumLbl.setBackground(Board.lilac);
		
		
		
		
		swapPosition = new Combo(this, SWT.DROP_DOWN|SWT.READ_ONLY);
		swapPosition.setItems(new String[] {"1","2","3","4","5"});
		swapPosition.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		swapPosition.setData(this);
		swapPosition.setText(String.valueOf(qIndex+1));
		
		swapPosition.addSelectionListener(qSwapListener);
		
		qDD = new Button(this, SWT.CHECK);
		qDD.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		qDD.setText("DD?");
		qDD.setSelection(q.getDD());
		qDD.setGrayed(false);
		/*
		 * qDD.addSelectionListener(new SelectionAdapter() {
		 * 
		 * @Override public void widgetSelected(SelectionEvent e) { ((Control)
		 * e.widget).getParent().redraw();
		 * 
		 * } });
		 */
		
		
		typeSelect = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
		typeSelect.setVisible(false);
		typeSelect.setLayoutData(new GridData(3,1));
		((GridData)typeSelect.getLayoutData()).exclude=true;
		
		openButton = new Button(this, SWT.NONE);
		openButton.setImage(SWTResourceManager.getImage(Colors.class, "Open16.gif"));
		openButton.setVisible(false);
		GridData openLayoutDetails = new GridData(GridData.FILL, GridData.FILL, true, false, 2, 1);
		//openLayoutDetails.minimumWidth = minWidth;
		//openLayoutDetails.widthHint=this.width;
		openLayoutDetails.exclude=true;
		openButton.setLayoutData(openLayoutDetails);
		
		
		
		viewButton = new Button(this, SWT.NONE);
		viewButton.setVisible(false);
		GridData viewLayoutDetails = new GridData(GridData.FILL, GridData.FILL, true, false, 1, 1);
		viewLayoutDetails.exclude=true;
		viewButton.setLayoutData(viewLayoutDetails);
		
		
		
		
		qEdit = new Text(this, SWT.MULTI|SWT.WRAP|SWT.V_SCROLL |SWT.BORDER);
		GridData qEditLayoutDetails=new GridData(GridData.FILL,GridData.FILL,true,false,3,1);
		qEditLayoutDetails.heightHint=boardFather.qBoxEditHeightDefault;
		//qEditLayoutDetails.heightHint=this.height*2/5;
		qEditLayoutDetails.minimumWidth = boardFather.qBoxMinWidth;
		qEdit.setLayoutData(qEditLayoutDetails);
		qEdit.setMessage("Question");
		qEdit.setText(q.getQuestion());
		qEdit.setEditable(false);

		qEdit.addTraverseListener(boardFather.tabLister);
		if(qEdit.getText().length()>Board.charLimit-1)qEdit.setText(qEdit.getText().substring(0, Board.charLimit-1));
		qEdit.setTextLimit(Board.charLimit);
		qEdit.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent focusEvent) {

			}

			@Override
			public void focusLost(FocusEvent focusEvent) {
				if (qEdit.getText()!=q.getQuestion()){

				}
			}
		});

		qAnswer=new Answer(this, q, boardFather.isMc());
		GridData qAnsLayoutDetails=new GridData(GridData.FILL,GridData.FILL,true,true,3,1);
		qAnsLayoutDetails.heightHint=boardFather.qBoxAnsHeightDefault;
		qAnsLayoutDetails.minimumWidth = boardFather.qBoxMinWidth;
		qAnswer.setLayoutData(qAnsLayoutDetails);
		this.layout();
		this.pack();
		
		
	}




	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	public String getText() {
		return qEdit.getText();
	}
	public String exportAnswer() {
		return qAnswer.ansExport();
	}
	public String getTypeDetails() {
		return typeSelect.getText();
	}


	public boolean getDD() {
		return qDD.getSelection();
	}

	public void setNewQObject(Question newQ) {
		qEdit.setText(newQ.getQuestion());
		qAnswer.clear();
		qAnswer.changeQ(newQ.getAnswer());
	}

	public void clear() {
		qEdit.setText("");
		qAnswer.clear();
		qDD.setSelection(false);
		
	}

	public Qbox setLevel(int newIndex){
		this.qIndex=newIndex;
		return this;
	}

	public Question reduceToQ(){
		char dd='F';
		if(qDD.getSelection()){
			dd='T';
		}
		Question q= new Question(qEdit.getText(), qAnswer.getAnswer(), dd,"",qIndex);
		q.setType(typeSelect.getText());
		return q;
	}

	public boolean hasPath(){
		return false;
	}

}
