package ru.practicum.shareit.user;

import lombok.Data;

@Data
public class UpdateUserRequest {
    String name;
    String email;

    public boolean hasName() {
        return  ! (name == null || email.isBlank());
    }

    public boolean hasEmail() {
        return ! (email == null || email.isBlank());
    }
}
