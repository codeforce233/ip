---
name: seedu-java-coding-standard
description: Enforce the SE-EDU Java coding standard (basic + intermediate) for all Java code in this project, including naming, layout, statements, and documentation conventions.
---

# SE-EDU Java coding standard

Follow the SE-EDU Java coding standard for this project at all times. In particular, apply the rules in the intermediate guide at https://se-education.org/guides/conventions/java/intermediate.html and use the Google Java Style Guide for cases not explicitly covered there.

## Required conventions

1. Package names must be all lowercase and use logical group names.
   - Example: `sage.core`, `sage.ui`, `sage.task`

2. Class, enum, and interface names must be nouns and use PascalCase.
   - Example: `TaskList`, `Storage`, `TaskType`

3. Method names must be verbs and use camelCase.
   - Example: `markAsDone`, `parseDateTime`, `showWelcome`

4. Variable and parameter names must be camelCase.
   - Example: `taskCount`, `fullCommand`, `byText`

5. Constant names must be uppercase with underscores.
   - Example: `MAX_TASKS`, `LINE`

6. Keep code layouts consistent and readable:
   - use one statement per line
   - keep braces consistent with standard Java style
   - break long lines when needed
   - keep related fields, constructors, and methods grouped logically

7. Write clear, purposeful code comments and JavaDoc:
   - document public classes and non-trivial public/protected methods
   - document non-trivial private methods and helper logic
   - keep comments factual and useful, not repetitive

8. Prefer descriptive, intention-revealing names over short cryptic names.
   - avoid single-letter variable names except in very small loop indices

9. Do not introduce code that ignores these rules or uses project-local naming conventions that conflict with the SE-EDU standard.

## Project requirement

All Java files in this repository must follow this standard. If code or generated changes violate these rules, fix the code before considering the task complete.
