/** Author:  Clayton Judge
  * Course:  COMP 342 Data Communications and Networking
  * Date:    5 May 2021
  * Description: Server side of an HTTP application
*/
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public class serverHTTP {
    private static final int PORT = 80;
    private static final String END = "\r\n";
    private static final Path CURRENT_DIRECTORY = Paths.get(System.getProperty("user.dir"));
    private static String fileName = "";

    public static void main(String[] args) {
        try {
            // create a ServerSocket object connected to port 80
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println(CURRENT_DIRECTORY.toString());

            while (true) {
                System.out.println("\nListening for connection on port " + PORT + "...");
                // This socket is only created when a client connects to this server
                Socket socket = serverSocket.accept();

                // read data over the socket
                InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                // write data over the socket
                OutputStream outStream = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(outStream, true);

                //get file name from the request
                String line = reader.readLine();
                if (line.startsWith("GET")) {
                    fileName = line.substring(line.indexOf('/'), line.lastIndexOf("HTTP") - 1);
                    if (fileName.trim().equals("/")) {
                        fileName = "/index.html";
                    }
                    fileName = fileName.substring(1);
                }
                // read in HTTP request
                while (!line.isEmpty()) {
                    System.out.println(line);
                    line = reader.readLine();
                }

                System.out.println("\nSending response...");

                // send requested file
                File file = new File(fileName);

                StringBuilder sb = new StringBuilder(8096);

                if (file.exists()) {
                    sb.append("HTTP/1.1 200 OK" + END);
                } else {
                    sb.append("HTTP/1/1 404 Not Found" + END);
                }
                sb.append("Date: " + new Date() + END);
                sb.append("Server: my custom server :)" + END);
                
                sb.append("Connection: closed" + END);
                sb.append(END);

                System.out.println(sb.toString());

                writer.write(sb.toString());
                writer.flush();

                sb.delete(0, sb.length());

                // send file to client
                if (file.exists()) {
                    try {
                        FileInputStream fileStream = new FileInputStream(file);

                        // send file 4 kilobytes at a time
                        byte[] buffer = new byte[4 * 1024];
                        int bytes;
                        // add bytes to the buffer until the end of the file is reached
                        while ((bytes = fileStream.read(buffer)) != -1) {
                            // System.out.write(buffer, 0, bytes);
                            outStream.write(buffer, 0, bytes);
                            outStream.flush();
                        }

                        fileStream.close();
                    } catch (FileNotFoundException e) {
                        System.out.println("Error: Invalid File Name");
                        writer.close();
                        return;
                    }
                } else {
                    //send 404 page
                    sb.append("<!DOCTYPE html>" + END);
                    sb.append("<html>" + END);
                    sb.append("<head>" + END);
                    sb.append("<title>404 Not Found</title>" + END);
                    sb.append("</head>" + END);
                    sb.append("<body>" + END);
                    sb.append("<h1>Not Found</h1>" + END);
                    sb.append("<p>The requested URL /" + fileName + " was not found on this server.</p>" + END);
                    sb.append("<body>" + END);
                    sb.append("</html>" + END);

                    writer.write(sb.toString());

                    
                }

                System.out.println("Done responding to client...");

                // close everything
                fileName = "";
                writer.close();
                reader.close();
                socket.close();
            }

            // serverSocket.close();
        } catch (IOException e) {
            System.err.println("Error: Connection terminated unexpectedly");
        }
    }
}