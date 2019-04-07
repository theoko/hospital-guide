package models.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.smu.tspell.wordnet.*;

public class SearchKeywords {

    // Main map
    private HashMap<String, List<String>> keys = new HashMap<>();

    // Categories:

    // Food
    // Drinks
    // Cafe
    // Beverage
    List<String> food = new ArrayList<>();
    List<String> drinks = new ArrayList<>();
    List<String> cafe = new ArrayList<>();
    List<String> beverage = new ArrayList<>();

    // Lab
    // Laboratory
    List<String> lab = new ArrayList<>();
    List<String> laboratory = new ArrayList<>();

    // MRI
    List<String> mri = new ArrayList<>();

    // Cat Scan
    // Cat Scan Alternative
    List<String> catScan = new ArrayList<>();
    List<String> ctScan = new ArrayList<>();

    // Podiatrist
    // Pediatrics
    List<String> legDoctor = new ArrayList<>();
    List<String> childDoctor = new ArrayList<>();

    // Police
    // Police alternative
    List<String> police = new ArrayList<>();
    List<String> security = new ArrayList<>();
    List<String> safety = new ArrayList<>();

    // Garage
    // Garage alternative (parking)
    List<String> garage = new ArrayList<>();
    List<String> parking = new ArrayList<>();

    // Bathroom
    List<String> bathroom = new ArrayList<>();
    List<String> wc = new ArrayList<>();
    List<String> restroom = new ArrayList<>();

    // ATM
    List<String> atm = new ArrayList<>();

    // Exit
    List<String> exit = new ArrayList<>();

    // Entrance
    List<String> entrance = new ArrayList<>();

    // International
    List<String> international = new ArrayList<>();

    public SearchKeywords() {

        System.setProperty("wordnet.database.dir", SearchKeywords.class.getResource("/data/dictionary").getFile());

        food.add("au bon pain");
        food.add("restaurant");
        food.add("drinks");
        food.add("cafe");
        food.add("beverage");
        addSynonyms("food", food);

        drinks.add("au bon pain");
        drinks.add("restaurant");
        drinks.add("food");
        drinks.add("cafe");
        drinks.add("beverage");
        addSynonyms("drinks", drinks);

        cafe.add("au bon pain");
        cafe.add("restaurant");
        cafe.add("drinks");
        cafe.add("food");
        cafe.add("beverage");
        addSynonyms("cafe", cafe);

        beverage.add("au bon pain");
        beverage.add("restaurant");
        beverage.add("drinks");
        beverage.add("cafe");
        beverage.add("food");
        addSynonyms("beverage", beverage);

        lab.add("laboratory");
        laboratory.add("lab");
        addSynonyms("lab", lab);

        addSynonyms("mri", mri);

        catScan.add("ct scan");
        catScan.add("computer tomography");
        addSynonyms("cat scan", catScan);

        ctScan.add("ct scan");
        ctScan.add("cat scan");
        addSynonyms("ct scan", ctScan);

        legDoctor.add("podiatrist");
        addSynonyms("podiatrist", legDoctor);

        childDoctor.add("pediatrist");
        childDoctor.add("pediatrics");
        addSynonyms("pediatrics", childDoctor);

        police.add("security");
        police.add("safety");
        addSynonyms("police", police);

        security.add("police");
        security.add("safety");
        addSynonyms("security", security);

        safety.add("security");
        safety.add("police");
        addSynonyms("safety", safety);

        garage.add("parking");
        garage.add("parking garage");
        addSynonyms("garage", garage);

        parking.add("garage");
        parking.add("parking garage");
        addSynonyms("parking", parking);

        wc.add("bathroom");
        wc.add("restroom");
        addSynonyms("wc", wc);

        bathroom.add("wc");
        bathroom.add("restroom");
        addSynonyms("bathroom", bathroom);

        restroom.add("bathroom");
        restroom.add("wc");
        addSynonyms("restroom", restroom);

        atm.add("atm");
        atm.add("money");
        atm.add("cash");
        atm.add("hard cash");
        atm.add("deep pockets");
        addSynonyms("atm", atm);

        exit.add("gate");
        exit.add("egress");
        exit.add("doorway");
        exit.add("gateway");
        exit.add("portal");
        exit.add("outlet");
        exit.add("vent");
        exit.add("departure");
        exit.add("evacuation");
        addSynonyms("exit", exit);

        entrance.add("ingress");
        entrance.add("access");
        entrance.add("approach");
        entrance.add("door");
        entrance.add("doorway");
        entrance.add("portal");
        entrance.add("gate");
        entrance.add("lobby");
        entrance.add("entryway");
        addSynonyms("entrance", entrance);

        international.add("salida");
        international.add("ingles");


        // Add to main map
        keys.put("food", food);
        keys.put("drinks", drinks);
        keys.put("cafe", cafe);
        keys.put("beverage", beverage);
        keys.put("lab", lab);
        keys.put("laboratory", laboratory);
        keys.put("cat scan", catScan);
        keys.put("ct scan", ctScan);
        keys.put("leg doctor", legDoctor);
        keys.put("child doctor", childDoctor);
        keys.put("police", police);
        keys.put("security", security);
        keys.put("safety", safety);
        keys.put("garage", garage);
        keys.put("parking", parking);
        keys.put("wc", wc);
        keys.put("restroom", restroom);
        keys.put("bathroom", bathroom);
        keys.put("atm", atm);
        keys.put("exit", exit);
        keys.put("entrance", entrance);
        keys.put("international", international);

    }

    public HashMap<String, List<String>> getKeys() {
        return keys;
    }

    private void addSynonyms(String wordForm, List<String> keywords) {

        //  Get the synsets containing the word form
        WordNetDatabase database = WordNetDatabase.getFileInstance();
        Synset[] synsets = database.getSynsets(wordForm);

        //  Display the word forms and definitions for synsets retrieved
        if (synsets.length > 0)
        {

            for (int i = 0; i < synsets.length; i++)
            {
                String[] wordForms = synsets[i].getWordForms();

//                String[] usage = synsets[i].getUsageExamples();
//
//                for (int j = 0; j < usage.length; j++) {
//                    System.out.println(usage[j]);
//                }

                for (int j = 0; j < wordForms.length; j++)
                {
                    String genWord = wordForms[j];

//                    System.out.println(genWord);

                    if(!keywords.contains(genWord)) {
                        keywords.add(genWord);
                    }

                }
            }

        }
        else
        {
//            System.err.println("No synsets exist that contain " +
//                    "the word form '" + wordForm + "'");
        }

    }

}
