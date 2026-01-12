package com.asdf.todo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

/**
 * 할 일(Todo) 정보를 관리하는 JPA 엔티티 클래스입니다.
 * 이 클래스는 데이터베이스의 'todo_db' 테이블과 매핑됩니다.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "todo_db")
public class Todo {

    /**
     * 할 일의 고유 식별자 (Primary Key)입니다.
     * MySQL의 AUTO_INCREMENT를 사용하여 자동으로 생성됩니다. (DB 위임)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 할 일의 제목입니다.
     * 데이터베이스 수준에서 NOT NULL 제약조건이 설정됩니다.
     */
    @NonNull
    @Column(nullable = false, length = 255)
    private String title;

    /**
     * 할 일에 대한 상세 내용입니다.
     */
    @NonNull
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * 할 일의 완료 상태를 나타냅니다.
     * true인 경우 완료, false인 경우 미완료로 간주됩니다.
     */
    @Column(nullable = false)
    private boolean completed = false;

    /**
     * 레코드가 생성된 일시입니다.
     * 데이터베이스 레벨에서 CURRENT_TIMESTAMP로 자동 설정되며,
     * 애플리케이션에서는 수정할 수 없도록 설정되었습니다.
     */
    @Column(
            nullable = false,
            name = "created_at",
            insertable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}