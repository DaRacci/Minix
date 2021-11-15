package me.racci.raccicore.locales

import me.racci.raccicore.utils.catch
import java.util.*

class LanguageTable(
    val locale: Locale
) {

    val messages = HashMap<LangKey, String>()

    fun addMessageBundle(
        bundleName: String,
        classLoader: ClassLoader = this::class.java.classLoader,
    ): Boolean {
        catch<MissingResourceException>({return false}) {
            return addResourceBundle(ResourceBundle.getBundle(bundleName, locale, classLoader, UTF8Control()))
        }
        return false
    }

    fun addResourceBundle(
        bundle: ResourceBundle,
    ): Boolean {
        for(key in bundle.keySet()) {
            messages[LangKey.of(key)] = bundle.getString(key)
        }
        return bundle.keySet().isNotEmpty()
    }

}