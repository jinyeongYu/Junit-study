package site.metacoding.junitproject.web;

import java.net.http.HttpResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.metacoding.junitproject.service.BookService;
import site.metacoding.junitproject.web.dto.request.BookSaveReqDto;
import site.metacoding.junitproject.web.dto.response.BookRespDto;
import site.metacoding.junitproject.web.dto.response.CMRespDto;

@RequiredArgsConstructor //IoC 컨테이너에 있는걸 dependency injection
@RestController
public class BookApiController { // composition = has 관계
    // final로 선언해서 BookApiController가 생성될 때 초기화 필요 -> @RequiredArgsConstructor
    private final BookService bookService;

    // 1. 책 등록
    // key = value & key = value
    // { "key": value, "key": value }
    @PostMapping("/api/v1/book")
    public ResponseEntity<?> saveBook(@RequestBody BookSaveReqDto bookSaveReqDto) {
        BookRespDto bookRespDto = bookService.책등록하기(bookSaveReqDto);
        CMRespDto<?> cmRespDto = CMRespDto.builder().code(1).msg("글 저장 성공").body(bookRespDto).build();
        return new ResponseEntity<>(cmRespDto, HttpStatus.CREATED); // 201 = insert
    }

    // 2. 책 목록 보기
    public ResponseEntity<?> getBookList() {
        return null;
    }

    // 3. 책 한 권 보기
    public ResponseEntity<?> getBookOne() {
        return null;
    }

    // 4. 책 삭제하기
    public ResponseEntity<?> deleteBook() {
        return null;
    }

    // 5. 책 수정하기
    public ResponseEntity<?> updateBook() {
        return null;
    }
}
