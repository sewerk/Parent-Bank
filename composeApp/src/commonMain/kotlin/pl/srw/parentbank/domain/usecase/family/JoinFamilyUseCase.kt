package pl.srw.parentbank.domain.usecase.family

import pl.srw.parentbank.domain.model.Family
import pl.srw.parentbank.domain.repository.FamilyRepository
import pl.srw.parentbank.domain.usecase.UseCaseWithParams
import pl.srw.parentbank.util.AppException
import pl.srw.parentbank.util.Outcome

/**
 * Use case for joining an existing family using family code.
 */
class JoinFamilyUseCase(
    private val familyRepository: FamilyRepository
) : UseCaseWithParams<JoinFamilyUseCase.Params, Family>() {

    override suspend fun execute(params: Params): Outcome<Family> {
        // Validate input
        if (params.familyCode.isBlank()) {
            return Outcome.Failure(
                AppException.ValidationError("familyCode", "Family code cannot be empty")
            )
        }
        if (params.familyCode.length != 6) {
            return Outcome.Failure(
                AppException.ValidationError("familyCode", "Family code must be 6 characters")
            )
        }

        // Try to find family by code
        return familyRepository.getFamilyByCode(params.familyCode.uppercase())
    }

    data class Params(
        val familyCode: String
    )
}
