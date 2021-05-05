
/** Author:  Clayton Judge
  * Course:  COMP 342 Data Communications and Networking
  * Date:    5 May 2021
  * Description: Client side of an HTTP application
*/
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class clientHTTP {
	
    private static String host;
    private static String path = null;
    private final static int port = 80;


    public static void main(String[] args) {
        // return if no host specified
        if (args.length < 1) {
            System.err.println("Error: insufficient command line arguments");
            return;
        }
        // get host and file path from command line argument
        host = args[0];

        if (args.length > 1) {
            path = args[1];

            if (path.isBlank()) {
                path = "index.html";
            }
        } else {
            path = "index.html";

        }
        try {
            // create a socket that connects to the server on port 80
            Socket mySocket = new Socket(host, port);

            System.out.println("Connection established to " + host);

            InputStreamReader streamReader = new InputStreamReader(mySocket.getInputStream());

            BufferedReader reader = new BufferedReader(streamReader);

            PrintWriter writer = new PrintWriter(mySocket.getOutputStream(), true);

            writer.println("GET /" + path + " HTTP/1.1");
            writer.println("Host: " + host + ":" + port);
            writer.println("Accept: text/*");
            writer.println("Connection: Close");
            writer.println();


            writer.flush();



            // run the parse on the server response
            boolean fileExists = PARSE(reader);


            System.out.println(path);

            // save to path
            if (fileExists) {
                if (path.lastIndexOf('\\') > 0) {
                    path = path.substring(path.lastIndexOf('\\') + 1);
                }
                System.out.println(path);
                SAVE(reader, path);
                // confirmation message
                System.out.println("Saved to: " + path);
            }
            
//            System.out.println(getSub("yet.txt"));

            reader.close();
            writer.close();
            mySocket.close();

        } catch (IOException e) {
            System.err.println("Error: Connection terminated unexpectedly");
        }
    }

    /**
     * parse data from response
     * 
     * @param reader BufferedReader to read the HTTP response from the server
     * @return true if the server has the requested file, false otherwise
     * @throws IOException
     */

    public static boolean PARSE(BufferedReader reader) throws IOException {
        String line = reader.readLine(); // initialize string for parsed

        if (line.contains("404")) {
            while (!line.isBlank()) {
                line = reader.readLine();
            }
            return false;
        } else if (line.contains("200")) {
            while (!line.isBlank()) {
                line = reader.readLine();
            }
            return true;
        }
        return false;
    }

    /**
     * saves the data to a file
     * 
     * @param reader data to save to file
     * @param path   file where data is to be saved
     * @throws IOException
     */

    public static void SAVE(BufferedReader reader, String path) throws IOException {
        File out = new File(path); // open file
        FileWriter fOut = new FileWriter(out);

        String line = reader.readLine();
        while (line != null) {
            fOut.write(line + "\n");
            line = reader.readLine();
        }

        fOut.flush();
        fOut.close();
    }
    
//    public static String getSub(String path) {
//    	
//    	Scanner scan = new Scanner(path);
//    	
//    	scan.useDelimiter("/");
//    	
//    	String curr = path;
//    	
//    	while (scan.hasNext()) {
//    		curr = scan.next();
//    	}
//    	
//    	    	
//    	return curr;
//    }
}
