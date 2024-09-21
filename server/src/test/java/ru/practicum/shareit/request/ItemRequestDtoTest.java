package ru.practicum.shareit.request;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoTest {
    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    @SneakyThrows
    public void testItemRequestDtoSerialize() {
        User user = User.builder()
                .id(1L)
                .name("Alex")
                .email("alex@mail.com")
                .build();


        ItemRequestDto dto = ItemRequestDto.builder()
                .id(1L)
                .description("Описание")
                .requestor(UserMapper.toUserDto(user))
                .created(LocalDateTime.now())
                .build();
        JsonContent<ItemRequestDto> content = json.write(dto);
        assertThat(content)
                .hasJsonPath("$.id")
                .hasJsonPath("$.description")
                .hasJsonPath("$.requestor")
                .hasJsonPath("$.created");

        assertThat(content).extractingJsonPathNumberValue("@.id").isEqualTo(1);
        assertThat(content).extractingJsonPathStringValue("@.description").isEqualTo("Описание");
        assertThat(content).extractingJsonPathStringValue("@.requestor.name")
                .isEqualTo("Alex");
    }
}
