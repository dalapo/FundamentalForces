package com.project_esoterica.esoterica.core.systems.screenshake;

import com.project_esoterica.esoterica.core.config.ClientConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.util.Mth;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Random;

public class ScreenshakeHandler {

    public static final ArrayList<ScreenshakeInstance> INSTANCES = new ArrayList<>();
    public static float intensity;
    public static float yawOffset;
    public static float pitchOffset;

    public static void cameraTick(Camera camera, Random random) {
        yawOffset = randomizeOffset(random);
        pitchOffset = randomizeOffset(random);
        camera.setRotation(camera.getYRot() + yawOffset, camera.getXRot() + pitchOffset);
    }

    public static void clientTick(Camera camera, Random random) {
        float newIntensity = (float) INSTANCES.stream().mapToDouble(i -> i.tick(camera, random)).sum();
        for (ScreenshakeInstance instance : INSTANCES)
        {
            newIntensity+=instance.tick(camera, random);
        }
        intensity = newIntensity;
        INSTANCES.removeIf(i -> i.intensity <= 0.01f);
    }

    public static void addScreenshake(ScreenshakeInstance instance) {
        INSTANCES.add(instance);
    }

    public static float randomizeOffset(Random random) {
        return Mth.nextFloat(random, -intensity * 2, intensity * 2);
    }
}
