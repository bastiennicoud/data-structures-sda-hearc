# DataStructures

> Experimentation's around data structures in java.

## Requirements

- Java 15
- Maven 3.6

## Install and run

**Clone the sources**

```
$ git clone git@github.com:bastiennicoud/data-structures-sda-hearc.git
$ cd data-structures-sda-hearc
```

### Run in IntelliJ

```
# Open the project in IntelliJ
$ idea
```

**When idea is open :**

- Go to *project structure* and set the JDK to jdk 15. (If you don't have jdk 15 you can choose *Add SDK > Download JDK*
  submenu)
- Wait until intelliJ fetch dependencies.
- Launch the program with the *Main* run configuration. (Or directly by clicking the Play button on the left of the main
  method in java file)

> You can build .jar with dependencies from intelliJ using execute maven goal :
> - Open the *Maven* pane
> - Click on the *Execute Maven Goal* button
> - Type `mvn compile assembly:single`

### Command line with maven

```
# Launch the project
$ mvn exec:java

# Build project .jar with the dependencies
$ mvn compile assembly:single

# Launch the fresh jar
$ java -jar ./target/data-structures-sda2020-1.0-jar-with-dependencies.jar
```