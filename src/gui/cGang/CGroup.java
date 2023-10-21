package gui.cGang;

import gui.qGang.Qbox;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import orgObjects.Category;
import orgObjects.Question;

public class CGroup extends Group {
    private Qbox[] theQs;
    private String type;
    private int index;
    private String title;
    private String[] pathBreakUp;

    public CGroup(Composite parent, int style,int index) {
        super(parent, style);
    }

    public void addQs(Qbox[] theQs){
        this.theQs=theQs;
    }
    public void addQs(Question orgQs){
        //TODO write this
    }


    public void setWithCategory(@org.jetbrains.annotations.NotNull Category catObj){
        this.title= catObj.getName();
        this.type=catObj.getType();
        this.pathBreakUp= catObj.getCommon();
        for (Question q:catObj.getQuestions()) {
            System.out.println("yo");

            }
            //TODO: turn the questions in this category into qboxes
        }


}


