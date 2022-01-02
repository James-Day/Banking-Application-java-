//Bryant Hays & James Day
//06/08/2021
//Professor Goyal
//CS 244 Advanced Data Structures in Java

//BankAccount Class
package bankingManagement;

import java.util.Random;
import java.util.Scanner;

public class BankAccount {

	//Variables

	public String name;
	public int accountNumber;
	public int pin;
	public int checkingBalance;
	public int savingsBalance;


	//Default constructor
	public BankAccount(String customerName, int userPin, int initialDeposit) {
		Scanner input = new Scanner(System.in);
		int choice = 0;
		name = customerName;

		accountNumber = newAccountNumber();
		System.out.println("Your account number is: " + accountNumber);
		pin = userPin;

		System.out.println("Which account would you like to place your initial deposit?");
		System.out.println("1. Checking");
		System.out.println("2. Savings");
		System.out.print("Enter your choice: ");

		choice = input.nextInt(); 

		switch(choice) {
		case 1:
			checkingBalance = initialDeposit;
			savingsBalance = 0;
			break;
		case 2:
			savingsBalance = initialDeposit;
			checkingBalance = 0;
			break;
		}
	}

	//Constructor for saved accounts.
	public BankAccount(String customerName, int userPin, int AccountNumber, int checking, int savings) { 
		name = customerName;
		pin = userPin;
		accountNumber = AccountNumber;
		checkingBalance = checking;
		savingsBalance = savings; 
	}

	//Methods
	
	//Generates a new random account number.
	public int newAccountNumber() {
		Random rand = new Random();
		int newNumber = rand.nextInt(Integer.MAX_VALUE - 1000000) + 1000000; // to make sure the number is at least 7
		// digits.
		return newNumber;
	}

	//Method to withdraw cash from a specific account. Dollar amount must be an increment of $20 and cannot exceed $500.
	public void withdraw(int dollarAmount) { 

		Scanner input = new Scanner(System.in);
		int choice = 0;



		if (dollarAmount % 20 == 0 && dollarAmount < 500) {

			System.out.println("Which account would you like to withdraw from?: ");

			System.out.println("1. Checking");
			System.out.println("2. Savings");
			System.out.println("Enter your choice: ");

			choice = input.nextInt(); 

			switch (choice) {

			case 1:
				if (checkingBalance - dollarAmount < 0) {
					System.out.print("Because this transaction will take you below $0"
							+ "\nyou will be charged a $30 overdraft fee" + "\nAre you sure you'd like to withdraw?(Y/N)");
					if (input.next().toLowerCase().equals("y")) {
						checkingBalance = (checkingBalance - dollarAmount) - 30;
						System.out.println("Your new balance after the overdraft fee is: $n" + checkingBalance);
					}
				}
				else {
					checkingBalance = checkingBalance - dollarAmount;
					System.out.println("Your new checking account balance is: $" + checkingBalance);
				}

				break;
			case 2:
				if (savingsBalance - dollarAmount < 0) {
					System.out.print("Because this transaction will take you below $0"
							+ "\nyou will be charged a $30 overdraft fee" + "\nAre you sure you'd like to withdraw?(Y/N):");
					if (input.next().toLowerCase().equals("y")) {
						savingsBalance = (savingsBalance - dollarAmount) - 30;
						System.out.println("Your new balance after the overdraft fee is: $" + savingsBalance);
					}

				}
				else {
					savingsBalance -= dollarAmount;
					System.out.println("Your new savings account balance is: $" + savingsBalance);
				}
				break;
			}
		}

		
		return;
	}

	//Deposits a specified dollar ammount into the users specified account and returns the necessary new balance.
	public void deposit(int dollarAmount) {
		Scanner input = new Scanner(System.in);
		int choice = 0;

		System.out.println("Which account would you like to deposit to?: ");

		System.out.println("1. Checking");
		System.out.println("2. Savings");
		System.out.println("Enter your choice: ");

		choice = input.nextInt(); 

		switch(choice) {
		case 1:
			checkingBalance += dollarAmount;
			System.out.println("Your new checking account balance is: $" + checkingBalance);

			break;
		case 2:
			savingsBalance += dollarAmount;
			System.out.println("Your new savings account balance is: $" + savingsBalance);

		}
	}

}
