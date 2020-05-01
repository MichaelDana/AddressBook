package audit;

import java.util.LocalTime;
import java.util.LocalDate;

class AuditRecord implements AuditRecordInterface{
    private LocalDate mDate;
    private LocalTime mTime;
    private RecordType mType;
    private String mUserID;
	private String mCommand;

    public AuditRecord(AuditRecord other) {
        mDate = other.mDate;
        mTime = other.mTime;
        mType = other.mType;
        mUserID = other.mUserID;
		mCommand = other.mCommand
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
