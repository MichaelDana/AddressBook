import subprocess
import pexpect

def getInputStr(testCase):
    inputStr = ""
    for testCaseCommand in testCase:
        inputStr += testCaseCommand + "\n"

    return inputStr 

testCases = {
    "login as admin" : ["LIN admin", "test", "test", "EXT"]
    # "login as admin, hlp" : ["LIN admin", "test", "HLP", "EXT"]
    # "hlp" : ["HLP", "HLP", "HLP", "EXT"]
}

command = "java -jar Fuzzer/AddressBook.jar"
for testCase in testCases: 
    testCaseCommands = testCases[testCase]
    # lazy use of universal_newlines to prevent the need for encoding/decoding
    # p = subprocess.Popen(command, stdin=subprocess.PIPE, stdout=subprocess.PIPE, shell=True, universal_newlines=True)  
    # output, err = p.communicate(getInputStr(testCaseCommands))
    # output, err = p.communicate("LIN admin")
    # stderr is not connected to a pipe, so err is None
    # print(*testCaseCommands, sep="\n")
    # print("----")
    # we just want the result of the command
    # print(output)
    # print(output[output.rfind(" "):-1])  # -1 to strip the final newline
    child = pexpect.spawn ('java -jar Fuzzer/AddressBook.jar')
    child.expect ('> ')
    child.sendline ('LIN admin')
    child.expect("Password: ")

