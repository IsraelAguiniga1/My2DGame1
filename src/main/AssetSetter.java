package main;


import entity.NPC_Man;
import monster.MON_SQUID;
import object.OBJ_Door;
import object.OBJ_Key;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }
    public void setObject() {
        gp.obj[0] = new OBJ_Key(gp);
        gp.obj[0].worldX = gp.tileSize * 21;
        gp.obj[0].worldY = gp.tileSize * 21;

        gp.obj[1] = new OBJ_Door(gp);
        gp.obj[1].worldX = gp.tileSize * 23;
        gp.obj[1].worldY = gp.tileSize * 25;






    }
    public void setNPC() {

        gp.npc[0]= new NPC_Man(gp);
        gp.npc[0].worldX = gp.tileSize * 21;
        gp.npc[0].worldY = gp.tileSize * 21;



    }
    public void setMonster() {

        gp.monster[0] = new MON_SQUID(gp);
        gp.monster[0].worldX = gp.tileSize * 23;
        gp.monster[0].worldY = gp.tileSize * 36;

        gp.monster[1] = new MON_SQUID(gp);
        gp.monster[1].worldX = gp.tileSize * 24;
        gp.monster[1].worldY = gp.tileSize * 36;

        gp.monster[2] = new MON_SQUID(gp);
        gp.monster[2].worldX = gp.tileSize * 25;
        gp.monster[2].worldY = gp.tileSize * 36;







    }
}
