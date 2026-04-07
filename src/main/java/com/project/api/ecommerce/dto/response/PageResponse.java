package com.project.api.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter // O Jackson usará os Getters para serializar (Objeto -> JSON)
public class PageResponse<T> {

    private final List<T> content;
    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;
    private final boolean first;
    private final boolean last;

    // O JsonCreator no construtor garante a deserialização (JSON -> Objeto)
    @JsonCreator
    public PageResponse(
            @JsonProperty("content") List<T> content,
            @JsonProperty("page") int page,
            @JsonProperty("size") int size,
            @JsonProperty("totalElements") long totalElements,
            @JsonProperty("totalPages") int totalPages,
            @JsonProperty("first") boolean first,
            @JsonProperty("last") boolean last) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.first = first;
        this.last = last;
    }

    /**
     * Método estático para converter o Page do Spring Data para este DTO.
     */
    public static <T> PageResponse<T> of(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }
}

//public record PageResponse<T>(
//        List<T> content,
//        int page,
//        int size,
//        long totalElements,
//        int totalPages,
//        boolean first,
//        boolean last
//) {
//    @JsonCreator
//    public PageResponse(
//            @JsonProperty("content") List<T> content,
//            @JsonProperty("page") int page,
//            @JsonProperty("size") int size,
//            @JsonProperty("totalElements") long totalElements,
//            @JsonProperty("totalPages") int totalPages,
//            @JsonProperty("first") boolean first,
//            @JsonProperty("last") boolean last) {
//        this.content = content;
//        this.page = page;
//        this.size = size;
//        this.totalElements = totalElements;
//        this.totalPages = totalPages;
//        this.first = first;
//        this.last = last;
//    }
//
//    public static <T> PageResponse<T> of( Page<T> page ) {
//        return new PageResponse<>(
//                page.getContent(),
//                page.getNumber(),
//                page.getSize(),
//                page.getTotalElements(),
//                page.getTotalPages(),
//                page.isFirst(),
//                page.isLast()
//        );
//    }
//}