package com.mysite.medium.write;

import com.mysite.medium.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WriteService {

    private final WriteRepository writeRepository;

    public List<Write> getList() {
        return this.writeRepository.findAll();
    }

    public Write getWrite(Integer id) {
        Optional<Write> write = this.writeRepository.findById(id);
        if (write.isPresent()) {
            return write.get();
        } else {
            throw new DataNotFoundException("write not found");
        }
    }
    public void create(String subject, String content) {
        Write w = new Write();
        w.setSubject(subject);
        w.setContent(content);
        w.setCreateDate(LocalDateTime.now());
        this.writeRepository.save(w);
    }
}
