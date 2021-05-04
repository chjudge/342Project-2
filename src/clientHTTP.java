/** Author:  Clayton Judge
  * Course:  COMP 342 Data Communications and Networking
  * Date:    5 May 2021
  * Description: Client side of an HTTP application
*/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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

            System.out.println("response:");

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

            System.out.println(sb.toString());


            mySocket.close();
        } catch (IOException e) {
            System.err.println("Error: Connection terminated unexpectedly");
        }
    }
}
