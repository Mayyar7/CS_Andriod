package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.HashSet;
import java.util.Arrays;
import java.util.HashMap;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private static int wordLength = DEFAULT_WORD_LENGTH;
    private Random random = new Random();
    private ArrayList wordList = new ArrayList();
    private HashSet<String> wordSet = new HashSet<String>();
    private HashMap<String , ArrayList<String>> lettersToWord = new HashMap<String , ArrayList<String> >();
    private HashMap<Integer , ArrayList<String>> sizeToWords = new HashMap<Integer , ArrayList<String>>();

    //Constructor
    public AnagramDictionary(InputStream wordListStream) throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;

        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);

            //Creating Hash Map Values
            ArrayList<String> temp_word = new ArrayList<String>();
            String sortWord = alphabeticalOrder(word);

            if(lettersToWord.containsKey(sortWord)){
                temp_word = lettersToWord.get(sortWord);
                temp_word.add(word);
                lettersToWord.put(sortWord,temp_word);
            }

            else {
                temp_word.add(word);
                lettersToWord.put(sortWord,temp_word);
            }
            ArrayList<String> temp_word1 = new ArrayList<String>();

            //Creating Hash Map Mapping words to their lengths
            if(sizeToWords.containsKey(word.length())){
                temp_word1 = sizeToWords.get(word.length());
                temp_word1.add(word);
                sizeToWords.put(word.length(),temp_word1);
            }
            else {
                temp_word1.add(word);
                sizeToWords.put(word.length(),temp_word1);
            }

        }

    }

    public String alphabeticalOrder(String word){
        char[] charArray = word.toCharArray();
        Arrays.sort(charArray);
        String newWord = new String(charArray);

        return newWord ;
    }

    public boolean isGoodWord(String word, String base) {
        if(wordSet.contains(word) && !word.contains(base))
             return true;
        else
            return false;
    }

    //public ArrayList<String> getAnagrams(String targetWord) {
       // ArrayList<String> result = new ArrayList<String>();

     //   return result;
   // }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        String temp_word1;
        for (char i='a';i<='z';i++){
            temp_word1 = i+word;
            temp_word1 = alphabeticalOrder(temp_word1);

            if (lettersToWord.containsKey(temp_word1)){
                result.addAll(lettersToWord.get(temp_word1));
            }
        }
        for (int i =0 ; i<result.size();i++){
            //Log.d("AD list",result.get(i));
            if(!isGoodWord(result.get(i),word)){
                //Log.d("AD list" , result.remove(i));
                result.remove(i);
            }

        }

        return result;
    }

    public String pickGoodStarterWord() {
        String word = new String();
        int j;
        ArrayList<String> lengthWords = new ArrayList<String>();

        if(wordLength<= MAX_WORD_LENGTH){
            lengthWords = sizeToWords.get(wordLength);
        }
        int i = random.nextInt(lengthWords.size());
        for (j=i;j<lengthWords.size();j++){
            if(getAnagramsWithOneMoreLetter(lengthWords.get(j)).size()>= MIN_NUM_ANAGRAMS){
                word = lengthWords.get(j);
                break;
            }

        }
        //Keep While in place of for and just add a counter
        if(j==lengthWords.size()-1 || word== null){
            for (j=0;j<i;j++){
                if(getAnagramsWithOneMoreLetter(lengthWords.get(j)).size()>= MIN_NUM_ANAGRAMS){
                    word = lengthWords.get(j);
                    break;
                }

            }
        }
        if (wordLength <MAX_WORD_LENGTH){
            wordLength++;
        }
        return word;
    }
}
