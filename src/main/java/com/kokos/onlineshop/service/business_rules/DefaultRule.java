package com.kokos.onlineshop.service.business_rules;

public class DefaultRule implements Rule{
    private Condition condition;
    private Action action;

    public DefaultRule(Condition condition, Action action) {
        this.condition = condition;
        this.action = action;
    }

    @Override
    public void perform(Facts facts) {
        if (condition.evaluate(facts)) {
            action.perform(facts);
        }
    }
}
