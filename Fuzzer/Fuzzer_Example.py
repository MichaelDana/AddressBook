import subprocess

values = (
    ("A", "B"), 
    ("C", "D"), 
    ("E", "F")
    )

command = "java -jar Test.jar"
for first, second in values: 
    # lazy use of universal_newlines to prevent the need for encoding/decoding
    p = subprocess.Popen(command, stdin=subprocess.PIPE, stdout=subprocess.PIPE, shell=True, universal_newlines=True)  
    output, err = p.communicate(input="{}\n{}\n".format(first, second))
    # stderr is not connected to a pipe, so err is None
    print(first, second, "-> ", end="")
    # we just want the result of the command
    print(output[2])
    # print(output[output.rfind(" "):-1])  # -1 to strip the final newline