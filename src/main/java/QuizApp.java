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
           String feedback = "";

           if (answers.contains("q1=extends")){
               score++;
               feedback += "Q1:Correct<br>";
           }else if(answers.contains("q1=")){
               feedback+="Q1.Wrong<br>";
           }else{
               feedback += "Q1: Not answered<br>";
           }
           if (answers.contains("q2=main")) {
               score++;
               feedback += "Q2:Correct<br>";
           }else if(answers.contains("q2=")){
               feedback+="Q2.Wrong<br>";
           }else{
               feedback += "Q2: Not answered<br>";
           }
           if (answers.contains("q3=new")){
               score++;
               feedback += "Q3:Correct<br>";
           }else if(answers.contains("q3=")){
               feedback+="Q3.Wrong<br>";
           }else{
               feedback += "Q3: Not answered<br>";
           }
           if (answers.contains("q4=int")) {
               score++;
               feedback += "Q4:Correct<br>";
           }else if(answers.contains("q4=")){
               feedback+="Q4.Wrong<br>";
           }else{
               feedback += "Q4: Not answered<br>";
           }
           if (answers.contains("q5=%3D%3D") || answers.contains("q5=%3d%3d") || answers.contains("q5===%3D")){
               score++;
               feedback += "Q5:Correct<br>";
           }else if(answers.contains("q5=")){
               feedback+="Q5.Wrong<br>";
           }else{
               feedback += "Q5: Not answered<br>";
           }




           String html = Files.readString(
                   Path.of("src/main/resources/static/quiz.html")
           );
           if (requestLine.startsWith("POST /submit")) {
               html = "<h1>Your Score: " + score + "/5</h1>" +feedback+"<br><a href='/'>Try Again</a>";
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
