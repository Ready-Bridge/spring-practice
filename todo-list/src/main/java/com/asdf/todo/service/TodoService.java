package com.asdf.todo.service;

import com.asdf.todo.entity.Todo;
import com.asdf.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 할 일(Todo)에 대한 비즈니스 로직을 처리하는 서비스 클래스.
 * 리포지토리와 컨트롤러 사이에서 데이터 가공 및 검증 역할을 수행합니다.
 */
@Service
@RequiredArgsConstructor
public class TodoService {

    /** 할 일 데이터를 관리하는 저장소 의존성 주입 */
    private final TodoRepository todoRepository;

    /**
     * 저장된 모든 할 일 목록을 조회합니다.
     * * @return 할 일 객체 리스트
     */
    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    /**
     * ID를 통해 특정 할 일을 상세 조회합니다.
     * * @param id 조회할 할 일의 식별자
     * @return 찾아낸 Todo 객체 (없을 경우 null 반환 가능)
     */
    public Todo findById(Long id) {
        return todoRepository.findById(id);
    }

    /**
     * 새로운 할 일을 등록합니다.
     * * @param todo 등록할 할 일 정보 (ID는 자동 생성됨)
     * @return 저장된 결과 Todo 객체
     */
    public Todo save(Todo todo) {
        return todoRepository.save(todo);
    }

    /**
     * 기존의 할 일 정보를 수정합니다.
     * * @param id   수정할 대상의 ID
     * @param todo 수정할 데이터가 담긴 객체
     * @return 수정이 완료된 Todo 객체
     */
    public Todo update(Long id, Todo todo) {
        // 기존 ID를 유지한 채 데이터를 덮어씌우도록 설정
        todo.setId(id);
        return todoRepository.save(todo);
    }

    /**
     * ID에 해당하는 할 일을 삭제합니다.
     * * @param id 삭제할 대상의 ID
     */
    public void delete(Long id) {
        todoRepository.deleteById(id);
    }
}