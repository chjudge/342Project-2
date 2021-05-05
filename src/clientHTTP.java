/** Author:  Clayton Judge
  * Course:  COMP 342 Data Communications and Networking
  * Date:    5 May 2021
  * Description: Client side of an HTTP application
*/
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class clientHTTP {
    private static String host;
    private static String path = null;
    private final static int port = 80;
    public static void main(String[] args) {
        //return if no host specified
        if (args.length < 1) {
            System.err.println("Error: insufficient command line arguments");
            return;
        }
        //get host and file path from command line argument
        host = args[0];

        if (args.length > 1) {
            path = args[1];
        } else {
        	path = "index.html";
        }
        try {
            //create a socket that connects to the server on port 80
            Socket mySocket = new Socket(host, port);

            System.out.println("Connection established");

            InputStreamReader streamReader = new InputStreamReader(mySocket.getInputStream());

            BufferedReader reader = new BufferedReader(streamReader);

            PrintWriter writer = new PrintWriter(mySocket.getOutputStream(), true);

            writer.println("GET / HTTP/1.1");
            writer.println("Host: " + host + ":" + port);
            writer.println("Connection: Close");
            writer.println();

//            System.out.println("response:");

            StringBuilder sb = new StringBuilder();

            for (int i = 0;;) {
                if (reader.ready()) {
                    while (i != -1) {
                        i = reader.read();
                        sb.append((char) i);
                    }
                    break;
                }
            }

            //run the parse on the server response
            String newStr = PARSE(sb.toString());
            
            // save to path
            SAVE(newStr, path);
           
            //confirmation message
            System.out.println("Saved to: " + path);
            
            mySocket.close();
         
        } catch (IOException e) {
            System.err.println("Error: Connection terminated unexpectedly");
        }
    }
    
    /**
     * parse data from response
     * @param str server response with header and data
     * @return data alone without header
     */
    public static String PARSE(String str) {
    	
    	String newStr = ""; //initialize string for parsed
    	
    	Scanner scan = new Scanner(str);
    	
    	//read through header
    	while (scan.hasNext()) { 
    		String curr = scan.next();
    		
    		//break loop after the connection termination message
    		if (curr.equals("Connection:")) {
    			scan.nextLine();
    			scan.nextLine();
    			break;
    		}
    	}
    	
    	//read rest of string (body of response)
    	while (scan.hasNextLine()) {
    		newStr += scan.nextLine() + "\n";
    	}
    	
    	scan.close();
    	return newStr;
    }
    
    /**
     * saves the data to a file
     * @param str data to save to file
     * @param path file where data is to be saved
     * @throws IOException
     */
    
    public static void SAVE(String str, String path) throws IOException {
    	
        File out = new File(path); //open file 
        FileOutputStream fOut = new FileOutputStream(out);
        
        // convert string to bytes for fos
        byte[] strB = str.getBytes();
        // write string to file
        fOut.write(strB);
       
        fOut.flush();
        fOut.close();
    }
}
