package gui;

<<<<<<< Updated upstream
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
=======
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.*;
>>>>>>> Stashed changes
import org.eclipse.wb.swt.SWTResourceManager;

import orgObjects.Category;
import orgObjects.Question;

import java.io.File;
<<<<<<< Updated upstream
=======
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
>>>>>>> Stashed changes
import java.util.Scanner;
import java.util.prefs.Preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;

public class AppBoard {

<<<<<<< Updated upstream
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
=======
	protected static List<Shell> shells;


	public static Preferences userPrefs;
	public static boolean advancedPathing;
	public static boolean mcEnabled;
	public static boolean localize;
	public static Display display;


	public static SelectionListener newBoardAdapter = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			Composite b=((Button)e.widget).getParent();
			Board bb=(Board)b.getParent().getParent();
			AppBoard.openBlank(bb.newBoardIsMC);
		}
	};

	public static SelectionListener openBoardAdapter = new SelectionAdapter(){
		@Override
		public void widgetSelected(SelectionEvent e) {
			FileDialog chooser = new FileDialog((Shell) e.widget.getData(), SWT.OPEN);
			try {
				chooser.setFilterExtensions(new String[] { "*.txt" });
				chooser.open();

				if (chooser.getFileName().length() > 1) {
					File source = new File(chooser.getFilterPath() + "\\" + chooser.getFileName());
					//openFile(source);
					createContents(parseBoard(source),source);
				}
			} catch (Exception err) {
				// nuthin
				err.printStackTrace();
			}
		}
	};






>>>>>>> Stashed changes
	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

<<<<<<< Updated upstream
		userPrefs = Preferences.userRoot(); 
		advancedPathing=userPrefs.getBoolean("advancedPathing", false);
=======
		display = Display.getDefault();

		userPrefs = Preferences.userRoot();
		advancedPathing = userPrefs.getBoolean("advancedPathing", false);
		localize = userPrefs.getBoolean("localize", false);
		mcEnabled = userPrefs.getBoolean("mcEnabled", false);
		shells= new ArrayList<>();
>>>>>>> Stashed changes
		try {
			//AppBoard window = new AppBoard();

<<<<<<< Updated upstream
			window.openBlank();
=======
			openBlank(false);
>>>>>>> Stashed changes
		} catch (Exception e) {
			e.printStackTrace();

		}
	}



	/**
	 * Open the window.
	 */
<<<<<<< Updated upstream
	public void openBlank() {
=======
	public static void openBlank(boolean mc) {
>>>>>>> Stashed changes

		Shell shell=new Shell();
		shells.add(shell);
		shell.setLayout(new FillLayout());
		shell.setText("Trivia Question Viewer");
<<<<<<< Updated upstream
		
		
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

=======

		Board current =new Board(shell,SWT.NONE,createBlank(mc),mc);
		shell.setMenuBar(createMenu(shell,current));
		shell.open();
		shell.layout();
>>>>>>> Stashed changes
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

<<<<<<< Updated upstream
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
=======
	public static void redraw(){
		for (Shell s:shells
		) {
			s.requestLayout();
			s.redraw();
		}



	}
	private static Menu createMenu(Shell shell3, Board curr) {


		Menu bar = new Menu(shell3, SWT.BAR);
		MenuItem fileItem = new MenuItem(bar, SWT.CASCADE);
		fileItem.setText("&File");
		Menu fileMenu = new Menu(shell3, SWT.DROP_DOWN);
		fileItem.setMenu(fileMenu);
		MenuItem saveMenuItem = new MenuItem(fileMenu, SWT.PUSH);
		saveMenuItem.setData(shell3);
		saveMenuItem.addSelectionListener(curr.saveASAdapter);
		saveMenuItem.setText("Save\t Ctrl+S");
		saveMenuItem.setAccelerator(SWT.MOD1 + 'S');

		MenuItem openMenuItem = new MenuItem(fileMenu, SWT.PUSH);
		openMenuItem.setData(shell3);
		openMenuItem.addSelectionListener(openBoardAdapter);
		openMenuItem.setText("Open\t Ctrl+O");
		openMenuItem.setAccelerator(SWT.MOD1 + 'O');

		MenuItem newMenuItem = new MenuItem(fileMenu, SWT.PUSH);
		newMenuItem.setData(shell3);
		newMenuItem.addSelectionListener(newBoardAdapter);
		newMenuItem.setText("New\t Ctrl+N");
		newMenuItem.setAccelerator(SWT.MOD1 + 'N');

	
		MenuItem mcToggleMenuItem = new MenuItem(fileMenu, SWT.CHECK);
		mcToggleMenuItem.setData(shell3);
		mcToggleMenuItem.setText("Toggle Multiple Choice Mode");
		mcToggleMenuItem.setSelection(mcEnabled);
		mcToggleMenuItem.addSelectionListener(new PrefMenuListener(mcToggleMenuItem,mcEnabled,"mcEnabled"));
/*		mcToggleMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				mcEnabled = !mcEnabled;
				userPrefs.putBoolean("mcEnabled", mcEnabled);
				pathingMenuItem.setSelection(mcEnabled);

			}
		});
		*/
		MenuItem pathingMenuItem = new MenuItem(fileMenu, SWT.CHECK);
		pathingMenuItem.setData(shell3);
		pathingMenuItem.setText("Toggle Advanced Pathing");
		pathingMenuItem.setSelection(advancedPathing);
		pathingMenuItem.addSelectionListener(new PrefMenuListener(pathingMenuItem,advancedPathing,"advancedPathing"));
//		pathingMenuItem.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				advancedPathing = !advancedPathing;
//				userPrefs.putBoolean("advancedPathing", advancedPathing);
//				pathingMenuItem.setSelection(advancedPathing);
//				shell3.requestLayout();
//				shell3.redraw();
//
//			}
//		});

		MenuItem localizeMenuItem = new MenuItem(fileMenu, SWT.CHECK);
		localizeMenuItem.setData(shell3);
		localizeMenuItem.setText("Toggle LOCALIZE");
		localizeMenuItem.setSelection(localize);
		localizeMenuItem.addSelectionListener(new PrefMenuListener(localizeMenuItem,localize,"localize"));
//		localizeMenuItem.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				localize = !localize;
//				userPrefs.putBoolean("localize", localize);
//				localizeMenuItem.setSelection(localize);
//				shell3.requestLayout();
//				shell3.redraw();
//
//			}
//		});
>>>>>>> Stashed changes
		
		return bar;
	}

<<<<<<< Updated upstream
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
=======

>>>>>>> Stashed changes

	/**
	 * Create contents of the window.
	 */
<<<<<<< Updated upstream
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
=======
	public static void createContents(Category[] cats, File source) {

		System.out.println(cats[0].getQuestions()[0].getAnswer().split("\\^").length);

		Shell shell = new Shell();
		shells.add(shell);
		shell.setLayout(new FillLayout());
		shell.setText("Trivia Question Viewer");

		Board board;
		if (source != null) {
			boolean mC = cats[0].getQuestions()[0].getAnswer().split("\\^").length > 2;
			for (String a : cats[0].getQuestions()[0].getAnswer().split("\\^"))
				System.out.println(a);
			board=new Board(shell, SWT.NONE, cats, source, mC);
		}else if(cats[0].getQuestions()[0].getAnswer().split("\\^").length > 2) {
			board=new Board(shell, SWT.NONE, cats, true);
		}else{
			board=new Board(shell, SWT.NONE, cats);
		}


		shell.setMenuBar(createMenu(shell, board));
		shell.open();
		shell.layout();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}

		}
	}

	private static Category[] createBlank(boolean mc) {
>>>>>>> Stashed changes
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

	public static Category[] parseBoard(File source) {
		Category[] cats = new Category[7];
		try {
			Scanner scanner = new Scanner(source);
			String position = "newCat";
			int qNum = 0;
			int catNum = 0;
			Category currentCat = null;
			Question[] questions = new Question[5];
			String text;
			String answer;
			String dd;
			String format;
			
			boolean sameType = true;

			while (catNum < 6 && scanner.hasNextLine()) {
				text = scanner.nextLine();
				switch (position) {

<<<<<<< Updated upstream
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
						if(!(dd.contains("N")||dd.contains("Y"))) {
							System.out.println(dd);
							answer=answer+" "+dd;
							dd=scanner.nextLine();
						}
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
=======
					// WHEN POSITION IS A NEW CATEGORY
					case "newCat" -> {
						if (text.length() > 1) {
							currentCat = new Category(text.trim(), catNum);
>>>>>>> Stashed changes
						} else {
							currentCat = new Category(" ", catNum);
						}
						position = "question";
					}

					// WHEN POSITION IS A NEW QUESTION
					case "question" -> {
						try {
							question = text;
							if (question.contains("^^^^"))
								throw new Exception("Your question looks like an answer");
							text = scanner.nextLine();
							answer = text;
							if (answer.length() > 3 && !answer.contains("^"))
								throw new Exception("Answer isn't formatted correctly");

							text = scanner.nextLine();
							dd = text;
							if (dd.length() > 2)
								throw new Exception("That doesn't look like a daily double...");

							text = scanner.nextLine();
							format = text;
							if (format.length() > 2) {
								if (!format.contains("#")) {
									throw new Exception("This question formatting line is incorrect");
								} else {
									textStyle = format.substring(1);
									format = String.valueOf(format.charAt(0));
								}
							}

							if (answer.endsWith("^^^^") && answer.length() > 4) {
								answer = answer.substring(0, answer.lastIndexOf("^^^^"));
							} else if (answer.length() <= 4) {
								answer = " ";
							}

							if (qNum > 0 && sameType) {
								sameType = questions[qNum - 1].getFormat().contains(format);
							}

							questions[qNum] = new Question(question.trim(), answer.trim(), dd.charAt(0), "", format, qNum);

							qNum++;
						} catch (Exception e1) {
							return parseErrorScreen(e1,text,cats);

						}
						// if we've got all out questions, change back to category start
						if (qNum > 4) {
							currentCat.addQuestions(questions);

							// set the type of the current category

							// if the text is more than 4 characters THEN its safe to do the switch
							// statement
							if (sameType) {
								switch (format.charAt(0)) {
									case 'P' -> currentCat.changeType("picture");

									//pathParts.addAll(List.of(questions[0].getQuestion().split(String.valueOf(source.separatorChar))));
									case 'S' -> currentCat.changeType("audio");

									//pathParts.addAll(List.of(questions[0].getQuestion().split(String.valueOf(source.separatorChar))));
									default -> currentCat.changeType("text");
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
					}
					default -> {
						String err="Umm something might be wrong? I recieved: " + position;
						return parseErrorScreen(null,err,cats);

					}
				}
			}
			if(catNum<6){
				return parseErrorScreen(null,"Not Long Enough!",cats);


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
				if(scanner.hasNext("`")) {
					homeFolder=scanner.nextLine();
				}
			} catch (Exception e1) {
				System.out.println("I couln't make the Final Question");
				e1.printStackTrace();
			}
			scanner.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
<<<<<<< Updated upstream
			e.printStackTrace();
=======
			return parseErrorScreen(e,"what the fuck",cats);

>>>>>>> Stashed changes
		}

		return cats;
	}

<<<<<<< Updated upstream
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
=======
	public static Category[] parseErrorScreen(Exception e1, String text,Category[] cats){
		boolean canRectify=Arrays.stream(cats).filter(e -> e != null).count()>1;
		Shell fuck = new Shell();
		fuck.setLayout(new FillLayout());
		Label actualError = new Label(fuck, SWT.NONE);
		String trace=text;
		if(e1!=null){
			trace = "FAILED AT: " + text + "\n" + e1.getMessage() + "\n";
			for (StackTraceElement eE : e1.getStackTrace()) {
				trace += eE.toString() + "\n";
			}

		}

		actualError.setText(trace);
		Image sad = SWTResourceManager.getImage(AppBoard.class, "MEDowo.png");
		actualError.setBackgroundImage(sad);

		int style=SWT.OK;
		if(canRectify){
			style=SWT.ICON_WARNING | SWT.YES | SWT.NO;
		}
		MessageBox ope = new MessageBox(fuck, style);

		ope.setMessage("Hmmmmm.... This file doesn't look quite like I was expecting it to. "
				+ "\n HINT: There's some info about what might be wrong in the sad-face window");
		ope.setText("File Read Error");
		fuck.open();
		fuck.setSize(fuck.computeSize(sad.getBounds().width, sad.getBounds().height));
		Category[] newCats;
		switch (ope.open()){
			case SWT.YES ->{
				newCats=fillInBlanks(cats);
			}
			default -> {
				newCats=null;
			}
		}

		fuck.dispose();

		return newCats;

	}

	private static Category[] fillInBlanks(Category[] cats) {
		Question[] qs;
		int index=0;
		boolean mc = cats[0].getQuestions()[0].getAnswer().split("\\^").length > 2;
		for (Category c : cats) {
			if (c == null ||c.getQuestions().length<1) {
				qs = new Question[5];

				for (int j = 0; j < qs.length; j++) {
					if (mc) {
						qs[j] = new Question(" ", "^ ^ ^ ^", 'N', "text", "", j);
					} else {
						qs[j] = new Question(" ", "", 'N', "text", "", j);
					}
				}
				try{
					cats[index]=new Category(" ",qs,index);
				}catch (Exception e){
					System.out.println("yep it's here");
					e.printStackTrace();
					return null;
				}

			}
		index++;
		}
		return cats;
	}

	private static String findCommonFolder(Category[] cats){
		ArrayList<String> filePaths=new ArrayList<>();
		for(Category c:cats){
			if(!c.getType().contains("text")){
				for (Question q:c.getQuestions()) {
					String text=q.getQuestion();
					if(q.getFormat().length()<2 && text.length()>3){
						filePaths.add(text);
					}
				}
			}
		}
		return null;
	}

>>>>>>> Stashed changes
}


