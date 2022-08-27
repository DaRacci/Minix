package dev.racci.minix.api.serializables

import dev.racci.minix.api.utils.adventure.PartialComponent
import dev.racci.minix.api.utils.data.Data
import org.spongepowered.configurate.serialize.TypeSerializer
import org.spongepowered.configurate.serialize.TypeSerializerCollection

object Serializer {

    val serializers: TypeSerializerCollection by lazy {
        TypeSerializerCollection.builder()
            .registerExact(AttributeModifierSerializer.Configurate)
            .registerExact(DurationSerializer.Configurate)
            .registerExact(EnchantSerializer.Configurate)
            .registerExact(LocationSerializer.Configurate)
            .registerExact(NamespacedKeySerializer.Configurate)
            .registerExact(PatternSerializer.Configurate)
            .registerExact(PotionEffectSerializer.Configurate)
            .registerExact(IntRangeSerializer.Configurate)
            .registerExact(LongRangeSerializer.Configurate)
            .registerExact(CharRangeSerializer.Configurate)
            .registerExact(DoubleRangeSerializer.Configurate)
            .registerExact(FloatRangeSerializer.Configurate)
            .registerExact(Data.Serializer)
            .registerExact(LoggingLevelSerializer)
            .registerExact(PartialComponent.Serializer)
            .register(PairSerializer)
            .build()
    }

    inline fun <reified T> TypeSerializerCollection.Builder.registerExact(serializer: TypeSerializer<T>): TypeSerializerCollection.Builder {
        return this.registerExact(T::class.java, serializer)
    }

    inline fun <reified T> TypeSerializerCollection.Builder.register(serializer: TypeSerializer<T>): TypeSerializerCollection.Builder {
        return this.register(T::class.java, serializer)
    }
}
