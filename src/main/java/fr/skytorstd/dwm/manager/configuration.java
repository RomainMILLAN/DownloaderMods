package fr.skytorstd.dwm.manager;

import io.github.cdimascio.dotenv.Dotenv;

public class configuration {

    private static final Dotenv dotenv = Dotenv.load();

    public static String get(String key){
        return dotenv.get(key);
    }
}
