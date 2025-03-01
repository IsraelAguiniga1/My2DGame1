package monster;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Axe;
import object.OBJ_Gold;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;
import object.OBJ_Potion_Red;
import object.OBJ_Shield_Blue;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.image.BufferedImage;
import java.util.Random;

public class MON_DungeonBoss extends Entity {

    GamePanel gp;
    private int attackCooldown = 0;
    private boolean isEnraged = false;
    private int phaseChangeThreshold;

    public MON_DungeonBoss(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_monster;
        name = "Dungeon Lord";
        speed = 1;
        maxLife = 20;
        life = maxLife;
        attack = 8;
        defense = 2;
        exp = 50;
        phaseChangeThreshold = maxLife / 2;

        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage() {
        // Using existing monster images with scaling to make the boss bigger
        up1 = setup("/monster/monsterdown", gp.tileSize+10, gp.tileSize+10);
        up2 = setup("/monster/monsterdown2", gp.tileSize+10, gp.tileSize+10);
        down1 = setup("/monster/monsterdown", gp.tileSize+10, gp.tileSize+10);
        down2 = setup("/monster/monsterdown2", gp.tileSize+10, gp.tileSize+10);
        left1 = setup("/monster/monsterdown", gp.tileSize+10, gp.tileSize+10);
        left2 = setup("/monster/monsterdown2", gp.tileSize+10, gp.tileSize+10);
        right1 = setup("/monster/monsterdown", gp.tileSize+10, gp.tileSize+10);
        right2 = setup("/monster/monsterdown2", gp.tileSize+10, gp.tileSize+10);
    }

    @Override
    public void setAction() {
        // Check if boss should enter enraged phase
        if (!isEnraged && life <= phaseChangeThreshold) {
            becomeEnraged();
        }

        actionLockCounter++;

        if (actionLockCounter == 60) { // Boss moves more frequently
            Random random = new Random();
            int i = random.nextInt(100) + 1;

            // Boss has smarter targeting - moves toward player more often
            if (isPlayerNearby()) {
                moveTowardPlayer();
            } else {
                // Random movement when player not nearby
                if (i <= 25) {
                    direction = "up";
                } else if (i > 25 && i <= 50) {
                    direction = "down";
                } else if (i > 50 && i <= 75) {
                    direction = "left";
                } else {
                    direction = "right";
                }
            }

            actionLockCounter = 0;
        }

        // Boss special attack logic
        attackCooldown++;
        if (attackCooldown >= (isEnraged ? 120 : 180)) { // Attack more frequently when enraged
            if (isPlayerInRange(5)) { // Check if player is within 5 tiles
                performSpecialAttack();
                attackCooldown = 0;
            }
        }
    }

    private boolean isPlayerNearby() {
        int playerCol = gp.player.worldX / gp.tileSize;
        int playerRow = gp.player.worldY / gp.tileSize;
        int monsterCol = worldX / gp.tileSize;
        int monsterRow = worldY / gp.tileSize;

        // Check if player is within 7 tiles
        return Math.abs(playerCol - monsterCol) < 7 && Math.abs(playerRow - monsterRow) < 7;
    }

    private boolean isPlayerInRange(int range) {
        int playerCol = gp.player.worldX / gp.tileSize;
        int playerRow = gp.player.worldY / gp.tileSize;
        int monsterCol = worldX / gp.tileSize;
        int monsterRow = worldY / gp.tileSize;

        return Math.abs(playerCol - monsterCol) <= range && Math.abs(playerRow - monsterRow) <= range;
    }

    private void moveTowardPlayer() {
        // Simple pathfinding - move in the direction of the player
        int playerX = gp.player.worldX;
        int playerY = gp.player.worldY;

        // Determine direction based on player position
        if (Math.abs(worldX - playerX) > Math.abs(worldY - playerY)) {
            // Move horizontally first
            if (worldX < playerX) {
                direction = "right";
            } else {
                direction = "left";
            }
        } else {
            // Move vertically first
            if (worldY < playerY) {
                direction = "down";
            } else {
                direction = "up";
            }
        }
    }

    private void becomeEnraged() {
        isEnraged = true;
        speed += 1;
        attack += 2;
        gp.ui.addMessage("Dungeon Lord becomes enraged!");
        gp.playSE(5); // Use hit monster sound for enrage
    }

    private void performSpecialAttack() {
        // Special attack - AOE damage to player if nearby
        gp.ui.addMessage("Dungeon Lord unleashes a powerful attack!");
        gp.playSE(10); // Use fireball sound for special attack

        // Visual effect for special attack
        hpBarOn = true;
        hpBarCounter = 0;

        // Deal damage to player if in range
        if (isPlayerInRange(3)) {
            int specialAttackDamage = attack * (isEnraged ? 2 : 1);
            damagePlayer(specialAttackDamage);
        }
    }

    @Override
    public void damageReaction() {
        actionLockCounter = 0;
        // When hit, boss has a chance to target player directly
        if (new Random().nextInt(100) < 50) {
            moveTowardPlayer();
        }
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

            BufferedImage image = null;

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

            // HP bar
            if (type == 2 && hpBarOn) {
                double oneScale = (double)gp.tileSize / maxLife;
                double hpBarValue = oneScale * life;

                g2.setColor(Color.BLACK);
                g2.fillRect(screenX, screenY - 16, gp.tileSize, 10);

                // Change color based on enraged state
                if (isEnraged) {
                    g2.setColor(Color.ORANGE);
                } else {
                    g2.setColor(Color.RED);
                }

                g2.fillRect(screenX, screenY - 15, (int)hpBarValue, 10);

                hpBarCounter++;

                if (hpBarCounter > 600) {
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }

            // Enraged visual effect
            if (isEnraged) {
                // Draw a red aura around the boss
                g2.setColor(new Color(255, 0, 0, 70));
                g2.fillOval(screenX - 5, screenY - 5, gp.tileSize + 20, gp.tileSize + 20);
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

            // Reset alpha
            changeAlpha(g2, 1f);
        }
    }

    @Override
    public void checkDrop() {
        // Boss drops multiple items
        dropItem(new OBJ_Gold(gp));
        dropItem(new OBJ_Gold(gp));
        dropItem(new OBJ_Gold(gp));
        dropItem(new OBJ_Heart(gp));
        dropItem(new OBJ_ManaCrystal(gp));

        // Rare items
        if (new Random().nextInt(100) < 50) {
            dropItem(new OBJ_Potion_Red(gp));
        }

        if (new Random().nextInt(100) < 30) {
            dropItem(new OBJ_Shield_Blue(gp));
        }

        if (new Random().nextInt(100) < 20) {
            dropItem(new OBJ_Axe(gp));
        }
    }
}