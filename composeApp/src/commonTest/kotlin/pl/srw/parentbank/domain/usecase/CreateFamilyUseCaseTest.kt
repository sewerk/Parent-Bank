package pl.srw.parentbank.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import pl.srw.parentbank.domain.model.Family
import pl.srw.parentbank.domain.model.User
import pl.srw.parentbank.domain.model.UserRole
import pl.srw.parentbank.domain.repository.FamilyRepository
import pl.srw.parentbank.domain.repository.UserRepository
import pl.srw.parentbank.util.AppException
import pl.srw.parentbank.util.Outcome
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class CreateFamilyUseCaseTest {

    private val fakeFamilyRepo = FakeFamilyRepository()
    private val fakeUserRepo = FakeUserRepository()
    private val useCase = CreateFamilyUseCase(fakeFamilyRepo, fakeUserRepo)

    @Test
    fun createFamilyWithValidInput() = runTest {
        val result = useCase(CreateFamilyParams("Test Family", "John"))

        assertIs<Outcome.Success<Family>>(result)
        assertEquals("Test Family", result.data.name)
        assertEquals(1, fakeFamilyRepo.families.size)
        assertEquals(1, fakeUserRepo.users.size)
        assertEquals(UserRole.PARENT, fakeUserRepo.users.first().role)
        assertEquals("John", fakeUserRepo.users.first().name)
    }

    @Test
    fun createFamilyWithBlankNameFails() = runTest {
        val result = useCase(CreateFamilyParams("", "John"))

        assertIs<Outcome.Failure>(result)
        assertIs<AppException.ValidationError>(result.exception)
    }

    @Test
    fun createFamilyWithBlankCreatorNameFails() = runTest {
        val result = useCase(CreateFamilyParams("Family", ""))

        assertIs<Outcome.Failure>(result)
        assertIs<AppException.ValidationError>(result.exception)
    }

    @Test
    fun createFamilyTrimsWhitespace() = runTest {
        val result = useCase(CreateFamilyParams("  My Family  ", "  John  "))

        assertIs<Outcome.Success<Family>>(result)
        assertEquals("My Family", result.data.name)
        assertEquals("John", fakeUserRepo.users.first().name)
    }

    @Test
    fun createFamilyGeneratesFamilyCode() = runTest {
        val result = useCase(CreateFamilyParams("Family", "John"))

        assertIs<Outcome.Success<Family>>(result)
        assertTrue(result.data.familyCode.isNotBlank())
        assertEquals(6, result.data.familyCode.length)
    }
}

class AddFamilyMemberUseCaseTest {

    private val fakeUserRepo = FakeUserRepository()
    private val useCase = AddFamilyMemberUseCase(fakeUserRepo)

    @Test
    fun addChildWithValidInput() = runTest {
        val result = useCase(AddFamilyMemberParams("family-1", "Alice", UserRole.CHILD, 10))

        assertIs<Outcome.Success<User>>(result)
        assertEquals("Alice", result.data.name)
        assertEquals(UserRole.CHILD, result.data.role)
        assertEquals(10, result.data.age)
    }

    @Test
    fun addParentWithValidInput() = runTest {
        val result = useCase(AddFamilyMemberParams("family-1", "Mom", UserRole.PARENT))

        assertIs<Outcome.Success<User>>(result)
        assertEquals(UserRole.PARENT, result.data.role)
    }

    @Test
    fun addChildWithoutAgeFails() = runTest {
        val result = useCase(AddFamilyMemberParams("family-1", "Alice", UserRole.CHILD, null))

        assertIs<Outcome.Failure>(result)
        assertIs<AppException.ValidationError>(result.exception)
    }

    @Test
    fun addMemberWithBlankNameFails() = runTest {
        val result = useCase(AddFamilyMemberParams("family-1", "", UserRole.CHILD, 10))

        assertIs<Outcome.Failure>(result)
        assertIs<AppException.ValidationError>(result.exception)
    }

    @Test
    fun addChildWithInvalidAgeFails() = runTest {
        val result = useCase(AddFamilyMemberParams("family-1", "Alice", UserRole.CHILD, 18))

        assertIs<Outcome.Failure>(result)
        assertIs<AppException.ValidationError>(result.exception)
    }
}

// --- Fakes ---

private class FakeFamilyRepository : FamilyRepository {
    val families = mutableListOf<Family>()

    override suspend fun createFamily(family: Family): Outcome<Family> {
        families.add(family)
        return Outcome.Success(family)
    }

    override suspend fun getFamilyById(id: String): Outcome<Family> {
        val family = families.find { it.id == id }
        return if (family != null) Outcome.Success(family)
        else Outcome.Failure(AppException.BusinessError("Not found"))
    }

    override suspend fun getFamilyByCode(code: String): Outcome<Family> {
        val family = families.find { it.familyCode == code }
        return if (family != null) Outcome.Success(family)
        else Outcome.Failure(AppException.BusinessError("Not found"))
    }

    override suspend fun updateFamily(family: Family): Outcome<Family> = Outcome.Success(family)
    override suspend fun deleteFamily(id: String): Outcome<Unit> = Outcome.Success(Unit)
    override fun observeFamily(id: String): Flow<Outcome<Family>> = flowOf()
}

private class FakeUserRepository : UserRepository {
    val users = mutableListOf<User>()

    override suspend fun createUser(user: User): Outcome<User> {
        users.add(user)
        return Outcome.Success(user)
    }

    override suspend fun getUserById(id: String): Outcome<User> {
        val user = users.find { it.id == id }
        return if (user != null) Outcome.Success(user)
        else Outcome.Failure(AppException.BusinessError("Not found"))
    }

    override suspend fun getUsersByFamilyId(familyId: String): Outcome<List<User>> {
        return Outcome.Success(users.filter { it.familyId == familyId })
    }

    override suspend fun updateUser(user: User): Outcome<User> = Outcome.Success(user)
    override suspend fun deleteUser(id: String): Outcome<Unit> = Outcome.Success(Unit)
    override fun observeUser(id: String): Flow<Outcome<User>> = flowOf()
    override fun observeFamilyUsers(familyId: String): Flow<Outcome<List<User>>> = flowOf()
}
