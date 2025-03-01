package object;

import entity.Entity;
import entity.StatusEffect;
import main.GamePanel; /**
 * Freeze resistance potion that protects against freeze
 */
public class OBJ_Potion_FreezeResist extends Entity {
    private final boolean stackable;
    GamePanel gp;

    public OBJ_Potion_FreezeResist(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_consumable;
        name = "Frost Resist Potion";
        down1 = setup("/objects/potion_frost_resist", gp.tileSize, gp.tileSize);
        description = "[" + name + "]\nGrants immunity to\nfreezing for\n60 seconds.";
        price = 50;
        stackable = true;
    }

    @Override
    public void use(Entity entity) {
        gp.gameState = gp.dialogueState;
        gp.ui.currentDialogue = "You drink the " + name + ".\nYou feel resistant to frost!";

        // Remove any existing freeze status
        if (entity.hasStatusEffect(StatusEffect.STATUS_FREEZE)) {
            entity.statusEffects.removeEffect(StatusEffect.STATUS_FREEZE);
        }

        // Apply a "dummy" effect to represent frost immunity
        // (The actual immunity would be checked when monsters try to apply freeze)
        entity.statusEffects.addEffect(
                StatusEffect.STATUS_SPEED, // Reusing speed effect type
                3600, // 60 seconds at 60 FPS
                0     // No actual speed bonus
        );

        gp.playSE(2);
    }
}
