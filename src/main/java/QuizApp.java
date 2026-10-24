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
           String header ;
           int contentLength =0;

           while((header=reader.readLine())!=null&&!header.isEmpty()){
               if(header.toLowerCase().startsWith("content-length:")){
                   contentLength = Integer.parseInt(header.substring(header.indexOf(':')+1).trim());
               }
           }


           char[] body = new char[contentLength];
           reader.read(body, 0, contentLength);
           String answers = new String(body);
           int score = 0;

           if (answers.contains("q1=extends")) score++;
           if (answers.contains("q2=main")) score++;
           if (answers.contains("q3=new")) score++;
           if (answers.contains("q4=int")) score++;
           if (answers.contains("q5=%3D%3D") || answers.contains("q5=%3d%3d") || answers.contains("q5===%3D")) score++;




           String html = Files.readString(
                   Path.of("src/main/resources/static/quiz.html")
           );
           if (requestLine.startsWith("POST /submit")) {
               html = "<h1>Your Score: " + score + "/5</h1><a href='/'>Try Again</a>";
           }
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
