package object;

import entity.Entity;
import entity.StatusEffect;
import main.GamePanel; /**
 * Antidote potion that cures poison
 */
public class OBJ_Potion_Antidote extends Entity {
    private final boolean stackable;
    GamePanel gp;

    public OBJ_Potion_Antidote(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_consumable;
        name = "Antidote";
        down1 = setup("/objects/potion_antidote", gp.tileSize, gp.tileSize);
        description = "[" + name + "]\nCures poison.";
        price = 30;
        stackable = true;
    }

    @Override
    public void use(Entity entity) {
        gp.gameState = gp.dialogueState;

        if (entity.hasStatusEffect(StatusEffect.STATUS_POISON)) {
            gp.ui.currentDialogue = "You drink the " + name + ".\nThe poison is cured!";
            entity.statusEffects.removeEffect(StatusEffect.STATUS_POISON);
        } else {
            gp.ui.currentDialogue = "You drink the " + name + ".\nNothing happens.";
        }

        gp.playSE(2);
    }
}
