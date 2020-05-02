interface AuthenticatorInterface
{
	boolean isUserAuthenticated();
	boolean login(User user);
	void logout();
	User getActiveUser(); 
}
