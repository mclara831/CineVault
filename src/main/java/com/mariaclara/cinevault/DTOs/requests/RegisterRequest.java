package com.mariaclara.cinevault.DTOs.requests;

import com.mariaclara.cinevault.entities.UserRole;

public record RegisterRequest(String username, String email, String password, UserRole roles) {
}
