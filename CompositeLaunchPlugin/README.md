## Composite launch plug-in 

The plug-in provides a tool to launch several configurations 'at once'.

Please note:
Modes 'run' and 'debug' are supported.
A composite-launch configuration itself (= a configuration of that type) cannot be selected. 

Approach:
Nowadays, people are used to touch screens and smart phones and their behavior. Items that are not possible to be moved or used are simply not shown; not moved, not started - instead of explanations given by a program, the program simply might not react.
Also, users might expect behavior of double-clicks to start a move (and drop&drag, however, that was not implemented), rather than to click a button therefore.
That approach was followed.

## Notes

The plugin was developed on Eclipse mars 2; with Java 7.
It is not tested on a higher version of Eclipse or Java.

## Installation

Please add the .jar file of the plug-in into the folder /eclipse/plugins of your eclipse installation.
Then, please restart eclipse.

## Usage

After installation (please see above), please restart eclipse.
Please select the icon 'arrow-down' in the top-menu, of the (run or debug) configurations.

In the upcoming drop-down menu, please select the link 'Run Configurations...' 
(for debug: 'Debug Configurations...').

A window opens, title 'Run Configurations' (for debug: 'Debug Configurations').
At the left side of the window, implemented configuration-types are listed.

Please 'double-click' onto "Composite launch", to create a new composite-launch-configuration.

Please see 'Configurations to launch (please select/unselect by dbl-click):';
please 'double-click' to select a configuration.

Please 'double-click' onto a selected configuration at the right, to un-select it 
(the un-selected configuration will be listed at the left).

Please click buttons 'Apply', and then 'Run'; to launch the selected configurations.


## TECHNICAL NOTE - FOR DEVELOPERS ***************************************

## Development environment

Ubuntu 16.04.1 LTS
Eclipse Version: Mars.2 Release (4.5.2), Build id: 20160218-0600
OpenJDK 1.7.0_95

## Plug-in creation

Please import the project.

Then, please select in the Menu: File > Export >
Plug-in development > Deployable plug-ins and fragments >

Available Plug-ins and Fragments: please select this plug-in

Destination: Please select (e.g.) a 'Directory' as location for the jar to generate.

Button Finish

Please find the org.vendor.CompositeLaunch_0.9.0.jar in the location.

## References and help in the web

The eclipse launch framework:
http://www.vogella.com/tutorials/EclipseLauncherFramework/article.html

Apply/revert and changes:
https://www.eclipse.org/forums/index.php/t/164755/

The TreeView:
http://www.vogella.com/tutorials/EclipseJFaceTree/article.html
http://www.buggybread.com/2013/11/java-jface-treeviewer-steps-to-create.html

## Known issues
To be fixed in v1.0

functional
- The 'revert' button does not revert the selection
- A re-opened composite-launch configuration does not show the selected configs
 (however they are store-able in a .launch file as elements of the attribute)

ui
- The icon was chosen of the eclipse-icons however, it is not correctly shown
- A scroll-bar should be shown for each tree, not for the entire composite
