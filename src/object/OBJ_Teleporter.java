package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Teleporter extends Entity {

    GamePanel gp;
    private int destinationX;
    private int destinationY;
    private String destinationMap;
    private String description;

    public OBJ_Teleporter(GamePanel gp, int destX, int destY, String destMap, String desc) {
        super(gp);
        this.gp = gp;

        name = "Teleporter";
        down1 = setup("/objects/door", gp.tileSize, gp.tileSize);
        collision = true;

        destinationX = destX;
        destinationY = destY;
        destinationMap = destMap;
        description = desc;

        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = 48;
        solidArea.height = 48;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    public void use() {
        gp.ui.addMessage("Teleporting to " + description + "...");
        gp.player.worldX = destinationX * gp.tileSize;
        gp.player.worldY = destinationY * gp.tileSize;

        // If we need to change maps
        if (destinationMap != null && !destinationMap.isEmpty()) {
            gp.currentMap = destinationMap;
            gp.tileM.loadMap(destinationMap);

            // Reset all entities for the new map
            for (int i = 0; i < gp.obj.length; i++) {
                gp.obj[i] = null;
            }

            for (int i = 0; i < gp.npc.length; i++) {
                gp.npc[i] = null;
            }

            for (int i = 0; i < gp.monster.length; i++) {
                gp.monster[i] = null;
            }

            // Set up new map
            gp.aSetter.setObject();
            gp.aSetter.setNPC();
            gp.aSetter.setMerchant();
            gp.aSetter.setMonster();
        }

        gp.playSE(4); // Use fanfare sound for teleportation
    }
}