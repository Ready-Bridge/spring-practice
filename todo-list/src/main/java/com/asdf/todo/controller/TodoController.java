package com.asdf.todo.controller;

import com.asdf.todo.model.Todo;
import com.asdf.todo.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Todo 항목에 대한 HTTP 요청을 처리하는 컨트롤러 클래스입니다.
 * <p>
 * 작업의 생성, 조회, 수정, 삭제(CRUD) 기능을 제공합니다.
 * </p>
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/todos/v1")
public class TodoController {

    private final TodoService todoService;

    /**
     * 저장된 모든 작업(Todo) 목록을 조회합니다.
     *
     * @return 작업 목록이 담긴 {@link ResponseEntity}.
     * 목록이 존재하면 200 OK와 함께 리스트를 반환하고,
     * 비어있을 경우 204 No Content를 반환합니다.
     */
    @GetMapping
    @Operation(summary = "전체 작업 조회", description = "전체 작업 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "204", description = "내용 없음")
    })
    public ResponseEntity<List<Todo>> getAllTodos() {
        List<Todo> todos = todoService.findAll();
        if(todos == null || todos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(todos);
    }

    /**
     * ID를 이용하여 특정 작업(Todo)을 단건 조회합니다.
     *
     * @param id 조회할 작업의 고유 ID (Path Variable)
     * @return 조회된 작업 객체가 담긴 {@link ResponseEntity}.
     * 작업이 존재하면 200 OK를 반환하고,
     * 존재하지 않으면 404 Not Found를 반환합니다.
     */
    @GetMapping("/{id}")
    @Operation(summary = "작업 조회", description = "ID로 작업 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "작업 없음"),
    })
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        Todo todo = todoService.findById(id);
        if(todo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(todo);
    }

    /**
     * 새로운 작업(Todo)을 생성합니다.
     *
     * @param todo 생성할 작업 정보가 담긴 객체 (Request Body)
     * @return 생성된 작업 객체와 201 Created 상태 코드를 포함한 {@link ResponseEntity}.
     */
    @PostMapping
    @Operation(summary = "작업 생성", description = "새로운 작업 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성됨")
    })
    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo) {
        return ResponseEntity.status(201).body(todoService.save(todo));
    }

    /**
     * 기존 작업(Todo)의 내용을 수정합니다.
     *
     * @param id 수정할 작업의 고유 ID (Path Variable)
     * @param todo 수정할 내용이 담긴 작업 객체 (Request Body)
     * @return 수정된 작업 객체가 담긴 {@link ResponseEntity}.
     * 대상 작업이 없으면 404 Not Found를 반환합니다.
     */
    @PutMapping("/{id}")
    @Operation(summary = "작업 수정", description = "ID로 작업 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "작업 없음")
    })
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo todo) {
        Todo existingTodo = todoService.findById(id);
        if(existingTodo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(todoService.save(todo));
    }

    /**
     * 특정 작업(Todo)을 삭제합니다.
     *
     * @param id 삭제할 작업의 고유 ID (Path Variable)
     * @return 삭제 성공 시 204 No Content를 반환하고,
     * 대상 작업이 없으면 404 Not Found를 반환합니다.
     */
    @DeleteMapping("/{id}") // 참고: 원본 코드에는 "/{id}"가 빠져있어 추가했습니다.
    @Operation(summary = "작업 삭제", description = "ID로 작업 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "내용 없음"),
            @ApiResponse(responseCode = "404", description = "작업 없음")

    })
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        Todo todo = todoService.findById(id);
        if(todo == null) {
            return ResponseEntity.notFound().build();
        }
        todoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}