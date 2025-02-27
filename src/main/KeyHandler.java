package main;

import entity.Entity;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed,shotKeyPressed;
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
        }
        if (code == KeyEvent.VK_F) {
            shotKeyPressed = true;
        }
        if (code == KeyEvent.VK_S && (e.isControlDown() || e.isMetaDown())) {
            gp.gameState = gp.saveState;
            gp.ui.commandNum = 0;
        }
        if (code == KeyEvent.VK_L && (e.isControlDown() || e.isMetaDown())) {
            gp.gameState = gp.loadState;
            gp.ui.commandNum = 0;
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
        if (code == KeyEvent.VK_W) {
            if (gp.ui.commandNum == 1) {
                gp.ui.commandNum = 0;
            }
        }

        if (code == KeyEvent.VK_S) {
            if (gp.ui.commandNum == 0) {
                gp.ui.commandNum = 1;
            }
        }

        if (code == KeyEvent.VK_ENTER) {
            if (gp.ui.commandNum == 0) {
                // Save
                gp.config.saveGame();
                gp.gameState = gp.playState;
            } else if (gp.ui.commandNum == 1) {
                // Do not save
                gp.gameState = gp.playState;
            }
        }
    }

    public void loadState(int code) {
        if (code == KeyEvent.VK_W) {
            if (gp.ui.commandNum == 1) {
                gp.ui.commandNum = 0;
            }
        }

        if (code == KeyEvent.VK_S) {
            if (gp.ui.commandNum == 0) {
                gp.ui.commandNum = 1;
            }
        }

        if (code == KeyEvent.VK_ENTER) {
            if (gp.ui.commandNum == 0) {
                // Load
                gp.config.loadGame();
                gp.gameState = gp.playState;
            } else if (gp.ui.commandNum == 1) {
                // Do not load
                gp.gameState = gp.playState;
            }
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
    public void shopState(int code) {
        if (code == KeyEvent.VK_ENTER) {
            gp.gameState = gp.dialogueState;
        }

        if (code == KeyEvent.VK_W) {
            if (gp.ui.commandNum > 0) {
                gp.ui.commandNum--;
                gp.playSE(9);
            }
        }

        if (code == KeyEvent.VK_S) {
            if (gp.ui.commandNum < 3) {
                gp.ui.commandNum++;
                gp.playSE(9);
            }
        }

        if (code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.playState;
            gp.ui.commandNum = 0;
        }

        if (code == KeyEvent.VK_ENTER) {
            Entity selectedItem = gp.ui.merchant.inventory.get(gp.ui.itemIndex);

            if (gp.ui.commandNum == 0) {
                // Buy
                if (gp.player.coin >= selectedItem.price) {
                    if (gp.player.inventory.size() < gp.player.maxInventorySize) {
                        gp.player.coin -= selectedItem.price;
                        gp.player.inventory.add(selectedItem);
                        gp.playSE(1);
                        gp.ui.addMessage("Bought " + selectedItem.name + "!");
                    } else {
                        gp.ui.addMessage("Inventory full!");
                    }
                } else {
                    gp.ui.addMessage("Not enough coins!");
                }
            }

            // Sell option would be commandNum == 1
            // Exit option would be commandNum == 2
        }
    }
}


