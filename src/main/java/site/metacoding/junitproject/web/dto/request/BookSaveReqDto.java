package site.metacoding.junitproject.web.dto.request;

import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import site.metacoding.junitproject.domain.Book;

@Getter
@Setter //Controller에서 Setter가 호출되면서 Dto에 값이 채워짐
// 컨트롤러 title, author -> BookSaveReqDto -> Service에 넘김 -> Book 엔티티 변환 -> BookRepository.save(book)
public class BookSaveReqDto {
    @Size(min=1, max=50)
    @NotBlank//null이랑 공백 체크
    private String title;

    @Size(min=2, max=20)
    @NotBlank
    private String author;

    public Book toEntity() {
        return Book.builder()
        .title(title)
        .author(author)
        .build();
    }
}