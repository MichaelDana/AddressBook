
interface AuditDatabaseInterface extends Writer{
    int MAX_ENTRIES = 512;
	int MAX_USERS = 7;

    void getAuditLog(AuthenticatorInterface authenticator, String userID);

    void getAuditLog(AuthenticatorInterface authenticator);

    void updateLog(AuditRecordInterface newRecord);

	void deleteLog(String userID);
}
