public class AuditFactory
{
	public AuditRecordInterface getAuditRecord(AuditType type, String userID,)
	{	return new AuditRecord(type, userID);
	}
	public AuditDatabaseInterface getAuditDatabase()
	{	return new AuditDatabase()
	}
	public get
}
