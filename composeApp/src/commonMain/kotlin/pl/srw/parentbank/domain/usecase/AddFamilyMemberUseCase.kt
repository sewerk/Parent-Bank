package pl.srw.parentbank.domain.usecase

import kotlinx.datetime.Clock
import pl.srw.parentbank.domain.model.User
import pl.srw.parentbank.domain.model.UserRole
import pl.srw.parentbank.domain.repository.UserRepository
import pl.srw.parentbank.util.AppException
import pl.srw.parentbank.util.Outcome
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class AddFamilyMemberParams(
    val familyId: String,
    val name: String,
    val role: UserRole,
    val age: Int? = null
)

class AddFamilyMemberUseCase(
    private val userRepository: UserRepository
) : UseCaseWithParams<AddFamilyMemberParams, User>() {

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun execute(params: AddFamilyMemberParams): Outcome<User> {
        if (params.name.isBlank()) {
            return Outcome.Failure(AppException.ValidationError("name", "Member name cannot be empty"))
        }
        if (params.role == UserRole.CHILD && params.age == null) {
            return Outcome.Failure(AppException.ValidationError("age", "Age is required for children"))
        }
        if (params.age != null && (params.age < 0 || params.age > 17)) {
            return Outcome.Failure(AppException.ValidationError("age", "Age must be between 0 and 17"))
        }

        val now = Clock.System.now().toEpochMilliseconds()
        val user = User(
            id = Uuid.random().toString(),
            familyId = params.familyId,
            name = params.name.trim(),
            role = params.role,
            age = params.age,
            createdAt = now,
            updatedAt = now
        )

        return userRepository.createUser(user)
    }
}
