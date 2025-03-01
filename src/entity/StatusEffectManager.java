package entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import main.GamePanel;
import main.UtilityTool;

/**
 * Manager for handling multiple status effects on an entity
 */
public class StatusEffectManager {
    private Entity entity;
    private GamePanel gp;
    private java.util.ArrayList<StatusEffect> activeEffects;
    
    public StatusEffectManager(GamePanel gp, Entity entity) {
        this.gp = gp;
        this.entity = entity;
        this.activeEffects = new java.util.ArrayList<>();
    }
    
    // Add a new status effect
    public void addEffect(int type, int duration, int power) {
        // Check if entity already has this effect
        for (StatusEffect effect : activeEffects) {
            if (effect.getType() == type) {
                // If so, just refresh the duration and power if higher
                if (power > effect.getPower()) {
                    activeEffects.remove(effect);
                    break;
                } else {
                    return; // Skip adding if current effect is stronger
                }
            }
        }
        
        // Add the new effect
        StatusEffect newEffect = new StatusEffect(gp, type, duration, power, entity);
        activeEffects.add(newEffect);
        
        // Notify player
        if (entity == gp.player) {
            gp.ui.addMessage("You are affected by " + newEffect.getName() + "!");
        }
    }
    
    // Update all active effects
    public void update() {
        for (int i = activeEffects.size() - 1; i >= 0; i--) {
            StatusEffect effect = activeEffects.get(i);
            boolean expired = effect.update();
            
            if (expired) {
                if (entity == gp.player) {
                    gp.ui.addMessage(effect.getName() + " has worn off.");
                }
                activeEffects.remove(i);
            }
        }
    }
    
    // Draw status effect icons
    public void draw(Graphics2D g2, int x, int y) {
        int spacing = 20;
        
        for (int i = 0; i < activeEffects.size(); i++) {
            StatusEffect effect = activeEffects.get(i);
            effect.draw(g2, x + (i * spacing), y);
        }
    }
    
    // Check if entity has a specific effect
    public boolean hasEffect(int type) {
        for (StatusEffect effect : activeEffects) {
            if (effect.getType() == type) {
                return true;
            }
        }
        return false;
    }
    
    // Remove a specific effect
    public void removeEffect(int type) {
        for (int i = activeEffects.size() - 1; i >= 0; i--) {
            StatusEffect effect = activeEffects.get(i);
            if (effect.getType() == type) {
                effect.remove();
                activeEffects.remove(i);
                
                if (entity == gp.player) {
                    gp.ui.addMessage(effect.getName() + " has been removed.");
                }
            }
        }
    }
    
    // Clear all effects
    public void clearEffects() {
        for (StatusEffect effect : activeEffects) {
            effect.remove();
        }
        activeEffects.clear();
    }
    
    // Get active effects
    public java.util.ArrayList<StatusEffect> getActiveEffects() {
        return activeEffects;
    }
}