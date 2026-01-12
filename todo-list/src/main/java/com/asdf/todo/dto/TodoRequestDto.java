package com.asdf.todo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * 할 일(Todo) 생성 및 수정을 위한 요청 데이터 전송 객체(DTO)입니다.
 * 클라이언트로부터 전달받은 할 일 정보를 서버로 전달하는 역할을 합니다.
 */
@Data
@NoArgsConstructor // 프레임워크(Jackson)의 JSON 역직렬화를 위해 필수적으로 포함
public class TodoRequestDto {

    /** 할 일의 제목 (필수) */
    @NonNull
    private String title;

    /** 할 일에 대한 상세 설명 (필수) */
    @NonNull
    private String description;

    /** * 할 일의 완료 여부
     * 기본값은 false(미완료)입니다.
     */
    private boolean completed = false;

    /**
     * 제목, 설명, 완료 여부를 모두 포함하는 생성자입니다.
     *
     * @param title 할 일 제목
     * @param description 할 일 상세 설명
     * @param completed 완료 여부
     */
    public TodoRequestDto(String title, String description, boolean completed) {
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    /**
     * 완료 여부를 제외한 필수 정보만으로 객체를 생성하는 생성자입니다.
     * 완료 여부는 기본값(false)으로 설정됩니다.
     *
     * @param title 할 일 제목
     * @param description 할 일 상세 설명
     */
    public TodoRequestDto(String title, String description) {
        this(title, description, false);
    }
}