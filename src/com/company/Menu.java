package com.company;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.util.Scanner;

public class Menu {
    private final Scanner scanner = new Scanner(System.in);
    private Playlist playlist;
    private Song currentSong;
    private String status;

    public Menu() {
        this.playlist = new Playlist();
    }

    public int showMenu() {
        int select;

        System.out.print("""
                |  Welcome to the Music Player |
                Select operation:
                1) View Playlist
                2) Add song
                3) Delete song
                4) Shuffle Playlist
                5) Start
                6) Pause
                7) Resume
                8) Restart
                9) Stop
                10) Next song
                11) Previous song""".indent(1));

        select = scanner.nextInt();
        return select;
    }

    public void runMenu() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        int select = showMenu();

        switch (select) {
            case 1 -> {
                viewPlaylist();
                savePlaylist();
                runMenu();
            }
            case 2 -> {
                addSong();
                savePlaylist();
                runMenu();
            }
            case 3 -> {
                deleteSong();
                savePlaylist();
                runMenu();
            }
            case 4 -> {
                shufflePlaylist();
                savePlaylist();
                runMenu();
            }
            case 5 -> {
                play();
                runMenu();
            }
            case 6 -> {
                pause();
                runMenu();
            }
            case 7 -> {
                resume();
                runMenu();
            }
            case 8 -> {
                restart();
                runMenu();
            }
            case 9 -> {
                stop();
                runMenu();
            }
            case 10 -> {
                nextSong();
                runMenu();
            }
            case 11 -> {
                previousSong();
                runMenu();
            }
        }

    }

    public void addSong() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        Song newSong = new Song();

        newSong.setArtist(scanner.nextLine());
        System.out.print("Enter artist: ");
        newSong.setArtist(scanner.nextLine());
        System.out.print("Enter title: ");
        newSong.setTitle(scanner.nextLine());
        System.out.print("Enter file path: ");
        newSong.setSongPath(scanner.nextLine());
        do {
            try {
                System.out.print("Enter length: ");
                newSong.setLength(scanner.next());
            } catch (LengthException invalidLength) {
                System.out.print(invalidLength.toString());
                System.out.print("Enter length as \"minutes:seconds\".\n");
            }
        } while (newSong.getLength() == null);

        playlist.addSong(newSong);
    }

    public void deleteSong() {
        if (playlist.getPlaylistSize() == 0) {
            System.out.println("\nPlaylist is empty!");
        } else {
            viewPlaylist();
            boolean flag = false;
            do {
                System.out.print("\nSelect a song to remove: ");
                int removeSelection = scanner.nextInt();

                if (removeSelection < playlist.getPlaylistSize() || removeSelection > playlist.getPlaylistSize()) {
                    System.out.print("That is not a valid selection!\n");
                } else {
                    playlist.deleteSong(--removeSelection);
                    flag = true;
                }
            } while (!flag);
        }
    }

    public void viewPlaylist() {
        if (playlist.getPlaylistSize() == 0) {
            System.out.println("\nPlaylist empty!");
        } else {
            for (int i = 0; i < playlist.getPlaylistSize(); i++) {
                System.out.print("\n#" + (i + 1) + " ");
                System.out.print("" + playlist.getArtist(i) + " - ");
                System.out.print("\"" + playlist.getTitle(i) + "\"" + ", ");
                System.out.print(playlist.getLength(i) + ", ");

            }
            System.out.print("\n");
        }
    }

    public void savePlaylist() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("playlist.txt");
            ObjectOutputStream outObjectStream = new ObjectOutputStream(fileOutputStream);
            outObjectStream.writeObject(playlist);
            outObjectStream.flush();
            outObjectStream.close();
        } catch (FileNotFoundException fnfException) {
            System.out.println("No file");
        } catch (IOException ioException) {
            System.out.println("bad IO");
        }

    }

    public void loadPlaylist() {
        try {
            FileInputStream fileInputStream = new FileInputStream("playlist.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            playlist = (Playlist) objectInputStream.readObject();
            objectInputStream.close();
        } catch (FileNotFoundException fnfException) {
            System.out.println("No File");
        } catch (IOException ioException) {
            ioException.printStackTrace();
            System.out.println("IO no good");
        } catch (ClassNotFoundException cnfException) {
            System.out.println("This is not a Playlist.");
        }
    }

    public void play() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (currentSong == null) {
            currentSong = playlist.getSong(0);
        }
        currentSong.init();
        currentSong.getClip().loop(Clip.LOOP_CONTINUOUSLY);
        status = "play";
    }

    public void pause() {
        if (status.equals("paused")) {
            System.out.println("audio is already paused");
            return;
        }
        currentSong.getClip().stop();
        status = "paused";
    }

    public void resume() throws UnsupportedAudioFileException,
            IOException, LineUnavailableException {
        if (status.equals("play")) {
            System.out.println("audio is already being played");
            return;
        }
        this.play();
    }

    public void restart() throws IOException, LineUnavailableException,
            UnsupportedAudioFileException {
        stop();
        play();
    }

    public void stop() {
        currentSong.getClip().stop();
        currentSong.getClip().setMicrosecondPosition(0);
    }

    public void nextSong() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        stop();
        status = "paused";
        int currentSongIndex = playlist.getSongIndex(currentSong);
        currentSong = (playlist.getPlaylistSize() == currentSongIndex + 1)
                ? playlist.getSong(0)
                : playlist.getSong(currentSongIndex + 1);
        play();
    }

    public void previousSong() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        stop();
        status = "paused";
        int currentSongIndex = playlist.getSongIndex(currentSong);
        currentSong = (currentSongIndex == 0)
                ? playlist.getSong(playlist.getPlaylistSize() - 1)
                : playlist.getSong(currentSongIndex - 1);
        play();
    }

    public void shufflePlaylist() {
        playlist.shuffle();
        currentSong = playlist.getSong(0);
    }
}