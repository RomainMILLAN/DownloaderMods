package fr.skytorstd.dwm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.*;

import fr.skytorstd.dwm.manager.ConsoleColor;
import fr.skytorstd.dwm.manager.downloader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class Main {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        System.out.println("______                    _                 _          ___  ___          _     \n" +
                "|  _  \\                  | |               | |         |  \\/  |         | |    \n" +
                "| | | |_____      ___ __ | | ___   __ _  __| | ___ _ __| .  . | ___   __| |___ \n" +
                "| | | / _ \\ \\ /\\ / / '_ \\| |/ _ \\ / _` |/ _` |/ _ \\ '__| |\\/| |/ _ \\ / _` / __|\n" +
                "| |/ / (_) \\ V  V /| | | | | (_) | (_| | (_| |  __/ |  | |  | | (_) | (_| \\__ \\\n" +
                "|___/ \\___/ \\_/\\_/ |_| |_|_|\\___/ \\__,_|\\__,_|\\___|_|  \\_|  |_/\\___/ \\__,_|___/" );
        System.out.println("Fait par Wabezeter & Foskcru" + "\n\n\n");


        String URLConfig = "https://romainmillan.fr/DWM/config.json";

        //CONFIG
        boolean configFolder = false;
        File ConfigJsonFile = new File("config.json");
        String folder = "";
        String modpack = "";
        while(!ConfigJsonFile.exists()){
            (new downloader(URLConfig, new File("config.json"))).run();
        }
        if(ConfigJsonFile.exists()){
            JSONParser ConfigParser = new JSONParser();
            Object ConfigObject = ConfigParser.parse(new FileReader(ConfigJsonFile));
            JSONArray ConfigJsonArray = (JSONArray)ConfigObject;
            JSONObject ConfigJsonObject = (JSONObject) ConfigJsonArray.get(0);
            folder = (String)ConfigJsonObject.get("folder");
            modpack = (String)ConfigJsonObject.get("modpacks");

            if(!folder.equalsIgnoreCase("")){
                System.out.print("Un dossier a ete trouve dans la configuration, voulez-vous l'utiliser pour telecharger les mods ? ('"+folder+"'): [Y/N] ");
                Scanner ConfigScanner = new Scanner(System. in);
                String ConfigResultat = ConfigScanner.nextLine();
                if (ConfigResultat.equalsIgnoreCase("Y")){
                    configFolder = true;
                }
            }
        }

        if(!configFolder){
            folder = getFolderPathByUser();

            JSONParser RewriteConfigJSONParser = new JSONParser();
            Object RewriteConfigJSONObject = RewriteConfigJSONParser.parse(new FileReader("config.json"));
            JSONArray RewriteConfigJSONJsonArray = (JSONArray)RewriteConfigJSONObject;
            JSONObject RewriteConfigJSONJsonObject = (JSONObject) RewriteConfigJSONJsonArray.get(0);
            RewriteConfigJSONJsonObject.put("folder", folder);
            Files.write(Paths.get("config.json"), RewriteConfigJSONJsonObject.toJSONString().getBytes());
            System.out.println("Dossier enregistre en configuration ('"+folder+"')");
        }


        //LINK JSON
        (new downloader(modpack, new File("modpacks.json"))).run();
        JSONParser ConfigModpacksParser = new JSONParser();
        Object ConfigModpackObject = ConfigModpacksParser.parse(new FileReader("modpacks.json"));
        JSONArray ConfigModPackJsonArray = (JSONArray)ConfigModpackObject;
        JSONObject ConfigModpackJsonObject = (JSONObject) ConfigModPackJsonArray.get(0);

        Map ConfigModpackAddress = ((Map)ConfigModpackJsonObject.get("modpacks"));
        Iterator<Map.Entry> itrConfigFile = ConfigModpackAddress.entrySet().iterator();

        HashMap<String, String> allNameAndLinkModPack = new HashMap<>();
        while(itrConfigFile.hasNext()){
            Map.Entry ConfigModpackPair = itrConfigFile.next();
            allNameAndLinkModPack.put((String)ConfigModpackPair.getKey(), (String)ConfigModpackPair.getValue());
        }


        String modpackSelect = "";
        do{
            System.out.print("\nSelectioner le Modpack (");
            Iterator<Map.Entry<String, String>> AllNameAndLinkModpackIterator = allNameAndLinkModPack.entrySet().iterator();
            while(AllNameAndLinkModpackIterator.hasNext()){
                Map.Entry set = AllNameAndLinkModpackIterator.next();
                System.out.print(set.getKey());
                if(AllNameAndLinkModpackIterator.hasNext()){
                    System.out.print("/");
                }
            }
            System.out.print("): ");
            Scanner ModpackSelectorScanner = new Scanner(System. in);
            modpackSelect = ModpackSelectorScanner.nextLine();
        }while (!allNameAndLinkModPack.containsKey(modpackSelect));
        String ModpackJsonURL = allNameAndLinkModPack.get(modpackSelect);

        String ModpackNameJSON = modpackSelect+".json";
        (new downloader(ModpackJsonURL, new File(ModpackNameJSON))).run();

        File folderDirectory = new File(folder);
        File[] filesToClear = folderDirectory.listFiles();
        for(File f : filesToClear){
            f.delete();
        }


        //DOWNLOAD ALL MODS
        try {
            JSONParser ModsParser = new JSONParser();
            ModsParser = new JSONParser();
            Object ModsObject = ModsParser.parse(new FileReader(ModpackNameJSON));
            JSONArray ModsJsonArray = (JSONArray)ModsObject;

            JSONObject ModsJsonObject = (JSONObject)ModsJsonArray.get(0);

            Map ModsAddress = ((Map)ModsJsonObject.get("mods"));

            Iterator<Map.Entry> ModsIterator = ModsAddress.entrySet().iterator();
            int currentModInt = 1;
            while (ModsIterator.hasNext()) {
                Map.Entry pair = ModsIterator.next();
                String nameMod = (String) pair.getKey();
                String linkModFile = "";
                if(folder.endsWith("\\")){
                    linkModFile = folder+nameMod;
                }else {
                    linkModFile = folder+"\\"+nameMod;
                }
                File modFile = new File(linkModFile);
                if (!modFile.exists()){
                    (new downloader((String) pair.getValue(), modFile)).downloadmod(ModsAddress.size(), currentModInt);
                }else {
                    System.out.println(nameMod + " est deja present dans le dossier");
                }
                currentModInt++;
            }


            System.out.println("\n\nTous les mods on etait telecharge avec succes !");
        }catch(Exception ex) {
            ex.printStackTrace();
        }


        System.out.print("Fin du programme (Appuyer sur entrer)... ");
        Scanner EndScanner = new Scanner(System. in);
        String end = EndScanner.nextLine();
    }

    private static String getFolderPathByUser(){
        System.out.print("Entrer le dossier ou telecharger les mods: ");
        Scanner FolderModsScanner = new Scanner(System. in);
        String folder = FolderModsScanner.nextLine();

        String verif = "";
        do {
            System.out.print(ConsoleColor.RED + "ATTENTION: Le dossier sera vider avant le telechargement des mods, etes-vous sure de vouloir faire ceci ? [Y/N]: " + ConsoleColor.RESET);
            Scanner FolderModsScannerVerif = new Scanner(System. in);
            verif = FolderModsScannerVerif.nextLine();
        }while(!verif.equalsIgnoreCase("Y") && !verif.equalsIgnoreCase("N"));

        if(verif.equalsIgnoreCase("Y")){
            return folder;
        }else {
            return getFolderPathByUser();
        }
    }
}
