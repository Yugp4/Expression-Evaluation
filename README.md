# Expression-Evaluation
Enter an arithmetic expression with numbers or variables and get the answer!

-------------------CODED IN JAVA----------------

This app is an arithmetic calculator that calculates the answer to an arithmetic expression no matter how long it may be!  (The only things this app does NOT accept as inputs are exponents and negative answers, however, even though this program doesn't accept negative numbers it can still give a negative answer. e.g: 2-3 = -1)

This app has the method evaluate which makes use of stacks and arraylists to evaluate a given function.

In this app a drivier application is provided which is called "Evaluator.java"(not created by me, but provided by my professor) which accepets two inputs:

First being the expression itself, and second being a variables file that has values corresponding to the variables.

How to run the expression app:

The first input will ask for is the expression itself:

For example an expression such as these will work: 

2+3*4/(3*6+9*(10/5))-(contains only basic arithmetic operators)

A[2]*a+b-var/(3*4+5-6 * arr[0])-(contains basic arithmetic operators and variables)

However an expression such as any of these will not work:

-1+2*-3/-4 (contains negative numbers)

3*2^5+4 (contains exponents)

The next input will ask for a file of variables, if the user has one:

In order to create a variables file:

First create a text file and input the data as follows:

(Note: only alphabetical names in the variables allowed, no numbers should occur in the names of the variables)

If the variable you want is a single variable only then enter the variable name followed by what value the variable holds
If you want to enter a new variable then start it on a new line

For example: 

Format of the file containing values of these variables:

a 6

b 7

var 10

Suppose the expression is as follows:

3*a+b/7-var*10

Next enter the name of the file that has the variable names, and the user should get their correct answer

If the variable you want is an array of numbers then enter the variable name followed by the length of the array followed by pairs of indexes and values that are to be stored in the array.
If you want to add a new variable then enter it on a new line

For example:

Format of the file containing values of these variables:

A 4 (0, 1), (2, 10)

arr 6 (0, 55), (4, 75), (5, 35)

{First entry is the name of the array variable, next entry is the length of the array, the following entries is what the array holds and at what index, (For A, the entry at index 0 is 1, the entry at index 2 is 10)}

(Note: every index inside the array doesn't have to have a value, some indexes can be left empty by simply not entering a value at all)

Suppose the expression is as follows:

(A[0]*3 + arr[4]/5)/(arr[4] + A[2])

Next enter the name of the file that contains the variable names, and then the user should get their correct answer

(Note: array variables and single variables can both be in one file, no need to create separate files for array variables and single variables, also an expression containing both single and array variables is accepted as input)


