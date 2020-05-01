
public class AuditFactory
{
	public static AuditRecordInterface getAuditRecord(AuditType type, 
													String command, 
													String userID)
	{	return new AuditRecord(type, command, userID);
	}
	public static AuditDatabaseInterface getAuditDatabase(String auditFile)
	{	return new AuditDatabase(auditFile);
	}
	public static AuditDatabaseInterface getAuditDatabase()
	{	return new AuditDatabase();
	}
}
