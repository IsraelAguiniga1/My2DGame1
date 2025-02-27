package main;

import java.awt.*;
import java.util.Random;

public class EnvironmentManager {

    GamePanel gp;

    // Lighting control
    private int dayCounter;
    private final int dayDuration = 3600; // 1 minute in game time = 1 hour
    private float filterAlpha = 0f; // 0 - day, 1 - night

    // Day cycle state
    private final int dayState = 0;
    private final int duskState = 1;
    private final int nightState = 2;
    private final int dawnState = 3;
    private int environmentState = dayState;

    private final int WEATHER_CLEAR = 0;
    private final int WEATHER_RAIN = 1;
    private final int WEATHER_SNOW = 2;
    private int weather = WEATHER_CLEAR;

    public EnvironmentManager(GamePanel gp) {
        this.gp = gp;

        // We'll initialize the particles array when a weather condition is active
        // but create the array now
        particles = new WeatherParticle[0]; // Will be resized when needed
    }

    public void update() {
        dayCounter++;

        // 24 hour day cycle
        if(dayCounter > dayDuration) {
            dayCounter = 0;
        }

        // Calculate day phase based on counter
        if(dayCounter < dayDuration/4) { // 6h - day
            environmentState = dayState;
            filterAlpha = 0f;
        }
        else if(dayCounter < dayDuration/3) { // 2h - dusk transition
            environmentState = duskState;

            // Gradually increase darkness
            filterAlpha += 0.001f;
            if(filterAlpha > 0.6f) {
                filterAlpha = 0.6f;
            }
        }
        else if(dayCounter < 2 * dayDuration/3) { // 8h - night
            environmentState = nightState;
            filterAlpha = 0.6f;
        }
        else { // 8h - dawn transition
            environmentState = dawnState;

            // Gradually decrease darkness
            filterAlpha -= 0.001f;
            if(filterAlpha < 0f) {
                filterAlpha = 0f;
            }
        }
    }

    public void draw(Graphics2D g2) {
        if(filterAlpha > 0f) {
            // Change environment color based on time
            Color filterColor = new Color(0, 0, 0.1f, filterAlpha);

            g2.setColor(filterColor);
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        }
    }

    public boolean isNight() {
        return environmentState == nightState;
    }

    public boolean isDusk() {
        return environmentState == duskState;
    }

    public boolean isDawn() {
        return environmentState == dawnState;
    }

    public boolean isDay() {
        return environmentState == dayState;
    }
    // Add these methods to your EnvironmentManager class if they're missing:

    // Getter for day state as string
    public String getDayStateName() {
        switch(environmentState) {
            case dayState: return "Day";
            case duskState: return "Dusk";
            case nightState: return "Night";
            case dawnState: return "Dawn";
            default: return "Unknown";
        }
    }

    // Getter for weather
    public String getWeatherName() {
        switch(weather) {
            case WEATHER_CLEAR: return "Clear";
            case WEATHER_RAIN: return "Rainy";
            case WEATHER_SNOW: return "Snowy";
            default: return "Unknown";
        }
    }
}