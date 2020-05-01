import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.io.*;

public class AddressBookWriter implements Writer{
	public void writeToDisk(String addressBookFile, 
							int numRecords,
							Iterator addressBookIterator) 
							throws IOException
	{
		// ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(addressBookFile));
		// out.writeInt(numRecords);
		// while(addressBookIterator.hasNext())
		// 	out.writeObject(addressBookIterator.next());

  	 	FileWriter writer = new FileWriter(addressBookFile,true);
		while(addressBookIterator.hasNext()){
			Map.Entry addressBookMapEntry = ((Map.Entry)addressBookIterator.next());
			writer.write(((Record)addressBookMapEntry.getValue()).toDbString() + "\n");
		}
		writer.close();
	}
	
	public List<Record> readFromDisk(String addressBookFile){
		List<Record> newRecords = null;
		try{
			FileReader reader = new FileReader(addressBookFile);
            BufferedReader in = new BufferedReader(reader);
			newRecords =  new ArrayList<>();
			String dbString;
            while ((dbString = in.readLine()) != null) {
                newRecords.add(Record.createRecordsFromArgs(Record.fromDbStringToArgsList(dbString)));
            }
            reader.close();
		} catch (Exception e){
			System.out.println(e);
			System.out.println(e.getStackTrace());
			return null;
		}

		return newRecords;
	}
}