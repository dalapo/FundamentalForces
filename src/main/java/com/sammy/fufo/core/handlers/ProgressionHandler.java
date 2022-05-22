package com.sammy.fufo.core.handlers;

import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;

public class ProgressionHandler {
    public ArrayList<String> unlockedProgress;
    public ArrayList<String> unlockedRunes;
    public ProgressionHandler(){
        unlockedProgress = new ArrayList<>();
        unlockedRunes = new ArrayList<>();
    }
    public void serializeNBT(CompoundTag tag){
        CompoundTag runeTag = new CompoundTag();
        CompoundTag progressTag = new CompoundTag();
        CompoundTag unlocked = new CompoundTag();

        for(String prog: unlockedProgress){
            progressTag.putBoolean(prog,true);
        }
        for (String rune: unlockedRunes){
            progressTag.putBoolean(rune,true);
        }

        tag.put("unlocked",unlocked);
        unlocked.put("runes",runeTag);
        unlocked.put("progress",progressTag);
    }
    public void deserializeNBT(CompoundTag tag){
        CompoundTag unlocked = tag.getCompound("unlocked");
        CompoundTag progressTag = unlocked.getCompound("progress");
        CompoundTag runeTag = unlocked.getCompound("runes");

        unlockedProgress.addAll(progressTag.getAllKeys());
        unlockedRunes.addAll(runeTag.getAllKeys());
    }
}
