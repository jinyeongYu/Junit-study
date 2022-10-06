package site.metacoding.junitproject.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    }

    // 2. 책 목록 보기

    // 3. 책 한 권 보기

    // 4. 책 수정

    // 5. 책 삭제
}
