package team.lodestar.fufo.registry.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import team.lodestar.fufo.FufoMod;
import team.lodestar.lodestone.systems.rendering.ExtendedShaderInstance;
import team.lodestar.lodestone.systems.rendering.ShaderHolder;

import java.io.IOException;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = FufoMod.FUFO, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FufoShaders {

	public static ShaderHolder TOGGLED_SPELL_SHINE = new ShaderHolder("Speed", "ShineCount");

	@SubscribeEvent
	public static void shaderRegistry(RegisterShadersEvent event) throws IOException {
		registerShader(event, ExtendedShaderInstance.createShaderInstance(TOGGLED_SPELL_SHINE, event.getResourceManager(), FufoMod.fufoPath("toggled_spell_shine"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP));
	}

	public static void registerShader(RegisterShadersEvent event, ExtendedShaderInstance extendedShaderInstance) {
		event.registerShader(extendedShaderInstance, s -> ((ExtendedShaderInstance) s).getHolder().setInstance(((ExtendedShaderInstance) s)));
	}
}