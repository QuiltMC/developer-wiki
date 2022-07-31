# Getting Started

QuiltMC Has an easy to use template mod which allows you to quickly get started generating mods. In this tutorial we will explain how to setup the development environment for QuiltMC

## Installing OpenJDK 17

Minecraft and QuiltMC are written in Java, in order to make mods you need to have Java installed. In order to make mods you need to be able to compile source code, in order to do this you will need a Development Version of Java, these often end in -dev or -devel in the package names (This mostly matters on Unix-Like Operating systems).

If you are on Linux or BSD, we recommend you use the package manager in your operating system to install OpenJDK. If you are on Windows we reccomend you get an OpenJDK from your preferred Vendor. The most common is Adoptuium, but you can also use an OpenJDK from RedHat or Azul. If you are on Solaris we suggest you get ptribble's OpenJDK Build. We do not recommend you use Oracle/Sun JDK because of licensing issues.

While Java 18 and newer may work, Java 17 is the suggested as it is a LTS version and the same version Minecraft is currently using.

### Downloading QuiltMC's Template Mod
After you have installed OpenJDK, you can now start developing Quilt Mods. To Download the QuiltMC example mod, you need to go to the QuiltMC Template Mod GitHub and then you need to download it. There are 2 main ways you can do this. The Suggested way is you click on `Clone -> Download Zip` and then download it as a Zip and then Extract it to your desired location.

Your may also clone it with git with the following command
` git clone https://github.com/QuiltMC/quilt-template-mod.git `
Please Note this will require you to have the git package installed.

After you have Downloaded and Extracted the folder you must now go inside of it and open a Terminal/Command Prompt inside of it. Some Operating Systems allow you to just right click in a folder and open a terminal from within the folder, but if you do not have this the `cd` command should work.

### Generating Sources and Creating Project

#### GenSources

The first thing you must do to start modding is to Generate Minecraft Source code and remap it to Readable. QuiltLoom makes this easy all you need to do is  run the commad `genSources`

On Windows (Except for PowerShell)
`gradlew genSources`

For Unix-Like
`./gradlew genSources`
Please Note: on Unix Like OSs the main difference is simply the ./

This command will download Minecraft's Source Code and will remap it to Yarn Intermediary and into QuiltMappings. QuiltMappings are the default mappings used by QuiltMC. This command will take a while.

#### Project Creating

After you have downloaded Minecrafts Source Code, you now need to create the Java Project. You have 2 main options,

If you are running Eclipse or an Eclipse Based IDE (Like CodeReady Studio), run the following command
`gradlew eclipse`
If you are running JetBrains IDEA run the folllowing command
`gradlew idea`
Be Sure if you are on Unix to include the ./ at the beginning.
VS Code is also an option, but not is not very common.

### Importing the Project to the IDE 

You will now need to import the project into the IDE.

#### Eclipse

If you are in Eclipse or a similar IDE you need to click `File -> Import` and then a new Window should come up and go to `General -> Existing Projects Into WorkSpace` and then specify the location where your QuiltMC Example Mod is located.

#### IntelliJ IDEA
//TODO

###Setting the Default Values of your Mod
Once you import your mod into your preferred IDE, you will see the packages it imports. There are 2 main places you need to worry about at first, `java` and `resources`. Resources is where most of the non-code stuff goes, including DataPacks, quiltmod.json, model files, and textures. Java is where most of the Java code is stored. Under Java you can rename your Package to something more specific to your mod, you can do the same with your Class. Please Note, that you must update the `package` and `class` declarations in your Java class to the new ones you named.

Under Resources you will see `quilt.mod.json`, this file tells QuiltLoader information about your mod and must be filled out. Be sure you do not use special charachers.
Here are the main things that you need to fill out in order for the mod to work:

`id` This is where you put the modid of your mod. It is a unique identifier for your mod. 
`version` The Version of your mod
`name` A Localised Name for Your Mod
`description` A Decsription for your Mod
`contributors` People who worked on your mod
`contact` this is the section where you can put the websites such as `homepage`, `issues`, or `sources`.
`icon` the path to the icon of your mod relative to the entry of the Jar (DO NOT INCLUDE RESOURCES IN THE NAME, START AT assets)
`intermediate_mappings` is the intermediary mappings your mod will be compiled to. For now Quilt only supports `net.fabricmc:intermediary`
`entrypoints` is there you specify all your entrypoints
`init` is the main entry point. You must specify the location of the Main Class of your mod in the following format `package.Class`. Do NOT include java. at the beginning.
`depends`specifies the dependancies of the mod. `id` is the modid, and `versions` is the version range. By Default a few are included such as Minecraft and Quilt-Loader and Quilted Fabric API.
`mixin` this is the name a Mixins JSON in the root of the assets folder and contains info about where the mixins are.

### Mixins File
In the root of your Resources folder you should see a .mixins.json file, this file should be the same name as specified in the `mixin` attribute in the quilt.mod.json. This file contains the package name of the mixins and the name of the class. `mixins` is where you put the names of all your client and server side mixins, while client is client side only. Each class name should be to a class in the package for that file and should be surrounded by double quotes "ClassName" and seperated with a comma, except for the last.


### Testing
After making some changes to the example mod you may now test the game to make sure it works still and that the mod count has gone up.To run the game and test run the following command
` gradlew runClient`
and then the game should start.

You may notice a new folder called `run` is created. This is the equivlent of a .minecraft folder and you can install additional mods there. We suggest the ModMenu Mod to verify that the quilt.mod.json worked correctly.

