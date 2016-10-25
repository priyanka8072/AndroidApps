package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList = new ArrayList<String>();
    private HashSet<String> wordSet = new HashSet<>();
    private HashMap<String, ArrayList> lettersToWord = new HashMap();
    private HashMap<Integer, ArrayList> sizeToWords = new HashMap();
    private int wordLength = DEFAULT_WORD_LENGTH;

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        ArrayList<String> hashValue = null;
        ArrayList<String> lenSort = null;
        while ((line = in.readLine()) != null) {
            String word = line.trim();
            int length = word.length();
            String key = sortLetters(word);
            wordList.add(word);
            if (lettersToWord.containsKey(key)) {
                hashValue = lettersToWord.get(key);
            } else
                hashValue = new ArrayList<>();
            hashValue.add(word);
            if (sizeToWords.containsKey(length)) {
                if (!sizeToWords.get(length).contains(word)) {
                    lenSort = sizeToWords.get(length);
                }
            } else
                lenSort = new ArrayList<>();
            lenSort.add(word);
            lettersToWord.put(key, hashValue);
            sizeToWords.put(length, lenSort);
        }
        wordSet.addAll(wordList);
    }

    public boolean isGoodWord(String word, String base) {
        if (wordSet.contains(word) && !word.contains(base)) {
            return true;
        } else
            return false;
    }

    public ArrayList<String> getAnagrams(String targetWord) {
        String sortedWord = sortLetters(targetWord);
        String word;
        int len;
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < wordList.size(); i++) {
            word = sortLetters(wordList.get(i));
            len = word.length();
            if (len == sortedWord.length() && word.equals(sortedWord)) {
                result.add(wordList.get(i));
            }
        }
        return result;
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        result = createArray(word);
        return result;
    }

    private ArrayList<String> createArray(String word) {
        ArrayList<String> result = new ArrayList<String>();
        String newWord = null;
        for (char letter = 'a'; letter <= 'z'; letter++) {
            newWord = null;
            String s = new String(new char[]{letter});
            newWord = word + s;
            result.addAll(getAnagrams(newWord));
        }
        return result;
    }

    public String pickGoodStarterWord() {
        ArrayList<String> temp = sizeToWords.get(wordLength);
        System.out.println(wordLength);
        Random randomNum = new Random();
        String random = null;
        int number = 0;
        while (number < MIN_NUM_ANAGRAMS) {
            random = temp.get(randomNum.nextInt(temp.size()));
            number = createArray(random).size();
            if (number >= MIN_NUM_ANAGRAMS) {
                if (wordLength < MAX_WORD_LENGTH) wordLength++;
                return random;
            }
        }
        return "cat";
    }

    private String sortLetters(String input) {
        char[] sorted = input.toCharArray();
        Arrays.sort(sorted);
        String sortedWord = new String(sorted);
        return sortedWord;
    }
}
