package com.asdf.todo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.asdf.todo.entity.Todo;
import com.asdf.todo.repository.TodoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class TodoServiceTests {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @Test
    @DisplayName("전체 조회를 하면 레포지토리가 반환하는 목록을 그대로 반환한다")
    void findAll_Success() {
        List<Todo> mockList = Arrays.asList(new Todo(), new Todo());
        given(todoRepository.findAll()).willReturn(mockList);

        List<Todo> result = todoService.findAll();

        assertThat(result).hasSize(2);
        verify(todoRepository).findAll();
    }

    @Test
    @DisplayName("ID로 조회하면 해당 데이터를 반환한다")
    void findById_Success() {
        Todo todo = new Todo(1L, "Title", "Desc", false);
        given(todoRepository.findById(1L)).willReturn(todo);

        Todo result = todoService.findById(1L);

        assertThat(result.getTitle()).isEqualTo("Title");
        verify(todoRepository).findById(1L);
    }

    @Test
    @DisplayName("할 일을 저장하면 ID가 부여된 객체를 반환한다")
    void save_Success() {
        Todo todo = new Todo(null, "Task", "Desc", false);
        given(todoRepository.save(any(Todo.class)))
                .willReturn(new Todo(1L, "Task", "Desc", false));

        Todo result = todoService.save(todo);

        assertThat(result.getId()).isEqualTo(1L);
        verify(todoRepository).save(todo);
    }

    @Test
    @DisplayName("수정 시 전달받은 ID로 셋팅하여 저장한다")
    void update_Success() {
        Long id = 1L;
        Todo updateData = new Todo(null, "New Title", "New Desc", true);
        given(todoRepository.save(any(Todo.class))).willReturn(updateData);

        todoService.update(id, updateData);

        assertThat(updateData.getId()).isEqualTo(id);
        verify(todoRepository).save(updateData);
    }

    @Test
    @DisplayName("삭제 요청 시 레포지토리의 deleteById를 호출한다")
    void delete_Success() {
        todoService.delete(1L);

        verify(todoRepository).deleteById(1L);
    }
}