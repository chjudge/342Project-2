
/** Author:  Clayton Judge
  * Course:  COMP 342 Data Communications and Networking
  * Date:    5 May 2021
  * Description: Server side of an HTTP application
*/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class serverHTTP {
    private final static int port = 80;

    public static void main(String[] args) {
        try {
            // create a ServerSocket object connected to port 80
            ServerSocket serverSocket = new ServerSocket(port);

            for (;;) {
                // This socket is only created when a client connects to this server
                Socket mySocket = serverSocket.accept();

                InputStreamReader streamReader = new InputStreamReader(mySocket.getInputStream());

                BufferedReader reader = new BufferedReader(streamReader);

                PrintWriter writer = new PrintWriter(mySocket.getOutputStream());

                String line = reader.readLine();            
                while (!line.isEmpty()) {
                    System.out.println(line);
                    line = reader.readLine();
                }

                // mySocket.close();
            }

            // serverSocket.close();
        } catch (IOException e) {
            System.err.println("Error: Connection terminated unexpectedly");
        }
    }
}
