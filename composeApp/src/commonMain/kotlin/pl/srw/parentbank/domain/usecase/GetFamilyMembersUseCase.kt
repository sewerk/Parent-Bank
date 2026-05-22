package pl.srw.parentbank.domain.usecase

import pl.srw.parentbank.domain.model.User
import pl.srw.parentbank.domain.repository.UserRepository
import pl.srw.parentbank.util.AppException
import pl.srw.parentbank.util.Outcome

class GetFamilyMembersUseCase(
    private val userRepository: UserRepository
) : UseCaseWithParams<String, List<User>>() {

    override suspend fun execute(params: String): Outcome<List<User>> {
        if (params.isBlank()) {
            return Outcome.Failure(AppException.ValidationError("familyId", "Family ID cannot be empty"))
        }
        return userRepository.getUsersByFamilyId(params)
    }
}
