package object;

import entity.Entity;
import entity.StatusEffect;
import main.GamePanel;

/**
 * Blue potion that increases defense temporarily
 */
public class OBJ_Potion_Blue extends Entity {
    private final boolean stackable;
    GamePanel gp;

    public OBJ_Potion_Blue(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_consumable;
        name = "Blue Potion";
        down1 = setup("/objects/potion_red", gp.tileSize, gp.tileSize);
        description = "[" + name + "]\nIncreases defense\nfor 60 seconds.";
        price = 35;
        stackable = true;

        // Effect stats
        value = 2; // +2 defense
    }

    @Override
    public void use(Entity entity) {
        gp.gameState = gp.dialogueState;
        gp.ui.currentDialogue = "You drink the " + name + ".\nDefense increased by " + value + "!";

        // Apply defense buff status effect
        entity.statusEffects.addEffect(
                StatusEffect.STATUS_DEFENSE,
                3600, // 60 seconds at 60 FPS
                value  // +2 defense
        );

        gp.playSE(2);
    }
}

