package src;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class main{

    public static void main(String[] args){


        File outFolder = new File("out");
        if(!outFolder.exists()){
            outFolder.mkdirs();
        }

        File langFolder = new File("out/lang");
        if(!langFolder.exists()){
            langFolder.mkdirs();
        }


        List<String> wordlist = new ArrayList<>();
        List<String> deflist = new ArrayList<>();
        List<String> blankOpts = new ArrayList<>();

        File words = new File("res/wordlist.txt");
        File def = new File("res/default.txt");        
        File blank = new File("res/blank.txt");

        try {
            wordlist = Files.readAllLines(words.toPath());
            deflist = Files.readAllLines(def.toPath());
            blankOpts = Files.readAllLines(blank.toPath());

        } catch (IOException e) {
            e.printStackTrace();
        }

        Random rand = new Random();
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter("out/lang/en_US.lang"));
            for (int i = 0; i < blankOpts.size(); i++) {
                int wordcount = 0;
                if(deflist.get(i).contains("=")){
                    String[] split = deflist.get(i).split("=");
                    if(split.length == 1){
                        wordcount = 3;
                    }else{
                        String[] split2 = split[1].split(" ");
                        wordcount = split2.length;
                    }
                }
                if(blankOpts.get(i).endsWith("=")){
                    String wString = blankOpts.get(i);
                    for (int j = 0; j < wordcount; j++) {
                        String appendString = wordlist.get(rand.nextInt(wordlist.size())) + " ";
                        appendString = appendString.substring(0,1).toUpperCase() + appendString.substring(1);
                        wString += appendString;
                    }
                    wString += "\n";
                    writer.write(wString);
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        try {
            File f = new File("out/lang_replace.zip");
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(f));
            ZipEntry e = new ZipEntry("lang/en_US.lang");
            out.putNextEntry(e);
            FileInputStream fis = new FileInputStream("out/lang/en_US.lang");

            byte[] bytes = new byte[1024];
        int length;

        while((length = fis.read(bytes)) >= 0) {
            out.write(bytes, 0, length);
        }

        out.close();
        fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Successfully generated new zip lang file.");
    }
}
