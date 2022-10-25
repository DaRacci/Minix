package dev.racci.minix.api.extensions

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.ComponentLike

@JvmName("sendAudience")
public infix fun Audience.send(message: ComponentLike) { this.sendMessage(message) }

@JvmName("sendAudienceArray")
public infix fun Audience.send(messages: Array<ComponentLike>) { messages.forEach(::send) }

@JvmName("sendAudienceCollection")
public infix fun Audience.send(messages: Collection<ComponentLike>) { messages.forEach(::send) }

@JvmName("msgAudience")
public infix fun ComponentLike.msg(audience: Audience) { audience send this }

@JvmName("msgAudienceArray")
public infix fun Array<ComponentLike>.msg(audience: Audience) { audience send this }

@JvmName("msgAudienceCollection")
public infix fun Collection<ComponentLike>.msg(audience: Audience) { audience send this }
