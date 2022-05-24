package orgObjects;

public class Question {
	private String q;
	private String a;
	private char dailyDouble;
	private String type;
	private String level;
	private int lvlIndex;
	private Category category;
	private String format;
	
	//Constructor
	public Question(String q, String a, char dailyDouble,String type,String format,int lvlIndex) {
		this.q=q;
		this.a=a;
		this.dailyDouble=dailyDouble;
		this.type=type;
		this.lvlIndex=lvlIndex;
		this.level= String.valueOf((lvlIndex+1));
		this.format=format;
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
	
	//I only want Category to be able to call this
	protected void setCategory(Category cat) {
		this.category=cat;
	}

	public String getTypeDetails() {
		// TODO Auto-generated method stub
		return this.format;
	}
	
	public Category getCategory() {
		return this.category;
	}

	public void setType(String newType) {
		this.type=newType;
		switch(newType) {
		case "text":
			this.format="T";
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

	public boolean getDDbool() {
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
}
