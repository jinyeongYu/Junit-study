package site.metacoding.junitproject.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import site.metacoding.junitproject.domain.Book;
import site.metacoding.junitproject.domain.BookRepository;
import site.metacoding.junitproject.util.MailSender;
import site.metacoding.junitproject.util.MailSenderStub;
import site.metacoding.junitproject.web.dto.BookRespDto;
import site.metacoding.junitproject.web.dto.BookSaveReqDto;

@ExtendWith(MockitoExtension.class) //Mockito -> 가짜 객체 보관 환경
public class BookServiceTest {

    @InjectMocks // 가짜 환경에 있는 가짜들의 의존성 주입
    private BookService bookService;

    @Mock // 가짜 환경에 가짜 repository 생성
    private BookRepository bookRepository;

    @Mock
    private MailSender mailSender;

    // 문제점 -> 서비스만 테스트하고 싶으넫, 레포지토리 레이어가 함께 테스트 된다는 점!! 
    @Test
    public void 책등록하기_테스트() {
        // given
        BookSaveReqDto dto = new BookSaveReqDto();
        dto.setTitle("junit강의");
        dto.setAuthor("메타코딩");

        // stub (가설)
        // when(bookRepository.save(dto.toEntity())).thenReturn(dto.toEntity());
        when(bookRepository.save(any())).thenReturn(dto.toEntity());
        when(mailSender.send()).thenReturn(true);

        //when
        
        BookRespDto bookRespDto = bookService.책등록하기(dto);
        
        //then
        // assertEquals("junit하하하", bookRespDto.getTitle());
        // assertEquals(dto.getAuthor(), bookRespDto.getAuthor());
        // assertJ
        assertThat(dto.getTitle()).isEqualTo(bookRespDto.getTitle());
        assertThat(dto.getAuthor()).isEqualTo(bookRespDto.getAuthor());
    }
}
