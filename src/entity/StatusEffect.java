package entity;

import main.GamePanel;
import main.UtilityTool;

import java.awt.*;
import java.awt.image.BufferedImage; /**
 * Represents a status effect that can be applied to entities
 */
public class StatusEffect {
    // Status effect types
    public static final int STATUS_POISON = 0;
    public static final int STATUS_BURN = 1;
    public static final int STATUS_FREEZE = 2;
    public static final int STATUS_STRENGTH = 3;
    public static final int STATUS_DEFENSE = 4;
    public static final int STATUS_SPEED = 5;
    
    // Status effect properties
    private int type;
    private int duration; // in frames
    private int power;
    private Entity target;
    private BufferedImage icon;
    private String name;
    private String description;
    private GamePanel gp;
    
    // Constructor
    public StatusEffect(GamePanel gp, int type, int duration, int power, Entity target) {
        this.gp = gp;
        this.type = type;
        this.duration = duration;
        this.power = power;
        this.target = target;
        
        // Set icon and description based on type
        setupEffect();
    }
    
    private void setupEffect() {
        switch(type) {
            case STATUS_POISON:
                name = "Poison";
                description = "Taking damage over time";
                icon = setupImage("/status/poison");
                break;
            case STATUS_BURN:
                name = "Burn";
                description = "Taking fire damage over time";
                icon = setupImage("/status/burn");
                break;
            case STATUS_FREEZE:
                name = "Freeze";
                description = "Movement speed reduced";
                icon = setupImage("/status/freeze");
                break;
            case STATUS_STRENGTH:
                name = "Strength Up";
                description = "Attack power increased";
                icon = setupImage("/status/strength");
                break;
            case STATUS_DEFENSE:
                name = "Defense Up";
                description = "Defense increased";
                icon = setupImage("/status/defense");
                break;
            case STATUS_SPEED:
                name = "Speed Up";
                description = "Movement speed increased";
                icon = setupImage("/status/speed");
                break;
        }
    }
    
    private BufferedImage setupImage(String imagePath) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = javax.imageio.ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaleImage(image, 16, 16);
        } catch (Exception e) {
            // If image not found, create a placeholder
            image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = image.createGraphics();
            g2.setColor(getColorForEffect());
            g2.fillRect(0, 0, 16, 16);
            g2.dispose();
        }
        return image;
    }
    
    private Color getColorForEffect() {
        switch(type) {
            case STATUS_POISON: return new Color(128, 0, 128); // Purple
            case STATUS_BURN: return new Color(255, 69, 0);   // Red-Orange
            case STATUS_FREEZE: return new Color(0, 191, 255); // Deep Sky Blue
            case STATUS_STRENGTH: return new Color(255, 0, 0); // Red
            case STATUS_DEFENSE: return new Color(0, 0, 255);  // Blue
            case STATUS_SPEED: return new Color(255, 255, 0);  // Yellow
            default: return Color.WHITE;
        }
    }
    
    // Apply the effect to the target
    public void apply() {
        switch(type) {
            case STATUS_POISON:
                applyPoison();
                break;
            case STATUS_BURN:
                applyBurn();
                break;
            case STATUS_FREEZE:
                applyFreeze();
                break;
            case STATUS_STRENGTH:
                applyStrength();
                break;
            case STATUS_DEFENSE:
                applyDefense();
                break;
            case STATUS_SPEED:
                applySpeed();
                break;
        }
    }
    
    // Remove the effect from the target
    public void remove() {
        switch(type) {
            case STATUS_POISON:
            case STATUS_BURN:
                // No cleanup needed for damage over time effects
                break;
            case STATUS_FREEZE:
                target.speed = target.defaultSpeed;
                break;
            case STATUS_STRENGTH:
                target.strength -= power;
                target.attack = target.getAttack();
                break;
            case STATUS_DEFENSE:
                target.defense -= power;
                break;
            case STATUS_SPEED:
                target.speed = target.defaultSpeed;
                break;
        }
    }
    
    // Effect implementations
    private void applyPoison() {
        if (duration % 60 == 0) { // Apply damage once per second
            int damage = power;
            if (damage < 1) damage = 1;
            
            target.life -= damage;
            
            // Show damage number
            if (target == gp.player) {
                gp.ui.addMessage(damage + " poison damage!");
            }
            
            // Check if target died from poison
            if (target.life <= 0) {
                target.dying = true;
                if (target == gp.player) {
                    gp.ui.addMessage("You died from poison!");
                }
            }
        }
    }
    
    private void applyBurn() {
        if (duration % 30 == 0) { // Apply damage twice per second
            int damage = power;
            if (damage < 1) damage = 1;
            
            target.life -= damage;
            
            // Show damage number
            if (target == gp.player) {
                gp.ui.addMessage(damage + " burn damage!");
            }
            
            // Check if target died from burn
            if (target.life <= 0) {
                target.dying = true;
                if (target == gp.player) {
                    gp.ui.addMessage("You died from burning!");
                }
            }
        }
    }
    
    private void applyFreeze() {
        // Store the original speed if not already stored
        if (target.defaultSpeed == 0) {
            target.defaultSpeed = target.speed;
        }
        
        // Reduce speed by power percent
        int reducedSpeed = (int)(target.defaultSpeed * (1 - (power / 100.0)));
        if (reducedSpeed < 1) reducedSpeed = 1; // Minimum speed of 1
        
        target.speed = reducedSpeed;
    }
    
    private void applyStrength() {
        // Increase strength by power
        target.strength += power;
        target.attack = target.getAttack();
    }
    
    private void applyDefense() {
        // Increase defense by power
        target.defense += power;
    }
    
    private void applySpeed() {
        // Store the original speed if not already stored
        if (target.defaultSpeed == 0) {
            target.defaultSpeed = target.speed;
        }
        
        // Increase speed by power percent
        target.speed = (int)(target.defaultSpeed * (1 + (power / 100.0)));
    }
    
    // Update the status effect (called each frame)
    public boolean update() {
        duration--;
        
        // Apply the effect
        apply();
        
        // Check if effect has expired
        if (duration <= 0) {
            remove();
            return true; // Effect should be removed
        }
        
        return false; // Effect continues
    }
    
    // Draw the status effect icon
    public void draw(Graphics2D g2, int x, int y) {
        g2.drawImage(icon, x, y, null);
    }
    
    // Getters
    public int getType() { return type; }
    public int getDuration() { return duration; }
    public int getPower() { return power; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BufferedImage getIcon() { return icon; }
}
