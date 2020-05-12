import java.util.Map;
import java.util.function.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.*;

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
        auditDatabase = (AuditDatabase)AuditFactory.getAuditDatabase();
        commands = new HashMap<>();

        // Login
        commands.put("LIN", (args) -> {
            if(args.size() > 0){
                User userAttemptingLogin = userModule.getUser(args.get(0));
                if(!authenticator.login(userAttemptingLogin)){
                    if(userAttemptingLogin != null && userAttemptingLogin.getPassword().isEmpty()){
                        System.out.print("This is the first time the account is being used. ");
                        userModule.CHP("", userAttemptingLogin.getUserId());
                        authenticator.authenticateUser(userAttemptingLogin);
                        auditDatabase.updateLog(AuditFactory.getAuditRecord(AuditRecordInterface.AuditType.L1, userAttemptingLogin.getUserId()));
                    } 
                    auditDatabase.updateLog(AuditFactory.getAuditRecord(AuditRecordInterface.AuditType.LF, ""));
                } else {
                    //Load users address book
                    auditDatabase.updateLog(AuditFactory.getAuditRecord(AuditRecordInterface.AuditType.LS, userAttemptingLogin.getUserId()));
                    addressBook.loadAddressBook(authenticator.getActiveUser().getUserId());
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
            User activeUser = authenticator.getActiveUser();
            if(activeUser != null){
                addressBook.saveAndClean(activeUser.getUserId());
            }
            auditDatabase.updateLog(AuditFactory.getAuditRecord(AuditRecordInterface.AuditType.LO, authenticator.getActiveUser().getUserId()));
            authenticator.logout();
            return false;
            }
        );
        //Change password
        commands.put("CHP", (args) -> {
            if(userModule.CHP(args.get(0), authenticator.getActiveUser().getUserId())){
                auditDatabase.updateLog(AuditFactory.getAuditRecord(AuditRecordInterface.AuditType.SPC, authenticator.getActiveUser().getUserId()));
            } else {
                auditDatabase.updateLog(AuditFactory.getAuditRecord(AuditRecordInterface.AuditType.FPC, authenticator.getActiveUser().getUserId()));
            }
            return false;
            }
        );
        //Add user
        commands.put("ADU", (args) -> {
            if(authenticator.getActiveUser().getUserId().equals("admin")){
                userModule.ADU(args.get(0));
                auditDatabase.updateLog(AuditFactory.getAuditRecord(AuditRecordInterface.AuditType.AU, authenticator.getActiveUser().getUserId()));
            } else {
                System.out.println("Admin not active");
            }
            return false;
            }
        );
        //Delete user
        commands.put("DEU", (args) -> {
                if(authenticator.getActiveUser().getUserId().equals("admin")){
                    userModule.DEU(args.get(0));
                    auditDatabase.updateLog(AuditFactory.getAuditRecord(AuditRecordInterface.AuditType.DU, authenticator.getActiveUser().getUserId()));
                } else {
                    System.out.println("Admin not active");
                }    
                return false;
            }
        );
        //Display audit log
        commands.put("DAL", (args) -> {
                if(args.size() == 1){
                    if(args.get(0).length() > 16){
                        System.out.println("Invalid userID");
                        return false;
                    }
					auditDatabase.getAuditLog(authenticator, args.get(0));
                } else if(args.size() == 0){
                    auditDatabase.getAuditLog(authenticator);
                }
                return false;
            }
        );
        //Add record
        commands.put("ADR", (args) -> {
                if(!authenticator.getActiveUser().getUserId().equals("admin")){
                    if(args.size() > 0){
                        addressBook.ADR(Record.createRecordsFromArgs(args));
                    } else {
                        System.out.println("No recordID");
                    }
                } else {
                    System.out.println("Admin not authorized");
                }
                return false;
            }
        );
        //Delete Record
        commands.put("DER", (args) -> {
                if(!authenticator.getActiveUser().getUserId().equals("admin")){
                    if(args.size() > 0){
                        addressBook.DER(args.get(0));
                    } else {
                        System.out.println("No recordID");
                    }
                } else {
                    System.out.println("Admin not authorized");
                }   
                return false;
            }
        );
        //Edit Record
        commands.put("EDR", (args) -> {
                if(!authenticator.getActiveUser().getUserId().equals("admin")){
                    if(args.size() > 0){
                        Record editedRecord = Record.createRecordsFromArgs(args);
                        if(editedRecord != null){
                            addressBook.EDR(editedRecord.getId(), editedRecord);
                        }
                    } else {
                        System.out.println("No recordID");
                    }
                } else {
                    System.out.println("Admin not authorized");
                }
                return false;
            }
        );
        //Read record
        commands.put("RER", (args) -> {
                if(!authenticator.getActiveUser().getUserId().equals("admin")){
                    if(args.size()==1){
                        //reading full record
                        Record r = addressBook.RER(args.get(0));
                        //Print record out
                        if(r!=null) {
                            System.out.println(r.toString());
                            System.out.println("OK");
                        }
                    } else if(args.size() > 1) {
                        //Read field of record
                        Record r = addressBook.RER(args.remove(0));
                        if(r!=null) {
                            String rec = r.toStringArgs(args);
                            if(rec != null){
                                System.out.println(rec);
                                System.out.println("OK");
                            }
                        }
                    } else {
                        System.out.println("No recordID");
                    }
                } else {
                    System.out.println("Admin not authorized");
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
                addressBook.exportAddressBook(args.get(0)); 
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
                //Log user out
                if(authenticator.getActiveUser() != null){
                    commands.get("LOU").apply(new ArrayList(Arrays.asList(authenticator.getActiveUser().getUserId())));
                }
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
        fullCommand = fullCommand.replace(command, "").trim();
        if(!fullCommand.isEmpty()){
            //Take id and put in args by itself
            args = new ArrayList(Arrays.asList(fullCommand.split("\\s+")));
            String idPlace = args.get(0);
            args = new ArrayList(Arrays.asList(idPlace));
            //if field values pairs, put in args properly
            String fieldValuePairs = fullCommand.replace(idPlace, "");
            if(!fieldValuePairs.isEmpty() && fieldValuePairs.contains("=")){
                String[] pairs = fieldValuePairs.split("=");
                String fieldName = null;
                for(int i = 0; i <pairs.length-1;i++){
                    if(fieldName == null){
                        String two = pairs[i+1].trim();
                        String [] spaceSplit = two.split(" ");
                        fieldName = spaceSplit[spaceSplit.length-1];
                        args.add(pairs[i].trim() + "=" + pairs[i+1].replace(fieldName, "").trim());
                    } else {
                        String two = pairs[i+1].trim();
                        String [] spaceSplit = two.split(" ");
                        String oldFieldName = fieldName;
                        fieldName = spaceSplit[spaceSplit.length-1];
                        if(i == pairs.length-2){
                            args.add(oldFieldName + "=" + pairs[i+1].trim());
                        } else {
                            args.add(oldFieldName + "=" + pairs[i+1].replace(fieldName, "").trim());
                        }
                    }
                }
            } else if(!fieldValuePairs.isEmpty()) {
                ArrayList<String> fields = new ArrayList(Arrays.asList(fieldValuePairs.trim().split("\\s+")));
                for(String f : fields){
                    args.add(f);
                }
            }
        }

        if(commands.containsKey(command)){
            //Try to execute command
            try{
                if(command.equals("LIN") || command.equals("EXT") || authenticator.isUserAuthen()){
                    return (Boolean)commands.get(command).apply(args);
                } else {
                    System.out.println("No active login session");
                    return false;
                }
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
