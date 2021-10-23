package me.racci.raccicore.hooks

//class PlaceHolderHooker {
//
//    private val REGISTERED_PLACEHOLDERS = HashMap<String, Placeholder>()
//    private val REGISTERED_HOOKS = HashSet<PlaceholderHook>()
//
//    fun addHook(hook: PlaceholderHook) {
//        hook.registerIntegration();
//        REGISTERED_HOOKS.add(hook);
//    }
//
//    fun registerPlaceholder(placeholder: PlaceholderEntry) {
//        REGISTERED_PLACEHOLDERS.remove(placeholder.getIdentifier());
//        REGISTERED_PLACEHOLDERS.put(placeholder.getIdentifier(), placeholder);
//    }
//
//    fun getResult(identifier: String, player: Player? = null) : String {
//        val placeholder = REGISTERED_PLACEHOLDERS[identifier.lowercase()] ?: return ""
//        if(player == null && placeholder.requiresPlayer()) return ""
//        return placeholder.getResult(player)
//    }
//
//    fun translatePlaceholders(text: String, player: Player? = null): String {
//        var processed = text
//        for (integration in REGISTERED_HOOKS) {
//            processed = integration.translate(processed, player)
//        }
//        return processed
//    }
//
//}
//
//interface PlaceholderHook : Hook {
//
//    fun registerIntegration()
//
//    fun translate(text: String, player: Player? = null)
//}
//
//class PlaceholderEntry {
//
//    private val identifier: String
//    private val function: Function<Player, String>
//    private val requiresPlayer: Boolean
//
//
//}