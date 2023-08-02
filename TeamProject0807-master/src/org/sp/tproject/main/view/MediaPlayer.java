package org.sp.tproject.main.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.ExceptionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.images.Artwork;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class MediaPlayer extends JPanel implements ActionListener {
    JLabel info = new JLabel("BBOMODORO Music Player");
    JLabel albumCoverLabel = new JLabel(); // To display album cover image
    JPanel songInfoPanel = new JPanel(new GridLayout(3, 1)); // Panel to display song information
    JButton addButton = new JButton("Add Music");
    JButton playButton = new JButton("Play");
    JButton stopButton = new JButton("Stop"); // Add the stop button
    JLabel currentSongLabel = new JLabel("Current Song: ");
    Font customFont = new Font("", Font.BOLD, 20);
    DefaultListModel<String> playlistModel = new DefaultListModel<>();
    JList<String> playlist = new JList<>(playlistModel);
    JScrollPane playlistScrollPane = new JScrollPane(playlist);
    JFileChooser browser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter("MP3 Sound", "mp3");
    int returnValue;
    String[] musics = new String[10];
    int index = 0;
    File selectedFile;
    File sound;
    Player player; // Player 객체를 멤버 변수로 선언합니다.
    boolean isPlaying = false; // Track whether the player is currently playing or paused

    MediaPlayer() {
        this.setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        addButton.addActionListener(this);
        playButton.addActionListener(this);
        stopButton.addActionListener(this); // Add action listener to the stop button

        info.setFont(new Font("", Font.ITALIC, 7));
        add(info, BorderLayout.PAGE_END);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.PINK);
        buttonPanel.add(stopButton);
        buttonPanel.add(addButton);
        buttonPanel.add(playButton);
        add(buttonPanel, BorderLayout.PAGE_START);

        addButton.setFont(customFont);
        addButton.setBackground(Color.BLACK);
        addButton.setForeground(Color.YELLOW);
        add(addButton, BorderLayout.LINE_START);

        playButton.setFont(customFont);
        playButton.setBackground(Color.BLACK);
        playButton.setForeground(Color.GREEN);
        add(playButton, BorderLayout.CENTER);

        stopButton.setFont(customFont);
        stopButton.setBackground(Color.BLACK);
        stopButton.setForeground(Color.RED);
        add(stopButton, BorderLayout.LINE_END);

        songInfoPanel.setBackground(Color.PINK);
        songInfoPanel.add(currentSongLabel);
        songInfoPanel.add(albumCoverLabel);
        add(songInfoPanel, BorderLayout.CENTER);

        playlist.setBackground(Color.BLACK);
        playlist.setForeground(Color.CYAN);
        playlistScrollPane.setPreferredSize(new Dimension(370, 200));
        playlist.setListData(musics);
        add(playlistScrollPane, BorderLayout.EAST);

        browser.setFileFilter(filter);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == addButton) {
            returnValue = browser.showOpenDialog(this);

            if (returnValue == browser.APPROVE_OPTION) {
                selectedFile = browser.getSelectedFile();
                musics[index] = selectedFile.toString();
                playlistModel.addElement("Song - " + index);

                // Java ID3 Tag 라이브러리를 이용하여 MP3 파일 정보를 추출하여 플레이리스트 아래에 표시
                displayMP3Info(selectedFile);

                index++;
            }
        } else if (ae.getSource() == playButton) {
            if (!isPlaying) {
                // Play action
                int selectedIndex = playlist.getSelectedIndex();
                if (selectedIndex >= 0 && selectedIndex < playlistModel.getSize()) {
                    try {
                        if (player != null) {
                            // 재생 중이던 음악이 있으면 멈춥니다.
                            player.close();
                        }
                        sound = new File(musics[selectedIndex]);
                        FileInputStream fis = new FileInputStream(sound);
                        player = new Player(fis);
                        // 오디오를 무한 반복하도록 설정합니다.
                        new Thread(() -> {
                            try {
                                while (true) {
                                    currentSongLabel.setText("Current Song: " + sound.getName());
                                    albumCoverLabel.setIcon(new ImageIcon("YOUR_ALBUM_COVER_IMAGE_PATH")); // Set album cover image path
                                    player.play();
                                    fis.getChannel().position(0); // 파일의 처음으로 돌아갑니다.
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }).start();
                        isPlaying = true;
                        playButton.setText("Pause");
                    } catch (JavaLayerException | FileNotFoundException e) {
                        JOptionPane.showMessageDialog(null, e);
                    }
                }
            } else {
                // Pause action
                if (player != null) {
                    player.close();
                    currentSongLabel.setText("Current Song: ");
                }
                isPlaying = false;
                playButton.setText("Play");
            }
        } else if (ae.getSource() == stopButton) {
            // Stop action
            if (player != null) {
                player.close();
                currentSongLabel.setText("Current Song: ");
            }
            isPlaying = false;
            playButton.setText("Play");
        }
    }

    // Java ID3 Tag 라이브러리를 이용하여 MP3 파일 정보를 추출하여 플레이리스트 아래에 표시
    private void displayMP3Info(File file) {
    	try {
            AudioFile audioFile = AudioFileIO.read(file);
            Tag tag = audioFile.getTag();
            MP3AudioHeader audioHeader = (MP3AudioHeader) audioFile.getAudioHeader();

            String title = tag.getFirst(FieldKey.TITLE);
            String artist = tag.getFirst(FieldKey.ARTIST);
            String album = tag.getFirst(FieldKey.ALBUM);
            String duration = formatDuration(audioHeader.getTrackLength());

            String info = "Title: " + title + ", Artist: " + artist + ", Album: " + album + ", Duration: " + duration;
            currentSongLabel.setText("Current Song: " + info);

            // Get the album cover image and set it to the label
            try {
                Artwork artwork = tag.getFirstArtwork();
                if (artwork != null) {
                    byte[] imageData = artwork.getBinaryData();
                    ImageIcon albumCover = new ImageIcon(imageData);
                    albumCoverLabel.setIcon(albumCover);
                } else {
                    // If no artwork is found, set the albumCoverLabel to null
                    albumCoverLabel.setIcon(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method to format duration in mm:ss format
    private String formatDuration(int durationInSeconds) {
        int minutes = durationInSeconds / 60;
        int seconds = durationInSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("BBOMODORO Music Player");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new MediaPlayer());
            frame.pack();
            frame.setVisible(true);
        });
    }
}
