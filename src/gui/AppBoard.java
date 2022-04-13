package gui;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import orgObjects.Category;
import orgObjects.Question;

import java.io.File;
import java.util.Scanner;
import java.util.prefs.Preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
	protected MenuItem pathingMenuItem;
	public static String homeFolder="";
	public static Preferences userPrefs;
	public static boolean advancedPathing;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		userPrefs = Preferences.userRoot();
		advancedPathing = userPrefs.getBoolean("advancedPathing", false);

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
		int shellW = (display.getBounds().width) *4/5;


		Board current = createContents(createBlank(), null);
		Menu menuBar = createMenu(current);
		shell.setMenuBar(menuBar);

		shell.open();
		// shell.layout();
		// shell.pack();
		Point size = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		shell.setBounds(0,0,shellW, size.y / 3 * 2);
	

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
		
		pathingMenuItem = new MenuItem (fileMenu, SWT.CHECK);
		pathingMenuItem.setData(curr.getShell());
		pathingMenuItem.setText ("Toggle Advanced Pathing");
		pathingMenuItem.setSelection(advancedPathing);
		pathingMenuItem.addSelectionListener (new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				advancedPathing=!advancedPathing;
				userPrefs.putBoolean("advancedPathing", advancedPathing); 
				pathingMenuItem.setSelection(advancedPathing);
				
			
			}
		});
		
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

			Menu menuBar = createMenu(openBoard);
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
			boolean mC = cats[0].getQuestions()[0].getAnswer().split("\\^").length > 2;
			for (String a : cats[0].getQuestions()[0].getAnswer().split("\\^"))
				System.out.println(a);
			return new Board(shell, SWT.NONE, cats, source, mC);
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
			String question;
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
						question = text;
						if (question.contains("^^^^"))
							throw new Exception("Your question looks like an answer");
						text = scanner.nextLine();
						answer = text;
						if (answer.length() > 3 && !answer.contains("^^^^"))
							throw new Exception("Answer isn't formatted correctly");

						text = scanner.nextLine();
						dd = text;
						if (dd.length() > 2)
							throw new Exception("That doesn't look like a daily double...");

						text = scanner.nextLine();
						format = text;
						if (format.length() > 2 && !format.contains("#"))
							throw new Exception("This question formatting line is incorrect");

						if (answer.endsWith("^^^^") && answer.length() > 4) {
							answer = answer.substring(0, answer.lastIndexOf("^^^^"));
						} else if (answer.length() <= 4) {
							answer = " ";
						}

						if (qNum > 0 && sameType) {
							sameType = questions[qNum - 1].getTypeDetails().contains(format);
						}

						questions[qNum] = new Question(question.trim(), answer.trim(), dd.charAt(0), "", format, qNum);

						qNum++;
					} catch (Exception e1) {
						// TODO Auto-generated catch block

						Shell fuck = new Shell();
						fuck.open();
						fuck.setLayout(new FillLayout());
						Label actualError = new Label(fuck, SWT.NONE);
						String trace = "FAILED AT: " + text + "\n" + e1.getMessage() + "\n";
						for (StackTraceElement eE : e1.getStackTrace()) {
							trace += eE.toString() + "\n";
						}
						actualError.setText(trace);

						Image sad = SWTResourceManager.getImage(AppBoard.class, "MEDowo.png");
						actualError.setBackgroundImage(sad);
						fuck.setSize(fuck.computeSize(sad.getBounds().width, sad.getBounds().height));

						MessageBox ope = new MessageBox(fuck, SWT.OK);

						ope.setMessage("Hmmmmm.... This file doesn't look quite like I was expecting it to. "
								+ "\n HINT: There's some info about what might be wrong in the sad-face window");
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

			} catch (Exception e1) {
				System.out.println("I couln't make the Final Question");
				text = "I couldn't find a final";
				answer = "so this is here instead";
			}

			Question finQ = new Question(text.trim(), answer.trim(), 'N', "text", "", 0);
			cats[catNum] = new Category("final", new Question[] { finQ }, 7);

			scanner.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("what the fuck");
		}

		return cats;
	}


}
