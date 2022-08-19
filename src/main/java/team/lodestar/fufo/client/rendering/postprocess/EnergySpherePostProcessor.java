package team.lodestar.fufo.client.rendering.postprocess;

import com.mojang.blaze3d.vertex.PoseStack;
import team.lodestar.fufo.FufoMod;
import team.lodestar.lodestone.systems.postprocess.MultiInstancePostProcessor;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.resources.ResourceLocation;

public class EnergySpherePostProcessor extends MultiInstancePostProcessor<EnergySphereFx> {
    private EffectInstance effectEnergySphere;

    @Override
    public ResourceLocation getPostChainLocation() {
        return FufoMod.fufoPath("energy_sphere");
    }

    @Override
    protected int getMaxInstances() {
        return 16;
    }

    @Override
    protected int getDataSizePerInstance() {
        return 8;
    }

    @Override
    public void init() {
        super.init();

        if (postChain != null)
            effectEnergySphere = effects[0];
    }

    @Override
    public void beforeProcess(PoseStack viewModelStack) {
        super.beforeProcess(viewModelStack);

        setDataBufferUniform(effectEnergySphere, "Data", "instanceCount");
    }

    @Override
    public void afterProcess() {

    }

    
}
