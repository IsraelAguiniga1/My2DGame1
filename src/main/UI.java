package main;

import entity.Entity;
import entity.NPC_Merchant;
import object.OBJ_Heart;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font maruMonica, purisaB;
    BufferedImage heart_full, heart_half, heart_blank, crystal_full, crystal_blank;
    BufferedImage questIcon;

    public boolean messageOn = false;
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    public boolean gameFinished = false;
    public String currentDialogue = "";
    public int commandNum = 0;
    public int titleScreenState = 0;
    public int slotCol = 0;
    public int slotRow = 0;

    public Entity merchant;
    public int itemIndex = 0;
    public int subState = 0;

    // Quest UI variables
    public int questPage = 0;
    public int questSelected = 0;

    public UI(GamePanel gp) {
        this.gp = gp;

        try {
            InputStream is = getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf");
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);
            is = getClass().getResourceAsStream("/font/Purisa Bold.ttf");
            purisaB = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create HUD object
        Entity heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;
        Entity crystal = new object.OBJ_ManaCrystal(gp);
        crystal_full = crystal.image;
        crystal_blank = crystal.image2;

        // Quest icon
        questIcon = setupImage("/objects/manacrystal_full");
    }

    private BufferedImage setupImage(String imagePath) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = javax.imageio.ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void addMessage(String text) {
        message.add(text);
        messageCounter.add(0);
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(maruMonica);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);

        // Draw UI based on game state
        switch (gp.gameState) {
            case 0: // Title state
                drawTitleScreen();
                break;
            case 1: // Play state
                drawPlayerLife();
                drawMessage();
                drawActiveQuestIndicator();
                break;
            case 2: // Pause state
                drawPlayerLife();
                drawPauseScreen();
                break;
            case 3: // Dialogue state
                drawPlayerLife();
                drawDialogueScreen();
                break;
            case 4: // Character state
                drawCharacterScreen();
                drawInventory();
                break;
            case 5: // Shop state
                drawShopScreen();
                break;
            case 6: // Save state
                drawSaveConfirmation();
                break;
            case 7: // Load state
                drawLoadConfirmation();
                break;
            case 8: // Quest state
                drawQuestScreen();
                break;
        }
    }

    private void drawDialogueScreen() {
        // Window
        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 4;

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));
        x += gp.tileSize;
        y += gp.tileSize;

        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, x, y);
            y += 40;
        }
    }

    private void drawPauseScreen() {
        // Window
        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.screenHeight - (gp.tileSize * 4);

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));
        String text = "PAUSED";
        int textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        x = gp.screenWidth / 2 - textLength / 2;
        y += gp.tileSize;

        g2.drawString(text, x, y);
    }

    private void drawShopScreen() {
        // Window
        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.screenHeight - (gp.tileSize * 4);

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));
        String text = "SHOP";
        int textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        x = gp.screenWidth / 2 - textLength / 2;
        y += gp.tileSize;

        g2.drawString(text, x, y);
    }

    private void drawCharacterScreen() {
        // Window
        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.screenHeight - (gp.tileSize * 4);

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));
        String text = "CHARACTER";
        int textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        x = gp.screenWidth / 2 - textLength / 2;
        y += gp.tileSize;

        g2.drawString(text, x, y);
    }

    private void drawInventory() {
        // Window
        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.screenHeight - (gp.tileSize * 4);

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));
        String text = "INVENTORY";
        int textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        x = gp.screenWidth / 2 - textLength / 2;
        y += gp.tileSize;

        g2.drawString(text, x, y);
    }

    private void drawSaveConfirmation() {
        // Window
        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.screenHeight - (gp.tileSize * 4);

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));
        String text = "SAVE GAME?";
        int textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        x = gp.screenWidth / 2 - textLength / 2;
        y += gp.tileSize;

        g2.drawString(text, x, y);
    }

    private void drawLoadConfirmation() {
        // Window
        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.screenHeight - (gp.tileSize * 4);

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));
        String text = "LOAD GAME?";
        int textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        x = gp.screenWidth / 2 - textLength / 2;
        y += gp.tileSize;

        g2.drawString(text, x, y);
    }

    private void drawTitleScreen() {
        // Background
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // Title
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 96F));
        String text = "GAME TITLE";
        int textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth / 2 - textLength / 2;
        int y = gp.tileSize * 3;

        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);

        // Menu
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 48F));
        text = "NEW GAME";
        textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        x = gp.screenWidth / 2 - textLength / 2;
        y += gp.tileSize * 3;

        g2.drawString(text, x, y);

        text = "LOAD GAME";
        textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        x = gp.screenWidth / 2 - textLength / 2;
        y += gp.tileSize;

        g2.drawString(text, x, y);

    }

    // New method to draw active quest indicator
    public void drawActiveQuestIndicator() {
        ArrayList<Quest> activeQuests = gp.questManager.getActiveQuests();

        if (!activeQuests.isEmpty()) {
            // Draw quest icon in top-right corner
            g2.drawImage(questIcon, gp.screenWidth - gp.tileSize - 20, 20, null);

            // Draw number of active quests
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));
            g2.setColor(Color.WHITE);
            g2.drawString(String.valueOf(activeQuests.size()), gp.screenWidth - 30, 30);

            // Draw first active quest info
            Quest firstQuest = activeQuests.get(0);
            String questInfo = firstQuest.getName() + ": " + firstQuest.getCurrentAmount() + "/" + firstQuest.getTargetAmount();

            int stringLength = (int)g2.getFontMetrics().getStringBounds(questInfo, g2).getWidth();
            int x = gp.screenWidth - stringLength - 30;
            int y = 60;

            // Draw background
            Color bgColor = new Color(0, 0, 0, 128);
            g2.setColor(bgColor);
            g2.fillRoundRect(x - 10, y - 20, stringLength + 20, 30, 10, 10);

            // Draw text
            g2.setColor(Color.WHITE);
            g2.drawString(questInfo, x, y);
        }
    }

    // New method to draw quest screen
    public void drawQuestScreen() {
        // Draw background
        drawSubWindow(gp.tileSize, gp.tileSize, gp.screenWidth - (gp.tileSize*2), gp.screenHeight - (gp.tileSize*2));

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));
        g2.setColor(Color.WHITE);

        // Title
        String title = "Quests";
        int x = getXForCenteredText(title);
        int y = gp.tileSize * 2;
        g2.drawString(title, x, y);

        // Get quests
        ArrayList<Quest> activeQuests = gp.questManager.getActiveQuests();
        ArrayList<Quest> completedQuests = gp.questManager.getCompletedQuests();

        // Display tabs
        x = gp.tileSize + 20;
        y = gp.tileSize * 3;
        g2.setFont(g2.getFont().deriveFont(24F));

        g2.drawString("Active", x, y);
        if (questPage == 0) {
            g2.drawString(">", x - 20, y);
        }

        x += 150;
        g2.drawString("Completed", x, y);
        if (questPage == 1) {
            g2.drawString(">", x - 20, y);
        }

        // Draw quest list based on selected tab
        ArrayList<Quest> questsToShow = (questPage == 0) ? activeQuests : completedQuests;

        if (questsToShow.isEmpty()) {
            x = gp.tileSize + 20;
            y = gp.tileSize * 4 + 20;
            g2.drawString("No quests in this category.", x, y);
        } else {
            x = gp.tileSize + 20;
            y = gp.tileSize * 4;

            for (int i = 0; i < questsToShow.size(); i++) {
                Quest quest = questsToShow.get(i);

                if (questSelected == i) {
                    g2.drawString(">", x - 20, y + 20);
                }

                // Draw quest name
                g2.setColor(Color.YELLOW);
                g2.drawString(quest.getName(), x, y + 20);

                // Draw quest progress
                g2.setColor(Color.WHITE);
                String progress = quest.getCurrentAmount() + "/" + quest.getTargetAmount();
                g2.drawString(progress, x + 250, y + 20);

                y += 40;
            }

            // Draw selected quest details
            if (questSelected >= 0 && questSelected < questsToShow.size()) {
                Quest selectedQuest = questsToShow.get(questSelected);

                x = gp.tileSize + 20;
                y = gp.screenHeight - (gp.tileSize * 4);

                // Description
                g2.setFont(g2.getFont().deriveFont(20F));
                g2.drawString("Description:", x, y);
                y += 30;
                g2.drawString(selectedQuest.getDescription(), x + 20, y);

                // Rewards
                y += 50;
                g2.drawString("Rewards:", x, y);
                y += 30;
                g2.drawString("EXP: " + selectedQuest.getExpReward(), x + 20, y);
                y += 30;
                g2.drawString("Gold: " + selectedQuest.getCoinReward(), x + 20, y);

                if (selectedQuest.getItemReward() != null) {
                    y += 30;
                    g2.drawString("Item: " + selectedQuest.getItemReward().name, x + 20, y);
                }
            }
        }

        // Instructions
        x = gp.tileSize + 20;
        y = gp.screenHeight - gp.tileSize;
        g2.setFont(g2.getFont().deriveFont(18F));
        g2.drawString("Press Q to return", x, y);
    }

    // The rest of the UI methods remain the same...

    // Include the original methods for drawPlayerLife, drawMessage, etc.
    public void drawPlayerLife() {
        int x = gp.tileSize/2;
        int y = gp.tileSize/2;
        int i = 0;

        // Draw max life
        while (i < gp.player.maxLife/2) {
            g2.drawImage(heart_blank, x, y, null);
            i++;
            x += gp.tileSize;
        }

        // Reset
        x = gp.tileSize/2;
        y = gp.tileSize/2;
        i = 0;

        // Draw current life
        while (i < gp.player.life) {
            g2.drawImage(heart_half, x, y, null);
            i++;
            if (i < gp.player.life) {
                g2.drawImage(heart_full, x, y, null);
            }
            i++;
            x += gp.tileSize;
        }

        // Draw max mana
        x = (gp.tileSize/2) - 5;
        y = (int)(gp.tileSize * 1.5);
        i = 0;
        while (i < gp.player.maxMana) {
            g2.drawImage(crystal_blank, x, y, null);
            i++;
            x += 35;
        }

        // Draw current mana
        x = (gp.tileSize/2) - 5;
        y = (int)(gp.tileSize * 1.5);
        i = 0;
        while (i < gp.player.mana) {
            g2.drawImage(crystal_full, x, y, null);
            i++;
            x += 35;
        }
    }

    public void drawMessage() {
        int messageX = gp.tileSize;
        int messageY = gp.tileSize * 4;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));

        for (int i = 0; i < message.size(); i++) {
            if (message.get(i) != null) {
                g2.setColor(Color.black);
                g2.drawString(message.get(i), messageX + 2, messageY + 2); // Shadow

                g2.setColor(Color.WHITE);
                g2.drawString(message.get(i), messageX, messageY); // Main text

                int counter = messageCounter.get(i) + 1; // Increment counter
                messageCounter.set(i, counter); // Update counter in list
                messageY += 50;

                if (messageCounter.get(i) > 180) {
                    message.remove(i);
                    messageCounter.remove(i);
                    messageY -= 50;
                }
            }
        }
    }

    // Other methods like drawTitleScreen, drawDialogueScreen, etc.

    public int getItemIndexOnSlot() {
        return slotCol + (slotRow * 5);
    }

    public void drawSubWindow(int x, int y, int width, int height) {
        Color c = new Color(0, 0, 0, 200);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 35, 35);
    }

    public int getXForCenteredText(String text) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth/2 - length/2;
    }

    public int getXForAlignToRightText(String text, int tailX) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return tailX - length;
    }
}