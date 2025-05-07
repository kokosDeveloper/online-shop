package com.kokos.onlineshop.service.business_rules;

import java.util.ArrayList;
import java.util.List;

public class BusinessRuleEngine {
    private List<Rule> rules;
    private Facts facts;

    public BusinessRuleEngine(Facts facts) {
        this.facts = facts;
        rules = new ArrayList<>();
    }

    public void addRule(Rule rule){
        rules.add(rule);
    }
    public void run(){
        for (Rule rule : rules)
            rule.perform(facts);
    }
}
