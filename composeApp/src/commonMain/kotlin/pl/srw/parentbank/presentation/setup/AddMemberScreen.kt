package pl.srw.parentbank.presentation.setup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import pl.srw.parentbank.domain.model.UserRole

@Composable
fun AddMemberScreen(
    uiState: FamilySetupUiState,
    onAddMember: (name: String, role: UserRole, age: Int?) -> Unit,
    onDone: () -> Unit,
    onErrorShown: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf(UserRole.CHILD) }
    var ageText by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
            onErrorShown()
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Add Family Members",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Family: ${uiState.familyName}",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Member name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Text("Role", style = MaterialTheme.typography.labelLarge)

            Spacer(Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedRole == UserRole.PARENT,
                    onClick = { selectedRole = UserRole.PARENT },
                    label = { Text("Parent") }
                )
                FilterChip(
                    selected = selectedRole == UserRole.CHILD,
                    onClick = { selectedRole = UserRole.CHILD },
                    label = { Text("Child") }
                )
            }

            if (selectedRole == UserRole.CHILD) {
                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = ageText,
                    onValueChange = { ageText = it.filter { c -> c.isDigit() } },
                    label = { Text("Age") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(24.dp))

            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                val isValid = name.isNotBlank() &&
                    (selectedRole == UserRole.PARENT || ageText.isNotBlank())

                Button(
                    onClick = {
                        val age = if (selectedRole == UserRole.CHILD) ageText.toIntOrNull() else null
                        onAddMember(name, selectedRole, age)
                        name = ""
                        ageText = ""
                    },
                    enabled = isValid,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Member")
                }
            }

            Spacer(Modifier.height(16.dp))

            if (uiState.members.size >= 2) {
                Button(
                    onClick = onDone,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Done (${uiState.members.size} members)")
                }
            }

            Spacer(Modifier.height(16.dp))

            if (uiState.members.isNotEmpty()) {
                Text(
                    text = "Members added:",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                uiState.members.forEach { member ->
                    MemberRow(member)
                    Spacer(Modifier.height(4.dp))
                }
            }
        }
    }
}
