package site.metacoding.junitproject.web;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.metacoding.junitproject.service.BookService;
import site.metacoding.junitproject.web.dto.request.BookSaveReqDto;
import site.metacoding.junitproject.web.dto.response.BookListRespDto;
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
    public ResponseEntity<?> saveBook(@RequestBody @Valid BookSaveReqDto bookSaveReqDto, BindingResult bindingResult) {
        // System.out.println("==============================");
        // System.out.println(bindingResult.hasErrors());
        // System.out.println("==============================");

        // AOP 처리하는 게 좋음!!
        if(bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for(FieldError fe : bindingResult.getFieldErrors()) {
                errorMap.put(fe.getField(), fe.getDefaultMessage());
            }
            System.out.println("==============================");
            System.out.println(errorMap.toString());
            System.out.println("==============================");

            throw new RuntimeException(errorMap.toString());
        }

        BookRespDto bookRespDto = bookService.책등록하기(bookSaveReqDto);
        return new ResponseEntity<>(CMRespDto.builder().code(1).msg("글 저장 성공").body(bookRespDto).build(), HttpStatus.CREATED); // 201 = insert
    }

    @PostMapping("/api/v2/book")
    public ResponseEntity<?> saveBookV2(@RequestBody BookSaveReqDto bookSaveReqDto) {
        BookRespDto bookRespDto = bookService.책등록하기(bookSaveReqDto);
        return new ResponseEntity<>(CMRespDto.builder().code(1).msg("글 저장 성공").body(bookRespDto).build(), HttpStatus.CREATED); // 201 = insert
    }

    // 2. 책 목록 보기
    @GetMapping("/api/v1/book")
    public ResponseEntity<?> getBookList() {
        BookListRespDto bookListRespDto = bookService.책목록보기();

        /**
         * 데이터 응답 시, List 형태 - 추천 X
         * 
         * code : 1
         * msg : "성공"
         * body : [...] 또는 {...} <= 결과값이 하나이냐 여러개이냐에 따라서 collection/object로 나눠짐. 반대쪽에서 parsing하기 굉장히 귀찮음
         */

        return new ResponseEntity<>(CMRespDto.builder().code(1).msg("글 목록보기 성공").body(bookListRespDto).build(), HttpStatus.OK); // 200 = ok;
    }

    // 3. 책 한 권 보기
    @GetMapping("/api/v1/book/{id}")
    public ResponseEntity<?> getBookOne(@PathVariable Long id) {
        BookRespDto bookRespDto = bookService.책한권보기(id);
        return new ResponseEntity<>(CMRespDto.builder().code(1).msg("책 한권보기 성공").body(bookRespDto).build(), HttpStatus.OK);
    }

    // 4. 책 삭제하기
    @DeleteMapping("/api/v1/book/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        bookService.책삭제하기(id);
        return new ResponseEntity<>(CMRespDto.builder().code(1).msg("글 삭제하기 성공").body(null).build(), HttpStatus.OK); // 200 = ok;
    }

    // 5. 책 수정하기
    public ResponseEntity<?> updateBook() {
        return null;
    }
}
