import java.util.HashMap;
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

}