package models.search;

import database.LocationTable;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;
import helpers.FileHelpers;
import models.map.Location;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class SearchKeywords {

    // Main map
    private HashMap<String, List<String>> keys = new HashMap<>();

    // WordNet Database
    WordNetDatabase database = WordNetDatabase.getFileInstance();

    // Categories:

    public List<String> getFood() {
        return food;
    }

    public List<String> getDrinks() {
        return drinks;
    }

    public List<String> getCafe() {
        return cafe;
    }

    public List<String> getBeverage() {
        return beverage;
    }

    public List<String> getLab() {
        return lab;
    }

    public List<String> getLaboratory() {
        return laboratory;
    }

    public List<String> getMri() {
        return mri;
    }

    public List<String> getCatScan() {
        return catScan;
    }

    public List<String> getCtScan() {
        return ctScan;
    }

    public List<String> getLegDoctor() {
        return legDoctor;
    }

    public List<String> getChildDoctor() {
        return childDoctor;
    }

    public List<String> getPolice() {
        return police;
    }

    public List<String> getSecurity() {
        return security;
    }

    public List<String> getSafety() {
        return safety;
    }

    public List<String> getGarage() {
        return garage;
    }

    public List<String> getParking() {
        return parking;
    }

    public List<String> getBathroom() {
        return bathroom;
    }

    public List<String> getWc() {
        return wc;
    }

    public List<String> getRestroom() {
        return restroom;
    }

    public List<String> getAtm() {
        return atm;
    }

    public List<String> getExit() {
        return exit;
    }

    public List<String> getEntrance() {
        return entrance;
    }

    public List<String> getInternational() {
        return international;
    }

    public List<String> getConference() {
        return conference;
    }

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

    // Nodes
    List<String> conference = new ArrayList<>();

    public static void initialize() {

        if (FileHelpers.checkJar()) {

            // Path to dictionary directory
            String pathToWordnet = "/data/dictionary.zip";

            try {

                // Copy files to filesystem if necessary
                String dictionary;
                dictionary = FileHelpers.extractFolder(pathToWordnet, "dictionary.zip");

                String dictionaryPath = FileHelpers.unzipFile(dictionary, "dictionary");

                // Point to dictionary location
                System.setProperty("wordnet.database.dir", dictionaryPath);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {

            // Point to dictionary location
            System.setProperty("wordnet.database.dir", SearchKeywords.class.getResource("/data/dictionary").getFile());

        }

    }

    public SearchKeywords() {

        HashMap<String, Location> locationNodes = LocationTable.getLocations();

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
        international.add("hola");
        international.add("domingo");
        international.add("zapato");
        international.add("mano");

        // Nodes

        Iterator locationsIterator = locationNodes.entrySet().iterator();

        while (locationsIterator.hasNext()) {

            Map.Entry pair = (Map.Entry) locationsIterator.next();

            Location currLocation = (Location) pair.getValue();

            switch (currLocation.getNodeType()) {
                case BATH:
                    bathroom.add(currLocation.getLongName());
                    break;
                case CONF:
                    conference.add(currLocation.getLongName());
                    break;
                case DEPT:

                    break;
                case ELEV:

                    break;
                case EXIT:
                    exit.add(currLocation.getLongName());
                    break;
                case HALL:

                    break;
                case INFO:

                    break;
                case LABS:
                    lab.add(currLocation.getLongName());
                    break;
                case REST:

                    break;
                case RETL:

                    break;
                case SERV:

                    break;
                case STAI:

                    break;
                case WORK:

                    break;
                case WRKT:

                    break;
                default:
            }

            locationsIterator.remove();
        }

        // Add to main map
        keys.put("food", food);
        keys.put("drinks", drinks);
        keys.put("cafe", cafe);
        keys.put("beverage", beverage);
        keys.put("cat scan", catScan);
        keys.put("ct scan", ctScan);
        keys.put("mri", mri);
        keys.put("leg doctor", legDoctor);
        keys.put("child doctor", childDoctor);
        keys.put("police", police);
        keys.put("security", security);
        keys.put("safety", safety);
        keys.put("garage", garage);
        keys.put("parking", parking);
        keys.put("wc", wc);
        keys.put("atm", atm);
        keys.put("cash", atm);
        keys.put("entrance", entrance);
        keys.put("international", international);

        // Nodes
        // Conference rooms
        keys.put("conference", conference);
        this.addSynonymsToMap("conference", conference);

        // Labs
        keys.put("lab", lab);
        keys.put("laboratory", laboratory);
        this.addSynonymsToMap("lab", lab);

        // Bathroom
        keys.put("restroom", restroom);
        keys.put("bathroom", bathroom);
        this.addSynonymsToMap("bathroom", bathroom);

        // Exits
        keys.put("exit", exit);
        this.addSynonymsToMap("exit", exit);

    }

    public HashMap<String, List<String>> getKeys() {
        return keys;
    }

    private void addSynonymsToMap(String word, List<String> wordlist) {
        for(String synonym : this.getSynonyms(word)) {
            keys.put(synonym, wordlist);
        }
    }

    private void addSynonyms(String wordForm, List<String> keywords) {

        //  Get the synsets containing the word form
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

    public Set<String> getSynonyms(String word) {

        Set<String> synonyms = new HashSet<>();
        Synset[] synsets = database.getSynsets(word);

        for (int i = 0; i < synsets.length; i++)
        {
            String[] wordForms = synsets[i].getWordForms();

            for (int j = 0; j < wordForms.length; j++)
            {

                String genWord = wordForms[j];

                synonyms.add(genWord);

            }
        }

        return synonyms;

    }

    public boolean validEnglishWord(String word) {

        return database.getSynsets(word).length > 0;

    }

    private boolean partiallyContains(List<String> words, String word) {

        for(int i=0; i<words.size(); i++) {

            System.out.println("This string: " + words.get(i) + ", Detect: " + word);
            if(Pattern.compile(Pattern.quote(words.get(i)), Pattern.CASE_INSENSITIVE).matcher(word).find()) {
                System.out.println("Contained: " + i + ": " + words.get(i));
                return true;
            }

//            System.out.println(words.get(i));
//            System.out.println("Contains? " + word);
//            if(words.get(i).contains(word)) {
//
//            }

        }

        return false;

    }

}
