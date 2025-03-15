package com.hotbarsurvival;

import net.minecraftforge.common.capabilities.Capability; import net.minecraftforge.common.capabilities.CapabilityManager; import net.minecraftforge.common.capabilities.CapabilityToken; import net.minecraftforge.common.capabilities.ICapabilitySerializable; import net.minecraft.nbt.CompoundTag; import net.minecraftforge.common.util.INBTSerializable; import net.minecraft.core.Direction;

public class HotbarCapability { public static final Capability<HotbarStorage> HOTBAR_CAP = CapabilityManager.get(new CapabilityToken<>() {});

public static class HotbarStorage implements INBTSerializable<CompoundTag> {
    private final CompoundTag hotbarData = new CompoundTag();

    public CompoundTag getHotbarData(int slot) {
        return hotbarData.getCompound("hotbar" + slot);
    }

    public void setHotbarData(int slot, CompoundTag data) {
        hotbarData.put("hotbar" + slot, data);
    }

    @Override
    public CompoundTag serializeNBT() {
        return hotbarData.copy();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        hotbarData.copyFrom(nbt);
    }
}

public static class Provider implements ICapabilitySerializable<CompoundTag> {
    private final HotbarStorage storage = new HotbarStorage();

    @Override
    public CompoundTag serializeNBT() {
        return storage.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        storage.deserializeNBT(nbt);
    }

    @Override
    public <T> Capability<T> getCapability(Capability<T> cap, Direction side) {
        return cap == HOTBAR_CAP ? HOTBAR_CAP.orEmpty(cap, storage) : null;
    }
}

