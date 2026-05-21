# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

Note: this report was generated before applying the provenance header corrections from
`port_lint_proposed_changes.md`; provenance-warning rows may be stale in this branch.

## Summary

- **Files Present:** 9/11 (81.8%)
- **Function parity:** 37/76 matched (target 78) — 48.7%
- **Class/type parity:** 17/29 matched (target 31) — 58.6%
- **Combined symbol parity:** 54/105 matched (target 109) — 51.4%
- **Average inline-code cosine:** 0.47 (function body across 7 matched files)
- **Average documentation cosine:** 0.77 (doc text across 7 matched files)
- **Cheat-zeroed Files:** 3
- **Critical Issues:** 7 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. provider.future

- **Target:** `future.Future [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 2
- **Priority Score:** 2040710.0
- **Functions:** 2/3 matched (target 4)
- **Missing functions:** `poll`
- **Types:** 1/4 matched
- **Missing types:** `BoxFuture`, `Output`, `ProvideToken`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/provider/future.rs` vs expected `provider/future.rs`
- **Proposed provenance header:** `// port-lint: source provider/future.rs` (current: `// port-lint: source src/provider/future.rs`)
- **Lint issues:** 1

### 2. provider.credentials

- **Target:** `provider.Credentials [PROVENANCE-FALLBACK]`
- **Similarity:** 0.14
- **Dependents:** 1
- **Priority Score:** 1081308.6
- **Functions:** 3/9 matched (target 7)
- **Missing functions:** `new`, `from`, `resolve_identity`, `cache_partition`, `reuses_cache_partition`, `account_id_can_be_retrieved_from_identity`
- **Types:** 2/4 matched (target 3)
- **Missing types:** `Result`, `Storer`
- **Tests:** 0/2 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/provider/credentials.rs` vs expected `provider/credentials.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/provider/credentials.rs` vs expected `provider/credentials.rs`
- **Proposed provenance header:** `// port-lint: source provider/credentials.rs` (current: `// port-lint: source src/provider/credentials.rs`)
- **Proposed provenance header:** `// port-lint: source provider/credentials.rs` (current: `// port-lint: source src/provider/credentials.rs`)
- **Lint issues:** 2

### 3. credentials_impl

- **Target:** `awscredentialtypes.CredentialsImpl [PROVENANCE-FALLBACK]`
- **Similarity:** 0.49
- **Dependents:** 0
- **Priority Score:** 103205.1
- **Functions:** 19/28 matched (target 37)
- **Missing functions:** `eq`, `fmt`, `new`, `expiry_mut`, `get_property_mut`, `get_property_mut_or_default`, `from`, `identity_inherits_feature_properties`, `from_credentials_adds_resolved_account_id_feature`
- **Types:** 3/4 matched (target 5)
- **Missing types:** `Foo`
- **Tests:** 2/4 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/credentials_impl.rs` vs expected `credentials_impl.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/credentials_impl.rs` vs expected `credentials_impl.rs`
- **Proposed provenance header:** `// port-lint: source credentials_impl.rs` (current: `// port-lint: source src/credentials_impl.rs`)
- **Proposed provenance header:** `// port-lint: source credentials_impl.rs` (current: `// port-lint: source src/credentials_impl.rs`)
- **Lint issues:** 2

### 4. credential_fn

- **Target:** `credentialfn.CredentialFn [PROVENANCE-FALLBACK]`
- **Similarity:** 0.30
- **Dependents:** 0
- **Priority Score:** 50907.0
- **Functions:** 3/8 matched (target 6)
- **Missing functions:** `fmt`, `assert_send_sync`, `creds_are_send_sync`, `check_is_str_ref`, `test_async_provider`
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Tests:** 1/5 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/credential_fn.rs` vs expected `credential_fn.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/credential_fn.rs` vs expected `credential_fn.rs`
- **Proposed provenance header:** `// port-lint: source credential_fn.rs` (current: `// port-lint: source src/credential_fn.rs`)
- **Proposed provenance header:** `// port-lint: source credential_fn.rs` (current: `// port-lint: source src/credential_fn.rs`)
- **Lint issues:** 2

### 5. provider.error

- **Target:** `error.Error [PROVENANCE-FALLBACK]`
- **Similarity:** 0.52
- **Dependents:** 0
- **Priority Score:** 21704.8
- **Functions:** 7/9 matched (target 18)
- **Missing functions:** `fmt`, `source`
- **Types:** 8/8 matched (target 14)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/provider/error.rs` vs expected `provider/error.rs`
- **Proposed provenance header:** `// port-lint: source provider/error.rs` (current: `// port-lint: source src/provider/error.rs`)
- **Lint issues:** 1

### 6. credential_feature

- **Target:** `credentialfeature.CredentialFeature [PROVENANCE-FALLBACK]`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 10200.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 1/2 matched (target 1)
- **Missing types:** `Storer`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/credential_feature.rs` vs expected `credential_feature.rs`
- **Proposed provenance header:** `// port-lint: source credential_feature.rs` (current: `// port-lint: source src/credential_feature.rs`)
- **Lint issues:** 1

### 7. lib

- **Target:** `awscredentialtypes.Lib [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10110.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/1 matched (target 0)
- **Missing types:** `Token`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/lib.rs` vs expected `lib.rs`
- **Proposed provenance header:** `// port-lint: source lib.rs` (current: `// port-lint: source src/lib.rs`)
- **Lint issues:** 1

### 8. attributes

- **Target:** `attributes.Attributes [PROVENANCE-FALLBACK]`
- **Similarity:** 0.86
- **Dependents:** 0
- **Priority Score:** 401.4
- **Functions:** 3/3 matched (target 6)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 2)
- **Missing types:** _none_
- **Tests:** 1/1 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/attributes.rs` vs expected `attributes.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/attributes.rs` vs expected `attributes.rs`
- **Proposed provenance header:** `// port-lint: source attributes.rs` (current: `// port-lint: source src/attributes.rs`)
- **Proposed provenance header:** `// port-lint: source attributes.rs` (current: `// port-lint: source src/attributes.rs`)
- **Lint issues:** 2

### 9. provider

- **Target:** `provider.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/provider.rs` vs expected `provider.rs`
- **Proposed provenance header:** `// port-lint: source provider.rs` (current: `// port-lint: source src/provider.rs`)
- **Lint issues:** 1

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

## Next Commands

Run `ast_distance` only as a direct clean invocation from the repository root. Do not wrap,
pipe, redirect, tail, or stack it behind shell control operators.
