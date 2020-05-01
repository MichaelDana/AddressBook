//package audit;

//import AddressBook.Writer;
//import AddressBook.RecordInterface;
import java.io.*;
//import audit.AuditDatabaseInterface;

class AuditWriter implements Writer 
{
    public void writeToDisk(String auditFile, 
							RecordInterface newRecord,
							AuditDatabaseInterface database) 
							throws IOException
	{
		ObjectOutputStream out = new ObjectOutputStream(
									new FileOutputStream(auditFile));
		out.writeInt(database.size())
		out.writeObject(newRecord);
		out.close();
    }
}
