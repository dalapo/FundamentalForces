package team.lodestar.fufo.core.magic.spell;

import team.lodestar.fufo.FufoMod;

public class EmptySpellType extends SpellType {
    public EmptySpellType() {
        super(FufoMod.fufoPath("empty"), null, null, null, (t) -> SpellInstance.EMPTY);
    }
}
