package pl.srw.parentbank.domain.usecase

import kotlinx.datetime.Clock
import pl.srw.parentbank.domain.model.Family
import pl.srw.parentbank.domain.model.User
import pl.srw.parentbank.domain.model.UserRole
import pl.srw.parentbank.domain.repository.FamilyRepository
import pl.srw.parentbank.domain.repository.UserRepository
import pl.srw.parentbank.util.AppException
import pl.srw.parentbank.util.Outcome
import pl.srw.parentbank.util.flatMap
import kotlin.random.Random
import kotlin.uuid.Uuid

data class CreateFamilyParams(
    val familyName: String,
    val creatorName: String
)

class CreateFamilyUseCase(
    private val familyRepository: FamilyRepository,
    private val userRepository: UserRepository
) : UseCaseWithParams<CreateFamilyParams, Family>() {

    override suspend fun execute(params: CreateFamilyParams): Outcome<Family> {
        if (params.familyName.isBlank()) {
            return Outcome.Failure(AppException.ValidationError("familyName", "Family name cannot be empty"))
        }
        if (params.creatorName.isBlank()) {
            return Outcome.Failure(AppException.ValidationError("creatorName", "Creator name cannot be empty"))
        }

        val now = Clock.System.now().toEpochMilliseconds()
        val family = Family(
            id = Uuid.random().toString(),
            name = params.familyName.trim(),
            familyCode = generateFamilyCode(),
            currency = "PLN",
            createdAt = now,
            updatedAt = now
        )

        return familyRepository.createFamily(family).flatMap { createdFamily ->
            val creator = User(
                id = Uuid.random().toString(),
                familyId = createdFamily.id,
                name = params.creatorName.trim(),
                role = UserRole.PARENT,
                createdAt = now,
                updatedAt = now
            )
            userRepository.createUser(creator).flatMap {
                Outcome.Success(createdFamily)
            }
        }
    }

    private fun generateFamilyCode(): String {
        val chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
        return (1..6).map { chars[Random.nextInt(chars.length)] }.joinToString("")
    }
}
