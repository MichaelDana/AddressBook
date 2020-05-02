//import AddressBook.UserInterface;
//import audit.AuditRecordInterface;

interface AuditDatabaseInterface {
    int MAX_ENTRIES = 512;

    void getAuditLog(UserInterface activeUser, String userID);

    void getAuditLog(UserInterface activeUser);

    void updateLog(AuditRecordInterface newRecord);

	void deleteLog(String userID);
}
