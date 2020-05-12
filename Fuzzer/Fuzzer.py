import subprocess
import difflib
import os
import shutil
import time
import random
import string
from pexpect import popen_spawn
from pexpect import TIMEOUT as expectTimeout

adminPassword = "oa2PFlBnLm46Zz8"
validUserId = "validUser"
validUserPassword = "xj7194hU0uo7NSH"
validUserNewPassword = "hzT52wBdC5zYwVp"
deletableUserId = "deletableUser"
validRecordId = "123456677"
deletableRecordId = "987654321"
fullRecordId = "097291709"
randomUserID = "1236712Us"
randomUserPassword = "TCBHHq7shwSM6Up"

setupTestCases = {
    "Login as admin, first time": [
        [
            "LIN admin",
            "This is the first time the account is being used. Create a new password. Passwords may contain up to 24 upper-or lower-case letters or numbers. Choose an uncommon password that would be difficult to guess."
        ],
        [
            adminPassword,
            "\r\nReenter the same password:"
        ],
        [
            adminPassword,
            "OK\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Login as admin, add users": [
        [
            "LIN admin",
            "Password: "
        ],
        [
            adminPassword,
            "OK\r\n>"
        ],
        [
            "ADU " + validUserId,
            "OK\r\n>"
        ],
        [
            "ADU " + deletableUserId,
            "OK\r\n>"
        ],
        [
            "ADU " + randomUserID,
            "OK\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Failed login as valid user non matching passwords, first time": [
        [
            "LIN " + validUserId,
            "This is the first time the account is being used. Create a new password. Passwords may contain up to 24 upper-or lower-case letters or numbers. Choose an uncommon password that would be difficult to guess."
        ],
        [
            validUserPassword,
            "\r\nReenter the same password:"
        ],
        [
            "not_matching12345$",
            "Passwords do not match\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Failed login as valid user easy to guess password, first time": [
        [
            "LIN " + validUserId,
            "This is the first time the account is being used. Create a new password. Passwords may contain up to 24 upper-or lower-case letters or numbers. Choose an uncommon password that would be difficult to guess."
        ],
        [
            "password",
            "\r\nReenter the same password:"
        ],
        [
            "password",
            "Password is too easy to guess\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Failed login as valid user illegal characters, first time": [
        [
            "LIN " + validUserId,
            "This is the first time the account is being used. Create a new password. Passwords may contain up to 24 upper-or lower-case letters or numbers. Choose an uncommon password that would be difficult to guess."
        ],
        [
            "password$12alksd#",
            "\r\nReenter the same password:"
        ],
        [
            "password$12alksd#",
            "Password contains illegal characters\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Login as valid user, first time": [
        [
            "LIN " + validUserId,
            "This is the first time the account is being used. Create a new password. Passwords may contain up to 24 upper-or lower-case letters or numbers. Choose an uncommon password that would be difficult to guess."
        ],
        [
            validUserPassword,
            "\r\nReenter the same password:"
        ],
        [
            validUserPassword,
            "OK\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Login as valid user, first time (random user)": [
        [
            "LIN " + randomUserID,
            "This is the first time the account is being used. Create a new password. Passwords may contain up to 24 upper-or lower-case letters or numbers. Choose an uncommon password that would be difficult to guess."
        ],
        [
            randomUserPassword,
            "\r\nReenter the same password:"
        ],
        [
            randomUserPassword,
            "OK\r\n>"
        ],
        [
            "EXT"
        ]
    ]
}

testCases = {
    "Login bad account": [
        [
            "LIN invalidAccount",
            "Invalid credentials\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Login valid account": [
        [
            "LIN " + validUserId,
            "Password: "
        ],
        [
            validUserPassword,
            "OK\r\n> "
        ],
        [
            "EXT"
        ]
    ],
    "Failed login valid account": [
        [
            "LIN " + validUserId,
            "Password: "
        ],
        [
            "not_password",
            "Invalid credentials\r\n> "
        ],
        [
            "EXT"
        ]
    ],
    "Logout no active login": [
        [
            "LOU",
            "No active login session"
        ],
        [
            "EXT"
        ]
    ],
    "Logout success": [
        [
            "LIN " + validUserId,
            "Password: "
        ],
        [
            validUserPassword,
            "OK\r\n> "
        ],
        [
            "LOU",
            "OK\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Add users not active login": [
        [
            "ADU newUserId",
            "No active login session\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Add users admin not active": [
        [
            "LIN " + validUserId,
            "Password: "
        ],
        [
            validUserPassword,
            "OK\r\n> "
        ],
        [
            "ADU newUserId",
            "Admin not active\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Login as admin, add user invalid user id": [
        [
            "LIN admin",
            "Password: "
        ],
        [
            adminPassword,
            "OK\r\n>"
        ],
        [
            "ADU 12345^%@",
            "Invalid userID\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Login as admin, add users account already exists": [
        [
            "LIN admin",
            "Password: "
        ],
        [
            adminPassword,
            "OK\r\n>"
        ],
        [
            "ADU " + validUserId,
            "Account already exists\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Delete users not active login": [
        [
            "DEU newUserId",
            "No active login session\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Delete user admin not active": [
        [
            "LIN " + validUserId,
            "Password: "
        ],
        [
            validUserPassword,
            "OK\r\n> "
        ],
        [
            "DEU newUserId",
            "Admin not active\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Delete user, invalid id": [
        [
            "LIN admin",
            "Password: "
        ],
        [
            adminPassword,
            "OK\r\n>"
        ],
        [
            "DEU nonExistantUser$%",
            "Invalid userID\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Delete user, does not exist": [
        [
            "LIN admin",
            "Password: "
        ],
        [
            adminPassword,
            "OK\r\n>"
        ],
        [
            "DEU nonExistantUser",
            "Account does not exist\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Delete user success": [
        [
            "LIN admin",
            "Password: "
        ],
        [
            adminPassword,
            "OK\r\n>"
        ],
        [
            "DEU " + deletableUserId,
            "OK\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Add record, no active login session": [
        [
            "ADR 12345 [name=firstName]",
            "No active login session"
        ],
        [
            "EXT"
        ]
    ],
    "Add record as admin": [
        [
            "LIN admin",
            "Password: "
        ],
        [
            adminPassword,
            "OK\r\n> "
        ],
        [
            "ADR 12345 GN=firstName",
            "Admin not authorized\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Add record no record id": [
        [
            "LIN " + validUserId,
            "Password: "
        ],
        [
            validUserPassword,
            "OK\r\n> "
        ],
        [
            "ADR GN=firstName",
            "No recordID\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Add record invalid id": [
        [
            "LIN " + validUserId,
            "Password: "
        ],
        [
            validUserPassword,
            "OK\r\n> "
        ],
        [
            "ADR 12345$ GN=firstName",
            "Invalid recordID\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Add record invalid field": [
        [
            "LIN " + validUserId,
            "Password: "
        ],
        [
            validUserPassword,
            "OK\r\n> "
        ],
        [
            "ADR 12345 XN=invalid",
            "One or more invalid record data fields\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Add record success": [
        [
            "LIN " + validUserId,
            "Password: "
        ],
        [
            validUserPassword,
            "OK\r\n> "
        ],
        [
            "ADR "+ validRecordId + " GN=firstName",
            "OK\r\n>"
        ],
        [
            "ADR "+ deletableRecordId + " GN=firstName",
            "OK\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Add full record success": [
        [
            "LIN " + validUserId,
            "Password: "
        ],
        [
            validUserPassword,
            "OK\r\n> "
        ],
        [
            "ADR " + fullRecordId + " GN=Susan SN=Owensby PEM=personal@gmail.com WEM=work@gmail.com PPH=763-263-7519 WPH=763-217-5750 SA=4284  Willison Street CITY=Big Lake CTY=USA PC=55309",
            "OK\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Add record duplicate id": [
        [
            "LIN " + validUserId,
            "Password: "
        ],
        [
            validUserPassword,
            "OK\r\n> "
        ],
        [
            "ADR "+ validRecordId + " GN=firstName",
            "Duplicate recordID\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Delete record, no active login session": [
        [
            "DER 12345",
            "No active login session"
        ],
        [
            "EXT"
        ]
    ],
    "Delete record as admin": [
        [
            "LIN admin",
            "Password: "
        ],
        [
            adminPassword,
            "OK\r\n> "
        ],
        [
            "DER 12345",
            "Admin not authorized\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Delete record no record id": [
        [
            "LIN " + validUserId,
            "Password: "
        ],
        [
            validUserPassword,
            "OK\r\n> "
        ],
        [
            "DER",
            "No recordID\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Delete record invalid id": [
        [
            "LIN " + validUserId,
            "Password: "
        ],
        [
            validUserPassword,
            "OK\r\n> "
        ],
        [
            "DER 1234$",
            "Invalid recordID\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Delete record success": [
        [
            "LIN " + validUserId,
            "Password: "
        ],
        [
            validUserPassword,
            "OK\r\n> "
        ],
        [
            "DER " + deletableRecordId,
            "OK\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Delete record, record not found": [
        [
            "LIN " + validUserId,
            "Password: "
        ],
        [
            validUserPassword,
            "OK\r\n> "
        ],
        [
            "DER " + deletableRecordId,
            "RecordID not found\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Edit record, no active login session": [
        [
            "EDR 12345 GN=name",
            "No active login session"
        ],
        [
            "EXT"
        ]
    ],
    "Edit record as admin": [
        [
            "LIN admin",
            "Password: "
        ],
        [
            adminPassword,
            "OK\r\n> "
        ],
        [
            "EDR 12345 GN=name",
            "Admin not authorized\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Edit record no record id": [
        [
            "LIN " + validUserId,
            "Password: "
        ],
        [
            validUserPassword,
            "OK\r\n> "
        ],
        [
            "EDR GN=name",
            "No recordID\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Edit record invalid id": [
        [
            "LIN " + validUserId,
            "Password: "
        ],
        [
            validUserPassword,
            "OK\r\n> "
        ],
        [
            "EDR 12#34 GN=name",
            "Invalid recordID\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Edit record, id not found": [
        [
            "LIN " + validUserId,
            "Password: "
        ],
        [
            validUserPassword,
            "OK\r\n> "
        ],
        [
            "EDR " + deletableRecordId + " GN=name",
            "RecordID not found\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Edit record invalid fields": [
        [
            "LIN " + validUserId,
            "Password: "
        ],
        [
            validUserPassword,
            "OK\r\n> "
        ],
        [
            "EDR " + validRecordId + " XN=invalid",
            "One or more invalid record data fields\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Edit record success": [
        [
            "LIN " + validUserId,
            "Password: "
        ],
        [
            validUserPassword,
            "OK\r\n> "
        ],
        [
            "EDR " + validRecordId + " GN=newName SN=newSurName",
            "OK\r\n>"
        ],
        [
            "RER " + validRecordId,
            validRecordId + " SN=newSurName PEM= WEM= PPH= WPH= SA= CITY= STP= CTY= PC=\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Read record, not active login": [
        [
            "RER " + fullRecordId + " PEM WEM SA",
            "No active login session\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Read record as admin": [
        [
            "LIN admin",
            "Password: "
        ],
        [
            adminPassword,
            "OK\r\n> "
        ],
        [
            "RER " + fullRecordId + " PEM WEM SA",
            "Admin not authorized\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Read record invalid id": [
        [
            "LIN " + validUserId,
            "Password: "
        ],
        [
            validUserPassword,
            "OK\r\n> "
        ],
        [
            "RER 12#34 GN=name",
            "Invalid recordID\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Read record, id not found": [
        [
            "LIN " + validUserId,
            "Password: "
        ],
        [
            validUserPassword,
            "OK\r\n> "
        ],
        [
            "RER " + deletableRecordId + " GN=name",
            "RecordID not found\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Read record invalid fields": [
        [
            "LIN " + validUserId,
            "Password: "
        ],
        [
            validUserPassword,
            "OK\r\n> "
        ],
        [
            "RER " + validRecordId + " XN=invalid",
            "One or more invalid record data fields\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Read partial record": [
        [
            "LIN " + validUserId,
            "Password: "
        ],
        [
            validUserPassword,
            "OK\r\n> "
        ],
        [
            "RER " + fullRecordId + " PEM WEM SA",
            fullRecordId + " PEM=personal@gmail.com WEM=work@gmail.com SA=4284  Willison Street\r\nOK\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Read full record": [
        [
            "LIN " + validUserId,
            "Password: "
        ],
        [
            validUserPassword,
            "OK\r\n> "
        ],
        [
            "RER " + fullRecordId,
            fullRecordId + " GN=Susan SN=Owensby PEM=personal@gmail.com WEM=work@gmail.com PPH=763-263-7519 WPH=763-217-5750 SA=4284  Willison Street CITY=Big Lake CTY=USA PC=55309\r\nOK\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Change password no active login": [
        [
            "CHP test",
            "No active login session"
        ],
        [
            "EXT"
        ]
    ],
    "Change password invalid credentials": [
        [
            "LIN " + validUserId,
            "Password: "
        ],
        [
            validUserPassword,
            "OK\r\n> "
        ],
        [
            "CHP invalid_pass",
            "Invalid credentials"
        ],
        [
            "EXT"
        ]
    ],
    "Change password non matching": [
        [
            "LIN " + validUserId,
            "Password: "
        ],
        [
            validUserPassword,
            "OK\r\n> "
        ],
        [
            "CHP " + validUserPassword,
            "Create a new password. Passwords may contain up to 24 upper-or lower-case letters or numbers. Choose an uncommon password that would be difficult to guess.\r\n"
        ],
        [
            validUserNewPassword,
            "Reenter the same password:"
        ],
        [
            "notMacthing1235$",
            "Passwords do not match"
        ],
        [
            "EXT"
        ]
    ],
    "Change password illegal characters": [
        [
            "LIN " + validUserId,
            "Password: "
        ],
        [
            validUserPassword,
            "OK\r\n> "
        ],
        [
            "CHP " + validUserPassword,
            "Create a new password. Passwords may contain up to 24 upper-or lower-case letters or numbers. Choose an uncommon password that would be difficult to guess.\r\n"
        ],
        [
            "illegalChar$",
            "Reenter the same password:"
        ],
        [
            "illegalChar$",
            "Password contains illegal characters"
        ],
        [
            "EXT"
        ]
    ],
    "Change password too easy to guess": [
        [
            "LIN " + validUserId,
            "Password: "
        ],
        [
            validUserPassword,
            "OK\r\n> "
        ],
        [
            "CHP " + validUserPassword,
            "Create a new password. Passwords may contain up to 24 upper-or lower-case letters or numbers. Choose an uncommon password that would be difficult to guess.\r\n"
        ],
        [
            "password",
            "Reenter the same password:"
        ],
        [
            "password",
            "Password is too easy to guess\r\n> "
        ],
        [
            "EXT"
        ]
    ],
    "Change password success": [
        [
            "LIN " + validUserId,
            "Password: "
        ],
        [
            validUserPassword,
            "OK\r\n> "
        ],
        [
            "CHP " + validUserPassword,
            "Create a new password. Passwords may contain up to 24 upper-or lower-case letters or numbers. Choose an uncommon password that would be difficult to guess.\r\n"
        ],
        [
            validUserNewPassword,
            "Reenter the same password:"
        ],
        [
            validUserNewPassword,
            "OK\r\n>"
        ],
        [
            "EXT"
        ]
    ],
    "Help command full": [
        [
            "LIN admin",
            "Password: "
        ],
        [
            adminPassword,
            "OK\r\n>"
        ],
        [
            "HLP",
            "EXT\r\nIMD <Input_File>\r\nCHP <old password>\r\nHLP [<command name>]\r\nDAL [<userID>]\r\nADR <recordID> [<field1=value1> <field2=value2> ...]\r\nEXD <Output_File>\r\nDER <recordID>\r\nLIN <userID>\r\nEDR <recordID><field1=value1> [<field2=value2> ...]\r\nADU <userID>\r\nLOU\r\nDEU <userID>\r\nRER [<recordID>][<fieldname> ...]\r\n> "
        ],
        ["EXT"]
    ],
    "login as admin, hlp EXT": [
        [
            "LIN admin",
            "Password: "
        ],
        [
            adminPassword,
            "OK\r\n>"
        ],
        [
            "HLP EXT",
            "EXT\r\n> "
        ],
        ["EXT"]
    ]
}
def cleanAddressBook():
    try:
        if(os.path.isdir("address_book")):
            if (os.path.isdir("address_book/users")):
                shutil.rmtree("address_book/users")
            if (os.path.isdir("address_book/addr")):
                shutil.rmtree("address_book/addr")
    except :
        print("Unable to clean before tests.")

def runTests(testCases):
    testsRun = 0
    testsFailed = 0
    for case in testCases:
        testsRun += 1
        testCase = testCases[case]
        child = popen_spawn.PopenSpawn('java -jar AddressBook.jar', timeout=1)
        child.expect('> ')
        for step in testCase:
            child.sendline(step[0])
            if len(step) > 1:
                try:
                    ret = child.expect_exact(step[1])
                except expectTimeout:
                    actual = child.before.decode("utf-8")
                    expected = step[1]
                    print("TEST FAILURE: " + case )
                    print("Command: " + step[0])
                    print("Expected: " + expected)
                    print("Actual: " + actual)
                    print("\n")
                    testsFailed += 1
                    break
        if not child.terminated:
            child.wait()
        time.sleep(0.2)
    return testsRun,testsFailed   

def randomString(stringLength):
    letters = string.ascii_lowercase
    return ''.join(random.choice(letters) for i in range(stringLength))

def generateRandomTestCase():
    testCase = {}
    for i in range(255):
        testID = randomString(16)
        gn = randomString(64)
        sn = randomString(64)
        testCase["Test " + str(i)] = [
            [
                "LIN " + randomUserID,
                "Password: "
            ],
            [
                randomUserPassword,
                "OK\r\n> "
            ],
            [
                "ADR "+ testID + " GN=" + gn + " SN=" + sn,
                "OK\r\n>"
            ],
            [
                "RER " + testID + " GN SN",
                testID + " GN=" + gn + " SN=" + sn
            ],
            [
                "EXT"
            ]
        ]
        testCase["Test 255"] = [
            [
                "LIN " + randomUserID,
                "Password: "
            ],
            [
                randomUserPassword,
                "OK\r\n> "
            ],
            [
                "ADR "+ testID + " GN=" + gn + " SN=" + sn,
                "Number of records exceeds maximum"
            ],
            [
                "EXT"
            ]
        ]   
    return testCase


cleanAddressBook()
randomTestCase = generateRandomTestCase()
setupTests,failedSetupTests = runTests(setupTestCases)
tests,failedTests = runTests(testCases)
randomTests,failedRandomTests = runTests(randomTestCase)

print("Failed: " + str(failedSetupTests + failedTests+failedRandomTests))
print("Passed: " + str((setupTests+tests+randomTests) - (failedSetupTests+failedTests+failedRandomTests)))