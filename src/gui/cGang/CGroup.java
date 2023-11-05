package gui.cGang;

import Resources.Colors;
import gui.Board;
import gui.qGang.QEdit;
import gui.qGang.QMedia;
import gui.qGang.QMixed;
import gui.qGang.Qbox;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.wb.swt.SWTResourceManager;
import orgObjects.Category;
import orgObjects.Question;

import java.io.PrintWriter;
import java.util.ArrayList;


public class CGroup extends Group {
    public final static String[] typeNames = new String[] { "text", "picture", "audio", "mixed" };
    private Board board;
    private Qbox[] theQs;
    private Combo typeSelector;
    private Text titleText;
    private int index;
    private String[] pathBreakUp;
    private Group commonPathSection;
    private Combo commonFolderSelect;
    public String homeFolder;
    private TraverseListener tabLister= e -> {
        if (e.detail == SWT.TRAVERSE_TAB_NEXT || e.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
            e.doit = true;
        }
    };
    private SelectionAdapter clearQs =new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
            //
            for (Qbox q : theQs) {
                q.clear();
            }

        }
    };

    //CONSTRUCTOR
    public CGroup(Composite parent, int index, Board realDad,Category catObj) {
        super(parent, SWT.NONE);
        board =realDad;
        this.index=index;
        setHomeFolder();
        headerSetup();
        constructCommons(catObj.getQuestions(), catObj.getCommon());
        setWithCategory(catObj);

    }

    public void swapQuestions(Qbox originalBox, int newLevel) {
        //TODO: got to switch the order , delete old box?
        int oldLevel = originalBox.qIndex;

        Qbox targetBox = (Qbox) theQs[newLevel];

        theQs[oldLevel]=targetBox.setLevel(newLevel);
        theQs[newLevel]=originalBox.setLevel(newLevel);
        Question[] newQs=new Question[5];
        for(int i=0;i<theQs.length;i++){
            newQs[i]=theQs[i].reduceToQ();
            theQs[i].dispose();
        }
        makeQBoxes(newQs);

    }


    public void setWithCategory(@org.jetbrains.annotations.NotNull Category catObj) {
        this.titleText.setText(catObj.getName().toUpperCase());
        this.typeSelector.setText(catObj.getType());
        this.pathBreakUp = catObj.getCommon();
        setTypeColor(catObj.getType());
        makeQBoxes(catObj.getQuestions());

    }

    private void constructCommons(Question[] qs,String[] catCommons) {
        
        commonPathSection = new Group(this, SWT.NONE);
        commonPathSection.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, true));
        ((GridData) commonPathSection.getLayoutData()).exclude = !board.pathingIsCatDependent|| typeSelector.getText().contains("text");
        commonPathSection.setText("Base Folder:");
        commonPathSection.setVisible(board.pathingIsCatDependent && !typeSelector.getText().contains("text"));
        commonPathSection.setLayout(new FillLayout());
        commonFolderSelect = new Combo(commonPathSection, SWT.DROP_DOWN | SWT.READ_ONLY);
        commonFolderSelect.setItems(catCommons);
        commonFolderSelect.addModifyListener(e -> {
            Combo w = (Combo) e.widget;
            homeFolder=w.getText();


        });

    }

    private void headerSetup() {

        this.setText("Category " + (this.index));
        this.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        this.setBackgroundMode(SWT.INHERIT_DEFAULT);
        this.setLayout(new GridLayout(2, false));


        // Category title
        titleText = new Text(this, SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
        titleText.setFont(SWTResourceManager.getFont("Segoe UI", 13, SWT.NORMAL));
        titleText.addTraverseListener(tabLister);
        GridData titleData = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
        titleData.heightHint = titleText.getLineHeight() * 2;
        titleText.setLayoutData(titleData);
        // WE SET TILE SIZE LATER AFTER GRABBING THE BOXES

        // Category type
       typeSelector = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
       typeSelector.setItems(typeNames);
       typeSelector.setLayoutData(new GridData(SWT.FILL, GridData.CENTER, true, false));
       typeSelector.addModifyListener(e -> {
           Combo w = (Combo) e.widget;
           if(theQs!=null){
               setType(w.getText());
           }


       });
       //Clear Button
        Button clear= new Button(this, SWT.PUSH);
        clear.setImage(SWTResourceManager.getImage(Colors.class, "Delete16.gif"));
        clear.setText("Clear");
        clear.addSelectionListener(clearQs);



    }

    private void makeQBoxes(Question[] qs) {
        theQs=new Qbox[5];
        switch (this.typeSelector.getText()) {
            case "mixed":
                for (int j = 0; j < theQs.length; j++) {
                    theQs[j] = new QMixed(this, qs[j],j);
                    theQs[j].setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
                }
                break;
            case "text":
                for (int j = 0; j < theQs.length; j++) {
                    theQs[j] = new QEdit(this, qs[j],j);
                    theQs[j].setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
                }
                break;
            default:
                for (int j = 0; j < theQs.length; j++) {
                    theQs[j] = new QMedia(this, qs[j],j);
                    theQs[j].setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
                }
        }
        this.layout();
        board.getShell().layout();




    }

    private void setTypeColor(String type){
        System.out.println(type);
        switch (type) {
            case "audio":
                this.setBackground(Colors.audioBG);
                break;
            case "picture":
                this.setBackground(Colors.picBG);
                break;
            case "mixed":
                this.setBackground(Colors.mixedBG);
                break;
            case "text":
                this.setBackground(Colors.lilac);
        }
    }

    public void setType(String newType){
        setTypeColor(newType);
        String oldType=typeSelector.getText();
        this.typeSelector.setText(newType);
        Question[] oldQs=new Question[5];

        for(int i=0;i<theQs.length;i++){
            oldQs[i]=theQs[i].reduceToQ();
            theQs[i].dispose();
        }
        makeQBoxes(oldQs);

    }

    public void togglePathViewType() {
        if(!typeSelector.getText().contains("text"))  {
            for (QMedia q:getPathHavers() ) {
                q.togglePathView(!board.useRelativePaths);
            }
        }

    }

    public void toggleCommonsView(){
        ((GridData) commonPathSection.getLayoutData()).exclude = !board.pathingIsCatDependent|| typeSelector.getText().contains("text");
        commonPathSection.setVisible(board.pathingIsCatDependent && !typeSelector.getText().contains("text"));

    }

    private ArrayList<QMedia> getPathHavers(){
        ArrayList<QMedia> pathHavers = new ArrayList<>();
        for (Qbox q:theQs) {
            if (q.hasPath()){
                pathHavers.add((QMedia) q);
            }
        }
        return pathHavers;
    }

    public void answersAreFileName() {
        if(!typeSelector.getText().contains("text"))  {
            for (QMedia q:getPathHavers() ) {
                q.setAnswerToFileName();
            }
        }
    }

    public void swapPathFronts() {
        if(!typeSelector.getText().contains("text"))  {
            for (QMedia q:getPathHavers() ) {
                q.swapPathFront(board.pathToHome);
            }
        }
    }

    public void setHomeFolder(){
        if(board.pathingIsCatDependent){
            homeFolder=commonFolderSelect.getText();
        }else{
            homeFolder=board.homeFolder;
        }

    }

    public void printCat(PrintWriter fileOut){
            fileOut.println(titleText.getText().replaceAll("[\\n\\r]", " "));
        for (Qbox q:theQs) {
            fileOut.println(q.getText().replaceAll("[\\n\\r]", " "));
            fileOut.println(" " + q.exportAnswer().replaceAll("\\n", " "));

            if(q.getDD())fileOut.println('Y');
            else  fileOut.println('N');

            if (q.getTypeDetails().contains("T")) {
                fileOut.println(q.getTypeDetails() + board.textStyle);
            } else {
                fileOut.println(q.getTypeDetails());
            }
        }

    }


    /*protected void setTypee(Composite catGroupParent, Combo typeSelect) {

        Qbox[] qBoxGroup = new Qbox[5];
        Question[] qs = new Question[5];

        String newType = typeSelect.getText();
        Control[] kids = catGroupParent.getChildren();// these kids are all the things in Category group:title,chooser,
        // q1,q2...q5
        for (int i = 0; i < qBoxGroup.length; i++) {
            qBoxGroup[i] = (Qbox) kids[i + qIndexInGroup];// start at 3 because 0-2 are title + chooser + clear
            qs[i] = qBoxGroup[i].getQobject(true);// also grab the question and add it to an array
            qBoxGroup[i].dispose();
        }

        Category c = qs[0].getCategory();
        c.changeType(newType);// change the type at category level
        makeQuestionGroup(catGroupParent, qs);
        catGroupParent.getParent().pack();

    }*/
    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }


}


