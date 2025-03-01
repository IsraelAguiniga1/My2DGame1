package object;

import entity.Entity;
import entity.StatusEffect;
import main.GamePanel; /**
 * Yellow potion that increases strength temporarily
 */
public class OBJ_Potion_Yellow extends Entity {
    private final boolean stackable;
    GamePanel gp;

    public OBJ_Potion_Yellow(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_consumable;
        name = "Yellow Potion";
        down1 = setup("/objects/potion_yellow", gp.tileSize, gp.tileSize);
        description = "[" + name + "]\nIncreases strength\nfor 45 seconds.";
        price = 45;
        stackable = true;

        // Effect stats
        value = 2; // +2 strength
    }

    @Override
    public void use(Entity entity) {
        gp.gameState = gp.dialogueState;
        gp.ui.currentDialogue = "You drink the " + name + ".\nStrength increased by " + value + "!";

        // Apply strength buff status effect
        entity.statusEffects.addEffect(
                StatusEffect.STATUS_STRENGTH,
                2700, // 45 seconds at 60 FPS
                value  // +2 strength
        );

        gp.playSE(2);
    }
}
