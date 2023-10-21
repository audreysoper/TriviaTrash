package orgObjects;

import java.io.PrintWriter;

public class Question {
	private String q;
	private String a;
	private char dailyDouble;
	private String type;
	private String level;
	private int lvlIndex;
	private Category category;
	private String format;
	private String mediaFileName;
	private String mediaPath;

	
	//Constructor
	public Question(String q, String a, char dailyDouble,String type,String format,int lvlIndex) {
		this.q=q;
		this.a=a;
		this.dailyDouble=dailyDouble;
		if(type.length()<1){
			switch (format.charAt(0)) {
				case 'P':
					this.type="picture";
					//pathParts.addAll(List.of(questions[0].getQuestion().split(String.valueOf(source.separatorChar))));
					break;
				case 'S':
					this.type="audio";
					//pathParts.addAll(List.of(questions[0].getQuestion().split(String.valueOf(source.separatorChar))));
					break;
				default:
					this.type="text";
					break;
			}
		}

		this.lvlIndex=lvlIndex;
		this.level= String.valueOf((lvlIndex+1));
		this.format=format;
		try{

			if(type!="text"&& q.contains("/")){

				this.mediaFileName=q.substring(q.lastIndexOf('/'));
				this.mediaPath=q.substring(0, q.lastIndexOf('/'));
			}
		}catch(Exception e){
			mediaFileName="";
			mediaPath="";
			e.printStackTrace();

		}
	}

	public void changePath(String newPath,boolean isPrefix){
		if(newPath.charAt(newPath.length()-1)!='/'){
			newPath+='/';
		}
		if(isPrefix){
			this.mediaPath=newPath+this.mediaPath;
		}else{
			this.mediaPath=newPath;
		}
	}


	public int getLvlIndex() {
		return this.lvlIndex;
		
	}
	public String getLevel() {

		return this.level;
	}

	public String getQuestion() {
		return this.q;
		
	}
	public String getAnswer() {
		return this.a;
		
	}
	

	public String setQuestion(String s){
		this.q=s;
		return this.q;
	}
	public String setAnswer(String s) throws Exception {
		int priorCarats=this.a.split("^" ).length;
		if (priorCarats!=s.split("^").length){
			throw new Exception();
		}
		this.a=s;
		return this.a;
	}
	public String getFormat() {
		// TODO Auto-generated method stub
		return this.format;
	}
	
	public Category getCategory() {
		return this.category;
	}

	//I only want Category to be able to call this
	protected void setCategory(Category cat) {
		this.category=cat;
	}
	public String getType(){
		return this.type;
	}
	public void setType(String newType) {
		this.type=newType;
		switch(newType) {
		case "text":
			this.format="T#MS Gothic#28#True#False#16645837#";
			break;
		case "picture":
			this.format="P";
			break;
		case "audio":
			this.format="S";
			break;
		default:
			this.format="";
		
		}
		
	}

	public boolean getDD() {
		// TODO Auto-generated method stub
		return (dailyDouble=='Y');
	}
	
	public void updateQobj(String q, String a) {
		this.q=q;
		this.a=a;
	}

	public void setDD(boolean selection) {
		// TODO Auto-generated method stub
		if (selection) {this.dailyDouble='Y';}
		else {this.dailyDouble='N';}
	}

	public void clear() {
		this.q="";
		this.a="";
		
	}
	public void printForExport(PrintWriter out){


	}

}
