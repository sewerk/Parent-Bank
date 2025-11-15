package pl.srw.parentbank.domain.usecase.user

import kotlinx.datetime.Clock
import pl.srw.parentbank.domain.model.User
import pl.srw.parentbank.domain.model.UserRole
import pl.srw.parentbank.domain.repository.UserRepository
import pl.srw.parentbank.domain.usecase.UseCaseWithParams
import pl.srw.parentbank.util.AppException
import pl.srw.parentbank.util.Outcome

/**
 * Use case for creating a new user (parent or child).
 */
class CreateUserUseCase(
    private val userRepository: UserRepository
) : UseCaseWithParams<CreateUserUseCase.Params, User>() {

    override suspend fun execute(params: Params): Outcome<User> {
        // Validate input
        if (params.name.isBlank()) {
            return Outcome.Failure(
                AppException.ValidationError("name", "User name cannot be empty")
            )
        }
        if (params.familyId.isBlank()) {
            return Outcome.Failure(
                AppException.ValidationError("familyId", "Family ID cannot be empty")
            )
        }

        // Validate role-specific requirements
        when (params.role) {
            UserRole.PARENT -> {
                if (params.email.isNullOrBlank()) {
                    return Outcome.Failure(
                        AppException.ValidationError("email", "Parents must have an email")
                    )
                }
            }
            UserRole.CHILD -> {
                if (params.age == null || params.age < 1) {
                    return Outcome.Failure(
                        AppException.ValidationError("age", "Children must have a valid age")
                    )
                }
            }
        }

        val currentTime = Clock.System.now().toEpochMilliseconds()
        val user = User(
            id = params.id,
            familyId = params.familyId,
            name = params.name,
            email = params.email,
            role = params.role,
            age = params.age,
            profilePictureUrl = params.profilePictureUrl,
            createdAt = currentTime,
            updatedAt = currentTime
        )

        return userRepository.createUser(user)
    }

    data class Params(
        val id: String,
        val familyId: String,
        val name: String,
        val email: String? = null,
        val role: UserRole,
        val age: Int? = null,
        val profilePictureUrl: String? = null
    )
}
