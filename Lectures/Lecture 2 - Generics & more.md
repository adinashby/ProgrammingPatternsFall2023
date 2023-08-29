# 1. Generics

In any nontrivial software project, bugs are simply a fact of life. Careful planning, programming, and testing can help reduce their pervasiveness, but somehow, somewhere, they'll always find a way to creep into your code. This becomes especially apparent as new features are introduced and your code base grows in size and complexity.

Fortunately, some bugs are easier to detect than others. Compile-time bugs, for example, can be detected early on; you can use the compiler's error messages to figure out what the problem is and fix it, right then and there. Runtime bugs, however, can be much more problematic; they don't always surface immediately, and when they do, it may be at a point in the program that is far removed from the actual cause of the problem.

Generics add stability to your code by making more of your bugs detectable at compile time.

## 1.1. Why use Generics?

In a nutshell, generics enable types (classes and interfaces) to be parameters when defining classes, interfaces and methods. Much like the more familiar formal parameters used in method declarations, type parameters provide a way for you to re-use the same code with different inputs. The difference is that **the inputs to formal parameters are values, while the inputs to type parameters are types.**

Code that uses generics has many benefits over non-generic code:

- **Stronger type checks at compile time.**

A Java compiler applies strong type checking to generic code and issues errors if the code violates type safety. Fixing compile-time errors is easier than fixing runtime errors, which can be difficult to find.

- **Elimination of casts.**

The following code snippet without generics requires casting:

```java
List list = new ArrayList();
list.add("hello");
String s = (String) list.get(0);
```

When re-written to use generics, the code does not require casting:

```java
List<String> list = new ArrayList<String>();
list.add("hello");
String s = list.get(0); // no cast
```

- **Enabling programmers to implement generic algorithms.**

By using generics, programmers can implement generic algorithms that work on collections of different types, can be customized, and are type safe and easier to read.

## 1.2. Generic Types

A generic type is a generic class or interface that is parameterized over types. The following Box class will be modified to demonstrate the concept.

### 1.2.1. A Simple Box Class

Begin by examining a non-generic Box class that operates on objects of any type. It needs only to provide two methods: set, which adds an object to the box, and get, which retrieves it:

```java
public class Box {
    private Object object;

    public void set(Object object) { this.object = object; }
    public Object get() { return object; }
}
```

Since its methods accept or return an Object, you are free to pass in whatever you want, provided that it is not one of the primitive types. There is no way to verify, at compile time, how the class is used. One part of the code may place an Integer in the box and expect to get Integers out of it, while another part of the code may mistakenly pass in a String, resulting in a runtime error.

### 1.2.2. A Generic Version of the Box Class

A generic class is defined with the following format:

```java
class name<T1, T2, ..., Tn> { /_ ... _/ }
```

The type parameter section, delimited by angle brackets (<>), follows the class name. It specifies the type parameters (also called type variables) T1, T2, ..., and Tn.

To update the Box class to use generics, you create a generic type declaration by changing the code "public class Box" to "public class Box<T>". This introduces the type variable, T, that can be used anywhere inside the class.

With this change, the Box class becomes:

```java
\**
  * Generic version of the Box class.
  * @param <T> the type of the value being boxed
  */
  public class Box<T> {
      // T stands for "Type"
      private T t;

      public void set(T t) { this.t = t; }
      public T get() { return t; }

  }
```

As you can see, all occurrences of Object are replaced by T. A type variable can be any **non-primitive** type you specify: any class type, any interface type, any array type, or even another type variable.

This same technique can be applied to create generic interfaces.

### 1.2.3. Type Parameter Naming Conventions

By convention, type parameter names are single, uppercase letters. This stands in sharp contrast to the variable naming conventions that you already know about, and with good reason: Without this convention, it would be difficult to tell the difference between a type variable and an ordinary class or interface name.

The most commonly used type parameter names are:

- E - Element (used extensively by the Java Collections Framework)
- K - Key
- N - Number
- T - Type
- V - Value
- S,U,V etc. - 2nd, 3rd, 4th types

You'll see these names used throughout the Java SE API and the rest of this lesson.

### 1.2.4. Invoking and Instantiating a Generic Type

To reference the generic Box class from within your code, you must perform a generic type invocation, which replaces T with some concrete value, such as Integer:

```java
Box<Integer> integerBox;
```

You can think of a generic type invocation as being similar to an ordinary method invocation, but instead of passing an argument to a method, you are passing a type argument — Integer in this case — to the Box class itself.

Like any other variable declaration, this code does not actually create a new Box object. It simply declares that integerBox will hold a reference to a "Box of Integer", which is how Box<Integer> is read.

An invocation of a generic type is generally known as **a parameterized type**.

To instantiate this class, use the new keyword, as usual, but place <Integer> between the class name and the parenthesis:

```java
Box<Integer> integerBox = new Box<Integer>();
```

### 1.2.5. The Diamond

In Java SE 7 and later, you can replace the type arguments required to invoke the constructor of a generic class with an empty set of type arguments (<>) as long as the compiler can determine, or infer, the type arguments from the context. This pair of angle brackets, <>, is informally called the diamond. For example, you can create an instance of Box<Integer> with the following statement:

```java
Box<Integer> integerBox = new Box<>();
```

### 1.2.6. Multiple Type Parameters

As mentioned previously, a generic class can have multiple type parameters. For example, the generic OrderedPair class, which implements the generic Pair interface:

```java
public interface Pair<K, V> {
    public K getKey();
    public V getValue();
}

public class OrderedPair<K, V> implements Pair<K, V> {

    private K key;
    private V value;

    public OrderedPair(K key, V value) {
    this.key = key;
    this.value = value;
    }

    public K getKey()	{ return key; }
    public V getValue() { return value; }

}
```

The following statements create two instantiations of the OrderedPair class:

```java
Pair<String, Integer> p1 = new OrderedPair<String, Integer>("Even", 8);
Pair<String, String> p2 = new OrderedPair<String, String>("hello", "world");
```

The code, new OrderedPair<String, Integer>, instantiates K as a String and V as an Integer. Therefore, the parameter types of OrderedPair's constructor are String and Integer, respectively. Due to autoboxing, it is valid to pass a String and an int to the class.

As mentioned in The Diamond, because a Java compiler can infer the K and V types from the declaration OrderedPair<String, Integer>, these statements can be shortened using diamond notation:

```java
OrderedPair<String, Integer> p1 = new OrderedPair<>("Even", 8);
OrderedPair<String, String> p2 = new OrderedPair<>("hello", "world");
```

To create a generic interface, follow the same conventions as for creating a generic class.

### 1.2.7.Parameterized Types

You can also substitute a type parameter (that is, K or V) with a parameterized type (that is, List<String>). For example, using the OrderedPair<K, V> example:

```java
OrderedPair<String, Box<Integer>> p = new OrderedPair<>("primes", new Box<Integer>(...));
```

## 1.3. Raw Types

A raw type is the name of a generic class or interface without any type arguments. For example, given the generic Box class:

```java
public class Box<T> {
    public void set(T t) { /* ... */ }
    // ...
}
```

To create a parameterized type of Box<T>, you supply an actual type argument for the formal type parameter T:

```java
Box<Integer> intBox = new Box<>();
```

If the actual type argument is omitted, you create a raw type of Box<T>:

```java
Box rawBox = new Box();
```

Therefore, Box is the raw type of the generic type Box<T>. However, **a non-generic class or interface type is not a raw type**.

## 1.4. Generic Methods

Generic methods are methods that introduce their own type parameters. This is similar to declaring a generic type, but the type parameter's scope is limited to the method where it is declared. Static and non-static generic methods are allowed, as well as generic class constructors.

The syntax for a generic method includes a list of type parameters, inside angle brackets, which appears before the method's return type. For static generic methods, the type parameter section must appear before the method's return type.

The Util class includes a generic method, compare, which compares two Pair objects:

```java
public class Util {
    public static <K, V> boolean compare(Pair<K, V> p1, Pair<K, V> p2) {
        return p1.getKey().equals(p2.getKey()) &&
               p1.getValue().equals(p2.getValue());
    }
}

public class Pair<K, V> {

    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public void setKey(K key) { this.key = key; }
    public void setValue(V value) { this.value = value; }
    public K getKey()   { return key; }
    public V getValue() { return value; }
}
```

The complete syntax for invoking this method would be:

```java
Pair<Integer, String> p1 = new Pair<>(1, "apple");
Pair<Integer, String> p2 = new Pair<>(2, "pear");
boolean same = Util.<Integer, String>compare(p1, p2);
```

The type has been explicitly provided, as shown in bold. Generally, this can be left out and the compiler will infer the type that is needed:

```java
Pair<Integer, String> p1 = new Pair<>(1, "apple");
Pair<Integer, String> p2 = new Pair<>(2, "pear");
boolean same = Util.compare(p1, p2);
```

This feature, known as type inference, allows you to invoke a generic method as an ordinary method, without specifying a type between angle brackets.

# 2. ArrayList (Implementation)

## 2.1. Without using Generics

```java
	private Object[] myStore;
	private int actSize = 0;

	public MyArrayList() {
		myStore = new Object[10];
	}

	public Object get(int index) {
		if (index < actSize) {
			return myStore[index];
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}
	}

	public void add(Object obj) {
		if (myStore.length - actSize <= 5) {
			increaseListSize();
		}

		myStore[actSize++] = obj;
	}

	public Object remove(int index) {
		if (index < actSize) {
			Object obj = myStore[index];
			myStore[index] = null;
			int tmp = index;

			while (tmp < actSize) {
				myStore[tmp] = myStore[tmp + 1];
				myStore[tmp + 1] = null;
				tmp++;
			}

			actSize--;
			return obj;
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}

	}

	public int size() {
		return actSize;
	}

	private void increaseListSize() {
		myStore = Arrays.copyOf(myStore, myStore.length * 2);
		System.out.println("\nNew length: " + myStore.length);
	}

	public static void main(String a[]) {
		MyArrayList mal = new MyArrayList();

		mal.add(new Integer(2));
		mal.add(new Integer(5));
		mal.add(new Integer(1));
		mal.add(new Integer(23));
		mal.add(new Integer(14));

		for (int i = 0; i < mal.size(); i++) {
			System.out.print(mal.get(i) + " ");
		}

		mal.add(new Integer(29));

		System.out.println("Element at Index 5: " + mal.get(5));
		System.out.println("List size: " + mal.size());
		System.out.println("Removing element at index 2: " + mal.remove(2));

		for (int i = 0; i < mal.size(); i++) {
			System.out.print(mal.get(i) + " ");
		}
	}
```

## 2.2. With using Generics

```java
import java.util.Arrays;

public class MyArrayList<E> {

	private Object[] myStore;
	private int actSize = 0;

	public MyArrayList() {
		myStore = new Object[10];
	}

	@SuppressWarnings("unchecked")
	public E get(int index) {
		if (index < actSize) {
			return (E) myStore[index];
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}
	}

	public void add(E e) {
		if (myStore.length - actSize <= 5) {
			increaseListSize();
		}

		myStore[actSize++] = e;
	}

	public E remove(int index) {
		if (index < actSize) {
			E e = (E) myStore[index];
			myStore[index] = null;
			int tmp = index;

			while (tmp < actSize) {
				myStore[tmp] = myStore[tmp + 1];
				myStore[tmp + 1] = null;
				tmp++;
			}

			actSize--;
			return e;
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}

	}

	public int size() {
		return actSize;
	}

	private void increaseListSize() {
		myStore = Arrays.copyOf(myStore, myStore.length * 2);
		System.out.println("\nNew length: " + myStore.length);
	}

	public static void main(String a[]) {
		MyArrayList<Integer> mal = new MyArrayList<>();

		mal.add(new Integer(2));
		mal.add(new Integer(5));
		mal.add(new Integer(1));
		mal.add(new Integer(23));
		mal.add(new Integer(14));

		for (int i = 0; i < mal.size(); i++) {
			System.out.print(mal.get(i) + " ");
		}

		mal.add(new Integer(29));

		System.out.println("Element at Index 5: " + mal.get(5));
		System.out.println("List size: " + mal.size());
		System.out.println("Removing element at index 2: " + mal.remove(2));

		for (int i = 0; i < mal.size(); i++) {
			System.out.print(mal.get(i) + " ");
		}
	}
}
```

# 3. LinkedList (Implementation)

## 3.1. Without using Generics

```java

public class LinkedList {
	Node head;

	class Node {
		int data;
		Node next;
	}

	public void insert(int data) {
		Node node = new Node();

		node.data = data;
		node.next = null;

		if (head == null) {
			head = node;
		} else {
			Node n = head;

			while (n.next != null) {
				n = n.next;
			}

			n.next = node;
		}

	}

	public void insertAtStart(int data) {
		Node node = new Node();

		node.data = data;
		node.next = null;
		node.next = head;

		head = node;
	}

	public void insertAt(int index, int data) {
		Node node = new Node();

		node.data = data;
		node.next = null;

		if (index == 0) {
			insertAtStart(data);
		} else {
			Node n = head;

			for (int i = 0; i < index - 1; i++) {
				n = n.next;
			}

			node.next = n.next;
			n.next = node;
		}
	}

	public void deleteAt(int index) {
		if (index == 0) {
			head = head.next;
		} else {
			Node n = head;
			// Node n1 = null;

			for (int i = 0; i < index - 1; i++) {
				n = n.next;
			}
      n.next = n.next.next;
			// n1 = n.next;
			// n.next = n1.next;
			// // System.out.println("n1 " + n1.data);
			// n1 = null;
		}
	}

	public void show() {
		Node node = head;

		while (node.next != null) {
			System.out.println(node.data);
			node = node.next;
		}

		System.out.println(node.data);
	}
}
```

```java
public class Driver {

	public static void main(String[] args) {

		LinkedList list = new LinkedList();

		list.insert(18);
		list.insert(45);
		list.insert(12);
		list.insertAtStart(25);

		list.insertAt(0, 55);

		list.deleteAt(2);

		list.show();
	}

}
```

# 4. Stack (Implementation)

## 4.1. Without using Generics

```java
class Stack {
	private int arr[];
	private int top;
	private int capacity;

	// Constructor to initialize the stack
	Stack(int size) {
		arr = new int[size];
		capacity = size;
		top = -1;
	}

	// Utility function to add an element `x` to the stack
	public void push(int x) {
		if (isFull()) {
			System.out.println("Overflow\nProgram Terminated\n");
			System.exit(-1);
		}

		System.out.println("Inserting " + x);
		arr[++top] = x;
	}

	// Utility function to pop a top element from the stack
	public int pop() {
		// check for stack underflow
		if (isEmpty()) {
			System.out.println("Underflow\nProgram Terminated");
			System.exit(-1);
		}

		System.out.println("Removing " + peek());

		// decrease stack size by 1 and (optionally) return the popped element
		return arr[top--];
	}

	// Utility function to return the top element of the stack
	public int peek() {
		if (!isEmpty()) {
			return arr[top];
		} else {
			System.exit(-1);
		}

		return -1;
	}

	// Utility function to return the size of the stack
	public int size() {
		return top + 1;
	}

	// Utility function to check if the stack is empty or not
	public boolean isEmpty() {
		return top == -1; // or return size() == 0;
	}

	// Utility function to check if the stack is full or not
	public boolean isFull() {
		return top == capacity - 1; // or return size() == capacity;
	}
}
```

```java
public class Driver {

	public static void main(String[] args) {

        Stack stack = new Stack(3);

        stack.push(1);      // inserting 1 in the stack
        stack.push(2);      // inserting 2 in the stack

        stack.pop();        // removing the top element (2)
        stack.pop();        // removing the top element (1)

        stack.push(3);      // inserting 3 in the stack

        System.out.println("The top element is " + stack.peek());
        System.out.println("The stack size is " + stack.size());

        stack.pop();        // removing the top element (3)

        // check if the stack is empty
        if (stack.isEmpty()) {
            System.out.println("The stack is empty");
        }
        else {
            System.out.println("The stack is not empty");
        }
	}

}
```

# 5. Queue (Implementation)

## 5.1. Without using Generics

```java
// A class to represent a queue
class Queue {
	private int[] arr; // array to store queue elements
	private int front; // front points to the front element in the queue
	private int rear; // rear points to the last element in the queue
	private int capacity; // maximum capacity of the queue
	private int count; // current size of the queue

	// Constructor to initialize a queue
	Queue(int size) {
		arr = new int[size];
		capacity = size;
		front = 0;
		rear = -1;
		count = 0;
	}

	// Utility function to dequeue the front element
	public int dequeue() {
		// check for queue underflow
		if (isEmpty()) {
			System.out.println("Underflow\nProgram Terminated");
			System.exit(-1);
		}

		int x = arr[front];

		System.out.println("Removing " + x);

		front = (front + 1) % capacity;
		count--;

		return x;
	}

	// Utility function to add an item to the queue
	public void enqueue(int item) {
		// check for queue overflow
		if (isFull()) {
			System.out.println("Overflow\nProgram Terminated");
			System.exit(-1);
		}

		System.out.println("Inserting " + item);

		rear = (rear + 1) % capacity;
		arr[rear] = item;
		count++;
	}

	// Utility function to return the front element of the queue
	public int peek() {
		if (isEmpty()) {
			System.out.println("Underflow\nProgram Terminated");
			System.exit(-1);
		}
		return arr[front];
	}

	// Utility function to return the size of the queue
	public int size() {
		return count;
	}

	// Utility function to check if the queue is empty or not
	public boolean isEmpty() {
		return (size() == 0);
	}

	// Utility function to check if the queue is full or not
	public boolean isFull() {
		return (size() == capacity);
	}
}

```

```java
public class Driver {

	public static void main(String[] args) {

		// create a queue of capacity 5
		Queue q = new Queue(5);

		q.enqueue(1);
		q.enqueue(2);
		q.enqueue(3);

		System.out.println("The front element is " + q.peek());
		q.dequeue();
		System.out.println("The front element is " + q.peek());

		System.out.println("The queue size is " + q.size());

		q.dequeue();
		q.dequeue();

		if (q.isEmpty()) {
			System.out.println("The queue is empty");
		} else {
			System.out.println("The queue is not empty");
		}
	}

}
```
