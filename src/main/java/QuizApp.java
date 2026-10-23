import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class QuizApp {
    public static void main(String[] args)throws IOException {
        System.out.println("Java Quiz Server");
        ServerSocket server = new ServerSocket(8080);
        System.out.println("Server started on port 8080");

       while (true){
           Socket client = server.accept();

           BufferedReader reader = new BufferedReader(
                   new InputStreamReader(client.getInputStream())
           );
            String requestLine = reader.readLine();
           System.out.println("Request : " + requestLine);

           String html = Files.readString(
                   Path.of("src/main/resources/static/quiz.html")
           );

           OutputStream out = client.getOutputStream();
           String response =
                          "HTTP/1.1 200 OK\r\n"+
                          "Content-type: text/html\r\n"+
                                   "\r\n"+
                           html;

                   out.write(response.getBytes(StandardCharsets.UTF_8));
                    out.flush();
           System.out.println("Java Quiz Server is running");
       }
    }
}
