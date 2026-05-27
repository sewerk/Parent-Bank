# Parent Bank - Product Requirements Document

## Project Title
**Parent Bank** - A Family Financial Management Application (Mobile & Web)

## Target Audience

### Primary Users
- **Parents/Guardians**: Adults who want to teach their children about money management and provide them with controlled access to funds
- **Children/Teens**: Ages 6-17 who are learning financial responsibility under parental supervision

### User Personas
1. **Tech-Savvy Parents**: Looking for digital solutions to manage family finances and teach financial literacy
2. **First-Time Allowance Givers**: Parents starting to give their children regular pocket money
3. **Multi-Child Families**: Parents managing finances for multiple children across different age groups
4. **Financial Literacy Advocates**: Parents who want to actively teach their children about saving, spending, money management, and how interest works to encourage savings behavior

## Objective

Parent Bank aims to provide a secure, intuitive, and educational platform for families to manage children's finances. The app empowers parents to control and oversee their children's financial activities while giving children visibility and limited autonomy in managing their own accounts.

### Primary Goals
1. Enable parents to create and manage virtual bank accounts for their children
2. Facilitate controlled financial transactions between parents and children
3. Teach children financial responsibility through tracked transactions and balance management
4. Educate children about interest rates and how savings can generate profits over time
5. Teach concepts of loans and credit through negative balance (overdraft) scenarios
6. Provide a safe environment for children to learn money management without real-world financial risks
7. Work fully offline first, then sync family financial data securely across multiple devices using Firebase
8. Support multiple languages (English and Polish) from the beginning

## Features

### 1. Family Management
- **Create Family**: Set up a family unit with a unique identifier
- **Add Family Members**: Add parents and children to the family unit
- **Member Roles**: Distinguish between Parent (admin) and Child (limited) roles
- **Profile Management**: Edit member information (name, age, profile picture)
- **Multi-Device Support**: Access family data from multiple devices

### 2. Account Management (Parent)
- **Create Child Accounts**: Set up individual accounts for each child
- **View All Accounts**: Dashboard showing all children's accounts and balances
- **Account Settings**: Configure account limits, permissions, and settings
- **Delete/Archive Accounts**: Remove or archive child accounts when needed

### 3. Transaction Management (Parent)
- **Add Income**: Record income transactions (allowance, gifts, rewards) to child accounts
- **Add Expenses**: Record expenses from child accounts (purchases, withdrawals)
- **Instant Processing**: Parent-created transactions are processed immediately without confirmation
- **Transaction Details**: Include title, amount, date, category, and optional notes
- **Review Child Requests**: Approve or deny transaction requests initiated by children
- **Bulk Transactions**: Apply the same transaction to multiple children simultaneously
- **Loan/Credit Support**: Balances can become negative to teach children about loans and credit concepts

### 4. Transaction Management (Child)
- **Request Transactions**: Submit expense or withdrawal requests to parents
- **Pending Status**: Track status of pending transaction requests
- **View Notifications**: Receive notifications when transactions are approved/denied
- **Transaction Categories**: Select from predefined expense categories

### 5. Scheduled Transactions
- **Create Recurring Transactions**: Set up automated recurring transactions (e.g., weekly allowance, monthly interest)
- **Fixed or Percentage-Based**: Support both fixed amount transactions and percentage-based calculations (e.g., 5% monthly interest on account balance)
- **Interest Education**: Teach children how interest works and that savings generate profits over time
- **Frequency Options**: Daily, weekly, bi-weekly, monthly
- **Start/End Dates**: Define when scheduled transactions begin and optionally end
- **Edit/Pause/Delete**: Modify or temporarily pause scheduled transactions
- **Notification Reminders**: Alert parents before scheduled transactions execute

### 6. Transaction History
- **Per-Account History**: View transaction history for individual accounts only
- **Transaction Details**: Tap any transaction to view full details

### 7. Child Dashboard
- **View Balance**: Display current account balance prominently
- **Recent Activity**: Show recent transactions (inflows, expenses, transfers)
- **Request Transaction**: Quick access to request money or record expenses
- **Savings Goals**: (Future enhancement) Set and track savings goals
- **Transaction History**: View personal transaction history

### 8. Offline-First with Firebase Integration
- **Offline-First Design**: App works fully offline as the first implementation step
- **Local Storage**: All data stored locally using SQLDelight or Realm
- **Firebase Integration**: Added after offline functionality is complete and stable
- **Real-time Sync**: Synchronize family data across all devices in real-time once Firebase is integrated
- **Authentication**: Secure user authentication using Firebase Auth
- **Cloud Storage**: Store transaction history, family data, and user profiles
- **Data Security**: Encrypt sensitive data and implement proper security rules

### 9. Notifications
- **Push Notifications**: Alert users of important events
- **Parent Notifications**: New transaction requests from children, scheduled transaction reminders
- **Child Notifications**: Transaction approvals/denials, new income received
- **In-App Notifications**: Notification center within the app

### 10. Settings & Configuration
- **App Settings**: Theme, notification preferences
- **Security Settings**: PIN/biometric authentication
- **Family Settings**: Family name, default currency, family code for joining
- **Multi-Language Support**: Automatic language detection from system settings (English, Polish)

## Usage Flow

### Parent User Flow

#### Initial Setup
1. Download and install Parent Bank app
2. Create account with email/password or social login (Firebase Auth)
3. Create a new family unit or join an existing one
4. Set up family profile (family name, icon)
5. Add children to the family (name, age, initial balance)
6. Configure notification preferences

#### Daily Operations
1. **Check Dashboard**: View all children's accounts and recent activity
2. **Add Transactions**:
   - Select child account
   - Choose transaction type (income/expense)
   - Enter amount, title, category, date
   - Add optional notes
   - Confirm transaction (processed immediately)
3. **Review Requests**:
   - View pending transaction requests from children
   - Review details
   - Approve or deny with optional message
4. **Schedule Allowance**:
   - Navigate to Scheduled Transactions
   - Create new recurring transaction
   - Set amount, frequency, start date
   - Assign to child(ren)
   - Save schedule
5. **View History**:
   - Access transaction history per account
   - Review spending patterns

### Child User Flow

#### Initial Setup
1. Receive invite/family code from parent
2. Download and install Parent Bank app
3. Join parent-created account using family code
4. Complete profile setup

#### Daily Operations
1. **Check Balance**: Open app to view current balance
2. **View Activity**: Review recent transactions
3. **Request Transaction**:
   - Tap "Request Transaction" button
   - Select transaction type (expense/withdrawal)
   - Enter amount, title, category
   - Add optional note
   - Submit request
   - Wait for parent approval
4. **Track Requests**: Check status of pending requests
5. **Receive Notifications**: Get notified when transactions are approved/denied

## Technology and Architecture

### Technology Stack

#### Kotlin Multiplatform (KMP)
- **Shared Business Logic**: Core functionality shared between Android, iOS, and Web
- **Common Modules**:
  - Domain layer (business logic, use cases)
  - Data layer (repositories, data sources)
  - Firebase integration
  - Transaction processing logic
  - Validation and business rules

#### Platform-Specific Code
- **Android**:
  - Jetpack Compose for UI
  - Material Design 3 components
  - Android-specific permissions and services
- **iOS**:
  - SwiftUI for UI
  - iOS design guidelines
  - iOS-specific integrations
- **Web**:
  - Compose for Web (Wasm/JS targets)
  - Responsive web design
  - Browser-specific APIs and storage

#### Backend & Services
- **Firebase**:
  - Firebase Authentication (user management)
  - Cloud Firestore (real-time database)
  - Firebase Cloud Messaging (push notifications)
  - Firebase Storage (optional: for profile pictures)
  - Firebase Security Rules (data protection)

### Architecture Pattern

#### Clean Architecture
```
┌─────────────────────────────────────┐
│         Presentation Layer          │
│   (Android: Compose, iOS: SwiftUI,  │
│      Web: Compose for Web)          │
│         Platform-Specific           │
└─────────────────────────────────────┘
                  ↕
┌─────────────────────────────────────┐
│          Domain Layer               │
│   (Use Cases, Business Logic)       │
│          Shared (KMP)               │
└─────────────────────────────────────┘
                  ↕
┌─────────────────────────────────────┐
│           Data Layer                │
│ (Repositories, Data Sources, DTOs)  │
│          Shared (KMP)               │
└─────────────────────────────────────┘
                  ↕
┌─────────────────────────────────────┐
│      External Services              │
│  (Firebase, Local Storage)          │
│    Platform-Specific Wrappers       │
└─────────────────────────────────────┘
```

#### Key Components

**Shared Module (commonMain)**
- Domain Models: Family, User, Account, Transaction, ScheduledTransaction
- Use Cases: CreateTransaction, ApproveRequest, ScheduleRecurringTransaction, etc.
- Repositories (Interfaces): FamilyRepository, TransactionRepository, UserRepository
- Firebase Client (expect/actual pattern)

**Platform Modules (androidMain, iosMain, webMain)**
- UI Implementation
- Firebase SDK integration (actual implementations)
- Local database (SQLDelight or Realm)
- Platform-specific utilities

## Error Handling

### Error Categories

#### 1. Network Errors
- **Scenario**: No internet connection, Firebase unavailable
- **Handling**:
  - Display user-friendly error message
  - Enable offline mode with local caching
  - Queue transactions for sync when connection restored
  - Show sync status indicator

#### 2. Authentication Errors
- **Scenario**: Login failed, session expired, unauthorized access
- **Handling**:
  - Redirect to login screen
  - Clear local session data
  - Display appropriate error message
  - Provide password reset option

#### 3. Validation Errors
- **Scenario**: Invalid input (negative amounts, empty fields, future dates)
- **Handling**:
  - Real-time field validation
  - Display inline error messages
  - Highlight invalid fields
  - Prevent form submission until valid

#### 4. Business Logic Errors
- **Scenario**: Insufficient balance, duplicate transaction, invalid permissions
- **Handling**:
  - Display clear error message explaining the issue
  - Suggest corrective action
  - Log error for debugging
  - Prevent invalid state

#### 5. Data Sync Errors
- **Scenario**: Conflict between local and remote data, sync failure
- **Handling**:
  - Implement conflict resolution strategy (last-write-wins or manual resolution)
  - Retry failed syncs with exponential backoff
  - Notify user of sync issues
  - Maintain data integrity

### Error Handling Strategy

#### Global Error Handler
```kotlin
sealed class AppError {
    data class NetworkError(val message: String) : AppError()
    data class AuthError(val message: String) : AppError()
    data class ValidationError(val field: String, val message: String) : AppError()
    data class BusinessError(val message: String) : AppError()
    data class UnknownError(val throwable: Throwable) : AppError()
}

interface ErrorHandler {
    fun handleError(error: AppError)
    fun logError(error: AppError)
}
```

#### User-Facing Messages
- Use clear, non-technical language
- Provide actionable next steps
- Avoid exposing technical details
- Include support contact for persistent issues

#### Logging & Monitoring
- Log all errors with context (user ID, action, timestamp)
- Use Firebase Crashlytics for crash reporting
- Monitor error rates and patterns
- Set up alerts for critical errors

#### Retry Mechanisms
- Implement exponential backoff for network retries
- Maximum retry attempts: 3-5 times
- User option to manually retry failed operations
- Clear indication of retry status

## Configuration

### Environment Configuration

#### Development
- Firebase project: `parent-bank-dev`
- Debug logging enabled
- Mock data available

#### Production
- Firebase project: `parent-bank-prod`
- Error logging only
- Production Firebase services
- Analytics enabled

### App Configuration

#### Build Variants
```kotlin
// Android build.gradle
buildTypes {
    debug {
        applicationIdSuffix = ".debug"
        isDebuggable = true
    }
    release {
        isMinifyEnabled = true
        proguardFiles(...)
    }
}
```

### API Keys & Secrets
- Store API keys in secure configuration files
- Use environment variables for sensitive data
- Never commit secrets to version control
- Rotate keys periodically

## Rules

### Development Guidelines

1. **Design Principles**
   - **Fail Fast**: Catch errors early and make them visible; prefer crashing over silent failures or corrupted state
   - **KISS over DRY**: Keep It Simple, Stupid - prioritize simplicity and readability over Don't Repeat Yourself; duplicate code is better than complex abstractions
   - **Offline-First**: Build full offline functionality before integrating Firebase sync
   - **Multi-Language Ready**: Design all UI and content with internationalization in mind from day one

2. **Architecture & Code Organization**
   - Use Kotlin Multiplatform best practices with maximum code sharing in common module
   - Follow clean architecture: separate UI, Domain, and Data layers
   - Use `expect`/`actual` pattern for platform-specific implementations
   - Keep UI layer platform-specific (Compose for Android, SwiftUI for iOS, Compose for Web)
   - Use dependency injection and write testable code

3. **Security**
   - Store API keys securely using environment variables (never hardcode)
   - Validate all inputs on both client and server side
   - Use Firebase Security Rules effectively
   - Encrypt sensitive data and implement rate limiting
   - Never log sensitive user information or expose internal IDs

4. **Error Handling & Logging**
   - Follow fail-fast principle: let the app crash early on critical errors rather than continuing in invalid state
   - Use crash reporting tools (Firebase Crashlytics) to catch and fix issues quickly
   - Display user-friendly error messages for recoverable errors (avoid technical jargon)
   - Log important events with appropriate levels
   - Validate inputs aggressively and fail immediately on invalid data
   - Never fail silently; provide clear feedback

5. **Performance & Optimization**
   - Lazy load data and implement pagination for large lists
   - Cache data locally for offline support
   - Minimize Firebase reads/writes
   - Optimize images and manage memory properly
   - Don't block UI thread with heavy operations

6. **Testing**
   - Write unit tests for business logic (>80% coverage for shared code)
   - Create integration tests for repositories
   - Implement UI tests for critical flows
   - Test edge cases and error scenarios
   - Never deploy untested code

7. **Platform Guidelines & UX**
   - Follow Material Design 3 for Android
   - Follow Human Interface Guidelines for iOS
   - Follow responsive web design principles for Web
   - Respect platform-specific navigation patterns
   - Implement proper accessibility (TalkBack/VoiceOver)
   - Provide clear loading states and user feedback

8. **Code Quality**
   - Use meaningful commit messages and feature branches
   - Review code before merging
   - Document complex logic with clear comments
   - Keep the app simple and intuitive (avoid unnecessary complexity)
   - Maintain up-to-date documentation

9. **Internationalization (i18n)**
   - Support English and Polish from launch
   - Use resource files for all user-facing strings
   - Automatically detect language from system settings (no manual language switcher)
   - Design UI to accommodate different text lengths
   - Handle date, time, and currency formats appropriately based on system locale
   - Test all features in both languages

### Business Rules

1. **Transaction Authorization**
   - Parent-initiated transactions process immediately without confirmation
   - Child-initiated transactions require parent approval
   - Only parents can approve/deny transaction requests
   - Transactions have separate types: Income (adds to balance) and Expense (subtracts from balance)

2. **Account Access & Permissions**
   - Parents have full visibility of all child accounts
   - Children can only view and manage their own account
   - Children cannot view siblings' account details
   - Children cannot create scheduled transactions

3. **Account Balance & Credit**
   - Balances can become negative (teaching loan/credit concepts)
   - Negative balance represents money owed (overdraft/credit)
   - Parents can set optional balance limits per account
   - Visual indicators should clearly show negative balances

4. **Data Integrity**
   - Maintain complete transaction audit trail
   - Never allow deletion of transaction history
   - Validate all operations before processing
   - App must work fully offline before implementing sync
   - Sync data in real-time across devices (once Firebase is integrated)

## Tasks

### Development Approach: Vertical Slices

This project follows an **iterative, vertical-slice** development approach. Instead of building
all repositories first, then all use cases, then all UI screens (horizontal layers), each slice
delivers a **complete, testable feature** from database through domain logic to UI.

**Why vertical slices?**
- After each slice, the app is **runnable and testable** end-to-end
- PRs stay small (~300-500 lines), making code review effective
- Issues are caught early within a narrow scope, not after building an entire layer
- Each slice has **clear acceptance criteria** — you can verify the feature works before moving on

**Rules for each slice:**
- One slice = one PR with a single goal
- Each PR includes unit tests for new business logic
- The app must compile and run after every merge
- No mixing of unrelated features in one slice
- Android UI first (Compose Multiplatform), then iOS/Web adaptations

**What's already done (Phase 0):**
- [x] KMP project structure (commonMain, androidMain, iosMain source sets)
- [x] Gradle build configuration with Compose Multiplatform
- [x] Domain models: Family, User, Account, Transaction, ScheduledTransaction
- [x] Enums: UserRole, TransactionType, TransactionStatus, Frequency
- [x] Repository interfaces (Family, User, Account, Transaction, ScheduledTransaction)
- [x] SQLDelight schemas for all entities
- [x] Koin DI setup (platform modules, data module, domain module)
- [x] Utility classes: Outcome, Logger, AppException
- [x] i18n infrastructure: Language, SystemLanguage, StringProvider, Strings
- [x] CI/CD: GitHub Actions (Android build/test + iOS framework build)
- [x] Base UseCase classes

---

### Slice 1: Family & User Creation

**Goal:** A user can create a family and add members (parent + children) through the UI.

#### Data Layer
- [ ] Implement `FamilyRepositoryImpl` using SQLDelight (CRUD, observe family by ID)
- [ ] Implement `UserRepositoryImpl` using SQLDelight (CRUD, get users by family)
- [ ] Register repository implementations in Koin data module

#### Domain Layer
- [ ] `CreateFamilyUseCase` — creates family with generated code, adds creating user as parent
- [ ] `AddFamilyMemberUseCase` — adds a child or parent to existing family
- [ ] `GetFamilyMembersUseCase` — returns all members of a family
- [ ] Input validation: family name not empty, member name not empty, fail-fast on invalid data

#### Presentation Layer
- [ ] Create family setup screen (Compose): enter family name, create family
- [ ] Create add member screen: enter name, select role (Parent/Child), add age for children
- [ ] Family members list view showing added members
- [ ] `FamilySetupViewModel` connecting UI to use cases
- [ ] Basic Compose Navigation: setup flow → members list

#### Tests
- [ ] Unit tests for `CreateFamilyUseCase` and `AddFamilyMemberUseCase`
- [ ] Unit tests for repository implementations (using in-memory SQLDelight driver)

#### Acceptance Criteria
- [ ] User can create a family with a name
- [ ] User can add parent and child members to the family
- [ ] Members list displays all added members with their roles
- [ ] Data persists across app restarts (SQLDelight)

---

### Slice 2: Navigation & Role-Based Routing

**Goal:** After family setup, users are routed to the correct dashboard based on their role (Parent vs Child).

#### Presentation Layer
- [ ] Implement role selection screen: "Who are you?" with list of family members
- [ ] Create parent dashboard skeleton (empty screen with title and member name)
- [ ] Create child dashboard skeleton (empty screen with title and member name)
- [ ] Navigation graph: Family setup → Role selection → Parent/Child dashboard
- [ ] `NavigationViewModel` or navigation state management
- [ ] Store selected user in local session (current user context)

#### Domain Layer
- [ ] `GetUserByIdUseCase` — retrieve user by ID for session context
- [ ] `SetCurrentUserUseCase` — store active user selection locally

#### Tests
- [ ] Unit test for role-based routing logic

#### Acceptance Criteria
- [ ] After creating family, user selects their profile from member list
- [ ] Parent member sees parent dashboard
- [ ] Child member sees child dashboard
- [ ] Back navigation works correctly

---

### Slice 3: Child Account Creation (Parent)

**Goal:** A parent can create bank accounts for their children and see them on the dashboard.

#### Data Layer
- [ ] Implement `AccountRepositoryImpl` using SQLDelight (create, get by ID, get by family, get by child, update balance)
- [ ] Register in Koin data module

#### Domain Layer
- [ ] `CreateChildAccountUseCase` — parent creates account for a child (initial balance = 0)
- [ ] `GetAllAccountsForFamilyUseCase` — returns all accounts in the family
- [ ] Validation: only parents can create accounts, one account per child, fail-fast on invalid input

#### Presentation Layer
- [ ] Parent dashboard: display list of child accounts with names and balances
- [ ] "Add account" button → create account screen (select child, confirm)
- [ ] `ParentDashboardViewModel` with accounts list state
- [ ] Account card composable showing child name and balance (formatted as currency)

#### Tests
- [ ] Unit tests for `CreateChildAccountUseCase` (happy path + validation)
- [ ] Unit tests for `AccountRepositoryImpl`

#### Acceptance Criteria
- [ ] Parent sees "no accounts yet" state on empty dashboard
- [ ] Parent can create an account for any child in the family
- [ ] Dashboard shows all child accounts with name and balance (0.00)
- [ ] Cannot create duplicate accounts for the same child

---

### Slice 4: Add Income Transaction

**Goal:** A parent can add income (allowance, gift) to a child's account and the balance updates.

#### Data Layer
- [ ] Implement `TransactionRepositoryImpl` using SQLDelight (create, get by account, observe by account)
- [ ] Register in Koin data module

#### Domain Layer
- [ ] `CreateTransactionUseCase` — creates an INCOME transaction, updates account balance
- [ ] Validation: amount must be positive, account must exist, only parent can create directly
- [ ] Transaction is created with status APPROVED (parent-initiated = instant)

#### Presentation Layer
- [ ] Account detail screen: shows current balance and "Add Income" button
- [ ] Add transaction screen: enter amount, title, optional note
- [ ] After adding, return to account detail with updated balance
- [ ] `TransactionViewModel` handling transaction creation

#### Tests
- [ ] Unit tests for `CreateTransactionUseCase` (income path)
- [ ] Test balance update after income transaction

#### Acceptance Criteria
- [ ] Parent taps a child account → sees account detail with balance
- [ ] Parent adds income → balance increases by the amount
- [ ] Transaction title and amount are stored
- [ ] Balance on parent dashboard updates after adding income

---

### Slice 5: Add Expense Transaction

**Goal:** A parent can record expenses from a child's account. Balance can go negative (loan/credit concept).

#### Domain Layer
- [ ] Extend `CreateTransactionUseCase` to handle EXPENSE type
- [ ] Validation: amount must be positive, balance is allowed to go negative
- [ ] EXPENSE subtracts from balance

#### Presentation Layer
- [ ] Add "Add Expense" button to account detail screen
- [ ] Transaction type selector on add transaction screen (Income / Expense)
- [ ] Visual indicator for negative balance (different color/icon)

#### Tests
- [ ] Unit tests for expense transaction creation
- [ ] Test balance going negative
- [ ] Test that negative balance is displayed correctly

#### Acceptance Criteria
- [ ] Parent can add an expense to a child's account
- [ ] Balance decreases after expense
- [ ] Balance can go below zero (negative)
- [ ] Negative balances are visually distinct (color/icon)

---

### Slice 6: Transaction History

**Goal:** Parents can view the full transaction history for each child account.

#### Domain Layer
- [ ] `GetTransactionHistoryUseCase` — returns transactions for an account, ordered by date descending

#### Presentation Layer
- [ ] Transaction history list on account detail screen (below balance)
- [ ] Each transaction row shows: date, title, amount (green for income, red for expense), running balance
- [ ] Transaction detail on tap (title, amount, type, date, note, who initiated)
- [ ] Empty state when no transactions exist

#### Tests
- [ ] Unit test for `GetTransactionHistoryUseCase` (ordering, filtering)

#### Acceptance Criteria
- [ ] Account detail screen shows list of all transactions
- [ ] Income shown in green/positive, expenses in red/negative
- [ ] Transactions ordered newest first
- [ ] Tapping a transaction shows full details

---

### Slice 7: Child Dashboard

**Goal:** A child can log in and see their own account balance and transaction history.

#### Presentation Layer
- [ ] Child dashboard: display own account balance prominently
- [ ] Recent transactions list (reuse transaction history composables from Slice 6)
- [ ] Child cannot see other children's accounts
- [ ] Child cannot add transactions directly (no "Add Income/Expense" buttons)

#### Domain Layer
- [ ] `GetAccountForChildUseCase` — returns the account for the current child user
- [ ] Permission check: child can only access own account data

#### Tests
- [ ] Unit test for `GetAccountForChildUseCase` (returns only own account)
- [ ] Test that child cannot access sibling accounts

#### Acceptance Criteria
- [ ] Child sees their balance on the dashboard
- [ ] Child sees their transaction history
- [ ] Child cannot see other family members' accounts
- [ ] No transaction creation buttons visible for child role

---

### Slice 8: Child Transaction Requests & Parent Approval

**Goal:** Children can request transactions. Parents review and approve or deny them.

#### Domain Layer
- [ ] `RequestTransactionUseCase` — child creates a transaction with status PENDING
- [ ] `GetPendingTransactionsUseCase` — returns all PENDING transactions in the family
- [ ] `ApproveTransactionUseCase` — parent approves, status → APPROVED, balance updated
- [ ] `DenyTransactionUseCase` — parent denies, status → DENIED, no balance change
- [ ] Validation: only children can create requests, only parents can approve/deny

#### Presentation Layer
- [ ] Child dashboard: "Request Transaction" button → request form (amount, title, type, note)
- [ ] Child: pending requests list with status indicators
- [ ] Parent dashboard: notification badge / section for pending requests
- [ ] Parent: approval screen showing request details with Approve / Deny buttons
- [ ] `PendingRequestsViewModel`

#### Tests
- [ ] Unit tests for the full request → approve flow
- [ ] Unit tests for the request → deny flow
- [ ] Test that only parents can approve/deny
- [ ] Test that only children can create requests

#### Acceptance Criteria
- [ ] Child submits a transaction request → appears as PENDING
- [ ] Parent sees pending requests on dashboard
- [ ] Parent approves → child balance updates, status = APPROVED
- [ ] Parent denies → balance unchanged, status = DENIED
- [ ] Child sees updated status of their requests

---

### Slice 9: Scheduled Transactions (Fixed Amount)

**Goal:** Parents can set up recurring transactions with fixed amounts (e.g., weekly allowance).

#### Data Layer
- [ ] Implement `ScheduledTransactionRepositoryImpl` using SQLDelight
- [ ] Register in Koin data module

#### Domain Layer
- [ ] `CreateScheduledTransactionUseCase` — create recurring transaction (fixed amount, frequency, start/end date)
- [ ] `GetScheduledTransactionsUseCase` — list all scheduled transactions for a family
- [ ] `ExecuteScheduledTransactionUseCase` — execute due transactions and update next execution date
- [ ] `PauseScheduledTransactionUseCase` — toggle active/paused state
- [ ] Validation: amount > 0, valid frequency, start date required

#### Presentation Layer
- [ ] Parent account detail: "Scheduled Transactions" section
- [ ] Create scheduled transaction screen: amount, frequency (daily/weekly/biweekly/monthly), start date, optional end date
- [ ] List of scheduled transactions with status (active/paused)
- [ ] Pause/resume toggle
- [ ] `ScheduledTransactionViewModel`

#### Tests
- [ ] Unit tests for scheduled transaction creation and validation
- [ ] Unit tests for execution logic (next date calculation per frequency)
- [ ] Test pause/resume

#### Acceptance Criteria
- [ ] Parent creates a weekly allowance of 10.00 for a child
- [ ] Scheduled transaction appears in the list as active
- [ ] When due date arrives, transaction is executed and balance updates
- [ ] Parent can pause and resume scheduled transactions
- [ ] Next execution date is calculated correctly for each frequency

---

### Slice 10: Scheduled Transactions (Percentage-Based)

**Goal:** Parents can create percentage-based scheduled transactions (e.g., 5% monthly interest).

#### Domain Layer
- [ ] Extend `ExecuteScheduledTransactionUseCase` to handle percentage-based calculations
- [ ] Calculate amount as percentage of current account balance at execution time
- [ ] Handle edge cases: zero balance, negative balance (interest on negative = charge)

#### Presentation Layer
- [ ] Extend create scheduled transaction screen: toggle between "Fixed amount" and "Percentage"
- [ ] When percentage selected: enter percentage value instead of amount
- [ ] Display calculated preview amount based on current balance
- [ ] Show percentage info in scheduled transaction list

#### Tests
- [ ] Unit tests for percentage calculation on positive balance
- [ ] Unit tests for percentage calculation on negative balance
- [ ] Unit tests for percentage calculation on zero balance

#### Acceptance Criteria
- [ ] Parent creates 5% monthly interest on a child's account
- [ ] On execution, amount = 5% of current balance
- [ ] Percentage on negative balance creates an expense (interest charge)
- [ ] Percentage on zero balance creates a 0-amount transaction (or skips)

---

### Slice 11: Internationalization (Polish & English)

**Goal:** The entire app displays in Polish or English based on system language settings.

#### Implementation
- [ ] Populate `StringProvider` with all user-facing strings in both EN and PL
- [ ] Replace all hardcoded strings in Compose UI with `StringProvider` lookups
- [ ] Integrate `SystemLanguage` detection into app startup (set language once on launch)
- [ ] Format currency amounts based on locale (PLN / USD / system default)
- [ ] Format dates based on locale
- [ ] Test all screens in both languages

#### Tests
- [ ] Unit test for `StringProvider` — all keys exist in both EN and PL maps
- [ ] Unit test for `SystemLanguage` detection logic
- [ ] Test that switching system language changes app strings

#### Acceptance Criteria
- [ ] App in Polish on Polish system, English on English system
- [ ] All screens display translated text (no hardcoded strings remain)
- [ ] Currency and date formats respect locale
- [ ] No layout overflow with longer Polish text

---

### Slice 12: Settings & App Polish

**Goal:** Settings screen, app theme, error handling, and overall UX polish.

#### Presentation Layer
- [ ] Settings screen: app theme (light/dark/system), notification preferences
- [ ] Family settings: display family name, family code, default currency
- [ ] App theme implementation (Material Design 3 with dynamic colors)
- [ ] Loading states for all async operations (shimmer/skeleton)
- [ ] Error states with user-friendly messages and retry buttons
- [ ] Empty states for all lists (accounts, transactions, requests)

#### Cross-Cutting
- [ ] Global error handling: catch unhandled exceptions, show error UI
- [ ] Consistent spacing, typography, and color usage across all screens
- [ ] App icon and splash screen

#### Tests
- [ ] UI consistency check across all screens
- [ ] Test error states display correctly
- [ ] Test theme switching

#### Acceptance Criteria
- [ ] Settings screen accessible from dashboard
- [ ] Theme switching works (light/dark/system)
- [ ] All async operations show loading indicators
- [ ] All error scenarios show user-friendly messages with retry option
- [ ] All empty lists show meaningful empty states

---

### Slice 13: iOS & Web Platform Adaptation

**Goal:** Ensure the app works correctly on iOS and Web platforms.

#### iOS
- [ ] Verify all Compose Multiplatform screens render correctly on iOS
- [ ] Test SQLDelight with NativeSqliteDriver on iOS
- [ ] Fix any platform-specific rendering issues
- [ ] Test navigation flow end-to-end on iOS Simulator

#### Web
- [ ] Verify Compose for Web (Wasm) renders all screens
- [ ] Test responsive layouts for different viewport sizes
- [ ] Fix any Web-specific issues (storage, navigation)
- [ ] Test in major browsers (Chrome, Firefox, Safari)

#### Tests
- [ ] Run shared tests on all platforms
- [ ] Platform-specific integration tests for database drivers

#### Acceptance Criteria
- [ ] Full app flow works on iOS Simulator
- [ ] Full app flow works in Web browser
- [ ] No platform-specific crashes or rendering bugs
- [ ] Data persistence works on all platforms

---

### Slice 14: Comprehensive Testing & QA

**Goal:** Achieve >80% test coverage for shared code and verify all edge cases.

#### Unit Tests
- [ ] Tests for all use cases (happy path + edge cases)
- [ ] Tests for all repository implementations
- [ ] Tests for business rule validation (negative balances, permissions, approval flow)
- [ ] Tests for fail-fast error handling

#### Integration Tests
- [ ] End-to-end test: create family → add members → create account → add transactions
- [ ] Test: child request → parent approve → balance update
- [ ] Test: scheduled transaction execution cycle
- [ ] Test: data persistence across app restart

#### Edge Case Tests
- [ ] Negative balance edge cases (expense on zero, percentage on negative)
- [ ] Large transaction amounts
- [ ] Empty family (no members)
- [ ] Concurrent transaction requests

#### Acceptance Criteria
- [ ] >80% code coverage for shared commonMain code
- [ ] All business rules verified with tests
- [ ] No failing tests in CI pipeline
- [ ] All edge cases documented and tested

---

### Future: Firebase Integration

**NOTE: Only start after all offline slices (1-14) are complete and stable.**

#### Firebase Setup
- [ ] Create Firebase projects (dev, prod)
- [ ] Add Firebase to Android, iOS, and Web apps
- [ ] Configure Firebase Authentication
- [ ] Set up Cloud Firestore database
- [ ] Define Firestore data structure matching SQLDelight schemas
- [ ] Write Firebase Security Rules

#### Sync Layer
- [ ] Create DTOs for Firebase ↔ domain model mapping
- [ ] Implement Firestore data sources alongside local SQLDelight
- [ ] Implement sync queue for offline actions
- [ ] Implement conflict resolution strategy (last-write-wins)
- [ ] Real-time listeners for cross-device sync
- [ ] Sync status indicators in UI

#### Authentication
- [ ] Replace local user selection with Firebase Auth (email/password)
- [ ] Family join flow with authentication
- [ ] Session management and token refresh

#### Notifications
- [ ] Firebase Cloud Messaging integration
- [ ] Push notifications for transaction approvals/denials
- [ ] Push notifications for scheduled transaction reminders

---

### Future: Launch Preparation

#### App Store
- [ ] App store listings (Google Play, App Store) in English and Polish
- [ ] Web hosting and domain setup
- [ ] Screenshots and promotional materials
- [ ] Privacy policy and terms of service (EN/PL)

#### Final QA
- [ ] Complete QA pass in both languages
- [ ] Production build testing
- [ ] Performance profiling and optimization
- [ ] Security audit
- [ ] Accessibility testing (TalkBack, VoiceOver, screen readers)

#### Post-Launch
- [ ] Crash monitoring and alerting
- [ ] User feedback collection
- [ ] Feature roadmap: savings goals, sibling transfers, spending analytics, multi-currency

## Success Metrics

### User Engagement
- Daily Active Users (DAU)
- Monthly Active Users (MAU)
- Session duration
- Retention rate (D1, D7, D30)

### Feature Usage
- Number of families created
- Number of transactions processed per week
- Transaction approval rate
- Scheduled transaction usage

### Technical Metrics
- App crash rate (<1%, excluding intentional fail-fast crashes during development)
- API response time (<500ms)
- Offline data access time (<100ms)
- Firebase read/write costs
- App size (< 50MB)
- Offline functionality availability (99.9%)

### Business Metrics
- User satisfaction (app store ratings >4.0)
- Net Promoter Score (NPS)
- User acquisition cost
- Retention rate

## Appendix

### Glossary
- **Family**: A group of users (parents and children) who share financial accounts
- **Parent**: Adult user with full permissions to manage family accounts
- **Child**: Minor user with limited permissions, requires parent approval
- **Transaction**: A financial event that can be Income (adds to balance) or Expense (subtracts from balance)
- **Income**: A transaction type that increases account balance
- **Expense**: A transaction type that decreases account balance
- **Scheduled Transaction**: A recurring transaction that executes automatically (supports fixed amounts or percentage-based)
- **Negative Balance**: When account balance is below zero, representing loan/credit (overdraft)
- **Offline-First**: Development approach where app works fully offline before integrating cloud sync
- **Fail-Fast**: Error handling strategy where invalid states crash immediately rather than silently continuing

### References
- [Kotlin Multiplatform Documentation](https://kotlinlang.org/docs/multiplatform.html)
- [Firebase Documentation](https://firebase.google.com/docs)
- [Material Design 3](https://m3.material.io/)
- [iOS Human Interface Guidelines](https://developer.apple.com/design/human-interface-guidelines/)

### Change Log
| Version | Date | Author | Changes |
|---------|------|--------|---------|
| 1.0 | 2025-11-04 | Initial | Initial PRD creation |
| 1.1 | 2025-11-05 | Updated | Added interest rate education feature; Updated scheduled transactions to support percentage-based calculations; Removed custom frequency intervals; Simplified transaction history to per-account only; Removed filter, search, and export functionality; Removed session timeout and privacy settings; Updated child setup flow; Removed tutorials/onboarding; Removed data models section; Removed staging environment and feature flags; Removed Firebase configuration section; Simplified rules section |
| 1.2 | 2025-11-05 | Updated | Added fail-fast error handling approach; Added KISS over DRY principle; Implemented offline-first development strategy (Firebase integration after offline functionality); Enabled negative balances for loan/credit education; Added multi-language support (English and Polish) with automatic system detection; Reorganized tasks to reflect offline-first approach |
| 1.3 | 2025-11-05 | Updated | Reverted to separate "Add Income" and "Add Expenses" transaction types (instead of signed amounts); Maintained negative balance support for loan/credit teaching; Changed language selection to automatic system detection (removed manual switcher) |
| 1.4 | 2025-11-05 | Updated | Removed all time estimates and week numbers from phases; Removed timeline references from documentation; Changed to phase-based organization without date commitments |

---

**Document Status**: Updated
**Last Updated**: 2025-11-05
**Next Review**: Upon project kickoff
