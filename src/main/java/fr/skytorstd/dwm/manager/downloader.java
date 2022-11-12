package fr.skytorstd.dwm.manager;

import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Stream;

public class downloader {
    private String link;
    private File out;

    private String beforeString;

    public downloader(String l, File out){
        this.link = l;
        this.out = out;
        this.beforeString = "none";
    }

    public void run(){
        try{
            URL url = new URL(this.link);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            double fileSize = http.getContentLengthLong();
            BufferedInputStream in = new BufferedInputStream(http.getInputStream());
            FileOutputStream fos = new FileOutputStream(this.out);
            BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            byte[] buffer = new byte[1024];
            double downloaded = 0.00;
            int read = 0;
            double percentDownloaded = 0;

            char[] animationChars = new char[]{'|', '/', '-', '\\'};


            while((read = in.read(buffer, 0, 1024)) >= 0){
                bout.write(buffer, 0, read);
                downloaded += read;
                percentDownloaded = (downloaded*100)/fileSize;
                String percent = String.format("%.0f", percentDownloaded);

                if(!this.beforeString.contains(percent)){
                    //this.clearConsole();
                    //System.out.println("[" + this.out.getName() + "] Download at " + percent + "%");
                    System.out.print(this.out.getName() + ": " + percent + "% \r");
                    this.beforeString = percent;
                }
            }
            bout.close();
            in.close();
            //System.out.println("Download complete");
            System.out.println(this.out.getName() + ": Done!");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadmod(int nbMods, int current){
        try{
            URL url = new URL(this.link);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            double fileSize = http.getContentLengthLong();
            BufferedInputStream in = new BufferedInputStream(http.getInputStream());
            FileOutputStream fos = new FileOutputStream(this.out);
            BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            byte[] buffer = new byte[1024];
            double downloaded = 0.00;
            int read = 0;
            double percentDownloaded = 0;

            char[] animationChars = new char[]{'|', '/', '-', '\\'};


            while((read = in.read(buffer, 0, 1024)) >= 0){
                bout.write(buffer, 0, read);
                downloaded += read;
                percentDownloaded = (downloaded*100)/fileSize;
                String percent = String.format("%.0f", percentDownloaded);

                if(!this.beforeString.contains(percent)){
                    System.out.print(ConsoleColor.YELLOW + "["+current+"/"+nbMods+"] " + ConsoleColor.BLUE + this.out.getName() + ": " + ConsoleColor.PURPLE + percent + "%" + ConsoleColor.RESET + " \r");
                    this.beforeString = percent;
                }
            }
            bout.close();
            in.close();
            //System.out.println("Download complete");
            System.out.println(ConsoleColor.YELLOW + "["+current+"/"+nbMods+"] " + ConsoleColor.BLUE + this.out.getName() + ": " + ConsoleColor.GREEN + "Successful!" + ConsoleColor.RESET);
        } catch (MalformedURLException e) {
            System.out.println(ConsoleColor.YELLOW + "["+current+"/"+nbMods+"] " + ConsoleColor.BLUE + this.out.getName() + ": " + ConsoleColor.RED + "Error!" + ConsoleColor.RESET);
            //e.printStackTrace();
        } catch (IOException e) {
            System.out.println(ConsoleColor.YELLOW + "["+current+"/"+nbMods+"] " + ConsoleColor.BLUE + this.out.getName() + ": " + ConsoleColor.RED + "Error!" + ConsoleColor.RESET);
            //e.printStackTrace();
        }
    }

    private void clearConsole(){
        for(int i=0; i<100; i++){
            System.out.println(" ");
        }
    }
}
