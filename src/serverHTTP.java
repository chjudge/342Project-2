
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
import java.util.Date;

public class serverHTTP {
    private final static int port = 80;

    public static void main(String[] args) {
        try {
            // create a ServerSocket object connected to port 80
            ServerSocket serverSocket = new ServerSocket(port);

            while(true) {
                System.out.println("Listening for connection on port " + port + "...");
                // This socket is only created when a client connects to this server
                Socket socket = serverSocket.accept();
                
                //read data over the socket
                InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                //write data over the socket
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

                String line = reader.readLine();            
                while (!line.isEmpty()) {
                    System.out.println(line);
                    line = reader.readLine();
                }

                Date date = new Date();
                String response = "HTTP/1.1 200 OK\r\n\r\n" + date;
                writer.write(response);
                writer.flush();

                //close everything
                reader.close();
                streamReader.close();
                writer.close();
                socket.close();
            }

            // serverSocket.close();
        } catch (IOException e) {
            System.err.println("Error: Connection terminated unexpectedly");
        }
    }
}
