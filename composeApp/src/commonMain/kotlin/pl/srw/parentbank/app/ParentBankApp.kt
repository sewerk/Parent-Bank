package pl.srw.parentbank.app

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject
import pl.srw.parentbank.domain.model.TransactionType
import pl.srw.parentbank.domain.model.UserRole
import pl.srw.parentbank.presentation.app.AppViewModel
import pl.srw.parentbank.presentation.dashboard.DashboardViewModel
import pl.srw.parentbank.presentation.family.FamilyViewModel
import pl.srw.parentbank.presentation.navigation.Navigator
import pl.srw.parentbank.presentation.navigation.Screen
import pl.srw.parentbank.presentation.navigation.rememberNavigator
import pl.srw.parentbank.presentation.transaction.TransactionViewModel
import pl.srw.parentbank.presentation.user.UserViewModel

@Composable
fun ParentBankApp() {
    MaterialTheme {
        val appViewModel: AppViewModel = koinInject()
        val appState by appViewModel.state.collectAsState()
        val navigator = rememberNavigator()

        // Handle Android system back button
        BackHandler(enabled = navigator.canNavigateBack()) {
            navigator.navigateBack()
        }

        // Check for existing session and navigate accordingly
        LaunchedEffect(appState.currentSession) {
            appState.currentSession?.let { session ->
                when (session.userRole) {
                    UserRole.PARENT -> navigator.navigateTo(
                        Screen.ParentDashboard(session.userId, session.familyId)
                    )
                    UserRole.CHILD -> navigator.navigateTo(
                        Screen.ChildDashboard(session.userId, session.familyId)
                    )
                }
            }
        }

        if (appState.isLoading) {
            // Show loading screen while checking session
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            when (val screen = navigator.currentScreen) {
                is Screen.Welcome -> WelcomeScreen(navigator)
                is Screen.CreateFamily -> CreateFamilyScreen(navigator)
                is Screen.JoinFamily -> JoinFamilyScreen(navigator)
                is Screen.CreateUser -> CreateUserScreen(navigator, screen.familyId, appViewModel)
                is Screen.ParentDashboard -> ParentDashboardScreen(navigator, screen.userId, screen.familyId, appViewModel)
                is Screen.ChildDashboard -> ChildDashboardScreen(navigator, screen.userId, screen.familyId, appViewModel)
                is Screen.CreateTransaction -> CreateTransactionScreen(navigator, screen.accountId, screen.userId)
                is Screen.PendingTransactions -> PendingTransactionsScreen(navigator, screen.familyId, screen.userId)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(navigator: Navigator) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Parent Bank") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Welcome to Parent Bank",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(Modifier.height(16.dp))
            Text(
                "Teach kids financial responsibility",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(48.dp))
            Button(
                onClick = { navigator.navigateTo(Screen.CreateFamily) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create New Family")
            }
            Spacer(Modifier.height(16.dp))
            OutlinedButton(
                onClick = { navigator.navigateTo(Screen.JoinFamily) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Join Existing Family")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateFamilyScreen(navigator: Navigator) {
    val viewModel: FamilyViewModel = koinInject()
    val state = viewModel.state

    LaunchedEffect(state.createdFamily) {
        state.createdFamily?.let {
            navigator.navigateTo(Screen.CreateUser(it.id))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Family") },
                navigationIcon = {
                    if (navigator.canNavigateBack()) {
                        IconButton(onClick = { navigator.navigateBack() }) {
                            Text("←")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            OutlinedTextField(
                value = state.familyName,
                onValueChange = viewModel::onFamilyNameChanged,
                label = { Text("Family Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = state.currency,
                onValueChange = viewModel::onCurrencyChanged,
                label = { Text("Currency") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(24.dp))

            state.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(16.dp))
            }

            Button(
                onClick = { viewModel.createFamily() },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (state.isLoading) "Creating..." else "Create Family")
            }

            state.createdFamily?.let {
                Spacer(Modifier.height(24.dp))
                Text("Family Code: ${it.familyCode}", style = MaterialTheme.typography.headlineSmall)
                Text("Share this code with family members", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinFamilyScreen(navigator: Navigator) {
    val viewModel: FamilyViewModel = koinInject()
    val state = viewModel.state

    LaunchedEffect(state.joinedFamily) {
        state.joinedFamily?.let {
            navigator.navigateTo(Screen.CreateUser(it.id))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Join Family") },
                navigationIcon = {
                    if (navigator.canNavigateBack()) {
                        IconButton(onClick = { navigator.navigateBack() }) {
                            Text("←")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            Text("Enter the 6-character family code", style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = state.familyCode,
                onValueChange = viewModel::onFamilyCodeChanged,
                label = { Text("Family Code") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(24.dp))

            state.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(16.dp))
            }

            Button(
                onClick = { viewModel.joinFamily() },
                enabled = !state.isLoading && state.familyCode.length == 6,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (state.isLoading) "Joining..." else "Join Family")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUserScreen(navigator: Navigator, familyId: String, appViewModel: AppViewModel) {
    val viewModel: UserViewModel = koinInject()
    val state = viewModel.state

    LaunchedEffect(state.createdUser) {
        state.createdUser?.let { user ->
            // Save session for persistence
            appViewModel.onUserLoggedIn(user)
            when (user.role) {
                UserRole.PARENT -> navigator.navigateTo(Screen.ParentDashboard(user.id, familyId))
                UserRole.CHILD -> navigator.navigateTo(Screen.ChildDashboard(user.id, familyId))
            }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Create Profile") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            Text("I am a...", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Row {
                FilterChip(
                    selected = state.selectedRole == UserRole.PARENT,
                    onClick = { viewModel.onRoleSelected(UserRole.PARENT) },
                    label = { Text("Parent") }
                )
                Spacer(Modifier.width(8.dp))
                FilterChip(
                    selected = state.selectedRole == UserRole.CHILD,
                    onClick = { viewModel.onRoleSelected(UserRole.CHILD) },
                    label = { Text("Child") }
                )
            }
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = state.name,
                onValueChange = viewModel::onNameChanged,
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            if (state.selectedRole == UserRole.PARENT) {
                OutlinedTextField(
                    value = state.email,
                    onValueChange = viewModel::onEmailChanged,
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                OutlinedTextField(
                    value = state.age.toString(),
                    onValueChange = viewModel::onAgeChanged,
                    label = { Text("Age") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(24.dp))
            state.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(16.dp))
            }

            Button(
                onClick = { viewModel.createUser(familyId) },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (state.isLoading) "Creating..." else "Continue")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentDashboardScreen(navigator: Navigator, userId: String, familyId: String, appViewModel: AppViewModel) {
    val viewModel: DashboardViewModel = koinInject()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(userId, familyId) {
        viewModel.loadDashboard(userId, familyId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Parent Dashboard") },
                actions = {
                    TextButton(onClick = {
                        appViewModel.logout()
                        navigator.navigateTo(Screen.Welcome)
                    }) {
                        Text("Logout")
                    }
                }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                state.currentUser?.let { user ->
                    Text("Welcome, ${user.name}!", style = MaterialTheme.typography.headlineSmall)
                }
                Spacer(Modifier.height(16.dp))

                Text("Family Accounts", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                state.accounts.forEach { account ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text("Child ID: ${account.childId}")
                            Text("Balance: $${account.balance / 100.0}")
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                if (state.pendingTransactions.isNotEmpty()) {
                    Button(
                        onClick = { navigator.navigateTo(Screen.PendingTransactions(familyId, userId)) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("View ${state.pendingTransactions.size} Pending Requests")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildDashboardScreen(navigator: Navigator, userId: String, familyId: String, appViewModel: AppViewModel) {
    val viewModel: DashboardViewModel = koinInject()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(userId, familyId) {
        viewModel.loadDashboard(userId, familyId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Account") },
                actions = {
                    TextButton(onClick = {
                        appViewModel.logout()
                        navigator.navigateTo(Screen.Welcome)
                    }) {
                        Text("Logout")
                    }
                }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                state.currentUser?.let { user ->
                    Text("Hi, ${user.name}!", style = MaterialTheme.typography.headlineSmall)
                }
                Spacer(Modifier.height(16.dp))

                state.userAccount?.let { account ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Your Balance", style = MaterialTheme.typography.titleMedium)
                            Text(
                                "$${account.balance / 100.0}",
                                style = MaterialTheme.typography.displayMedium
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { navigator.navigateTo(Screen.CreateTransaction(account.id, userId)) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Request Money")
                    }
                }

                Spacer(Modifier.height(16.dp))
                Text("Recent Transactions", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                LazyColumn {
                    items(state.recentTransactions) { transaction ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Row(
                                Modifier.fillMaxWidth().padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(transaction.title)
                                    Text(transaction.status.name, style = MaterialTheme.typography.bodySmall)
                                }
                                Text("$${transaction.amount / 100.0}")
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTransactionScreen(navigator: Navigator, accountId: String, userId: String) {
    val viewModel: TransactionViewModel = koinInject()
    val state = viewModel.state

    LaunchedEffect(state.transactionCreated) {
        state.transactionCreated?.let {
            navigator.navigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Request Transaction") },
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateBack() }) {
                        Text("←")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            Row {
                FilterChip(
                    selected = state.type == TransactionType.INCOME,
                    onClick = { viewModel.onTypeSelected(TransactionType.INCOME) },
                    label = { Text("Income") }
                )
                Spacer(Modifier.width(8.dp))
                FilterChip(
                    selected = state.type == TransactionType.EXPENSE,
                    onClick = { viewModel.onTypeSelected(TransactionType.EXPENSE) },
                    label = { Text("Expense") }
                )
            }
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = state.title,
                onValueChange = viewModel::onTitleChanged,
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = if (state.amountCents == 0L) "" else (state.amountCents / 100.0).toString(),
                onValueChange = viewModel::onAmountChanged,
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = state.notes,
                onValueChange = viewModel::onNotesChanged,
                label = { Text("Notes (optional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))
            state.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(16.dp))
            }

            Button(
                onClick = { viewModel.createTransaction(accountId, userId) },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (state.isLoading) "Creating..." else "Submit Request")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PendingTransactionsScreen(navigator: Navigator, familyId: String, userId: String) {
    val dashboardViewModel: DashboardViewModel = koinInject()
    val transactionViewModel: TransactionViewModel = koinInject()
    val state by dashboardViewModel.state.collectAsState()

    LaunchedEffect(familyId, userId) {
        dashboardViewModel.loadDashboard(userId, familyId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pending Requests") },
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateBack() }) {
                        Text("←")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)
        ) {
            items(state.pendingTransactions) { transaction ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(transaction.title, style = MaterialTheme.typography.titleMedium)
                        Text("$${transaction.amount / 100.0}", style = MaterialTheme.typography.headlineSmall)
                        Text("Type: ${transaction.type.name}")
                        transaction.notes?.let { Text("Notes: $it") }

                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    transactionViewModel.approveTransaction(transaction.id, userId) {
                                        dashboardViewModel.refresh(userId, familyId)
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Approve")
                            }
                            OutlinedButton(
                                onClick = {
                                    transactionViewModel.denyTransaction(transaction.id, userId) {
                                        dashboardViewModel.refresh(userId, familyId)
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Deny")
                            }
                        }
                    }
                }
            }
        }
    }
}
