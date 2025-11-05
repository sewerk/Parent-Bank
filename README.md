# Parent Bank

A family financial management mobile application built with Kotlin Multiplatform for Android and iOS.

## General Project Idea

Parent Bank is a secure, intuitive, and educational platform designed to help families manage children's finances. The app empowers parents to control and oversee their children's financial activities while giving children visibility and limited autonomy in managing their own accounts.

### Core Concept

The application creates a safe environment where children can learn money management without real-world financial risks. Parents can create virtual bank accounts for their children, manage transactions, set up allowances, and even teach concepts like interest rates and savings through percentage-based scheduled transactions.

### Key Features

- **Family Management**: Create family units with multiple parents and children
- **Account Management**: Parents create and manage individual accounts for each child
- **Transaction Control**:
  - Parents can add transactions with positive (income) or negative (spending) amounts
  - Children request transactions that require parent approval
  - Support for negative balances to teach loan/credit concepts
- **Scheduled Transactions**: Set up recurring transactions (allowances, interest payments) with support for both fixed amounts and percentage-based calculations
- **Interest Education**: Teach children how savings generate profits through automated interest payments
- **Loan/Credit Education**: Allow negative balances to teach children about loans and overdrafts
- **Transaction History**: Per-account transaction tracking
- **Offline-First**: App works fully offline before Firebase sync is integrated
- **Multi-Language**: Full support for English and Polish from launch
- **Firebase Sync**: Real-time data synchronization across all family devices (added after offline functionality)
- **Multi-Platform**: Native experience on both Android (Jetpack Compose) and iOS (SwiftUI)

### Target Audience

- **Parents/Guardians**: Adults who want to teach their children about money management
- **Children/Teens**: Ages 6-17 learning financial responsibility under parental supervision

### Technology Stack

- **Kotlin Multiplatform (KMP)**: Shared business logic across platforms
- **Android**: Jetpack Compose, Material Design 3
- **iOS**: SwiftUI, iOS Human Interface Guidelines
- **Backend**: Firebase (Authentication, Firestore, Cloud Messaging) - integrated after offline functionality
- **Local Storage**: SQLDelight or Realm for offline-first data persistence
- **Architecture**: Clean Architecture with separation of UI, Domain, and Data layers

### Development Philosophy

- **Offline-First**: The app is built to work completely offline before any cloud sync is added
- **Fail-Fast**: Errors crash early rather than silently corrupting state
- **KISS over DRY**: Simplicity and readability prioritized over code reusability
- **Multi-Language from Day 1**: English and Polish support built in from the start

## Documentation

For detailed product requirements, features, and implementation plan, see [PRD.md](PRD.md).

## Project Status

This project is currently in the planning phase. See the [PRD.md](PRD.md) for the complete 24-week implementation roadmap.

The development follows an offline-first approach, with Firebase integration planned after all core offline functionality is complete and tested.

## License

TBD