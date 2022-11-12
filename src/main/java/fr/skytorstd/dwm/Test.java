package fr.skytorstd.dwm;

import fr.skytorstd.dwm.manager.downloader;

import java.io.File;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        String urlJSON="https://romainmillan.fr/DWM/usbc.json";
        (new downloader(urlJSON, new File("mods.json"))).run();
    }
}
