package models.search;

import java.util.*;

public class SearchEngine {

    String term;
    Set<String> results = new HashSet<>();
    Stack<String> neighbors = new Stack<>();
    Set<String> uniqueNeighbors = new HashSet<>();

    public SearchEngine(String term) {
        this.term = term;
        this.search();
    }

    // TODO: Check if valid english word. If not, show results not found.
    // TODO: Link spanish words to categories
    private void search() {

        SearchKeywords searchKeywords = new SearchKeywords();

        if(searchKeywords.validEnglishWord(this.term)) {

            search(searchKeywords);

        } else {

            // Convert to valid word
            runDamerau(searchKeywords);

        }

    }

    private void search(SearchKeywords searchKeywords) {

        Iterator it = searchKeywords.getKeys().entrySet().iterator();

        while (it.hasNext()) {

            // Get next item
            Map.Entry pair = (Map.Entry) it.next();

            // Get category
            String category = pair.getKey().toString();

            if(this.term.equals(category)) { // Found category match!

                results.add(category);

                for(String keysInCategory : (ArrayList<String>) pair.getValue()) {
                    results.add(keysInCategory);
                }

                for(String s : searchKeywords.getSynonyms(this.term)) {

                    neighbors.push(s);

                }

                break;

            }

            List<String> keywords = (List<String>) pair.getValue();

            for(String k : keywords) {

                if(this.term.equals(k)) { // Found match!

                    results.add(k);

                    for(String keysInCategory : (ArrayList<String>) pair.getValue()) {
                        results.add(keysInCategory);
                    }

                    for(String s : searchKeywords.getSynonyms(this.term)) {

                        results.add(s);
                        neighbors.push(s);

                    }

                    break;

                }

            }

            // Remove item
            it.remove();

        }

    }

    private void runDamerau(SearchKeywords searchKeywords) {

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
//                System.out.println("currCategory: " + closestCategory);
            }

            List<String> keywords = (List<String>) pair.getValue();

            for(String k : keywords) {
                double currDistKeyword = damerau.distance(this.term, k);

                if(currDistKeyword < minDistanceKeyword) {
                    minDistanceKeyword = currDistKeyword;
                    closestKeyword = k;
//                    System.out.println("currKeyword: " + closestKeyword);

                    neighbors.push(closestKeyword);
                }
            }

            // Remove item
            it.remove();

        }

        if(minDistance == Double.MAX_VALUE) {
            System.out.println("No match!");
        } else {
//            System.out.println("Distance is: " + minDistance);
//            System.out.println("Distance for keyword is: " + minDistanceKeyword);
//
//            System.out.println("Search term: " + this.term);
//            System.out.println("Closest category: " + closestCategory);
//            System.out.println("Closest keyword: " + closestKeyword);

            results.add(closestCategory);
            results.add(closestKeyword);
        }

    }

    public Set<String> getResults() {

        while(!neighbors.isEmpty()) {

            String key = neighbors.pop();

            uniqueNeighbors.add(key);

        }

        results.addAll(uniqueNeighbors);

        return results;
    }

    public static void main(String[] args)
    {

        SearchEngine searchEngine = new SearchEngine("hola");

    }

}
