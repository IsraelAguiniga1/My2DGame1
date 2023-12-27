package main;

import entity.Entity;
import entity.Player;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GamePanel extends JPanel implements Runnable{// GamePanel is a JPanel

    // Screen settings
    final int originalTileSize = 16; // 16px
    final int scale = 3; // 3x
    public final int tileSize = originalTileSize * scale; // 48x48px
    public int maxScreenCol = 16;
   public final int maxScreenRow = 12;
    public int screenWidth = tileSize * maxScreenCol;// 864px
    public int screenHeight = tileSize * maxScreenRow; // 672px

    //world settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    //FPS settings
    int FPS = 60;// Set target FPS
    //SYSTEM SETTINGS
    TileManager tileM = new TileManager(this); // Create a new TileManager
    public KeyHandler keyH = new KeyHandler(this);// Create a new KeyHandler
    Sound music = new Sound();// Create a new Sound
    Sound se = new Sound();// Create a new Sound

    public CollisionChecker cChecker = new CollisionChecker(this);// Create a new CollisionChecker
    public AssetSetter aSetter = new AssetSetter(this);// Create a new AssetSetter
    public UI ui = new UI(this);// Create a new UI
    public EventHandler eHandler = new EventHandler(this);// Create a new EventHandler

    Thread gameThread;// Thread for the game loop

    // Entity and object settings
    public Player player = new Player(this, keyH); // Create a new Player
    public Entity obj[] = new Entity[10];  // Create a new SuperObject array
    public Entity npc[] = new Entity[10]; // Create a new Entity array
    public Entity monster[] = new Entity[20]; // Create a new Entity array
    ArrayList<Entity> entityList = new ArrayList<>(); // Create a new ArrayList of Entity objects

    //GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;





    public  GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));// 864x672 pixels
        this.setBackground(Color.BLACK);// Set background color to black
        this.setDoubleBuffered(true);// Enable double buffering
        this.addKeyListener(keyH);//
        this.setFocusable(true);// Set focus to the JPanel
    }

    public void setupGame(){

        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
        playMusic(0);
        stopMusic();
        gameState = titleState;

    }
    public void startGameThread() {
        gameThread = new Thread(this);// Create a new thread
        gameThread.start();// Start the thread
    }

    @Override
    public void run() {
            double drawInterval = 1000000000 / FPS;// Calculate the time between each frame
            double nextDrawTime = System.nanoTime() + drawInterval;// Calculate the time of the next frame

            while (gameThread != null) {

                // While the gameThread is running
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

    public void update() {// Called every frame
        //GAME STATE
        if (gameState == playState){
            //player
            player.update();
            //NPC
            for (int i =0;i<npc.length;i++){
                if (npc[i] != null){
                    npc[i].update();
                }
            }
            for (int i =0;i<monster.length;i++){
                if (monster[i] != null){
                    monster[i].update();
                }
            }
        }
        if (gameState == pauseState){

        }




    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);// Call the super class's paintComponent method
        Graphics2D g2 = (Graphics2D)g;// Cast the Graphics object to a Graphics2D object

        //DEBUG
        long drawStart =0;
        if (keyH.checkDrawTime==true){
            drawStart = System.nanoTime();
        }

        //TITLE SCREEN
        if (gameState == titleState){
            ui.draw(g2);
        }
        //OTHERS
        else {
            //TILE
            tileM.draw(g2);// drawing tiles

            //ADD ALL ENTITIES TO THE ENTITY LIST
            entityList.add(player);// Add the player to the entityList

            for (int i = 0; i < npc.length; i++) {// Add all the NPCs to the entityList
                if (npc[i] != null) {
                    entityList.add(npc[i]);
                }
            }

            for (int i = 0; i < obj.length; i++) {// Add all the objects to the entityList
                if (obj[i] != null) {
                    entityList.add(obj[i]);
                }
            }
            for (int i = 0; i < monster.length; i++) {// Add all the monsters to the entityList
                if (monster[i] != null) {
                    entityList.add(monster[i]);
                }
            }


            //SORT
            Collections.sort(entityList, new Comparator<Entity>() {// Sort the entityList by the y value of each entity
                @Override
                public int compare(Entity e1, Entity e2) {
                    int result = Integer.compare(e1.worldY, e2.worldY);
                    return result;
                }
            });

            //DRAW ENTITIES
            for (int i = 0; i < entityList.size(); i++) {// Draw all the entities in the entityList
                entityList.get(i).draw(g2);
            }
            //EMPTY ENTITY LIST
            for (int i = 0; i < entityList.size(); i++) {// Empty the entityList
                entityList.remove(i);
            }

            //UI
            ui.draw(g2);
        }





        //DEBUG
        if (keyH.checkDrawTime==true){
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            g2.setColor(Color.WHITE);
            g2.drawString("Draw time: " + passed , 10, 400);
            System.out.println("Draw time: " + passed);
        }



        g2.dispose();// Dispose of the Graphics2D object
    }
    public void playMusic(int i){
        music.setFile(i);
        music.play();
        music.loop();
    }
    public void stopMusic(){
        music.stop();
    }
    public void playSE(int i){
        se.setFile(i);
        se.play();
    }
}
