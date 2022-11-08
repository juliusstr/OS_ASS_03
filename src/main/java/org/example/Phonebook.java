package org.example;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class Phonebook {
    private HashMap<String, String> phoneBook;//key name , value phone number

    /*
    inits the phonebook or clears it.
     */
    public Phonebook() {
        phoneBook = new HashMap<String, String>();
    }

    public String put(String k, String v) {
        return phoneBook.put(k, v);
    }

    public String get(String k) {
        return phoneBook.get(k) == null ? "fejl" : phoneBook.get(k);
    }

    public String remove(String k) {
        return phoneBook.remove(k);
    }

    public int size() {
        return phoneBook.size();
    }
}

