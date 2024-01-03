package entity;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;
import object.OBJ_Fireball;
import object.OBJ_Key;
import object.OBJ_Shield_Wood;
import object.OBJ_Sword_Normal;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageFilter;
import java.io.IOException;
import java.util.ArrayList;

public class Player extends Entity {


    KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    int standCounter = 0;
    public boolean attackCancel = false;
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 20;


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
        getPlayerAttackImage();
        setItems();
    }

    public void setDefaultValues() {//set player default values and position
        worldX = gp.tileSize * 22;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "down";

        //CHARACTER STATS
        level = 1;
        maxLife = 6;
        life = maxLife;
        maxMana = 4;
        mana = maxMana;
        ammo = 10;
        strength = 1; //the higher the strength, the higher the attack
        dexterity = 1; //the higher the dexterity, the less damage taken
        exp = 0;
        nextLevelExp = 5;
        coin = 0;
        currentWeapon = new OBJ_Sword_Normal(gp);
        currentShield = new OBJ_Shield_Wood(gp);
        projectile = new OBJ_Fireball(gp);
        attack= getAttack(); //total attack value is determined by strength and weapon
        defense= getDefense(); //total defense value is determined by dexterity and shield

    }
    public void setItems(){
        inventory.add(currentWeapon);
        inventory.add(currentShield);
        inventory.add(new OBJ_Key(gp));

    }
    public int getAttack(){
        attackArea = currentWeapon.attackArea;
        return attack = strength * currentWeapon.attackValue;
    }
    public int getDefense(){
        return defense = dexterity * currentShield.defenseValue;
    }

    public void getPlayerImage() {//get player image

        up1 = setup("/player/playerwalkingup1", gp.tileSize, gp.tileSize);
        up2 = setup("/player/playerwalkingup2", gp.tileSize, gp.tileSize);
        down1 = setup("/player/mcwalking1", gp.tileSize, gp.tileSize);
        down2 = setup("/player/mcwalking2", gp.tileSize, gp.tileSize);
        down3 = setup("/player/mcwalking3", gp.tileSize, gp.tileSize);
        down4 = setup("/player/mcwalking4", gp.tileSize, gp.tileSize);
        left1 = setup("/player/playerwalkingleft", gp.tileSize, gp.tileSize);
        left2 = setup("/player/playerwalkingleft2", gp.tileSize, gp.tileSize);
        right1 = setup("/player/playerwalkingright", gp.tileSize, gp.tileSize);
        right2 = setup("/player/playerwalkingright2", gp.tileSize, gp.tileSize);
    }

    public void getPlayerAttackImage() {

        if (currentWeapon.type == type_sword) {
            attackUp1 = setup("/player/boy_attack_up_1", gp.tileSize, gp.tileSize * 2);
            attackUp2 = setup("/player/boy_attack_up_2", gp.tileSize, gp.tileSize * 2);

            attackDown1 = setup("/player/boy_attack_down_1", gp.tileSize, gp.tileSize * 2);
            attackDown2 = setup("/player/boy_attack_down_2", gp.tileSize, gp.tileSize * 2);
            attackLeft1 = setup("/player/boy_attack_left_1", gp.tileSize * 2, gp.tileSize);
            attackLeft2 = setup("/player/boy_attack_left_2", gp.tileSize * 2, gp.tileSize);
            attackRight1 = setup("/player/boy_attack_right_1", gp.tileSize * 2, gp.tileSize);
            attackRight2 = setup("/player/boy_attack_right_2", gp.tileSize * 2, gp.tileSize);
        }
        if (currentWeapon.type == type_axe) {
            attackUp1 = setup("/player/boy_axe_up_1", gp.tileSize, gp.tileSize * 2);
            attackUp2 = setup("/player/boy_axe_up_2", gp.tileSize, gp.tileSize * 2);
            attackDown1 = setup("/player/boy_axe_down_1", gp.tileSize, gp.tileSize * 2);
            attackDown2 = setup("/player/boy_axe_down_2", gp.tileSize, gp.tileSize * 2);
            attackLeft1 = setup("/player/boy_axe_left_1", gp.tileSize*2, gp.tileSize);
            attackLeft2 = setup("/player/boy_axe_left_2", gp.tileSize*2, gp.tileSize);
            attackRight1 = setup("/player/boy_axe_right_1", gp.tileSize*2, gp.tileSize);
            attackRight2 = setup("/player/boy_axe_right_2", gp.tileSize*2, gp.tileSize);
        }

    }


    public void update() {//update player position

        if (attacking == true) {
            attacking();
        }

       else if (keyH.upPressed == true || keyH.downPressed == true ||
                keyH.leftPressed == true || keyH.rightPressed == true || keyH.enterPressed == true) {//player is standing still if no key is pressed


            if (keyH.upPressed) {
                direction = "up";
            } else if (keyH.downPressed) {
                direction = "down";
            } else if (keyH.leftPressed) {
                direction = "left";
            } else if (keyH.rightPressed) {
                direction = "right";
            }


            // check for player collision
            collisionOn = false;
            gp.cChecker.checkTile(this);

            // check for object collision
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            //check npc collision
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            //check monster collision
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);

            //CHECK EVENT
            gp.eHandler.checkEvent();


            // if collison is false player can move
            if (collisionOn == false && keyH.enterPressed == false) {

                switch (direction) {
                    case "up":
                        worldY -= speed;
                        break;
                    case "down":
                        worldY += speed;
                        break;
                    case "left":
                        worldX -= speed;
                        break;
                    case "right":
                        worldX += speed;
                        break;
                }
            }
            if (keyH.enterPressed == true && attackCancel == false) {
                gp.playSE(7);
                attacking = true;
                spriteCounter = 0;
            }
            attackCancel = false;

            gp.keyH.enterPressed = false;

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
       if (keyH.shotKeyPressed== true&& projectile.alive == false && shotAvailableCounter == 30 &&
               projectile.haveResource(this) == true){

            //set default projectile position
           projectile.set(worldX,worldY,direction,true,this);

           //subtract resource(MANA or AMMO)
              projectile.subtractResource(this);

           //add it to the arraylist
              gp.projectileList.add(projectile);

              shotAvailableCounter = 0;

              gp.playSE(10);

       }
        //INVINCIBLE COUNTER
        if (invincible == true) {
            invincibleCounter++;
            if (invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
        if (shotAvailableCounter < 30){
            shotAvailableCounter++;
        }
        if (life > maxLife) {
            life = maxLife;
        }
        if (mana > maxMana) {
            mana = maxMana;
        }
    }

    public void attacking() {

        spriteCounter++;

        if (spriteCounter <= 5) {//change sprite every 10 frames
            spriteNum = 1;
        }
        if (spriteCounter > 5 && spriteCounter <= 25) {//change sprite every 10 frames
            spriteNum = 2;
            //save the current world position
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            //adjust player worldx/y for attack area
            switch (direction) {
                case "up":
                    worldY -= attackArea.height;
                    break;
                case "down":
                    worldY += attackArea.height;
                    break;
                case "left":
                    worldX -= attackArea.width;
                    break;
                case "right":
                    worldX += attackArea.width;
                    break;
            }
            //attack Area becomes solid area
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;
            //check monster collision with the updated solid area and world area
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            damageMonster(monsterIndex,attack);

            //after checking collision, reset the player world position and solid area
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;


        }
        if (spriteCounter > 25) {//change sprite every 10 frames
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }

    public void pickUpObject(int i) {//pick up object

        if (i != 999) {

            //pickup only items
            if (gp.obj[i].type == type_pickupOnly) {

                gp.obj[i].use(this);
                gp.obj[i] = null;
            }

            //inventory items
            else {
                String text;

                if (inventory.size() != maxInventorySize){

                    inventory.add(gp.obj[i]);
                    gp.playSE(1);
                    text = "You picked up " + gp.obj[i].name + "!";
                }
                else{
                    text = "Your inventory is full!";
                }
                gp.ui.addMessage(text);
                gp.obj[i]= null;
            }


        }
    }

    public void interactNPC(int i) {//interact with npc

        if (gp.keyH.enterPressed == true) {
            if (i != 999) {
                attackCancel = true;
                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
            }


        }
    }



    public void contactMonster ( int i){

            if (i != 999) {
                if (invincible == false && gp.monster[i].dying == false) {
                    gp.playSE(6);

                    int damage = gp.monster[i].attack - defense;
                    if (damage < 0) {
                        damage = 0;
                    }

                    life -= damage;
                    invincible = true;
                }

            }
        }
        public void damageMonster ( int i,int attack){

            if (i != 999) {

                if (gp.monster[i].invincible == false) {

                    gp.playSE(5);

                    int damage = attack - gp.monster[i].defense;
                    if (damage < 0) {
                        damage = 0;
                    }

                    gp.monster[i].life -= damage;
                    gp.ui.addMessage(damage + " damage!");

                    gp.monster[i].invincible = true;
                    gp.monster[i].damageReaction();

                    if (gp.monster[i].life <= 0) {
                        gp.monster[i].dying = true;
                        gp.ui.addMessage(gp.monster[i].name + " is defeated!");
                        gp.ui.addMessage("You gained " + gp.monster[i].exp + " exp!");
                        exp += gp.monster[i].exp;
                        checkLevelUp();
                    }
                }
            }
        }
        public void checkLevelUp(){
            if (exp >= nextLevelExp){
                level++;
                nextLevelExp = nextLevelExp * 2;
                maxLife += 2;
                strength++;
                dexterity++;
                attack = getAttack();
                defense = getDefense();

                gp.playSE(8);
                gp.gameState = gp.dialogueState;
                gp.ui.currentDialogue = "Level up! You are now level " + level + "!\n " +
                        "You Feel stronger!";
            }
        }
        public void selectItem(){

            int itemIndex = gp.ui.getItemIndexOnSlot();

            if (itemIndex < inventory.size()){

                Entity selectedItem = inventory.get(itemIndex);

                if(selectedItem.type == type_sword || selectedItem.type == type_axe){
                    currentWeapon = selectedItem;
                    attack = getAttack();
                    getPlayerAttackImage();


                }
                if(selectedItem.type == type_shield){
                    currentShield = selectedItem;
                    defense = getDefense();

                }
                if (selectedItem.type == type_consumable){

                    selectedItem.use(this);
                    inventory.remove(itemIndex);
                }
            }
        }

        public void draw (Graphics2D g2){//draw player


            BufferedImage image = null;
            int tempScreenX = screenX;
            int tempScreenY = screenY;


            switch (direction) {
                case "up":
                    if (attacking == false) {
                        if (spriteNum == 1) {
                            image = up1;
                        }
                        if (spriteNum == 2) {
                            image = up2;
                        }
                    }
                    if (attacking == true) {
                        tempScreenY = screenY - gp.tileSize;
                        if (spriteNum == 1) {
                            image = attackUp1;
                        }
                        if (spriteNum == 2) {
                            image = attackUp2;
                        }
                    }
                    break;
                case "down":
                    if (attacking == false) {
                        if (spriteNum == 1) {
                            image = down1;
                        }
                        if (spriteNum == 2) {
                            image = down2;
                        }
                        if (spriteNum == 3) {
                            image = down3;
                        }
                        if (spriteNum == 4) {
                            image = down4;
                        }
                    }
                    if (attacking == true) {
                        if (spriteNum == 1) {
                            image = attackDown1;
                        }
                        if (spriteNum == 2) {
                            image = attackDown2;
                        }
                    }
                    break;
                case "left":
                    if (attacking == false) {
                        if (spriteNum == 1) {
                            image = left1;
                        }
                        if (spriteNum == 2) {
                            image = left2;
                        }
                    }
                    if (attacking == true) {
                        tempScreenX = screenX - gp.tileSize;
                        if (spriteNum == 1) {
                            image = attackLeft1;
                        }
                        if (spriteNum == 2) {
                            image = attackLeft2;
                        }
                    }
                    break;
                case "right":
                    if (attacking == false) {
                        if (spriteNum == 1) {
                            image = right1;
                        }
                        if (spriteNum == 2) {
                            image = right2;
                        }
                    }
                    if (attacking == true) {
                        if (spriteNum == 1) {
                            image = attackRight1;
                        }
                        if (spriteNum == 2) {
                            image = attackRight2;
                        }
                    }
                    break;
            }
            int x = screenX;
            int y = screenY;

            if (screenX > worldX) {
                x = worldX;
            }
            if (screenY > worldY) {
                y = worldY;
            }
            int rightOffset = gp.screenWidth - screenX;
            if (rightOffset > gp.worldWidth - worldX) {
                x = gp.screenWidth - (gp.worldWidth - worldX);
            }
            int bottomOffset = gp.screenHeight - screenY;
            if (bottomOffset > gp.worldHeight - worldY) {
                y = gp.screenHeight - (gp.worldHeight - worldY);
            }
            //become transparent when invincible
            if (invincible == true) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));

            }
            g2.drawImage(image, tempScreenX, tempScreenY, null);//draw player image

            //reset alpha
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));



        }




}








