package site.metacoding.junitproject.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.metacoding.junitproject.domain.Book;
import site.metacoding.junitproject.domain.BookRepository;
import site.metacoding.junitproject.util.MailSender;
import site.metacoding.junitproject.web.dto.BookRespDto;
import site.metacoding.junitproject.web.dto.BookSaveReqDto;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final MailSender mailSender;

    // 1. 책 등록
    @Transactional(rollbackFor = RuntimeException.class)
    public BookRespDto 책등록하기(BookSaveReqDto dto) {
        Book bookPS = bookRepository.save(dto.toEntity());
        if(bookPS != null) {
            if(!mailSender.send()) {
                throw new RuntimeException("메일이 전송되지 않았습니다");
            }
        }
        return new BookRespDto().toDto(bookPS);
    }
    /**
     * client -(filter)-> dispatcher servlet -> controller -> service -> repository -> persistent context -> db
     * 
     */

    // 2. 책 목록 보기
    public List<BookRespDto> 책목록보기() {
        // 본코드에 문제가 있나??
        List<BookRespDto> dtos = bookRepository.findAll().stream()
                // .map(new BookRespDto()::toDto) <= 얘 때문에! new가 두번 X, new는 한번 실행하고 toDto() method만 두번 샐행
                .map(Book::toDto)
                // .map(bookPS -> new BookRespDto().toDto(bookPS))
                .collect(Collectors.toList());

        // print
        // dtos.stream().forEach(dto -> {
        //     System.out.println("======================= 본코드");
        //     System.out.println(dto.getId());
        //     System.out.println(dto.getTitle());
        // });
        
        return dtos;
    }

    // 3. 책 한 권 보기
    public BookRespDto 책한권보기(Long id) {
        Optional<Book> bookOP = bookRepository.findById(id);
        if(bookOP.isPresent()) { // 찾았다면
            Book bookPS = bookOP.get();
            return bookPS.toDto();
            // return new BookRespDto().toDto(bookOP.get());
        } else {
            throw new RuntimeException("해당 아이디를 찾을 수 없습니다.");
        }
    }

    // 4. 책 삭제
    @Transactional(rollbackFor = RuntimeException.class)
    public void 책삭제하기(Long id) {
        bookRepository.deleteById(id); // 못찾아도 오류가 나지는 않음
    }

    // 5. 책 수정
    @Transactional(rollbackFor = RuntimeException.class)
    public BookRespDto 책수정하기(Long id, BookSaveReqDto dto) { //id, title, author
        Optional<Book> bookOP = bookRepository.findById(id);
        if(bookOP.isPresent()) { // 찾았다면
            Book bookPS = bookOP.get();
            bookPS.update(dto.getTitle(), dto.getAuthor());
            return bookPS.toDto();
        } else {
            throw new RuntimeException("해당 아이디를 찾을 수 없습니다.");
        }
    } // method 종료시에 더티체킹(flush)으로 update 된다
}
