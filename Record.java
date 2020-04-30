

import java.util.*;
import java.util.HashMap;


public class Record
{
	protected final String surname;  
	protected final String givenName;
	protected final String id;
	protected final String personalEmail;
	protected final String workEmail;
	protected final String personalPhone;
	protected final String workPhone;
	protected final String streetAddress;
	protected final String city;
	protected final String state;
	protected final String country;
	protected final String postalCode;

		
 	  public Record(String surname, String givenName, String id, String personalEmail, String workEmail, String personalPhone, String workPhone, String streetAddress, String city, String state, String country, String postal code){
	
			mySurname= surname;
			myGivenName= givenName;
			myId = id;
			myPersonalEmail = personalEmail;
			myworkEmail = workEmail;
			myPersonalPhone = personalPhone;
			myWorkPhone = workPhone;
			myStreetAddress = streetAddress;
			myCity = city;
			myState = state;
			myCountry = country;
			myPostalCode = postalCode;
}
