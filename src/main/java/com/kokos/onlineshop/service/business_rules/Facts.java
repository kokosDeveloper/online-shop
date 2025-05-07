package com.kokos.onlineshop.service.business_rules;

import java.util.HashMap;
import java.util.Map;

public class Facts {
    private Map<String, Object> facts;

    public Facts() {
        this.facts = new HashMap<>();
    }
    public Object getFact(String name){
        return facts.get(name);
    }
    public void addFact(String name, Object value){
        facts.put(name, value);
    }
}
