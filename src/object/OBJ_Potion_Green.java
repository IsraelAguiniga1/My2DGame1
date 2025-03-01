package object;

import entity.Entity;
import entity.StatusEffect;
import main.GamePanel; /**
 * Green potion that increases speed temporarily
 */
public class OBJ_Potion_Green extends Entity {
    private final boolean stackable;
    GamePanel gp;

    public OBJ_Potion_Green(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_consumable;
        name = "Green Potion";
        down1 = setup("/objects/potion_red", gp.tileSize, gp.tileSize);
        description = "[" + name + "]\nIncreases speed\nfor 30 seconds.";
        price = 40;
        stackable = true;

        // Effect stats
        value = 25; // 25% speed increase
    }

    @Override
    public void use(Entity entity) {
        gp.gameState = gp.dialogueState;
        gp.ui.currentDialogue = "You drink the " + name + ".\nSpeed increased by " + value + "%!";

        // Apply speed buff status effect
        entity.statusEffects.addEffect(
                StatusEffect.STATUS_SPEED,
                1800, // 30 seconds at 60 FPS
                value  // 25% speed increase
        );

        gp.playSE(2);
    }
}
