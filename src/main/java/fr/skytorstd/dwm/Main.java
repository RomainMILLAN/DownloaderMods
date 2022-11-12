package fr.skytorstd.dwm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import fr.skytorstd.dwm.manager.downloader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class Main {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        System.out.println(" _____     ______     __     __     __   __     __         ______     ______     _____     __    __     ______     _____     ______    \n" +
                "/\\  __-.  /\\  __ \\   /\\ \\  _ \\ \\   /\\ \"-.\\ \\   /\\ \\       /\\  __ \\   /\\  __ \\   /\\  __-.  /\\ \"-./  \\   /\\  __ \\   /\\  __-.  /\\  ___\\   \n" +
                "\\ \\ \\/\\ \\ \\ \\ \\/\\ \\  \\ \\ \\/ \".\\ \\  \\ \\ \\-.  \\  \\ \\ \\____  \\ \\ \\/\\ \\  \\ \\  __ \\  \\ \\ \\/\\ \\ \\ \\ \\-./\\ \\  \\ \\ \\/\\ \\  \\ \\ \\/\\ \\ \\ \\___  \\  \n" +
                " \\ \\____-  \\ \\_____\\  \\ \\__/\".~\\_\\  \\ \\_\\\\\"\\_\\  \\ \\_____\\  \\ \\_____\\  \\ \\_\\ \\_\\  \\ \\____-  \\ \\_\\ \\ \\_\\  \\ \\_____\\  \\ \\____-  \\/\\_____\\ \n" +
                "  \\/____/   \\/_____/   \\/_/   \\/_/   \\/_/ \\/_/   \\/_____/   \\/_____/   \\/_/\\/_/   \\/____/   \\/_/  \\/_/   \\/_____/   \\/____/   \\/_____/ ");
        String modpack = "USB-C";
        System.out.println("Bienvenue dans le telecharger du modpack " + modpack + "\n\n");


        //CONFIG
        boolean configFolder = false;
        File configJSON = new File("config.json");
        String folder = "";
        if(configJSON.exists()){
            JSONParser parserConfig = new JSONParser();
            Object configObj = parserConfig.parse(new FileReader(configJSON));
            JSONArray jsonArrayConfig = (JSONArray)configObj;
            JSONObject jsonObjectConfig = (JSONObject) jsonArrayConfig.get(0);
            folder = (String)jsonObjectConfig.get("folder");

            if(!folder.equalsIgnoreCase("")){
                System.out.print("Un dossier à etait trouve dans la configuration, voulez-vous l'utiliser pour telecharger les mods ? ("+folder+"): [Y/N]");
                Scanner scannerConfig = new Scanner(System. in);
                String resConfig = scannerConfig.nextLine();
                if (resConfig.equalsIgnoreCase("Y")){
                    configFolder = true;
                }
            }
        }
        if(configFolder == false){
            System.out.print("Entrer le dossier ou telecharger les mods: ");
            Scanner scanner = new Scanner(System. in);
            folder = scanner.nextLine();
            System.out.println("Dossier enregistrer ("+folder+")");
        }


        //LINK JSON
        JSONParser parserJSONFile = new JSONParser();
        Object jsonFileObj = parserJSONFile.parse(new FileReader(configJSON));
        JSONArray jsonArrayjsonFile = (JSONArray)jsonFileObj;
        JSONObject jsonObjectjsonFile = (JSONObject) jsonArrayjsonFile.get(0);

        String urlJSON = (String) jsonObjectjsonFile.get("linkJSON");
        (new downloader(urlJSON, new File("mods.json"))).run();


        //DOWNLOAD ALL MODS
        try {
            JSONParser parser = new JSONParser();
            parser = new JSONParser();
            Object obj = parser.parse(new FileReader("mods.json"));
            JSONArray jsonarray = (JSONArray)obj;

            JSONObject jsonObject = (JSONObject)jsonarray.get(0);

            Map address = ((Map)jsonObject.get("mods"));

            Iterator<Map.Entry> itr1 = address.entrySet().iterator();
            while (itr1.hasNext()) {
                Map.Entry pair = itr1.next();
                String url = (String) pair.getValue();
                String urlArgs[] = url.split("/");
                String nameMod = urlArgs[urlArgs.length-1];
                String linkModFile = "";
                if(folder.endsWith("\\")){
                    linkModFile = folder+nameMod;
                }else {
                    linkModFile = folder+"\\"+nameMod;
                }
                File modFile = new File(linkModFile);
                if (!modFile.exists()){
                    (new downloader((String) pair.getValue(), modFile)).run();
                    System.out.println(nameMod + " downloaded");
                }else {
                    System.out.println(nameMod + " deja present dans le dossier");
                }
            }


            System.out.println("\n\nTous les mods on etait telecharger avec succes !");
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
