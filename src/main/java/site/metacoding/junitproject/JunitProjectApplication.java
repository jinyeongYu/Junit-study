package site.metacoding.junitproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JunitProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(JunitProjectApplication.class, args);
	}

}

/**
 * 스프링 레이어 -> 역할 -> 수행 제대로 못하면..!!
 * 클라이언트 단에서부터 디버깅 하기 싫으면 역할 분담 제대로 하기
 * 
 * 클라이언트 -title, author 입력-> 컨트롤러 -전달-> 서비스 -전달-> Repository(dao 내장) -insert 요청-> DB
 * 
 * 1. 클라이언트 단에서 값을 제대로 입력 받았는지
 * 2. 컨트롤러에서 값을 제대로 전달 받았는지
 * 3. 서비스 레이어에서 값을 제대로 전달 받아서 서비스 로직이 잘 수행됐는지
 * 4. db 체크
 */