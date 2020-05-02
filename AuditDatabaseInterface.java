
interface AuditDatabaseInterface extends Writer{
    int MAX_ENTRIES = 512;
	int MAX_USERS = 7;

    void getAuditLog(UserInterface activeUser, String userID);

    void getAuditLog(UserInterface activeUser);

    void updateLog(AuditRecordInterface newRecord);

	void deleteLog(String userID);
}
