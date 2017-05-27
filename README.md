# jRestrict

jRestrict is a tiny language built to restrict .java files into using only what's specified in a simple jRestrict script. The script may contain two out of three main commands:

1. ``requires`` - specifies clauses that has to be in the java code for it to be valid. If a single clause is not in the java code, then an error is thrown. 
2. ``mandates`` - specifies the list of clauses that can be in the java code. The code may not include all clauses, but it can only include the clauses specified here.
3. ``prohibits`` - specifies a list of clauses which may not be in the java code. If any clause specified here is found in the code, then an error is thrown. 

Prohibition is always higher in rank in jReMaP and therefore if both requirement and prohibition are specified for the same clause, prohibition is taken as the correct answer. 

Each of the main commands accept a list of clauses. This list of clause follows the pattern ``clause-regex: restricion1, restriction2, ..., restrictionN;``

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