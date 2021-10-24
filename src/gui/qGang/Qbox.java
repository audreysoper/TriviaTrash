package gui.qGang;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
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
	public int height;
	public int width;
	public final int infoHeight=15;
	final public String qNumber;
	protected Board boardFather;
	protected Text qEdit;
	protected Text qAnswer;
	protected Question q;
	protected Button qDD;
	protected Button openButton;
	protected Combo typeSelect;
	protected Combo swapPosition;
	protected final int vSpace=3;
	protected int editHeightDefault;
	protected int ansHeightDefault;
	
	
	public Qbox(Composite parent, int style, Question q) {
		super(parent, style);
		boardFather=((Board)parent.getParent().getParent().getParent());
		this.q=q;

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
		//GridData qDDLayoutDetails=new GridData(GridData.FILL,GridData.FILL,true,true);
		//qDDLayoutDetails.minimumHeight=infoHeight;
		qDD.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		qDD.setText("DD?");
		qDD.setSelection(q.getDD());
		qDD.setGrayed(false);
		//qDD.setBackground(Board.lilac);
		
		typeSelect = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
		typeSelect.setVisible(false);
		typeSelect.setLayoutData(new GridData(3,1));
		((GridData)typeSelect.getLayoutData()).exclude=true;
		
		openButton = new Button(this, SWT.NONE);
		openButton.setImage(SWTResourceManager.getImage(Qbox.class, "Open16.gif"));
		openButton.setVisible(false);
		GridData openLayoutDetails = new GridData(GridData.FILL, GridData.FILL, true, false, 3, 1);
		openLayoutDetails.minimumWidth = this.width;
		openLayoutDetails.exclude=true;
		openButton.setLayoutData(openLayoutDetails);
		
		
		height=computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
		width=computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		
		qEdit = new Text(this, SWT.MULTI|SWT.WRAP|SWT.V_SCROLL |SWT.BORDER);
		GridData qEditLayoutDetails=new GridData(GridData.FILL,GridData.FILL,true,false,3,1);
		editHeightDefault=qEdit.getLineHeight()*5;
		qEditLayoutDetails.heightHint=editHeightDefault;
		//qEditLayoutDetails.heightHint=this.height*2/5;
		qEditLayoutDetails.minimumWidth=this.width;
		qEdit.setLayoutData(qEditLayoutDetails);
		qEdit.setMessage("Question");
		qEdit.setText(q.getQuestion());
		qEdit.setEditable(false);
		
		qAnswer = new Text(this, SWT.MULTI|SWT.WRAP|SWT.V_SCROLL |SWT.BORDER);
		GridData qAnswerLayoutDetails=new GridData(GridData.FILL,GridData.FILL,true,false,3,1);
		ansHeightDefault=qAnswer.getLineHeight()*3;
		qAnswerLayoutDetails.minimumWidth=this.width;
		qAnswerLayoutDetails.heightHint=ansHeightDefault;
		qAnswer.setMessage("Answer");
		qAnswer.setLayoutData(qAnswerLayoutDetails);
		qAnswer.setText(q.getAnswer());
		qAnswer.setEditable(false);
		this.layout(true);
		this.pack();
		
	}

	protected void swapQ(int level) {
		// TODO Auto-generated method stub
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
	public String getAnswer() {
		return qAnswer.getText();
	}
	public String getTypeDetails() {
		return q.getTypeDetails();
	}
	public Question getQobject() {
		q.swapQtext(qEdit.getText(), qAnswer.getText());
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
		qAnswer.setText(q.getAnswer());
	}
	
	
	

}
