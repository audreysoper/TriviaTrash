package orgObjects;

import java.util.ArrayList;

public class Category {
	private String name;
	private Question[] questions;
	private int size;
	private String type;
	final public int catGroupIndex;
	private boolean isFinal;
	
	//Constructor for if you have all questions
	// hi!
	public Category(String name, Question[] questions,int catGroupIndex) throws Exception {
		this.catGroupIndex=catGroupIndex;
		if(questions.length!=5 && questions.length!=1 ) {
			throw new Exception("Categories need to be exactly 5 questions, or 1 question");
		}
		for(int i=0;i<questions.length;i++) {
			if(questions[i].getLvlIndex() != i) {
				throw new Exception("Your questions don't appear to be ordered correctly");
			}
		}
		this.name=name;
		this.questions=questions;
		this.size=questions.length;

		for (Question question : questions) {
			question.setCategory(this);
		}
	}
	
	//Constructor for if you only have name
	public Category(String name,int catGroupIndex) {
		this.catGroupIndex=catGroupIndex;
		this.name=name;

		if (catGroupIndex==7) {
			this.isFinal=true;
		}
	}
	
	
	
	
	//ADD QUESTIONS
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
		//But DON'T bother if they're mixed
		if(this.questions.length==5 && !newType.contains("mixed")) {
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
	public String getName() {
		return this.name;
	}
	
	public String getType() {
		// TODO Auto-generated method stub
		if (this.type==null){
			this.type="text";
		}
		return this.type;
	}
	
	public Question[] getQuestions() {
		return this.questions;
	}
	
	public void addQPrefix(String prefix,boolean isPath) {
		if (this.type!= "Text"&& isPath) {
			for (Question q:this.questions) {
				if(q.getFormat().charAt(0)!='T'){
					q.changePath(prefix, true);
				}
			}

		}else{ //this is for when it's just text
			for (Question q:this.questions) {
				if(q.getFormat().charAt(0)=='T'){
					//TODO: not sure if I care about implementing for text, but it's here
				}
			}
		}

		
	}

	public boolean isFinal() {
		return isFinal;
	}

	public String[] getCommon(){
		if(this.type.contains("text")) return new String[]{""};
		String[] common=null;
		for (Question q:questions) {
			String[] text=q.getQuestion().split("\\\\");
			System.out.println(text[0]);
			if(q.getFormat().length()<3 && text.length>1){
				if(common==null) common=text;
				else {
					ArrayList<String> newL=new ArrayList<String>();

					for(int i=0;i<common.length&&i< text.length;i++){
						System.out.println(text[i]);
						if(common[i].contains(text[i])) newL.add(common[i]);
					}
					common= new String[1];
					common= newL.toArray(common);
				}
			}
		}
		if(common==null) return new String[]{""};
		return common;
	}
}
