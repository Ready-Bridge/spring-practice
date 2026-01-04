package com.asdf.todo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * 할 일(Todo) 데이터를 담는 모델 클래스
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Todo {

    /** 시스템에서 관리하는 고유 식별자 */
    private Long id;

    /** 할 일의 제목 (필수 값) */
    @NonNull
    private String title;

    /** 할 일에 대한 상세 설명 */
    private String description;

    /** 완료 여부 (true: 완료, false: 미완료) */
    private boolean completed;
}