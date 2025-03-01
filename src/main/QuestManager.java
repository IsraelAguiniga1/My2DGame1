package main;

import java.io.Serializable;
import java.util.ArrayList; /**
 * Manages all quests in the game
 */
public class QuestManager implements Serializable {
    private ArrayList<Quest> quests;
    private GamePanel gp;
    
    public QuestManager(GamePanel gp) {
        this.gp = gp;
        quests = new ArrayList<>();
        setupQuests();
    }
    
    private void setupQuests() {
        // Add your quests here
        quests.add(new Quest(
            "slime_hunt",
            "Squid Hunt", 
            "Defeat 3 squids in the forest.",
            Quest.QUEST_TYPE_KILL,
            "Squid",
            3,
            5,  // EXP reward
            50, // Coin reward
            new object.OBJ_Shield_Blue(gp) // Item reward
        ));
        
        quests.add(new Quest(
            "ghost_hunt",
            "Ghost Hunt", 
            "Defeat 2 ghosts in the dungeon.",
            Quest.QUEST_TYPE_KILL,
            "Ghost",
            2,
            10, // EXP reward
            100, // Coin reward
            new object.OBJ_Potion_Red(gp) // Item reward
        ));
        
        quests.add(new Quest(
            "collect_crystals",
            "Crystal Collector", 
            "Collect 5 mana crystals.",
            Quest.QUEST_TYPE_COLLECT,
            "Mana Crystal",
            5,
            8, // EXP reward
            80, // Coin reward
            null // No item reward
        ));
    }
    
    public Quest getQuestById(String id) {
        for (Quest quest : quests) {
            if (quest.getId().equals(id)) {
                return quest;
            }
        }
        return null;
    }
    
    public void activateQuest(String id) {
        Quest quest = getQuestById(id);
        if (quest != null && quest.getState() == Quest.QUEST_NOT_STARTED) {
            quest.setState(Quest.QUEST_ACTIVE);
            gp.ui.addMessage("New Quest: " + quest.getName());
        }
    }
    
    public void updateKillQuest(String monsterName) {
        for (Quest quest : quests) {
            if (quest.getState() == Quest.QUEST_ACTIVE && 
                quest.getTargetName().equals(monsterName) &&
                quest.questType == Quest.QUEST_TYPE_KILL) {
                
                quest.updateProgress(1);
                
                if (quest.isComplete()) {
                    gp.ui.addMessage("Quest complete: " + quest.getName());
                } else {
                    gp.ui.addMessage(quest.getName() + ": " + quest.getCurrentAmount() + "/" + quest.getTargetAmount());
                }
            }
        }
    }
    
    public void updateCollectQuest(String itemName) {
        for (Quest quest : quests) {
            if (quest.getState() == Quest.QUEST_ACTIVE && 
                quest.getTargetName().equals(itemName) &&
                quest.questType == Quest.QUEST_TYPE_COLLECT) {
                
                quest.updateProgress(1);
                
                if (quest.isComplete()) {
                    gp.ui.addMessage("Quest complete: " + quest.getName());
                } else {
                    gp.ui.addMessage(quest.getName() + ": " + quest.getCurrentAmount() + "/" + quest.getTargetAmount());
                }
            }
        }
    }
    
    public ArrayList<Quest> getActiveQuests() {
        ArrayList<Quest> activeQuests = new ArrayList<>();
        for (Quest quest : quests) {
            if (quest.getState() == Quest.QUEST_ACTIVE) {
                activeQuests.add(quest);
            }
        }
        return activeQuests;
    }
    
    public ArrayList<Quest> getCompletedQuests() {
        ArrayList<Quest> completedQuests = new ArrayList<>();
        for (Quest quest : quests) {
            if (quest.getState() == Quest.QUEST_COMPLETED) {
                completedQuests.add(quest);
            }
        }
        return completedQuests;
    }
    
    public ArrayList<Quest> getAllQuests() {
        return quests;
    }
}
