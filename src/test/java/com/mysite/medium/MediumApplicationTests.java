package com.mysite.medium;

import com.mysite.medium.write.WriteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class SbbApplicationTests {

    @Autowired
    private WriteService writeService;

    @Test
    void testJpa() {
        for (int i = 1; i <= 30; i++) {
            String subject = String.format("테스트 데이터입니다:[%03d]", i);
            String content = "내용무";
            this.writeService.create(subject, content);
        }
    }
}
