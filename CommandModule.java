import java.util.Map;
import java.util.function.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandModule {
    Map<String, Function<List<String>,Boolean>> commands;
    AddressBook addressBook;

    public CommandModule(){
        addressBook = new AddressBook();
         commands = new HashMap<>();

        // Login
        commands.put("LIN", (args) -> {
            System.out.println("Not implemented");
            return false;
            }
        );
        //Logout 
        commands.put("LOU", (args) -> {
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
            System.out.println("Not implemented");
            return false;
            }
        );
        //Delete user
        commands.put("DEU", (args) -> {
                System.out.println("Not implemented");
                return false;
            }
        );
        //Display audit log
        commands.put("DAL", (args) -> {
                System.out.println("Not implemented");
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
                System.out.println("Not implemented");
                return false;
            }
        );
        //Export database
        commands.put("EXD", (args) -> {
                System.out.println("Not implemented");
                return false;
            }
        );
        //Help
        commands.put("HLP", (args) -> {
                System.out.println("Not implemented");
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