import java.time.LocalTime;
import java.time.LocalDate;

class AuditRecord implements AuditRecordInterface
{
    private LocalDate mDate;
    private LocalTime mTime;
    private AuditType mType;
    private String mUserID;

    public AuditRecord(AuditRecord other) {
        mDate = other.mDate;
        mTime = other.mTime;
        mType = other.mType;
        mUserID = other.mUserID;
    }
	public AuditRecord(AuditType type, String userID)
	{
		mDate = LocalDate.now();
		mTime = LocalTime.now();
		mType = type;
		mUserID = userID;
	}
	public void display()
	{	System.out.println(mDate+" "+mTime+" "+mType+" "+mUserID);	
	}
	public String getKey()
	{	return mUserID;
	}
}
