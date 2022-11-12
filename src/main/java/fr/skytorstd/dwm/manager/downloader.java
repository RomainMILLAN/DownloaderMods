package fr.skytorstd.dwm.manager;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
            while((read = in.read(buffer, 0, 1024)) >= 0){
                bout.write(buffer, 0, read);
                downloaded += read;
                percentDownloaded = (downloaded*100)/fileSize;
                String percent = String.format("%.0f", percentDownloaded);

                if(!this.beforeString.contains(percent)){
                    System.out.println("[" + this.out.getName() + "] Download at " + percent + "%");
                    this.beforeString = percent;
                }
            }
            bout.close();
            in.close();
            System.out.println("Download complete");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
