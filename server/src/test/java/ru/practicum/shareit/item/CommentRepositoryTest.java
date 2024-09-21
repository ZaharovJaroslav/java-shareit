package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.repository.CommentRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private User savedUser;
    private Item savedItem;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setName("Alex");
        user.setEmail("alex@mail.com");
        savedUser = userRepository.save(user);

        Item item = new Item();
        item.setName("Предмет");
        item.setDescription("Описание предмета");
        item.setAvailable(true);
        item.setOwnerId(savedUser.getId());
        savedItem = itemRepository.save(item);
    }

    @Test
    void testFindAllByItemId_shouldReturnCommentsForGivenItemId() {
        Comment comment1 = new Comment();
        comment1.setText("Хороший предмет!");
        comment1.setAuthor(savedUser);
        comment1.setItem(savedItem);
        comment1.setCreated(LocalDateTime.now());
        commentRepository.save(comment1);

        Comment comment2 = new Comment();
        comment2.setText("Отлично");
        comment2.setAuthor(savedUser);
        comment2.setItem(savedItem);
        comment2.setCreated(LocalDateTime.now());
        commentRepository.save(comment2);

        List<Comment> comments = commentRepository.findAllByItemId(savedItem.getId());

        assertThat(comments).hasSize(2);
        assertThat(comments).extracting(Comment::getText).containsExactlyInAnyOrder("Хороший предмет!",
                "Отлично");
    }

    @Test
    void testFindAllByItemId_shouldReturnEmptyListWhenNoComments() {
        List<Comment> comments = commentRepository.findAllByItemId(savedItem.getId());

        assertThat(comments).isEmpty();
    }
}
