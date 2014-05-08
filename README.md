SuperMacalester
===============

When you set up the project, I recommend you first create a superfolder.  I called mine SuperMacalester as well.  Before you import the actual project, first download the additional modules from the following link and save them in the superfolder you just created.
https://www.dropbox.com/sh/rd06fxudjymsdfe/Pg6XybdFOH

Next, import this project as the main module into the superfolder previously created.  It may automatically import the other modules, but if not you will need to import each one manually.  Once all the modules are present in the project, you will probably need to go into the project structure and set the main module and its library(s) as dependencies for the additional modules.

In order to run the program, you can either open up the SuperMacalester-desktop module and run the Main class's main method.  This will run the program as a java application on your computer.  Alternatively, you can run the MainActivity in the SuperMacalester-android as an Android program.  The latter of course requires a device or emulator.

The SuperMacalester-robovm module allows for iOS application support, however RoboVM and IntelliJ are not compatible.  For those familiar with RoboVM (I am not), you may be able to import the modules into Eclipse and run RoboVM through Eclipse.

The SuperMacalester-html module allows for web applications, however I have little experience with web development.

The RoboVM and Android modules each maintain their own data folders, so it is necessary to copy the contents of data directory from the main module into their respective directories.  For Android, the folder is in the assets directory; for RoboVM it is a top level directory.  
