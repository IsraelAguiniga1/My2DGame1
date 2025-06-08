package main;

import entity.Entity;
import entity.NPC_Merchant;
import object.OBJ_Teleporter;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, shotKeyPressed;
    //DEBUG
    boolean showDebugText = false;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();// Get the key code of the key that was pressed

        if (code == KeyEvent.VK_Z && e.isControlDown()) {
            gp.gameState = gp.saveState;
            gp.ui.commandNum = 0;
            return;
        }
        if (code == KeyEvent.VK_L && e.isControlDown()) {
            gp.gameState = gp.loadState;
            gp.ui.commandNum = 0;
            return;
        }

        //TITLE STATE
        if (gp.gameState == gp.titleState) {
            titleState(code);
        }
        //PLAY STATE
        else if (gp.gameState == gp.playState){
            playState(code);
        }
        //PAUSE STATE
        else if (gp.gameState == gp.pauseState){
            pauseState(code);
        }
        //DIALOGUE STATE
        else if (gp.gameState == gp.dialogueState){
            dialogueState(code);
        }
        //CHARACTER STATE
        else if (gp.gameState == gp.characterState){
            characterState(code);
        }
        //Shop state
        else if (gp.gameState == gp.shopState) {
            shopState(code);
        }
        else if (gp.gameState == gp.saveState) {
            saveState(code);
        }
        else if (gp.gameState == gp.loadState) {
            loadState(code);
        }
    }
    public void titleState(int code){
        if (gp.ui.titleScreenState == 0) {

            if (code == KeyEvent.VK_W) {
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 2;
                }
            }
            if (code == KeyEvent.VK_S) {
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 2) {
                    gp.ui.commandNum = 0;
                }
            }
            if (code == KeyEvent.VK_ENTER) {
                if (gp.ui.commandNum == 0) {
                    gp.ui.titleScreenState = 1;
                }
                if (gp.ui.commandNum == 1) {
                    //add load game
                }
                if (gp.ui.commandNum == 2) {
                    System.exit(0);
                }

            }
        }
        else if (gp.ui.titleScreenState == 1) {

            if (code == KeyEvent.VK_W) {
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 3;
                }
            }
            if (code == KeyEvent.VK_S) {
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 2) {
                    gp.ui.commandNum = 0;
                }
            }
            if (code == KeyEvent.VK_ENTER) {
                if (gp.ui.commandNum == 0) {
                    System.out.println("Fighter");
                    gp.gameState = gp.playState;
                    //gp.playMusic(0);
                }
                if (gp.ui.commandNum == 1) {
                    System.out.println("Thief");
                    gp.gameState = gp.playState;
                    // gp.playMusic(0);
                }
                if (gp.ui.commandNum == 2) {
                    System.out.println("Sorcerer");
                    gp.gameState = gp.playState;
                    // gp.playMusic(0);
                }
                if (gp.ui.commandNum == 3) {
                    gp.ui.titleScreenState = 0;
                }

            }
        }
    }
    public void playState(int code){
        if (code == KeyEvent.VK_W) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = true;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = true;
        }
        if (code == KeyEvent.VK_P) {
            gp.gameState = gp.pauseState;
        }
        if (code == KeyEvent.VK_C) {
            gp.gameState = gp.characterState;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;

            // Check if player is on a teleporter
            int objIndex = gp.cChecker.checkObject(gp.player, true);
            if (objIndex != 999) {
                if (gp.obj[objIndex] instanceof OBJ_Teleporter) {
                    ((OBJ_Teleporter) gp.obj[objIndex]).use();
                }
            }
        }
        if (code == KeyEvent.VK_F) {
            shotKeyPressed = true;
        }

        //DEBUG
        if (code == KeyEvent.VK_T) {
            if (showDebugText == false) {
                showDebugText = true;
            } else if (showDebugText == true) {
                showDebugText = false;
            }
        }
        if (code == KeyEvent.VK_R) {
            gp.tileM.loadMap("/maps/worldV2.txt");
            System.out.println("Map Reloaded");
        }
    }
    public void saveState(int code) {
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            if (gp.ui.commandNum == 1) {
                gp.ui.commandNum = 0;
                gp.playSE(9);
            }
        }

        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            if (gp.ui.commandNum == 0) {
                gp.ui.commandNum = 1;
                gp.playSE(9);
            }
        }

        if (code == KeyEvent.VK_ENTER) {
            if (gp.ui.commandNum == 0) {
                // Save
                try {
                    gp.config.saveGame();
                    gp.ui.addMessage("Game saved successfully!");
                } catch (Exception e) {
                    gp.ui.addMessage("Error saving game!");
                    System.out.println("Save error: " + e);
                }
                gp.gameState = gp.playState;
            } else if (gp.ui.commandNum == 1) {
                // Do not save
                gp.gameState = gp.playState;
            }
        }

        // Also allow ESC to cancel
        if (code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.playState;
        }
    }

    public void loadState(int code) {
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            if (gp.ui.commandNum == 1) {
                gp.ui.commandNum = 0;
                gp.playSE(9);
            }
        }

        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            if (gp.ui.commandNum == 0) {
                gp.ui.commandNum = 1;
                gp.playSE(9);
            }
        }

        if (code == KeyEvent.VK_ENTER) {
            if (gp.ui.commandNum == 0) {
                // Load
                try {
                    gp.config.loadGame();
                    gp.ui.addMessage("Game loaded successfully!");
                } catch (Exception e) {
                    gp.ui.addMessage("Error loading game or no save file!");
                    System.out.println("Load error: " + e);
                }
                gp.gameState = gp.playState;
            } else if (gp.ui.commandNum == 1) {
                // Do not load
                gp.gameState = gp.playState;
            }
        }

        // Also allow ESC to cancel
        if (code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.playState;
        }
    }
    public void pauseState(int code){
        if (code == KeyEvent.VK_P) {
            gp.gameState = gp.playState;
        }
    }
    public void dialogueState(int code){
        if (code == KeyEvent.VK_ENTER) {
            gp.gameState = gp.playState;
        }
    }
    public void characterState(int code){
        if (code == KeyEvent.VK_C) {
            gp.gameState = gp.playState;
        }
        if (code == KeyEvent.VK_W) {
            if (gp.ui.slotRow != 0){
                gp.ui.slotRow--;
                gp.playSE(9);
            }
        }
        if (code == KeyEvent.VK_A) {
            if (gp.ui.slotCol != 0){
                gp.ui.slotCol--;
                gp.playSE(9);
            }
        }
        if (code == KeyEvent.VK_S) {
            if (gp.ui.slotRow != 3){
                gp.ui.slotRow++;
                gp.playSE(9);
            }
        }
        if (code == KeyEvent.VK_D) {
            if (gp.ui.slotCol != 4){
                gp.ui.slotCol++;
                gp.playSE(9);
            }
        }
        if(code == KeyEvent.VK_ENTER) {
            gp.player.selectItem();
        }
    }

    public void shopState(int code) {
        // Navigation
        if (code == KeyEvent.VK_W) {
            if (gp.ui.commandNum > 0) {
                gp.ui.commandNum--;
                gp.playSE(9);
            }
        }

        if (code == KeyEvent.VK_S) {
            if (gp.ui.commandNum < 2) {
                gp.ui.commandNum++;
                gp.playSE(9);
            }
        }

        // Item selection - Left/Right to navigate shop items
        if (code == KeyEvent.VK_A) {
            if (gp.ui.itemIndex > 0) {
                gp.ui.itemIndex--;
                gp.playSE(9);
            }
        }

        if (code == KeyEvent.VK_D) {
            if (gp.ui.merchant != null && gp.ui.merchant instanceof NPC_Merchant) {
                NPC_Merchant merchant = (NPC_Merchant) gp.ui.merchant;
                if (gp.ui.itemIndex < merchant.inventory.size() - 1) {
                    gp.ui.itemIndex++;
                    gp.playSE(9);
                }
            }
        }

        // Exit shop
        if (code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.playState;
            gp.ui.commandNum = 0;
            gp.ui.itemIndex = 0;
        }

        // Handle commands
        if (code == KeyEvent.VK_ENTER) {
            if (gp.ui.merchant != null && gp.ui.merchant instanceof NPC_Merchant) {
                NPC_Merchant merchant = (NPC_Merchant) gp.ui.merchant;

                if (merchant.inventory.size() > 0 && gp.ui.itemIndex < merchant.inventory.size()) {
                    Entity selectedItem = merchant.inventory.get(gp.ui.itemIndex);

                    if (gp.ui.commandNum == 0) {
                        // Buy
                        if (gp.player.coin >= selectedItem.price) {
                            if (gp.player.inventory.size() < gp.player.maxInventorySize) {
                                gp.player.coin -= selectedItem.price;

                                // Create a new instance of the selected item
                                Entity boughtItem = getItemCopy(selectedItem);

                                gp.player.inventory.add(boughtItem);
                                gp.playSE(1);
                                gp.ui.addMessage("Bought " + selectedItem.name + "!");
                            } else {
                                gp.ui.addMessage("Inventory full!");
                            }
                        } else {
                            gp.ui.addMessage("Not enough coins!");
                        }
                    }
                    else if (gp.ui.commandNum == 1) {
                        // Sell
                        if (gp.player.inventory.size() > 0) {
                            // Switch to inventory selection state
                            gp.gameState = gp.characterState;
                            gp.ui.addMessage("Select an item to sell.");
                            gp.ui.subState = 1; // 1 = sell mode
                        }
                        else {
                            gp.ui.addMessage("You have nothing to sell!");
                        }
                    }
                    else if (gp.ui.commandNum == 2) {
                        // Exit
                        gp.gameState = gp.playState;
                        gp.ui.commandNum = 0;
                        gp.ui.itemIndex = 0;
                    }
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {// Called when a key is released

        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = false;
        }
        if (code == KeyEvent.VK_F) {
            shotKeyPressed = false;
        }
    }

    private Entity getItemCopy(Entity original) {
        Entity copy = null;

        // Create the appropriate item based on type
        if (original.type == original.type_sword) {
            copy = new object.OBJ_Sword_Normal(gp);
        }
        else if (original.type == original.type_shield) {
            if (original.name.equals("Blue Shield")) {
                copy = new object.OBJ_Shield_Blue(gp);
            } else {
                copy = new object.OBJ_Shield_Wood(gp);
            }
        }
        else if (original.type == original.type_consumable) {
            if (original.name.equals("Red Potion")) {
                copy = new object.OBJ_Potion_Red(gp);
            }
        }
        else if (original.type == original.type_axe) {
            copy = new object.OBJ_Axe(gp);
        }
        else {
            // Default - create a key
            copy = new object.OBJ_Key(gp);
        }

        // Set the price to match the original
        copy.price = original.price;

        return copy;
    }
}