package com.company;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Playlist implements Serializable {

    @Serial
    private static final long serialVersionUID = 3208173065813083901L;

    public final ArrayList<Song> playlist;

    public Playlist() {
        playlist = new ArrayList<>();
    }

    public void addSong(Song song) {
        playlist.add(song);
    }

    public Song getSong(int index) {
        return playlist.get(index);
    }

    public int getSongIndex(Song song) {
        return playlist.indexOf(song);
    }

    public String getArtist(int index) {
        return playlist.get(index).getArtist();
    }

    public String getTitle(int index) {
        return playlist.get(index).getTitle();
    }

    public String getLength(int index) {
        return playlist.get(index).getLength();
    }


    public void deleteSong(int index) {
        playlist.remove(index);
    }

    public void shuffle() {
        Collections.shuffle(playlist);
    }

    public int getPlaylistSize() {
        return playlist.size();
    }
}