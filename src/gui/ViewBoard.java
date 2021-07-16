package gui;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import gui.qGang.Qbox;
import orgObjects.Category;
import orgObjects.Question;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;

public class ViewBoard {

	protected Shell shell;
	private int boxH;

	/**
	 * Launch the application.
	 * 
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

		shell = new Shell();
		shell.setLayout(new FillLayout());
		shell.setText("Trivia Question Viewer");

		Display display = Display.getDefault();
		boxH = (display.getBounds().height) / 7;
		int shellW = (display.getBounds().width) / 3;

		createContents(createBlank(), null);
		shell.open();
		// shell.layout();
		// shell.pack();
		Point size = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		shell.setSize(size.x, size.y / 3 * 2);

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
		shell = new Shell();
		shell.setLayout(new FillLayout());
		shell.setText("Trivia Question Viewer");
		Display display = Display.getDefault();
		boxH = (display.getBounds().height) / 7;

		Category[] cats = parseBoard(source);
		if (cats != null) {
			Board openBoard = createContents(cats, source);
			shell.open();
			Point size = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			shell.setSize(size.x, size.y / 3 * 2);

			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} else {

			shell.dispose();

		}

	}

	/**
	 * Create contents of the window.
	 */
	protected Board createContents(Category[] cats, File source) {

		// scrollContainer.setBackground(listBG);
		/*
		 * scrollContainer. addDisposeListener(new DisposeListener() {
		 * 
		 * @Override public void widgetDisposed(DisposeEvent e) { listBG.dispose(); }
		 * });
		 */
		// Board myBoard = new Board(scrollContainer, SWT.NONE,createBlank());
		// Board myBoard =new Board(scrollContainer, SWT.NONE,parseBoard(dir));
		if (source != null) {
			return new Board(shell, SWT.NONE, cats, source);
		}
		return new Board(shell, SWT.NONE, cats);

	}

	private Category[] createBlank() {
		Category[] cats = new Category[6];
		Question[] qs;
		for (int i = 0; i < cats.length; i++) {
			qs = new Question[5];

			for (int j = 0; j < qs.length; j++) {
				qs[j] = new Question("", "", 'N', "text", "", j);
			}
			try {
				cats[i] = new Category("", qs, i);
				cats[i].changeType("text");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return cats;
	}

	public Category[] parseBoard(File source) {
		Category[] cats = new Category[6];
		try {
			Scanner scanner = new Scanner(source);
			String position = "newCat";
			int qNum = 0;
			int catNum = 0;
			Category currentCat = null;
			Question questions[] = new Question[5];
			String text;
			String answer;
			String dd;
			String format;

			while (catNum < 6 && scanner.hasNextLine()) {
				text = scanner.nextLine();
				switch (position) {
				
				//WHEN POSITION IS A NEW CATEGORY
				case "newCat":
					if (text.length() > 1) {
						currentCat = new Category(text.trim(), catNum);
						position = "question";
					}else if(text.length() > 0) {
						currentCat = new Category(" ", catNum);
						position = "question";
					}
					break;

					
					
				//WHEN POSITION IS A NEW QUESTION	
				case "question":
					try {
						answer = scanner.nextLine();
						dd = scanner.nextLine();
						format = scanner.nextLine();
						if (answer.endsWith("^^^^") && answer.length()>4) {
							answer = answer.substring(0, answer.indexOf('^'));
						}else if(answer.length()<=4){
							answer=" ";
						}
					
					
						questions[qNum] = new Question(text.trim(), answer.trim(), dd.charAt(0), currentCat.getType(), format, qNum);
						qNum++;
					}catch (Exception e1) {
						// TODO Auto-generated catch block
						Composite sad = getSad();
						shell.open();
						MessageBox ope = new MessageBox(shell, SWT.OK);
						ope.setMessage("Hmmmmm.... This file doesn't look quite like I was expecting it to. "
								+ "\n You sure this is a trivia file that's formatted properly?"
								+ "\n If so please send it to Audrey to figure out why I can't read it");
						ope.setText("File Read Error");
						ope.open();
						// e.printStackTrace();
						return null;
					}
						// if we've got all out questions, change back to category start
						if (qNum > 4) {
							currentCat.addQuestions(questions);
							
							//set the type of the current category
							 if (text.length() < 4) {
							currentCat.changeType("text");
							 } else {
								 //if the text is more than 4 characters THEN its safe to do the switch statement
								 switch (text.substring(text.length() - 3)) {
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
							 
							}
							 
							//ELSE needs to end BEFORE these statements	
							cats[catNum] = currentCat;
							catNum++;
							qNum = 0;
							position = "newCat";
							questions = new Question[5];
						}
						break;
						
					
				default:
					System.out.println("Umm something might be wrong? I recieved: " + position);
					break;
				}
			}
			scanner.close();
		}catch(Exception e)
		{
		// TODO Auto-generated catch block
		e.printStackTrace();
		}

	return cats;
	}

	public Composite getSad() {
		Composite sad = new Composite(shell, SWT.NONE);
		sad.setLayout(new FillLayout());
		Label owoSticker = new Label(sad, SWT.NONE);
		Image bigOwo = SWTResourceManager.getImage(ViewBoard.class, "MEDowo.png");
		owoSticker.setImage(bigOwo);
		owoSticker.setBounds(bigOwo.getBounds());
		// owoSticker.setText("Plz stop breaking me");
		shell.layout(true);
		shell.pack();
		return sad;
	}
}
