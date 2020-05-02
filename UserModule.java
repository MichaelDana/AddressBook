import java.util.Arrays;
import java.util.HashMap;
import java.util.*;
import java.io.File;

public class UserModule{
        private static final int MAX_USERS = 7;
        private HashMap<String,User> users;
        private Authenticator authenticator;

        public UserModule (Authenticator authenticator){
                this.authenticator = authenticator;
                users = new HashMap<>(MAX_USERS);
                //Begin setup
                ArrayList<File> existingFiles = new ArrayList(Arrays.asList(new File("./").listFiles()));
                File addressBookFile = null;
                //Search for addressbook files
                for(File file: existingFiles){
                        if(file.getName().equals("address_book")){
                                addressBookFile = file;
                        }
                }
                //Try to load users
                loadUsers("./address_book/users");
        }

        private void loadUsers(String fileName){
                UserWriter writer = new UserWriter();
                // List<User> users = writer.readFromDisk(fileName);
        }
        
        public void ADU(String userId) throws RuntimeException
        {

                if(users.size() <= MAX_USERS){
                        users.put(userId, new User(userId));
                        System.out.println("USER ADDED");
                }
                else
                        throw new RuntimeException("No more space for new users.");
        }

        public void DEU(String UserId) throws RuntimeException
        {
                if(!users.get(UserID))
                        throw new RuntimeException("No user matching ID to delete");
                else
                        users.remove(UserId);
        }

        public void CHP(String password) throws RuntimeException
        {
                if(!authenticator.getActiveUser().getPassword().equals(password)){
                        System.out.println("Failed to change password");
                } else {
                        //Change password
                        System.out.println("Create a new password. Passwords may contain up to 24 upper-or lower-case letters or numbers. Choose an uncommon password that would be difficult to guess.");
                        Scanner userIn = new Scanner(System.in);
                        String newPassword = userIn.nextLine();
                        System.out.print("Reenter the same password:");
                        String newPasswordDuplicate = userIn.nextLine();
                        if(!newPassword.equals(newPasswordDuplicate)){
                                System.out.println("Passwords do not match");
                        } else {
                                //Validate password
                                if(verifyPassword(newPassword)){
                                        System.out.println("OK");
                                }
                        }
                }
        }

        public Boolean IUN(String UserID)
        {
                return users.get(UserID).getPassword().isEmpty();
        }

        public User getUser(String userID){
                return users.get(userID);
        }

        private boolean verifyPassword(String password){
                return true;
        }
}
