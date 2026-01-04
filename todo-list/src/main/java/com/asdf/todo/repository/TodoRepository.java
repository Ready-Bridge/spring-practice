package com.asdf.todo.repository;

import com.asdf.todo.model.Todo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TodoRepository {
    /** 할 일 데이터를 저장하는 인메모리 저장소 */
    private final Map<Long, Todo> todoMap = new HashMap<>();

    /** 중복 없는 ID 생성을 위한 원자적 카운터 */
    private final AtomicLong counter = new AtomicLong();

    /**
     * 전체 할 일 목록을 조회합니다.
     * @return 저장된 모든 Todo 리스트
     */
    public List<Todo> findAll() {
        return new ArrayList<>(todoMap.values());
    }

    /**
     * ID로 특정 할 일을 조회합니다.
     * @param id 조회할 할 일의 식별자
     * @return 찾아낸 Todo 객체 (없으면 null)
     */
    public Todo findById(Long id) {
        return todoMap.get(id);
    }

    /**
     * 할 일을 저장하거나 수정합니다.
     * ID가 없으면 새로 생성하여 저장(Insert)하고, 있으면 해당 ID로 덮어씁니다(Update).
     * @return ID가 할당되어 저장 완료된 Todo 객체
     */
    public Todo save(Todo todo) {
        if (todo.getId() == null) {
            todo.setId(counter.incrementAndGet());
        }
        todoMap.put(todo.getId(), todo);
        return todo;
    }

    /**
     * ID에 해당하는 할 일을 삭제합니다.
     */
    public void deleteById(Long id) {
        todoMap.remove(id);
    }
}