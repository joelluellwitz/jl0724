# Retail Point of Sale Demo Application

Repository website: https://github.com/joelluellwitz/jl0724

## Overview

An undisclosed employer asked me to create this application as part of their hiring process. Basically, this console application prompts the user to enter tool rental parameters and then a rental agreement is displayed.

## Notes to Reviewer

First, thank you for reviewing my demo project. I hope you are as impressed by this as I think you will be.

### This Application is Over-architected

I intentionally added extra functionality, complexity, and layers of abstraction to demonstrate mastery of Java, Spring, and other libraries and design patterns. Admittedly, a real application of this size probably shouldn't have all this complexity.

Particularly, I'm trying to demonstrate:

* The 3-tier model.
* Spring Boot.
* Spring JPA and Hibernate.
* Spring proxies.
* Immutable objects.
* Package organization.
* Swapping out the persistence implementation during unit testing.
* Logging.
* Solid understanding of Maven.
* Threading (via some unit tests).

Even with the over-architecting, I believe I strictly adhered to all the requirements in the requirements document.

I feel it is important to note that in a real job environment, I might not put this level of detail into my code. For example, to develop code more quickly, I typically do not Javadoc unit test methods.

### Files the Reviewer Will Care Most About

Most of the logic the reviewer is probably interested in is in the following files:

* RetailPointOfSaleImpl.java
* RentalAgreementImpl.java
* RequiredRetailPointOfSaleImplTests.java

### Comments Explaining Rationale

I included comments in the source explaining my rationale for many of my design decisions. The most important comments start with the phrase "Note:". I recommend searching on that phrase to make sure you don't miss any of these comments.

The rationale for more broad design decisions are explained below.

### Package and Class Organization

#### Grouping Files by Business Function

One thing the reviewer might notice is that I did not group files by file type. What I mean by this is, I intentionally did not put all DTO classes in one package and all Repo classes in another package, etc. This is because I do not think it is useful organize files this way. I generally try to group files by business function.

This application is too small to have business function divisions, so I did not attempt to model this in this project. However, suppose this application is a bit more complex and there is also user account functionality. In this case, I might have organized some of the project with this package layout:
* io.github.joelluellwitz.jl0724.exposed.service.account.api
* io.github.joelluellwitz.jl0724.exposed.service.rental.api
* io.github.joelluellwitz.jl0724.internal.data.account.api
* io.github.joelluellwitz.jl0724.internal.data.rental.api
* io.github.joelluellwitz.jl0724.internal.service.account.impl
* io.github.joelluellwitz.jl0724.internal.service.rental.impl

With this system, related files are kept together but no package grows so large that it becomes unmanageable. If a package does grow too large, it should be split into more delineated business functions.

#### Package Hierarchy

Below the project identification part of the package name (in this case, io.github.joelluellwitz.jl0724), packages are organized with the following hierarchy:

1. exposed/internal - Identifies the parts of the project intended to be consumed by other projects (jars) as a dependency. This layer is only needed when consumable interfaces and classes are not split out into a separate project (jar).
2. Tier in the 3-tier (n-tier) model - Typically either 'ui', 'service' (business logic), or 'data'. Note: I could have put RentalConsole.java under io.github.joelluellwitz.jl0724.internal.ui.console, but Spring Boot applications look a little cleaner if the main loop class is found in the root of the package hierarchy.
3. Business function - See the prior section for a description. Not used in this demo due to the application's small size. (I thought it would be too confusing.)
4. api/impl - 'api' packages identify interfaces and classes intended to be used by a different tier in the 3-tier model. (Note that unlike the internal/external classification, 'api' interfaces and classes might be intended to *only* be used within the same project.) 'impl' packages contain implementations of 'api' interfaces as well as related helper classes. If an implementation package can be associated with a particular technology, the name 'impl' should be replaced with the name of that technology. For example, if there is an SQLite implementation of io.github.joelluellwitz.jl0724.internal.data.api, that package should be called io.github.joelluellwitz.jl0724.internal.data.sqlite.

### Use of 'final' with Most Variables

Some developers are really annoyed about this, but in my own code, I make a best effort to only use immutable variables. In multithreaded applications, using immutable variables makes code less prone to side-effect errors. Even in single threaded programs, I find that always using immutable variables usually makes code less confusing and reduces error potential.

That said, Java is not Scala. Due to missing language constructs to handle immutable variables, sometimes it is practically necessary to use mutable variables.

## Building and Running the Demo

### System Requirements

**This demo requires Java 21.** The project is known to build correctly with Maven version 3.6.3. It was tested on Ubuntu 22.04.4 LTS.

Building the project requires access to Maven Central (https://repo1.maven.org/).

Unfortunately, I do not have reasonable access to MacOS or Windows for cross platform testing. (I only use Linux on my personal computers.)

### Building and Running

This is a Maven project. From the project root, you can execute the unit tests by running `mvn clean test`. You can build the console application jar (which also executes the unit tests) by running `mvn clean package`. Once built, you can start the console application by running `java -jar target/jl0724-1.0.0.jar`.

### Logging

Because this is a console based application, logging to the console is disabled by default. If you want to enable debug logging, run the program like this:

```
java -jar target/jl0724-1.0.0.jar --logging.file.name=jl0724.log --logging.level.io.github.joelluellwitz.jl0724=debug
```

Logs will be recorded in 'jl0724.log' in the current working directory.