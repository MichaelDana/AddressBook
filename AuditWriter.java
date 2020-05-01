import writer.Writer;
import java.io.*;

class AuditWriter implements Writer 
{
    public void writeToDisk(String auditFile, 
							AuditRecordInterface newRecord) 
							throws IOException
	{
		ObjectOutputStream out = new ObjectOutputStream(
									new FileOutputStream(auditFile));
		out.writeObject(newRecord);
		out.close();
    }
}
