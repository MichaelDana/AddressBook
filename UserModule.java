import java.util.Arrays;
import java.util.HashMap;
import java.util.*;
import java.io.File;

public class UserModule{
        private static final int MAX_USERS = 7;
        private HashMap<String,User> users;
        private Authenticator authenticator;
        private static final String userModuleKey = "userModAddrssKey";

        public UserModule (Authenticator authenticator){
                this.authenticator = authenticator;
                users = new HashMap<>(MAX_USERS);
                //Begin setup
                ArrayList<File> existingFiles = new ArrayList(Arrays.asList(new File("./").listFiles()));
                File addressBookFile = null;
                File usersFile = null;
                //Search for users files
                ArrayList<File> addressBookFiles = new ArrayList(Arrays.asList(new File("./address_book").listFiles()));
                for(File f:addressBookFiles){
                        if(f.getName().equals("users")){
                                usersFile = f;
                                break;
                        }
                }
                if(usersFile == null){
                        new File("./address_book/users").mkdir();
                }
                //Try to load users
                if(addressBookFile != null){
                        loadUsers("./address_book/users/users");
                }
        }

        private void loadUsers(String fileName){
                UserWriter writer = new UserWriter();
                List<User> loadedUsers = null;
                try {
                        loadedUsers= writer.readFromDisk(fileName, UserModule.userModuleKey);
                } catch (Exception e){
                        System.out.println("Failed to initialize users");
                }
                for(User user : loadedUsers){
                        users.put(user.getUserId(), user);
                }
        }

        public void save(){
                try{
                        new UserWriter().writeToDisk("./address_book/users/users", UserModule.userModuleKey, this.users);
                } catch (Exception e){
                        System.out.println("Failed to save users");
                }
        }
        
        public void ADU(String userId) throws RuntimeException
        {
                //verify user id

                if(users.size() <= MAX_USERS){
                        if(!users.containsKey(userId)){
                                users.put(userId, new User(userId));
                                System.out.println("OK");
                        } else {
                                System.out.println("Account already exists");
                        }
                }
                        
        }

        public void DEU(String UserId) throws RuntimeException
        {
                //verify user id

                if(!users.get(UserID)){
                        System.out.println("Account does not exist");
                } else{
                        users.remove(UserId);
                        System.out.println("OK");
                }
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
