package me.racci.raccicore.utils

import me.racci.raccicore.RacciPlugin
import me.racci.raccicore.utils.extensions.KotlinListener
import net.kyori.adventure.text.BlockNBTComponent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.EntityNBTComponent
import net.kyori.adventure.text.KeybindComponent
import net.kyori.adventure.text.ScoreComponent
import net.kyori.adventure.text.SelectorComponent
import net.kyori.adventure.text.StorageNBTComponent
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.TextReplacementConfig
import net.kyori.adventure.text.TranslatableComponent
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.util.RGBLike

inline fun <reified T : Throwable, reified U : Any> catch(
    err: (T) -> U,
    run: () -> U
): U = try {
    run()
} catch (ex: Throwable) {
    if (ex is T) err(ex) else throw ex
}

inline fun <reified T : Throwable> catch(
    err: (T) -> Unit = { it.printStackTrace() },
    run: () -> Unit
): Unit = catch<T, Unit>(err, run)

inline fun <reified T : Throwable, reified U : Any> catch(
    default: U,
    run: () -> U
): U = catch<T, U>({ default }, run)

inline fun <reified T : BukkitEvent> RacciPlugin.listen(
    priority: BukkitEventPriority = BukkitEventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    crossinline callback: (T) -> Unit
) = pm.registerEvent(T::class.java, object : KotlinListener {},
    priority, { _, it -> if(it is T) callback(it) },
    this, ignoreCancelled
)

fun <T> T.not(other: T) = takeUnless { it == other }
fun <T> T.notIn(container: Iterable<T>) = takeUnless { it in container }


fun blockNBT(builder: BlockNBTComponent.Builder.() -> Unit): BlockNBTComponent = Component.blockNBT(builder)

fun entityNBT(builder: EntityNBTComponent.Builder.() -> Unit): EntityNBTComponent = Component.entityNBT(builder)

fun keybind(builder: KeybindComponent.Builder.() -> Unit): KeybindComponent = Component.keybind(builder)

fun score(builder: ScoreComponent.Builder.() -> Unit): ScoreComponent = Component.score(builder)

fun selector(builder: SelectorComponent.Builder.() -> Unit): SelectorComponent = Component.selector(builder)

fun storageNBT(builder: StorageNBTComponent.Builder.() -> Unit): StorageNBTComponent = Component.storageNBT(builder)

fun text(builder: TextComponent.Builder.() -> Unit): TextComponent = Component.text(builder)

fun TextComponent.replace(builder: TextReplacementConfig.Builder.() -> Unit) = builder

fun translatable(builder: TranslatableComponent.Builder.() -> Unit): TranslatableComponent = Component.translatable(builder)

operator fun Component.plus(that: ComponentLike): Component = this.append(that)

operator fun RGBLike.component1(): Int = this.red()

operator fun RGBLike.component2(): Int = this.green()

operator fun RGBLike.component3(): Int = this.blue()

fun style(builder: Style.Builder.() -> Unit): Style = Style.style(builder)