package net.galaxycore.galaxycorecore.packets

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer

class PacketFactory {
    fun createEntityDestroyPacket(entity: Int): PacketContainer {
        val packet = PacketContainer(PacketType.Play.Server.ENTITY_DESTROY)
        packet.integers.write(0, 1)
        packet.integerArrays.write(0, intArrayOf(entity))
        return packet

    }
}