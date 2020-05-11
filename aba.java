import java.util.*;
import java.io.File;

public class aba{
    
    public static void main(String[] args) {
        //Setup address book file
        boolean addressBookFileExists = false;
        ArrayList<File> files = new ArrayList(Arrays.asList(new File("./").listFiles()));
        for(File f:files){
                if(f.getName().equals("address_book")){
                     addressBookFileExists = true;
                        break;
                }
        }
        if(!addressBookFileExists){
            new File("./address_book").mkdir();
        }
        Scanner userIn = new Scanner(System.in);
        boolean terminateProgram = false;
        Authenticator authenticator = new Authenticator(userIn);
        UserModule userModule = new UserModule(authenticator, userIn); 
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
            String userInput = userIn.nextLine();
            System.out.print("");            
            //interpret command
            terminateProgram = commandModule.executeCommand(userInput);
        }
        userModule.save();
        userIn.close();
    }
}