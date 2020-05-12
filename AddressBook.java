import java.util.*;
import java.io.*;
public class AddressBook{
	private static final int MAX_NUMBER_OF_RECORDS = 256;
	private static final String addressBookModuleKey = "userAddrsBookKey";
	private HashMap<String,Record> records;
	private int numberOfRecords;

	public AddressBook()
	{   
		records = new HashMap<>(MAX_NUMBER_OF_RECORDS);
		numberOfRecords = 0;
		ArrayList<File> addressBookFiles = new ArrayList(Arrays.asList(new File("./address_book").listFiles()));
		File addressBookFileDir = null;
		for(File f:addressBookFiles){
				if(f.getName().equals("addr")){
					addressBookFileDir = f;
						break;
				}
		}
		if(addressBookFileDir == null){
			new File("./address_book/addr").mkdir();
		}
	}

	public void loadAddressBook(String userID){
		List<Record> addRecords = null;
		try {
			AddressBookWriter writer = new AddressBookWriter();
			addRecords = writer.loadAddressBook("./address_book/addr/" + userID + "_addr", addressBookModuleKey);
			for(Record r : addRecords){
				records.put(r.getId(), r);
				numberOfRecords++;
			}
		} catch (Exception e) {
			System.out.println("Error loading address book.");
		}
	}

	public void saveAndClean(String userID){
		try {
			new AddressBookWriter().saveAddressBook("./address_book/addr/" + userID + "_addr", addressBookModuleKey, records);
			records = new HashMap<>(MAX_NUMBER_OF_RECORDS);
		} catch (Exception e) {
			System.out.println("Error saving address book.");
		}
	}

	public void ADR(Record record) throws RuntimeException
	{   
		if(numberOfRecords < MAX_NUMBER_OF_RECORDS && record != null)
        {	
			//Validate id
			if(!validId(record.getId())){
				System.out.println("Invalid recordID");
				return;
			}
			//Check if duplicate
			if(records.get(record.getId()) != null){
				System.out.println("Duplicate recordID");
				return;
			}
			records.put(record.getId(), record);
			numberOfRecords++;
			System.out.println("OK");
		}
		// else if(record == null)
			// System.out.print("Failed to add record");
			
        else if(record != null){
			System.out.print("Number of records exceeds maximum");
		}
	}

	private boolean validId(String id){
		if(id.length() > 16 || !id.matches("^([A-Za-z]|[0-9])*$")){
			return false;
		}
		return true;
	}

	public void DER(String recID) throws RuntimeException
	{
		// if(records.isEmpty())	
			// throw new RuntimeException("No more records to delete");
		// else
		if(!validId(recID)){
			System.out.println("Invalid recordID");
			return;
		}
		if(records.get(recID) != null){
			records.remove(recID);
			numberOfRecords--;
			System.out.println("OK");
		} else {
			System.out.println("RecordID not found");
		}
	}
	public Record RER(String recID)
	{
			if(!validId(recID)){
				System.out.println("Invalid recordID");
				return null;
			}
			if(records.get(recID) == null){
				System.out.println("RecordID not found");
				return null;
			}
			return records.get(recID);
	}
	public void EDR(String recID, Record updatedRecord)
	{
		if(!validId(recID)){
			System.out.println("Invalid recordID");
			return;
		}
		if(updatedRecord != null){
			if(records.get(recID) != null){
				Record r = records.get(recID);
				if(!updatedRecord.getSurname().isEmpty()){
					r.setSurname(updatedRecord.getSurname());
				}
				if(!updatedRecord.getGivenName().isEmpty()){
					r.setGivenName(updatedRecord.getGivenName());
				}
				if(!updatedRecord.getPersonalEmail().isEmpty()){
					r.setPersonalEmail(updatedRecord.getPersonalEmail());
				}
				if(!updatedRecord.getWorkEmail().isEmpty()){
					r.setWorkEmail(updatedRecord.getWorkEmail());
				}
				if(!updatedRecord.getPersonalPhone().isEmpty()){
					r.setPersonalPhone(updatedRecord.getPersonalPhone());
				}
				if(!updatedRecord.getWorkPhone().isEmpty()){
					r.setWorkPhone(updatedRecord.getWorkPhone());
				}
				if(!updatedRecord.getStreetAddress().isEmpty()){
					r.setStreetAddress(updatedRecord.getStreetAddress());
				}
				if(!updatedRecord.getCity().isEmpty()){
					r.setCity(updatedRecord.getCity());
				}
				if(!updatedRecord.getState().isEmpty()){
					r.setState(updatedRecord.getState());
				}
				if(!updatedRecord.getCountry().isEmpty()){
					r.setCountry(updatedRecord.getCountry());
				}
				if(!updatedRecord.getPostalCode().isEmpty()){
					r.setPostalCode(updatedRecord.getPostalCode());
				}
				System.out.println("OK");
			} else {
				System.out.println("RecordID not found");
			}
		}
	}

	public int getNumberOfRecords(){
		return this.numberOfRecords;
	}

	public void exportAddressBook(String fileName) throws IOException{
		//fix address book file
		new AddressBookWriter().writeToDisk(fileName, numberOfRecords, records.entrySet().iterator());
	}

	public void importAddressBook(String fileName) throws IOException{
		AddressBookWriter adw = new AddressBookWriter();
		List<Record> newRecords = adw.readFromDisk(fileName);
		for(Record newRecord:newRecords){
			ADR(newRecord);
		}
	}

}