package org.example;

import java.util.HashMap;

public class Phonebook {
    private HashMap<String, String> phoneBookMap;//key name , value phone number

    /*
    inits the phonebook or clears it.
     */
    public Phonebook() {
        phoneBookMap = new HashMap<String, String>();
    }

    public String put(String k, String v) {
        return phoneBookMap.put(k, v);
    }

    public String get(String k) {
        return phoneBookMap.get(k);
    }

    public String remove(String k) {
        return phoneBookMap.remove(k);
    }

    public int size() {
        return phoneBookMap.size();
    }

    public Phonebook clone(){
        Phonebook phonebook = new Phonebook();
        phonebook.phoneBookMap = (HashMap<String, String>) this.phoneBookMap.clone();
        return phonebook;
    }


}

