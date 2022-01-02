//Bryant Hays & James Day
//06/08/2021
//Professor Goyal
//CS 244 Advanced Data Structures in Java

//MainMenu Class
package bankingManagement;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class MainMenu {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		int choice = 0;
		boolean repeat = true;
		String continuing = "y";
		AVLTree customerAccounts = new AVLTree();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(" MM/dd/yyyy HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		customerAccounts.enterAccountsFromFile();		//comment this out if you want to do testing without accounts from the file
		
		
		
		//choice != 6
		while (repeat) {
			System.out.println("North-Western Credit Union" + dtf.format(now));
			System.out.println("----------------------------------------------");
			System.out.println("Welcome to the Main Menu");
			System.out.println("\nPlease select an option by pressing the appropriate key:");
			

			System.out.println();
			System.out.println("1. Create a New Account");
			System.out.println("2. Make a transaction"); // Will have to look up account, and ask for bank account
			// number, and pin
			System.out.println("3. Change information for your account"); // if someone wants a new card, or they want a
			// new pin or change their name etc, etc.
			System.out.println("4. view a list of customers"); // simple in order traversal.
			System.out.println("5. Delete an account"); // AVL tree deletion
			System.out.println("6. exit");
			System.out.println();
			System.out.print("     Enter your choice: ");

			choice = input.nextInt();
			switch (choice) {
			case 1:									
				customerAccounts.newAccount();
				break;
			case 2:
				customerAccounts.transaction();
				break;
			case 3:
				customerAccounts.changeInfo();
				break;
			case 4:
				 customerAccounts.inOrder();
				break;
			case 5:
				customerAccounts.deleteAccount();
				break;
			case 6:
				//saves to a text file in the project folder called Accounts.txt
				customerAccounts.saveAccountsToFile(); 

				//System.out.println("\n exiting...");
				repeat = false;
				continuing = "n";
				//System.exit(0);
			}

			System.out.println();

			if(choice != 6) {
				System.out.print("Would you like to preform another action (Y/N): ");
				continuing = input.next().toLowerCase();

				while (!continuing.equalsIgnoreCase("n") && !continuing.equalsIgnoreCase("y")) {
					System.out.print("Error: Please enter a correct option:");
					continuing = input.next().toLowerCase();
				}

			}


			if (continuing.equalsIgnoreCase("n")) { 
				repeat = false;
			}

			if (continuing.equalsIgnoreCase("y")) { 
				repeat = true;
			}
			if (repeat == false && continuing.equalsIgnoreCase("n")) {

				System.out.print("\n exiting.");
				try {
					TimeUnit.MILLISECONDS.sleep(500);//Artificial wait time before refreshing the
					System.out.print(".");
					TimeUnit.MILLISECONDS.sleep(500);//main screen for a new user
					System.out.print(".");
					TimeUnit.MILLISECONDS.sleep(500);
					System.out.println(".");
					TimeUnit.MILLISECONDS.sleep(500);
					System.out.println();					
					System.out.println();

					//System.out.println("Welcome to the Main Menu");
				} catch (InterruptedException e) {

				}
				repeat = true;
			}

		}

		input.close();

	}

}
