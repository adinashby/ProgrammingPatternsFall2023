# 1. Searching Algorithms

## 1.1. Sequential Search

Sequential search is a basic form of searching that checks if an element is present in a given list. This method will return a value telling us whether or not the searched value is in the given list.

```java
public class SequentialSearch {

	public static void main(String[] args) {
		int[] exampleVariableOne = { 2, 9, 6, 7, 4, 5, 3, 0, 1 };
		int target = 4;

		sequentialSearch(exampleVariableOne, target);
	}

	public static void sequentialSearch(int[] parameterOne, int parameterTwo) {
		int index = -1;
		// searches each index of the array until it reaches the last index
		for (int i = 0; i < parameterOne.length; i++) {
			if (parameterOne[i] == parameterTwo) {
				// if the target is found, int index is set as the value of i and
				// the for loop is terminated
				index = i;
				break;
			}
		}

		if (index == -1) {
			System.out.println("Your target integer does not exist in the array");
		} else {
			System.out.println("Your target integer is in index " + index + " of the array");
		}
	}
}
```

## 1.2. Binary Search

Sequential search can take an excessively long time since they look through every element in an array. This is like flipping through each page of the dictionary from the beginning even if we are trying to find the word "zero". Let's introduce the concept of binary search, which is similar to flipping right to the 'Z' section of the dictionary to start looking for the word "zero". If an array or ArrayList is ordered, the binary search method can be used. Look below and review the functionality of a binary search:

```java
public class BinarySearch {

	public static void main(String[] args) {
		// the array has to be sorted before binary search
		int[] exampleVariableOne = { 1, 11, 24, 34, 67, 89, 102 };
		int target = 102;

		binarySearch(exampleVariableOne, target);
	}

	public static void binarySearch(int[] parameterOne, int parameterTwo) {
		int index = -1;
		int lowEnd = 0;
		int highEnd = parameterOne.length - 1;

		while (highEnd >= lowEnd) { // Difference of highEnd and lowEnd decreases as the search range narrows
			int middle = (lowEnd + highEnd) / 2;
			// checks if the middle of the lowEnd and the highEnd is the target
			if (parameterOne[middle] == parameterTwo) {
				index = middle; // the target is found
				break;
			} else if (parameterOne[middle] < parameterTwo) {
				// changes the lowEnd to narrow the search range
				lowEnd = middle + 1;
			} else if (parameterOne[middle] > parameterTwo) {
				// changes the highEnd to narrow the search range
				highEnd = middle - 1;
			}
		}

		if (index == -1) {
			System.out.println("Your target integer does not exist in the array");
		} else {
			System.out.println("Your target integer is in index " + index + " of the array");
		}
	}
}
```

## 1.3. Binary Search Tree (BST)

A binary Search Tree is a node-based binary tree data structure which has the following properties:

- The left subtree of a node contains only nodes with keys lesser than the node’s key.
- The right subtree of a node contains only nodes with keys greater than the node’s key.
- The left and right subtree each must also be a binary search tree. There must be no duplicate nodes.

```java
public class BinarySearchTree {

	/*
	 * Class containing left and right child of current node and key value
	 */
	class Node {
		int key;
		Node left, right;

		public Node(int item) {
			key = item;
			left = right = null;
		}
	}

	// Root of BST
	Node root;

	// Constructor
	BinarySearchTree() {
		root = null;
	}

	BinarySearchTree(int value) {
		root = new Node(value);
	}

	// This method mainly calls insertRec()
	void insert(int key) {
		root = insertRec(root, key);
	}

	/*
	 * A recursive function to insert a new key in BST
	 */
	Node insertRec(Node root, int key) {

		/*
		 * If the tree is empty, return a new node
		 */
		if (root == null) {
			root = new Node(key);
			return root;
		}

		/* Otherwise, recur down the tree */
		else if (key < root.key)
			root.left = insertRec(root.left, key);
		else if (key > root.key)
			root.right = insertRec(root.right, key);

		/* return the (unchanged) node pointer */
		return root;
	}

	// This method mainly calls InorderRec()
	void inorder() {
		inorderRec(root);
	}

	// A utility function to
	// do inorder traversal of BST
	void inorderRec(Node root) {
		if (root != null) {
			inorderRec(root.left);
			System.out.println(root.key);
			inorderRec(root.right);
		}
	}

	// Driver Code
	public static void main(String[] args) {
		BinarySearchTree tree = new BinarySearchTree();

		/*
		 * Let us create following BST 50 / \ 30 70 / \ / \ 20 40 60 80
		 */
		tree.insert(50);
		tree.insert(30);
		tree.insert(20);
		tree.insert(40);
		tree.insert(70);
		tree.insert(60);
		tree.insert(80);

		// print inorder traversal of the BST
		tree.inorder();
	}
}
```

## 1.4. AVL Tree

AVL tree is a self-balancing Binary Search Tree (BST) where the difference between heights of left and right subtrees cannot be more than one for all nodes.

### 1.4.1. Example of AVL Tree

![](Imgs/../../Imgs/week_4_AVL_Tree_1.jpg)

The above tree is AVL because the differences between heights of left and right subtrees for every node are less than or equal to 1.

### 1.4.2. Example of a Tree that is NOT an AVL Tree

![](Imgs/../../Imgs/week_4_AVL_Tree_2.jpg)

The above tree is not AVL because the differences between the heights of the left and right subtrees for 8 and 12 are greater than 1.

```java
class Node {
	int key, height;
	Node left, right;

	Node(int d) {
		key = d;
		height = 1;
	}
}

class AVLTree {

	Node root;

	// A utility function to get the height of the tree
	int height(Node N) {
		if (N == null)
			return 0;

		return N.height;
	}

	// A utility function to get maximum of two integers
	int max(int a, int b) {
		return (a > b) ? a : b;
	}

	// A utility function to right rotate subtree rooted with y
	// See the diagram given above.
	Node rightRotate(Node y) {
		Node x = y.left;
		Node T2 = x.right;

		// Perform rotation
		x.right = y;
		y.left = T2;

		// Update heights
		y.height = max(height(y.left), height(y.right)) + 1;
		x.height = max(height(x.left), height(x.right)) + 1;

		// Return new root
		return x;
	}

	// A utility function to left rotate subtree rooted with x
	// See the diagram given above.
	Node leftRotate(Node x) {
		Node y = x.right;
		Node T2 = y.left;

		// Perform rotation
		y.left = x;
		x.right = T2;

		// Update heights
		x.height = max(height(x.left), height(x.right)) + 1;
		y.height = max(height(y.left), height(y.right)) + 1;

		// Return new root
		return y;
	}

	// Get Balance factor of node N
	int getBalance(Node N) {
		if (N == null)
			return 0;

		return height(N.left) - height(N.right);
	}

	Node insert(Node node, int key) {

		/* 1. Perform the normal BST insertion */
		if (node == null)
			return (new Node(key));

		if (key < node.key)
			node.left = insert(node.left, key);
		else if (key > node.key)
			node.right = insert(node.right, key);
		else // Duplicate keys not allowed
			return node;

		/* 2. Update height of this ancestor node */
		node.height = 1 + max(height(node.left), height(node.right));

		/*
		 * 3. Get the balance factor of this ancestor node to check whether this node
		 * became unbalanced
		 */
		int balance = getBalance(node);

		// If this node becomes unbalanced, then there
		// are 4 cases Left Left Case
		if (balance > 1 && key < node.left.key)
			return rightRotate(node);

		// Right Right Case
		if (balance < -1 && key > node.right.key)
			return leftRotate(node);

		// Left Right Case
		if (balance > 1 && key > node.left.key) {
			node.left = leftRotate(node.left);
			return rightRotate(node);
		}

		// Right Left Case
		if (balance < -1 && key < node.right.key) {
			node.right = rightRotate(node.right);
			return leftRotate(node);
		}

		/* return the (unchanged) node pointer */
		return node;
	}

	// A utility function to print preorder traversal
	// of the tree.
	// The function also prints height of every node
	void preOrder(Node node) {
		if (node != null) {
			System.out.print(node.key + " ");
			preOrder(node.left);
			preOrder(node.right);
		}
	}

	public static void main(String[] args) {
		AVLTree tree = new AVLTree();

		/* Constructing tree given in the above figure */
		tree.root = tree.insert(tree.root, 10);
		tree.root = tree.insert(tree.root, 20);
		tree.root = tree.insert(tree.root, 30);
		tree.root = tree.insert(tree.root, 40);
		tree.root = tree.insert(tree.root, 50);
		tree.root = tree.insert(tree.root, 25);

		/*
		 * The constructed AVL Tree would be 30 / \ 20 40 / \ \ 10 25 50
		 */
		System.out.println("Preorder traversal" + " of constructed tree is : ");
		tree.preOrder(tree.root);
	}
}
```
