package fr.gsb.rv.visiteur.technique;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Ip {
    public static String ip = "";
    public static String getIp(){
        try{
            File file = new File("/home/theophile/Documents/coursTS2SIO/ANDROID/GSB-RV/GSB-RV-Visiteur/" + "local.properties");
            Properties p = new Properties();
            FileInputStream fip = new FileInputStream(file);
            p.load(fip);
            ip = p.getProperty("ip");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ip;
    }
}
