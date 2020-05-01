public class UserModule{
        private final HashMap<String,User> users;
        private int numberOfUsers;


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
                        Users.remove(id);
        }

        public void CHP(String UserId) throws RuntimeException
        {
                if(User.get(UserId))
        }

        public Boolean IUN(String recID)
        {
                if (User.get(UserId)).flag == 0 return true;
                else return false;

        }
}
