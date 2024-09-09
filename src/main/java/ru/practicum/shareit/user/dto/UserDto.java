package ru.practicum.shareit.user.dto;


import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

@Component
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;

    public static UserDto toUserDto(User user) {
        log.debug("toUserDto({})", user);
        return new UserDto(user.getId(),
                    user.getName(),
                    user.getEmail()
        );
    }
}
