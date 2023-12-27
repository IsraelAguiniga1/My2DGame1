package main;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");


        JFrame frame = new JFrame();// Create a new JFrame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Set default close operation
        frame.setResizable(false);// Disable resizing
        frame.setTitle("2D Game");// Set title

        GamePanel gamePanel = new GamePanel();// Create a new GamePanel
        frame.add(gamePanel);// Add the GamePanel to the JFrame

        frame.pack();// Pack the JFrame

        frame.setLocationRelativeTo(null);// Center the JFrame
        frame.setVisible(true);// Make the JFrame visible


        gamePanel.setupGame();
        gamePanel.startGameThread();// Start the game loop
    }
}