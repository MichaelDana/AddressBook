
import java.util.*;
import java.sql.*;

public class Authenticator{
    private class LoggedInUser{
       private User loggedInUser;
       private Time lastAuthenticatedAction;
	}
	    private static final float timeoutTime = 300000;//5 min
		private LoggedInUser loggedInUser;

		public Authenticator(){
			 loggedInUser = null;
		}
		public boolean isUserAuthen(){
			boolean answer = false;
			if(timeoutTime > ((System.currentTimeMillis())-lastAuthenticatedAction)
				return answer;
			else
				
				answer = true;
				return answer;
				lastAuthenticatedAction = System.currentTimeMillis();	
		}
		public login(User){
			loggedInUser = new LoggedInUser(user);
			lastAuthenticatedAction = System.currentTimeMillis();
		}

		public void logout(){
			User.loggedInUser = null;
		}  
}
