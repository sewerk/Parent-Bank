package pl.srw.parentbank.presentation.setup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.srw.parentbank.domain.model.User
import pl.srw.parentbank.domain.model.UserRole

@Composable
fun MembersListScreen(
    uiState: FamilySetupUiState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = uiState.familyName,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "Code: ${uiState.familyCode}",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Family Members",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.members) { member ->
                MemberCard(member)
            }
        }
    }
}

@Composable
private fun MemberCard(user: User) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleMedium
                )
                if (user.age != null) {
                    Text(
                        text = "Age: ${user.age}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            RoleBadge(user.role)
        }
    }
}

@Composable
internal fun MemberRow(user: User) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = user.name,
            style = MaterialTheme.typography.bodyMedium
        )
        RoleBadge(user.role)
    }
}

@Composable
private fun RoleBadge(role: UserRole) {
    Text(
        text = when (role) {
            UserRole.PARENT -> "Parent"
            UserRole.CHILD -> "Child"
        },
        style = MaterialTheme.typography.labelMedium,
        color = when (role) {
            UserRole.PARENT -> MaterialTheme.colorScheme.primary
            UserRole.CHILD -> MaterialTheme.colorScheme.tertiary
        }
    )
}
