package me.racci.raccicore.locales

import java.util.*
import java.util.function.Function


class LocaleManager<T> internal constructor(
    private val localeMapper: Function<T, Locale>,
    var defaultLocale: Locale = Locale.ENGLISH,
) {

    private val tables = HashMap<Locale, LanguageTable>()

    fun setDefaultLocal(
        defaultLocale: Locale,
    ): Locale {
        val p = this.defaultLocale
        this.defaultLocale = defaultLocale
        return p
    }

    /**
     * If a list of locales is supplied, loads the matching message bundle for each locale.
     * If none are supplied, just the default locale is loaded.
     */
    fun addMessageBundle(
        bundleName: String,
        classLoader: ClassLoader = this::class.java.classLoader,
        vararg locales: Locale,
    ) {
        var ls = arrayOf(*locales)
        if(locales.isEmpty()) ls = arrayOf(defaultLocale)
        val found = ls.firstOrNull {getTable(it).addMessageBundle(bundleName, classLoader)} != null
    }

    fun addMessages(
        locale: Locale,
        messages: Map<LangKey, String>,
    ) = getTable(locale).messages.putAll(messages)

    fun addMessages(
        locale: Locale,
        vararg messages: Pair<LangKey, String>,
    ) = getTable(locale).messages.putAll(messages)

    fun addMessage(
        locale: Locale,
        key: LangKey,
        message: String,
    ) {
        getTable(locale).messages[key] = message
    }


    fun getMessage(
        context: T,
        key: LangKey,
    ): String {
        val l = localeMapper.apply(context)
        var m = getTable(l).messages[key]
        if(m == null && l.country.isNotEmpty()) {
            m = getTable(Locale(l.language)).messages[key]
        }
        if(m == null && l != defaultLocale) {
            m = getTable(defaultLocale).messages[key]
        }
        return m ?: ""
    }

    fun getTable(
        locale: Locale,
    ) = tables.computeIfAbsent(locale) {LanguageTable(locale)}

    fun addResourceBundle(
        bundle: ResourceBundle,
        locale: Locale,
    ) = getTable(locale).addResourceBundle(bundle)

    companion object {
        /**
         *
         * @param localeMapper Mapper to map a context to Locale
         * @param defaultLocale Default Locale
         * @param T Context Class Type
         */
        fun <T> create(localeMapper: Function<T, Locale>, defaultLocale: Locale = Locale.ENGLISH) =
                LocaleManager(localeMapper, defaultLocale)
    }

}