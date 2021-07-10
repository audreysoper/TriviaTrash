package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

import orgObjects.Category;
import orgObjects.Question;

public class Reader {

	public static HashSet<Category> library;
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
		File dir = new File("dummydata");
		File[] directoryListing = dir.listFiles();
		  if (directoryListing != null) {
		    for (File child : directoryListing) {
		    	parseBoards(child);
		    }
		  }
	}

	public static void parseBoards(File source) {
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
			
			while(catNum<7 && scanner.hasNextLine()) {
				text=scanner.nextLine();
				switch(position) {
				
				case "newCat":
					if(text.length()>0) {
						currentCat= new Category(text,catNum);
						position="question";
						catNum++;
					}
					break;
					
				case "question":
					String bop=text.substring(text.length()-3);
						switch(bop) {
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
						
					answer=scanner.nextLine();
					dd=scanner.nextLine();
					format=scanner.nextLine();
					questions[qNum]=new Question(text,answer,dd.charAt(0),currentCat.getType(),format,qNum+1);
					qNum++;
					//if we've got all out questions, change back to category start
					if(qNum>4) {
						qNum=0;
						position="newCat";
						library.add(currentCat);
					}
					break;
					
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
		
		
		for(Category c :library) {
			System.out.println(c.getName());
			System.out.println(c.getType());
			System.out.println();
		}
	}
}
