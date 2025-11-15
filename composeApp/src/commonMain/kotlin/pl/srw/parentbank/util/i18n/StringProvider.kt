package pl.srw.parentbank.util.i18n

/**
 * Interface for providing localized strings.
 * Platform-specific implementations will use system language settings.
 */
interface StringProvider {
    fun getString(key: String): String
    fun getString(key: String, vararg args: Any): String
    fun getCurrentLanguage(): Language
}

/**
 * String resources container.
 */
internal object StringResources {
    /**
     * Default string resources for English.
     */
    val englishStrings = mapOf(
    Strings.APP_NAME to "Parent Bank",
    Strings.OK to "OK",
    Strings.CANCEL to "Cancel",
    Strings.SAVE to "Save",
    Strings.DELETE to "Delete",
    Strings.EDIT to "Edit",
    Strings.CLOSE to "Close",
    Strings.CONFIRM to "Confirm",
        Strings.ANSWER_YES to "Yes",
        Strings.ANSWER_NO to "No",
    Strings.ERROR to "Error",
    Strings.SUCCESS to "Success",
    Strings.LOADING to "Loading...",

    Strings.LOGIN to "Login",
    Strings.LOGOUT to "Logout",
    Strings.REGISTER to "Register",
    Strings.EMAIL to "Email",
    Strings.PASSWORD to "Password",
    Strings.FORGOT_PASSWORD to "Forgot Password",

    Strings.FAMILY to "Family",
    Strings.CREATE_FAMILY to "Create Family",
    Strings.JOIN_FAMILY to "Join Family",
    Strings.FAMILY_NAME to "Family Name",
    Strings.FAMILY_CODE to "Family Code",

    Strings.PARENT to "Parent",
    Strings.CHILD to "Child",

    Strings.ACCOUNT to "Account",
    Strings.BALANCE to "Balance",
    Strings.ACCOUNT_DETAILS to "Account Details",

    Strings.TRANSACTION to "Transaction",
    Strings.TRANSACTIONS to "Transactions",
    Strings.INCOME to "Income",
    Strings.EXPENSE to "Expense",
    Strings.AMOUNT to "Amount",
    Strings.TITLE to "Title",
    Strings.CATEGORY to "Category",
    Strings.DATE to "Date",
    Strings.NOTES to "Notes",
    Strings.PENDING to "Pending",
    Strings.APPROVED to "Approved",
    Strings.DENIED to "Denied",

    Strings.SCHEDULED_TRANSACTION to "Scheduled Transaction",
    Strings.SCHEDULED_TRANSACTIONS to "Scheduled Transactions",
    Strings.FREQUENCY to "Frequency",
    Strings.DAILY to "Daily",
    Strings.WEEKLY to "Weekly",
    Strings.BIWEEKLY to "Bi-weekly",
    Strings.MONTHLY to "Monthly",

    Strings.ERROR_NETWORK to "Network error occurred",
    Strings.ERROR_UNKNOWN to "An unknown error occurred",
    Strings.ERROR_VALIDATION to "Validation error",
    Strings.ERROR_AUTH to "Authentication error"
)

    /**
     * Default string resources for Polish.
     */
    val polishStrings = mapOf(
    Strings.APP_NAME to "Bank Rodzinny",
    Strings.OK to "OK",
    Strings.CANCEL to "Anuluj",
    Strings.SAVE to "Zapisz",
    Strings.DELETE to "Usuń",
    Strings.EDIT to "Edytuj",
    Strings.CLOSE to "Zamknij",
    Strings.CONFIRM to "Potwierdź",
        Strings.ANSWER_YES to "Tak",
        Strings.ANSWER_NO to "Nie",
    Strings.ERROR to "Błąd",
    Strings.SUCCESS to "Sukces",
    Strings.LOADING to "Ładowanie...",

    Strings.LOGIN to "Zaloguj się",
    Strings.LOGOUT to "Wyloguj się",
    Strings.REGISTER to "Zarejestruj się",
    Strings.EMAIL to "Email",
    Strings.PASSWORD to "Hasło",
    Strings.FORGOT_PASSWORD to "Zapomniałem hasła",

    Strings.FAMILY to "Rodzina",
    Strings.CREATE_FAMILY to "Utwórz rodzinę",
    Strings.JOIN_FAMILY to "Dołącz do rodziny",
    Strings.FAMILY_NAME to "Nazwa rodziny",
    Strings.FAMILY_CODE to "Kod rodziny",

    Strings.PARENT to "Rodzic",
    Strings.CHILD to "Dziecko",

    Strings.ACCOUNT to "Konto",
    Strings.BALANCE to "Saldo",
    Strings.ACCOUNT_DETAILS to "Szczegóły konta",

    Strings.TRANSACTION to "Transakcja",
    Strings.TRANSACTIONS to "Transakcje",
    Strings.INCOME to "Przychód",
    Strings.EXPENSE to "Wydatek",
    Strings.AMOUNT to "Kwota",
    Strings.TITLE to "Tytuł",
    Strings.CATEGORY to "Kategoria",
    Strings.DATE to "Data",
    Strings.NOTES to "Notatki",
    Strings.PENDING to "Oczekujące",
    Strings.APPROVED to "Zaakceptowane",
    Strings.DENIED to "Odrzucone",

    Strings.SCHEDULED_TRANSACTION to "Zaplanowana transakcja",
    Strings.SCHEDULED_TRANSACTIONS to "Zaplanowane transakcje",
    Strings.FREQUENCY to "Częstotliwość",
    Strings.DAILY to "Codziennie",
    Strings.WEEKLY to "Co tydzień",
    Strings.BIWEEKLY to "Co dwa tygodnie",
    Strings.MONTHLY to "Co miesiąc",

    Strings.ERROR_NETWORK to "Wystąpił błąd sieci",
    Strings.ERROR_UNKNOWN to "Wystąpił nieznany błąd",
    Strings.ERROR_VALIDATION to "Błąd walidacji",
    Strings.ERROR_AUTH to "Błąd uwierzytelniania"
)
}

/**
 * Simple string provider implementation.
 */
class SimpleStringProvider(private val language: Language = getSystemLanguage()) : StringProvider {
    private val strings = when (language) {
        Language.ENGLISH -> StringResources.englishStrings
        Language.POLISH -> StringResources.polishStrings
    }

    override fun getString(key: String): String {
        return strings[key] ?: key
    }

    override fun getString(key: String, vararg args: Any): String {
        val template = strings[key] ?: return key
        return formatString(template, *args)
    }

    override fun getCurrentLanguage(): Language = language
}

/**
 * Cross-platform string formatting using numbered placeholders {0}, {1}, etc.
 * This replaces the JVM-specific String.format() for multiplatform compatibility.
 */
private fun formatString(template: String, vararg args: Any): String {
    var result = template
    args.forEachIndexed { index, arg ->
        result = result.replace("{$index}", arg.toString())
    }
    return result
}
