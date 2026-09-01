#!/usr/bin/env python3
import argparse
import json
import re
import subprocess
import sys
from pathlib import Path


def extract_json_block(text: str):
    match = re.search(r"```json\s*(\{.*?\})\s*```", text, flags=re.DOTALL)
    if not match:
        raise ValueError("No JSON block found in test/ui-test-plan.md")
    return json.loads(match.group(1))


def run_case(app_command: str, case: dict):
    proc = subprocess.run(
        app_command,
        shell=True,
        input=case.get("input", ""),
        text=True,
        capture_output=True,
        executable="/bin/bash",
    )
    actual = proc.stdout
    expected = case.get("expected", "")
    return proc, actual, expected


def main():
    parser = argparse.ArgumentParser(description="Run UI acceptance tests from a markdown test plan.")
    parser.add_argument("--plan", default="test/ui-test-plan.md", help="Path to the UI test plan markdown file.")
    args = parser.parse_args()

    plan_path = Path(args.plan)
    try:
        payload = extract_json_block(plan_path.read_text(encoding="utf-8"))
    except FileNotFoundError:
        print(f"Test plan not found: {plan_path}", file=sys.stderr)
        return 2
    except ValueError as exc:
        print(str(exc), file=sys.stderr)
        return 2

    app_command = payload.get("app_command")
    cases = payload.get("cases", [])
    if not app_command:
        print("The test plan must include an 'app_command' field.", file=sys.stderr)
        return 2
    if not cases:
        print("The test plan must include at least one case.", file=sys.stderr)
        return 2

    print("=== UI Test Session ===")
    for case in cases:
        case_id = case.get("id", "unnamed-case")
        aim = case.get("aim", "No description provided.")
        input_text = case.get("input", "")
        expected = case.get("expected", "")

        print(f"\n--- Case: {case_id} ---")
        print(f"Aim: {aim}")
        print("Input:")
        print(input_text)

        proc, actual, expected = run_case(app_command, case)
        print("Output:")
        print(actual)

        if actual != expected:
            print("\nFAIL: output mismatch")
            print("Expected:")
            print(expected)
            print("Actual:")
            print(actual)
            print("\nSession terminated immediately after failing case.")
            return 1

        print("Result: PASS")

    print("\n=== End of UI Test Session ===")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
