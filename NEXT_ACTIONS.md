# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 11/11 (100.0%)
- **Function parity:** 82/95 matched (target 140) — 86.3%
- **Class/type parity:** 29/29 matched (target 48) — 100.0%
- **Combined symbol parity:** 111/124 matched (target 188) — 89.5%
- **Average inline-code cosine:** 0.14 (function body across 11 matched files)
- **Average documentation cosine:** 0.90 (doc text across 11 matched files)
- **Cheat-zeroed Files:** 7
- **Critical Issues:** 10 files with <0.60 function similarity
- **Needs Review:** 0 files with 0.60-0.84 function similarity
- **Excellent:** 1 files with >=0.85 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. provider.future

- **Target:** `future.Future [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 2
- **Priority Score:** 2001010.0
- **Functions:** 6/6 matched (target 10)
- **Missing functions:** _none_
- **Types:** 4/4 matched (target 9)
- **Missing types:** _none_

### 2. provider.credentials

- **Target:** `provider.Credentials [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1021610.0
- **Functions:** 10/12 matched (target 13)
- **Missing functions:** `provide_credentials`, `provide_credentials`
- **Types:** 4/4 matched (target 5)
- **Missing types:** _none_
- **Tests:** 2/2 matched

### 3. provider.token

- **Target:** `token.Token`
- **Similarity:** 0.11
- **Dependents:** 1
- **Priority Score:** 1011108.9
- **Functions:** 7/8 matched (target 10)
- **Missing functions:** `provide_token`
- **Types:** 3/3 matched (target 4)
- **Missing types:** _none_
- **Tests:** 1/1 matched

### 4. token_fn

- **Target:** `tokenfn.TokenFn [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 30910.0
- **Functions:** 5/8 matched (target 12)
- **Missing functions:** `assert_send_sync`, `check_is_str_ref`, `test_async_provider`
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Tests:** 2/5 matched

### 5. credential_fn

- **Target:** `credentialfn.CredentialFn [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 30910.0
- **Functions:** 5/8 matched (target 11)
- **Missing functions:** `assert_send_sync`, `check_is_str_ref`, `test_async_provider`
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Tests:** 2/5 matched

### 6. credentials_impl

- **Target:** `awscredentialtypes.CredentialsImpl [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 23810.0
- **Functions:** 32/34 matched (target 48)
- **Missing functions:** `eq`, `fmt`
- **Types:** 4/4 matched (target 6)
- **Missing types:** _none_
- **Tests:** 4/4 matched

### 7. provider.error

- **Target:** `error.Error`
- **Similarity:** 0.18
- **Dependents:** 0
- **Priority Score:** 22408.2
- **Functions:** 14/16 matched (target 20)
- **Missing functions:** `fmt`, `fmt`
- **Types:** 8/8 matched (target 14)
- **Missing types:** _none_

### 8. attributes

- **Target:** `attributes.Attributes`
- **Similarity:** 0.21
- **Dependents:** 0
- **Priority Score:** 407.9
- **Functions:** 3/3 matched (target 6)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Tests:** 1/1 matched

### 9. credential_feature

- **Target:** `credentialfeature.CredentialFeature`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 200.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 2/2 matched
- **Missing types:** _none_

### 10. lib

- **Target:** `awscredentialtypes.Lib [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 110.0
- **Functions:** 0/0 matched (target 10)
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_

### 11. provider

- **Target:** `provider.Mod [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Lint issues:** 1

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

