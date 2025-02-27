package main;

import entity.Entity;
import object.*;

import java.io.*;
import java.util.ArrayList;

public class Config {
    
    GamePanel gp;
    
    public Config(GamePanel gp) {
        this.gp = gp;
    }
    
    public void saveGame() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("save.dat"));
            
            // Save player stats
            DataStorage ds = new DataStorage();
            
            // Player stats
            ds.level = gp.player.level;
            ds.maxLife = gp.player.maxLife;
            ds.life = gp.player.life;
            ds.maxMana = gp.player.maxMana;
            ds.mana = gp.player.mana;
            ds.strength = gp.player.strength;
            ds.dexterity = gp.player.dexterity;
            ds.exp = gp.player.exp;
            ds.nextLevelExp = gp.player.nextLevelExp;
            ds.coin = gp.player.coin;
            
            // Player position
            ds.worldX = gp.player.worldX;
            ds.worldY = gp.player.worldY;
            
            // Player inventory
            ds.itemNames = new String[gp.player.inventory.size()];
            for(int i = 0; i < gp.player.inventory.size(); i++) {
                ds.itemNames[i] = gp.player.inventory.get(i).name;
            }
            
            // Write object
            oos.writeObject(ds);
            
            // Close stream
            oos.close();
            gp.ui.addMessage("Game saved!");
            
        } catch(Exception e) {
            System.out.println("Save exception: " + e);
        }
    }
    
    public void loadGame() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("save.dat"));
            
            // Load the DataStorage object
            DataStorage ds = (DataStorage)ois.readObject();
            
            // Restore player stats
            gp.player.level = ds.level;
            gp.player.maxLife = ds.maxLife;
            gp.player.life = ds.life;
            gp.player.maxMana = ds.maxMana;
            gp.player.mana = ds.mana;
            gp.player.strength = ds.strength;
            gp.player.dexterity = ds.dexterity;
            gp.player.exp = ds.exp;
            gp.player.nextLevelExp = ds.nextLevelExp;
            gp.player.coin = ds.coin;
            
            // Restore position
            gp.player.worldX = ds.worldX;
            gp.player.worldY = ds.worldY;
            
            // Clear current inventory
            gp.player.inventory.clear();
            
            // Restore inventory
            for(String itemName : ds.itemNames) {
                gp.player.inventory.add(getObject(itemName));
            }
            
            gp.ui.addMessage("Game loaded!");
            
            // Close stream
            ois.close();
            
        } catch(Exception e) {
            System.out.println("Load exception: " + e);
        }
    }
    
    // Helper method to create objects by name
    private Entity getObject(String itemName) {
        Entity obj = null;
        
        switch(itemName) {
            case "Key":
                obj = new OBJ_Key(gp);
                break;
            case "Wood Shield":
                obj = new OBJ_Shield_Wood(gp);
                break;
            case "Blue Shield":
                obj = new OBJ_Shield_Blue(gp);
                break;
            case "Normal Sword":
                obj = new OBJ_Sword_Normal(gp);
                break;
            case "Wood Axe":
                obj = new OBJ_Axe(gp);
                break;
            case "Red Potion":
                obj = new OBJ_Potion_Red(gp);
                break;
        }
        
        return obj;
    }
}

// This class contains all the data we want to save
class DataStorage implements Serializable {
    // Player stats
    int level;
    int maxLife;
    int life;
    int maxMana;
    int mana;
    int strength;
    int dexterity;
    int exp;
    int nextLevelExp;
    int coin;
    
    // Player position
    int worldX;
    int worldY;
    
    // Player inventory
    String[] itemNames;
}
