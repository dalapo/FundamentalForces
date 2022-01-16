package com.project_esoterica.esoterica.common.entity.falling;

import com.project_esoterica.esoterica.core.setup.EntityRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class FallingCrashpodEntity extends FallingEntity {
    public FallingCrashpodEntity(Level p_37249_) {
        super(EntityRegistry.FALLING_CRASHPOD.get(), p_37249_);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag p_20052_) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag p_20139_) {

    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}