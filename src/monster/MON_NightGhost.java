package monster;

import entity.Entity;
import entity.StatusEffect;
import main.GamePanel;
import object.OBJ_Gold;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random; /**
 * A special version of Ghost that only appears at night
 * and is more powerful
 */
public class MON_NightGhost extends Entity {
    
    private GamePanel gp;
    private int moveCounter = 0;
    private boolean visible = false;
    private int visibilityCounter = 0;
    public boolean active = false;
    
    public MON_NightGhost(GamePanel gp) {
        super(gp);
        this.gp = gp;
        
        type = type_monster;
        name = "Night Ghost";
        speed = 2; // Faster than normal ghost
        maxLife = 8;
        life = maxLife;
        attack = 5;
        defense = 2;
        exp = 10;
        
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
        // Default speed for status effects
        defaultSpeed = speed;
        
        getImage();
        
        // Only active at night
        active = false;
    }
    
    public void getImage() {
        // This is a placeholder until you have actual night ghost images
        // We'll recolor the regular ghost to make it look different
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
        // Only update if it's night time or dusk
        if (gp.envManager.isNight() || gp.envManager.isDusk()) {
            if (!active) {
                active = true;
                gp.ui.addMessage("The night brings spectral enemies!");
            }
            
            super.update();
            
            // Special night ghost behavior - become visible/invisible
            visibilityCounter++;
            if (visibilityCounter > 180) { // Change every 3 seconds
                visible = !visible;
                visibilityCounter = 0;
                
                // When becoming visible, teleport closer to player
                if (visible && isPlayerNearby(15)) { // Only teleport if player is within 15 tiles
                    teleportNearPlayer();
                }
            }
        } else {
            // During daytime, deactivate
            if (active) {
                active = false;
                
                // Move far away or make inactive
                worldX = -100;
                worldY = -100;
            }
        }
    }
    
    private void teleportNearPlayer() {
        // Calculate a position 3-5 tiles away from player in a random direction
        Random random = new Random();
        int distance = 3 + random.nextInt(3); // 3 to 5 tiles
        int direction = random.nextInt(4); // 0: up, 1: right, 2: down, 3: left
        
        switch (direction) {
            case 0: // Up
                worldX = gp.player.worldX;
                worldY = gp.player.worldY - (distance * gp.tileSize);
                break;
            case 1: // Right
                worldX = gp.player.worldX + (distance * gp.tileSize);
                worldY = gp.player.worldY;
                break;
            case 2: // Down
                worldX = gp.player.worldX;
                worldY = gp.player.worldY + (distance * gp.tileSize);
                break;
            case 3: // Left
                worldX = gp.player.worldX - (distance * gp.tileSize);
                worldY = gp.player.worldY;
                break;
        }
        
        // Ensure we're not teleporting into a solid tile
        int col = worldX / gp.tileSize;
        int row = worldY / gp.tileSize;
        
        if (col >= 0 && col < gp.maxWorldCol && row >= 0 && row < gp.maxWorldRow) {
            if (gp.tileM.mapTileNum[col][row] != 0) {
                // If it would teleport into a wall, just move it a bit
                worldX = gp.player.worldX + gp.tileSize * 2;
                worldY = gp.player.worldY + gp.tileSize * 2;
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
    
    @Override
    public void setAction() {
        actionLockCounter++;
        
        if (actionLockCounter == 60) { // More frequent direction changes
            Random random = new Random();
            
            // If player is nearby, move toward player
            if (isPlayerNearby(10)) {
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
    public void draw(Graphics2D g2) {
        if (!active) return;
        
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
                g2.setColor(new Color(140, 0, 210)); // Purple for night ghost
                g2.fillRect(screenX, screenY - 15, (int)hpBarValue, 10);
                
                hpBarCounter++;
                
                if (hpBarCounter > 600) {
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }
            
            // Change alpha based on visibility
            if (!visible) {
                changeAlpha(g2, 0.3f);
            } else if (invincible) {
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
        
        // When damaged, become visible and move toward player
        visible = true;
        visibilityCounter = 0;
        moveTowardPlayer();
    }
    
    @Override
    public void checkDrop() {
        // Night ghosts drop better loot
        dropItem(new OBJ_Gold(gp));
        dropItem(new OBJ_Gold(gp));
        
        int i = new Random().nextInt(100) + 1;
        
        if (i < 50) {
            dropItem(new OBJ_Heart(gp));
        } else {
            dropItem(new OBJ_ManaCrystal(gp));
        }
    }
    
    @Override
    public void damagePlayer(int attack) {
        if (!gp.player.invincible) {
            gp.playSE(6);
            
            int damage = attack - gp.player.defense;
            if (damage < 0) {
                damage = 0;
            }
            
            gp.player.life -= damage;
            gp.player.invincible = true;
            
            // Night ghosts can cause freeze effect
            if (new Random().nextInt(100) < 40) { // 40% chance
                gp.player.statusEffects.addEffect(
                    StatusEffect.STATUS_FREEZE, 
                    240, // 4 seconds at 60 FPS
                    40   // 40% speed reduction
                );
                gp.ui.addMessage("You feel a chill running down your spine!");
            }
        }
    }
}
