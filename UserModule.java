public class UserModule{
        
        public void ADU(String UserId) throws RuntimeException
        {

		{			
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
