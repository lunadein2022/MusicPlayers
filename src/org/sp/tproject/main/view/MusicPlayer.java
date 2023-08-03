package org.sp.tproject.main.view;
import javax.swing.*;
import java.awt.*;

public class MusicPlayer extends JFrame {
    private JPanel albumCoverPanel;
    private JPanel playlistPanel;
    private JPanel controlPanel;
    private JButton prevButton;
    private JButton playPauseButton;
    private JButton nextButton;
    private JButton addButton;

    public MusicPlayer() {
        // Set frame properties
        setTitle("Music Player");
        setSize(370, 400);
        setLayout(new BorderLayout());

        // Create panels for album cover and playlist
        albumCoverPanel = new JPanel();
        playlistPanel = new JPanel();
        albumCoverPanel.setPreferredSize(new Dimension(370, 200));
        playlistPanel.setPreferredSize(new Dimension(370, 200));

        // Create control panel for buttons
        controlPanel = new JPanel();
        prevButton = new JButton("Previous");
        playPauseButton = new JButton("Play/Pause");
        nextButton = new JButton("Next");
        addButton = new JButton("Add Song");

        // Add buttons to the control panel
        controlPanel.add(prevButton);
        controlPanel.add(playPauseButton);
        controlPanel.add(nextButton);
        controlPanel.add(addButton);

        // Add panels to the main frame
        add(albumCoverPanel, BorderLayout.WEST);
        add(playlistPanel, BorderLayout.EAST);
        add(controlPanel, BorderLayout.SOUTH);

        // Set the frame visible
        setVisible(true);
    }

    public static void main(String[] args) {
        new MusicPlayer();
    }
}
