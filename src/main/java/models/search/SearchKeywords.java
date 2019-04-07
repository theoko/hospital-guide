package models.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchKeywords {

    // Main map
    public static HashMap<String, List<String>> keys;

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

        food.add("au bon pain");
        food.add("restaurant");
        food.add("drinks");
        food.add("cafe");
        food.add("beverage");

        drinks.add("au bon pain");
        drinks.add("restaurant");
        drinks.add("food");
        drinks.add("cafe");
        drinks.add("beverage");

        cafe.add("au bon pain");
        cafe.add("restaurant");
        cafe.add("drinks");
        cafe.add("food");
        cafe.add("beverage");

        beverage.add("au bon pain");
        beverage.add("restaurant");
        beverage.add("drinks");
        beverage.add("cafe");
        beverage.add("food");

        lab.add("laboratory");
        laboratory.add("lab");

        catScan.add("ct scan");
        catScan.add("computer tomography");

        ctScan.add("ct scan");
        ctScan.add("cat scan");

        legDoctor.add("podiatrist");

        childDoctor.add("pediatrist");
        childDoctor.add("pediatrics");

        police.add("security");
        police.add("safety");

        security.add("police");
        security.add("safety");

        safety.add("security");
        safety.add("police");

        garage.add("parking");
        garage.add("parking garage");

        parking.add("garage");
        parking.add("parking garage");

        wc.add("bathroom");
        wc.add("restroom");

        bathroom.add("wc");
        bathroom.add("restroom");

        restroom.add("bathroom");
        restroom.add("wc");

        atm.add("atm");
        atm.add("money");
        atm.add("cash");
        atm.add("hard cash");
        atm.add("deep pockets");

        exit.add("gate");
        exit.add("egress");
        exit.add("doorway");
        exit.add("gateway");
        exit.add("portal");
        exit.add("outlet");
        exit.add("vent");
        exit.add("departure");
        exit.add("evacuation");

        entrance.add("ingress");
        entrance.add("access");
        entrance.add("approach");
        entrance.add("door");
        entrance.add("doorway");
        entrance.add("portal");
        entrance.add("gate");
        entrance.add("lobby");
        entrance.add("entryway");

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

    public static HashMap<String, List<String>> getKeys() {
        return keys;
    }
}
