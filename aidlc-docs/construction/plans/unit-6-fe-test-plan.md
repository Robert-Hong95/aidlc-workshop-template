# Test Plan - Unit 6-FE (Store/Table 관리 UI)

## 1. API Client Tests

### stores.ts
- **TC-6FE-001**: getStores calls GET /api/admin/stores
- **TC-6FE-002**: createStore calls POST /api/admin/stores
- **TC-6FE-003**: updateStore calls PUT /api/admin/stores/{id}

### tables.ts
- **TC-6FE-004**: getTables calls GET /api/admin/tables with storeId
- **TC-6FE-005**: createTable calls POST /api/admin/tables
- **TC-6FE-006**: completeSession calls POST /api/admin/tables/{id}/complete-session
- **TC-6FE-007**: deleteOrder calls DELETE /api/admin/orders/{id}
- **TC-6FE-008**: getOrderHistory calls GET with filter params

## 2. Component Tests

### StoreForm
- **TC-6FE-009**: renders name field as required
- **TC-6FE-010**: shows validation error when name empty
- **TC-6FE-011**: prefills data when editing

### StoreList
- **TC-6FE-012**: renders store list
- **TC-6FE-013**: calls onEdit when edit button clicked

### StoreSwitcher
- **TC-6FE-014**: shows dropdown on click
- **TC-6FE-015**: calls onSwitch when store selected

### TableSetupForm
- **TC-6FE-016**: validates 4-digit PIN
- **TC-6FE-017**: validates positive table number
- **TC-6FE-018**: shows error for invalid PIN

### ViewToggle
- **TC-6FE-019**: toggles between list and card mode

### SessionCompleteDialog
- **TC-6FE-020**: shows total amount in dialog
- **TC-6FE-021**: calls onConfirm when confirmed

### OrderHistoryModal
- **TC-6FE-022**: renders with date presets
- **TC-6FE-023**: changes filter on preset click

### DatePresets
- **TC-6FE-024**: renders 3 preset buttons
- **TC-6FE-025**: calls onSelect with correct date range
