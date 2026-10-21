"""Parser fixtures only; these are not Swift product or consumer executions."""

import unittest

from verify_swift_tests import REQUIRED_CASES, verify_declared_cases, verify_log


def fixture(cases=REQUIRED_CASES, count=None):
    lines = [f"✔ Test {name}() passed after 0.001 seconds." for name in cases]
    lines.append(f"✔ Test run with {len(cases) if count is None else count} tests passed.")
    return "\n".join(lines)


class SwiftResultGuardTest(unittest.TestCase):
    def test_all_cases_pass_despite_empty_xctest_suite(self):
        log = "Executed 0 tests, with 0 failures.\n" + fixture()
        self.assertEqual(verify_log(log)["tests"], 6)

    def test_zero_cases_rejected(self):
        with self.assertRaises(ValueError):
            verify_log(fixture((), 0))

    def test_partial_suite_rejected(self):
        with self.assertRaises(ValueError):
            verify_log(fixture(REQUIRED_CASES[:2]))

    def test_duplicate_case_rejected(self):
        with self.assertRaises(ValueError):
            verify_log(fixture(REQUIRED_CASES + REQUIRED_CASES[:1]))

    def test_wrong_summary_rejected(self):
        with self.assertRaises(ValueError):
            verify_log(fixture(count=5))

    def test_skipped_case_rejected(self):
        log = fixture().replace(
            f"✔ Test {REQUIRED_CASES[0]}() passed",
            f"↷ Test {REQUIRED_CASES[0]}() skipped",
        )
        with self.assertRaises(ValueError):
            verify_log(log)

    def test_failed_case_cannot_be_hidden_by_summary(self):
        with self.assertRaises(ValueError):
            verify_log(fixture() + f"\n✘ Test {REQUIRED_CASES[0]}() failed.")

    def test_duplicate_summary_rejected(self):
        with self.assertRaises(ValueError):
            verify_log(fixture() + "\n✔ Test run with 6 tests passed.")

    def test_ansi_color_and_real_declared_inventory(self):
        verify_declared_cases()
        self.assertEqual(verify_log("\x1b[32m" + fixture().replace("✔", "✔\ufe0f") + "\x1b[0m")["tests"], 6)


if __name__ == "__main__":
    unittest.main()
