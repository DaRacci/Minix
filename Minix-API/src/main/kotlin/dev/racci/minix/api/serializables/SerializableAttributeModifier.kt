package dev.racci.minix.api.serializables

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlot
import java.util.UUID

@Serializable
class SerializableAttributeModifier {

    @SerialName("UUID")
    @Serializable(with = UUIDSerializer::class)
    var uuid: UUID = UUID.randomUUID()

    @SerialName("Name")
    var name: String = ""

    @SerialName("Amount")
    var amount: Double = 0.0

    @SerialName("Operation")
    var operation: AttributeModifier.Operation = AttributeModifier.Operation.ADD_SCALAR

    @SerialName("EquipmentSlot")
    var slot: EquipmentSlot? = null

    fun toAttributeModifier(): AttributeModifier =
        AttributeModifier(
            uuid,
            name,
            amount,
            operation,
            slot
        )

    companion object {

        fun fromAttributeModifier(modifier: AttributeModifier): SerializableAttributeModifier {
            val serializable = SerializableAttributeModifier()
            with(serializable) {
                uuid = modifier.uniqueId
                name = modifier.name
                amount = modifier.amount
                operation = modifier.operation
                slot = modifier.slot
            }
            return serializable
        }
    }
}
