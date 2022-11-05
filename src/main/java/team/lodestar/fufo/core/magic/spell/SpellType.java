package team.lodestar.fufo.core.magic.spell;

import team.lodestar.fufo.FufoMod;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.fufo.common.magic.spell.effects.ToggledEffect;
import team.lodestar.fufo.core.magic.MagicElementType;
import team.lodestar.fufo.registry.common.magic.FufoMagicElements;
import team.lodestar.fufo.registry.common.magic.FufoSpellCastModes;
import team.lodestar.fufo.registry.common.magic.FufoSpellDataKeys;

import java.util.Arrays;
import java.util.function.Function;

public class SpellType {
    public final ResourceLocation id;
    public final SpellEffect effect;
    public final MagicElementType element;
    public final SpellCastMode defaultCastMode;
    public final Function<SpellType, SpellInstance> defaultInstanceSupplier;
    public final FufoSpellDataKeys.SpellAttributeMap<SpellAttribute> spellAttributes;

    protected SpellType(ResourceLocation id, SpellEffect effect, MagicElementType element, SpellCastMode defaultCastMode, Function<SpellType, SpellInstance> defaultInstanceSupplier) {
        this(id, effect, element, defaultCastMode, defaultInstanceSupplier, new FufoSpellDataKeys.SpellAttributeMap<>());
    }
    protected SpellType(ResourceLocation id, SpellEffect effect, MagicElementType element, SpellCastMode defaultCastMode, Function<SpellType, SpellInstance> defaultInstanceSupplier, FufoSpellDataKeys.SpellAttributeMap<SpellAttribute> spellAttributes) {
        this.id = id;
        this.effect = effect;
        this.element = element;
        this.defaultCastMode = defaultCastMode;
        this.defaultInstanceSupplier = defaultInstanceSupplier;
        this.spellAttributes = spellAttributes;
    }

    public ResourceLocation getIconLocation(SpellInstance instance) {
        return FufoMod.fufoPath("textures/spell/" + element.id.getPath() + "/" + id.getPath() + ".png");
    }

    public ResourceLocation getBackgroundLocation(SpellInstance instance) {
        if (isToggledEffectActive(instance)) {
            return FufoMod.fufoPath("textures/spell/" + element.id.getPath() + "/" + id.getPath() + "_background_active.png");
        }
        return FufoMod.fufoPath("textures/spell/" + element.id.getPath() + "/" + id.getPath() + "_background.png");
    }

    public boolean isToggledEffectActive(SpellInstance instance) {
        return instance.extraData.getBoolean(ToggledEffect.NBT);
    }

    public static SpellTypeBuilder createSpellType(ResourceLocation id, SpellEffect spellEffect) {
        return new SpellTypeBuilder(id, spellEffect);
    }

    public static class SpellTypeBuilder {
        private final ResourceLocation id;
        private final SpellEffect spellEffect;
        private MagicElementType element = FufoMagicElements.FORCE;
        public SpellCastMode defaultCastMode = FufoSpellCastModes.INSTANT;
        private Function<SpellType, SpellInstance> defaultInstanceSupplier = SpellInstance::new;
        public final FufoSpellDataKeys.SpellAttributeMap<SpellAttribute> spellAttributes = new FufoSpellDataKeys.SpellAttributeMap<>();

        public SpellTypeBuilder(ResourceLocation id, SpellEffect spellEffect) {
            this.id = id;
            this.spellEffect = spellEffect;
        }

        public SpellTypeBuilder setDefaultInstance(Function<SpellType, SpellInstance> defaultInstanceSupplier) {
            this.defaultInstanceSupplier = defaultInstanceSupplier;
            return this;
        }

        public SpellTypeBuilder setSpellElement(MagicElementType element) {
            this.element = element;
            return this;
        }

        public SpellTypeBuilder setCastMode(SpellCastMode castMode) {
            this.defaultCastMode = castMode;
            return this;
        }

        public SpellTypeBuilder addAttributes(SpellAttribute... attributes) {
            Arrays.stream(attributes).forEach(a -> FufoSpellDataKeys.DATA_KEYS.get(a.id).putAttribute(spellAttributes, a));
            return this;
        }

        public SpellType build() {
            return new SpellType(id, spellEffect, element, defaultCastMode, defaultInstanceSupplier, spellAttributes);
        }
    }
}