package org.example;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class Phonebook {
    private static HashMap<String, String> phoneBook;//key name , value phone number

    /*
    inits the phonebook or clears it.
     */
    public static void initPhonebook(){
            if (phoneBook == null){
                phoneBook = new HashMap<String, String>();
            } else {
                phoneBook.clear();
            }
    }

    public static String put(String k, String v){
        return phoneBook.put(k,v);
    }

    public static String get(String k){
        return phoneBook.get(k);
    }

    public static String remove(String k){
        return phoneBook.remove(k);
    }

    public static int size(){
        return phoneBook.size();
    }

    public static Set keySet(){
        return phoneBook.keySet();
    }

    public static Collection<String> values(){
        return phoneBook.values();
    }
}
