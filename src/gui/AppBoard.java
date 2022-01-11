package gui;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import orgObjects.Category;
import orgObjects.Question;

import java.io.File;
import java.util.Scanner;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;

public class AppBoard {

	protected Shell shell;
	private int boxH;
	protected MenuItem saveMenuItem;
	protected MenuItem openMenuItem;
	protected MenuItem newMenuItem;
	protected MenuItem mcToggleMenuItem;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			AppBoard window = new AppBoard();

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

		Board current =createContents(createBlank(), null);
		Menu menuBar=createMenu(current);
		shell.setMenuBar(menuBar);
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

	private Menu createMenu(Board curr) {
		Menu bar= new Menu(shell,SWT.BAR);
		MenuItem fileItem = new MenuItem (bar, SWT.CASCADE);
		fileItem.setText ("&File");
		Menu fileMenu = new Menu (shell, SWT.DROP_DOWN);
		fileItem.setMenu (fileMenu);
		saveMenuItem = new MenuItem (fileMenu, SWT.PUSH);
		saveMenuItem.setData(curr.getShell());
		saveMenuItem.addSelectionListener (curr.saveASAdapter);
		saveMenuItem.setText ("Save\t Ctrl+S");
		saveMenuItem.setAccelerator (SWT.MOD1 + 'S');
		
		
		openMenuItem=new MenuItem (fileMenu, SWT.PUSH);
		openMenuItem.setData(curr.getShell());
		openMenuItem.addSelectionListener (curr.openBoardAdapter);
		openMenuItem.setText ("Open\t Ctrl+O");
		openMenuItem.setAccelerator (SWT.MOD1 + 'O');
		
		newMenuItem=new MenuItem (fileMenu, SWT.PUSH);
		newMenuItem.setData(curr.getShell());
		newMenuItem.addSelectionListener (curr.newBoardAdapter);
		newMenuItem.setText ("New\t Ctrl+N");
		newMenuItem.setAccelerator (SWT.MOD1 + 'N');
		return bar;
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
			Menu menuBar=createMenu(openBoard);
			shell.setMenuBar(menuBar);
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
			boolean mC=cats[0].getQuestions()[0].getAnswer().split("\\^").length>2;
			for(String a:cats[0].getQuestions()[0].getAnswer().split("\\^"))
				System.out.println(a);
			return new Board(shell, SWT.NONE, cats, source,mC);
		}
		return new Board(shell, SWT.NONE, cats);

	}

	private Category[] createBlank() {
		Category[] cats = new Category[7];
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

	static public Category[] parseBoard(File source) {
		Category[] cats = new Category[7];
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
			boolean sameType = true;

			while (catNum < 6 && scanner.hasNextLine()) {
				text = scanner.nextLine();
				switch (position) {

				// WHEN POSITION IS A NEW CATEGORY
				case "newCat":
					if (text.length() > 1) {
						currentCat = new Category(text.trim(), catNum);
					} else if (text.length() > 0) {
						currentCat = new Category(" ", catNum);
					}
					position = "question";
					break;

				// WHEN POSITION IS A NEW QUESTION
				case "question":
					try {
						answer = scanner.nextLine();
						dd = scanner.nextLine();
						format = scanner.nextLine();
						if (answer.endsWith("^^^^") && answer.length() > 4) {
							answer = answer.substring(0, answer.lastIndexOf("^^^^"));
						} else if (answer.length() <= 4) {
							answer = " ";
						}
						
						if (qNum > 0 && sameType) {
							sameType = questions[qNum - 1].getTypeDetails().contains(format);
						}

						questions[qNum] = new Question(text.trim(), answer.trim(), dd.charAt(0), "", format, qNum);

						qNum++;
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						Shell fuck=new Shell();
						fuck.open();
						Image sad=SWTResourceManager.getImage(AppBoard.class, "MEDowo.png");
						fuck.setBackgroundImage(sad);
						fuck.setSize(fuck.computeSize(sad.getBounds().width,sad.getBounds().height));
						
						MessageBox ope = new MessageBox(fuck, SWT.OK);
						ope.setMessage("Hmmmmm.... This file doesn't look quite like I was expecting it to. "
								+ "\n You sure this is a trivia file that's formatted properly?"
								+ "\n If so please send it to Audrey to figure out why I can't read it");
						ope.setText("File Read Error");
						ope.open();
						fuck.dispose();
						e1.printStackTrace();
						return null;
					}
					// if we've got all out questions, change back to category start
					if (qNum > 4) {
						currentCat.addQuestions(questions);

						// set the type of the current category

						// if the text is more than 4 characters THEN its safe to do the switch
						// statement
						if (sameType) {
							switch (format.charAt(0)) {
							case 'P':
								currentCat.changeType("picture");
								break;
							case 'S':
								currentCat.changeType("audio");
								break;
							default:
								currentCat.changeType("text");
								break;
							}
						} else {
							currentCat.changeType("mixed");
						}

						// ELSE needs to end BEFORE these statements
						cats[catNum] = currentCat;
						catNum++;
						qNum = 0;
						position = "newCat";
						questions = new Question[5];
						sameType = true;
					}
					break;

				default:
					System.out.println("Umm something might be wrong? I recieved: " + position);
					break;
				}
			}
			try {
				text = scanner.nextLine();
				answer = scanner.nextLine();
				if (answer.endsWith("^^^^") && answer.length() > 4) {
					answer = answer.substring(0, answer.indexOf('^'));
				} else if (answer.length() <= 4) {
					answer = " ";
				}

				Question finQ = new Question(text.trim(), answer.trim(), 'N', "text", "", 0);
				cats[catNum] = new Category("final", new Question[] { finQ }, 7);
			} catch (Exception e1) {
				System.out.println("I couln't make the Final Question");
				e1.printStackTrace();
			}
			scanner.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return cats;
	}

	public static Composite getSad() {
		Shell fuck=new Shell();
		fuck.open();
		fuck.setBackgroundImage(null);
		Composite sad = new Composite(fuck, SWT.NONE);
		sad.setLayout(new FillLayout());
		Label owoSticker = new Label(sad, SWT.NONE);
		Image bigOwo = SWTResourceManager.getImage(AppBoard.class, "MEDowo.png");
		owoSticker.setImage(bigOwo);
		owoSticker.setBounds(bigOwo.getBounds());
		// owoSticker.setText("Plz stop breaking me");
		fuck.layout(true);
		fuck.pack();
		return sad;
	}
}
