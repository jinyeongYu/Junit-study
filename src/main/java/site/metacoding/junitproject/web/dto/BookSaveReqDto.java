package site.metacoding.junitproject.web.dto;

import lombok.Setter;
import site.metacoding.junitproject.domain.Book;

@Setter //Controller에서 Setter가 호출되면서 Dto에 값이 채워짐
// 컨트롤러 title, author -> BookSaveReqDto -> Service에 넘김 -> Book 엔티티 변환 -> BookRepository.save(book)
public class BookSaveReqDto {
    private String title;
    private String author;

    public Book toEntity() {
        return Book.builder()
        .title(title)
        .author(author)
        .build();
    }
}
