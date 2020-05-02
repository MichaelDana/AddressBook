
import java.util.*;
import java.util.HashMap;
import java.util.stream.*;

public class Record {
	private String mySurname;
	private String myGivenName;
	private String myId;
	private String myPersonalEmail;
	private String myWorkEmail;
	private String myPersonalPhone;
	private String myWorkPhone;
	private String myStreetAddress;
	private String myCity;
	private String myState;
	private String myCountry;
	private String myPostalCode;

	public Record(String surname, String givenName, String id, String personalEmail, String workEmail,
			String personalPhone, String workPhone, String streetAddress, String city, String state, String country,
			String postalCode) {

		mySurname = surname;
		myGivenName = givenName;
		myId = id;
		myPersonalEmail = personalEmail;
		myWorkEmail = workEmail;
		myPersonalPhone = personalPhone;
		myWorkPhone = workPhone;
		myStreetAddress = streetAddress;
		myCity = city;
		myState = state;
		myCountry = country;
		myPostalCode = postalCode;
	}

	public static Record createRecordsFromArgs(List<String> args) {
		String mySurname = "";
		String myGivenName = "";
		String myId = "";
		String myPersonalEmail = "";
		String myWorkEmail = "";
		String myPersonalPhone = "";
		String myWorkPhone = "";
		String myStreetAddress = "";
		String myCity = "";
		String myState = "";
		String myCountry = "";
		String myPostalCode = "";

		// parse args
		//must have id
		if(args != null && args.size() > 0){
			myId = args.remove(0);
		} else {
			return null;
		}
		for (String kvPair : args) {
			if(kvPair.indexOf('=') > 0){
				String field = kvPair.substring(0, kvPair.indexOf('='));
				String value = kvPair.substring(kvPair.indexOf('=') + 1);
				switch (field) {
				case "SN":
					mySurname = value;
					break;
				case "GN":
					myGivenName = value;
					break;
				case "PEM":
					myPersonalEmail = value;
					break;
				case "WEM":
					myWorkEmail = value;
					break;
				case "PPH":
					myPersonalPhone = value;
					break;
				case "WPH":
					myWorkPhone = value;
					break;
				case "SA":
					myStreetAddress = value;
					break;
				case "CITY":
					myCity = value;
					break;
				case "STP":
					myState = value;
					break;
				case "CTY":
					myCountry = value;
					break;
				case "PC":
					myPostalCode = value;
					break;
				default:
					// Invalid field
				}
			}
		}
		return new Record(mySurname, myGivenName, myId, myPersonalEmail, myWorkEmail, myPersonalPhone, myWorkPhone,
				myStreetAddress, myCity, myState, myCountry, myPostalCode);
	}

	public String getSurname() {
		return this.mySurname;
	}

	public void setSurname(String mySurname) {
		this.mySurname = mySurname;
	}

	public String getGivenName() {
		return this.myGivenName;
	}

	public void setGivenName(String myGivenName) {
		this.myGivenName = myGivenName;
	}

	public String getId() {
		return this.myId;
	}

	public void setId(String myId) {
		this.myId = myId;
	}

	public String getPersonalEmail() {
		return this.myPersonalEmail;
	}

	public void setPersonalEmail(String myPersonalEmail) {
		this.myPersonalEmail = myPersonalEmail;
	}

	public String getWorkEmail() {
		return this.myWorkEmail;
	}

	public void setWorkEmail(String myWorkEmail) {
		this.myWorkEmail = myWorkEmail;
	}

	public String getPersonalPhone() {
		return this.myPersonalPhone;
	}

	public void setPersonalPhone(String myPersonalPhone) {
		this.myPersonalPhone = myPersonalPhone;
	}

	public String getWorkPhone() {
		return this.myWorkPhone;
	}

	public void setWorkPhone(String myWorkPhone) {
		this.myWorkPhone = myWorkPhone;
	}

	public String getStreetAddress() {
		return this.myStreetAddress;
	}

	public void setStreetAddress(String myStreetAddress) {
		this.myStreetAddress = myStreetAddress;
	}

	public String getCity() {
		return this.myCity;
	}

	public void setCity(String myCity) {
		this.myCity = myCity;
	}

	public String getState() {
		return this.myState;
	}

	public void setState(String myState) {
		this.myState = myState;
	}

	public String getCountry() {
		return this.myCountry;
	}

	public void setCountry(String myCountry) {
		this.myCountry = myCountry;
	}

	public String getPostalCode() {
		return this.myPostalCode;
	}

	public void setPostalCode(String myPostalCode) {
		this.myPostalCode = myPostalCode;
	}

	public String toString() {
		String recordStr = "----------------------\n" + "Name: " + this.myGivenName + " " + this.mySurname + "\n"
				+ "Personal Email: " + this.myPersonalEmail + "\n" + "Work Email: " + this.myWorkEmail + "\n"
				+ "Personal Phone: " + this.myPersonalPhone + "\n" + "Work Phone: " + this.myWorkPhone + "\n"
				+ "Address: " + this.myStreetAddress + "\n" + "City: " + this.myCity + "\n" + "State/Province: "
				+ this.myState + "\n" + "Country: " + this.myCountry + "\n" + "Postal Code: " + this.myPostalCode + "\n"
				+ "----------------------";
		return recordStr;
	}

	public String toDbString() {
		return myId + ";" + mySurname + ";" + myGivenName + ";" + myPersonalEmail + ";" + myWorkEmail + ";"
				+ myPersonalPhone + ";" + myWorkPhone + ";" + myStreetAddress + ";" + myCity + ";" + myState + ";"
				+ myCountry + ";" + myPostalCode + ";";
	}

	public static ArrayList<String> fromDbStringToArgsList(String dbString){
		ArrayList<String> fields = new ArrayList(Arrays.asList(dbString.replaceAll(";"," ;").split(";")));
		for(int i = 0; i < fields.size(); i++){
			fields.set(i, fields.get(i).trim());
		}
		//Annotate args
		Map<Integer, String> argAnnotations = Stream.of(new Object[][] { 
			{ 1, "SN" }, 
			{ 2, "GN" }, 
			{ 3, "PEM" }, 
			{ 4, "WEM" }, 
			{ 5, "PPH" }, 
			{ 6, "WPH" }, 
			{ 7, "SA" }, 
			{ 8, "CITY" }, 
			{ 9, "STP" }, 
			{ 10, "CTY" }, 
			{ 11, "PC" }, 
		}).collect(Collectors.toMap(data -> (Integer) data[0], data -> (String) data[1]));
		String field;
		if(fields.size() >1){
			for(int i = 1; i < 12; i++){
				String annotation = argAnnotations.get(i) + "=";
				fields.set(i, !(field = fields.get(i)).isEmpty() ? annotation+field : "");
			}
		}
		return fields;
	}
}