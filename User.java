import java.util.*;
import java.util.HashMap;


public class User{
	private String myUserId;
	private String myPassword;

	public User(String userId){
		this(userId, ""));
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
	
	public String getUserId() {
		return this.myUserId;
	}

	public void setUserId(String UserId) {
		this.myUserId = UserId;
	}	

	public String getPassword() {
		return this.myPassword;
	}

}
