package org.valkyrienskies.mod.common.networking.impl

import net.minecraft.client.MinecraftClient
import org.valkyrienskies.core.networking.IVSPacket
import org.valkyrienskies.core.networking.IVSPacketClientHandler
import org.valkyrienskies.core.networking.impl.VSPacketShipDataList
import org.valkyrienskies.mod.common.shipObjectWorld

object VSPacketShipDataClientHandler : IVSPacketClientHandler {
    override fun handlePacket(vsPacket: IVSPacket) {
        vsPacket as VSPacketShipDataList

        val gameWorld = MinecraftClient.getInstance().world
        val shipWorld = gameWorld?.shipObjectWorld ?: return

        vsPacket.shipDataList.forEach {
            if (shipWorld.queryableShipData.getShipDataFromUUID(it.shipUUID) == null) {
                // Convert [ShipDataCommon] to [ShipDataClient]
                shipWorld.queryableShipData.addShipData(it)
            } else {
                // Update the next ship transform
                shipWorld.shipObjects[it.shipUUID]?.updateNextShipTransform(it.shipTransform)
            }
        }
    }
}
