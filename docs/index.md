## What is it?

jRestrict is a domain specific language built to restrict ``.java`` files into using only what's specified in a simple ``.jstrict`` script.  With jRestrict you can **prohibit** clauses from being used, **require** that a list of clauses is used or **enclose** your java file to a list of clauses. 

### Syntax

The syntax for a jscript file is composed of four main commands: 

```markdown
files {
  # here comes a list of files that will be validated by jRestrict
}
requires {
  # here comes a list of clauses that should be on every java file specified in files{}
}
encloses {
  # here comes a list of clauses that encloses all clauses which can be used in the java files
}
prohibits {
  # here comes a list of clauses that cannot be used in the java files
}
```

For more details see [GitHub Flavored Markdown](https://guides.github.com/features/mastering-markdown/).

### Jekyll Themes

Your Pages site will use the layout and styles from the Jekyll theme you have selected in your [repository settings](https://github.com/johncurcio/jRestrict/settings). The name of this theme is saved in the Jekyll `_config.yml` configuration file.

### Support or Contact

Having trouble with Pages? Check out our [documentation](https://help.github.com/categories/github-pages-basics/) or [contact support](https://github.com/contact) and we’ll help you sort it out.
