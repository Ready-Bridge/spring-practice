package com.asdf.todo.service;

import com.asdf.todo.dto.TodoRequestDto;
import com.asdf.todo.dto.TodoResponseDto;
import com.asdf.todo.entity.Todo;
import com.asdf.todo.repository.TodoRepository;
import com.asdf.todo.util.EntityDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 할 일(Todo)에 대한 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * 클라이언트의 요청(DTO)을 엔티티로 변환하여 저장하거나,
 * 데이터베이스의 엔티티를 응답용 DTO로 가공하여 반환하는 역할을 수행합니다.
 */
@Service
@RequiredArgsConstructor
public class TodoService {

    /** 데이터베이스 접근을 위한 레포지토리 */
    private final TodoRepository todoRepository;

    /**
     * 저장된 모든 할 일 목록을 조회합니다.
     *
     * @return {@link TodoResponseDto} 객체 리스트
     */
    @Transactional(readOnly = true)
    public List<TodoResponseDto> findAll() {
        return todoRepository.findAll().stream()
                .map(EntityDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * 식별자(ID)를 통해 특정 할 일을 상세 조회합니다.
     *
     * @param id 조회할 할 일의 식별자
     * @return 조회된 {@link TodoResponseDto} 객체 (존재하지 않을 경우 null)
     */
    @Transactional(readOnly = true)
    public TodoResponseDto findById(Long id) {
        return todoRepository.findById(id)
                .map(EntityDtoMapper::toDto)
                .orElse(null);
    }

    /**
     * 새로운 할 일을 시스템에 등록합니다.
     *
     * @param todoRequestDto 등록할 할 일 정보가 담긴 DTO
     * @return 저장 및 ID가 부여된 {@link TodoResponseDto} 객체
     */
    @Transactional
    public TodoResponseDto save(TodoRequestDto todoRequestDto) {
        Todo todo = EntityDtoMapper.toEntity(todoRequestDto);
        Todo savedTodo = todoRepository.save(todo);
        return EntityDtoMapper.toDto(savedTodo);
    }

    /**
     * 기존의 할 일 정보를 업데이트합니다.
     *
     * @param id 수정할 대상의 식별자
     * @param todoRequestDto 수정할 데이터가 담긴 DTO
     * @return 수정된 결과 정보를 담은 {@link TodoResponseDto} 객체
     */
    @Transactional
    public TodoResponseDto update(Long id, TodoRequestDto todoRequestDto) {
        Todo todo = EntityDtoMapper.toEntity(todoRequestDto);
        todo.setId(id);
        Todo updatedTodo = todoRepository.save(todo);
        return EntityDtoMapper.toDto(updatedTodo);
    }

    /**
     * 지정된 식별자(ID)에 해당하는 할 일을 삭제합니다.
     *
     * @param id 삭제할 대상의 식별자
     */
    @Transactional
    public void delete(Long id) {
        todoRepository.deleteById(id);
    }
}