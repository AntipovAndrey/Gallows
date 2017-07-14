package gallows.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by andrey on 12.07.17.
 */
public class Dictionary {


    static ArrayList<String> dict = new ArrayList<>();

    static {
        Scanner s = null;
        try {
            s = new Scanner(new File("dictionary.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (s.hasNext()){
            dict.add(s.next());
        }
        s.close();
    }

    static String getWord() {
        return dict.get((int) (Math.random() * dict.size()));
    }
}
