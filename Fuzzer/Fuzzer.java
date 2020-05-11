import java.io.*;
public class Fuzzer{
    public static void main(String[] args) {
        String command = "java -jar Fuzzer/AddressBook.jar";
        BufferedReader reader = null;
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            // reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            
            writer.write("LIN admin");
            writer.write("test");
            writer.write("test");
            writer.write("EXT");
            // String line;
            // line = reader.readLine();
            // System.out.println(line);
            // writer.write("LIN admin");
            // while ((line = reader.readLine()) != null) {
            //     System.out.println(line);
            // }

            // writer.close();
            // reader.close();
        } catch (Exception e){
            System.out.println(e);
        }
    }
}