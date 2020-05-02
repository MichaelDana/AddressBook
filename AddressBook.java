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
		if(numberOfRecords < MAX_NUMBER_OF_RECORDS)
        {	
			records.put(record.getId(), record);
			numberOfRecords++;
			System.out.println("RECORD ADDED");
		}
		else if(record == null)
			System.out.print("Failed to add record");
        else
	       throw new RuntimeException("No more space for new records.");
	}
	public void DER(String recID) throws RuntimeException
	{
		if(records.isEmpty())	
			throw new RuntimeException("No more records to delete");
		else
			records.remove(recID);
			numberOfRecords--;
	}
	public Record RER(String recID)
	{
			return records.get(recID);
	}
	public void EDR(String recID)
	{
		// records.put(Record.myId, record);

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