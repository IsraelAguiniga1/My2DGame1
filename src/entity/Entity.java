package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Entity {
    GamePanel gp;
    public BufferedImage up1, up2, down1, down2, down3, down4, left1, left2, right1, right2;
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2,
            attackLeft1, attackLeft2, attackRight1, attackRight2;
    public BufferedImage image, image2, image3;
    public Rectangle solidArea = new Rectangle(0,0,48,48);
    public Rectangle attackArea = new Rectangle(0,0,0,0);
    public int solidAreaDefaultX, solidAreaDefaultY;

    public boolean collision = false;
    String dialogues[] = new String[20];

    // STATE
    public int worldX, worldY;
    public String direction = "down";
    public int spriteNum = 1;
    int dialogueIndex = 0;
    public boolean collisionOn = false;
    public boolean invincible = false;
    boolean attacking = false;
    public boolean alive = true;
    public boolean dying = false;
    public boolean hpBarOn = false;

    // COUNTER
    public int spriteCounter = 0;
    public int actionLockCounter = 0;
    public int invincibleCounter = 0;
    public int shotAvailableCounter = 0;
    int dyingCounter = 0;
    public int hpBarCounter = 0;

    // CHARACTER ATTRIBUTES
    public String name;
    public int defaultSpeed; // Added for status effects
    public int speed;
    public int maxLife;
    public int life;
    public int maxMana;
    public int mana;
    public int ammo;
    public int level;
    public int strength;
    public int dexterity;
    public int attack;
    public int defense;
    public int exp;
    public int nextLevelExp;
    public int coin;
    public Entity currentWeapon;
    public Entity currentShield;
    public Projectile projectile;

    // Status effect system
    public StatusEffectManager statusEffects;

    // ITEM ATTRIBUTES
    public int value;
    public int attackValue;
    public int defenseValue;
    public String description = "";
    public int useCost;

    // SHOP
    public int price;

    // TYPE
    public int type;
    public final int type_player = 0;
    public final int type_npc = 1;
    public final int type_monster = 2;
    public final int type_sword = 3;
    public final int type_axe = 4;
    public final int type_shield = 5;
    public final int type_consumable = 6;
    public final int type_pickupOnly = 7;
    public final int type_obstacle = 8;
    public final int type_light = 9;

    public Entity(GamePanel gp) {
        this.gp = gp;
        statusEffects = new StatusEffectManager(gp, this);
    }

    public void setAction() {
        // Override in subclasses
    }

    public void damageReaction() {
        // Override in subclasses
    }

    public void speak() {
        if (dialogues[dialogueIndex] == null) {
            dialogueIndex = 0;
        }
        gp.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;

        switch (gp.player.direction) {
            case "up":
                direction = "down";
                break;
            case "down":
                direction = "up";
                break;
            case "left":
                direction = "right";
                break;
            case "right":
                direction = "left";
                break;
        }
    }

    public void use(Entity entity) {
        // Override in subclasses
    }

    public void checkDrop() {
        // Override in subclasses
    }

    public void dropItem(Entity droppedItem) {
        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] == null) {
                gp.obj[i] = droppedItem;
                gp.obj[i].worldX = worldX; // the dead monster's position
                gp.obj[i].worldY = worldY;
                break;
            }
        }
    }

    public void update() {
        // Update status effects first
        statusEffects.update();

        setAction();

        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.monster);
        boolean contactPlayer = gp.cChecker.checkPlayer(this);

        if (this.type == type_monster && contactPlayer) {
            damagePlayer(attack);
        }

        if (collisionOn == false) {
            switch (direction) {
                case "up": worldY -= speed; break;
                case "down": worldY += speed; break;
                case "left": worldX -= speed; break;
                case "right": worldX += speed; break;
            }
        }

        spriteCounter++;
        if (spriteCounter > 12) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }

        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        if (shotAvailableCounter < 30) {
            shotAvailableCounter++;
        }
    }

    public void damagePlayer(int attack) {
        if (!gp.player.invincible) {
            // we can give the player damage
            gp.playSE(6);

            int damage = attack - gp.player.defense;
            if (damage < 0) {
                damage = 0;
            }
            gp.player.life -= damage;

            gp.player.invincible = true;

            // Check if attack should apply a status effect
            if (this.type == type_monster) {
                applyMonsterStatusEffects();
            }
        }
    }

    // New method to apply status effects from monster attacks
    private void applyMonsterStatusEffects() {
        // Example: Ghosts have a chance to apply freeze effect
        if (this.name.equals("Ghost")) {
            if (new java.util.Random().nextInt(100) < 30) { // 30% chance
                gp.player.statusEffects.addEffect(
                        StatusEffect.STATUS_FREEZE,
                        180, // 3 seconds at 60 FPS
                        30   // 30% speed reduction
                );
            }
        }

        // Example: Squids have a chance to apply poison effect
        else if (this.name.equals("Squid")) {
            if (new java.util.Random().nextInt(100) < 20) { // 20% chance
                gp.player.statusEffects.addEffect(
                        StatusEffect.STATUS_POISON,
                        300, // 5 seconds at 60 FPS
                        1    // 1 damage per tick
                );
            }
        }

        // Example: Dungeon Lord has a chance to apply burn effect
        else if (this.name.equals("Dungeon Lord")) {
            if (new java.util.Random().nextInt(100) < 50) { // 50% chance
                gp.player.statusEffects.addEffect(
                        StatusEffect.STATUS_BURN,
                        360, // 6 seconds at 60 FPS
                        2    // 2 damage per tick
                );
            }
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

            switch (direction) {
                case "up":
                    if (spriteNum == 1) image = up1;
                    if (spriteNum == 2) image = up2;
                    break;
                case "down":
                    if (spriteNum == 1) image = down1;
                    if (spriteNum == 2) image = down2;
                    break;
                case "left":
                    if (spriteNum == 1) image = left1;
                    if (spriteNum == 2) image = left2;
                    break;
                case "right":
                    if (spriteNum == 1) image = right1;
                    if (spriteNum == 2) image = right2;
                    break;
            }

            // MOnster hp bar
            if (type == 2 && hpBarOn) {
                double oneScale = (double)gp.tileSize / maxLife;
                double hpBarValue = oneScale * life;

                g2.setColor(Color.BLACK);
                g2.fillRect(screenX, screenY - 16, gp.tileSize, 10);
                g2.setColor(Color.RED);
                g2.fillRect(screenX, screenY - 15, (int)hpBarValue, 10);

                hpBarCounter++;

                if (hpBarCounter > 600) {
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }

            if (invincible) {
                hpBarOn = true;
                hpBarCounter = 0;
                changeAlpha(g2, 0.4f);
            }

            if (dying) {
                dyingAnimation(g2);
            }

            g2.drawImage(image, screenX, screenY, null);

            // Draw status effect icons if entity has any
            if (statusEffects.getActiveEffects().size() > 0) {
                statusEffects.draw(g2, screenX, screenY - 20);
            }

            changeAlpha(g2, 1f);
        }
    }

    public void dyingAnimation(Graphics2D g2) {
        dyingCounter++;

        int i = 5;

        if (dyingCounter <= i) changeAlpha(g2, 0f);
        if (dyingCounter > i && dyingCounter <= i*2) changeAlpha(g2, 1f);
        if (dyingCounter > i*2 && dyingCounter <= i*3) changeAlpha(g2, 0f);
        if (dyingCounter > i*3 && dyingCounter <= i*4) changeAlpha(g2, 1f);
        if (dyingCounter > i*4 && dyingCounter <= i*5) changeAlpha(g2, 0f);
        if (dyingCounter > i*5 && dyingCounter <= i*6) changeAlpha(g2, 1f);
        if (dyingCounter > i*6 && dyingCounter <= i*7) changeAlpha(g2, 0f);
        if (dyingCounter > i*7 && dyingCounter <= i*8) changeAlpha(g2, 1f);
        if (dyingCounter > i*8) {
            alive = false;
        }
    }

    public void changeAlpha(Graphics2D g2, float alphaValue) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }

    public BufferedImage setup(String imagePath, int width, int height) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaleImage(image, width, height);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    // Method to apply healing (useful for potions)
    public void heal(int amount) {
        life += amount;
        if (life > maxLife) {
            life = maxLife;
        }
    }

    // Method to restore mana
    public void restoreMana(int amount) {
        mana += amount;
        if (mana > maxMana) {
            mana = maxMana;
        }
    }

    // Method to clear all status effects
    public void clearStatusEffects() {
        statusEffects.clearEffects();
    }

    // Check if the entity is affected by a specific status
    public boolean hasStatusEffect(int type) {
        return statusEffects.hasEffect(type);
    }

    public int getAttack() {
        int attackValue = attack;

        // Add weapon attack bonus
        if (currentWeapon != null) {
            attackValue += currentWeapon.attackValue;
        }

        // Add status effect bonuses
        if (statusEffects.hasEffect(StatusEffect.STATUS_STRENGTH)) {
            attackValue += 5; // Example bonus
        }

        return attackValue;
    }
}