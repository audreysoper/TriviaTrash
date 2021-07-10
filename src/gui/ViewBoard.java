package gui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import orgObjects.Category;
import orgObjects.Question;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;

public class ViewBoard {

	protected Shell shell;
	private int boxH;
	
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			ViewBoard window = new ViewBoard();
			window.openBlank();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void openBlank() {
		Display display = Display.getDefault();
		boxH = (display.getBounds().height)/7;
		//listBG = display.getSystemColor(SWT.COLOR_LIST_BACKGROUND);
		//For BLANK start with
		createContents(createBlank(),null);
		
		//To Open File from Start
		//File dir = new File("dummydata//1.txt");
		// createContents(parseBoard(dir));
		shell.open();
		shell.layout();
		shell.pack();
		shell.setSize(shell.getSize().x, boxH*4);
		//shell.setBackground(listBG);
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	
	
	/*
	 * FOR OPENING OTHER FILES AFTER INITIAL
	*/
	
	public void openFile(File source) {
		Display display = Display.getDefault();
		boxH = (display.getBounds().height)/7;
		Board openBoard= createContents(parseBoard(source),source);
		
		shell.open();
		shell.layout();
		shell.pack();
		
		shell.setSize(shell.getSize().x, boxH*4);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected Board createContents(Category[]cats,File source) {
		
		shell = new Shell();
		shell.setLayout(new FillLayout());
		shell.setText("Trivia Question Viewer");
		//scrollContainer.setBackground(listBG);
		/*
		scrollContainer. addDisposeListener(new DisposeListener() {
            @Override
			public void widgetDisposed(DisposeEvent e)
            {
                listBG.dispose();
            }
        });
		*/
		//Board myBoard = new Board(scrollContainer, SWT.NONE,createBlank());
		//Board myBoard =new Board(scrollContainer, SWT.NONE,parseBoard(dir));
		if(source!=null) {
			return new Board(shell, SWT.NONE,cats,source);
		}
		return new Board(shell, SWT.NONE,cats);
		 
		
		

	}
	
	
	
	private Category[] createBlank() {
		Category[] cats= new Category[6];
		Question[]qs;
		for (int i = 0; i < cats.length; i++) {
			qs=new Question[5];
			
			for(int j = 0; j < qs.length; j++) {
				qs[j]=new Question("","",'N',"text","",j);
			}
			try {
				cats[i]=new Category("",qs,i);
				cats[i].changeType("text");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return cats;
	}

	
	public Category[] parseBoard(File source) {
		Category[] cats= new Category[6];
		try {
			Scanner scanner = new Scanner(source);
			String position="newCat";
			int qNum=0;
			int catNum=0;
			Category currentCat = null;
			Question questions[]= new Question[5];
			String text;
			String answer;
			String dd;
			String format;
		
			while(catNum<6 && scanner.hasNextLine()) {
				text=scanner.nextLine();
				switch(position) {
				
				case "newCat":
					if(text.length()>0) {
						currentCat= new Category(text,catNum);
						position="question";
					
					}
					break;
					
				case "question":	
					answer=scanner.nextLine();
					dd=scanner.nextLine();
					format=scanner.nextLine();
					if(answer.endsWith("^^^^")) {
						answer=answer.substring(0,answer.indexOf('^'));}
					questions[qNum]=new Question(text,answer,dd.charAt(0),currentCat.getType(),format,qNum);
					qNum++;
					//if we've got all out questions, change back to category start
					if(qNum>4) {
						try {
							currentCat.addQuestions(questions);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							MessageBox ope=new MessageBox(shell,SWT.OK);
							ope.setMessage("Hmmmmm.... This file doesn't look quite like I was expecting it to. "
									+ "\n You sure this is a trivia file that's formatted properly?"
									+ "\n If so please send it to Audrey to figure out why I can't read it");
							ope.setText("File Read Error");
							ope.open();
							e.printStackTrace();
						}
					
						if(text.length()<4) {currentCat.changeType("text");}
						else {
						switch(text.substring(text.length()-3)) {
						case "jpg":
							currentCat.changeType("picture");
							break;
						case "mp3":
							currentCat.changeType("audio");
							break;
						default:
							currentCat.changeType("text");
							break;
						}
						
						cats[catNum]=currentCat;
						catNum++;
						qNum=0;
						position="newCat";
						questions= new Question[5];
					}
					break;
					}
				case "answer":
					
					break;
				
					
				default:
					System.out.println("Umm something might be wrong? I recieved: "+position);
					break;
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return cats;
	}
}
