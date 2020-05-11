import subprocess
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
            "This is the first time the account is being used. Create a new password. Passwords may contain up to 24 upper-or lower-case letters or numbers. Choose an uncommon password that would be difficult to guess.12341"
        ],
        [
            "test",
            "Reenter the same password: "
        ],
        [
            "test",
            "OK"
        ],
        [
            "EXT"
        ]
    ]
    # "login as admin, hlp" : ["LIN admin", "test", "HLP", "EXT"]
    # "hlp" : ["HLP", "HLP", "HLP", "EXT"]
}

for case in testCases:
    testCase = testCases[case]
    child = popen_spawn.PopenSpawn('java -jar AddressBook.jar', timeout=1)
    child.expect('> ')
    for step in testCase:
        child.sendline(step[0])
        if len(step) > 1:
            ret = child.expect([step[1], expectTimeout])
            if(ret == 1):
                # failure
                print("Test Failure: " + case )
                print("Command: " + step[0])
                print("Expected: " + step[1])
                print("Actual: " + child.before.decode("utf-8"))
                break
