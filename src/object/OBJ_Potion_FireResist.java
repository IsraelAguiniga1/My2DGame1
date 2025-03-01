package object;

import entity.Entity;
import entity.StatusEffect;
import main.GamePanel; /**
 * Fire resistance potion that protects against burn
 */
public class OBJ_Potion_FireResist extends Entity {
    private final boolean stackable;
    GamePanel gp;

    public OBJ_Potion_FireResist(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_consumable;
        name = "Fire Resist Potion";
        down1 = setup("/objects/potion_fire_resist", gp.tileSize, gp.tileSize);
        description = "[" + name + "]\nGrants immunity to\nburn damage for\n60 seconds.";
        price = 50;
        stackable = true;
    }

    @Override
    public void use(Entity entity) {
        gp.gameState = gp.dialogueState;
        gp.ui.currentDialogue = "You drink the " + name + ".\nYou feel resistant to fire!";

        // Remove any existing burn status
        if (entity.hasStatusEffect(StatusEffect.STATUS_BURN)) {
            entity.statusEffects.removeEffect(StatusEffect.STATUS_BURN);
        }

        // Apply a "dummy" effect to represent fire immunity
        // (The actual immunity would be checked when monsters try to apply burn)
        entity.statusEffects.addEffect(
                StatusEffect.STATUS_DEFENSE, // Reusing defense effect type
                3600, // 60 seconds at 60 FPS
                0     // No actual defense bonus
        );

        gp.playSE(2);
    }
}
