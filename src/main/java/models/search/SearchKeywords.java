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

    // International
    List<String> international = new ArrayList<>();

    /**
     * NODES
     */

    // Bathroom
    List<String> bathroom = new ArrayList<>();
    List<String> wc = new ArrayList<>();

    // Conference rooms
    List<String> conference = new ArrayList<>();

    // Departments
    // Medical departments, clinics, waiting room areas
    // Podiatrist
    // Pediatrics
    List<String> department = new ArrayList<>();
    List<String> legDoctor = new ArrayList<>();
    List<String> childDoctor = new ArrayList<>();

    // Elevators
    List<String> elevator = new ArrayList<>();

    // Exits
    // Exit/entrance
    List<String> exit = new ArrayList<>();
    List<String> entrance = new ArrayList<>();
    List<String> road = new ArrayList<>();
    List<String> garage = new ArrayList<>();
    List<String> parking = new ArrayList<>();

    // Hall
    // Hallway
    List<String> hall = new ArrayList<>();

    // Info
    // Information desks, security desks, lost and found
    List<String> police = new ArrayList<>();
    List<String> security = new ArrayList<>();
    List<String> safety = new ArrayList<>();
    List<String> info = new ArrayList<>();
    List<String> information = new ArrayList<>();
    List<String> service = new ArrayList<>();

    // Labs
    // Labs, imaging centers, and medical testing areas
    List<String> lab = new ArrayList<>();
    List<String> laboratory = new ArrayList<>();

    // MRI
    List<String> mri = new ArrayList<>();

    // Cat Scan
    // Cat Scan Alternative
    List<String> catScan = new ArrayList<>();
    List<String> ctScan = new ArrayList<>();

    // Restroom
    List<String> restroom = new ArrayList<>();

    // Retl
    // Shops, food, pay phone, areas that provide non-medical services for immediate payment
    // Food
    // Drinks
    // Cafe
    // Beverage
    List<String> food = new ArrayList<>();
    List<String> drinks = new ArrayList<>();
    List<String> cafe = new ArrayList<>();
    List<String> beverage = new ArrayList<>();
    List<String> vending = new ArrayList<>();
    List<String> places = new ArrayList<>();

    // Services
    // Hospital non-medical services, interpreters, shuttles, spiritual,library, patient financial, etc.
    List<String> services = new ArrayList<>();
    List<String> atm = new ArrayList<>();
    List<String> cashier = new ArrayList<>();
    List<String> library = new ArrayList<>();
    List<String> porch = new ArrayList<>();
    List<String> room = new ArrayList<>();
    List<String> floor = new ArrayList<>();

    // Stairs
    // Staircase

    // Workt
    // Workspaces

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
            System.setProperty("wordnet.database.dir", SearchKeywords.class.getResource("/data/dictionary").getPath().replaceAll("%20", " ").substring(1));

        }

    }

    public SearchKeywords() {

        HashMap<String, Location> locationNodes = LocationTable.getLocations();

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
                    department.add(currLocation.getLongName());

                    // Orthopedics
                    if(currLocation.getNodeID().equals("ADEPT00102"))
                        legDoctor.add(currLocation.getLongName());
                    break;

                case ELEV:
                    elevator.add(currLocation.getLongName());
                    break;

                case EXIT:
                    if (currLocation.getLongName().toLowerCase().contains("exit"))
                        exit.add(currLocation.getLongName());
                    else if (currLocation.getLongName().toLowerCase().contains("entrance"))
                        entrance.add(currLocation.getLongName());
                    else if (currLocation.getLongName().toLowerCase().contains("parking"))
                        parking.add(currLocation.getLongName());
                    else if (currLocation.getLongName().toLowerCase().contains("garage"))
                        garage.add(currLocation.getLongName());
                    else if (currLocation.getLongName().toLowerCase().contains("road"))
                        road.add(currLocation.getLongName());
                    else
                        exit.add(currLocation.getLongName());

                    break;

                case HALL:
                    hall.add(currLocation.getLongName());

                    break;

                case INFO:
                    if(currLocation.getLongName().toLowerCase().contains("security")) {
                        security.add(currLocation.getLongName());
                        police.add(currLocation.getLongName());
                        safety.add(currLocation.getLongName());
                    } else if (currLocation.getLongName().toLowerCase().contains("information")
                                || currLocation.getLongName().toLowerCase().contains("info")) {
                        info.add(currLocation.getLongName());
                        information.add(currLocation.getLongName());
                    } else if (currLocation.getLongName().toLowerCase().contains("service")) {
                        service.add(currLocation.getLongName());
                    } else {
                        info.add(currLocation.getLongName());
                    }

                    break;

                case LABS:
                    lab.add(currLocation.getLongName());

                    // MRI CT Scan Imaging
                    if(currLocation.getNodeID().equals("ALABS001L2")) {
                        mri.add(currLocation.getLongName());
                        catScan.add(currLocation.getLongName());
                        ctScan.add(currLocation.getLongName());
                    }
                    break;

                case REST:
                    restroom.add(currLocation.getLongName());

                    break;

                case RETL:
                    if(currLocation.getLongName().toLowerCase().contains("cafe")) {
                        cafe.add(currLocation.getLongName());
                    } else if (currLocation.getLongName().toLowerCase().contains("vending")) {
                        vending.add(currLocation.getLongName());
                    } else {
                        places.add(currLocation.getLongName());
                    }

                    break;

                case SERV:
                    if (currLocation.getLongName().toLowerCase().contains("service")) {
                        services.add(currLocation.getLongName());
                    } else if (currLocation.getLongName().toLowerCase().contains("atm")) {
                        atm.add(currLocation.getLongName());
                    } else if (currLocation.getLongName().toLowerCase().contains("cashier")) {
                        cashier.add(currLocation.getLongName());
                    } else if (currLocation.getLongName().toLowerCase().contains("library")) {
                        library.add(currLocation.getLongName());
                    } else if (currLocation.getLongName().toLowerCase().contains("porch")) {
                        porch.add(currLocation.getLongName());
                    } else if (currLocation.getLongName().toLowerCase().contains("room")) {
                        room.add(currLocation.getLongName());
                    } else if (currLocation.getLongName().toLowerCase().contains("floor")) {
                        floor.add(currLocation.getLongName());
                    } else {
                        services.add(currLocation.getLongName());
                    }

                    break;

                case STAI:


                    break;

                case WRKT:
                    System.out.println(currLocation.getLongName());

                    break;

                default:
            }

            locationsIterator.remove();
        }

        // Add to main map
//        keys.put("food", food);
//        keys.put("drinks", drinks);
//        keys.put("cafe", cafe);
//        keys.put("beverage", beverage);
//        keys.put("cat scan", catScan);
//        keys.put("ct scan", ctScan);
//        keys.put("mri", mri);
//        keys.put("leg doctor", legDoctor);
//        keys.put("child doctor", childDoctor);
//        keys.put("police", police);
//        keys.put("security", security);
//        keys.put("safety", safety);
//        keys.put("garage", garage);
//        keys.put("parking", parking);
//        keys.put("wc", wc);
//        keys.put("atm", atm);
//        keys.put("cash", atm);
//        keys.put("entrance", entrance);
//        keys.put("international", international);

        // Nodes

        // Bathroom
        bathroom.add("wc");
        bathroom.add("restroom");
        restroom.add("bathroom");
        restroom.add("wc");
        wc.add("bathroom");
        wc.add("restroom");
        this.addSynonyms("wc", wc);
        this.addSynonyms("bathroom", bathroom);
        this.addSynonyms("restroom", restroom);
        keys.put("restroom", restroom);
        keys.put("bathroom", bathroom);
        this.addSynonymsToMap("bathroom", bathroom);

        // Conference rooms
        keys.put("conference", conference);
        this.addSynonymsToMap("conference", conference);

        // Department
        /*this.addSynonyms("mri", mri);
        catScan.add("ct scan");
        catScan.add("computer tomography");
        this.addSynonyms("cat scan", catScan);
        ctScan.add("ct scan");
        ctScan.add("cat scan");
        this.addSynonyms("ct scan", ctScan);
        legDoctor.add("podiatrist");
        this.addSynonyms("podiatrist", legDoctor);
        childDoctor.add("pediatrist");
        childDoctor.add("pediatrics");
        this.addSynonyms("pediatrics", childDoctor);*/


        // Elevators
        this.addSynonyms("elevator", elevator);
        keys.put("elevator", elevator);
        this.addSynonymsToMap("elevator", elevator);

        // Exits
        exit.add("gate");
        exit.add("egress");
        exit.add("doorway");
        exit.add("gateway");
        exit.add("portal");
        exit.add("outlet");
        exit.add("vent");
        exit.add("departure");
        exit.add("evacuation");
        this.addSynonyms("exit", exit);
        keys.put("exit", exit);
        this.addSynonymsToMap("exit", exit);

        // Labs
        lab.add("laboratory");
        laboratory.add("lab");
        this.addSynonyms("lab", lab);
        keys.put("lab", lab);
        keys.put("laboratory", laboratory);
        this.addSynonymsToMap("lab", lab);
        this.addSynonymsToMap("laboratory", laboratory);

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
