package fr.albanrecly;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    private static final File mainDir = new File(System.getProperty("user.dir"));
    private static final File rawDir = new File(System.getProperty("user.dir")+File.separator+"RAW");

    public static void main(String[] args) {
        if(mainDir.exists() && mainDir.isDirectory()){
            if(args.length > 0 && args[0].equals("setup")){
                if(setup()){
                    System.out.println("Cleaning...");
                    clean();
                }
                else{
                    System.out.println("Goodbye.");
                }
                return;
            }
            clean();
        }
    }


    private static boolean setup(){
        File[] files = mainDir.listFiles();
        if(files==null) return false;
        if(!rawDir.exists()){
            rawDir.mkdir();
        }
        for(File f : files){
            if(f.getName().endsWith(".CR2") || f.getName().endsWith(".cr2")){
                try {
                    Files.move(f.toPath(), Paths.get(rawDir.toPath().toString(), File.separator, f.getName()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Done setting up. Would you like to clean now ? (Y/N)");
        String answer = scanner.next();

        return (answer.toLowerCase().equals("y") || answer.toLowerCase().equals("yes") || answer.toLowerCase().equals("o") || answer.toLowerCase().equals("oui"));
    }



    private static void clean() {
        File[] mainFiles = mainDir.listFiles();
        if (mainFiles == null) {
            return;
        }

        ArrayList<String> okPictures = new ArrayList<String>();

        for (File file : mainFiles) {
            if (file.isFile() && (file.getName().contains(".jpg") || file.getName().contains(".JPG"))) {
                okPictures.add(file.getName().substring(0, file.getName().indexOf('.')));
            }
        }


        if (rawDir.exists() && rawDir.isDirectory()) {
            File[] rawPictures = rawDir.listFiles();
            if (rawPictures == null) {
                return;
            }

            for (File raw : rawPictures) {
                if (!raw.getName().endsWith(".CR2") && !raw.getName().endsWith(".cr2")) {
                    continue;
                }
                if (!okPictures.contains(raw.getName().substring(0, raw.getName().indexOf(".")))) {
                    raw.delete();
                } else {
                    okPictures.remove(raw.getName().substring(0, raw.getName().indexOf(".")));
                }
            }
            System.out.println("Done deleting.");
            if (okPictures.size() > 0) {
                System.out.println("Missing raw pictures : ");
                for (String missing : okPictures) {
                    System.out.println(missing);
                }
            }
        }
    }
}