package org.valkyrienskies.mod.common

import io.netty.buffer.ByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import org.valkyrienskies.core.game.IPlayer
import org.valkyrienskies.core.networking.IVSPacket
import org.valkyrienskies.core.networking.IVSPacketToClientSender
import org.valkyrienskies.core.networking.VSPacketRegistry
import org.valkyrienskies.core.networking.impl.VSPacketSetupFastNetwork
import org.valkyrienskies.core.networking.impl.VSPacketShipDataList
import org.valkyrienskies.core.networking.udp.VSUdpClient
import org.valkyrienskies.core.pipelines.VSPipeline
import org.valkyrienskies.mod.common.networking.impl.VSPacketSetupUDPClientHandler
import org.valkyrienskies.mod.common.networking.impl.VSPacketShipDataClientHandler

/**
 * Registers the [IVSPacket]s, and stores [IVSPacketToClientSender]
 * and [IVSPacketToServerSender] packet senders.
 */
object VSNetworking {

    private val vsPacketRegistry = VSPacketRegistry<ServerPlayerEntity>()
    lateinit var shipDataPacketToClientSender: IVSPacketToClientSender<ServerPlayerEntity>
    lateinit var fastToClientSender: IVSPacketToClientSender<ServerPlayerEntity>
    lateinit var fastClient: VSUdpClient
    var usesUdp = false

    internal fun registerVSPackets() {
        vsPacketRegistry.registerVSPacket(
            VSPacketShipDataList::class.java,
            { VSPacketShipDataList.createEmpty() },
            VSPacketShipDataClientHandler,
            null
        )

        vsPacketRegistry.registerVSPacket(
            VSPacketSetupFastNetwork::class.java,
            { VSPacketSetupFastNetwork.createEmpty() },
            VSPacketSetupUDPClientHandler,
            null
        )
    }

    fun handleVSPacketClient(byteBuf: ByteBuf) {
        vsPacketRegistry.handleVSPacketClient(byteBuf)
    }

    fun handleVSPacketClient(vsPacket: IVSPacket) {
        vsPacketRegistry.handleVSPacketClient(vsPacket)
    }

    fun handleVSPacketServer(byteBuf: ByteBuf, sender: ServerPlayerEntity) {
        vsPacketRegistry.handleVSPacketServer(byteBuf, sender)
    }

    fun writeVSPacket(vsPacket: IVSPacket, byteBuf: ByteBuf) {
        vsPacketRegistry.writeVSPacket(vsPacket, byteBuf)
    }

    fun readVSPacket(byteBuf: ByteBuf): IVSPacket {
        return vsPacketRegistry.readVSPacket(byteBuf)
    }

    fun setupServer() {
        if (!this::fastToClientSender.isInitialized) {
            fastToClientSender = shipDataPacketToClientSender
        } else usesUdp = true

        VSPipeline.getVSPipeline().configureNetwork(fastToClientSender as IVSPacketToClientSender<IPlayer>)
    }
}
