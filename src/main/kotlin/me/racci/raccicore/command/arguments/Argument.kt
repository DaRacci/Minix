package me.racci.raccicore.command.arguments

import me.racci.raccicore.command.Executor
import me.racci.raccicore.command.Failure
import me.racci.raccicore.command.TabCompleter
import me.racci.raccicore.utils.catchAndReturn


inline fun TabCompleter.argumentCompleteBuilder(
    index: Int,
    block: (String) -> List<String>
): List<String> {
    if(args.size == index+1) {
        return block(args.getOrNull(index) ?: "")
    }
    return emptyList()
}

inline fun <T> Executor<*>.optional(
    block: () -> T
) = catchAndReturn<Failure.CommandFailureException, T>({if(it.argMissing) return null else throw it}) {
    block()
}

inline fun <reified T> Executor<*>.array(
    startIndex: Int,
    endIndex: Int,
    usageIndexPerArgument: Int = 1,
    block: (index: Int) -> T
): Array<T> {
    if(endIndex <= startIndex) throw IllegalArgumentException("endIndex can't be lower or equal to startIndex.")
    if(usageIndexPerArgument <= 0) throw IllegalArgumentException("usageIndexPerArgument cannot be below 1.")
    val arguments = (endIndex - startIndex) / usageIndexPerArgument
    return Array(arguments) {
        block(startIndex + (it * usageIndexPerArgument))
    }
}