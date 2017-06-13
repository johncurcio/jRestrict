## What is it?

jRestrict is a domain specific language built to restrict ``.java`` files into using only what's specified in a simple ``.jstrict`` script.  With jRestrict you can **prohibit** clauses from being used, **require** that a list of clauses is used or **enclose** your java file to a list of clauses. 

### Syntax

The syntax for a jscript file is composed of four main commands: 

```markdown
files {
  # list of files that will be validated by jRestrict
}
requires {
  # list of clauses that should be on every java file specified in files{}
}
encloses {
  # list of clauses that encloses all clauses which can be used in the java files
}
prohibits {
  # list of clauses that cannot be used in the java files
}
```

#### Files

The files command accept a list of java files separated by ;. E.g:

```markdown
files {
  Foo.java;
  Bar.java;
}
```

#### Encloses, Requires and Prohibits

These three commands accept a list of specific clauses made to validate some aspects of java files. The list of clauses is:

```markdown
type: <java-type>; - restricts a java type usage in the whole code. This includes return type, variable type and argument type.

returntype: <java-type>; - restricts all methods from returning a java types defined here.

vartype: <java-type>; - restricts all varibles from using this java type.

operator: <java-operator>; - restricts the usage of java operators such as assignment.

modifier: <java-modifier>; - restricts the usage of modifiers such as public, private and protected.

import: <java-imports>; - restricts the usage of the import clause.

loop: <java-loop>; - restricts the usage of loop, break and continue.

branch: <java-branch>; - restricts the usage of if and switch. 
```

In which the java arguments are separated by ``,`` and correspond to the arguments you want to restrict:

```markdown
<java-type>: int, double, float, String, Object...
<java-operator>: +, -, --, +=, <<=, <=, !...
<java-modifier>: public, private, protected, static, volatile...
<java-imports>: java.util.*, java.util.HashSet...
<java-loop>: break, continue, for, foreach...
<java-branch>: if, switch 
```

