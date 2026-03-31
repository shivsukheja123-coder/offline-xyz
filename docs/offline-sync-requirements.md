# Tally Prime App: Offline + Auto Sync Design

## Goal
App mein Tally ka data **offline mode** mein bhi उपलब्ध रहे, aur internet wapas aane par data **automatically re-sync** ho jaye.

## Expected User Experience
1. User online hote hue latest data fetch karta hai.
2. App data ko local secure storage mein save karta hai.
3. Internet chala jaye tab bhi user offline page par saved data dekh sakta hai.
4. User offline changes kare to changes queue mein store hon.
5. Internet aate hi queued changes server par sync hon aur fresh current data pull ho.

## Core Modules

### 1) Local Data Store
- SQLite / IndexedDB / encrypted local DB use karein.
- Main tables/collections:
  - `company_master`
  - `ledgers`
  - `vouchers`
  - `stock_items`
  - `sync_meta`
  - `pending_operations`

### 2) Sync Metadata
`sync_meta` mein store karein:
- `last_successful_sync_at`
- `last_server_change_token` (or lastTxnId)
- `schema_version`

### 3) Offline Operation Queue
`pending_operations` item structure:
- `op_id` (uuid)
- `entity_type`
- `entity_id`
- `action` (create/update/delete)
- `payload`
- `created_at`
- `retry_count`

## Sync Strategy (Recommended)

### A. Initial Full Sync
- Login/first launch pe full dataset pull karo.
- Batch-wise save karo taaki app freeze na ho.

### B. Incremental Pull Sync
- Server se sirf delta changes lo (`last_server_change_token` ke basis par).
- Conflict safe merge:
  - master data: last-write-wins + audit logs
  - vouchers: strict validation + conflict review

### C. Push Sync
- Offline queue se operations server par FIFO order mein push karo.
- Success par queue item remove karo.
- Failure par exponential backoff retry.

### D. Reconciliation Cycle
- Push complete ke baad fresh pull sync karo.
- Final checksum/record-count compare se verify karo.

## Conflict Handling
- Same record offline aur server dono side update hua:
  1. `updated_at` compare
  2. Business rules apply
  3. Agar critical mismatch ho to “Needs Review” bucket mein bhejo

## Security Requirements
- Local DB encryption mandatory.
- Access token secure storage (Keychain/Keystore) mein rakho.
- Sync payload sign/validate karo (TLS + token rotation).

## Performance Guardrails
- Sync in chunks (e.g., 500 records/batch).
- Background sync worker use karo.
- UI par sync status chips:
  - `Offline`
  - `Syncing...`
  - `Synced at HH:MM`
  - `Conflict needs review`

## Minimal API Contract (Server)
- `GET /sync/full`
- `GET /sync/delta?changeToken=...`
- `POST /sync/push`
- `GET /sync/health`

## Pseudocode

```text
onAppStart():
  if isOnline():
    runSyncCycle()
  else:
    loadFromLocalDB()

runSyncCycle():
  pushPendingOperations()
  pullDeltaChanges()
  updateSyncMeta()

onEntityChangeOffline(change):
  saveToLocalDB(change)
  enqueuePendingOperation(change)

onNetworkRestored():
  runSyncCycle()
```

## Rollout Plan
1. Phase 1: Read-only offline cache.
2. Phase 2: Offline create/update + queue.
3. Phase 3: Conflict UI + reconciliation report.
4. Phase 4: Monitoring dashboards (sync lag, failure rate).

## Acceptance Criteria
- Offline state mein last synced data 100% visible.
- Internet restore par queued operations auto sync.
- Sync fail hone par data loss na ho.
- User ko clear sync state indicators milein.
