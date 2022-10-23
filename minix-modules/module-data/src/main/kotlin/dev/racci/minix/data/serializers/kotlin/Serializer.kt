package dev.racci.minix.data.serializers.kotlin

import dev.racci.minix.api.utils.adventure.PartialComponent
import dev.racci.minix.data.serializers.configurate.ConfigurateAttributeModifierSerializer
import dev.racci.minix.data.serializers.configurate.ConfigurateDataSerializer
import dev.racci.minix.data.serializers.configurate.ConfigurateDurationSerializer
import dev.racci.minix.data.serializers.configurate.ConfigurateEnchantSerializer
import dev.racci.minix.data.serializers.configurate.ConfigurateLocationSerializer
import dev.racci.minix.data.serializers.configurate.ConfigurateNamespacedKeySerializer
import dev.racci.minix.data.serializers.configurate.ConfiguratePairSerializer
import dev.racci.minix.data.serializers.configurate.ConfiguratePatternSerializer
import dev.racci.minix.data.serializers.configurate.ConfiguratePotionSerializer
import dev.racci.minix.data.serializers.kotlin.minecraft.BlockPosSerializer
import org.spongepowered.configurate.serialize.TypeSerializer
import org.spongepowered.configurate.serialize.TypeSerializerCollection

public object Serializer {

    public val serializers: TypeSerializerCollection by lazy {
        TypeSerializerCollection.builder()
            .registerExact(ConfigurateAttributeModifierSerializer)
            .registerExact(ConfigurateDurationSerializer)
            .registerExact(ConfigurateEnchantSerializer)
            .registerExact(ConfigurateLocationSerializer)
            .registerExact(ConfigurateNamespacedKeySerializer)
            .registerExact(ConfiguratePatternSerializer)
            .registerExact(ConfiguratePotionSerializer)
            .registerExact(IntRangeSerializer.Configurate)
            .registerExact(LongRangeSerializer.Configurate)
            .registerExact(CharRangeSerializer.Configurate)
            .registerExact(DoubleRangeSerializer.Configurate)
            .registerExact(FloatRangeSerializer.Configurate)
            .registerExact(ConfigurateDataSerializer)
            .registerExact(PartialComponent.Serializer)
            .registerExact(BlockPosSerializer)
            .register(ConfiguratePairSerializer)
            .build()
    }

    private inline fun <reified T> TypeSerializerCollection.Builder.registerExact(serializer: TypeSerializer<T>): TypeSerializerCollection.Builder {
        return this.registerExact(T::class.java, serializer)
    }

    private inline fun <reified T> TypeSerializerCollection.Builder.register(serializer: TypeSerializer<T>): TypeSerializerCollection.Builder {
        return this.register(T::class.java, serializer)
    }
}
