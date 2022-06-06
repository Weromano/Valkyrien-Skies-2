package org.valkyrienskies.mod.common.networking.impl

import org.valkyrienskies.core.networking.IVSPacket
import org.valkyrienskies.core.networking.IVSPacketClientHandler
import org.valkyrienskies.core.networking.impl.VSPacketSetupFastNetwork
import org.valkyrienskies.core.networking.udp.VSUdpClient
import org.valkyrienskies.mod.common.VSNetworking

object VSPacketSetupUDPClientHandler : IVSPacketClientHandler {
    override fun handlePacket(vsPacket: IVSPacket) {
        vsPacket as VSPacketSetupFastNetwork

        if (!vsPacket.noUdp) {
            val address = TODO("Get current minecraft servers address")
            VSNetworking.usesUdp = true
            VSNetworking.fastClient = VSUdpClient(address)
        }
    }
}
