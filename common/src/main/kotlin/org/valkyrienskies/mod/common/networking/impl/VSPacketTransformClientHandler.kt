package org.valkyrienskies.mod.common.networking.impl

import net.minecraft.client.MinecraftClient
import org.valkyrienskies.core.networking.IVSPacket
import org.valkyrienskies.core.networking.IVSPacketClientHandler
import org.valkyrienskies.core.networking.impl.VSPacketTransform
import org.valkyrienskies.mod.common.shipObjectWorld

object VSPacketTransformClientHandler : IVSPacketClientHandler {
    override fun handlePacket(vsPacket: IVSPacket) {
        vsPacket as VSPacketTransform

        val gameWorld = MinecraftClient.getInstance().world
        val shipWorld = gameWorld?.shipObjectWorld ?: return

        vsPacket.ships.forEach {
            shipWorld.shipObjects[it.uuid]?.updateNextShipTransform(it.transform!!)
        }
    }
}
