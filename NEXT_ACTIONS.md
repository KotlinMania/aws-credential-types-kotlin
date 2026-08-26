# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 11/11 (100.0%)
- **Function parity:** 54/75 matched (target 114) — 72.0%
- **Class/type parity:** 23/29 matched (target 41) — 79.3%
- **Combined symbol parity:** 77/104 matched (target 155) — 74.0%
- **Average inline-code cosine:** 0.47 (function body across 10 matched files)
- **Average documentation cosine:** 0.73 (doc text across 10 matched files)
- **Cheat-zeroed Files:** 1
- **Critical Issues:** 9 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. provider.future

- **Target:** `future.Future`
- **Similarity:** 0.34
- **Dependents:** 2
- **Priority Score:** 2030706.6
- **Functions:** 2/3 matched (target 8)
- **Missing functions:** `poll`
- **Types:** 2/4 matched (target 6)
- **Missing types:** `BoxFuture`, `Output`

### 2. provider.credentials

- **Target:** `provider.Credentials`
- **Similarity:** 0.42
- **Dependents:** 1
- **Priority Score:** 1031305.8
- **Functions:** 8/9 matched (target 12)
- **Missing functions:** `resolve_identity`
- **Types:** 2/4 matched (target 3)
- **Missing types:** `Result`, `Storer`
- **Tests:** 2/2 matched

### 3. provider.token

- **Target:** `provider.Token`
- **Similarity:** 0.33
- **Dependents:** 1
- **Priority Score:** 1021006.8
- **Functions:** 5/7 matched (target 8)
- **Missing functions:** `from`, `resolve_identity`
- **Types:** 3/3 matched (target 4)
- **Missing types:** _none_
- **Tests:** 1/1 matched

### 4. credentials_impl

- **Target:** `awscredentialtypes.CredentialsImpl`
- **Similarity:** 0.55
- **Dependents:** 0
- **Priority Score:** 73204.5
- **Functions:** 22/28 matched (target 40)
- **Missing functions:** `eq`, `fmt`, `expiry_mut`, `get_property_mut`, `get_property_mut_or_default`, `from`
- **Types:** 3/4 matched (target 5)
- **Missing types:** `Foo`
- **Tests:** 4/4 matched

### 5. token_fn

- **Target:** `tokenfn.TokenFn`
- **Similarity:** 0.31
- **Dependents:** 0
- **Priority Score:** 50906.9
- **Functions:** 3/8 matched (target 7)
- **Missing functions:** `fmt`, `assert_send_sync`, `creds_are_send_sync`, `check_is_str_ref`, `test_async_provider`
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Tests:** 1/5 matched

### 6. credential_fn

- **Target:** `credentialfn.CredentialFn`
- **Similarity:** 0.34
- **Dependents:** 0
- **Priority Score:** 40906.6
- **Functions:** 4/8 matched (target 7)
- **Missing functions:** `fmt`, `assert_send_sync`, `check_is_str_ref`, `test_async_provider`
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Tests:** 2/5 matched

### 7. provider.error

- **Target:** `error.Error`
- **Similarity:** 0.52
- **Dependents:** 0
- **Priority Score:** 21704.8
- **Functions:** 7/9 matched (target 18)
- **Missing functions:** `fmt`, `source`
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
- **Similarity:** 0.86
- **Dependents:** 0
- **Priority Score:** 401.4
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
- **Functions:** 0/0 matched (target 8)
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_

### 11. provider

- **Target:** `provider.Mod [STUB]`
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

