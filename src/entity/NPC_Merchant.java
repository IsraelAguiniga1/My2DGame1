package entity;

import main.GamePanel;
import object.*;

import java.util.ArrayList;
import java.util.Random;

public class NPC_Merchant extends Entity {

    // Make the inventory public for easier access
    public ArrayList<Entity> inventory = new ArrayList<>();

    public NPC_Merchant(GamePanel gp) {
        super(gp);

        direction = "down";
        speed = 1;

        // Set merchant appearance
        getImage();
        setDialogue();
        setItems();
    }

    public void getImage() {
        // Using existing NPC images instead of merchant-specific ones
        // This avoids the need for new image files
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
        dialogues[0] = "Hello traveler! I have some rare items\nfor sale if you have the coin!";
        dialogues[1] = "Take your time browsing my wares.";
        dialogues[2] = "Would you like to see what I have\nfor sale?";
        dialogues[3] = "Come back anytime!";
    }

    public void setItems() {
        inventory.add(new OBJ_Potion_Red(gp));
        inventory.add(new OBJ_Key(gp));
        inventory.add(new OBJ_Sword_Normal(gp));
        inventory.add(new OBJ_Shield_Blue(gp));
        inventory.add(new OBJ_Axe(gp));

        // Set prices for each item
        for(Entity item : inventory) {
            item.price = setPrice(item);
        }
    }

    private int setPrice(Entity item) {
        int price = 0;

        // Set prices based on item type
        switch(item.name) {
            case "Red Potion": price = 25; break;
            case "Key": price = 50; break;
            case "Normal Sword": price = 100; break;
            case "Blue Shield": price = 150; break;
            case "Wood Axe": price = 125; break;
            default: price = 10; break;
        }

        return price;
    }

    public void speak() {
        super.speak();
        gp.gameState = gp.shopState;
        gp.ui.merchant = this;
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

    // Add a getter for inventory to avoid casting in other classes
    public ArrayList<Entity> getInventory() {
        return inventory;
    }
}