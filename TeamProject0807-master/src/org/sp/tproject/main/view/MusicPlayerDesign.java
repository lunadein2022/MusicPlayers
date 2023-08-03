package org.sp.tproject.main.view;

import javax.swing.*;
import java.awt.*;

public class MusicPlayerDesign extends JPanel {
    private JPanel albumCoverPanel;
    private JPanel playlistPanel;
    private JPanel controlPanel;
    private JButton prevButton;
    private JButton playPauseButton;
    private JButton nextButton;
    private JButton addButton;

    public MusicPlayerDesign() {
        // Set panel properties
        setPreferredSize(new Dimension(370, 400));
        setLayout(new BorderLayout());

        // Create panels for album cover and playlist
        albumCoverPanel = new JPanel();
        playlistPanel = new JPanel();
        albumCoverPanel.setPreferredSize(new Dimension(300, 300));
        playlistPanel.setPreferredSize(new Dimension(370, 50));

        // Create control panel for buttons
        controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS)); // Set BoxLayout to vertical
        prevButton = new JButton("Previous");
        playPauseButton = new JButton("Play/Pause");
        nextButton = new JButton("Next");
        addButton = new JButton("Add Song");

        // Add buttons to the control panel
        controlPanel.add(prevButton);
        controlPanel.add(playPauseButton);
        controlPanel.add(nextButton);
        controlPanel.add(addButton);

        // Add panels to the main panel
        add(albumCoverPanel, BorderLayout.WEST);
        add(playlistPanel, BorderLayout.SOUTH);
        add(controlPanel, BorderLayout.EAST);
    }
}
