package pl.srw.parentbank.domain.repository

import pl.srw.parentbank.domain.model.Account
import pl.srw.parentbank.util.Outcome
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for account operations.
 * Supports negative balances for loan/credit teaching.
 */
interface AccountRepository {
    suspend fun createAccount(account: Account): Outcome<Account>
    suspend fun getAccountById(id: String): Outcome<Account>
    suspend fun getAccountByChildId(childId: String): Outcome<Account>
    suspend fun getAccountsByFamilyId(familyId: String): Outcome<List<Account>>
    suspend fun updateAccount(account: Account): Outcome<Account>
    suspend fun updateBalance(accountId: String, newBalance: Long): Outcome<Account>
    suspend fun deactivateAccount(id: String): Outcome<Unit>
    fun observeAccount(id: String): Flow<Outcome<Account>>
    fun observeFamilyAccounts(familyId: String): Flow<Outcome<List<Account>>>
}
