package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Gold extends Entity {

    GamePanel gp;
    public OBJ_Gold(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_pickupOnly;
        name = "Gold";
        value = 1;
        down1 = setup("/objects/coin_bronze",gp.tileSize,gp.tileSize);




    }
    public void use(Entity entity){

        gp.playSE(1);
        gp.ui.addMessage("You picked up " + value + " gold.");
        gp.player.coin+=value;


    }
}
