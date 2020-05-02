import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.io.*;
public class AddressBook{
	private static final int MAX_NUMBER_OF_RECORDS = 256;
	private final HashMap<String,Record> records;
	private int numberOfRecords;

	public AddressBook()
	{   
		records = new HashMap<>(256);
		numberOfRecords = 0;
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

	public void exportAddressBook() throws IOException{
		//fix address book file
		new AddressBookWriter().writeToDisk("user_address_book", numberOfRecords, records.entrySet().iterator());
	}

	public void importAddressBook(String fileName) throws IOException{
		AddressBookWriter adw = new AddressBookWriter();
		List<Record> newRecords = adw.readFromDisk(fileName);
		for(Record newRecord:newRecords){
			ADR(newRecord);
		}
	}

}