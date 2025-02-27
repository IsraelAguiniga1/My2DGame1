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

    // Weather states
    private final int WEATHER_CLEAR = 0;
    private final int WEATHER_RAIN = 1;
    private final int WEATHER_SNOW = 2;
    private int weather = WEATHER_CLEAR;
    private int weatherTimer = 0;
    private final int weatherDuration = 7200; // Weather changes every 2 minutes

    // Particles
    private WeatherParticle[] particles;
    private final int numParticles = 100;

    public EnvironmentManager(GamePanel gp) {
        this.gp = gp;

        // Initialize particles array
        particles = new WeatherParticle[numParticles];
    }

    // Method to change weather
    private void setupWeather(int weatherType) {
        weather = weatherType;

        // Setup weather particles
        if(weather != WEATHER_CLEAR) {
            for(int i = 0; i < numParticles; i++) {
                if(weather == WEATHER_RAIN) {
                    particles[i] = new WeatherParticle(gp, WeatherParticle.PARTICLE_RAIN);
                }
                else if(weather == WEATHER_SNOW) {
                    particles[i] = new WeatherParticle(gp, WeatherParticle.PARTICLE_SNOW);
                }
            }
        }
    }

    public void update() {
        // Update day/night cycle
        dayCounter++;

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

        // Update weather
        weatherTimer++;
        if(weatherTimer >= weatherDuration) {
            weatherTimer = 0;

            // Random weather change
            int chance = new Random().nextInt(100);

            if(chance < 60) {
                setupWeather(WEATHER_CLEAR); // 60% chance of clear weather
            }
            else if(chance < 90) {
                setupWeather(WEATHER_RAIN); // 30% chance of rain
            }
            else {
                setupWeather(WEATHER_SNOW); // 10% chance of snow
            }
        }

        // Update particles
        if(weather != WEATHER_CLEAR) {
            for(int i = 0; i < numParticles; i++) {
                if(particles[i] != null) {
                    particles[i].update();
                }
            }
        }
    }

    public void draw(Graphics2D g2) {
        // Draw day/night filter
        if(filterAlpha > 0f) {
            // Change environment color based on time
            Color filterColor = new Color(0, 0, 0.1f, filterAlpha);

            g2.setColor(filterColor);
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        }

        // Draw weather particles
        if(weather != WEATHER_CLEAR) {
            for(int i = 0; i < numParticles; i++) {
                if(particles[i] != null) {
                    particles[i].draw(g2);
                }
            }
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

    // Helper methods
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
}