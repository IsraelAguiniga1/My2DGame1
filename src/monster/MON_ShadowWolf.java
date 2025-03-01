package monster;

import entity.Entity;
import entity.Projectile;
import entity.StatusEffect;
import main.GamePanel;
import object.OBJ_Gold;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;
import object.OBJ_Rock;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * A monster that becomes more aggressive at night
 */
public class MON_ShadowWolf extends Entity {

    private GamePanel gp;
    private int howlCounter = 0;
    private boolean hasHowled = false;

    public MON_ShadowWolf(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_monster;
        name = "Shadow Wolf";

        // Default daytime stats
        speed = 2;
        maxLife = 6;
        life = maxLife;
        attack = 4;
        defense = 1;
        exp = 8;

        // Default speed for status effects
        defaultSpeed = speed;

        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage() {
        // Placeholder wolf images - using monster for now
        up1 = setup("/monster/monsterdown", gp.tileSize, gp.tileSize);
        up2 = setup("/monster/monsterdown2", gp.tileSize, gp.tileSize);
        down1 = setup("/monster/monsterdown", gp.tileSize, gp.tileSize);
        down2 = setup("/monster/monsterdown2", gp.tileSize, gp.tileSize);
        left1 = setup("/monster/monsterdown", gp.tileSize, gp.tileSize);
        left2 = setup("/monster/monsterdown2", gp.tileSize, gp.tileSize);
        right1 = setup("/monster/monsterdown", gp.tileSize, gp.tileSize);
        right2 = setup("/monster/monsterdown2", gp.tileSize, gp.tileSize);
    }

    @Override
    public void update() {
        // Check if it's night or dusk and transform the wolf
        if (gp.envManager.isNight() || gp.envManager.isDusk()) {
            // Night-time stats boost
            if (speed == defaultSpeed) { // Only boost once
                speed = 3;
                attack = 6;

                // If at night and player is nearby, howl
                if (!hasHowled && isPlayerNearby(10)) {
                    gp.ui.addMessage("A wolf howls in the darkness...");
                    gp.playSE(6); // Use receive damage sound for now
                    hasHowled = true;
                }
            }

            // Revert to normal at dawn
        } else if (speed > defaultSpeed) {
            speed = defaultSpeed;
            attack = 4;
            hasHowled = false;
        }

        super.update();

        // Howl periodically at night to alert other wolves
        if ((gp.envManager.isNight() || gp.envManager.isDusk()) && isPlayerNearby(15)) {
            howlCounter++;

            if (howlCounter > 300) { // Howl every 5 seconds
                if (new Random().nextInt(100) < 30) { // 30% chance to howl
                    gp.ui.addMessage("The wolves are hunting...");
                    // Alert other wolves to player's position
                    alertOtherWolves();
                }
                howlCounter = 0;
            }
        }
    }

    private boolean isPlayerNearby(int tileDistance) {
        int playerCol = gp.player.worldX / gp.tileSize;
        int playerRow = gp.player.worldY / gp.tileSize;
        int monsterCol = worldX / gp.tileSize;
        int monsterRow = worldY / gp.tileSize;

        // Check if player is within specified tiles
        return Math.abs(playerCol - monsterCol) < tileDistance &&
               Math.abs(playerRow - monsterRow) < tileDistance;
    }

    private void alertOtherWolves() {
        // Find other wolves and make them move toward player
        for (int i = 0; i < gp.monster.length; i++) {
            if (gp.monster[i] != null && gp.monster[i] != this) {
                if (gp.monster[i] instanceof MON_ShadowWolf) {
                    MON_ShadowWolf otherWolf = (MON_ShadowWolf) gp.monster[i];
                    otherWolf.moveTowardPlayer();
                }
            }
        }
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

    @Override
    public void setAction() {
        actionLockCounter++;

        if (actionLockCounter == 120) {
            Random random = new Random();

            // At night, higher chance to target player if nearby
            if ((gp.envManager.isNight() || gp.envManager.isDusk()) && isPlayerNearby(10) && random.nextInt(100) < 75) {
                moveTowardPlayer();
            } else {
                // Random movement
                int i = random.nextInt(100) + 1;

                if (i <= 25) direction = "up";
                else if (i <= 50) direction = "down";
                else if (i <= 75) direction = "left";
                else direction = "right";
            }

            actionLockCounter = 0;
        }
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

            // Draw with different tint at night
            if (gp.envManager.isNight() || gp.envManager.isDusk()) {
                // Create darker, more menacing version
                BufferedImage tintedImage = new BufferedImage(
                    image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = tintedImage.createGraphics();

                // Draw with a blue-ish night tint
                g2d.drawImage(image, 0, 0, null);
                g2d.setComposite(AlphaComposite.SrcAtop.derive(0.3f));
                g2d.setColor(new Color(0, 0, 50)); // Dark blue tint
                g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
                g2d.dispose();

                // Use the tinted image
                image = tintedImage;
            }

            // HP bar
            if (type == 2 && hpBarOn) {
                double oneScale = (double)gp.tileSize / maxLife;
                double hpBarValue = oneScale * life;

                g2.setColor(Color.BLACK);
                g2.fillRect(screenX, screenY - 16, gp.tileSize, 10);

                // Different color for day/night
                if (gp.envManager.isNight() || gp.envManager.isDusk()) {
                    g2.setColor(new Color(100, 0, 0)); // Darker red at night
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
    public void damageReaction() {
        actionLockCounter = 0;

        // At night, when damaged, always target player
        if (gp.envManager.isNight() || gp.envManager.isDusk()) {
            moveTowardPlayer();

            // At night, howl to call for help when damaged
            if (new Random().nextInt(100) < 50) { // 50% chance
                gp.ui.addMessage("The wolf howls for help!");
                alertOtherWolves();
            }
        } else {
            // During day, 50% chance to run away when damaged
            if (new Random().nextInt(100) < 50) {
                // Run away from player
                String oppositeDirection = "";
                switch (gp.player.direction) {
                    case "up": oppositeDirection = "down"; break;
                    case "down": oppositeDirection = "up"; break;
                    case "left": oppositeDirection = "right"; break;
                    case "right": oppositeDirection = "left"; break;
                }
                direction = oppositeDirection;
            }
        }
    }

    @Override
    public void checkDrop() {
        // Drop different items based on time of day
        if (gp.envManager.isNight() || gp.envManager.isDusk()) {
            // Better drops at night
            dropItem(new OBJ_Gold(gp));

            int i = new Random().nextInt(100) + 1;

            if (i < 50) {
                dropItem(new OBJ_Heart(gp));
            } else if (i < 90) {
                dropItem(new OBJ_ManaCrystal(gp));
            } else {
                // Rare drop: potion
                dropItem(new object.OBJ_Potion_Red(gp));
            }
        } else {
            // Normal drops during day
            int i = new Random().nextInt(100) + 1;

            if (i < 50) {
                dropItem(new OBJ_Gold(gp));
            } else if (i < 75) {
                dropItem(new OBJ_Heart(gp));
            } else {
                dropItem(new OBJ_ManaCrystal(gp));
            }
        }
    }
}