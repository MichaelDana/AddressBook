import java.util.Map;
import java.util.function.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.*;
// import audit.*;

public class CommandModule {
    private static final Map<String, String> commandSyntaxMap = Stream.of(new Object[][] { 
        { "LIN", "LIN <userID>" }, 
        { "LOU", "LOU" }, 
        { "CHP", "CHP <old password>" }, 
        { "ADU", "ADU <userID>" }, 
        { "DEU", "DEU <userID>" }, 
        { "DAL", "DAL [<userID>]" }, 
        { "ADR", "ADR <recordID> [<field1=value1> <field2=value2> ...]" }, 
        { "DER", "DER <recordID>" }, 
        { "EDR", "EDR <recordID><field1=value1> [<field2=value2> ...]" }, 
        { "RER", "RER [<recordID>][<fieldname> ...]" }, 
        { "IMD", "IMD <Input_File>" },
        { "EXD", "EXD <Output_File>" },
        { "HLP", "HLP [<command name>]"},
        { "EXT", "EXT"},
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (String) data[1]));
    String field;
    private Map<String, Function<List<String>,Boolean>> commands;
    private AddressBook addressBook;
    private Authenticator authenticator;
    private AuditDatabase auditDatabase;
    UserModule userModule;

    public CommandModule(AddressBook addressBook, Authenticator authenticator, UserModule userModule){
        this.addressBook = addressBook;
        this.authenticator = authenticator;
        this.userModule = userModule;
        auditDatabase = AuditFactory.getAuditDatabase();
        commands = new HashMap<>();

        // Login
        commands.put("LIN", (args) -> {
            if(args.size() > 0){
                User userAttemptingLogin = userModule.getUser(args.get(0));
                if(!authenticator.login(userAttemptingLogin)){
                    if(userAttemptingLogin != null && userAttemptingLogin.getPassword().isEmpty())
                        System.out.print("This is the first time the account is being used. ");
                } else {
                    //Load users address book
                }
            } else {
                System.out.println("Invalid use:");
                commands.get("HLP").apply(new ArrayList(Arrays.asList("LIN")));
            }
            return false;
            }
        );
        //Logout 
        commands.put("LOU", (args) -> {
            authenticator.logout();
            // addressBook.flush()
            System.out.println("Not implemented");
            return false;
            }
        );
        //Change password
        commands.put("CHP", (args) -> {
            System.out.println("Not implemented");
            return false;
            }
        );
        //Add user
        commands.put("ADU", (args) -> {
            userModule.ADU(args.get(0));
            return false;
            }
        );
        //Delete user
        commands.put("DEU", (args) -> {
                userModule.DEU(args.get(0));
                return false;
            }
        );
        //Display audit log
        commands.put("DAL", (args) -> {
				if(args.size() == 1)
					auditDatabase.getAuditLog(authenticator, args.get(0));
				else if(args.size() == 0)
					auditDatabase.getAuditLog(authenticator);
                return false;
            }
        );
        //Add record
        commands.put("ADR", (args) -> {
                addressBook.ADR(Record.createRecordsFromArgs(args));
                return false;
            }
        );
        //Delete Record
        commands.put("DER", (args) -> {
                addressBook.DER(args.get(0));
                return false;
            }
        );
        //Edit Record
        commands.put("EDR", (args) -> {
                // editedRecord = Record.createRecordsFromArgs(args);
                System.out.println("Not implemented");
                return false;
            }
        );
        //Read record
        commands.put("RER", (args) -> {
                if(args.size()==1){
                    //reading full record
                    Record r = addressBook.RER(args.get(0));
                    //Print record out
                    if(r!=null) {
                        System.out.println(r.toString());
                    } else {
                        System.out.println("Reocrd not found.");
                    }
                } else {
                    //Read field of record
                }
                return false;
            }
        );
        //Import database
        commands.put("IMD", (args) -> {
                try {
                    addressBook.importAddressBook(args.get(0));
                } catch (Exception e) {
                }
                return false;
            }
        );
        //Export database
        commands.put("EXD", (args) -> {
            try {
                addressBook.exportAddressBook(); 
            } catch (Exception e) {
            }
                return false;
            }
        );
        //Help
        commands.put("HLP", (args) -> {
                if(args.size() > 0){
                    String helpCommand = args.get(0);
                    if(commandSyntaxMap.containsKey(helpCommand)){
                        System.out.println(commandSyntaxMap.get(helpCommand));
                    } else {
                        System.out.println(helpCommand + " is not a supported command");
                    }
                    
                } else {
                    for (Map.Entry<String,String> command : commandSyntaxMap.entrySet()){
                        System.out.println(command.getValue());
                    } 
                }
                return false;
            }
        );
        //Exit
        commands.put("EXT", (args) -> {
                return true;
            }
        );
    }

    public boolean executeCommand(String fullCommand){
        //Parse command
        String command = "";
        ArrayList<String> args = new ArrayList(Arrays.asList(fullCommand.split("\\s+")));
        if(args.size() > 0){ 
            command = args.remove(0);
        }

        // //Verify User is authenticated
        // if(!command.equals("LIN")){
        //     return authenticator.isUserAuthen();
        // }

        if(commands.containsKey(command)){
            //Try to execute command
            try{
                return (Boolean)commands.get(command).apply(args);
            } catch (Exception e){
                System.out.println("Error: Failed to execute command: " + command);
            }
        } else {
            //Not a valid command
            System.out.println("Error: " + command + " is not a valid command");
        }
        return false;
    }
}
