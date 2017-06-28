# jRestrict

jRestrict is a tiny language built to restrict ``.java`` files into using only what's specified in a simple ``.jstrict`` script. The script has 3 main commands:

1. ``requires`` - specifies clauses that has to be in the java code for it to be valid. If a single clause is not in the java code then an error is thrown. 
2. ``encloses`` - specifies the list of clauses that can be in the java code. The code may not include all clauses, but it can only include the clauses specified here.
3. ``prohibits`` - specifies a list of clauses which may not be in the java code. If any clause specified here is found in the code, then an error is thrown.  

Each of the main commands accept a list of clauses. And each clause takes a list of java arguments. 

## File reading

A special clause ``file{ <file1.java>; <file2.java>; ... }`` has been added to the language, so that an input ``.java`` file can be read and analysed. 

## jRestrict grammar

```
 script    := file requires? prohibits? encloses?
 
 filename  := [a-zA-Z]+ ".java" ";"
 file      := "files"     "{" filename+ "}"
 requires  := "requires"  "{" clauses*  "}"
 encloses  := "encloses"  "{" clauses*  "}"
 prohibits := "prohibits" "{" clauses*  "}"
 clauses   := (clause-type | clause-returntype | clause-vartype | clause-operator | clause-modifier | clause-import | clause-loop | clause-branch)
 
 clause-type       := "type:" java-type ("," java-type)* ";" 
 clause-returntype := "returntype:" java-type ("," java-type)* ";" 
 clause-vartype    := "vartype:" java-type ("," java-type)* ";" 
 java-type         := ("int" | "double" | "boolean" | "float" | "char" | "byte" | "short" | "long" | "void"| JavaIdentifier)
 clause-modifier   := "modifier:" java-modifier ("," java-modifier)* ";"
 java-modifier     := ("public" | "private" | "protected" | "static" | "final" | "abstract" | "volatile" | "synchronized")
 clause-loop       := "loop:" java-loop ("," java-loop)* ";" 
 java-loop         := ("while" | "do" | "for" | "foreach" | "break" | "continue")
 clause-branch     := "branch:" java-branch ("," java-branch)* ";" 
 java-branch       := ("switch" | "if" | "else")
 clause-operator   := "operator:" java-operator ("," java-operator)* ";" 
 java-operator     := (java-ternaryop | java-binaryop | java-unaryop)
 java-unaryop      := ("+" | "-" | "/" | "*" | "%" | "|" | "&" | "=" | "!" | "^" | "~")
 java-binaryop     := ("++" | "--" | ">>" | "<<" | "+=" | "-=" | "*=" | "/=" | "%=" | "&=" | "^=" | "|=" | "&&" | "||" | "==" | "<=" | ">=" | "!=")
 java-ternaryop    := (">>>=" | ">>>" | "<<=" | ">>=")
 clause-import     := "import:" java-import ("," java-import)* ";"
 java-import       := [a-zA-Z] ("." [a-zA-Z]* | ".*")
 
 comments  := "#" [^\n]*
 spaces    := [ \n\r\t]+ | comments

```

## Accepted clauses

``type: <java-type>;`` - restricts a java type usage in the whole code. This includes return type, variable type and argument type.

``returntype: <java-type>;`` - restricts all methods from returning a java types defined here.

``vartype: <java-type>;`` - restricts all varibles from using this java type.

``operator: <java-operator>;`` - restricts the usage of java operators such as assignment.

``modifier: <java-modifier>;`` - restricts the usage of modifiers such as public, private and protected.

``import: <java-imports>;`` - restricts the usage of the import clause.

``loop: <java-loop>;`` - restricts the usage of loop, break and continue.

``branch: <java-branch>;`` - restricts the usage of if and switch. 

## Analysis

You can't both require and prohibit the same clause; 
You can't require a clause that has been restricted by encloses;
You can't prohibit a clause that has been restricted by encloses.

## How to use it

To use jRestrict, you can add the ``core/`` folder to your eclipse project and then add the provided ``core/main/javaparser-core-3.2.6.jar`` to your classpath.



