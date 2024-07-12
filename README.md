# Retail Point of Sale Demo Application

## Overview

An undisclosed employer asked me to create this application as part of their hiring process. Basically, the user selects a tool for rental and some rental parameters and a tool rental agreement is displayed.

## Notes to Reviewer

### This Application is Over-architected

I intentionally added extra functionality, complexity, and layers of abstraction to demonstrate mastery of Java, Spring, and other libraries and design patterns. Admittedly, a real application of this size probably shouldn't have all this complexity.

Particularly, I'm trying to demonstrate:

* The 3-tier model.
* Spring JPA and Hibernate.
* Spring proxies.
* Immutable objects.
* Package organization.
* Swapping out the persistence implementation during unit testing.
* Logging.

Even with the over-architecting, I believe I strictly adhered to all the requirements in the requirements document.

### Files the Reviewer Will Care Most About

Most of the logic the reviewer is probably interested in is in the following files:

* RetailPointOfSaleImpl.java
* RentalAgreementImpl.java
* RequiredRetailPointOfSaleImplTests.java

### Comments Explaining Rationale

I included comments in the source explaining my rationale for many of my design decisions. The most important comments start with the phrase "Note:". I recommend searching on that phrase to make sure you don't miss any of these comments.



* Mention "Resulting total rounded half up to cents." implies partial days are supported? I suspect this is an error.
* Mention organizing by functional area and not file type.
* Mention 3 tier or multi-tier model and how that relates to categorizing by functional area.
  * Explain exposed vs internal.
* Maybe mention why not all versions are listed in properties (in pom.xml).
* Attempt to autoformat everything.
* Explain use of final.
* Remove TODOs.
* Remove SNAPSHOT Modifier.
* Reduce required Java version.
* Generate Javadoc HTML.
* Test on other computer.
* Limit README.md to 80 characters wide.
* Remove TODOs from README.md.
* Include notes about development environment.
* Mention mocking System.out and System.in.