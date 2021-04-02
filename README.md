# Virtual ATM Machine Project
This is the main project in University of Toronto's **CSC207: Software Design Course**. Multiple design patterns and the SOLID principle was incorporated into the design of this project.

The original repo was rooted on UofT's CS marking website, so another repo is created here for display purposes. I worked with four other teammates. All five of us contributed equally to the project. Our contact info can be found in **group.txt**.  To get an overview of the design and interaction between classes, please see **design.pdf** for a full design of all classes, and **uml.jpg** for the UML diagram. 

# Tutorial
*****
To run this bank_system.ATM program, please (set phase2 as Sources Root) and run Starter class.

There are three different ATMs to be selected when the program starts running
 ,they have no difference other than the initial cash storage. Select anyone you want.

After that, see data -> loginData.txt for login information.
There are three types of person:manager,user and staff as required.

NOTE: Our bank will be closed from 23:57 to 00:03. During this time, cannot login to atm. However if you already login,
you may keep using the atm until you logout.

********
## User Part:
The following shows a correct input example for "user,johncena1997,12345678".
"Please enter username and password in the form of bracket exclusive.
 (username-password): johncena1997-12345678
 Welcome!

 Main menu:
 Type 1 to view the balance of each account.
 Type 2 to transfer money.
 Type 3 to deposit
 Type 4 to withdraw
 Type 5 to change primary account
 Type 6 to change password
 Type 7 to check account creation time
 Type 8 to check transactions
 Type 9 to manage funding projects
 Type 10 to purchase stock
 Type 11 to sell stock
 Type 12 to interact EMT
 Type LOGOUT to exit."

 *Purchasing stock and selling stock(10 and 11) won't work unless the manager
 releases some financial products. This should be done using the manager's account.

*Things worth mentioning:
1) After typing 2, it goes like:
   "Type 1 to transfer to another user.
    Type 2 to pay bill."
   If you wish to pay bills, enter any payee such as "KFC" or "UberEats".

2)Each account,no matter the account type, can only hold one type of currency.
(transferring money from a USD account to a CAD account is unacceptable)

3) Each user/staff can have one primary account for each currency.(USD,CAD)
    Money will be automatically deposited into primary accounts.
    Also,Whenever you see
     "Please enter the username or accountID that you wish to transfer money to"
     Entering a username in this case will automatically transferr the money into
     his/her primary account;
4) Whenever you see:
    "Please enter the type of currency of this account."
    Only CAD or USD should be entered.

********
## Manager Part:

"Main menu:
Type 1 to create a new user.
Type 2 to create a new account.
Type 3 to create a joint account for two users.
Type 4 to interact with ATM cash.
Type 5 to undo transaction
Type 6 to check cash in ATM
Type 7 to change password
Type 8 to start a new funding project.
Type 9 to start a new stock.
Type LOGOUT to exit."

*Things worth mentioning:
1)After a new user is created, a CAD Chequing account is automatically created for this user.
2)After typing 4, it goes like:
"Type 1 to add cash.
 Type 2 to add a denomination
 Type 3 to add a new currency.
 Type EXIT back to main."
  It is suggested to add a new currency first, then add a denomination, then add
  the cash for this currency.
  Also, when adding a new denomination for, for example CAD 25$,
  some paper bills will automatically be added into the ATM, so you have to specify the
  number using the keyboard.


3) *Read this only if you want to know more about our funding project.
 After typing 8, you will be able to start a new funding project:
 Banks use funding projects to gather money from users. During the funding period,
 funds are released like stocks at a relatively high price which allows users to buy it.
 Then the interest period starts, interests rates changed over time but are qualitatively
 controlled by the value of interest rate shifter. By the end of interest period, users can get
 their money back.

 Example below shows a funding whose interest period starts at 2019-12-31 and lasts for
 4 months, with 1000 units at a unit price of 10 dollars. Interest rate is approximately 0.02.
 "
 Type 1 to start a new quick funding project with unit price pre-set to 5000CAD and total of 500 units.
 Type 2 to start a new customized funding project.
 Type Cancel to exit.
 2
 Please enter a name for the new funding, type CANCEL to cancel.
 1
 Please enter desired end date for funding period (aka. start date for interest period) follows format: YYYY-MM-DD, type CANCEL to cancel:
 Funding will start immediately.
 2019-12-31
 Please enter desired duration (in terms of months) for the interest period. Type CANCEL to cancel:
 4
 Please enter interest rate shifter. Type CANCEL to cancel:
 Positive shifter can shift interest rate right along x-axis, vice versa.
 !Friendly Reminder! Usually shifter is between 0.0015 to 0.02 to avoid potentially Bankrupt.
 0.02
 Please enter amount of units wish to create. Type CANCEL to cancel:
 1000
 Please enter desired unit price. Type CANCEL to cancel:
 1000
 Funding Project Created, Starting Date: 2019-12-31, Ending Date: 4.
 Total funding goal is 1000000 CAD
 "


4)*Read this only if you want to know more about our stock project.
   After typing 9, you will be able to start a new stock:
   Users can buy unlimited number of stocks after bankmanager releases it.
   Interest rate controller determines the likelihood of this stock to appreciate or
   depreciate, and by how much.(Not an absolute measurement)

   Example below shows a stock whose initial price is set to 100$ and more likely
   to increase over time.
   "
   Please enter desired positive unit price for the new stock.
   Type CANCEL to cancel:
   100
   Please enter desired interest rate controller for this stock.
   From -0.00200 to 0.00100.
   Type CANCEL to cancel: 0.00005
   Done!
   "
5) The manager can choose to undo a specific transaction on any account,
but some type of transactions are undoable, such as EMT(interact-Etransfer), bill-paying,
or transferring money out of Credit Card.

********
## Worker Part:
"
Please enter username and password in the form of bracket exclusive.
(username-password): staff1-123456
Welcome!

## Main menu:
Type 1 to check your own accounts.
Type 2 to create a new user.
Type 3 to create a new account.
Type 4 to create a joint account for two users.
Type LOGOUT to exit.
"
*Things worth mentioning:
1) After typing 1, this staff goes into the exact same page as the one for users.
2) The staff has fewer powers than managers. A staff is never able to implement functions associated
with money, such as undo, add money to ATM, or create a new type of currency. All the other
functions are listed as type 2 to 4.
