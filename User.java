import java.util.*;
import java.util.HashMap;


public class User{
	private String myUserId;
	private String myPassword;

	public User(String userId){
		this(userId, "");
	}

	public User(String userId, String password){

		myUserId= userId;
		myPassword= password;
	}

	public static User createUserFromArgs(String UserId){

		String myUserId = "";
		String myPassword = "";

		//parse args
		return new User(userId, password);
	}

	public static User createUserFromDBString(String dbString){
		String [] fields = dbString.replaceAll(";"," ;").split(";");
		return new User(fields[0].trim(), fields[1].trim());
	}
	
	public String getUserId() {
		return this.myUserId;
	}

	public void setUserId(String UserId) {
		this.myUserId = UserId;
	}	

	public String getPassword() {
		return this.myPassword;
	}

	public String toString(){
		return this.myUserId+";"+this.myPassword;
	}

}
