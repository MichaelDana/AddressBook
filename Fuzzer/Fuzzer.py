import subprocess
import difflib
from pexpect import popen_spawn
from pexpect import TIMEOUT as expectTimeout


def getInputStr(testCase):
    inputStr = ""
    for testCaseCommand in testCase:
        inputStr += testCaseCommand + "\n"

    return inputStr


testCases = {
    "Login as admin, first time": [
        [
            "LIN admin",
            "This is the first time the account is being used. Create a new password. Passwords may contain up to 24 upper-or lower-case letters or numbers. Choose an uncommon password that would be difficult to guess."
        ],
        [
            "test",
            "\r\nReenter the same password:"
        ],
        [
            "test",
            "OK\r\n>"
        ],
        [
            "EXT"
        ]
    ],
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
            "LIN validAccount",
            "Password: "
        ],
        [
            "password",
            "OK\r\n> "
        ],
        [
            "EXT"
        ]
    ],
    "Failed login valid account": [
        [
            "LIN validAccount",
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
    "login as admin, hlp": [
        [
            "LIN admin",
            "Password: "
        ],
        [
            "test",
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
            "test",
            "OK\r\n>"
        ],
        [
            "HLP EXT",
            "EXT\r\n> "
        ],
        ["EXT"]
    ]
}
numTestCases = len(testCases)
numFailed = 0
for case in testCases:
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
                print("Test Failure: " + case )
                print("Command: " + step[0])
                print("Expected: " + expected)
                print("Actual: " + actual)
                print("\n")
                numFailed += 1
                break
print("Failed: " + str(numFailed))
print("Passed: " + str(numTestCases - numFailed))