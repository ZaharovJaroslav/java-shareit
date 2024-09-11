package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserDto {
    private Long id;
    private String name;
    private String email;
}
