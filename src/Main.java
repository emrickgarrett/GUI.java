import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;


/**
 * 
 * @author garrett
 * This project uses a template file located in HTML/Template.html
 * You can link to CSS files by putting them into the CSS folder and 
 * linking them as you would a normal HTML file.
 * 
 * You can also make small changes to each page based on new Tags...
 * 
 * <?id/> will put the pages id into each of the pages, good for Headers for example
 * 
 */
public class Main {

	static String HTML_directory = "./HTML";
	static String Additional_directory = "./Additional_Files";
	static String OUTPUT_directory = "./output/";
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Welcome to the GUI.Java portion of my project");
		System.out.println("If I continue the project, I will be creating a GUI to help with the process");
		System.out.println("For now, simply create a template HTML file and put it in the 'HTML' directory");
		System.out.println("You can read more in the README included with the project or on Github (emrickgarrett/GUI.java)");
		
		System.out.println("\n\nCopy your files into the HTML, CSS, JS, and Additional_HTML files and hit enter when you are ready. Your site will be located in the output folder");
		Scanner keyboardReader = new Scanner(System.in);
		keyboardReader.nextLine();
		
		Main main = new Main();
		
		keyboardReader.close();
	}
	
	public Main(){
		File HTML_folder = new File(Main.HTML_directory);
		File Additional_folder = new File(Main.Additional_directory);
		File Output_folder = new File(Main.OUTPUT_directory);
	
		checkDirectories(HTML_folder, Additional_folder, Output_folder);
		
		//Open up the HTML parser to read in the Template file, this version only supports one template file.
		HTML_Parser parser = new HTML_Parser(HTML_folder);
		
		
		try{
			copyDirectory(Additional_folder, Output_folder);
			}catch(IOException ex){
				ex.printStackTrace();
				System.err.println("Error copying your additional files to the output folder...");
		}
		
		parser.readFiles();
		
		
		
		
	}
	
	public void checkDirectories(File HTML_folder, File Additional_folder, File Output_folder){
		
		
		if(!HTML_folder.exists())
			HTML_folder.mkdir();
		if(!Additional_folder.exists())
			Additional_folder.mkdir();
		if(!Output_folder.exists())
			Output_folder.mkdir();
	}
	
	public void copyDirectory(File sourceLocation , File targetLocation)
		    throws IOException {

		        if (sourceLocation.isDirectory()) {
		            if (!targetLocation.exists()) {
		                targetLocation.mkdir();
		            }

		            String[] children = sourceLocation.list();
		            for (int i=0; i<children.length; i++) {
		                copyDirectory(new File(sourceLocation, children[i]),
		                        new File(targetLocation, children[i]));
		            }
		        } else {

		            InputStream in = new FileInputStream(sourceLocation);
		            OutputStream out = new FileOutputStream(targetLocation);

		            // Copy the bits from instream to outstream
		            byte[] buf = new byte[1024];
		            int len;
		            while ((len = in.read(buf)) > 0) {
		                out.write(buf, 0, len);
		            }
		            in.close();
		            out.close();
		        }
		    }

}
