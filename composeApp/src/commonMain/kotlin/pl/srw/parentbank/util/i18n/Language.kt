package pl.srw.parentbank.util.i18n

/**
 * Supported languages.
 */
enum class Language(val code: String) {
    ENGLISH("en"),
    POLISH("pl");

    companion object {
        fun fromCode(code: String): Language {
            return entries.find { it.code == code.lowercase().take(2) } ?: ENGLISH
        }
    }
}
