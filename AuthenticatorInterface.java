interface AuthenticatorInterface
{
	boolean isUserAuthen();
	boolean login(User user);
	void logout();
	User getActiveUser(); 
}
