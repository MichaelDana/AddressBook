interface AuditRecordInterface extends RecordInterface
{
    enum AuditType 
	{	LF, LS, L1, LO, SPC, FPC, AU, DU
    }
	public void display();
	public String getKey();
}
