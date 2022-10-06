package site.metacoding.junitproject.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


//Test 코드 작성하는 이유
// 1. 메서드는 하나의 기능을 가져야 함 - 책임을 분리할 수 있다.
// 2. 각 기능마다 테스트 코드를 작성하면 유지 보수에 유리
// 3. 시간 단축

@DataJpaTest //db와 관련된 컴포넌트만 메모리에 로딩
public class BookRepositoryTest {

    @Autowired //DI
    private BookRepository bookRepository;

    // @BeforeAll // 테스트 시작전에 한번만 실행
    @BeforeEach // 각 테스트 시작전에 한번씩 실행
    public void 데이터준비() {
        // given (데이터 준비)
        String title = "junit";
        String author = "겟인데어";
        Book book = Book.builder()
                        .title(title)
                        .author(author)
                        .build();
        bookRepository.save(book);
    } // 트랜잭션 종료 됐다면 말이 안됨
    // 가정 1: [데이터준비() + 1 책등록] (T), [데이터준비() + 2 책 목록보기] (T)
    // 가정 2: [데이터준비() + 1 책등록 + 데이터준비() + 2 책 목록보기] (T)

    // 1. 책 등록
    @Test
    public void 책등록_test() {
        // given (데이터 준비)
        String title = "junit5";
        String author = "메타코딩";
        Book book = Book.builder()
                        .title(title)
                        .author(author)
                        .build();

        // when (테스트 실행)
        // persistance : 영구적으로 저장되었다
        Book bookPS = bookRepository.save(book); //db에 저장(pk 생성 = id 생성 완료) -> save 메서드가 db에 저장된 book을 return(db 데이터와 동기화된 데이터)

        // then (검증)
        assertEquals(title, bookPS.getTitle());
        assertEquals(author, bookPS.getAuthor());
    } // 트랜잭션 종료(저장된 데이터를 초기화함)

    // 2. 책 목록 보기
    @Test
    public void 책목록보기_test() {
        // given
        String title = "junit";
        String author = "겟인데어";;

        // when
        List<Book> booksPS = bookRepository.findAll();

        System.out.println("tkdlwm: ========================================================== :" + booksPS.size());

        // then
        // System.out.println(books.size());
        assertEquals(title, booksPS.get(0).getTitle());
        assertEquals(author, booksPS.get(0).getAuthor());
    }

    // 3. 책 한 권 보기
    @Test
    public void 책한권보기_test() {
        //given
        String title = "junit";
        String author = "겟인데어";

        //when
        Book bookPS = bookRepository.findById(1L).get();

        //then
        assertEquals(title, bookPS.getTitle());
        assertEquals(author, bookPS.getAuthor());
    }

    // 4. 책 수정

    // 5. 책 삭제
}
