package monster;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Gold;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;
import object.OBJ_Rock;

import java.util.Random;

public class MON_SQUID extends Entity {
    GamePanel gp;
    public MON_SQUID(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_monster;
        name = "Squid";
        speed = 1;
        maxLife = 4;
        life = maxLife;
        attack = 5;
        defense = 0;
        exp = 2;
        projectile = new OBJ_Rock(gp);


        solidArea.x=3;
        solidArea.y=18;
        solidArea.width=42;
        solidArea.height=30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        getImage();


    }
    public void getImage(){

        up1 = setup("/monster/monsterdown",gp.tileSize,gp.tileSize);
        up2 = setup("/monster/monsterdown2",gp.tileSize,gp.tileSize);
        down1 = setup("/monster/monsterdown",gp.tileSize,gp.tileSize);
        down2 = setup("/monster/monsterdown2",gp.tileSize,gp.tileSize);
        left1 = setup("/monster/monsterdown",gp.tileSize,gp.tileSize);
        left2 = setup("/monster/monsterdown2",gp.tileSize,gp.tileSize);
        right1 = setup("/monster/monsterdown",gp.tileSize,gp.tileSize);
        right2 = setup("/monster/monsterdown2",gp.tileSize,gp.tileSize);
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

        int i = new Random().nextInt(100)+1;
        if(i > 99 && projectile.alive==false && shotAvailableCounter==30){
            projectile.set(worldX,worldY,direction,true,this);
            gp.projectileList.add(projectile);
            shotAvailableCounter = 0;
        }
    }
    public void damageReaction(){

        actionLockCounter = 0;
        direction = gp.player.direction;
    }
    public void checkDrop(){

        //cast a die
        int i = new Random().nextInt(100)+1;

        //set the monster's drop
        if(i < 50){
            dropItem(new OBJ_Gold(gp));
        }
        if(i >= 50 && i < 75){
            dropItem(new OBJ_Heart(gp));
            dropItem(new OBJ_Gold(gp));
        }
        if(i >= 75 && i < 100){
            dropItem(new OBJ_ManaCrystal(gp));
            dropItem(new OBJ_Gold(gp));
            dropItem(new OBJ_Gold(gp));
        }
    }
}
