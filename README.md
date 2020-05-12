# Address Book

### Running AddressBook 

To run the AddressBook run:
```
java -jar AddressBook.jar
```

### Running Fuzzer

Fuzzer requires python and pexpect `pip install pexpect`
<br>

Run Fuzzer in `Fuzzer` directory:
```
python Fuzzer.py
```


### Fuzzer Tests
Fuzzer contains 57 tests covering the functionality specified by the address book command line interface.
These are listed in the top of the Fuzzer.
Example:
```
"Login as admin, first time": [
        [
            #Step 1

            # command
            "LIN admin", 
            # expected result
            "This is the first time the account is being used. Create a new password. Passwords may contain up to 24 upper-or lower-case letters or numbers. Choose an uncommon password that would be difficult to guess."
        ],
        [
            #Step 2

            #command
            adminPassword,
            #expected result
            "\nReenter the same password:"
        ],
        [
            #Step 3

            #command
            adminPassword,
            #expected result
            "OK\n>"
        ],
        [
            "EXT"
        ]
    ],
```

### Fuzzer Results
```
TEST FAILURE: Edit record success
Command: RER 123456677
Expected: 123456677 SN=newSurName PEM= WEM= PPH= WPH= SA= CITY= STP= CTY= PC=
>
Actual:  123456677 SN=newSurName PEM= WEM= PPH= WPH= SA= CITY= STP= CTY= PC=
OK
> 


TEST FAILURE: Read partial record
Command: RER 097291709 PEM WEM SA
Expected: 097291709 PEM=personal@gmail.com WEM=work@gmail.com SA=4284  Willison Street
OK
>
Actual: 097291709 PEM=personal@gmail.com WEMwork@gmail.com SA=4284  Willison Street 
OK
> 


TEST FAILURE: Read full record
Command: RER 097291709
Expected: 097291709 GN=Susan SN=Owensby PEM=personal@gmail.com WEM=work@gmail.com PPH=763-263-7519 WPH=763-217-5750 SA=4284  Willison Street CITY=Big Lake CTY=USA PC=55309
OK
>
Actual: 097291709 SN=Owensby PEM=personal@gmail.com WEM=work@gmail.com PPH=763-263-7519 WPH=763-217-5750 SA=4284  Willison Street CITY=Big Lake STP= CTY=USA PC=55309
OK
> 


Failed: 3
Passed: 310
```

### Lynis and Programming Mistake Detector(PMD) Results
Results from Lynis and PMD can be found in `Testing` directory
