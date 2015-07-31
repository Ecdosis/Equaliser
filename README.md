## Equaliser
The [equaliser](https://github.com/Ecdosis/Equaliser) program is a commandline tool that checks all the element and atribute combinations in a set of XML files against a set of supplied patterns. Anything that does not any of the supplied patterns will be flagged as an error. Optionally the user can ask for all locations of errors to be recorded and printed out at the end. A further option allows for message to be printed suggesting remedies for the errors. The hard-wired defaults for patterns and remedies only work for the Harpur edition. In all other cases please supply replacements for the harpur.pat and harpur.rem files, and specify them when invoking the equaliser.

### Installing
Download equaliser from the [github site](https://github.com/Ecdosis/Equaliser). Unpack the .zip file and follow the instructions for running it in this README.

### Requirements
The equaliser program requires a Java JRE version 1.7 or higher. You can install one from the [Oracle downloads site](http://www.oracle.com/technetwork/java/javase/downloads/jre7-downloads-1880261.html).

The commandline syntax is:
    java -jar equaliser.jar [-R] -l] [-p pat-file] [-r remedy-file] xml-file-or-folder name

### Remedy file format
The remedy file specifies suggestions for specific errors or classes of error. Each remedy occupies one line, each line separated by exactly one newline character (\n). The format for each remedy is:
    element-name:attributes:message

* 'element-name' is just the name of the element
* 'attributes' is a comma-separated list of name=value pairs, e.g. type=source,resp=MJS, with no quotes around the attribute values. The remedy will match so long as the element name and all the remedy attributes are found in the error. But the error can have *more* attribute pairs than the matching remedy. Also the wildcard "*" is recognised for attribute values.
* 'message' is the message to be displayed when that error occurs. 

The example remedy file is harpur.rem, which is hard-wired into the code in case no remedy file is supplied. The remedies file is specified with the -r option.

### Pattern file format
The pattern file specifies element plus attribute combinations that will *not* generate errors. Each pattern occupies one line, each line being separated by a newline character (\n). The format of a pattern is:
   element-name:attributes:

* 'element-name' is the name of the permitted element. 
* 'attributes' is a comma-separated list of attribute-specifications. The attribute-specifications may take the following forms:

1. A simple name=value pair. No quotes are allowed around the values. 
2. If some name=value pairs are already specified for the pattern, the name=value pair -=* means that the bare element name with no attributes is also permitted. 
3. If no name=value attributes are specified at all this means that only the bare element name will match. 
4. The wildcard * for an attribute value means that any attribute value will match the given attribute name. 
5. If several attribute values may occur with the same attribute name the format is name=list, where 'list' is a bracketed list of attribute values separated by the vertical bar. e.g. type=(title|subtitle|parthead) is shorthand for type=title,type=subtitle,type=parthead.

The example file is harpur.pat, which is hard-wired into the code in the case where no pattern file is supplied. The pattern file is specified with the -p option.

### Locations
By default equaliser does not print out the locations where the errors occur. Instead, only a summary of the errors found is printed. If locations are desired then the -l commandline option should be used. This will print out, for each error, the locations in the files where it was found.

### Remedies
By default equaliser does not print out suggested remedies for the errors. If these are wanted, then for each error type a remedy will be suggested if specified in the remedies file. If no remedy is available then it prints out the error and the word 'ask'. The command line option for priting remedies is -R

### Clean run
If no errors are found then the message:
    No errors found! 
is printed.
