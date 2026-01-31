# Jax project template

This is a project template for a greenfield Java project. It's named after the a random mascot _Jax_. Given below are instructions on how to use it.

## Setting up in IntelliJ

Prerequisites: JDK 17, update IntelliJ to the most recent version.

1. Open IntelliJ (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
2. Open the project into IntelliJ as follows:
   1. Click `Open`.
   2. Select the project directory, and click `OK`.
   3. If there are any further prompts, accept the defaults.
3. Configure the project to use git push origin master **JDK 17** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
   1. After that, locate the `src/main/java/Jax.java` file, right-click it, and choose `Run Jax.main()`<br>(if the code editor is showing compile errors, try restarting the IDE).<br>If the setup is correct, you should see something like the below as the output:
      ```
      Hello from
        __
        \ \  __ ___   __
         \ \/  _` \ \/ /
      /\_/ /  (_| | >  <
      \___/ \__,_/\_/\_\
      ```
