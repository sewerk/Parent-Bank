package pl.srw.parentbank.util.i18n

import java.util.Locale

/**
 * Android implementation to get system language.
 */
actual fun getSystemLanguage(): Language {
    val languageCode = Locale.getDefault().language
    return Language.fromCode(languageCode)
}
