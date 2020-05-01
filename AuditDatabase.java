import java.util.HashMap;
import java.util.LinkedList;
import java.io.*;

class AuditDatabase implements AuditDatabaseInterface 
{
    private HashMap<String, LinkedList<Integer>> mUsersToPositions;
    private ArrayList<AuditRecordInterface> mRecords;

    public AuditDatabase() 
    {   
		mUsersToPositions = new HashMap<>(MAX_USERS, 1);
		mRecords = new ArrayList<>(MAX_ENTRIES);
    }
    public AuditDatabase(AuditDatabase other)
    {	
		mUsersToPositions = new HashMap<>(other.mUsersToPositions);
		mRecords = new ArrayList<>(other.mRecords);
    }
	public AuditDatabase(String auditFile)
	{	
		this();
		loadFromFile(auditFile, mUsersToPositions, mRecords);
	}
    public void getAuditLog(UserInterface activeUser, 
                         	String userID)
    {
    
        /* TODO: Implement conditions for getting audit log for display.  The
		 * conditions include the following:
		 * 		1.) A user is active
		 *		2.) User is admin
		 *		3.) User ID is valid
		 *		4.) Account with userID exists
		 */

        
        if (/*FIXME:*/)
        {
            HashMap<String, LinkedList<Integer>> positions =
			mUsersToPositions.get(userID);
			for(int i: positions)
				mRecords.get(i).display();
        }
    }

    public void getAuditLog(UserInterface activeUser)
    {
		// See getAuditLog(activerUser, userID)
        if(/*FIXME*/)
        {
			for (AuditRecordInterface record: mRecords)
				record.display();
        }
    }

    public void updateLog(AuditRecordInterface newRecord)
    {
		LinkedList<Integer> positions;
        if(mRecords.size() >= MAX_ENTRIES)
		{
			AuditRecordInterface firstRecord = mRecords.get(0);
			positions = mUsersToPositions.get(firstRecord.getKey());
			positions.remove(0);
			mRecords.remove(0);
			firstRecord = null;	
		}
		mRecords.add(newRecord);
		positions = mUsersToPositions.get(newRecord.getKey());
		positions.add(mRecords.size() - 1);
    }
    private void loadFile(HashMap<String, LinkedList<Integer>>
						usersToPositions, 
						ArrayList<AuditRecordInterface> records,
						String auditFile) throws IOException
    {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(
													auditFile));

    }
}