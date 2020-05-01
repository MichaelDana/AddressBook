public class User{
        private static final int MAX_NUMBER_OF_USERS = 256;
        private final HashMap<String,User> users;
        private int numberOfUsers;

        public class User
	{
	private String myUserId;  
	private String myPassword;
		
	public User(String userId, String password){
  
		myUserId= userId;
		myPassword= password;
	}

	public static User createUserFromArgs(List<String> args){
		String myUserId = "";  
		String myPassword = "";

		//parse args
		myUserId = args.remove(0);
			
		}
        return new User(userId, password);
	}
	
        public void ADU(String UserId) throws RuntimeException
        {

                if(numberOfUsers < MAX_NUMBER_OF_USERS)
                {

                        users.put(User.myId, user);
                        numberOfUsers++;
                        System.out.println("USER ADDED");
                }
                else
                        throw new RuntimeException("No more space for new users.");
        }

        public void DEU(String UserId) throws RuntimeException
        {
                if(User.get(UserId)
                        throw new RuntimeException("No user matching ID to delete");
                else
                        Users.remove(UserId);
        }

        public void CHP(String password) throws RuntimeException
        {
                if(this.get(myUserId)!= (null || "") ) 
        }

        public Boolean IUN(String UserID)
        {
                if (this.get(password) == "") return true;
                else return false;

        }
}
