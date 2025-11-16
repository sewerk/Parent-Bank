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
  - Compose Multiplatform for UI (shared with iOS)
  - Material Design 3 components
  - Android-specific database drivers and initialization
- **iOS**:
  - Compose Multiplatform for UI (shared with Android)
  - Material Design 3 components
  - iOS-specific database drivers and initialization
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
│   (Compose Multiplatform UI,        │
│    ViewModels, Navigation)          │
│     Shared (KMP) - ~95% shared      │
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
│  Platform-Specific (~5% of code)    │
└─────────────────────────────────────┘
```

#### Key Components

**Shared Module (commonMain)**
- Domain Models: Family, User, Account, Transaction, ScheduledTransaction, UserSession
- Use Cases: CreateFamily, JoinFamily, CreateUser, CreateAccount, CreateTransaction, ApproveTransaction, DenyTransaction, CreateScheduledTransaction, ExecuteScheduledTransactions, SaveSession, GetSession, ClearSession
- Repositories (Interfaces): FamilyRepository, UserRepository, AccountRepository, TransactionRepository, ScheduledTransactionRepository, SessionRepository
- Repository Implementations: All repository implementations with SQLDelight
- Presentation Layer: ViewModels (FamilyViewModel, UserViewModel, DashboardViewModel, TransactionViewModel, AppViewModel)
- UI Screens: All Compose Multiplatform screens (Welcome, CreateFamily, JoinFamily, CreateUser, ParentDashboard, ChildDashboard, CreateTransaction, PendingTransactions)
- Navigation: Navigator and Screen sealed class
- Dependency Injection: Koin modules
- Firebase Client (expect/actual pattern) - Planned

**Platform Modules (androidMain, iosMain)**
- Database Driver Factory: Platform-specific SQLDelight driver
- Koin Initialization: Platform-specific app initialization
- Platform Context: Android Application context / iOS platform specifics

**Platform Modules (webMain)** - Planned
- Web-specific implementations

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

### Phase 1: Project Setup & Foundation ✅ COMPLETE

#### Task 1.1: Project Initialization
- [x] Create Kotlin Multiplatform project structure
- [x] Set up commonMain, androidMain, iosMain source sets
- [x] Configure Gradle build files
- [x] Add necessary KMP dependencies
- [x] Set up version catalog for dependency management
- [x] Initialize Git repository and create .gitignore
- [ ] Set up CI/CD pipeline (GitHub Actions or similar)
- [ ] Configure internationalization support (English, Polish)

#### Task 1.2: Architecture Setup (Offline-First)
- [x] Define project architecture (Clean Architecture)
- [x] Create module structure (data, domain, presentation)
- [x] Set up dependency injection (Koin)
- [x] Create base classes and interfaces (Outcome, AppException)
- [x] Set up navigation structure (Compose Multiplatform Navigation - shared)
- [x] Configure local database (SQLDelight) for offline-first storage
- [x] Set up error handling with fail-fast approach
- [ ] Set up i18n resource files for English and Polish
- [ ] Configure string resources and localization utilities

### Phase 2: Core Features - Data Layer (OFFLINE ONLY) ✅ COMPLETE

#### Task 2.1: Data Models (Offline-First)
- [x] Create domain models (Family, User, Account, Transaction, ScheduledTransaction)
- [x] Design Transaction model with separate types: Income and Expense
- [x] Design Account model to allow negative balances (loan/credit feature)
- [x] Create enums (UserRole, TransactionType, TransactionStatus, Frequency)
- [x] Add data validation logic with fail-fast approach
- [ ] Add i18n keys for all user-facing text in models

#### Task 2.2: Repositories (Local Storage Only)
- [x] Create repository interfaces in domain layer
- [x] Implement FamilyRepository with local database (SQLDelight)
- [x] Implement UserRepository with local database (SQLDelight)
- [x] Implement AccountRepository with local database (support negative balances)
- [x] Implement TransactionRepository with local database (Income and Expense types)
- [x] Implement ScheduledTransactionRepository with local database
- [x] Add fail-fast error handling and result wrapping (Outcome sealed class)
- [x] Test all repositories work completely offline

### Phase 3: Business Logic - Domain Layer ✅ COMPLETE

#### Task 3.1: Use Cases - Family & User Management
- [x] CreateFamilyUseCase (generates unique 6-character family code)
- [x] JoinFamilyUseCase (validates family code format)
- [ ] AddFamilyMemberUseCase
- [ ] GetFamilyMembersUseCase
- [ ] UpdateUserProfileUseCase
- [ ] AuthenticateUserUseCase
- [ ] GetUserByIdUseCase

#### Task 3.2: Use Cases - Account Management
- [x] CreateAccountUseCase (auto-creates for child users)
- [ ] GetAccountByIdUseCase
- [ ] GetAllAccountsForFamilyUseCase
- [ ] UpdateAccountBalanceUseCase
- [ ] DeactivateAccountUseCase

#### Task 3.3: Use Cases - Transaction Management
- [x] CreateTransactionUseCase (parent=auto-approved, child=pending)
- [x] ApproveTransactionUseCase (parent-only, updates balance)
- [x] DenyTransactionUseCase (parent-only, no balance update)
- [ ] GetTransactionHistoryUseCase
- [ ] GetPendingTransactionsUseCase

#### Task 3.4: Use Cases - Scheduled Transactions
- [x] CreateScheduledTransactionUseCase (support fixed and percentage-based)
- [ ] UpdateScheduledTransactionUseCase
- [ ] DeleteScheduledTransactionUseCase
- [x] ExecuteScheduledTransactionsUseCase (handle both fixed amounts and percentage calculations)
- [ ] GetScheduledTransactionsUseCase
- [ ] PauseScheduledTransactionUseCase

#### Task 3.5: Business Rules Validation
- [x] Implement balance validation (allow negative balances for loan/credit)
- [x] Implement permission checks (parent vs child in transaction creation)
- [x] Implement transaction approval logic
- [x] Add transaction amount validation (positive amounts only)
- [x] Add transaction type validation (Income or Expense)
- [x] Add date validation
- [x] Use fail-fast validation (Outcome.Failure with AppException)

### Phase 4-5: UI - Mobile (Android & iOS) ✅ COMPLETE

**NOTE: Using Compose Multiplatform, all UI screens are shared in commonMain between Android and iOS**

#### Task 4-5.1: Shared Authentication & Setup Screens
- [x] Create welcome screen (Compose Multiplatform)
- [x] Implement role selection (Parent/Child toggle)
- [x] Create family creation screen
- [x] Create join family screen (6-character code entry)
- [x] Create user profile creation screen
- [ ] Implement automatic language detection from system settings
- [ ] Add i18n support for all screens

#### Task 4-5.2: Shared Parent Screens
- [x] Create parent dashboard/home screen (show all family accounts)
- [x] Create transaction creation screen (separate Income and Expense options)
- [x] Create pending transaction approval screen
- [ ] Create scheduled transaction screen (support fixed and percentage-based)
- [ ] Create detailed transaction history screen
- [ ] Create family settings screen
- [ ] Show negative balances with clear visual indicators
- [ ] Ensure all screens support English and Polish

#### Task 4-5.3: Shared Child Screens
- [x] Create child dashboard/home screen (balance display, recent transactions)
- [x] Create transaction request screen
- [x] Create transaction history list
- [ ] Create pending requests status screen
- [ ] Add logout functionality ✅

#### Task 4-5.4: Shared Common UI
- [x] Create navigation system (Navigator class, Screen sealed class)
- [x] Create app theme (Material Design 3)
- [x] Create reusable composables (buttons, cards, inputs, chips)
- [x] Create loading states (CircularProgressIndicator)
- [x] Create error states (inline error messages)
- [ ] Implement notifications UI
- [ ] Create settings screen

#### Task 4-5.5: Shared ViewModels
- [x] Create FamilyViewModel (create/join family)
- [x] Create UserViewModel (user profile creation)
- [x] Create DashboardViewModel (parent and child dashboard data)
- [x] Create TransactionViewModel (create, approve, deny)
- [x] Create AppViewModel (session management, logout)
- [ ] Create ScheduledTransactionViewModel
- [ ] Create SettingsViewModel

#### Task 4-5.6: Platform-Specific Setup
- [x] Android: ParentBankApplication with Koin initialization
- [x] Android: MainActivity with setContent
- [x] iOS: MainViewController with Koin initialization
- [x] iOS: ComposeUIViewController wrapper

### Phase 6: UI - Web

#### Task 6.1: Web - Authentication & Setup
- [ ] Create login screen (Compose for Web) with i18n support
- [ ] Create registration screen with i18n support
- [ ] Implement role selection with i18n support
- [ ] Create family creation screen with i18n support
- [ ] Create join family screen with i18n support
- [ ] Implement automatic language detection from browser settings

#### Task 6.2: Web - Parent Screens
- [ ] Create parent dashboard/home screen (show negative balances clearly)
- [ ] Create account management screen
- [ ] Create transaction creation screen (separate Income and Expense options)
- [ ] Create transaction approval screen
- [ ] Create scheduled transaction screen (support fixed and percentage-based)
- [ ] Create transaction history screen
- [ ] Create family settings screen
- [ ] Ensure all screens support English and Polish
- [ ] Implement responsive layouts for different screen sizes

#### Task 6.3: Web - Child Screens
- [ ] Create child dashboard/home screen
- [ ] Create account view screen
- [ ] Create transaction request screen
- [ ] Create transaction history screen
- [ ] Create pending requests screen
- [ ] Ensure responsive design for tablets and desktops

#### Task 6.4: Web - Common UI
- [ ] Create navigation structure
- [ ] Create app theme (responsive web design)
- [ ] Create reusable composables (buttons, cards, inputs)
- [ ] Create loading states
- [ ] Create error states
- [ ] Implement notifications UI
- [ ] Create settings screen
- [ ] Add mobile-first responsive breakpoints

#### Task 6.5: Web - ViewModels
- [ ] Create AuthViewModel
- [ ] Create ParentDashboardViewModel
- [ ] Create ChildDashboardViewModel
- [ ] Create TransactionViewModel
- [ ] Create ScheduledTransactionViewModel
- [ ] Create SettingsViewModel

### Phase 7: Offline Features & Session Persistence (PARTIALLY COMPLETE)

#### Task 7.1: Session Persistence ✅ COMPLETE
- [x] Create UserSession domain model
- [x] Create SessionRepository interface
- [x] Create Session.sq SQLDelight schema
- [x] Implement SessionRepositoryImpl
- [x] Create SaveSessionUseCase, GetSessionUseCase, ClearSessionUseCase
- [x] Create AppViewModel for session management
- [x] Implement automatic session restoration on app start
- [x] Add logout functionality to dashboards

#### Task 7.2: Scheduled Transactions (Offline)
- [x] Implement scheduled transaction execution logic (ExecuteScheduledTransactionsUseCase)
- [ ] Create background job scheduler (Android: WorkManager, iOS: Background Tasks)
- [ ] Test recurring transaction creation with fixed amounts
- [ ] Test percentage-based interest calculations
- [ ] Test with negative balances
- [ ] Implement local notifications
- [ ] Add pause/resume functionality

#### Task 7.3: Testing Offline Functionality
- [ ] Test complete offline app flow (end-to-end)
- [ ] Test negative balance scenarios
- [ ] Test Income and Expense transaction types
- [ ] Test all features work without network
- [ ] Test session persistence across app restarts
- [ ] Test both English and Polish localizations
- [ ] Performance testing of local database
- [ ] Test fail-fast error handling

### Phase 8: Firebase Integration

**NOTE: Only start this phase after offline functionality is fully working and tested**

#### Task 8.1: Firebase Setup
- [ ] Create Firebase projects (dev, prod)
- [ ] Add Firebase to Android app
- [ ] Add Firebase to iOS app
- [ ] Add Firebase to Web app
- [ ] Configure Firebase Authentication
- [ ] Set up Cloud Firestore database
- [ ] Define Firestore data structure
- [ ] Write Firebase Security Rules
- [ ] Configure Firebase in KMP (expect/actual implementations)

#### Task 8.2: Firebase Data Layer
- [ ] Create DTOs for Firebase
- [ ] Implement Firestore data sources
- [ ] Implement real-time listeners for data sync
- [ ] Add Firebase Cloud Messaging integration
- [ ] Implement file upload for profile pictures (Firebase Storage)

#### Task 8.3: Sync Implementation
- [ ] Implement sync queue for offline actions
- [ ] Implement conflict resolution strategy
- [ ] Add sync status indicators
- [ ] Test data synchronization between devices
- [ ] Test offline/online scenarios
- [ ] Implement push notifications

### Phase 9: Testing

#### Task 9.1: Unit Tests
- [ ] Write tests for domain use cases
- [ ] Write tests for business logic validation (negative balances, Income/Expense types)
- [ ] Write tests for data models
- [ ] Write tests for fail-fast validation
- [ ] Achieve >80% code coverage for shared code

#### Task 9.2: Integration Tests
- [ ] Test repository implementations (offline and with Firebase)
- [ ] Test Firebase integration
- [ ] Test data synchronization
- [ ] Test offline/online scenarios

#### Task 9.3: UI Tests
- [ ] Write UI tests for critical flows (Android)
- [ ] Write UI tests for critical flows (iOS)
- [ ] Write UI tests for critical flows (Web)
- [ ] Test user authentication flow
- [ ] Test transaction creation with Income and Expense types
- [ ] Test negative balance display
- [ ] Test scheduled transaction creation
- [ ] Test language support (English/Polish)

#### Task 9.4: End-to-End Tests
- [ ] Test complete parent workflow
- [ ] Test complete child workflow
- [ ] Test multi-device sync (with Firebase)
- [ ] Test edge cases and error scenarios with fail-fast
- [ ] Test loan/credit scenarios (negative balances)

### Phase 10: Polish & Optimization

#### Task 10.1: Performance Optimization
- [ ] Optimize Firebase queries
- [ ] Optimize local database queries
- [ ] Implement pagination for transaction history
- [ ] Optimize image loading
- [ ] Profile app performance
- [ ] Fix memory leaks
- [ ] Reduce app size

#### Task 10.2: UX Improvements
- [ ] Improve loading states
- [ ] Add animations and transitions
- [ ] Improve error messages (both languages)
- [ ] Improve negative balance visual indicators
- [ ] Conduct usability testing
- [ ] Implement feedback

#### Task 10.3: Accessibility
- [ ] Add content descriptions (Android)
- [ ] Add accessibility labels (iOS)
- [ ] Add ARIA labels (Web)
- [ ] Test with TalkBack/VoiceOver/screen readers in both languages
- [ ] Ensure proper contrast ratios
- [ ] Support dynamic text sizing
- [ ] Test keyboard navigation

#### Task 10.4: Security Hardening
- [ ] Review and update Firebase Security Rules
- [ ] Implement rate limiting
- [ ] Add input sanitization
- [ ] Enable ProGuard/R8 (Android)
- [ ] Conduct security audit
- [ ] Implement certificate pinning (if needed)

### Phase 11: Beta Testing & Refinement

#### Task 11.1: Beta Preparation
- [ ] Prepare beta builds (Android: Internal Testing, iOS: TestFlight)
- [ ] Create beta tester documentation (English and Polish)
- [ ] Set up feedback collection mechanism
- [ ] Create bug reporting template
- [ ] Recruit beta testers (include Polish speakers)

#### Task 11.2: Beta Testing
- [ ] Distribute beta builds
- [ ] Monitor crash reports
- [ ] Collect user feedback
- [ ] Track usage analytics
- [ ] Identify and prioritize issues

#### Task 11.3: Bug Fixes & Refinements
- [ ] Fix critical bugs
- [ ] Address user feedback
- [ ] Improve performance based on analytics
- [ ] Update documentation
- [ ] Prepare for public release

### Phase 12: Launch Preparation

#### Task 12.1: App Store Preparation
- [ ] Create app store listings (Google Play) in English and Polish
- [ ] Create app store listings (App Store) in English and Polish
- [ ] Set up web hosting and domain for Web version
- [ ] Prepare screenshots and promotional materials (both languages)
- [ ] Write app descriptions (both languages)
- [ ] Create privacy policy (English and Polish)
- [ ] Create terms of service (English and Polish)

#### Task 12.2: Final Checks
- [ ] Complete final QA pass (both languages)
- [ ] Review all app store requirements
- [ ] Verify Firebase configuration (production)
- [ ] Test production builds
- [ ] Verify offline-first functionality
- [ ] Test fail-fast error handling in production
- [ ] Prepare rollback plan

#### Task 12.3: Launch
- [ ] Submit to Google Play
- [ ] Submit to App Store
- [ ] Deploy Web version to production
- [ ] Monitor submission review process
- [ ] Prepare launch announcement (English and Polish)
- [ ] Set up monitoring and alerts
- [ ] Launch app!

### Phase 13: Post-Launch (Ongoing)

#### Task 13.1: Monitoring
- [ ] Monitor crash reports (pay attention to fail-fast crashes)
- [ ] Monitor user reviews (in both English and Polish)
- [ ] Track key metrics (DAU, MAU, retention)
- [ ] Monitor Firebase usage and costs
- [ ] Monitor offline functionality performance
- [ ] Set up alerts for critical issues

#### Task 13.2: Maintenance
- [ ] Respond to user feedback (in English and Polish)
- [ ] Fix bugs as they arise
- [ ] Update dependencies
- [ ] Address security vulnerabilities
- [ ] Maintain compatibility with new OS versions
- [ ] Update translations as needed

#### Task 13.3: Future Enhancements
- [ ] Plan feature roadmap
- [ ] Implement savings goals feature
- [ ] Expand interest/rewards system
- [ ] Implement transfer between siblings
- [ ] Add spending analytics and insights
- [ ] Create educational content for financial literacy
- [ ] Multi-currency support

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
| 1.5 | 2025-11-16 | Updated | Updated architecture to reflect Compose Multiplatform implementation (shared UI for Android/iOS instead of separate SwiftUI); Marked Phases 1-5 and partial Phase 7 as complete; Added session persistence implementation details; Updated Key Components to reflect actual implementation; Consolidated Phase 4 and 5 into shared mobile UI phase |

---

**Document Status**: Updated
**Last Updated**: 2025-11-16
**Next Review**: Upon completion of Phase 8 (Firebase Integration)
