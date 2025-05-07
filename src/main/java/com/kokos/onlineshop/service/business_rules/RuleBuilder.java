package com.kokos.onlineshop.service.business_rules;

public class RuleBuilder {
    private Condition condition;
    private RuleBuilder(Condition condition){
        this.condition = condition;
    }
    public static RuleBuilder when(Condition condition){
        return new RuleBuilder(condition);
    }
    public Rule then(Action action){
        return new DefaultRule(condition, action);
    }
}
