package ru.practicum.shareit.user.request;

import lombok.Data;

@Data
public class UpdateUserRequest {
    String name;
    String email;

    public boolean hasName() {
        return  ! (name == null || name.isBlank());
    }

    public boolean hasEmail() {
        return ! (email == null || email.isBlank());
    }
}
