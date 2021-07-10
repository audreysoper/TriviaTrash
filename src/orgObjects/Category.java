package orgObjects;

public class Category {
	private String name;
	private Question[] questions;
	private int size;
	private String type;
	final public int catGroupIndex;
	
	//Constructor for if you have all questions
	// hi!
	public Category(String name, Question[] questions,int catGroupIndex) throws Exception {
		this.catGroupIndex=catGroupIndex;
		if(questions.length!=5) {
			throw new Exception("Categories need to be exactly 5 questions");
		}
		for(int i=0;i<questions.length;i++) {
			if(questions[i].getLvlIndex() != i) {
				throw new Exception("Your questions don't appear to be ordered correctly");
			}
		}
		this.name=name;
		this.questions=questions;
		this.size=questions.length;
		
		for(int i=0;i<questions.length;i++) {
			questions[i].setCategory(this);
		}
	}
	
	//Constructor for if you only have name
	public Category(String name,int catGroupIndex) {
		this.catGroupIndex=catGroupIndex;
		this.name=name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void addQuestions (Question[] questions) throws Exception{
		if(questions.length!=5) {
			throw new Exception("Categories need to be exactly 5 questions");
		}
		for(int i=0;i<questions.length;i++) {
			if(questions[i].getLvlIndex() != i) {
				throw new Exception("Your questions don't appear to be ordered correctly");
			}
		}
		//make the array MY array of questions
		this.questions=questions;
		this.size=questions.length;
		//go through each question and update it's category to match
		for(int i=0;i<questions.length;i++) {
			questions[i].setCategory(this);
		}
	}
	
	
	
	public void changeType(String newType){
		
		this.type=newType;
		
		//go through each question and update it's TYPE to match
		if(this.questions.length==5) {
			for(int i=0;i<this.questions.length;i++) {
				this.questions[i].setType(newType);
			}
		}
	}
	
	
	/* I...am not sure wtf this would be useful for????
	public void swapSameLevelQuestion(Question newQ) {
		int position=newQ.getLevel() +1;
		this.questions[position]=newQ;
	}
*/

	public String getType() {
		// TODO Auto-generated method stub
		return this.type;
	}
	
	public Question[] getQuestions() {
		return this.questions;
	}
	
	
	
}
