package com.google.engedu.ghost;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;
    private int first = 0;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while ((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
                words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        if (prefix.isEmpty()) {
            Random rand = new Random();
            return Character.toString((char) (rand.nextInt(26) + 'a'));
        } else {
            binarySearch(0, words.size() - 1, words, prefix);
            return words.get(first);
        }
    }

    private void binarySearch(int low, int high, ArrayList<String> array, String prefix) {
        while (high >= low) {
            int middle = (low + high) / 2;
            if (array.get(middle).startsWith(prefix)) {
                first = middle;
                break;
            }
            if (prefix.compareTo(array.get(middle)) > 0) {
                low = middle + 1;
                binarySearch(low, high, array, prefix);
            }
            if (prefix.compareTo(array.get(middle)) < 0) {
                high = middle - 1;
                binarySearch(low, high, array, prefix);
            }
        }
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        boolean flag = true;
        String result = "";
        ArrayList<String> even = new ArrayList<>();
        ArrayList<String> odd = new ArrayList<>();
        Random rand = new Random();
        /*Computer's turn first*/
        if (prefix.isEmpty()) {
            //lowercase letters
            prefix = Character.toString((char) (rand.nextInt(26) + 'a'));
            Log.d("random letter", prefix);
            flag = false;
        }
        binarySearch(0, words.size() - 1, words, prefix);
        for (int i = first; i < words.size(); i++) {
            if (words.get(i).startsWith(prefix))
                if (words.get(i).length() % 2 == 0)
                    even.add(words.get(i));
                else odd.add(words.get(i));
        }
        if (!odd.isEmpty() && !flag) {
            result = odd.get(rand.nextInt(odd.size()));
            Log.d("Word is ", result);
            return result;
        } else if (!even.isEmpty() && flag) {
            result = even.get(rand.nextInt(even.size()));
            Log.d("Word is ", result);
            return result;
        } else {
            Log.d("Word is ", result);
            return result;
        }
    }
}
