package com.kokos.onlineshop.service.business_rules;

public interface Condition {
    boolean evaluate(Facts facts);
}
