package com.mysite.medium;

import com.mysite.medium.write.Write;
import com.mysite.medium.write.WriteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;


@SpringBootTest
class SbbApplicationTests {

    @Autowired
    private WriteRepository writeRepository;

    @Test
    void testJpa() {
        Write w1 = new Write();
        w1.setSubject("sbb가 무엇인가요?");
        w1.setContent("sbb에 대해서 알고 싶습니다.");
        w1.setCreateDate(LocalDateTime.now());
        this.writeRepository.save(w1);  // 첫번째 질문 저장

        Write w2 = new Write();
        w2.setSubject("스프링부트 모델 질문입니다.");
        w2.setContent("id는 자동으로 생성되나요?");
        w2.setCreateDate(LocalDateTime.now());
        this.writeRepository.save(w2);  // 두번째 질문 저장
    }
}
