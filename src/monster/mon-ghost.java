package monster;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Gold;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;

import java.util.Random;

public class MON_Ghost extends Entity {
    GamePanel gp;
    private int moveCounter = 0;
    private int phaseCounter = 0;
    private boolean phased = false;
    
    public MON_Ghost(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_monster;
        name = "Ghost";
        speed = 1;
        maxLife = 6;
        life = maxLife;
        attack = 4;
        defense = 1;
        exp = 5;

        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
        getImage();
    }
    
    public void getImage() {
        up1 = setup("/monster/ghost_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("/monster/ghost_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("/monster/ghost_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/monster/ghost_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/monster/ghost_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("/monster/ghost_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("/monster/ghost_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/monster/ghost_right_2", gp.tileSize, gp.tileSize);
    }
    
    public void setAction() {
        actionLockCounter++;
        
        // Randomly change direction
        if (actionLockCounter == 120) {
            Random random = new Random();
            int i = random.nextInt(100) + 1;
            
            if (i <= 25) {
                direction = "up";
            } else if (i > 25 && i <= 50) {
                direction = "down";
            } else if (i > 50 && i <= 75) {
                direction = "left";
            } else if (i > 75 && i <= 100) {
                direction = "right";
            }
            
            actionLockCounter = 0;
        }
        
        // Ghost phases in and out (can pass through walls when phased)
        phaseCounter++;
        if (phaseCounter >= 300) { // Every 5 seconds
            phased = !phased;
            phaseCounter = 0;
        }
    }
    
    @Override
    public void update() {
        // Override the update method to handle phasing
        setAction();
        
        // Only check collisions when not phased
        if (!phased) {
            collisionOn = false;
            gp.cChecker.checkTile(this);
            gp.cChecker.checkObject(this, false);
            gp.cChecker.checkEntity(this, gp.npc);
            gp.cChecker.checkEntity(this, gp.monster);
        } else {
            // When phased, we still check for player contact
            gp.cChecker.checkPlayer(this);
        }
        
        // Move if no collision or if phased
        if (!collisionOn || phased) {
            switch (direction) {
                case "up": worldY -= speed; break;
                case "down": worldY += speed; break;
                case "left": worldX -= speed; break;
                case "right": worldX += speed; break;
            }
        }
        
        // Animation
        spriteCounter++;
        if (spriteCounter > 12) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
        
        // Invincibility frames
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
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
            
            // HP bar
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
            
            // Draw with transparency when phased
            if (phased) {
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
    
    public void damageReaction() {
        actionLockCounter = 0;
        direction = gp.player.direction;
    }
    
    public void checkDrop() {
        // Higher chance to drop mana crystals (ghosts are magical)
        int i = new Random().nextInt(100) + 1;
        
        if (i < 50) {
            dropItem(new OBJ_Gold(gp));
        } else if (i >= 50 && i < 75) {
            dropItem(new OBJ_Heart(gp));
        } else {
            dropItem(new OBJ_ManaCrystal(gp));
            dropItem(new OBJ_Gold(gp));
        }
    }
}
