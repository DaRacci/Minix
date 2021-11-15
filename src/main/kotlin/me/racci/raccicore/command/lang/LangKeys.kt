package me.racci.raccicore.command.lang

import me.racci.raccicore.locales.LangKey
import me.racci.raccicore.locales.LangKeyProvider
import java.util.*

enum class LangKeys: LangKeyProvider {
    PERMISSION_DENIED,
    PERMISSION_DENIED_PARAMETER, // WHAT?

    ERROR_GENERIC_LOGGED, // WHAT?

    UNKOWN_COMMAND,
    INVALID_SYNTAX,

    ERROR_PREFIX, // WHAT?
    ERROR_PERFORMING_COMMAND,

    INFO_MESSAGE,
    PLEASE_SPECIFY_ONE_OF, // WHAT?

    MUST_BE_A_NUMBER,
    MUST_BE_MIN_LENGTH,
    MUST_BE_MAX_LENGTH,

    PLEASE_SPECIFY_AT_LEAST,
    PLEASE_SPECIFY_AT_MOST,

    NOT_ALLOWED_ON_CONSOLE,

    COULD_NOT_FIND_PLAYER,

    NO_COMMAND_MATCHED_SEARCH, // WHAT?

    HELP_PAGE_INFORMATION,
    HELP_NO_RESULTS,
    HELP_HEADER,
    HELP_FORMAT,
    HELP_DETAILED_HEADER,
    HELP_DETAILED_COMMAND_FORMAT,
    HELP_DETAILED_PARAMETER_FORMAT,
    HELP_SEARCH_HEADER;

    override val langKey = LangKey.of("RacciCore.${name.replaceFirstChar {it.titlecase(Locale.ENGLISH)}}")

}