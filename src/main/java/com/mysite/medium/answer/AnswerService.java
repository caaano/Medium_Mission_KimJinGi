package com.mysite.medium.answer;

import com.mysite.medium.user.SiteUser;
import com.mysite.medium.write.Write;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;

    public Answer create(Write write, String content, SiteUser author) {
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setAuthor(author);
        answer.setWrite(write);
        this.answerRepository.save(answer);
        return answer;
    }
}
