//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package gui.qGang;

import gui.qGang.Qbox;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import orgObjects.Question;

public class Answer extends Composite {
	public final boolean multipleChoice;
	private Text[] answerBoxes;
	private Button[] correctSelector;
	private String correctAnswer;
	private String answerFormatted;

	public Answer(Qbox parent, Question q,boolean multipleChoice) {
		super(parent, 0);
		this.multipleChoice = multipleChoice;

		try {
			this.answerFormatted = q.getAnswer();
		} catch (Exception var5) {
			var5.printStackTrace();
			this.correctAnswer = "";
			this.answerFormatted = "";
		}

		GridLayout lay = new GridLayout(2, false);
		lay.marginHeight = 0;
		lay.marginWidth = 0;
		this.setLayout(lay);
		if (multipleChoice) {
			this.answerBoxes = new Text[4];
			this.correctSelector = new Button[4];

			for(int i = 0; i < this.answerBoxes.length; ++i) {
				this.answerBoxes[i] = new Text(this, 2052);
				this.answerBoxes[i].setLayoutData(new GridData(4, 4, true, true));
				this.correctSelector[i] = new Button(this, 16);
				this.correctSelector[i].setData(this.answerBoxes[i]);
				this.correctSelector[i].setLayoutData(new GridData(4, 4, false, false));
				this.correctSelector[i].addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						Button correct = (Button)e.widget;
						Answer.this.correctAnswer = ((Text)correct.getData()).getText();
						if (Answer.this.correctAnswer == null) {
							Answer.this.correctAnswer = " ";
						}

					}
				});
			}

			if (!this.answerFormatted.contains("^^^^") && this.answerFormatted.length() > 2) {
				this.parseMCAnsString(this.answerFormatted);
			}
		} else {
			this.correctAnswer = this.answerFormatted;
			this.answerBoxes = new Text[]{new Text(this, 2626)};
			GridData qAnswerLayoutDetails = new GridData(4, 4, true, true, 2, 1);
			this.answerBoxes[0].setMessage("Answer");
			this.answerBoxes[0].setLayoutData(qAnswerLayoutDetails);
			this.answerBoxes[0].setText(this.correctAnswer);
		}

		this.layout();
	}

	private void parseMCAnsString(String importedAnsText) {
		boolean answerFound = false;
		String[] answers = importedAnsText.split("\\^");
		System.out.println(importedAnsText);
		this.correctAnswer = answers[0].trim();

		for(int i = 0; i < this.answerBoxes.length && i + 1 < answers.length; ++i) {
			this.answerBoxes[i].setText(answers[i + 1].trim());
			if (answers[i + 1].trim().matches(answers[0].trim()) && !answerFound) {
				this.correctSelector[i].setSelection(true);
				answerFound = true;
			}
		}

	}

	public void clear() {
		for(int i = 0; i < this.answerBoxes.length; ++i) {
			this.answerBoxes[i].setText("");
			if (this.multipleChoice) {
				this.correctSelector[i].setSelection(false);
			}
		}

		this.correctAnswer = "";
		this.answerFormatted = "";
	}

	public String ansExport() {
		this.correctAnswer = this.answerBoxes[0].getText().trim();
		if (this.multipleChoice) {
			this.answerFormatted = "^ " + this.correctAnswer;
			Text[] var4;
			int var3 = (var4 = this.answerBoxes).length;

			for(int var2 = 0; var2 < var3; ++var2) {
				Text a = var4[var2];
				this.answerFormatted = this.answerFormatted + "^ " + a.getText();
			}

			this.answerFormatted = this.answerFormatted + "^^^^";
		} else {
			this.answerFormatted = this.correctAnswer + " ^^^^";
		}

		return this.answerFormatted;
	}

	public void changeQ(String answer) {
		if (this.multipleChoice) {
			this.parseMCAnsString(answer);
		} else {
			this.correctAnswer = answer;
			this.answerBoxes[0].setText(this.correctAnswer);
		}

	}

	public String getAnswer() {
		this.correctAnswer = this.answerBoxes[0].getText().trim();
		if (!this.multipleChoice) {
			return this.correctAnswer;
		} else {
			this.answerFormatted = "^ " + this.correctAnswer;
			Text[] var4;
			int var3 = (var4 = this.answerBoxes).length;

			for(int var2 = 0; var2 < var3; ++var2) {
				Text a = var4[var2];
				this.answerFormatted = this.answerFormatted + "^ " + a.getText();
			}

			return this.answerFormatted;
		}
	}
}
