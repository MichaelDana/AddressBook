import java.util.HashMap;
import java.util.LinkedList;
import java.io.*;

class AuditDatabase implements AuditDatabaseInterface, Writer 
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
		else if(!mUsersToPositions.containsKey(newRecord.getKey()))
			mUsersToPositions.put(newRecord.getKey(), new LinkedList<Integer>);
		mRecords.add(newRecord);
		positions = mUsersToPositions.get(newRecord.getKey());
		positions.add(mRecords.size() - 1);
    }
	public void deleteLog(String userID)
	{
		LinkedList<Integer> positions = mUsersToPositions.get(userID);
		for(int i: positions)
			mRecords.remove(i);
		positions.clear();
		positions = null;
		mUsersToPositions.remove(userID);
	}
    public void writeToDisk(String auditFile, RecordInterface newRecord)
	{
		if(!(newRecord instanceof AuditRecordInterface))
			throw new RuntimeException("Record is not an Audit Record.");
		
		this.updateLog((AuditRecordInterface) newRecord);
		if(mRecords.size() 

	}
    private void loadFile(HashMap<String, 
    					LinkedList<Integer>> usersToPositions, 
						ArrayList<AuditRecordInterface> records,
						String auditFile)
						
    {
		ObjectInputStream in = null;
		try
		{
			in = new ObjectInputStream(new FileInputStream(
													auditFile));
			int numberOfRecords = in.readInt();
			LinkedList<Integer> positions;
			for(int i = 0; i < numberOfRecords; i++)
			{
				AuditRecordInterface record = (AuditRecordInterface) in.readObject();
				if(usersToPositions.containsKey(record.getKey()))
				{
					positions = usersToPositions.get(record.getKey());
					positions.add(i);
				}
				else
				{
					positions = new LinkedList<>();
					positions.add(i);
					usersToPositions.put(record.getKey(), positions);
				}		
				records.add((AuditRecordInterface) in.readObject());	
			}
		}
		catch(IOException e)
		{	System.out.println(e);
		}
		catch(ClassNotFoundException e)
		{	System.out.println(e);
		}
		finally
		{
			try
			{	in.close();
			}
			catch(IOException e)
			{	System.out.println(e);
				System.exit(1);
			}
		}
    }
}
