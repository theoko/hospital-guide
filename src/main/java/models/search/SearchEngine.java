package models.search;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SearchEngine {

    String term;

    public SearchEngine(String term) {
        this.term = term;
        this.search();
    }

    private void search() {

        SearchKeywords searchKeywords = new SearchKeywords();
        Damerau damerau = new Damerau();

        Iterator it = searchKeywords.getKeys().entrySet().iterator();

        double minDistance = Double.MAX_VALUE;
        double minDistanceKeyword = Double.MAX_VALUE;

        String closestCategory = "";
        String closestKeyword = "";
        while (it.hasNext()) {

            // Get next item
            Map.Entry pair = (Map.Entry) it.next();

            // Get category
            String category = pair.getKey().toString();

            double currDist = damerau.distance(this.term, category);

            if(currDist < minDistance) {
                minDistance = currDist;
                closestCategory = category;
            }


            List<String> keywords = (List<String>) pair.getValue();

            for(String k : keywords) {
                double currDistKeyword = damerau.distance(this.term, k);

                if(currDistKeyword < minDistanceKeyword) {
                    minDistanceKeyword = currDistKeyword;
                    closestKeyword = k;
                }
            }

            // Remove item
            it.remove();

        }

        if(minDistance == Double.MAX_VALUE) {
            System.out.println("No match!");
        } else {
            System.out.println("Distance is: " + minDistance);
            System.out.println("Distance for keyword is: " + minDistanceKeyword);

            System.out.println("Search term: " + this.term);
            System.out.println("Closest category: " + closestCategory);
            System.out.println("Closest keyword: " + closestKeyword);
        }

    }

    public static void main(String[] args)
    {

        SearchEngine searchEngine = new SearchEngine("coffee shop");

    }

}
