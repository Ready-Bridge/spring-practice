package com.asdf.todo.repository;

import com.asdf.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Todo 엔티티에 대한 데이터베이스 액세스 처리를 담당하는 레포지토리 인터페이스입니다.
 * Spring Data JPA의 JpaRepository를 상속받아 기본적인 CRUD 기능을 제공합니다.
 */
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
}