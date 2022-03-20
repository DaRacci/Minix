package dev.racci.minix.api.serializables

import dev.racci.minix.api.utils.data.Data
import org.spongepowered.configurate.serialize.TypeSerializer
import org.spongepowered.configurate.serialize.TypeSerializerCollection

object Serializer {

    val serializers: TypeSerializerCollection by lazy {
        TypeSerializerCollection.builder()
            .register(AttributeModifierSerializer.Configurate)
            .register(DurationSerializer.Configurate)
            .register(EnchantSerializer.Configurate)
            .register(LocationSerializer.Configurate)
            .register(NamespacedKeySerializer.Configurate)
            .register(PatternSerializer.Configurate)
            .register(PotionEffectSerializer.Configurate)
            .register(IntRangeSerializer.Configurate)
            .register(LongRangeSerializer.Configurate)
            .register(CharRangeSerializer.Configurate)
            .register(DoubleRangeSerializer.Configurate)
            .register(FloatRangeSerializer.Configurate)
            .register(Data.Serializer)
            .build()
    }

    inline fun <reified T> TypeSerializerCollection.Builder.register(serializer: TypeSerializer<T>): TypeSerializerCollection.Builder {
        return this.register(T::class.java, serializer)
    }
}
