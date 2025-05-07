package com.kokos.onlineshop.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    ADMIN_ADD_PRODUCT("addProduct"),
    ADMIN_DELETE_PRODUCT("deleteProduct"),
    ADMIN_UPDATE_PRODUCT("updateProduct"),

    ADMIN_ADD_CATEGORY("addCategory"),
    ADMIN_DELETE_CATEGORY("deleteCategory"),
    ADMIN_UPDATE_CATEGORY("updateCategory"),

    ADMIN_GET_USER_ORDERS("getUserOrders"),

    ;
    @Getter
    private final String permission;
}
