<h4>FILE SPLITTER</h4>

**Task**  
You have to create an application for splitting a big file into parts. Splitting should use several threads. You should 
foresee using the app algorithm for restoring the file back.

*Optional*

    - convert the project to a multi-module one
    - add logging
    - add unit and integration tests
    - feel free to add some useful features

**The description of the app work**  
After running, a user inputs a command to start splitting - _split_, the path to the file, and a desirable size of 
a chunk. The app shows the work progress (in total and by each thread) using next format:  
    
    Total progress: 40%, thread 1: 0%, thread 2: 80%, time remaining: 20c

Progress information should be indicated each second. By the end of the process, the app prompts user to input next 
commands. A user can type _exit_ in order to exit.

**Commands**  

    split - breaks file into parts
    split -p /home/user/file.avi -s 20M
        -p - the file path
        -s - the size of a chunk
    exit - stops program executing