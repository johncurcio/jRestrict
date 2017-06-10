# jRestrict

jRestrict is a tiny language built to restrict ``.java`` files into using only what's specified in a simple ``.jstrict`` script. The script has 3 main commands:

1. ``requires`` - specifies clauses that has to be in the java code for it to be valid. If a single clause is not in the java code, then an error is thrown. 
2. ``encloses`` - specifies the list of clauses that can be in the java code. The code may not include all clauses, but it can only include the clauses specified here.
3. ``prohibits`` - specifies a list of clauses which may not be in the java code. If any clause specified here is found in the code, then an error is thrown. 

PS.: Prohibition is always higher in rank in jRestrict and therefore if both requirement and prohibition are specified for the same clause, prohibition is taken as the correct answer. 

Each of the main commands accept a list of clauses. This list of clause follows the pattern ``clause-regex: restricion1, restriction2, ..., restrictionN;``

## File reading

A special clause ``file: <name of file>;`` has been added to the language, so that an input .java file can be read and analysed. This clause might be removed from the final version of the language. 

## Accepted clauses

``type: <java-type>;`` - restricts a java type usage in the whole code. Ex.: ``type: int, double, Graph;``

``returntype: <java-type>;`` - restricts all methods using the java types defined here. Ex.: ``returntype: int, Object;``

``argtype: <java-type>;`` - retricts all arguments usage of types.

``vartype: <java-type>;``

``operator: <java-operator>;``

``modifier: <java-modifier>;``

``import: <java-imports>;``

``exception: <java-exceptions>;``

``loop: <java-loop>;``

``branch: <java-branch>;``

## jRestrict grammar

```
script    := (file mainc | mainc file) 
mainc     := (encloses? requires?) | (prohibits? requires?)

filename  := [a-zA-Z]+ ".java" ";"
file      := "files"     "{" filename+ "}"
requires  := "requires"  "{" clauses*  "}"
encloses  := "encloses"  "{" clauses*  "}"
prohibits := "prohibits" "{" clauses*  "}"
comments  := "--"[^\n]*

clauses   := (clause-type | clause-returntype | clause-argtype | clause-vartype | clause-operator | clause-modifier | clause-import | clause-exception | clause-loop | clause-branch )

clause-type       := "type:" java-type ("," java-type)* ";"
clause-returntype := "returntype:" java-type ("," java-type)* ";" 
clause-argtype    := "argtype:" java-type ("," java-type)* ";"
clause-vartype    := "vartype:" java-type ("," java-type)* ";"
clause-operator   := "operator:" java-operator ("," java-operator)* ";" 
clause-modifier   := "modifier:" java-modifier ("," java-modifier)* ";"
clause-import     := "import:" java-import ("," java-import)* ";" 
clause-exception  := "exception:" java-exception ("," java-exception)* ";" 
clause-loop       := "loop:" java-loop ("," java-loop)* ";" 
clause-branch     := "branch:" java-branch ("," java-branch)* ";" 

java-type      := ("int" | "double" | "String" | "Object" | "boolean" | "float" | "char")
java-operator  := ("+" | "-" | "/" | "*" | "=" | "%" | "&" | "|" | "!" | ">" | "<")
java-modifier  := ("public" | "private" | "protected" | "static" | "final")
java-import    := identifier (("." identifier)* | ".*")
java-exception := identifier "Exception"
java-loop      := ("while" | "do" | "for" | "break" | "continue")
java-branch    := ("switch" | "if")


java-arithmeticop := ("+" | "-" | "/" | "*" | "%" | "++" | "--")
java-assignmentop := ("=" | "+=" | "-=" | "*=" | "/=" | "%=" | "<<=" | ">>=" | "&=" | "^=" | "|=")
java-bitwiseop := ("|" | "&" | "^" | "~" | ">>" | "<<" | ">>>") 
java-boolop  := ("&&" | "||" | "==" | "<=" | ">=" | "!=" | "!")

```