package entity;

import main.GamePanel;

import java.util.Random;


public class NPC_Man extends Entity {


    public NPC_Man(GamePanel gp) {
        super(gp);

        direction = "down";
        speed = 2;

        getImage();
        setDialogue();
    }

    public void getImage() {//get player image

        up1 = setup("/npc/npcwalkingup");
        up2 = setup("/npc/npcwalkingup2");
        down1 = setup("/npc/npcwalkingdown");
        down2 = setup("/npc/npcwalkingdown2");
        left1 = setup("/npc/npcdown");
        right1 = setup("/npc/npcwalkingright");
        right2 = setup("/npc/npcwalkingright2");
    }
    public void setDialogue(){
        dialogues[0] = "Man: Hello, buddy.\n How are you? ";
        dialogues[1] = "How are you?";
        dialogues[2] = "I'm fine, thanks";
        dialogues[3] = "How about you?";
        dialogues[4] = "I'm fine too";
        dialogues[5] = "See you later";
        dialogues[6] = "Bye. Have a nice day. im going to the\n market and buy some\n food for my family.";


    }

    public void setAction() {

        actionLockCounter++;

        if (actionLockCounter == 120) {


            Random random = new Random();
            int i = random.nextInt(100) + 1;//random number between 1 and 100

            if (i <= 25) {
                direction = "up";
            }
            if (i > 25 && i <= 50) {
                direction = "down";
            }
            if (i > 50 && i <= 75) {
                direction = "left";
            }
            if (i > 75 && i <= 100) {
                direction = "right";
            }
            actionLockCounter = 0;
        }


    }
    public void speak(){
    super.speak();
    }
}
