package monster;

import entity.Entity;
import main.GamePanel;

import java.util.Random;

public class MON_SQUID extends Entity {
    public MON_SQUID(GamePanel gp) {
        super(gp);
        name = "Squid";
        speed = 1;
        maxLife = 2;
        life = maxLife;

        solidArea.x=3;
        solidArea.y=10;
        solidArea.width=42;
        solidArea.height=30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        getImage();


    }
    public void getImage(){

        up1 = setup("/monster/monsterdown");
        up2 = setup("/monster/monsterdown2");
        down1 = setup("/monster/monsterdown");
        down2 = setup("/monster/monsterdown2");
        left1 = setup("/monster/monsterdown");
        left2 = setup("/monster/monsterdown2");
        right1 = setup("/monster/monsterdown");
        right2 = setup("/monster/monsterdown2");
    }
    public void setAction(){

        actionLockCounter++;

        if (actionLockCounter == 120) {


            Random random = new Random();
            int i = random.nextInt(100) + 1;//random number between 1 and 100

            if (i <= 25) {
                direction = "up";
            }
            if (i > 25 && i <= 50) {
                direction = "down";
            }
            if (i > 50 && i <= 75) {
                direction = "left";
            }
            if (i > 75 && i <= 100) {
                direction = "right";
            }
            actionLockCounter = 0;
        }
    }
}
