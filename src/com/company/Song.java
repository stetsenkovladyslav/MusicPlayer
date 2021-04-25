package com.company;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;

public class Song implements Serializable {

    @Serial
    private static final long serialVersionUID = -4403483325971577703L;

    private String artist;
    private String title;
    private String length;
    private String filePath;
    transient private AudioInputStream audioInputStream;
    transient private Clip clip;

    public void setSongPath(String path) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        this.filePath = path;
        init();
    }

    public void init() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
    }

    public Clip getClip() {
        return clip;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) throws LengthException {
        if (length.matches("(\\d.*):(\\d.*)")) {
            this.length = length;
        } else {
            throw new LengthException();
        }
    }
}