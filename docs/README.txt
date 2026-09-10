=======================================================================
PRITHEE
Homework 2, SDE, University of Virginia
=======================================================================

A console game built on a custom of the Blackfriars Playhouse in
Staunton.  During a preview performance, an actor who forgets a line
calls out "Prithee" and a fellow player waiting in the wings with the
script supplies the next word.

This program puts you in the wings.  It prints Shakespeare's Sonnet 18,
stops at a random word, and prints underscores in its place.  You type
the missing word.  Three correct answers save the show; three misses
drop the curtain.


-----------------------------------------------------------------------
QUICK START
-----------------------------------------------------------------------

Open a terminal in the project root - the folder holding
build.gradle.kts, one level above this docs folder - and run one of the
following.

  Play the game

      Windows:        .\gradlew.bat run --console=plain
      macOS, Linux:   ./gradlew run --console=plain

  Compile

      Windows:        .\gradlew.bat build
      macOS, Linux:   ./gradlew build

  Test

      Follow the table in TEST-PLAN.txt: type each input at the prompt
      and compare what the program prints with the expected output.

Nothing needs to be installed except a JDK 26.  The Gradle wrapper is
committed to the repository and downloads Gradle on the first build.


-----------------------------------------------------------------------
DOCUMENTATION
-----------------------------------------------------------------------

  DESIGN.txt            The design: goals, UML class diagrams for every
                        class, what each class is responsible for, how a
                        round flows, the decisions behind the design,
                        and the testing strategy.

  HOW-TO-RUN.txt        Requirements, every way to run the program,
                        how to play, how to run the tests, how to
                        generate the Javadoc, the project layout, and
                        what to do if something goes wrong.

  TEST-PLAN.txt         The test plan: a table of each test's input,
                        expected output and actual output.


-----------------------------------------------------------------------
BUILT WITH
-----------------------------------------------------------------------

  Java 26
  Gradle 9.6, configured with the Kotlin DSL (build.gradle.kts)
