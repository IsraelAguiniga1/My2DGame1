package entity;

import main.GamePanel;
import main.Quest;

import java.util.Random;

public class NPC_QuestGiver extends Entity {
    
    private String questId;
    private boolean questCompleted = false;
    private boolean rewardsGiven = false;

    public NPC_QuestGiver(GamePanel gp, String questId) {
        super(gp);
        
        this.questId = questId;
        direction = "down";
        speed = 1;
        
        getImage();
        setDialogue();
        updateDialogueBasedOnQuestState();
    }

    public void getImage() {
        // Using existing NPC images for now
        up1 = setup("/npc/npcwalkingup", gp.tileSize, gp.tileSize);
        up2 = setup("/npc/npcwalkingup2", gp.tileSize, gp.tileSize);
        down1 = setup("/npc/npcwalkingdown", gp.tileSize, gp.tileSize);
        down2 = setup("/npc/npcwalkingdown2", gp.tileSize, gp.tileSize);
        left1 = setup("/npc/npcdown", gp.tileSize, gp.tileSize);
        left2 = setup("/npc/npcdown", gp.tileSize, gp.tileSize);
        right1 = setup("/npc/npcwalkingright", gp.tileSize, gp.tileSize);
        right2 = setup("/npc/npcwalkingright2", gp.tileSize, gp.tileSize);
    }
    
    public void setDialogue() {
        // Default dialogues - will be updated based on quest state
        dialogues[0] = "Hello adventurer! I have a task for you.";
        dialogues[1] = "Would you be willing to help me?";
        dialogues[2] = "Thank you! Return to me when you're done.";
        dialogues[3] = "I'm still waiting for you to complete my task.";
        dialogues[4] = "Excellent work! Here's your reward.";
        dialogues[5] = "Thank you for your help!";
    }
    
    private void updateDialogueBasedOnQuestState() {
        Quest quest = gp.questManager.getQuestById(questId);
        if (quest != null) {
            switch (quest.getState()) {
                case Quest.QUEST_NOT_STARTED:
                    dialogues[0] = "Hello adventurer! I have a task for you.";
                    dialogues[1] = "I need you to " + quest.getDescription();
                    dialogues[2] = "Would you help me? You'll be rewarded!";
                    break;
                case Quest.QUEST_ACTIVE:
                    if (quest.isComplete() && !rewardsGiven) {
                        dialogues[0] = "You've completed the task! Well done!";
                        dialogues[1] = "Here's your reward: " + quest.getExpReward() + " EXP and " + quest.getCoinReward() + " coins.";
                        if (quest.getItemReward() != null) {
                            dialogues[2] = "And also this " + quest.getItemReward().name + "!";
                        } else {
                            dialogues[2] = "Thank you for your help!";
                        }
                    } else {
                        dialogues[0] = "How's your progress?";
                        dialogues[1] = "Remember, I need you to " + quest.getDescription();
                        dialogues[2] = "Current progress: " + quest.getCurrentAmount() + "/" + quest.getTargetAmount();
                    }
                    break;
                case Quest.QUEST_COMPLETED:
                    dialogues[0] = "Thank you for your help with that task!";
                    dialogues[1] = "You're a true hero.";
                    dialogues[2] = "Perhaps we'll work together again someday.";
                    break;
            }
        }
    }

    public void setAction() {
        actionLockCounter++;

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
    }
    
    @Override
    public void speak() {
        updateDialogueBasedOnQuestState();
        
        Quest quest = gp.questManager.getQuestById(questId);
        if (quest != null) {
            // If quest not started yet, activate it
            if (quest.getState() == Quest.QUEST_NOT_STARTED) {
                quest.setState(Quest.QUEST_ACTIVE);
            }
            
            // If quest complete but rewards not given yet
            if (quest.getState() == Quest.QUEST_ACTIVE && quest.isComplete() && !rewardsGiven) {
                quest.completeQuest(gp.player);
                rewardsGiven = true;
                quest.setState(Quest.QUEST_COMPLETED);
            }
        }
        
        super.speak();
    }
}