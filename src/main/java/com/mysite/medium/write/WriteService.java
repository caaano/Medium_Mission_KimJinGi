package com.mysite.medium.write;

import com.mysite.medium.DataNotFoundException;
import com.mysite.medium.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public void create(String subject, String content, SiteUser user) {
        Write w = new Write();
        w.setSubject(subject);
        w.setContent(content);
        w.setCreateDate(LocalDateTime.now());
        w.setAuthor(user);
        this.writeRepository.save(w);
    }

    public Page<Write> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 30, Sort.by(sorts));
        return this.writeRepository.findAll(pageable);
    }

    public void modify(Write write, String subject, String content) {
        write.setSubject(subject);
        write.setContent(content);
        write.setModifyDate(LocalDateTime.now());
        this.writeRepository.save(write);
    }

    public void delete(Write write) {
        this.writeRepository.delete(write);
    }
}