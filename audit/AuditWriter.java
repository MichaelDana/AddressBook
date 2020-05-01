//import AddressBook.Writer;
//import AddressBook.RecordInterface;
import java.io.*;
//import audit.AuditDatabaseInterface;

interface AuditWriterInterface extends Writer 
{
	public void writeToDisk

    public void writeToDisk(String auditFile, 
							RecordInterface newRecord) 
							throws IOException
	{
		ObjectOutputStream out = new ObjectOutputStream(
									new FileOutputStream(auditFile));
		out.writeInt(database.size())
		out.writeObject(newRecord);
		out.close();
    }
}
