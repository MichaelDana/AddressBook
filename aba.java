import java.util.*;

public class aba{
    
    public static void main(String[] args) {
        Scanner userIn = new Scanner(System.in);
        boolean terminateProgram = false;
        Authenticator authenticator = new Authenticator();
        UserModule userModule = new UserModule(authenticator); 
        AddressBook addressBook = new AddressBook();
        CommandModule commandModule = new CommandModule(addressBook, authenticator, userModule);
        
        while(!terminateProgram){
            //Login user
            // boolean userLoggedIn = false; //temp
            // while(!userLoggedIn){
            //     System.out.print("User ID: ");
            //     String userName = userIn.nextLine();
            //     commandModule.executeCommand("LIN " + userName);
            // }
            //Begin accepting commands from user
            System.out.print("> ");
            //interpret command
            terminateProgram = commandModule.executeCommand(userIn.nextLine());
        }
        userIn.close();
    }
}