# Parent Bank

A family financial management application built with Kotlin Multiplatform for Android, iOS, and Web.

## General Project Idea

Parent Bank is a secure, intuitive, and educational platform designed to help families manage children's finances. The app empowers parents to control and oversee their children's financial activities while giving children visibility and limited autonomy in managing their own accounts.

### Core Concept

The application creates a safe environment where children can learn money management without real-world financial risks. Parents can create virtual bank accounts for their children, manage transactions, set up allowances, and even teach concepts like interest rates and savings through percentage-based scheduled transactions.

### Key Features

- **Family Management**: Create family units with multiple parents and children
- **Account Management**: Parents create and manage individual accounts for each child
- **Transaction Control**:
  - Parents can add Income (increases balance) and Expenses (decreases balance)
  - Children request transactions that require parent approval
  - Support for negative balances to teach loan/credit concepts
- **Scheduled Transactions**: Set up recurring transactions (allowances, interest payments) with support for both fixed amounts and percentage-based calculations
- **Interest Education**: Teach children how savings generate profits through automated interest payments
- **Loan/Credit Education**: Allow negative balances to teach children about loans and overdrafts
- **Transaction History**: Per-account transaction tracking
- **Offline-First**: App works fully offline before Firebase sync is integrated
- **Multi-Language**: Full support for English and Polish from launch
- **Firebase Sync**: Real-time data synchronization across all family devices (added after offline functionality)
- **Multi-Platform**: Native experience on Android (Jetpack Compose), iOS (SwiftUI), and Web (Compose for Web)

### Target Audience

- **Parents/Guardians**: Adults who want to teach their children about money management
- **Children/Teens**: Ages 6-17 learning financial responsibility under parental supervision

### Technology Stack

- **Kotlin Multiplatform (KMP)**: Shared business logic across platforms
- **Compose Multiplatform**: Shared UI framework for Android and iOS
- **Android**: Material Design 3 with Compose Multiplatform
- **iOS**: Compose Multiplatform (shared UI with Android)
- **Web**: Compose for Web (Wasm/JS targets) - Planned
- **Backend**: Firebase (Authentication, Firestore, Cloud Messaging) - integrated after offline functionality
- **Local Storage**: SQLDelight for offline-first data persistence
- **Architecture**: Clean Architecture with separation of UI, Domain, and Data layers
- **Dependency Injection**: Koin for dependency injection

### Development Philosophy

- **Offline-First**: The app is built to work completely offline before any cloud sync is added
- **Fail-Fast**: Errors crash early rather than silently corrupting state
- **KISS over DRY**: Simplicity and readability prioritized over code reusability
- **Multi-Language from Day 1**: English and Polish support built in from the start

## Project Structure

- `/composeApp` - Shared Kotlin Multiplatform code
  - **`commonMain`** - **~95% of app code shared across all platforms**
    - Complete business logic (domain models, repositories, use cases)
    - Complete UI implementation with Compose Multiplatform
    - ViewModels and navigation
    - Database schema (SQLDelight)
    - Dependency injection modules (Koin)
  - `androidMain` - Android-specific code (~5%)
    - Database driver implementation
    - Application class and Koin initialization
    - Platform-specific dependencies
  - `iosMain` - iOS-specific code (~5%)
    - Database driver implementation
    - MainViewController and Koin initialization
    - Platform-specific dependencies
  - `webMain` - Web-specific implementations (planned)
- `/iosApp` - iOS application entry point (calls shared code from `iosMain`)

### Code Sharing Strategy

The project maximizes code reuse through **Compose Multiplatform**:
- **100% shared UI** - All screens written once in `commonMain`
- **100% shared business logic** - All use cases, repositories, and models in `commonMain`
- **Platform-specific only when necessary** - Database drivers and initialization (~5% of code)

This approach results in approximately 95% code sharing between Android and iOS platforms.

## Development

### Android
```shell
./gradlew :composeApp:assembleDebug
```

### iOS
Open `/iosApp` in Xcode and run from there.

### Web
```shell
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

## Documentation

For detailed product requirements, features, and implementation plan, see [PRD.md](PRD.md).

## Project Status

### ✅ Completed Phases

**Phase 1: Project Setup & Foundation** - Complete
- Kotlin Multiplatform project structure
- Domain models (Family, User, Account, Transaction, ScheduledTransaction)
- Repository interfaces
- SQLDelight database schema
- Error handling utilities (Outcome, AppException)
- Internationalization setup (EN/PL)

**Phase 2: Data Layer** - Complete
- Repository implementations with SQLDelight
- Database drivers for Android and iOS
- Type-safe database queries
- Reactive Flow support

**Phase 3: Domain Layer** - Complete
- Family management use cases (Create, Join)
- User and account creation use cases
- Transaction use cases (Create, Approve, Deny)
- Scheduled transaction use cases
- Automated execution engine for recurring transactions

**Phase 4-5: Presentation Layer (Mobile)** - Complete
- Navigation system
- ViewModels with state management
- Complete UI implementation using Compose Multiplatform
- Shared UI across Android and iOS (100% code sharing)
- Koin dependency injection setup

**Phase 7: Session Persistence** - Complete
- User session management with local storage
- Automatic login restoration on app restart
- Logout functionality for both parent and child dashboards
- Session repository with SQLDelight persistence

### 📱 Current App Features

The mobile app is **fully functional** with:
- Family creation and joining with unique codes
- Parent and child user profiles
- Parent dashboard with family account overview
- Child dashboard with personal account
- Transaction creation with approval workflow
- Pending transaction management for parents
- Support for Income and Expense transactions
- Negative balances for teaching loans/credit concepts
- **Session persistence** - Login state preserved across app restarts
- **Logout functionality** - Secure session management

### 🎯 Next Steps

- **Phase 7**: Background scheduler for scheduled transactions
- **Phase 8**: Firebase Integration (Authentication, Cloud Sync)
- **Phase 9**: Input validation, UI polish, transaction history improvements
- **Phase 10+**: Testing, optimization, and launch preparation

The development follows an offline-first approach, with Firebase integration planned after all core offline functionality is complete and tested.

## License

TBD