package com.sammy.fufo.common.blockentity;

import com.sammy.fufo.common.entity.weave.HologramWeaveEntity;
import com.sammy.fufo.common.entity.weave.WeaveEntity;
import com.sammy.fufo.common.recipe.WeaveRecipe;
import com.sammy.fufo.core.systems.magic.weaving.Bindable;
import com.sammy.fufo.core.systems.magic.weaving.StandardWeave;
import com.sammy.fufo.core.systems.magic.weaving.recipe.ItemStackBindable;
import com.sammy.ortus.helpers.BlockHelper;
import com.sammy.ortus.systems.blockentity.ItemHolderBlockEntity;
import com.sammy.ortus.systems.blockentity.OrtusBlockEntityInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import org.jetbrains.annotations.NotNull;

public class CrudePrimerBlockEntity extends ItemHolderBlockEntity {
    public CrudePrimerBlockEntity(BlockEntityType<? extends CrudePrimerBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        inventory = new OrtusBlockEntityInventory(1, 1){
            @Override
            public void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                BlockHelper.updateAndNotifyState(level, worldPosition);
            }
        };
    }

    @Override
    public void tick() {
        super.tick();
        if(!this.inventory.isEmpty()){
            WeaveEntity item = new WeaveEntity(level);
            item.weave = new StandardWeave(new ItemStackBindable(this.inventory.extractItem(0,1,false)));
            item.setPos(worldPosition.getX() + 0.5, worldPosition.getY() + 1.5, worldPosition.getZ() + 0.5);
            level.addFreshEntity(item);
        }
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return inventory.inventoryOptional.cast();
        }
        return super.getCapability(cap);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return inventory.inventoryOptional.cast();
        }
        return super.getCapability(cap, side);
    }
}
