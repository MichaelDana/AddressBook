import java.util.Arrays;
import java.util.HashMap;
import java.util.*;
import java.io.File;

public class UserModule {
        private static final ArrayList<String> commonPasswords = new ArrayList(Arrays.asList("12345", "123456",
                        "123456789", "test1", "password", "12345678", "zinch", "g_czechout", "asdf", "qwerty",
                        "1234567890", "1234567", "Aa123456.", "iloveyou", "1234", "abc123", "111111", "123123",
                        "dubsmash", "test", "princess", "qwertyuiop", "sunshine", "BvtTest123", "11111", "ashley",
                        "00000", "000000", "password1", "monkey", "livetest", "55555", "soccer", "charlie", "asdfghjkl",
                        "654321", "family", "michael", "123321", "football", "baseball", "q1w2e3r4t5y6", "nicole",
                        "jessica", "purple", "shadow", "hannah", "chocolate", "michelle", "daniel", "maggie",
                        "qwerty123", "hello", "112233", "jordan", "tigger", "666666", "987654321", "superman",
                        "12345678910", "summer", "1q2w3e4r5t", "fitness", "bailey", "zxcvbnm", "fuckyou", "121212",
                        "buster", "butterfly", "dragon", "jennifer", "amanda", "justin", "cookie", "basketball",
                        "shopping", "pepper", "joshua", "hunter", "ginger", "matthew", "abcd1234", "taylor", "samantha",
                        "whatever", "andrew", "1qaz2wsx3edc", "thomas", "jasmine", "animoto", "madison", "0987654321",
                        "54321", "flower", "Password", "maria", "babygirl", "lovely", "sophie", "Chegg123"

        ));
        private static final int MAX_USERS = 7;
        private HashMap<String, User> users;
        private Authenticator authenticator;
        private static final String userModuleKey = "userModAddrssKey";
        private Scanner userIn;

        public UserModule(Authenticator authenticator, Scanner scanner) {
                userIn = scanner;
                this.authenticator = authenticator;
                users = new HashMap<>(MAX_USERS);
                // Begin setup
                // ArrayList<File> existingFiles = new ArrayList(Arrays.asList(new
                // File("./").listFiles()));
                File usersDir = null;
                File usersFile = null;
                String usersFileName = "users";
                // Search for users files
                ArrayList<File> addressBookFiles = new ArrayList(Arrays.asList(new File("./address_book").listFiles()));
                for (File f : addressBookFiles) {
                        if (f.getName().equals("users")) {
                                usersDir = f;
                                ArrayList<File> usersDirFiles = new ArrayList(
                                                Arrays.asList(new File("./address_book/users").listFiles()));
                                for (File uf : usersDirFiles) {
                                        if (uf.getName().equals(usersFileName)) {
                                                usersFile = uf;
                                                break;
                                        }
                                }
                                break;
                        }
                }
                if (usersDir == null) {
                        new File("./address_book/users").mkdir();
                }
                // Try to load users
                if (usersFile != null) {
                        loadUsers("./address_book/users/users");
                } else {
                        // No users file, only admin
                        ADU("admin");
                }
        }

        private void loadUsers(String fileName) {
                UserWriter writer = new UserWriter();
                List<User> loadedUsers = null;
                try {
                        loadedUsers = writer.readFromDisk(fileName, UserModule.userModuleKey);
                } catch (Exception e) {
                        System.out.println("Failed to initialize users");
                }
                for (User user : loadedUsers) {
                        users.put(user.getUserId(), user);
                }
        }

        public void save() {
                try {
                        new UserWriter().writeToDisk("./address_book/users/users", UserModule.userModuleKey,
                                        this.users);
                } catch (Exception e) {
                        System.out.println("Failed to save users");
                }
        }

        public void ADU(String userId) throws RuntimeException {
                // verify user id
                if (!validId(userId)) {
                        System.out.println("Invalid userID");
                        return;
                }
                if (users.size() <= MAX_USERS) {
                        if (!users.containsKey(userId)) {
                                users.put(userId, new User(userId));
                                System.out.println("OK");
                        } else {
                                System.out.println("Account already exists");
                        }
                }

        }

        private boolean validId(String id) {
                if (id.length() > 16 || !id.matches("^([A-Za-z]|[0-9])*$")) {
                        return false;
                }
                return true;
        }

        public void DEU(String UserId) throws RuntimeException {
                // verify user id
                if (!validId(UserId)) {
                        System.out.println("Invalid userID");
                        return;
                }
                if (!users.containsKey(UserId)) {
                        System.out.println("Account does not exist");
                } else {
                        users.remove(UserId);
                        System.out.println("OK");
                }
        }

        public boolean CHP(String password, String userId) throws RuntimeException {
                if (!password.isEmpty() && !authenticator.getActiveUser().getPassword().equals(password)) {
                        System.out.println("Invalid credentials");
                } else {
                        // Change password
                        System.out.println(
                                        "Create a new password. Passwords may contain up to 24 upper-or lower-case letters or numbers. Choose an uncommon password that would be difficult to guess.");
                        // Scanner userIn = new Scanner(System.in);
                        String newPassword = userIn.nextLine();
                        System.out.print("Reenter the same password:");
                        String newPasswordDuplicate = userIn.nextLine();
                        System.out.print("");
                        if (!newPassword.equals(newPasswordDuplicate)) {
                                System.out.println("Passwords do not match");
                        } else {
                                // Validate password
                                if (verifyPassword(newPassword)) {
                                        if (!tooEasy(newPassword)) {
                                                users.get(userId).setPassWord(newPassword);
                                                System.out.println("OK");
                                                return true;
                                        } else {
                                                System.out.println("Password is too easy to guess");
                                        }
                                } else {
                                        System.out.println("Password contains illegal characters");
                                }
                        }
                }
                return false;
        }

        public Boolean IUN(String UserID) {
                return users.get(UserID).getPassword().isEmpty();
        }

        public User getUser(String userID) {
                return users.get(userID);
        }

        private boolean verifyPassword(String password) {
                return (password.matches("[a-zA-Z0-9]*") && password.length() < 25);
        }

        private boolean tooEasy(String password){
                return commonPasswords.contains(password);
        }
}
