package object;

import entity.Entity;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Key extends Entity {


    public OBJ_Key(GamePanel gp){
        super(gp);

        name = "Key";
        down1 = setup("/objects/key");





    }

}
