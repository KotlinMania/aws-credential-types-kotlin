"""Verify the required AWS consumer cases in actual Swift Testing output."""

import collections
import json
from pathlib import Path
import re
import sys


REQUIRED_CASES = (
    "credentialsRoundTripAndZeroize",
    "tokenRoundTripAndRedaction",
    "credentialsFutureRetainsItsValueAcrossAwaitAndPoll",
    "tokenFutureRetainsItsValueAcrossAwaitAndPoll",
    "credentialsProviderResolvesIdentityOnCallerActor",
    "tokenProviderResolvesIdentityOnCallerActor",
)
ANSI = re.compile(r"\x1b\[[0-?]*[ -/]*[@-~]")
PASSED = re.compile(r"^\s*[✔✓][\uFE0E\uFE0F]?\s+Test\s+(\w+)\(\)\s+passed\b", re.MULTILINE)
SUMMARY = re.compile(
    r"^\s*[✔✓][\uFE0E\uFE0F]?\s+Test run with (\d+) tests? passed\b", re.MULTILINE
)
FAILED_OR_SKIPPED = re.compile(
    r"^\s*\S+\s+Test\s+.+\s+(?:failed|skipped)\b", re.MULTILINE
)


def verify_log(text):
    text = ANSI.sub("", text)
    if FAILED_OR_SKIPPED.search(text):
        raise ValueError("Swift Testing reported a failed or skipped test")
    counts = collections.Counter(PASSED.findall(text))
    required = collections.Counter(REQUIRED_CASES)
    if counts != required:
        missing = sorted((required - counts).elements())
        unexpected = sorted((counts - required).elements())
        raise ValueError(
            f"Expected every required case once; missing={missing}, extra/duplicate={unexpected}"
        )
    summaries = SUMMARY.findall(text)
    if summaries != [str(len(REQUIRED_CASES))]:
        raise ValueError(f"Expected one six-case Swift Testing summary; found {summaries}")
    return {"tests": len(REQUIRED_CASES), "cases": sorted(counts), "skipped": 0}


def verify_declared_cases():
    tests = Path(__file__).resolve().parents[1] / "swift-test-harness" / "Tests"
    declared = []
    for source in sorted(tests.rglob("*.swift")):
        declared.extend(re.findall(r"@Test\s+func\s+(\w+)\s*\(", source.read_text()))
    if collections.Counter(declared) != collections.Counter(REQUIRED_CASES):
        raise ValueError(f"Required case inventory differs from declared tests: {declared}")


def main():
    if len(sys.argv) != 2:
        raise SystemExit("Usage: verify_swift_tests.py <actual-swift-test.log>")
    try:
        verify_declared_cases()
        result = verify_log(Path(sys.argv[1]).read_text(errors="replace"))
    except (OSError, ValueError) as error:
        raise SystemExit(f"Swift consumer acceptance failed: {error}") from error
    print(json.dumps(result, sort_keys=True))


if __name__ == "__main__":
    main()
