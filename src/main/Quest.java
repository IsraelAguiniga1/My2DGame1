package main;

import entity.Entity;
import entity.Player;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a single quest in the game
 */
public class Quest implements Serializable {
    // Quest states
    public static final int QUEST_NOT_STARTED = 0;
    public static final int QUEST_ACTIVE = 1;
    public static final int QUEST_COMPLETED = 2;
    
    // Quest types
    public static final int QUEST_TYPE_KILL = 0;
    public static final int QUEST_TYPE_COLLECT = 1;
    public static final int QUEST_TYPE_TALK = 2;
    
    // Quest data
    private String id;
    private String name;
    private String description;
    int questType;
    private int state;
    private String targetName;
    private int targetAmount;
    private int currentAmount;
    
    // Rewards
    private int expReward;
    private int coinReward;
    private Entity itemReward;
    
    public Quest(String id, String name, String description, int questType, 
                 String targetName, int targetAmount, int expReward, int coinReward, Entity itemReward) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.questType = questType;
        this.state = QUEST_NOT_STARTED;
        this.targetName = targetName;
        this.targetAmount = targetAmount;
        this.currentAmount = 0;
        this.expReward = expReward;
        this.coinReward = coinReward;
        this.itemReward = itemReward;
    }
    
    public void updateProgress(int amount) {
        if (state == QUEST_ACTIVE) {
            currentAmount += amount;
            if (currentAmount >= targetAmount) {
                currentAmount = targetAmount;
            }
        }
    }
    
    public boolean isComplete() {
        return currentAmount >= targetAmount;
    }
    
    public void completeQuest(Player player) {
        if (state == QUEST_ACTIVE && isComplete()) {
            state = QUEST_COMPLETED;
            
            // Give rewards
            player.exp += expReward;
            player.coin += coinReward;
            
            // Add item reward if there is one
            if (itemReward != null && player.inventory.size() < player.maxInventorySize) {
                player.inventory.add(itemReward);
            }
            
            // Check for level up
            player.checkLevelUp();
        }
    }
    
    // Getters and setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getState() { return state; }
    public void setState(int state) { this.state = state; }
    public String getTargetName() { return targetName; }
    public int getTargetAmount() { return targetAmount; }
    public int getCurrentAmount() { return currentAmount; }
    public int getExpReward() { return expReward; }
    public int getCoinReward() { return coinReward; }
    public Entity getItemReward() { return itemReward; }
}

