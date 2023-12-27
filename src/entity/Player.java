package entity;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity{


    KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    int standCounter = 0;


    public Player(GamePanel gp, KeyHandler keyH) {//player constructor
        super(gp);


        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle(screenX, screenY, gp.tileSize, gp.tileSize); //set player collision area
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues(){//set player default values and position
        worldX = gp.tileSize *23;
        worldY = gp.tileSize *21;
        speed = 4;
        direction = "down";

        //CHARACTER STATS
        maxLife = 6;
        life = maxLife;
    }
    public void getPlayerImage(){//get player image

        up1 = setup("/player/playerwalkingup");
        up2 = setup("/player/playerwalkingup2");
        down1 = setup("/player/playerwalkingdown");
        down2 = setup("/player/playerwalkingdown2");
        left1 = setup("/player/playerwalkingleft");
        left2 = setup("/player/playerwalkingleft2");
        right1 = setup("/player/playerwalkingright");
        right2 = setup("/player/playerwalkingright2");
    }


    public void update() {//update player position

        if (keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true) {//player is standing still if no key is pressed


            if (keyH.upPressed) {
                direction = "up";
            } else if (keyH.downPressed) {
                direction = "down";
            } else if (keyH.leftPressed) {
                direction = "left";
            } else if (keyH.rightPressed) {
                direction = "right";
            }


            // check for collision
            collisionOn = false;
            gp.cChecker.checkTile(this);

            // check for object collision
           int objIndex = gp.cChecker.checkObject(this, true);
           pickUpObject(objIndex);

           //check npc collision
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            //CHECK EVENT
            gp.eHandler.checkEvent();

            gp.keyH.enterPressed = false;

            // if collison is false player can move
            if (collisionOn == false){

                switch (direction){
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }


            spriteCounter++;
            if (spriteCounter > 12) {//change sprite every 10 frames
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;
                }

                spriteCounter = 0;
            }
        }
    }
    public void pickUpObject(int i){//pick up object

        if (i != 999) {


        }
    }
    public void interactNPC(int i){//interact with npc

        if (i != 999) {
            if (gp.keyH.enterPressed == true){
                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
            }
            }

        }

    public void draw(Graphics2D g2){//draw player
       // g2.setColor(Color.WHITE);

      //  g2.fillRect(x,y,gp.tileSize,gp.tileSize);

            BufferedImage image = null;


        switch (direction){
                case "up":
                    if (spriteNum == 1){
                        image = up1;
                    }
                    if (spriteNum == 2){
                        image = up2;
                    }
                    break;
                case "down":
                    if (spriteNum == 1){
                        image = down1;
                    }
                    if (spriteNum == 2){
                        image = down2;
                    }
                    break;
                case "left":
                    if (spriteNum == 1){
                        image = left1;
                    }
                    if (spriteNum == 2){
                        image = left2;
                    }
                    break;
                case "right":
                    if (spriteNum == 1){
                        image = right1;
                    }
                    if (spriteNum == 2){
                        image = right2;
                    }
                    break;
            }
            int x = screenX;
            int y = screenY;

            if (screenX> worldX){
                x = worldX;
            }
            if (screenY> worldY){
                y = worldY;
            }
        int rightOffset = gp.screenWidth - screenX;
        if (rightOffset > gp.worldWidth - worldX){
            x = gp.screenWidth - (gp.worldWidth - worldX);
        }
        int bottomOffset = gp.screenHeight - screenY;
        if (bottomOffset > gp.worldHeight - worldY){
            y = gp.screenHeight - (gp.worldHeight - worldY);
        }


        g2.drawImage(image,x,y,null);//draw player image

        }
    }






