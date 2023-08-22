# How to work with Git & GitHub

Here are what steps you need to take to work with Git, GitHub, and GitHub Classroom:

1. Create a folder on your desktop, rename it to "Labs" and open up the command prompt (terminal).
2. Type in "cd " and drag and drop the folder you just created to the terminal window.
3. Then, type in "git clone <URL-to-repo>" and add your URL to the repository.
4. Now type in "cd " again and drag and drop the folder you just cloned to the terminal window. Leave this window open.
5. Open NetBeans/Eclipse and go to File -> Open Project. Find the folder you cloned and open the project. **Make sure you check "Trust Project Build Script** if you are using NetBeans, otherwise, you will not be able to run the tests.
6. Do the assignment!
7. Go to Run -> Clean and Build the project/Test Project to see if your test cases are passing.
8. Once you are done, go back to the terminal window and type in "git status" to see what files need to be staged. The files will be in red.
9. Next, type in "git add ." to stage all files.
10. Type in "git status" once again to see they are staged. The files should be green now.
11. Next, type in "git commit -m "<YOUR-COMMENT>"" to commit the changes.
12. And lastly, type in "git push" to push your code to the remote repository on GitHub.
13. Now you can go to your GitHub repository page -> Actions to check your resutls.
