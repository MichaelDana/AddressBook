
import java.util.*;

public class Authenticator implements AuthenticatorInterface{
	private static final float timeoutTime = 300000;//5 min
	private Scanner userIn;
	private class LoggedInUser {
		private User loggedInUser;
		private long lastAuthenticatedAction;
		public LoggedInUser(User loggedInUser){
			this.loggedInUser = loggedInUser;
			lastAuthenticatedAction = System.currentTimeMillis();
		}

		public User getLoggedInUser(){
			return this.loggedInUser;
		}

		public long getLastAuthenticatedAction(){
			return this.lastAuthenticatedAction;
		}

		public void setLastAuthenticatedAction(long lastAuthenticatedAction){
			this.lastAuthenticatedAction = lastAuthenticatedAction;
		}
	}
	private LoggedInUser loggedInUser;

	public Authenticator(Scanner scanner){
		userIn = scanner;
		this.loggedInUser = null;
	}
	public boolean isUserAuthen(){
		boolean answer = false;
		if(loggedInUser == null || timeoutTime < ((System.currentTimeMillis())-loggedInUser.getLastAuthenticatedAction()))
			return answer;
		else
			answer = true;
			loggedInUser.setLastAuthenticatedAction(System.currentTimeMillis());
			return answer;
	}
	public boolean login(User user){
		if(loggedInUser == null){
			if(user == null){
				// dont login, user does not exist
				System.out.println("Invalid credentials");
			} else {
				//try to login
				if(!user.getPassword().isEmpty()){
					// Scanner userIn = new Scanner(System.in);
					System.out.print("Password: ");
					String password = userIn.nextLine();
					System.out.print("");
					if(password.equals(user.getPassword())){
						//Valid password login success
						System.out.println("OK");
						loggedInUser = new LoggedInUser(user);
						loggedInUser.setLastAuthenticatedAction(System.currentTimeMillis());
						return true;
					} else {
						System.out.println("Invalid credentials");
					}
				}
			}
			loggedInUser = null;
		} else {
			System.out.println("An account is currently active; logout before proceeding");
		}
		return false;
	}

	public void authenticateUser(User user){
		loggedInUser = new LoggedInUser(user);
		loggedInUser.setLastAuthenticatedAction(System.currentTimeMillis());
	}

	public void logout(){
		this.loggedInUser = null;
		System.out.println("OK");
	}

	public User getActiveUser(){
		if (this.loggedInUser == null){
			return null;
		} else {
			return this.loggedInUser.getLoggedInUser();	
		}
	}
}
