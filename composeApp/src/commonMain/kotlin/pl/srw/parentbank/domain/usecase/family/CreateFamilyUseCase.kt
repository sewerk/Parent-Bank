package pl.srw.parentbank.domain.usecase.family

import kotlinx.datetime.Clock
import pl.srw.parentbank.domain.model.Family
import pl.srw.parentbank.domain.repository.FamilyRepository
import pl.srw.parentbank.domain.usecase.UseCaseWithParams
import pl.srw.parentbank.util.AppException
import pl.srw.parentbank.util.Outcome
import kotlin.random.Random

/**
 * Use case for creating a new family.
 * Generates a unique 6-character family code.
 */
class CreateFamilyUseCase(
    private val familyRepository: FamilyRepository
) : UseCaseWithParams<CreateFamilyUseCase.Params, Family>() {

    override suspend fun execute(params: Params): Outcome<Family> {
        // Validate input
        if (params.name.isBlank()) {
            return Outcome.Failure(
                AppException.ValidationError("name", "Family name cannot be empty")
            )
        }
        if (params.currency.isBlank()) {
            return Outcome.Failure(
                AppException.ValidationError("currency", "Currency cannot be empty")
            )
        }

        // Generate unique family code
        val familyCode = generateFamilyCode()

        val currentTime = Clock.System.now().toEpochMilliseconds()
        val family = Family(
            id = params.id,
            name = params.name,
            familyCode = familyCode,
            currency = params.currency,
            createdAt = currentTime,
            updatedAt = currentTime
        )

        return familyRepository.createFamily(family)
    }

    /**
     * Generates a random 6-character alphanumeric family code.
     */
    private fun generateFamilyCode(): String {
        val chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789" // Excluded similar-looking chars
        return (1..6)
            .map { chars[Random.nextInt(chars.length)] }
            .joinToString("")
    }

    data class Params(
        val id: String,
        val name: String,
        val currency: String
    )
}
