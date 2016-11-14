package com.google.engedu.ghost;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class TrieNode {
    private HashMap<Character, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
        HashMap<Character, TrieNode> element = children;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            TrieNode trieNode;
            if (element.containsKey(c)) {
                trieNode = element.get(c);
            } else {
                trieNode = new TrieNode();
                element.put(c, trieNode);
            }

            element = trieNode.children;

            if (i == (s.length() - 1)) {
                trieNode.isWord = true;
            }
        }
    }

    public boolean isWord(String s) {
        TrieNode trieNode = search(s);
        if (trieNode != null && trieNode.isWord) {
            return true;
        } else {
            return false;
        }
    }

    public String getAnyWordStartingWith(String prefix) {
        if (prefix.isEmpty()) {
            Random rand = new Random();
            return Character.toString((char) (rand.nextInt(26) + 'a'));
        }
        TrieNode trieNode = search(prefix);
        String finalWord = prefix + "";
        Log.d("final word : ", finalWord);
        HashMap<Character, TrieNode> element;

        if (trieNode == null) {
            return null;
        } else {
            while (!(trieNode.isWord)) {
                element = trieNode.children;
                Character nextChar = (Character) element.keySet().toArray()[0];
                finalWord += nextChar;
                trieNode = element.get(nextChar);
            }
        }
        return finalWord;
    }

    public TrieNode search(String s) {
        HashMap<Character, TrieNode> element = children;
        TrieNode trieNode = null;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (element.containsKey(c)) {
                trieNode = element.get(c);
                element = trieNode.children;
            } else {
                return null;
            }
        }
        return trieNode;
    }

    public String getGoodWordStartingWith(String prefix) {
        boolean flag = true;
        String result = "";
        Random rand = new Random();
        if (prefix.isEmpty()) {
            prefix = Character.toString((char) (rand.nextInt(26) + 'a'));
            Log.d("random letter", prefix);
        }
        result = "";
        TrieNode trieNode = search(prefix);
        int incrementer = 0;
        if (trieNode == null) {
        } else {
            HashMap<Character, TrieNode> element = trieNode.children;
            Character nextChar;
            if (element.size() > 0)
                do {
                    result = prefix;
                    nextChar = (Character) element.keySet().toArray()[rand.nextInt(element.keySet().size())];
                    result += nextChar;
                    ++incrementer;
                } while (isWord(result) && element.size() > 1 && element.size() < incrementer);
        }
        Log.d("Final word is ", result);
        return result;
    }
}
