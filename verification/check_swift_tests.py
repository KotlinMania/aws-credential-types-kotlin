"""Require the six real AwsCredentialTypes Swift Testing cases and verify compiler settings."""
import argparse
import json
import re
import sys
from pathlib import Path

REQUIRED_CASES = [
    ("credentialsRoundTripAndZeroize", "credentialsRoundTripAndZeroize"),
    ("tokenRoundTripAndRedaction", "tokenRoundTripAndRedaction"),
    ("credentialsFutureRetainsItsValueAcrossAwaitAndPoll", "credentialsFutureRetainsItsValueAcrossAwaitAndPoll"),
    ("tokenFutureRetainsItsValueAcrossAwaitAndPoll", "tokenFutureRetainsItsValueAcrossAwaitAndPoll"),
    ("credentialsProviderResolvesIdentityOnCallerActor", "credentialsProviderResolvesIdentityOnCallerActor"),
    ("tokenProviderResolvesIdentityOnCallerActor", "tokenProviderResolvesIdentityOnCallerActor"),
]


def verify(text):
    clean = re.sub(r"\x1b\[[0-9;]*m", "", text)
    if not re.search(r"Test run with 6 tests[^\r\n]*passed", clean):
        raise SystemExit("Expected all six AwsCredentialTypes Swift tests to pass")
    if re.search(r"(?m)^.*(?:Test|Suite) .*\b(?:failed|skipped)\b", clean):
        raise SystemExit("AwsCredentialTypes Swift test failed or was skipped")

    for name, description in REQUIRED_CASES:
        label = r'(?:"' + re.escape(description) + r'"|' + re.escape(name) + r'\(\))'
        alias = r"(?: \(aka '" + re.escape(name) + r"\(\)'\))?"
        if len(re.findall(r"Test " + label + alias + r" passed\b", clean)) != 1:
            raise SystemExit("Expected exactly one passing execution of " + name)

    frontends = []
    for line in text.splitlines():
        if "swift-frontend" not in line or "-module-name " not in line:
            continue
        module = re.findall(r"-module-name\s+(\S+)", line)[-1]
        if module == "main" and "Package.swift" in line:
            continue
        modes = re.findall(r"-swift-version\s+(\S+)", line)
        mode = modes[-1] if modes else None
        if "-warnings-as-errors" not in line or not re.search(
            r"-target\s+arm64-apple-macos(?:x)?15\.0", line
        ):
            raise SystemExit("Missing Swift warning/deployment gate: " + module)
        if module == "SwiftTestHarnessTests":
            if mode not in ["6", "6.0"]:
                raise SystemExit("Authored Swift consumer must compile in mode 6")
        elif module == "SwiftTestHarnessPackageTests":
            if mode not in [None, "6", "6.0"]:
                raise SystemExit("Unexpected generated test-runner mode")
        elif mode not in ["5", "5.0"]:
            raise SystemExit("Published generated dependencies require mode 5: " + module)
        frontends.append(
            {
                "module": module,
                "swift_language_mode": mode,
                "warnings_as_errors": True,
                "macos_deployment_target": "15.0",
            }
        )
    required = {
        "AwsCredentialTypes",
        "KotlinRuntimeSupport",
        "KotlinCoroutineSupport",
        "SwiftTestHarnessTests",
    }
    if not required <= {row["module"] for row in frontends}:
        raise SystemExit("Missing actual dependency/consumer frontend evidence")
    return {
        "actual_behavior_tests": 6,
        "failures": 0,
        "skips": 0,
        "frontends": frontends,
        "generated_runner_classified_separately": True,
    }


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("log", type=Path)
    parser.add_argument("--output", type=Path)
    args = parser.parse_args()
    log_text = args.log.read_text(errors="replace")
    result = verify(log_text)
    output = args.output or args.log.with_suffix(".json")
    output.write_text(json.dumps(result, indent=2) + "\n")
    print(
        "Verified six actual AwsCredentialTypes Swift behaviors and effective compiler settings"
    )
