package models.search;

import database.LocationTable;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;
import edu.smu.tspell.wordnet.impl.file.RetrievalException;
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

    boolean dictionaryAvailable = true;

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
    List<String> stairs = new ArrayList<>();

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
            System.setProperty("wordnet.database.dir", SearchKeywords.class.getResource("/data/dictionary").getPath().replaceAll("%20", " "));

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
                    keys.put(currLocation.getLongName(), bathroom);
                    break;

                case CONF:
                    conference.add(currLocation.getLongName());
                    keys.put(currLocation.getLongName(), conference);
                    break;

                case DEPT:
                    department.add(currLocation.getLongName());

                    // Orthopedics
                    if(currLocation.getNodeID().equals("ADEPT00102")) {
                        legDoctor.add(currLocation.getLongName());
                        keys.put(currLocation.getLongName(), legDoctor);
                    }

                    keys.put(currLocation.getLongName(), department);
                    break;

                case ELEV:
                    elevator.add(currLocation.getLongName());
                    keys.put(currLocation.getLongName(), elevator);
                    break;

                case EXIT:
                    if (currLocation.getLongName().toLowerCase().contains("exit")) {
                        exit.add(currLocation.getLongName());
                        keys.put(currLocation.getLongName(), exit);
                    } else if (currLocation.getLongName().toLowerCase().contains("entrance")) {
                        entrance.add(currLocation.getLongName());
                        keys.put(currLocation.getLongName(), entrance);
                    } else if (currLocation.getLongName().toLowerCase().contains("parking")) {
                        parking.add(currLocation.getLongName());
                        keys.put(currLocation.getLongName(), parking);
                    } else if (currLocation.getLongName().toLowerCase().contains("garage")) {
                        garage.add(currLocation.getLongName());
                        keys.put(currLocation.getLongName(), garage);
                    } else if (currLocation.getLongName().toLowerCase().contains("road")) {
                        road.add(currLocation.getLongName());
                        keys.put(currLocation.getLongName(), road);
                    } else {
                        exit.add(currLocation.getLongName());
                        keys.put(currLocation.getLongName(), exit);
                    }

                    break;

                case HALL:
                    hall.add(currLocation.getLongName());
                    keys.put(currLocation.getLongName(), hall);
                    break;

                case INFO:
                    if(currLocation.getLongName().toLowerCase().contains("security")) {
                        security.add(currLocation.getLongName());
                        police.add(currLocation.getLongName());
                        safety.add(currLocation.getLongName());

                        keys.put(currLocation.getLongName(), security);
                        keys.put(currLocation.getLongName(), police);
                        keys.put(currLocation.getLongName(), safety);
                    } else if (currLocation.getLongName().toLowerCase().contains("information")
                                || currLocation.getLongName().toLowerCase().contains("info")) {
                        info.add(currLocation.getLongName());
                        information.add(currLocation.getLongName());

                        keys.put(currLocation.getLongName(), info);
                        keys.put(currLocation.getLongName(), information);
                    } else if (currLocation.getLongName().toLowerCase().contains("service")) {
                        service.add(currLocation.getLongName());
                        keys.put(currLocation.getLongName(), service);
                    } else {
                        info.add(currLocation.getLongName());
                        keys.put(currLocation.getLongName(), info);
                    }

                    break;

                case LABS:
                    lab.add(currLocation.getLongName());
                    keys.put(currLocation.getLongName(), lab);

                    // MRI CT Scan Imaging
                    if(currLocation.getNodeID().equals("ALABS001L2")) {
                        mri.add(currLocation.getLongName());
                        catScan.add(currLocation.getLongName());
                        ctScan.add(currLocation.getLongName());

                        keys.put(currLocation.getLongName(), mri);
                        keys.put(currLocation.getLongName(), catScan);
                        keys.put(currLocation.getLongName(), ctScan);
                    }
                    break;

                case REST:
                    restroom.add(currLocation.getLongName());
                    keys.put(currLocation.getLongName(), restroom);
                    break;

                case RETL:
                    food.add(currLocation.getLongName());
                    if(currLocation.getLongName().toLowerCase().contains("cafe")) {
                        cafe.add(currLocation.getLongName());
                        beverage.add(currLocation.getLongName());
                        drinks.add(currLocation.getLongName());

                        keys.put(currLocation.getLongName(), cafe);
                        keys.put(currLocation.getLongName(), beverage);
                        keys.put(currLocation.getLongName(), drinks);
                    } else if (currLocation.getLongName().toLowerCase().contains("vending")) {
                        vending.add(currLocation.getLongName());
                        keys.put(currLocation.getLongName(), vending);
                    } else {
                        places.add(currLocation.getLongName());
                        keys.put(currLocation.getLongName(), places);
                    }

                    break;

                case SERV:
                    if (currLocation.getLongName().toLowerCase().contains("service")) {
                        services.add(currLocation.getLongName());
                        keys.put(currLocation.getLongName(), services);
                    } else if (currLocation.getLongName().toLowerCase().contains("atm")) {
                        atm.add(currLocation.getLongName());
                        keys.put(currLocation.getLongName(), atm);
                    } else if (currLocation.getLongName().toLowerCase().contains("cashier")) {
                        cashier.add(currLocation.getLongName());
                        keys.put(currLocation.getLongName(), cashier);
                    } else if (currLocation.getLongName().toLowerCase().contains("library")) {
                        library.add(currLocation.getLongName());
                        keys.put(currLocation.getLongName(), library);
                    } else if (currLocation.getLongName().toLowerCase().contains("porch")) {
                        porch.add(currLocation.getLongName());
                        keys.put(currLocation.getLongName(), porch);
                    } else if (currLocation.getLongName().toLowerCase().contains("room")) {
                        room.add(currLocation.getLongName());
                        keys.put(currLocation.getLongName(), room);
                    } else if (currLocation.getLongName().toLowerCase().contains("floor")) {
                        floor.add(currLocation.getLongName());
                        keys.put(currLocation.getLongName(), floor);
                    } else {
                        services.add(currLocation.getLongName());
                        keys.put(currLocation.getLongName(), services);
                    }

                    break;

                case STAI:
                    stairs.add(currLocation.getLongName());
                    keys.put(currLocation.getLongName(), stairs);

                    break;

                case WRKT:
                    System.out.println(currLocation.getLongName());

                    break;

                case WORK:
                    System.out.println(currLocation.getLongName());

                    break;

                default:
            }

            locationsIterator.remove();
        }

        // Add to main map
        /*keys.put("food", food);
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
        keys.put("international", international)*/

        // Nodes

        // Bathroom
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
        this.addSynonymsToMap("department", department);
        this.addSynonymsToMap("foot doctor", legDoctor);

        // Elevators
        /*this.addSynonyms("elevator", elevator);*/
        keys.put("elevator", elevator);
        this.addSynonymsToMap("elevator", elevator);

        // Exits
        /*exit.add("gate");
        exit.add("egress");
        exit.add("doorway");
        exit.add("gateway");
        exit.add("portal");
        exit.add("outlet");
        exit.add("vent");
        exit.add("departure");
        exit.add("evacuation");
        this.addSynonyms("exit", exit);*/
        keys.put("exit", exit);
        this.addSynonymsToMap("exit", exit);

        // Hall
        this.addSynonymsToMap("hall", hall);

        // Info
        this.addSynonymsToMap("info", info);
        this.addSynonymsToMap("information", information);

        // Labs
        /*lab.add("laboratory");
        laboratory.add("lab");
        this.addSynonyms("lab", lab);*/
//        keys.put("lab", lab);
//        keys.put("laboratory", laboratory);
        this.addSynonymsToMap("lab", lab);
        this.addSynonymsToMap("laboratory", laboratory);

        // Restroom
        this.addSynonymsToMap("restroom", restroom);

        // Retl
        this.addSynonymsToMap("restroom", restroom);

        // Services
        this.addSynonymsToMap("food", food);
        this.addSynonymsToMap("cafe", cafe);
        this.addSynonymsToMap("beverage", beverage);
        this.addSynonymsToMap("drinks", drinks);
        this.addSynonymsToMap("vending", vending);
        this.addSynonymsToMap("places", places);

        // Stairs
        this.addSynonymsToMap("stairs", stairs);

        // Workspaces

        // Wrkt
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

        try {
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

        } catch (RetrievalException e) {

            dictionaryAvailable = false;
            System.out.println("Falling back to basic search. Not adding synonyms.");

        }

    }

    public Set<String> getSynonyms(String word) {

        try {
            Set<String> synonyms = new HashSet<>();
            Synset[] synsets = database.getSynsets(word);

            for (int i = 0; i < synsets.length; i++) {
                String[] wordForms = synsets[i].getWordForms();

                for (int j = 0; j < wordForms.length; j++) {

                    String genWord = wordForms[j];

                    synonyms.add(genWord);

                }
            }

            return synonyms;

        } catch (RetrievalException e) {

            System.out.println("Cannot get synonyms. Dictionary unavailable.");

            return new HashSet<>();

        }

    }

    public boolean validEnglishWord(String word) {

        if(dictionaryAvailable)
            return database.getSynsets(word).length > 0;
        else
            return true;

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
