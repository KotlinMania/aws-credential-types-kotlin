# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 11/11 (100.0%)
- **Function parity:** 77/95 matched (target 126) — 81.1%
- **Class/type parity:** 27/29 matched (target 45) — 93.1%
- **Combined symbol parity:** 104/124 matched (target 171) — 83.9%
- **Average inline-code cosine:** 0.31 (function body across 11 matched files)
- **Average documentation cosine:** 0.65 (doc text across 11 matched files)
- **Cheat-zeroed Files:** 1
- **Critical Issues:** 9 files with <0.60 function similarity
- **Needs Review:** 0 files with 0.60-0.84 function similarity
- **Excellent:** 2 files with >=0.85 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. provider.future

- **Target:** `future.Future`
- **Similarity:** 0.16
- **Dependents:** 2
- **Priority Score:** 2001008.4
- **Functions:** 6/6 matched (target 10)
- **Missing functions:** _none_
- **Types:** 4/4 matched (target 9)
- **Missing types:** _none_

### 2. provider.credentials

- **Target:** `provider.Credentials`
- **Similarity:** 0.13
- **Dependents:** 1
- **Priority Score:** 1041608.8
- **Functions:** 9/12 matched
- **Missing functions:** `provide_credentials`, `provide_credentials`, `resolve_identity`
- **Types:** 3/4 matched
- **Missing types:** `Storer`
- **Tests:** 2/2 matched

### 3. provider.token

- **Target:** `token.Token`
- **Similarity:** 0.11
- **Dependents:** 1
- **Priority Score:** 1021108.9
- **Functions:** 6/8 matched (target 9)
- **Missing functions:** `provide_token`, `resolve_identity`
- **Types:** 3/3 matched (target 4)
- **Missing types:** _none_
- **Tests:** 1/1 matched

### 4. credential_fn

- **Target:** `credentialfn.CredentialFn`
- **Similarity:** 0.23
- **Dependents:** 0
- **Priority Score:** 40907.7
- **Functions:** 4/8 matched
- **Missing functions:** `fmt`, `assert_send_sync`, `check_is_str_ref`, `test_async_provider`
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Tests:** 2/5 matched

### 5. token_fn

- **Target:** `tokenfn.TokenFn`
- **Similarity:** 0.25
- **Dependents:** 0
- **Priority Score:** 40907.5
- **Functions:** 4/8 matched (target 9)
- **Missing functions:** `fmt`, `assert_send_sync`, `check_is_str_ref`, `test_async_provider`
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Tests:** 2/5 matched

### 6. credentials_impl

- **Target:** `awscredentialtypes.CredentialsImpl`
- **Similarity:** 0.19
- **Dependents:** 0
- **Priority Score:** 33808.1
- **Functions:** 31/34 matched (target 43)
- **Missing functions:** `eq`, `fmt`, `from`
- **Types:** 4/4 matched (target 5)
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

### 8. credential_feature

- **Target:** `credentialfeature.CredentialFeature`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 10200.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 1/2 matched (target 1)
- **Missing types:** `Storer`

### 9. attributes

- **Target:** `attributes.Attributes`
- **Similarity:** 0.21
- **Dependents:** 0
- **Priority Score:** 407.9
- **Functions:** 3/3 matched (target 6)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Tests:** 1/1 matched

### 10. lib

- **Target:** `awscredentialtypes.Lib [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 110.0
- **Functions:** 0/0 matched (target 9)
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_

### 11. provider

- **Target:** `provider.Mod`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 0.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

