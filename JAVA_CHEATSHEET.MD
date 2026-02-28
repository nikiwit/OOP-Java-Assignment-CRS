# Java Cheatsheet

## Table of Contents
- [Key Terms](#key-terms)
- [Visibility Modifiers](#visibility-modifiers)
- [Data Types](#data-types)
- [Basic Class Structure](#basic-class-structure)
- [Keywords and Modifiers](#keywords-and-modifiers)
- [Control Flow](#control-flow)
- [Arrays](#arrays)
- [String Methods](#string-methods)
- [Object-Oriented Programming](#object-oriented-programming)
- [Constructors](#constructors)
- [Static vs Instance](#static-vs-instance)
- [Exception Handling](#exception-handling)
- [Input/Output](#inputoutput)
- [Common Patterns](#common-patterns)
- [Quick Reference: Operators](#quick-reference-operators)
- [Naming Conventions](#naming-conventions)
- [Common Mistakes to Avoid](#common-mistakes-to-avoid)
- [Quick Syntax Reference](#quick-syntax-reference)

## Key Terms

|Term|Definition|
|---|---|
|**Class**|Blueprint for creating objects containing attributes and methods|
|**Object**|Instance of a class|
|**Method**|Function defined inside a class|
|**Constructor**|Special method called when object is created|
|**Encapsulation**|Hiding data using private access modifiers|
|**Inheritance**|Mechanism where one class inherits from another|
|**Polymorphism**|Ability of objects to take many forms|
|**Static**|Belongs to class rather than instance|
|**Final**|Cannot be changed (constant)|

## Visibility Modifiers

> [!important] Access Modifiers Control the visibility and accessibility of classes, methods, and variables.

### UML Notation

|Symbol|Modifier|Description|
|---|---|---|
|`+`|**public**|Accessible from anywhere|
|`-`|**private**|Only accessible within the same class|
|`#`|**protected**|Accessible within the class and its subclasses|
|`~`|**package** (default)|Accessible within the same package|

### Access Level Table

|Modifier|Same Class|Same Package|Subclass|Anywhere|
|---|---|---|---|---|
|`public`|✓|✓|✓|✓|
|`protected`|✓|✓|✓|✗|
|`package`|✓|✓|✗|✗|
|`private`|✓|✗|✗|✗|

**Example:**

```java
public class Person {
    public String name;          // + accessible anywhere
    protected int age;           // # accessible in subclasses
    String address;              // ~ package-private
    private String password;     // - only within Person class
    
    public void display() {      // + public method
        System.out.println(name);
    }
    
    private void secret() {      // - private method
        System.out.println(password);
    }
}
```

## Data Types

### Primitive Types

|Type|Size|Range|Default|Example|
|---|---|---|---|---|
|`byte`|1 byte|-128 to 127|0|`byte b = 100;`|
|`short`|2 bytes|-32,768 to 32,767|0|`short s = 1000;`|
|`int`|4 bytes|-2³¹ to 2³¹-1|0|`int i = 10000;`|
|`long`|8 bytes|-2⁶³ to 2⁶³-1|0L|`long l = 100000L;`|
|`float`|4 bytes|~6-7 decimal digits|0.0f|`float f = 3.14f;`|
|`double`|8 bytes|~15 decimal digits|0.0d|`double d = 3.14159;`|
|`boolean`|1 bit|true or false|false|`boolean b = true;`|
|`char`|2 bytes|Unicode character|'\u0000'|`char c = 'A';`|

### Reference Types

```java
// String
String name = "John";

// Arrays
int[] numbers = {1, 2, 3, 4, 5};
String[] names = new String[3];

// Objects
Person person = new Person();
```

## Basic Class Structure

```java
public class ClassName {
    // Attributes (Fields)
    private String attribute1;
    private int attribute2;
    
    // Constructor
    public ClassName() {
        this.attribute1 = "default";
        this.attribute2 = 0;
    }
    
    // Parameterized Constructor
    public ClassName(String attr1, int attr2) {
        this.attribute1 = attr1;
        this.attribute2 = attr2;
    }
    
    // Getter
    public String getAttribute1() {
        return attribute1;
    }
    
    // Setter
    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }
    
    // Method
    public void displayInfo() {
        System.out.println(attribute1 + ", " + attribute2);
    }
}
```

## Keywords and Modifiers

### Common Keywords

|Keyword|Purpose|Example|
|---|---|---|
|`class`|Define a class|`class Student { }`|
|`new`|Create new object|`Student s = new Student();`|
|`this`|Refer to current object|`this.name = name;`|
|`super`|Refer to parent class|`super.method();`|
|`static`|Belongs to class, not instance|`static int count;`|
|`final`|Cannot be changed|`final double PI = 3.14;`|
|`void`|Method returns nothing|`public void display()`|
|`return`|Return value from method|`return value;`|
|`extends`|Inherit from class|`class Dog extends Animal`|
|`implements`|Implement interface|`class C implements I`|

### Non-Access Modifiers

| Modifier   | Description                   | Example                       |
| ---------- | ----------------------------- | ----------------------------- |
| `static`   | Belongs to class, not objects | `static int count = 0;`       |
| `final`    | Cannot be modified            | `final String NAME = "Java";` |
| `abstract` | Cannot be instantiated        | `abstract class Shape { }`    |

## Control Flow

### Conditional Statements

```java
// if-else
if (condition) {
    // code
} else if (condition2) {
    // code
} else {
    // code
}

// switch
switch (variable) {
    case value1:
        // code
        break;
    case value2:
        // code
        break;
    default:
        // code
}

// Ternary operator
result = (condition) ? valueIfTrue : valueIfFalse;
```

### Loops

```java
// for loop
for (int i = 0; i < 10; i++) {
    System.out.println(i);
}

// enhanced for loop
for (String item : array) {
    System.out.println(item);
}

// while loop
while (condition) {
    // code
}

// do-while loop
do {
    // code
} while (condition);
```

## Arrays

### Array Declaration and Initialization

```java
// Declaration
int[] numbers;
String[] names;

// Initialization
int[] numbers = new int[5];
String[] names = {"John", "Alice", "Bob"};
int[] scores = {90, 85, 88, 92};

// Access elements
numbers[0] = 10;
String firstName = names[0];

// Array length
int size = numbers.length;

// Loop through array
for (int i = 0; i < numbers.length; i++) {
    System.out.println(numbers[i]);
}
```

### ArrayList

```java
import java.util.ArrayList;

// Create ArrayList
ArrayList<String> list = new ArrayList<>();

// Add elements
list.add("item1");
list.add("item2");

// Get element
String item = list.get(0);

// Remove element
list.remove(0);

// Size
int size = list.size();

// Loop through
for (String s : list) {
    System.out.println(s);
}
```

## String Methods

> [!tip] Common String Operations

|Method|Description|Example|
|---|---|---|
|`length()`|Get string length|`str.length()`|
|`charAt(i)`|Get character at index|`str.charAt(0)`|
|`substring(start, end)`|Get substring|`str.substring(0, 5)`|
|`equals(str)`|Compare strings|`str1.equals(str2)`|
|`equalsIgnoreCase(str)`|Compare ignoring case|`str1.equalsIgnoreCase(str2)`|
|`toLowerCase()`|Convert to lowercase|`str.toLowerCase()`|
|`toUpperCase()`|Convert to uppercase|`str.toUpperCase()`|
|`trim()`|Remove whitespace|`str.trim()`|
|`split(delimiter)`|Split string into array|`str.split(",")`|
|`contains(str)`|Check if contains substring|`str.contains("test")`|
|`replace(old, new)`|Replace characters|`str.replace("a", "b")`|

```java
String text = "Hello World";
int len = text.length();              // 11
char first = text.charAt(0);          // 'H'
String lower = text.toLowerCase();    // "hello world"
String[] words = text.split(" ");     // ["Hello", "World"]
boolean has = text.contains("World"); // true
```

## Object-Oriented Programming

### Encapsulation

```java
public class Student {
    // Private attributes
    private String name;
    private int age;
    
    // Public getter
    public String getName() {
        return name;
    }
    
    // Public setter
    public void setName(String name) {
        this.name = name;
    }
}
```

### Inheritance

```java
// Parent class
class Animal {
    protected String name;
    
    public void eat() {
        System.out.println("Animal is eating");
    }
}

// Child class
class Dog extends Animal {
    public void bark() {
        System.out.println("Dog is barking");
    }
    
    @Override
    public void eat() {
        System.out.println("Dog is eating");
    }
}
```

### Polymorphism

```java
// Method Overloading (Compile-time polymorphism)
class Calculator {
    public int add(int a, int b) {
        return a + b;
    }
    
    public double add(double a, double b) {
        return a + b;
    }
}

// Method Overriding (Runtime polymorphism)
Animal animal = new Dog();
animal.eat(); // Calls Dog's eat() method
```

### Interfaces

```java
interface Drawable {
    void draw();  // abstract method
}

class Circle implements Drawable {
    @Override
    public void draw() {
        System.out.println("Drawing circle");
    }
}
```

## Constructors

> [!note] Constructor Types

```java
public class Person {
    private String name;
    private int age;
    
    // Default constructor
    public Person() {
        this.name = "Unknown";
        this.age = 0;
    }
    
    // Parameterized constructor
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    // Constructor overloading
    public Person(String name) {
        this(name, 0);  // Calls parameterized constructor
    }
}
```

## Static vs Instance

|Aspect|Static|Instance|
|---|---|---|
|**Keyword**|`static`|No keyword|
|**Belongs to**|Class|Object|
|**Access**|`ClassName.member`|`object.member`|
|**Memory**|Single copy|Copy per object|

```java
public class Counter {
    // Static variable - shared by all objects
    static int count = 0;
    
    // Instance variable - unique to each object
    int id;
    
    // Static method
    public static void displayCount() {
        System.out.println(count);
    }
    
    // Instance method
    public void displayId() {
        System.out.println(id);
    }
}

// Usage
Counter.count++;              // Access static variable
Counter.displayCount();       // Access static method

Counter c1 = new Counter();
c1.id = 1;                    // Access instance variable
c1.displayId();               // Access instance method
```

## Exception Handling

### Try-Catch Block

```java
try {
    // Code that may throw exception
    int result = 10 / 0;
} catch (ArithmeticException e) {
    // Handle exception
    System.out.println("Cannot divide by zero");
} finally {
    // Always executes
    System.out.println("Cleanup code");
}
```

### Common Exceptions

|Exception|Cause|
|---|---|
|`NullPointerException`|Accessing null object|
|`ArrayIndexOutOfBoundsException`|Invalid array index|
|`ArithmeticException`|Division by zero|
|`NumberFormatException`|Invalid number format|

## Input/Output

### Console Input

```java
import java.util.Scanner;

Scanner scanner = new Scanner(System.in);

// Read different types
String name = scanner.nextLine();    // Read line
int age = scanner.nextInt();         // Read integer
double price = scanner.nextDouble(); // Read double
char grade = scanner.next().charAt(0); // Read character

scanner.close();
```

### Console Output

```java
// Print without newline
System.out.print("Hello");

// Print with newline
System.out.println("Hello World");

// Formatted output
System.out.printf("Name: %s, Age: %d, GPA: %.2f", name, age, gpa);
```

## Common Patterns

### Getter and Setter Pattern

```java
private String name;

public String getName() {
    return name;
}

public void setName(String name) {
    this.name = name;
}
```

### Constructor Chaining

```java
public class Student {
    private String name;
    private int age;
    private String id;
    
    public Student(String name) {
        this(name, 0);
    }
    
    public Student(String name, int age) {
        this(name, age, "Unknown");
    }
    
    public Student(String name, int age, String id) {
        this.name = name;
        this.age = age;
        this.id = id;
    }
}
```

### toString() Method

```java
@Override
public String toString() {
    return "Person{name='" + name + "', age=" + age + "}";
}
```

## Quick Reference: Operators

### Arithmetic Operators

|Operator|Description|Example|
|---|---|---|
|`+`|Addition|`a + b`|
|`-`|Subtraction|`a - b`|
|`*`|Multiplication|`a * b`|
|`/`|Division|`a / b`|
|`%`|Modulus (remainder)|`a % b`|
|`++`|Increment|`a++` or `++a`|
|`--`|Decrement|`a--` or `--a`|

### Comparison Operators

|Operator|Description|Example|
|---|---|---|
|`==`|Equal to|`a == b`|
|`!=`|Not equal to|`a != b`|
|`>`|Greater than|`a > b`|
|`<`|Less than|`a < b`|
|`>=`|Greater than or equal|`a >= b`|
|`<=`|Less than or equal|`a <= b`|

### Logical Operators

|Operator|Description|Example|
|---|---|---|
|`&&`|Logical AND|`a && b`|
|`||`|Logical OR|`a || b`|
|`!`|Logical NOT|`!a`|

## Naming Conventions

> [!tip] Java Naming Standards

|Element|Convention|Example|
|---|---|---|
|**Class**|PascalCase|`StudentRecord`, `BankAccount`|
|**Method**|camelCase|`getName()`, `calculateTotal()`|
|**Variable**|camelCase|`studentName`, `totalScore`|
|**Constant**|UPPER_SNAKE_CASE|`MAX_VALUE`, `PI`|
|**Package**|lowercase|`com.example.project`|

## Common Mistakes to Avoid

> [!warning] Common Pitfalls

1. **Comparing strings with `==`**
    
    ```java
    // Wrong
    if (str1 == str2) { }
    
    // Correct
    if (str1.equals(str2)) { }
    ```
    
2. **Array index out of bounds**
    
    ```java
    // Wrong
    for (int i = 0; i <= array.length; i++) { }
    
    // Correct
    for (int i = 0; i < array.length; i++) { }
    ```
    
3. **Not initializing variables**
    
    ```java
    // Wrong
    int count;
    count++; // Error
    
    // Correct
    int count = 0;
    count++;
    ```
    
4. **Forgetting to import classes**
    
    ```java
    import java.util.Scanner;
    import java.util.ArrayList;
    ```
    
5. **Not closing Scanner**
    
    ```java
    Scanner scanner = new Scanner(System.in);
    // ... use scanner
    scanner.close(); // Don't forget!
    ```
    

## Quick Syntax Reference

### Main Method

```java
public class Main {
    public static void main(String[] args) {
        // Program starts here
    }
}
```

### Creating and Using Objects

```java
// Create object
ClassName objectName = new ClassName();

// Access attributes
objectName.attribute = value;

// Call methods
objectName.method();
```

### Importing Packages

```java
import java.util.Scanner;        // Import specific class
import java.util.*;              // Import all classes in package
```

## References

1. Oracle. (2024). Java Documentation. https://docs.oracle.com/en/java/ 
2. W3Schools. Java Tutorial. https://www.w3schools.com/java/ 
3. GeeksforGeeks. Java Programming Language. https://www.geeksforgeeks.org/java/ 