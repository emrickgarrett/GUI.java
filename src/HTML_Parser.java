import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;


public class HTML_Parser {

	private File templateFolder;
	
	/**
	 * HTML_Parser that parses the Folder that comes in.
	 * @param templateFile
	 */
	public HTML_Parser(File templateFolder){
		this.templateFolder = templateFolder;
	
	}
	
	/**
	 * Potentially allow more than one HTML Template file, but for project this semester keep it limited to 1.
	 */
	public void readFiles(){
		//do read
		for(final File fileEntry : templateFolder.listFiles()){
			//read the top file then exit
			createFromTemplate(fileEntry);
			return; //For now, ideally it would read all files in HTML directory to generate the CSS
		}
		
	}
	
	
	/**
	 * Method creates a series of template files in the output folder based on input file
	 * @param file : The template file
	 */
	public void createFromTemplate(File file){
		Scanner scan = null;
		try{
			scan = new Scanner(file);
		}catch(Exception ex){
			ex.printStackTrace();
			System.err.println("There was an error opening the HTML file in the folder...");
			return;
		}
		
		String template = "";
		String curString = "";
		ArrayList<String> templateSplit = new ArrayList<String>();
		ArrayList<String> pageNames = new ArrayList<String>();
		
		
		while(scan.hasNext()){
			curString = scan.nextLine();
			if(curString.contains("top-menu")){
				int firstIndex = curString.indexOf("top-menu") + 9;
				
				template += curString.substring(0, firstIndex);
				curString = curString.substring(firstIndex, curString.length());
				
				
				String temp = "";
				boolean shouldCon = true;
				while(shouldCon && scan.hasNextLine()){
					curString += scan.nextLine();
					if(curString.indexOf("<div>") != curString.lastIndexOf("<div>")){
						if(curString.indexOf("</div>") != curString.lastIndexOf("</div>"))
								shouldCon = false;
					}
					if(!curString.contains("</div>")){
						shouldCon = true;
					}
				}
				
				
				String orig = curString;
				String replace = "";
				temp = orig.substring(orig.indexOf("<a"), orig.indexOf("</a>"));
				while(temp.contains("<a")){
					pageNames.add(temp.substring(temp.indexOf(">")+1));
					if(temp.contains("href=\"[%page]\"")){
						temp = temp.replace("href=\"[%page]\"", "href=\"" + pageNames.get(pageNames.size()-1) + ".html\"");
						replace += temp;
					}else
						replace += temp;
					if(orig.indexOf("</a>") == -1){
						
					}else if(orig.substring(orig.indexOf("</a>")).indexOf("<a") == -1){
						replace += orig.substring(orig.indexOf("</a>"));
					}else{try{
						if(orig.substring(orig.indexOf("</a>") + 4, 4).equals("</li>")){
							replace += "</a></li>";
						}
					}catch(Exception ex){
						replace +="</a>";
					}
					}
					orig = orig.substring(orig.indexOf("</a>")+4);
					if(orig.contains("<a") && orig.contains("</a>")){
						replace += orig.substring(0, orig.indexOf("<a"));
						temp = orig.substring(orig.indexOf("<a"), orig.indexOf("</a>"));
					}else{
						temp = "";
					}
					System.out.println(temp.substring(temp.indexOf(">")+1));
				}
				
				curString = curString.substring(0, curString.indexOf("<a")) + replace;
				
				
				
			}
			
			if(curString.contains("<?id/>")){
				while(curString.contains("<?id/>")){
					template += curString.substring(0, curString.indexOf("<?id/>"));
					templateSplit.add(template);
					curString = curString.substring(curString.indexOf("<?id/>")+6, curString.length());
					template = curString;
				}
			}else{
				template += curString;
			}
			
			
		}//End Scanning of file
		
		scan.close();
		templateSplit.add(template);
		
		createFiles(templateSplit, pageNames);
		
	}
	
	public void createFiles(ArrayList<String> templateSplit, ArrayList<String> pageNames){
		for(int i = 0; i < pageNames.size(); i++){
			
			
			File file = new File(Main.OUTPUT_directory + pageNames.get(i) + ".html");
			
			BufferedWriter bw;
			
			try{
				
				bw = new BufferedWriter(new FileWriter(file));
				
			}catch(Exception ex){
				ex.printStackTrace();
				System.err.println("Error creating the buffered writer for the file");
				return;
			}
			
			try{
				//create the file
				if(templateSplit.size() == 1){
					bw.write(templateSplit.get(0));
				}else{
					for(int j = 0; j < templateSplit.size()-1; j+=2){
						bw.write(templateSplit.get(j));
						bw.write(pageNames.get(i));
						bw.write(templateSplit.get(j+1));
					}
					if(templateSplit.size()%2 == 1){
						bw.write(pageNames.get(i));
						bw.write(templateSplit.get(templateSplit.size()-1));
					}
				}
			
			}catch(Exception ex){
				ex.printStackTrace();
				System.err.println("Error writing to the files");
			}
			
			
			try{
			bw.close();
			}catch(Exception ex){
				ex.printStackTrace();
				System.err.println("Error closing the buffered Writer");
			}
			
		}
		
		
	}

}
