package com.asdf.todo.util;

import com.asdf.todo.dto.TodoRequestDto;
import com.asdf.todo.dto.TodoResponseDto;
import com.asdf.todo.entity.Todo;

/**
 * Entity와 DTO 간의 데이터 변환을 담당하는 매퍼 클래스입니다.
 */
public class EntityDtoMapper {

    /**
     * Todo 엔티티를 TodoResponseDto로 변환합니다.
     * * @param todo 변환할 엔티티 객체
     * @return 변환된 DTO 객체
     */
    public static TodoResponseDto toDto(Todo todo) {
        return new TodoResponseDto(
                todo.getId(),
                todo.getTitle(),
                todo.getDescription(),
                todo.isCompleted()
        );
    }

    /**
     * TodoRequestDto를 Todo 엔티티로 변환합니다. (생성/수정용)
     * id와 createdAt은 DB에서 자동 생성되므로 포함하지 않습니다.
     *
     * @param dto 변환할 요청 DTO 객체
     * @return 변환된 Todo 엔티티 객체
     */
    public static Todo toEntity(TodoRequestDto dto) {
        Todo todo = new Todo();
        todo.setTitle(dto.getTitle());
        todo.setDescription(dto.getDescription());
        todo.setCompleted(dto.isCompleted());
        return todo;
    }
}
