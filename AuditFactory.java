public class AuditFactory
{
	public static AuditRecordInterface getAuditRecord(AuditRecordInterface.AuditType type, 
													String userID)
	{	return new AuditRecord(type, userID);
	}
	public static AuditDatabaseInterface getAuditDatabase(String auditFile)
	{	return new AuditDatabase(auditFile);
	}
	public static AuditDatabaseInterface getAuditDatabase()
	{	return new AuditDatabase();
	}
}
