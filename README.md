# Sage

Sage is a task-management chatbot with a JavaFX chat interface. Commands entered in the window are saved to
`data/sage.txt`, so tasks and notes remain available the next time Sage starts.

## Remembering information with notes

Use `note <text>` to save a short, single-line snippet, for example:

```text
note Waist size: 32 inches
note Movie to watch: Spirited Away
list
find waist
delete 1
```

Notes appear as `[N]` entries alongside tasks. Use the number shown by `list` when deleting a note;
search results are numbered separately. Notes can be searched and deleted but cannot be marked as completed.
Empty notes are rejected. Unicode and pipe characters (`|`) are preserved when notes are saved and reloaded.
Notes share the existing 100-entry limit with tasks.

## Setting up in IntelliJ

Prerequisites: JDK 25 and a recent IntelliJ version. On macOS, use the JavaFX-enabled Azul Zulu distribution required
by the SE-EDU JavaFX tutorial (`sdk use java 25.0.3.fx-zulu`).

1. Open IntelliJ (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project
   first).
1. Open the project in IntelliJ as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. Locate `src/main/java/sage/Launcher.java`, right-click it, and choose `Run Launcher.main()` (if the code editor
   shows compile errors, try reloading the Gradle project or restarting the IDE).

You can also start the GUI from a terminal with `./gradlew run`. The `sage.Sage` class retains the text-based entry
point for automated testing.

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.
