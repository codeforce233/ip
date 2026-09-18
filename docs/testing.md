# Testing Sage

## Automated checks

Use Java 25 for every build and test run:

```sh
./gradlew check
python3 .codex/skills/test-ui/scripts/run-ui-tests.py --plan test/ui-test-plan.md
```

JUnit covers command parsing, list mutations, task/date formatting, notes, file encoding,
storage failures, and the application response boundary. GUI resource tests also check that
FXML and stylesheet resources exist and reference valid packaged paths. Java assertions are
enabled in tests. The coverage gate excludes only JavaFX controllers/views and the launcher;
it requires 90% line coverage and 80% branch coverage for the remaining code.

The CLI plan specifies exact expected output independently of the application. Each case
starts in a fresh temporary directory; tests never use the developer's real `data/sage.txt`.
See the [release checks](release.md#release-checks) for testing the packaged JAR after relocation.

## Manual GUI checklist

Use a fresh writable folder containing only the appropriate JAR. Do not test against personal data.
Repeat these checks on each supported operating system before publishing a release:

- [ ] Window title and all product labels say Sage.
- [ ] Keyboard focus starts in the input field; blank input cannot be sent.
- [ ] Enter and Send each submit a command exactly once.
- [ ] Add a task and a Unicode note; verify both with `list` and `find`.
- [ ] Mark and unmark a task; verify the displayed state changes.
- [ ] A malformed command displays an explicitly labelled error card, distinct without colour alone.
- [ ] Failed command input stays selected for correction; a corrected command succeeds.
- [ ] Up recalls the previous command when the input is empty.
- [ ] Commands opens and closes the reference without losing conversation history.
- [ ] Long messages wrap and stay readable at the minimum window size and a larger size.
- [ ] Long conversations scroll; the newest response is visible after sending a command.
- [ ] `bye` displays a farewell and closes the window; reopening preserves saved entries.
- [ ] A malformed data file produces a visible startup warning and cannot be overwritten by a command.

Automated tests exercise deletion, failed writes, and corrupt-file preservation using disposable
fixtures. No personal files need to be deleted or permission settings changed for manual testing.

## Verification record

Verified locally on 18 September 2026 using macOS Apple Silicon and Zulu Java 25.0.3:

- Clean build and all **95 JUnit tests passed**.
- Non-GUI coverage: **574/579 lines (99.1%)** and **260/266 branches (97.7%)**.
- All **18 exact-output CLI scenarios passed** against compiled sources and against each release JAR.
- All **6 Python acceptance-runner regression tests passed**.
- Both release archives passed launcher, resources, native architecture, relocation, Unicode,
  and add/reload/delete persistence checks.
- All seven macOS native libraries in each archive have the intended architecture; neither archive
  contains duplicate entries or personal task data.
- The Apple Silicon GUI started from an isolated temporary folder. A second startup excluded the
  JDK's JavaFX modules with `--limit-modules java.se,jdk.unsupported`; diagnostic logs confirmed that
  JavaFX classes and the initial graphics libraries were loaded from the JAR itself.

Remaining uncovered lines are the small CLI entry point and operating-system-dependent atomic-move
fallback/temporary-file cleanup handlers. Assertion-failure branches document internal invariants;
normal usage is not expected to enter them.

**Manual GUI interaction remains pending:** the computer-use plugin could not access the locked Mac.
The checklist above is deliberately unchecked; startup logs do not establish layout quality,
keyboard behaviour, resizing, or error visibility. Unlock the desktop and complete this checklist
before treating GUI acceptance as finished.

Windows, Linux, and Intel macOS were not executed locally. Their CI matrix is configured but has not
been run as part of this local change. A configured matrix is not a substitute for successful runs
and GUI checks on those operating systems.
