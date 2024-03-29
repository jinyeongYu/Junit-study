package site.metacoding.junitproject.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;


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
    // 가정 1: [데이터준비() + 1 책등록] (T), [데이터준비() + 2 책 목록보기] (T) -> 사이즈 1 (검증 완료)
    // 가정 2: [데이터준비() + 1 책등록 + 데이터준비() + 2 책 목록보기] (T) -> 사이즈 2 (검증 실패)

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
    @Sql("classpath:db/tableInit.sql")
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


    /*
     * Junit 테스트
     * 
     * 1. 테스트 메서드 3개가 있다. (순서 보장이 안된다) - Order() annotation 순서 보장
     * 2. 테스트 메서드가 하나 실행 후 종료되면 데이터가 초기화된다. (@DataJpaTest - @AutoConfigureTestDatabase)
     *      - Transactional() annotation
     *      - 트랜잭션 종료 -> 데이터 초기화
     *      ** primary key auto_increment 값이 초기화가 안됨
     * 
     *  => id를 찾는 모든 테스트에는 '@Sql("classpath:db/tableInit.sql")'를 붙여주는 게 좋음
     */

    // 4. 책 삭제
    @Sql("classpath:db/tableInit.sql")
    @Test
    public void 책삭제_test() {
        // given
        Long id = 1L;

        // when
        bookRepository.deleteById(id);

        // then
        assertFalse(bookRepository.findById(id).isPresent());
    }

    // 5. 책 수정
    @Sql("classpath:db/tableInit.sql") //auto_increment 문제 때문에 table drop 했다가 다시 생성 -> id 검증할 때 필수.. 실제 서버로 테스트하려면 id 검증하지 마세요!
    @Test
    public void 책수정_test() {
        // given
        Long id = 1L;
        String title = "junit5";
        String author = "메타코딩";
        Book book = new Book(id, title, author);

        // when
        // bookRepository.findAll().stream()
        //     .forEach(b -> {
        //         System.out.println(b.getId());
        //         System.out.println(b.getTitle());
        //         System.out.println(b.getAuthor());
        //         System.out.println("1.================================");
        //     });

        Book bookPS = bookRepository.save(book);
        
        // bookRepository.findAll().stream()
        //     .forEach(b -> {
        //         System.out.println(b.getId());
        //         System.out.println(b.getTitle());
        //         System.out.println(b.getAuthor());
        //         System.out.println("2.================================");
        //     });

        // then
        assertEquals(id, bookPS.getId());
        assertEquals(title, bookPS.getTitle());
        assertEquals(author, bookPS.getAuthor());


        /**
         * Rollback
         * 
         * 메모리 -> 하드디스크 (커밋)
         * 메모리에 있는 data 지우기 -> 롤백
         * 
         * 
         * Junit -> 테스트 도구(라이브러리)
         *      1. 메서드 실행(트랜잭션 시작) -> 종료(트랜잭션 종료) -> Rollback
         * 
         * 
         * 1. BeforeEach(1건 save) -> 트랜잭션 시작
         *      -> 테이블 drop
         * 2. update test(1건 update) -> 트랜잭션 종료
         */

    }

}
