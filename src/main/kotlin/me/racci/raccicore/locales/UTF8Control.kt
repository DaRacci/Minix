package me.racci.raccicore.locales

import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.*

class UTF8Control: ResourceBundle.Control() {

    override fun newBundle(
        baseName: String,
        locale: Locale,
        format: String,
        loader: ClassLoader,
        reload: Boolean,
    ): ResourceBundle? {
        val bn = toBundleName(baseName, locale)
        val rn = toResourceName(bn, "properties")
        val b: ResourceBundle?
        val s: InputStream? = if(reload) {
            val url = loader.getResource(rn)
            val c = url?.openConnection()
            c?.useCaches = false
            c?.getInputStream()
        } else loader.getResourceAsStream(rn)
        s.use {
            b = PropertyResourceBundle(InputStreamReader(it!!, StandardCharsets.UTF_8))
        }
        return b
    }

}