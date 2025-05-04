package com.kokos.onlineshop.domain.dto;

import com.kokos.onlineshop.domain.dto.groups.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryRequest(
        @NotNull(groups = OnUpdate.class, message = "Category id is required for update")
        Long id,
        @NotBlank(message = "Category name is required")
        String title
) {
}
