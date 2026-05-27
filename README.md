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
- **Android**: Jetpack Compose, Material Design 3
- **iOS**: SwiftUI, iOS Human Interface Guidelines
- **Web**: Compose for Web (Wasm/JS targets)
- **Backend**: Firebase (Authentication, Firestore, Cloud Messaging) - integrated after offline functionality
- **Local Storage**: SQLDelight or Realm for offline-first data persistence
- **Architecture**: Clean Architecture with separation of UI, Domain, and Data layers

### Development Philosophy

- **Offline-First**: The app is built to work completely offline before any cloud sync is added
- **Fail-Fast**: Errors crash early rather than silently corrupting state
- **KISS over DRY**: Simplicity and readability prioritized over code reusability
- **Multi-Language from Day 1**: English and Polish support built in from the start

## Project Structure

- `/composeApp` - Shared Kotlin Multiplatform code
  - `commonMain` - Shared business logic and UI
  - `androidMain` - Android-specific implementations
  - `iosMain` - iOS-specific implementations
  - `webMain` - Web-specific implementations
- `/iosApp` - iOS application entry point and SwiftUI code

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

**Current phase:** Offline-first feature development (vertical slices)

The project follows an **iterative, vertical-slice** development approach. Each slice delivers a complete feature from database through domain logic to UI, keeping PRs small and the app testable after every merge.

| Slice | Feature | Status |
|-------|---------|--------|
| 0 | Project setup & foundation | Done |
| 1 | Family & User creation | To Do |
| 2 | Navigation & role-based routing | To Do |
| 3 | Child account creation | To Do |
| 4 | Add Income transaction | To Do |
| 5 | Add Expense transaction | To Do |
| 6 | Transaction history | To Do |
| 7 | Child dashboard | To Do |
| 8 | Child requests & parent approval | To Do |
| 9 | Scheduled transactions (fixed) | To Do |
| 10 | Scheduled transactions (%) | To Do |
| 11 | Internationalization (PL/EN) | To Do |
| 12 | Settings & app polish | To Do |
| 13 | iOS & Web adaptation | To Do |
| 14 | Comprehensive testing & QA | To Do |

Firebase integration is planned after all offline slices are complete and stable. See [PRD.md](PRD.md) for detailed task breakdowns and the [GitHub Issues](https://github.com/sewerk/Parent-Bank/issues) for tracking.

## License

TBD