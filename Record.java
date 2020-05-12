
import java.util.*;
import java.util.HashMap;
import java.util.stream.*;

public class Record {
	private static final int MAX_DATA_FIELD_SIZE = 64;
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
			if(myId.contains("=")){
				System.out.println("No recordID");
				return null;
			}
		} else {
			return null;
		}
		for (String kvPair : args) {
			if(kvPair.indexOf('=') > 0){
				String field = kvPair.substring(0, kvPair.indexOf('='));
				String value = kvPair.substring(kvPair.indexOf('=') + 1);
				if(!validValue(value)){
					System.out.println("One or more invalid data fields");
					return null;
				}
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
					System.out.println("One or more invalid record data fields");
					return null;
				}
			}
		}
		return new Record(mySurname, myGivenName, myId, myPersonalEmail, myWorkEmail, myPersonalPhone, myWorkPhone,
				myStreetAddress, myCity, myState, myCountry, myPostalCode);
	}

	private static boolean validValue(String value){
		int count = 0;
		for(char c: value.toCharArray()){
			if(count > MAX_DATA_FIELD_SIZE){
				//field too large
				return false;
			}
			if((int)c > 126 || (int)c < 32){
				//not a readable character
				return false;
			}
			count++;
		}
		return true;
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
		String recordStr = this.myId + " SN=" + this.mySurname + " PEM=" + this.myPersonalEmail + " WEM=" + this.myWorkEmail + " PPH=" + this.myPersonalPhone +" WPH=" + this.myWorkPhone + " SA=" + this.myStreetAddress + " CITY=" + this.myCity + " STP="
				+ this.myState + " CTY=" + this.myCountry +" PC=" + this.myPostalCode;
		return recordStr;
	}

	public String toStringArgs(List<String> args){
		String recordStr = myId + " ";
		for (String field : args) {	
				switch (field) {
				case "SN":	
					recordStr += "SN="+this.mySurname + " ";
					break;
				case "GN":
					recordStr += "GN="+this.myGivenName + " ";
					break;
				case "PEM":
					recordStr += "PEM=" +this.myPersonalEmail + " ";
					break;
				case "WEM":
					recordStr += "WEM" +this.myWorkEmail + " ";
					break;
				case "PPH":
					recordStr += "PPH="+ this.myPersonalPhone + " ";
					break;
				case "WPH":
					recordStr += "WPH="+ this.myWorkPhone + " ";
					break;
				case "SA":
					recordStr += "SA="+this.myStreetAddress + " ";
					break;
				case "CITY":
					recordStr += "CITY=" +this.myCity + " ";
					break;
				case "STP":
					recordStr += "STP=" +this.myState + " ";
					break;
				case "CTY":
					recordStr += "CTY="+this.myCountry + " ";
					break;
				case "PC":
					recordStr += "PC="+this.myPostalCode + " ";
					break;
				default:
					// Invalid field
					System.out.println("One or more invalid record data fields");
					return null;
				}
			}
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