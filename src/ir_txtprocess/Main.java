//SOURCES REFERRED::
//Source for compute frequency- https://docs.oracle.com/javase/tutorial/collections/interfaces/map.html
//Source for regex- http://www.newthinktank.com/2012/02/java-video-tutorial-19/
//http://www.programcreek.com/2013/03/java-sort-map-by-value/
//http://stackoverflow.com/questions/17338480/how-to-retrieve-key-values-from-hashmap
//http://stackoverflow.com/questions/3656762/n-gram-generation-from-a-sentence


package ir_txtprocess;



import java.io.*;
import java.util.*;
import java.util.regex.*;



public class Main {

    public static void main(String[] args) throws FileNotFoundException {


        String textFile = "C:\\Users\\arpanpune\\Desktop\\pg100"; //INPUT TEXT FILE


        String textFile2 = "C:\\Users\\arpanpune\\Desktop\\dictionary1.txt"; //DICTIONARY TEXT FILE

        //ArrayList<String> dictString = tokenizeFile(textFile2);



        HashMap<String, ArrayList<String>> dictMap = new HashMap<>();


        //Creating a reference HashMap for dictionary, to find anagrams

        /*for (String s : dictString) {
            char[] sArray = s.toCharArray();
            Arrays.sort(sArray);
            String s1 = new String(sArray);
            ArrayList<String> anaList = dictMap.get(s1);
            if (anaList == null) anaList = new ArrayList<String>();
            anaList.add(s);
            dictMap.put(s1, anaList);
        }*/



        //Print token List
        ArrayList<String> resultList = tokenizeFile(textFile);
        //print(resultList);


        //Print frequency list
        HashMap<String, Integer> frequencyMap = computeWordFrequencies(resultList);
        print(frequencyMap);


        //3-grams
        //ArrayList<String> threeGrams = threeGramBuilder(resultList);
        //print(threeGrams);

        //Print 3-Gram frequency list
        //HashMap<String, Integer> threeGramMap = computeThreeGramFrequencies(threeGrams);
        //print(threeGramMap);

        //Anagrams
        //HashMap<String,ArrayList<String>> anagramMap= detectAnagrams(resultList,dictMap);
        //printAnagrams(anagramMap);


    }

    static void print(ArrayList<String> resultList) {
        System.out.println("======================================================================");
        System.out.println("*********************TOKEN LIST/3-GRAM List***************************");
        System.out.println("======================================================================");

        for (String s : resultList) {
            System.out.println(s);
        }
        System.out.println("Total number of tokens/3-grams parsed from text file= "+resultList.size());
        System.out.println("\n\n");


    }

    static void print(HashMap<String, Integer> frequencyMap) {

        System.out.println("======================================================================");
        System.out.println("***************************FREQUENCY LIST*****************************");
        System.out.println("======================================================================");

        ValueComparator vc =  new ValueComparator(frequencyMap);
        TreeMap<String,Integer> sortedMap = new TreeMap<String,Integer>(vc);
        sortedMap.putAll(frequencyMap);
        //System.out.println(sortedMap);
        int count=0;
        for (HashMap.Entry<String, Integer> entry : sortedMap.entrySet()) {
        	if(count<=10){
            String key = entry.getKey().toString();
            Integer value = entry.getValue();
            System.out.println("(Token, Frequency)::\t(" + key + ", " + value + ")");
            count++; } break;
        }
        System.out.println("Total number of elements in the list from text file= "+sortedMap.size());
        System.out.println("\n\n");


    }

    public static ArrayList<String> tokenizeFile(String textFile) {
        ArrayList<String> tokenList = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(textFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String s = "";

        try {
            while ((s = br.readLine()) != null) {

                Pattern checkRegex = Pattern.compile("[A-Za-z0-9']{1,100}");
                Matcher regexMatcher = checkRegex.matcher(s);

                while (regexMatcher.find()) {
                    if (regexMatcher.group().length() != 0) {


                        tokenList.add((regexMatcher.group().trim().toLowerCase()));

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return tokenList;

    }

    public static HashMap<String, Integer> computeWordFrequencies(ArrayList<String> resultList) {
        HashMap<String, Integer> m = new HashMap<String, Integer>();
        for (String a : resultList) {
            Integer freq = m.get(a);
            m.put(a, (freq == null) ? 1 : freq + 1);
        }
        return m;

    }

    public static ArrayList<String> threeGramBuilder(ArrayList<String> resultList) {
        ArrayList<String> threeGrams = new ArrayList<>();


        for (int i = 0; i < resultList.size() - 3 + 1; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = i; j < i + 3; j++)
                sb.append((j > i ? " " : "") + resultList.get(j));


            threeGrams.add(sb.toString());
        }

        return threeGrams;

    }

    public static HashMap<String, Integer> computeThreeGramFrequencies(ArrayList<String> threeGrams) {
        HashMap<String, Integer> m = new HashMap<String, Integer>();
        for (String a : threeGrams) {
            Integer freq = m.get(a);
            m.put(a, (freq == null) ? 1 : freq + 1);
        }
        return m;

    }

    public static HashMap<String, ArrayList<String>> detectAnagrams(ArrayList<String> resultList, HashMap<String, ArrayList<String>> dictMap) {

        HashMap<String,ArrayList<String>> anaMap=new HashMap<>();
        for(String s:resultList){
            char[] sArray = s.toCharArray();
            Arrays.sort(sArray);
            String s1 = new String(sArray);
            if(dictMap.containsKey(s1)) {
                anaMap.put(s, dictMap.get(s1));
                if (anaMap.get(s).contains(s))
                    anaMap.get(s).remove(s);
            }

        }return anaMap;



    }

    static void printAnagrams(HashMap<String, ArrayList<String>> anaMap) {

        System.out.println("======================================================================");
        System.out.println("*********************ANAGRAM LIST*************************************");
        System.out.println("======================================================================");

        for (HashMap.Entry<String, ArrayList<String>> entry : anaMap.entrySet()) {
            String key = entry.getKey();
            ArrayList<String> value = entry.getValue();
            System.out.println("(String, Anagrams)::\t(" + key + ", " + value + ")");
        }        System.out.println("\n\n");



    }

    static class ValueComparator implements Comparator<String> {

        Map<String, Integer> map;

        public ValueComparator(Map<String, Integer> base) {
            this.map = base;
        }

        public int compare(String a, String b) {
            if (map.get(a) >= map.get(b)) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}






/*Complexity for anagram computation
Time required to build a hash map with dictionary elements "N"= O(N)
Time required to scan through map= O(1)
Time required to sort a string of length "n"=O(n(log(n)))
For N strings= N*O(n(log(n)))
The dominant term here is N, therefore overall complexity= O(N)*/









