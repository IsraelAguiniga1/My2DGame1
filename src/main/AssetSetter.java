package main;

import entity.NPC_Man;
import entity.NPC_Merchant;
import entity.NPC_QuestGiver;
import monster.*;
import object.*;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {
        // Get the current map name to determine which objects to place
        String currentMap = gp.currentMap;

        if (currentMap.equals("/maps/worldV2.txt") || currentMap.isEmpty()) {
            setWorldObjects();
        } else if (currentMap.equals("/maps/dungeon.txt")) {
            setDungeonObjects();
        }
    }

    private void setWorldObjects() {
        int i = 0;

        // Gold coins
        gp.obj[i] = new OBJ_Gold(gp);
        gp.obj[i].worldX = gp.tileSize * 21;
        gp.obj[i].worldY = gp.tileSize * 21;
        i++;

        gp.obj[i] = new OBJ_Gold(gp);
        gp.obj[i].worldX = gp.tileSize * 23;
        gp.obj[i].worldY = gp.tileSize * 25;
        i++;

        // Heart (health)
        gp.obj[i] = new OBJ_Heart(gp);
        gp.obj[i].worldX = gp.tileSize * 23;
        gp.obj[i].worldY = gp.tileSize * 26;
        i++;

        // Mana crystal
        gp.obj[i] = new OBJ_ManaCrystal(gp);
        gp.obj[i].worldX = gp.tileSize * 23;
        gp.obj[i].worldY = gp.tileSize * 27;
        i++;

        // Axe weapon
        gp.obj[i] = new OBJ_Axe(gp);
        gp.obj[i].worldX = gp.tileSize * 23;
        gp.obj[i].worldY = gp.tileSize * 28;
        i++;

        // Blue shield
        gp.obj[i] = new OBJ_Shield_Blue(gp);
        gp.obj[i].worldX = gp.tileSize * 23;
        gp.obj[i].worldY = gp.tileSize * 29;
        i++;

        // Red potion (healing)
        gp.obj[i] = new OBJ_Potion_Red(gp);
        gp.obj[i].worldX = gp.tileSize * 23;
        gp.obj[i].worldY = gp.tileSize * 30;
        i++;

        // New status effect potions
        gp.obj[i] = new OBJ_Potion_Blue(gp);
        gp.obj[i].worldX = gp.tileSize * 24;
        gp.obj[i].worldY = gp.tileSize * 30;
        i++;

        gp.obj[i] = new OBJ_Potion_Green(gp);
        gp.obj[i].worldX = gp.tileSize * 25;
        gp.obj[i].worldY = gp.tileSize * 30;
        i++;

        // Dungeon teleporter
        gp.obj[i] = new OBJ_Teleporter(gp, 25, 25, "/maps/dungeon.txt", "Mysterious Dungeon");
        gp.obj[i].worldX = gp.tileSize * 28;
        gp.obj[i].worldY = gp.tileSize * 28;
        i++;
    }

    private void setDungeonObjects() {
        int i = 0;

        // Dungeon entrance teleporter (back to main world)
        gp.obj[i] = new OBJ_Teleporter(gp, 28, 29, "/maps/worldV2.txt", "Overworld");
        gp.obj[i].worldX = gp.tileSize * 25;
        gp.obj[i].worldY = gp.tileSize * 25;
        i++;

        // Treasure chest
        gp.obj[i] = new OBJ_Chest(gp);
        gp.obj[i].worldX = gp.tileSize * 30;
        gp.obj[i].worldY = gp.tileSize * 30;
        i++;

        // Gold coins
        gp.obj[i] = new OBJ_Gold(gp);
        gp.obj[i].worldX = gp.tileSize * 28;
        gp.obj[i].worldY = gp.tileSize * 32;
        i++;

        gp.obj[i] = new OBJ_Gold(gp);
        gp.obj[i].worldX = gp.tileSize * 29;
        gp.obj[i].worldY = gp.tileSize * 32;
        i++;

        gp.obj[i] = new OBJ_Gold(gp);
        gp.obj[i].worldX = gp.tileSize * 30;
        gp.obj[i].worldY = gp.tileSize * 32;
        i++;

        // Heart (health)
        gp.obj[i] = new OBJ_Heart(gp);
        gp.obj[i].worldX = gp.tileSize * 32;
        gp.obj[i].worldY = gp.tileSize * 28;
        i++;

        // Mana crystal
        gp.obj[i] = new OBJ_ManaCrystal(gp);
        gp.obj[i].worldX = gp.tileSize * 33;
        gp.obj[i].worldY = gp.tileSize * 28;
        i++;

        // Potions
        gp.obj[i] = new OBJ_Potion_Red(gp);
        gp.obj[i].worldX = gp.tileSize * 35;
        gp.obj[i].worldY = gp.tileSize * 30;
        i++;

        gp.obj[i] = new OBJ_Potion_Yellow(gp);
        gp.obj[i].worldX = gp.tileSize * 36;
        gp.obj[i].worldY = gp.tileSize * 30;
        i++;

        gp.obj[i] = new OBJ_Potion_Antidote(gp);
        gp.obj[i].worldX = gp.tileSize * 37;
        gp.obj[i].worldY = gp.tileSize * 30;
        i++;
    }

    public void setNPC() {
        // Get the current map name to determine which NPCs to place
        String currentMap = gp.currentMap;

        if (currentMap.equals("/maps/worldV2.txt") || currentMap.isEmpty()) {
            setWorldNPCs();
        } else if (currentMap.equals("/maps/dungeon.txt")) {
            setDungeonNPCs();
        }
    }

    private void setWorldNPCs() {
        // Regular NPC
        gp.npc[0] = new NPC_Man(gp);
        gp.npc[0].worldX = gp.tileSize * 21;
        gp.npc[0].worldY = gp.tileSize * 21;

        gp.npc[1] = new NPC_Man(gp);
        gp.npc[1].worldX = gp.tileSize * 22;
        gp.npc[1].worldY = gp.tileSize * 22;

        // Quest givers
        gp.npc[2] = new NPC_QuestGiver(gp, "slime_hunt");
        gp.npc[2].worldX = gp.tileSize * 25;
        gp.npc[2].worldY = gp.tileSize * 23;

        gp.npc[3] = new NPC_QuestGiver(gp, "collect_crystals");
        gp.npc[3].worldX = gp.tileSize * 26;
        gp.npc[3].worldY = gp.tileSize * 24;
    }

    private void setDungeonNPCs() {
        // Dungeon merchant
        gp.npc[0] = new NPC_Merchant(gp);
        gp.npc[0].worldX = gp.tileSize * 27;
        gp.npc[0].worldY = gp.tileSize * 27;

        // Dungeon quest giver
        gp.npc[1] = new NPC_QuestGiver(gp, "ghost_hunt");
        gp.npc[1].worldX = gp.tileSize * 28;
        gp.npc[1].worldY = gp.tileSize * 27;
    }

    public void setMonster() {
        // Get the current map name to determine which monsters to place
        String currentMap = gp.currentMap;

        if (currentMap.equals("/maps/worldV2.txt") || currentMap.isEmpty()) {
            setWorldMonsters();
        } else if (currentMap.equals("/maps/dungeon.txt")) {
            setDungeonMonsters();
        }
    }

    private void setWorldMonsters() {
        int i = 0;

        // Regular squids
        gp.monster[i] = new MON_SQUID(gp);
        gp.monster[i].worldX = gp.tileSize * 23;
        gp.monster[i].worldY = gp.tileSize * 37;
        i++;

        gp.monster[i] = new MON_SQUID(gp);
        gp.monster[i].worldX = gp.tileSize * 23;
        gp.monster[i].worldY = gp.tileSize * 38;
        i++;

        gp.monster[i] = new MON_SQUID(gp);
        gp.monster[i].worldX = gp.tileSize * 23;
        gp.monster[i].worldY = gp.tileSize * 39;
        i++;

        gp.monster[i] = new MON_SQUID(gp);
        gp.monster[i].worldX = gp.tileSize * 23;
        gp.monster[i].worldY = gp.tileSize * 40;
        i++;

        gp.monster[i] = new MON_SQUID(gp);
        gp.monster[i].worldX = gp.tileSize * 23;
        gp.monster[i].worldY = gp.tileSize * 41;
        i++;

        // Regular ghosts
        if (i < gp.monster.length - 1) {
            gp.monster[i] = new MON_Ghost(gp);
            gp.monster[i].worldX = gp.tileSize * 25;
            gp.monster[i].worldY = gp.tileSize * 30;
            i++;
        }

        if (i < gp.monster.length - 1) {
            gp.monster[i] = new MON_Ghost(gp);
            gp.monster[i].worldX = gp.tileSize * 26;
            gp.monster[i].worldY = gp.tileSize * 31;
            i++;
        }

        // Add shadow wolves
        if (i < gp.monster.length - 1) {
            gp.monster[i] = new MON_ShadowWolf(gp);
            gp.monster[i].worldX = gp.tileSize * 27;
            gp.monster[i].worldY = gp.tileSize * 32;
            i++;
        }

        if (i < gp.monster.length - 1) {
            gp.monster[i] = new MON_ShadowWolf(gp);
            gp.monster[i].worldX = gp.tileSize * 28;
            gp.monster[i].worldY = gp.tileSize * 33;
            i++;
        }

        if (i < gp.monster.length - 1) {
            gp.monster[i] = new MON_ShadowWolf(gp);
            gp.monster[i].worldX = gp.tileSize * 29;
            gp.monster[i].worldY = gp.tileSize * 34;
            i++;
        }

        // Add night ghosts (only active at night)
        if (i < gp.monster.length - 1) {
            gp.monster[i] = new MON_NightGhost(gp);
            gp.monster[i].worldX = gp.tileSize * 30;
            gp.monster[i].worldY = gp.tileSize * 35;
            i++;
        }

        if (i < gp.monster.length - 1) {
            gp.monster[i] = new MON_NightGhost(gp);
            gp.monster[i].worldX = gp.tileSize * 31;
            gp.monster[i].worldY = gp.tileSize * 36;
            i++;
        }

        if (i < gp.monster.length - 1) {
            gp.monster[i] = new MON_NightGhost(gp);
            gp.monster[i].worldX = gp.tileSize * 32;
            gp.monster[i].worldY = gp.tileSize * 37;
            i++;
        }
    }

    private void setDungeonMonsters() {
        int i = 0;

        // More challenging monsters in the dungeon
        gp.monster[i] = new MON_Ghost(gp);
        gp.monster[i].worldX = gp.tileSize * 30;
        gp.monster[i].worldY = gp.tileSize * 27;
        i++;

        gp.monster[i] = new MON_Ghost(gp);
        gp.monster[i].worldX = gp.tileSize * 31;
        gp.monster[i].worldY = gp.tileSize * 28;
        i++;

        gp.monster[i] = new MON_Ghost(gp);
        gp.monster[i].worldX = gp.tileSize * 32;
        gp.monster[i].worldY = gp.tileSize * 29;
        i++;

        gp.monster[i] = new MON_Ghost(gp);
        gp.monster[i].worldX = gp.tileSize * 33;
        gp.monster[i].worldY = gp.tileSize * 30;
        i++;

        gp.monster[i] = new MON_SQUID(gp);
        gp.monster[i].worldX = gp.tileSize * 35;
        gp.monster[i].worldY = gp.tileSize * 35;
        i++;

        gp.monster[i] = new MON_SQUID(gp);
        gp.monster[i].worldX = gp.tileSize * 36;
        gp.monster[i].worldY = gp.tileSize * 36;
        i++;

        // Add dungeon boss
        if (i < gp.monster.length - 1) {
            gp.monster[i] = new MON_DungeonBoss(gp);
            gp.monster[i].worldX = gp.tileSize * 40;
            gp.monster[i].worldY = gp.tileSize * 40;
            i++;
        }
    }

    public void setMerchant() {
        // Find the first available NPC slot to avoid overwriting existing NPCs
        for (int i = 0; i < gp.npc.length; i++) {
            if (gp.npc[i] == null) {
                // Place merchant based on current map
                if (gp.currentMap.equals("/maps/dungeon.txt")) {
                    gp.npc[i] = new NPC_Merchant(gp);
                    gp.npc[i].worldX = gp.tileSize * 27;
                    gp.npc[i].worldY = gp.tileSize * 27;
                } else {
                    gp.npc[i] = new NPC_Merchant(gp);
                    gp.npc[i].worldX = gp.tileSize * 24;
                    gp.npc[i].worldY = gp.tileSize * 24;
                }
                break;
            }
        }
    }

    // Method to set teleporters
    public void setTeleporters() {
        // Teleporters are handled in setObject() for simplicity
    }

    // Method to update monsters based on time of day
    public void updateTimeBasedMonsters() {
        // Make night creatures more visible/active at night
        boolean isNightTime = gp.envManager.isNight() || gp.envManager.isDusk();

        for (int i = 0; i < gp.monster.length; i++) {
            if (gp.monster[i] != null) {
                if (gp.monster[i] instanceof MON_NightGhost) {
                    // Night ghosts only become active at night
                    MON_NightGhost ghost = (MON_NightGhost) gp.monster[i];

                    if (isNightTime && !ghost.active) {
                        // Spawn them somewhere close to the player during night
                        if (new java.util.Random().nextInt(100) < 30) { // 30% chance for any individual ghost
                            int distance = 10 + new java.util.Random().nextInt(5); // 10-15 tiles away
                            int direction = new java.util.Random().nextInt(4); // random direction

                            switch (direction) {
                                case 0: // Up
                                    ghost.worldX = gp.player.worldX;
                                    ghost.worldY = gp.player.worldY - (distance * gp.tileSize);
                                    break;
                                case 1: // Right
                                    ghost.worldX = gp.player.worldX + (distance * gp.tileSize);
                                    ghost.worldY = gp.player.worldY;
                                    break;
                                case 2: // Down
                                    ghost.worldX = gp.player.worldX;
                                    ghost.worldY = gp.player.worldY + (distance * gp.tileSize);
                                    break;
                                case 3: // Left
                                    ghost.worldX = gp.player.worldX - (distance * gp.tileSize);
                                    ghost.worldY = gp.player.worldY;
                                    break;
                            }

                            ghost.active = true;
                        }
                    }
                    else if (!isNightTime && ghost.active) {
                        // Hide them during the day
                        ghost.active = false;
                        ghost.worldX = -100;
                        ghost.worldY = -100;
                    }
                }
            }
        }
    }

    // Method to spawn random enemies
    public void spawnRandomEnemies() {
        // Count current monsters
        int currentMonsterCount = 0;
        for (int i = 0; i < gp.monster.length; i++) {
            if (gp.monster[i] != null) {
                currentMonsterCount++;
            }
        }

        // Only spawn if there are fewer than 12 monsters
        if (currentMonsterCount < 12) {
            // Find empty slot
            for (int i = 0; i < gp.monster.length; i++) {
                if (gp.monster[i] == null) {
                    // Determine type based on time of day and location
                    boolean isNightTime = gp.envManager.isNight() || gp.envManager.isDusk();
                    boolean inDungeon = gp.currentMap.equals("/maps/dungeon.txt");

                    if (inDungeon) {
                        // Spawn dungeon monsters
                        int monsterType = new java.util.Random().nextInt(3);

                        switch (monsterType) {
                            case 0:
                                gp.monster[i] = new MON_Ghost(gp);
                                break;
                            case 1:
                                gp.monster[i] = new MON_SQUID(gp);
                                break;
                            case 2:
                                // Rare chance for a mini-boss
                                if (new java.util.Random().nextInt(100) < 10) { // 10% chance
                                    gp.monster[i] = new MON_DungeonBoss(gp);
                                } else {
                                    gp.monster[i] = new MON_Ghost(gp);
                                }
                                break;
                        }
                    } else {
                        // Spawn overworld monsters
                        if (isNightTime) {
                            // Night spawns
                            int monsterType = new java.util.Random().nextInt(3);

                            switch (monsterType) {
                                case 0:
                                    gp.monster[i] = new MON_NightGhost(gp);
                                    break;
                                case 1:
                                    gp.monster[i] = new MON_ShadowWolf(gp);
                                    break;
                                case 2:
                                    gp.monster[i] = new MON_SQUID(gp);
                                    break;
                            }
                        } else {
                            // Day spawns
                            int monsterType = new java.util.Random().nextInt(2);

                            switch (monsterType) {
                                case 0:
                                    gp.monster[i] = new MON_SQUID(gp);
                                    break;
                                case 1:
                                    gp.monster[i] = new MON_ShadowWolf(gp);
                                    break;
                            }
                        }
                    }

                    // Set monster position - somewhere near but not too close to player
                    int distance = 8 + new java.util.Random().nextInt(8); // 8-15 tiles away
                    int angle = new java.util.Random().nextInt(360); // random angle

                    // Convert angle to radians and calculate position
                    double radians = Math.toRadians(angle);
                    int xOffset = (int)(Math.cos(radians) * distance * gp.tileSize);
                    int yOffset = (int)(Math.sin(radians) * distance * gp.tileSize);

                    int x = gp.player.worldX + xOffset;
                    int y = gp.player.worldY + yOffset;

                    // Make sure position is within world bounds
                    if (x < 0) x = 0;
                    if (x > gp.worldWidth - gp.tileSize) x = gp.worldWidth - gp.tileSize;
                    if (y < 0) y = 0;
                    if (y > gp.worldHeight - gp.tileSize) y = gp.worldHeight - gp.tileSize;

                    gp.monster[i].worldX = x;
                    gp.monster[i].worldY = y;

                    break; // Only spawn one monster per update
                }
            }
        }
    }
}