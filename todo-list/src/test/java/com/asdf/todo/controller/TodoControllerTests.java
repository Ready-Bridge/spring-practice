package com.asdf.todo.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.asdf.todo.dto.TodoRequestDto;
import com.asdf.todo.dto.TodoResponseDto;
import com.asdf.todo.service.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

@WebMvcTest(TodoController.class)
class TodoControllerTests {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private TodoService todoService;

    @Test
    @DisplayName("ID로 할 일을 조회하면 200 OK와 해당 데이터를 반환한다")
    void testGetTodoById() throws Exception {
        TodoResponseDto todo = new TodoResponseDto(1L, "Test Todo", "Description", false);

        given(todoService.findById(1L)).willReturn(todo);

        mockMvc.perform(get("/api/todos/v2/1") // v2 반영
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Todo"));
    }

    @Test
    @DisplayName("할 일 목록이 비어있을 때 204 No Content를 반환한다")
    void testGetAllTodos_Empty() throws Exception {
        given(todoService.findAll()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/api/todos/v2")) // v2 반영
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("할 일 목록이 존재할 때 200 OK와 리스트를 반환한다")
    void testGetAllTodos_Success() throws Exception {
        TodoResponseDto todo = new TodoResponseDto(1L, "Test Todo", "Description", false);
        given(todoService.findAll()).willReturn(Collections.singletonList(todo));

        mockMvc.perform(get("/api/todos/v2") // v2 반영
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Test Todo"));
    }

    @Test
    @DisplayName("새로운 할 일을 생성하면 201 Created와 생성된 데이터를 반환한다")
    void testCreateTodo() throws Exception {
        // RequestDto 사용
        TodoRequestDto requestDto = new TodoRequestDto("New Todo", "Description");
        // ResponseDto 사용
        TodoResponseDto responseDto = new TodoResponseDto(1L, "New Todo", "Description", false);

        given(todoService.save(any(TodoRequestDto.class))).willReturn(responseDto);

        mockMvc.perform(post("/api/todos/v2") // v2 반영
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("New Todo"));

        verify(todoService).save(any(TodoRequestDto.class));
    }

    @Test
    @DisplayName("할 일을 수정하면 200 OK와 수정된 데이터를 반환한다")
    void testUpdateTodo() throws Exception {
        // RequestDto 및 ResponseDto 사용
        TodoRequestDto requestDto = new TodoRequestDto("Updated Todo", "Description");
        TodoResponseDto responseDto = new TodoResponseDto(1L, "Updated Todo", "Description", false);

        // 수정 시나리오: findById로 존재 확인 후 update 호출
        given(todoService.findById(1L)).willReturn(responseDto);
        given(todoService.update(anyLong(), any(TodoRequestDto.class))).willReturn(responseDto);

        mockMvc.perform(
                        put("/api/todos/v2/1") // v2 반영
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Updated Todo"));

        verify(todoService).findById(1L);
        verify(todoService).update(anyLong(), any(TodoRequestDto.class));
    }

    @Test
    @DisplayName("존재하는 할 일을 삭제하면 204 No Content를 반환하고 서비스를 호출한다")
    void testDeleteTodo_Success() throws Exception {
        TodoResponseDto responseDto = new TodoResponseDto(1L, "Title", "Desc", false);
        given(todoService.findById(1L)).willReturn(responseDto);

        mockMvc.perform(delete("/api/todos/v2/1")) // v2 반영
                .andExpect(status().isNoContent());

        verify(todoService).delete(1L);
    }

    @Test
    @DisplayName("존재하지 않는 할 일을 삭제하려 하면 404 Not Found를 반환하고 삭제를 수행하지 않는다")
    void testDeleteTodo_NotFound() throws Exception {
        given(todoService.findById(1L)).willReturn(null);

        mockMvc.perform(delete("/api/todos/v2/1")) // v2 반영
                .andExpect(status().isNotFound());

        verify(todoService).findById(1L); // 조회가 일어났는지 확인
        verify(todoService, never()).delete(anyLong()); // 삭제는 절대 호출되지 않았는지 확인
    }
}