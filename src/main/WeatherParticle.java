package main;

import java.awt.*;
import java.util.Random;

public class WeatherParticle {

    private int x, y;
    private int speed;
    private int size;
    private Color color;
    private int alpha;
    private GamePanel gp;

    // Particle types
    public static final int PARTICLE_RAIN = 0;
    public static final int PARTICLE_SNOW = 1;
    private int type;

    public WeatherParticle(GamePanel gp, int type) {
        this.gp = gp;
        this.type = type;

        Random random = new Random();

        // Position - somewhere on screen
        x = random.nextInt(gp.screenWidth);
        y = random.nextInt(gp.screenHeight) - gp.screenHeight; // Start above screen

        if(type == PARTICLE_RAIN) {
            size = 2;
            speed = 5 + random.nextInt(5);
            color = new Color(190, 190, 220);
            alpha = 180 + random.nextInt(75);
        }
        else if(type == PARTICLE_SNOW) {
            size = 3 + random.nextInt(3);
            speed = 1 + random.nextInt(2);
            color = Color.WHITE;
            alpha = 180 + random.nextInt(75);
        }
    }

    public void update() {
        // Update position based on type
        if(type == PARTICLE_RAIN) {
            y += speed;
            x -= speed/2; // Rain falls diagonally
        }
        else if(type == PARTICLE_SNOW) {
            y += speed;

            // Snow drifts side to side
            if(new Random().nextInt(100) > 50) {
                x += new Random().nextInt(2);
            } else {
                x -= new Random().nextInt(2);
            }
        }

        // If particle goes off screen, reset it
        if(y > gp.screenHeight || x < -size || x > gp.screenWidth) {
            Random random = new Random();
            y = random.nextInt(gp.screenHeight/2) - gp.screenHeight; // Start above screen
            x = random.nextInt(gp.screenWidth);
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));

        if(type == PARTICLE_RAIN) {
            g2.drawLine(x, y, x - size, y + size*2);
        }
        else if(type == PARTICLE_SNOW) {
            g2.fillRect(x, y, size, size);
        }
    }
}