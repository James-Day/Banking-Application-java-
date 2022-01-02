//Bryant Hays & James Day
//06/08/2021
//Professor Goyal
//CS 244 Advanced Data Structures in Java

//Node Class
package bankingManagement;

public class Node {

	//Variables
	BankAccount account;
	Node left, right, parent;
	int height;

	//Default constructor
	public Node(BankAccount user) {
		account = user;
		left = null;
		right = null;
		height = 0;
	}
}
