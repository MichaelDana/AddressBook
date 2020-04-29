import java.util.*;

public class aba{
    
    public static void main(String[] args) {
        Scanner userIn = new Scanner(System.in);
        boolean terminateProgram = false;
        CommandModule commandModule = new CommandModule();
        
        while(!terminateProgram){
            //Login user
            boolean userLoggedIn = true; //temp
            while(!terminateProgram && userLoggedIn){
                //Begin accepting commands from user
                System.out.print("> ");
                //interpret command
                terminateProgram = commandModule.executeCommand(userIn.nextLine());
            }
        }
    }
}