package pl.srw.parentbank.util.i18n

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

/**
 * iOS implementation to get system language.
 */
actual fun getSystemLanguage(): Language {
    val languageCode = NSLocale.currentLocale.languageCode
    return Language.fromCode(languageCode)
}
