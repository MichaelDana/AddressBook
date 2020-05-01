import java.time.LocalTime;
import java.time.LocalDate;
//import audit.AuditRecordInterface;

class AuditRecord implements AuditRecordInterface
{
    private LocalDate mDate;
    private LocalTime mTime;
    private AuditType mType;
    private String mUserID;
	private String mCommand;

    public AuditRecord(AuditRecord other) {
        mDate = other.mDate;
        mTime = other.mTime;
        mType = other.mType;
        mUserID = other.mUserID;
		mCommand = other.mCommand;
    }
	public AuditRecord(AuditType type, String command, String userID)
	{
		mDate = LocalDate.now();
		mTime = LocalTime.now();
		mType = type;
		mUserID = userID;
		mCommand = command;
	}
	public void display()
	{	System.out.println(mDate+" "+mTime+" "+mType+" "+mCommand+" "+mUserID);	
	}
	public String getKey()
	{	return mUserID;
	}
}
