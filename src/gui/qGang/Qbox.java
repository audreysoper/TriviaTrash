package gui.qGang;

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
	public int width;
	final public String qNumber;
	final public boolean isMC;
	protected Board boardFather;
	protected Text qEdit;
	protected Answer qAnswer;
	protected Question q;
	protected Button qDD;
	protected Button openButton;
	protected Button viewButton;
	protected Combo typeSelect;
	protected Combo swapPosition;
	protected final int vSpace=3;
	protected int editHeightDefault;
	protected int openAnsHeightDefault;
	protected int minWidth;


	public Qbox(Composite parent, int style, Question q) {
		super(parent, style);
		boardFather=((Board)parent.getParent().getParent().getParent());
		this.q=q;
		isMC=boardFather.isMc();
		width=parent.getSize().x-10;
		getDefaults();

		
		
		this.qNumber=q.getLevel();
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
		swapPosition.setText(this.qNumber);
		
		swapPosition.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				Combo swapPositionCombo=(Combo) e.widget;
				swapQ(Integer.parseInt(swapPositionCombo.getText())-1);
				}
			});
		
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
		openButton.setImage(SWTResourceManager.getImage(Qbox.class, "Open16.gif"));
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
		GridData qEditLayoutDetails=new GridData(GridData.FILL,GridData.FILL,true,true,3,1);
		qEditLayoutDetails.heightHint=editHeightDefault;
		//qEditLayoutDetails.heightHint=this.height*2/5;
		qEditLayoutDetails.widthHint=this.width;
		qEditLayoutDetails.minimumWidth = minWidth;
		qEdit.setLayoutData(qEditLayoutDetails);
		qEdit.setMessage("Question");
		qEdit.setText(q.getQuestion());
		qEdit.setEditable(false);
<<<<<<< Updated upstream
		
=======
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

>>>>>>> Stashed changes
		qAnswer=new Answer(this,boardFather.isMc());
		GridData qAnsLayoutDetails=new GridData(GridData.FILL,GridData.FILL,true,true,3,1);
		qAnsLayoutDetails.heightHint=openAnsHeightDefault;
		qAnsLayoutDetails.widthHint=this.width;
		qAnsLayoutDetails.minimumWidth = minWidth;
		qAnswer.setLayoutData(qAnsLayoutDetails);
		this.layout();
		this.pack();
		
		
	}

	private void getDefaults() {
		Text defaultTester= new Text(this,SWT.MULTI);
		GC gc = new GC(defaultTester);
	    FontMetrics fm = gc.getFontMetrics();
	    minWidth = (int) (fm.getAverageCharacterWidth()*15);
	    gc.dispose();
	    int lineHeight=defaultTester.getLineHeight();
	    openAnsHeightDefault=lineHeight*3+lineHeight/2;
		if(isMC) {
			editHeightDefault=lineHeight*3;
		}else{
			editHeightDefault=lineHeight*4;
			
		}
		
		defaultTester.dispose();
		
	}

	protected void swapQ(int level) {
		boardFather.swapQuestions(this,level);
		swapPosition.setText(this.qNumber);
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
		return q.getFormat();
	}
	public Question getQobject(boolean forceUpdate) {
		if(forceUpdate) {
			q.updateQobj(qEdit.getText(), qAnswer.getAnswer());
		}
		q.setDD(qDD.getSelection());
		return this.q;
	}

	public char getDD() {
		q.setDD(qDD.getSelection());
		if(q.getDD()) {
			return 'Y';
		}else {
			return 'N';
		}
	}
	public void setNewQObject(Question newQ) {
		this.q=newQ;
		qEdit.setText(q.getQuestion());
		qAnswer.clear();
		qAnswer.changeQ(q.getAnswer());
	}

	public void clear() {
		qEdit.setText("");
		qAnswer.clear();
		qDD.setSelection(false);
		
	}
		

}
