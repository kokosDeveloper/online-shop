package com.kokos.onlineshop.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EmailTemplateName {
    ACTIVATE_ACCOUNT("activate_account", "Account activation"),
    ORDER_CONFIRMATION("order_confirmation", "Order confirmation")
    ;
    private final String name;
    @Getter
    private final String subject;
}
