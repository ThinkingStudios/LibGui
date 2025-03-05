package org.thinkingstudio.libgui_foxified.network;

import io.github.cottonmc.cotton.gui.impl.LibGuiCommon;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record LibGuiPacket(int syncId, Identifier message, PacketByteBuf rest) implements CustomPayload {
    public static final Identifier ID = LibGuiCommon.id("screen_message");

    public LibGuiPacket(PacketByteBuf buf) {
        this(buf.readVarInt(), buf.readIdentifier(), buf);
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeVarInt(syncId);
        buf.writeIdentifier(message);
        buf.writeBytes(rest);
    }

    @Override
    public Identifier id() {
        return ID;
    }
}
