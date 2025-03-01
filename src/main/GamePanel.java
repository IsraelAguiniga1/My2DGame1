package main;

import entity.Entity;
import entity.Player;
import monster.MON_DungeonBoss;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable {

    // Screen settings
    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    public int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public int screenWidth = tileSize * maxScreenCol;
    public int screenHeight = tileSize * maxScreenRow;

    // World settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    // Current map
    public String currentMap = "/maps/worldV2.txt";

    // FPS settings
    int FPS = 60;

    // Game loop counter for timed events
    private int gameLoopCounter = 0;
    private int monsterSpawnCounter = 0;
    private boolean bossDefeated = false;

    // System components
    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    Sound music = new Sound();
    Sound se = new Sound();

    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public EventHandler eHandler = new EventHandler(this);
    public Config config = new Config(this);
    public QuestManager questManager = new QuestManager(this);

    Thread gameThread;

    // Entities and objects
    public Player player = new Player(this, keyH);
    public Entity obj[] = new Entity[20]; // Increased from 10
    public Entity npc[] = new Entity[10];
    public Entity monster[] = new Entity[30]; // Increased from 20
    public ArrayList<Entity> projectileList = new ArrayList<>();
    ArrayList<Entity> entityList = new ArrayList<>();
    public EnvironmentManager envManager = new EnvironmentManager(this);

    // Game states
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int characterState = 4;
    public final int shopState = 5;
    public final int saveState = 6;
    public final int loadState = 7;
    public final int questState = 8;
    public final int gameOverState = 9;
    public final int winState = 10;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setupGame() {
        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMerchant();
        aSetter.setMonster();
        playMusic(0);
        //stopMusic();
        gameState = titleState;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {
            update();
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        // Increment game loop counter
        gameLoopCounter++;

        // Periodic game world updates
        if (gameLoopCounter % 600 == 0) { // Every 10 seconds
            // Update time-based monsters
            aSetter.updateTimeBasedMonsters();

            // Check if all monsters are defeated in dungeon for win condition
            if (currentMap.equals("/maps/dungeon.txt")) {
                checkDungeonProgress();
            }
        }

        // Random monster spawning
        monsterSpawnCounter++;
        if (monsterSpawnCounter >= 1800) { // Every 30 seconds
            if (new Random().nextInt(100) < 30) { // 30% chance
                aSetter.spawnRandomEnemies();
            }
            monsterSpawnCounter = 0;
        }

        if (gameState == playState) {
            // Update player
            player.update();

            // Check player life for game over
            if (player.life <= 0) {
                gameState = gameOverState;
                stopMusic();
                playSE(6); // Death sound
            }

            // Update NPCs
            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    npc[i].update();
                }
            }

            // Update monsters
            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null) {
                    if (monster[i].alive && !monster[i].dying) {
                        monster[i].update();
                    }
                    if (!monster[i].alive) {
                        // Handle quest update for monster kill
                        if (monster[i].name != null) {
                            questManager.updateKillQuest(monster[i].name);

                            // Check if dungeon boss was defeated
                            if (monster[i] instanceof MON_DungeonBoss) {
                                bossDefeated = true;
                                ui.addMessage("The Dungeon Lord has been defeated!");
                                playSE(4); // Fanfare
                            }
                        }

                        monster[i].checkDrop();
                        monster[i] = null;
                    }
                }
            }

            // Update projectiles
            for (int i = 0; i < projectileList.size(); i++) {
                if (projectileList.get(i) != null) {
                    if (projectileList.get(i).alive) {
                        projectileList.get(i).update();
                    }
                    if (!projectileList.get(i).alive) {
                        projectileList.remove(i);
                    }
                }
            }

            // Update environment
            envManager.update();
        }
    }

    private void checkDungeonProgress() {
        // Check if boss is defeated and all quests complete
        if (bossDefeated) {
            boolean allQuestsComplete = true;
            for (Quest quest : questManager.getAllQuests()) {
                if (quest.getState() != Quest.QUEST_COMPLETED) {
                    allQuestsComplete = false;
                    break;
                }
            }

            if (allQuestsComplete) {
                gameState = winState;
                ui.currentDialogue = "You have defeated the Dungeon Lord and completed all quests!\nCongratulations, brave adventurer!";
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Debug info
        long drawStart = 0;
        if (keyH.showDebugText) {
            drawStart = System.nanoTime();
        }

        // Title screen
        if (gameState == titleState) {
            ui.draw(g2);
        }
        // Game over and win screens
        else if (gameState == gameOverState || gameState == winState) {
            ui.draw(g2);
        }
        // Other game states
        else {
            // Draw game world
            tileM.draw(g2);

            // Add entities to the list for sorting
            entityList.add(player);

            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    entityList.add(npc[i]);
                }
            }

            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null) {
                    entityList.add(obj[i]);
                }
            }

            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null) {
                    entityList.add(monster[i]);
                }
            }

            for (int i = 0; i < projectileList.size(); i++) {
                if (projectileList.get(i) != null) {
                    entityList.add(projectileList.get(i));
                }
            }

            // Sort entities by Y position for proper depth
            Collections.sort(entityList, new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2) {
                    return Integer.compare(e1.worldY, e2.worldY);
                }
            });

            // Draw all entities
            for (int i = 0; i < entityList.size(); i++) {
                entityList.get(i).draw(g2);
            }

            // Clear entity list
            entityList.clear();

            // Draw environment effects
            if (gameState == playState || gameState == pauseState) {
                envManager.draw(g2);
            }

            // Draw UI
            ui.draw(g2);
        }

        // Debug information
        if (keyH.showDebugText) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;

            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.setColor(Color.WHITE);
            int x = 10;
            int y = 400;
            int lineHeight = 20;

            g2.drawString("WorldX: " + player.worldX, x, y); y += lineHeight;
            g2.drawString("WorldY: " + player.worldY, x, y); y += lineHeight;
            g2.drawString("Col: " + (player.worldX + player.solidArea.x)/tileSize, x, y); y += lineHeight;
            g2.drawString("Row: " + (player.worldY + player.solidArea.y)/tileSize, x, y); y += lineHeight;
            g2.drawString("Draw Time: " + passed, x, y); y += lineHeight;
            g2.drawString("Current Map: " + currentMap, x, y); y += lineHeight;
            g2.drawString("Time: " + envManager.getDayStateName(), x, y); y += lineHeight;
            g2.drawString("Weather: " + envManager.getWeatherName(), x, y); y += lineHeight;

            // Count active monsters
            int monsterCount = 0;
            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null) monsterCount++;
            }
            g2.drawString("Active Monsters: " + monsterCount, x, y);
        }

        g2.dispose();
    }

    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSE(int i) {
        se.setFile(i);
        se.play();
    }

    // Method to change maps
    public void changeMap(String mapPath) {
        currentMap = mapPath;
        tileM.loadMap(mapPath);

        // Reset entities for the new map
        for (int i = 0; i < obj.length; i++) {
            obj[i] = null;
        }

        for (int i = 0; i < npc.length; i++) {
            npc[i] = null;
        }

        for (int i = 0; i < monster.length; i++) {
            monster[i] = null;
        }

        // Clear projectiles
        projectileList.clear();

        // Reset boss defeated flag when leaving dungeon
        if (!mapPath.equals("/maps/dungeon.txt")) {
            bossDefeated = false;
        }

        // Set up new map entities
        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMerchant();
        aSetter.setMonster();
    }

    // Method to reset the game after game over
    public void resetGame() {
        player.setDefaultValues();
        player.setItems();

        // Reset maps and entities
        currentMap = "/maps/worldV2.txt";
        tileM.loadMap(currentMap);

        for (int i = 0; i < obj.length; i++) {
            obj[i] = null;
        }

        for (int i = 0; i < npc.length; i++) {
            npc[i] = null;
        }

        for (int i = 0; i < monster.length; i++) {
            monster[i] = null;
        }

        projectileList.clear();
        bossDefeated = false;

        // Re-initialize the world
        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMerchant();
        aSetter.setMonster();

        // Reset game state
        gameState = playState;

        // Start music again
        playMusic(0);
    }
}