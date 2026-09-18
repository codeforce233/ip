---
title: Build and release guide
---

# Building and sharing Sage

[User guide](README.md) · [Testing notes](testing.md)

Sage requires **Java 25** on the receiving computer. It is a desktop application; copying the JAR does not
install Java. On macOS, use the [SE-EDU recommended Zulu JDK 25 with JavaFX](https://se-education.org/guides/tutorials/javaInstallationMac.html).
On Windows and Linux, use a 64-bit Java 25 installation. The GUI also requires a working desktop display;
Linux needs the usual GTK desktop libraries.

## Choose the correct build

| Device and Java runtime | Output file | Build command |
| --- | --- | --- |
| Windows x64, Linux x64, or Intel macOS | `build/libs/sage.jar` | `./gradlew clean check shadowJar -PmacArm64=false` |
| Apple Silicon macOS with an ARM64 Java runtime | `build/libs/sage-mac-arm64.jar` | `./gradlew clean check shadowJar -PmacArm64=true` |

Use `gradlew.bat` instead of `./gradlew` in Windows Command Prompt or PowerShell.
The committed build selects Intel macOS libraries by default; `macArm64=true` selects ARM64 libraries.
Explicit flags make release builds independent of the build machine.

Both variants contain Windows x64 and Linux x64 libraries, but only one macOS architecture at a time.
A clean build removes older output. Copy each build to a separate release folder before building
the other. Preserve the distinct asset names so users can identify the macOS architecture.
Neither variant claims support for 32-bit Java, ARM Linux, or ARM Windows.

The Intel build follows the [SE-EDU JavaFX tutorial's explicit JavaFX 17.0.7 dependencies](https://se-education.org/guides/tutorials/javaFxPart1.html#setting-up-java-fx)
for Windows, macOS, and Linux, and uses the separate `sage.Launcher` entry point. The ARM variant changes only
the macOS classifier. Separate builds prevent Intel and ARM libraries with identical names from
overwriting each other inside a fat JAR. The build intentionally does not combine these dependencies with
an additional platform-selecting JavaFX plugin. Transitive resolution is disabled only for the explicitly
listed JavaFX modules, because their POMs would otherwise add host-selected native libraries again.

## Run on another computer

1. Confirm `java -version` reports Java 25 and that its architecture matches the chosen JAR.
2. Copy only the chosen JAR to a writable folder on the other computer. No source checkout or IDE is needed.
3. Open a terminal in that folder and run `java -jar sage.jar` (use the actual filename if renamed for release).
4. Add an item, close Sage, reopen it from the same folder, and confirm the item is still listed.

Sage stores its data in `data/sage.txt` relative to the folder from which it was launched. Keep that folder
consistent when reopening Sage. To move existing tasks and notes, close Sage first and copy the `data` folder
along with the JAR. Data is personal: do not include it in a public release. Run one Sage instance per data
folder to avoid concurrent edits overwriting one another.

For terminal-only usage, run `java -cp sage.jar sage.Sage` (substitute the actual filename if renamed).
The optional `-ea` JVM flag enables the application's internal Java assertions.

## Release checks

Run these commands from the repository root with Java 25 and Python 3.10 or newer:

```sh
./gradlew clean check shadowJar -PmacArm64=false
python3 scripts/check-release.py --jar build/libs/sage.jar
python3 .codex/skills/test-ui/scripts/run-ui-tests.py --plan test/ui-test-plan.md --jar build/libs/sage.jar
```

For the Apple Silicon release, build with `-PmacArm64=true` and add `--mac-architecture arm64` to
`check-release.py`. Substitute `build/libs/sage-mac-arm64.jar` in both verification commands.
On Windows, use `python` if `python3` is unavailable.

The release check inspects the launcher, GUI resources, Java version, native architecture, and absence of
personal data, then copies the JAR into a fresh temporary folder whose name contains spaces. It verifies
adding, reloading, and deleting data, including Unicode notes, without access to the source checkout.
The acceptance runner compares complete CLI output for each case and starts each case with separate data.
Nonzero exits and timeouts are failures even if output otherwise matches.

`check` runs JUnit with assertions enabled and enforces at least **90% line coverage and 80% branch coverage**
for automatically testable application code. Only JavaFX GUI classes and the launcher are excluded.
[JaCoCo 0.8.14 supports Java 25](https://www.jacoco.org/jacoco/trunk/doc/changes.html).
Reports are generated under `build/reports/tests/test/` and `build/reports/jacoco/test/html/`.

GitHub Actions has a matrix for Windows x64, Linux x64, Intel macOS, and Apple Silicon macOS.
Configuration is not evidence that a remote run has passed: check the actual workflow results before
publishing. CLI and archive checks do not establish GUI rendering quality on those machines. Manually check
startup, resizing, long messages, error visibility, keyboard submission, Unicode input, and the `bye` command
on each supported desktop OS before claiming it has been tested there.

## Repository hygiene

Runtime data, build output, local environment files, logs, IDE state, and locally installed visual-review
tooling are ignored. Keep the Gradle Wrapper JAR tracked: it is build tooling, not a release artifact.
Before publishing, review `git status --short` and `git diff --cached --stat`; only stage intended project files.
Upload release JARs as release assets or workflow artifacts, not as source files in Git.

Ignoring or untracking a file does not remove it from older commits. Earlier commits may still contain
previously tracked personal data or machine paths. Removing past content requires a separate history rewrite;
these changes deliberately do not rewrite shared history.

## Publish the product website

The website source is in `docs`, including the representative screenshot named exactly `Ui.png`.
Publishing remains a repository-owner action:

1. Push the documentation commit to `master`.
2. Open [Settings → Pages](https://github.com/codeforce233/ip/settings/pages).
3. Select **Deploy from a branch**, branch **master**, and folder **/docs**, then click **Save**.
4. Wait for the Pages deployment in **Actions** to succeed.
5. Open [the Sage website](https://codeforce233.github.io/ip/) and verify the guide, navigation,
   formatting, screenshot, and layout at both desktop and narrow browser widths.
6. Check that [Ui.png](https://codeforce233.github.io/ip/Ui.png) loads directly and that the course's
   iP Showcase displays it after its next update.

The site includes its own layout and stylesheet; no additional theme is needed. Do not add
`.nojekyll`, because the site uses Jekyll to render Markdown and relative links.
If an image returns 404, check its exact filename case in the pushed repository. A local preview
does not establish that GitHub Pages is published.
