package com.asdf.todo.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.asdf.todo.dto.TodoRequestDto;
import com.asdf.todo.dto.TodoResponseDto;
import com.asdf.todo.entity.Todo;
import com.asdf.todo.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@SpringBootTest
@Testcontainers
class TodoServiceTests {

    @Container
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.32")
            .withDatabaseName("todo_test_db")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired private TodoService todoService;
    @Autowired private TodoRepository todoRepository;

    @BeforeEach
    void setUp() {
        // 각 테스트 실행 전에 DB를 깨끗하게 비웁니다.
        todoRepository.deleteAll();
    }

    @Test
    @DisplayName("할 일을 저장하면 DB에 기록되고 ID가 포함된 DTO를 반환한다")
    void save_Success() {
        TodoRequestDto request = new TodoRequestDto("Save Test", "Content");

        TodoResponseDto result = todoService.save(request);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Save Test");
    }

    @Test
    @DisplayName("전체 조회를 하면 DB에 저장된 모든 목록을 DTO로 반환한다")
    void findAll_Success() {
        todoRepository.save(new Todo(null, "Task 1", "Desc 1", false, null));
        todoRepository.save(new Todo(null, "Task 2", "Desc 2", true, null));

        List<TodoResponseDto> result = todoService.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("ID로 조회 시 실제 DB의 데이터를 정확히 찾아 DTO로 변환한다")
    void findById_Success() {
        Todo saved = todoRepository.save(new Todo(null, "Find Me", "Desc", false, null));

        TodoResponseDto result = todoService.findById(saved.getId());

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Find Me");
    }

    @Test
    @DisplayName("수정 시 DB의 기존 데이터를 변경하고 업데이트된 정보를 반환한다")
    void update_Success() {
        Todo saved = todoRepository.save(new Todo(null, "Old Title", "Old Desc", false, null));
        TodoRequestDto updateRequest = new TodoRequestDto("New Title", "New Desc", true);

        TodoResponseDto result = todoService.update(saved.getId(), updateRequest);

        assertThat(result.getTitle()).isEqualTo("New Title");
        assertThat(result.isCompleted()).isTrue();
    }

    @Test
    @DisplayName("삭제 시 DB에서 해당 레코드가 제거된다")
    void delete_Success() {
        Todo saved = todoRepository.save(new Todo(null, "To be deleted", "Desc", false, null));

        todoService.delete(saved.getId());

        assertThat(todoRepository.findById(saved.getId())).isEmpty();
    }
}