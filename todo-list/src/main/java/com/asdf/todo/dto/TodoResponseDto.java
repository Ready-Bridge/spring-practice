package com.asdf.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * 할 일(Todo) 정보 응답을 위한 데이터 전송 객체(DTO)입니다.
 * 서버에서 처리된 할 일 데이터를 클라이언트에게 전달하는 용도로 사용됩니다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoResponseDto {

    /**
     * 할 일의 고유 식별자 (ID)
     */
    @NonNull
    private Long id;

    /**
     * 할 일의 제목
     */
    @NonNull
    private String title;

    /**
     * 할 일에 대한 상세 설명 (데이터가 없을 경우 null이 될 수 있음)
     */
    private String description;

    /**
     * 할 일의 완료 여부 (true: 완료, false: 미완료)
     */
    private boolean completed;
}