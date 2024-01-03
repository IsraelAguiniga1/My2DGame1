package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Potion_Red extends Entity {

    GamePanel gp;


    public OBJ_Potion_Red(GamePanel gp) {
        super(gp);

        this.gp = gp;


        type = type_consumable;
        name = "Red Potion";
        down1 = setup("/objects/potion_red",gp.tileSize,gp.tileSize);
        value = 5;
        description = "[" + name + "]\n" +
                "A red potion ." + "\n" + "HP: " + value;
    }
    public void use(Entity entity){

        gp.gameState = gp.dialogueState;
        gp.ui.currentDialogue = "You used a Red Potion . HP + " + value + ".";
        entity.life+=value; //heal

        gp.playSE(2);

    }
}
