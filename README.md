# Mikrojava Compiler

This project is an implementation of a compiler for the **Mikrojava programming language**, developed as part of the *Programming Compilers 1* course at the Faculty of Electrical Engineering, University of Belgrade.

The compiler translates Mikrojava source code into **MJVM bytecode**, which can be executed on the Mikrojava virtual machine.

---

## Project Overview

The compiler is structured into four classical compiler phases:

1. **Lexical Analysis**
2. **Syntax Analysis**
3. **Semantic Analysis**
4. **Code Generation**

Each phase is implemented according to the official Mikrojava language specification and course requirements.

---

##  Compiler Phases

### 1. Lexical Analysis
- Implemented using **JFlex**
- Converts source code into a stream of tokens
- Recognizes:
  - Keywords
  - Identifiers
  - Constants
  - Operators
  - Comments
- Ignores whitespace characters
- Reports lexical errors with line and column information
- CUP-compatible lexer interface (`next_token()`)

---

### 2. Syntax Analysis
- Implemented using **AST-CUP (extended CUP parser)**
- Based on a formal LALR(1) grammar of Mikrojava
- Builds an **Abstract Syntax Tree (AST)**
- Supports:
  - Expressions and statements
  - Functions and method calls
  - Classes and inheritance (depending on level)
  - Arrays and basic types
- Includes **error recovery mechanisms**
- On successful parsing, prints the AST structure

---

### 3. Semantic Analysis
- Implemented using the **Visitor design pattern**
- Traverses the AST to perform semantic checks
- Uses the provided **symbol table library (`symboltable.jar`)**
- Performs:
  - Type checking
  - Declaration validation
  - Scope resolution
  - Semantic error detection
- Outputs symbol table contents after analysis

---

### 4. Code Generation
- Generates executable **MJVM bytecode (`.obj` file)**
- Implemented via AST traversal (Visitor pattern)
- Supports generation for:
  - Arithmetic and logical expressions
  - Control structures (`if`, `for`, `while`, `switch`)
  - Functions and method calls
  - Arrays
- Produces runnable output for MJ virtual machine

---

## Supported Language Features

Depending on implementation level (A / B / C), the compiler supports:

- Basic data types (`int`, `char`, `bool`)
- Arrays of primitive types
- Functions and procedures
- Global and local variables
- Control flow structures

---

## Tools and Technologies

- Java 8 (JDK 1.8)
- JFlex (lexer generator)
- AST-CUP (parser generator)
- MJVM runtime environment
- Provided symbol table library (`symboltable.jar`)



Typical project structure:
