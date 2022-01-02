//Bryant Hays & James Day
//06/08/2021
//Professor Goyal
//CS 244 Advanced Data Structures in Java

//AVLTree Class
package bankingManagement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

//Default constructor for AVL tree.
public class AVLTree {
	Node root;
	Scanner input = new Scanner(System.in);

	AVLTree() {
		root = null;
	}

	//Methods

	//Method to insert a node (customer) into the tree.
	boolean Insert(Node ins) {
		Node current = root;
		Node previous = null;
		boolean inserted = false;
		if (root == null) { // inserting root
			root = ins;
			ins.parent = null;
			ins.height = 0;
			return true;
		}
		// search where to insert
		while (current != null) { // While not at an empty node
			previous = current;
			if (ins.account.accountNumber < current.account.accountNumber) {
				current = current.left; // go left
			} else if (ins.account.accountNumber >= current.account.accountNumber) {
				current = current.right; // go right
			}
		}
		if (ins.account.accountNumber < previous.account.accountNumber) { // insert to the left
			previous.left = ins;
			ins.parent = previous;
			inserted = true;
		} else if (ins.account.accountNumber >= previous.account.accountNumber) { // insert to the right
			previous.right = ins;
			ins.parent = previous;
			inserted = true;
		}
		current = ins;
		current = current.parent;
		while (current != null) { // balances all parents of the inserted node
			balance(current);
			current = current.parent;
		}
		return inserted;
	}

	//Method to balance the tree to restore AVL tree balance properties.
	private Node balance(Node current) {
		updateHeight(current);
		if (getBalanceFactor(current) == -2) { // right - right
			if (getBalanceFactor(current.right) == 1) { // right - left - double rotation case
				rotateRight(current.right);
			}
			rotateLeft(current);
			return current;
		} else if (getBalanceFactor(current) == 2) { // left - left
			if (getBalanceFactor(current.left) == -1) {// left- right- double rotation case
				rotateLeft(current.left);
			}
			rotateRight(current);
			return current;
		}
		return current;
	}

	//Utility function to retrieve and return the balance factor so the tree can be re-balanced if necessary.  
	private int getBalanceFactor(Node curNode) {
		int leftHeight = -1;
		int rightHeight = -1;
		if (curNode.left != null) {
			leftHeight = curNode.left.height;
		}
		if (curNode.right != null) {
			rightHeight = curNode.right.height;
		}
		return leftHeight - rightHeight;
	}

	//Rotates the degenerate node left so that it becomes the child of its own child node and the child becomes the parent...
	//restoring AVL tree balancing properties.
	private void rotateLeft(Node leftNode) {

		Node rightleft = leftNode.right.left;
		if (leftNode.parent != null) {
			replaceChild(leftNode.parent, leftNode, leftNode.right);
		} else {
			leftNode.right.left = root; // node is root
			root = leftNode.right;
			root.parent = null;
		}
		SetChild(leftNode.right, "left", leftNode);
		SetChild(leftNode, "right", rightleft);
		while (leftNode != null && leftNode.parent != null) {
			updateHeight(leftNode);
			leftNode = leftNode.parent;
		}
	}

	//Rotates the degenerate node right so that it becomes the child of its own child node and the child becomes the parent...
	//restoring AVL tree balancing properties.
	private void rotateRight(Node rightNode) {
		Node leftRight = rightNode.left.right;
		if (rightNode.parent != null) {

			replaceChild(rightNode.parent, rightNode, rightNode.left);

		} else {
			rightNode.left.right = root;// node is root
			root = rightNode.left;
			root.parent = null;

		}
		SetChild(rightNode.left, "right", rightNode);
		SetChild(rightNode, "left", leftRight);

		while (rightNode != null && rightNode.parent != null) {
			updateHeight(rightNode);
			rightNode = rightNode.parent;
		}
	}

	//Updates the parent-child pointers.
	private boolean SetChild(Node node, String which, Node setNode) {
		if (which != "left" && which != "right")
			return false;
		if (which == "left") {
			node.left = setNode;
		} else {
			node.right = setNode;
		}
		if (setNode != null) {
			setNode.parent = node;
			while (setNode != null && setNode.parent != null) {
				updateHeight(setNode);
				setNode = setNode.parent;
			}
		}
		return true;
	}
	//Replaces the child when a rotation is performed.
	boolean replaceChild(Node parent, Node currentChild, Node newChild) {

		if (parent.left == currentChild) {
			return SetChild(parent, "left", newChild);
		} else if (parent.right == currentChild) {
			return SetChild(parent, "right", newChild);
		}
		return false;
	}


	//Updates the the height of the tree and its subtrees.
	private void updateHeight(Node cur) {
		int leftHeight = -1;
		int rightHeight = -1;
		if (cur.left != null) {
			leftHeight = cur.left.height;
		}
		if (cur.right != null) {
			rightHeight = cur.right.height;
		}
		cur.height = Math.max(leftHeight, rightHeight) + 1;
	}

	//Deletes and returns the specified node.
	public Node delete(int AccountNumber) {
		Node parent = null;
		Node curr = root;
		while (curr != null && curr.account.accountNumber != AccountNumber) { // search for key
			parent = curr;
			if (AccountNumber < curr.account.accountNumber) {
				curr = curr.left; // left
			} else {
				curr = curr.right; // right
			}
		}
		if (curr.right == null && curr.left == null) { // no children
			if (parent == null) {
				root = null; // delete root
				return null;
			}
			if (parent.left == curr) {
				parent.left = null;
			} else {
				parent.right = null;
			}
		} else if (curr.right != null && curr.left != null) { // two child
			Node successorParent = findSuccessorParentForDeletion(curr);
			if (successorParent == null) {
				
				curr.account = curr.right.account;
				curr.right = curr.right.right;
			} else {
				Node successor = successorParent.left;
				
				curr.account = successor.account;

				if (successor.right != null) { // Successor can only have a right child or else it wouldn't be a
					// successor.
					successorParent.left = successor.right;
				} else {
					successorParent.left = null; // successor is right left with no children
				}
			}
		} else { // one children
			if (parent == null) { // delete node is root with one child
				if (root.left != null) {
					root = root.left;
				} else {
					root = root.right;
				}
			} else if (curr.right != null) { // deleted node has right child
				if (parent.right == curr) { // deleted node is a right child
					parent.right = curr.right;
					curr = null;
				} else { // deleted node is a left child
					parent.left = curr.right;
					curr = null;
				}
			} else { // deleted node has a left child
				if (parent.right == curr) { // deleted node is a right child
					parent.right = curr.left;
				} else { // deleted node is a left child
					parent.left = curr.left;
				}
			}
		}
		// Now that the node is deleted we need to retrace up the tree to balance the
		// tree

		while (parent != null) {
			balance(parent);
			parent = parent.parent; // balancing each parent until at the root.

		}
		return curr; // returns the deleted node
	}

	private Node findSuccessorParentForDeletion(Node curr) {
		Node successParent = null;
		curr = curr.right;
		while (curr.left != null) {
			successParent = curr;
			curr = curr.left;
		}
		return successParent;
	}

	//Creates a new account (customer) node and inserts it into the tree.
	public BankAccount newAccount() {
		System.out.print("please enter your first name: ");
		// some reason nextLine doesn't work on the second attempt to make an account
		String name = input.next();

		System.out.print("Enter the 4 digit PIN you would like to use: ");
		Integer PIN = 0;
		while(PIN.toString().length() != 4) {					// ensures that the pin is a integer and 4 digits
			while(!input.hasNextInt()) {
				System.out.println("Error: please enter a 4 digit pin: ");	
				input.next();
			}
			PIN = input.nextInt();
			if(PIN.toString().length() != 4) {
				System.out.print("Error: PIN must be 4 digits "
						+ "\n please enter a 4 digit pin:");
			}
		}
		System.out.print("What will your inital deposit be?: $");
		while(!input.hasNextInt())
		{
			System.out.println("Error: Please enter a valid dollar amount: ");
			input.next();
		}
		int initialDeposit = input.nextInt();
		BankAccount newUser = new BankAccount(name, PIN, initialDeposit);
		Node accountNode = new Node(newUser);
		this.Insert(accountNode);

		saveAccountToFile(newUser);
		return newUser;
	}

	//Sub-menu that allows the user to select/perform a specific financial transaction.
	public void transaction() {

		BankAccount currentAccount = findAccount();
		System.out.println("1. Deposit cash");
		System.out.println("2. Withdrawl cash");
		System.out.println("3. Transfer funds to another account");
		System.out.println("4. View account balances");
		System.out.print("     Enter your choice: ");
		int choice = input.nextInt();
		switch (choice) {
		case 1:

			System.out.print("How much cash would you like to deposit?: ");
			while(!input.hasNextInt()) {
				System.out.println("Error: Please enter a valid dollar amount: ");
				input.next();
			}
			int depositAmount = input.nextInt();
			currentAccount.deposit(depositAmount);
			break;
		case 2:

			System.out.print("How much cash would you like to withdrawl? (Notice: must be an increment of $20 and no greater than $500): ");
			while(!input.hasNextInt()) {
				System.out.println("Error: Please enter a valid dollar amount: ");
				input.next();
			}
			int withdrawlAmount = input.nextInt(); // check for overdraft fee
			
			while(withdrawlAmount % 20 !=0 || withdrawlAmount > 500) {
				System.out.println("\nError: Amount must be an increment of 20 and cannot exceed $500!: ");
				withdrawlAmount = input.nextInt();
			}
			currentAccount.withdraw(withdrawlAmount);

			break;
		case 3:
			int choice2 = 0;
			System.out.print("Enter the account number you would like to transfer money to: ");
			Node TransfertoNode = this.find(input.nextInt());
			while (TransfertoNode == null) {

				System.out.println("could not find an account with that account number\n "
						+ "make sure you are entering the correct account number and try again");
				System.out.print("Please Enter your bank account number: ");
				TransfertoNode = this.find(input.nextInt());
			}
			BankAccount TransferToAccount = TransfertoNode.account;
			System.out.print("how much money would you like to transfer to this account: $");
			while(!input.hasNextInt()) {
				System.out.println("Error: Please enter a valid dollar amount: ");
				input.next();
			}
			int transferAmmount = input.nextInt();
			System.out.println("Which account would you like to transfer funds to?: ");
			currentAccount.withdraw(transferAmmount);
			System.out.println("1. Checking");
			System.out.println("2. Savings");

			switch(choice2) {

			case 1:

				TransferToAccount.checkingBalance += transferAmmount;
				break;

			case 2:
				TransferToAccount.savingsBalance += transferAmmount;

			}

			break;
		case 4:
			System.out.println("\n" + currentAccount.name + ", your current account balances are: ");
			System.out.println("Checking Balance: $" + currentAccount.checkingBalance);
			System.out.println("Savings Balance: $" + currentAccount.savingsBalance);
		}
	}

	//Utility function that locates and returns a specific account by the accounts number.
	private Node find(int accountNumber) {
		Node curr = root;
		while (curr != null && accountNumber != curr.account.accountNumber) {
			if (accountNumber < curr.account.accountNumber) {
				curr = curr.left; // search left
			} else if (accountNumber > curr.account.accountNumber) {
				curr = curr.right; // search right
			}
			if (curr == null) {
				return null;
			}
		}
		return curr;
	}

	//Allows the user to update specific bank account information.
	public BankAccount changeInfo() {
		BankAccount userAccount = findAccount();
		System.out.println("what do you want to change?");
		System.out.println("1. Change name on the account");
		System.out.println("2. Change the PIN");
		System.out.println("3. Get a new Bank account number");
		System.out.print("     Enter your choice: ");
		int choice = input.nextInt();
		switch (choice) {
		case 1:
			System.out.println("\nWhat name would you like for your new account: ");
			String newName = input.nextLine();
			userAccount.name = newName;
			System.out.println("The name on your account has been changed to -- " + newName + " --");
			break;
		case 2:
			System.out.print("What PIN would you like for your new account: ");
			Integer PIN = input.nextInt();
			while (PIN.toString().length() != 4) {
				System.out.print("That pin is not 4 digits, try again: ");
				PIN = input.nextInt();
			}
			userAccount.pin = PIN;
			System.out.println("The PIN on your account has been changed to -- " + PIN + " --");
			break;
		case 3:
			System.out.print("Are you sure you'd like to change your bank account number (Y/N): ");
			if (input.next().toLowerCase().equals("y")) {
				this.delete(userAccount.accountNumber); // since the account number is changing we have to remove it
				// from the tree and then reinsert it.
				userAccount.accountNumber = userAccount.newAccountNumber();
				System.out.println("Your new account number is: " + userAccount.accountNumber);
				this.Insert(new Node(userAccount));
			}

			break;
		}
		return userAccount;
	}

	//Locates an account utilizing the find function.
	public BankAccount findAccount() {
		System.out.print("Please Enter your bank account number: ");
		Node currentNode = this.find(input.nextInt());
		System.out.print("Enter your pin for this account: ");
		int PIN = input.nextInt();
		while (currentNode == null || PIN != currentNode.account.pin) {
			System.out.println("could not find an account with that account and PIN number\n "
					+ "make sure you are entering the correct account and PIN number and try again");
			System.out.print("Please Enter your bank account number: ");
			currentNode = this.find(input.nextInt());
			System.out.print("Please Enter your PIN number: ");
			PIN = input.nextInt();
		}
		BankAccount currentAccount = currentNode.account;
		System.out.println();
		return currentAccount;
	}
	
	//Prints the existing accounts (in order).
	private void inOrder(Node curr) {
		if (curr == null) {
			return;
		}
		inOrder(curr.left);
		System.out.println(curr.account.name + "'s balances are: ");
		System.out.println("Checking balance: $" + curr.account.checkingBalance);
		System.out.println("Savings balance: $" + curr.account.savingsBalance);
		inOrder(curr.right);
	}

	public void inOrder() {
		if (root != null) {
			inOrder(root);
		}
	}


	//Save all the files when the system exits.
	public void saveAccountsToFile() {	
		this.clearFile();
		saveAccountsToFileRec(root);
	}

	public void saveAccountsToFileRec(Node node) {
		if (node == null) {
			return;
		}
		saveAccountsToFileRec(node.left);
		saveAccountToFile(node.account);
		saveAccountsToFileRec(node.right);		
	}

	//Deletes a specific account from the tree.
	public void deleteAccount() {
		this.delete(findAccount().accountNumber);
	}
	public void saveAccountToFile(BankAccount account) {

		File file = new File("Accounts.txt");

		try {
			FileWriter writer = new FileWriter(file.getAbsoluteFile(), true);
			writer.write( account.name + " Savings:$" + account.savingsBalance + " Checkings:$" + 
					account.checkingBalance+ " AccountNumber:"+account.accountNumber+" PIN:" +account.pin + "\n");
			writer.close();
		} catch (IOException e) {

		}
	}
	public void enterAccountsFromFile() {
		File file = new File("Accounts.txt");
		try {
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextLine()) {
				String nextAccount = scanner.nextLine();
				String[] parts = nextAccount.split(" ");
				String name = parts[0];
				String savings = parts[1].substring(9 ,parts[1].length());;
				String checking = parts[2].substring(11 ,parts[2].length());;
				String accountNumber = parts[3].substring(14, parts[3].length());;
				String PIN = parts[4];
				PIN = PIN.substring(4, 8);

				this.Insert(new Node(new BankAccount(name,Integer.parseInt(PIN),
						Integer.parseInt(accountNumber),Integer.parseInt(checking), 
						Integer.parseInt(savings))));
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("could not load bank accounts");
		}

	}
	public void clearFile(){
		File file = new File("Accounts.txt");
		try {
			FileWriter writer = new FileWriter(file);
			writer.close();
		} catch (IOException e) {
		}
	}
}
