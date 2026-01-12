package com.asdf.todo.controller;

import com.asdf.todo.dto.TodoRequestDto;
import com.asdf.todo.dto.TodoResponseDto;
import com.asdf.todo.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Todo 항목에 대한 HTTP 요청을 처리하는 v2 컨트롤러 클래스입니다.
 * <p>
 * v2 버전은 데이터베이스 연동 및 DTO(Data Transfer Object)를 사용하여
 * 계층 간 데이터를 전송합니다.
 * </p>
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/todos/v2")
public class TodoController {

    private final TodoService todoService;

    /**
     * 저장된 모든 작업(Todo) 목록을 조회합니다.
     *
     * @return {@link TodoResponseDto}의 리스트.
     * 데이터가 없을 경우 204 No Content를 반환합니다.
     */
    @GetMapping
    @Operation(summary = "전체 작업 조회", description = "시스템에 저장된 모든 할 일 목록을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "204", description = "데이터 없음")
    })
    public ResponseEntity<List<TodoResponseDto>> getAllTodos() {
        List<TodoResponseDto> todos = todoService.findAll();
        if (todos == null || todos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(todos);
    }

    /**
     * ID를 이용하여 특정 작업(Todo)을 단건 조회합니다.
     *
     * @param id 조회할 작업의 고유 ID
     * @return 조회된 {@link TodoResponseDto} 객체.
     * ID에 해당하는 작업이 없으면 404 Not Found를 반환합니다.
     */
    @GetMapping("/{id}")
    @Operation(summary = "작업 상세 조회", description = "고유 식별자(ID)를 통해 특정 할 일을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 작업이 존재하지 않음"),
    })
    public ResponseEntity<TodoResponseDto> getTodoById(@PathVariable Long id) {
        TodoResponseDto todo = todoService.findById(id);
        if (todo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(todo);
    }

    /**
     * 새로운 작업(Todo)을 생성합니다.
     *
     * @param todoRequestDto 생성할 작업 정보가 담긴 {@link TodoRequestDto}
     * @return 생성 완료된 {@link TodoResponseDto}와 201 Created 상태 코드.
     */
    @PostMapping
    @Operation(summary = "작업 생성", description = "새로운 할 일 항목을 시스템에 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "성공적으로 생성됨")
    })
    public ResponseEntity<TodoResponseDto> createTodo(@RequestBody TodoRequestDto todoRequestDto) {
        TodoResponseDto savedTodo = todoService.save(todoRequestDto);
        return ResponseEntity.status(201).body(savedTodo);
    }

    /**
     * 기존 작업(Todo)의 내용을 수정합니다.
     *
     * @param id 수정할 작업의 고유 ID
     * @param todoRequestDto 수정할 데이터가 담긴 {@link TodoRequestDto}
     * @return 수정이 완료된 {@link TodoResponseDto}.
     * 대상 작업이 없으면 404 Not Found를 반환합니다.
     */
    @PutMapping("/{id}")
    @Operation(summary = "작업 수정", description = "기존 작업의 제목, 설명, 완료 여부를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 완료"),
            @ApiResponse(responseCode = "404", description = "수정할 대상이 존재하지 않음")
    })
    public ResponseEntity<TodoResponseDto> updateTodo(@PathVariable Long id, @RequestBody TodoRequestDto todoRequestDto) {
        TodoResponseDto existingTodo = todoService.findById(id);
        if (existingTodo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(todoService.update(id, todoRequestDto));
    }

    /**
     * 특정 작업(Todo)을 삭제합니다.
     *
     * @param id 삭제할 작업의 고유 ID
     * @return 삭제 성공 시 204 No Content.
     * 대상 작업이 없으면 404 Not Found를 반환합니다.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "작업 삭제", description = "고유 식별자(ID)를 통해 특정 작업을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공 (반환 데이터 없음)"),
            @ApiResponse(responseCode = "404", description = "삭제할 대상이 존재하지 않음")
    })
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        TodoResponseDto todo = todoService.findById(id);
        if (todo == null) {
            return ResponseEntity.notFound().build();
        }
        todoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}