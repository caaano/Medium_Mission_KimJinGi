package com.mysite.medium.write;

import com.mysite.medium.DataNotFoundException;
import com.mysite.medium.answer.Answer;
import com.mysite.medium.user.SiteUser;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WriteService {

    private final WriteRepository writeRepository;
    private Object userService;

    private final PasswordEncoder passwordEncoder;

    public WriteService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    private Specification<Write> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Write> r, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Write, SiteUser> u1 = r.join("author", JoinType.LEFT);
                Join<Write, Answer> a = r.join("answerList", JoinType.LEFT);
                Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(r.get("subject"), "%" + kw + "%"), // 제목
                        cb.like(r.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
            }
        };
    }

    public List<Write> getList() {
        return this.writeRepository.findAll();
    }

    public Page<Write> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Write> spec = search(kw);
        return this.writeRepository.findAll(spec, pageable);
//        return this.writeRepository.findAllByKeyword(kw, pageable);
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

    public void modify(Write write, String subject, String content) {
        write.setSubject(subject);
        write.setContent(content);
        write.setModifyDate(LocalDateTime.now());
        this.writeRepository.save(write);
    }

    public void delete(Write write) {
        this.writeRepository.delete(write);
    }
    public void vote(Write write, SiteUser siteUser) {
        write.getVoter().add(siteUser);
        this.writeRepository.save(write);
    }

    public void generateSampleData() {
        generatePaidMemberships();
        generatePaidPosts();
    }

    private void generatePaidMemberships() {
        for (int i = 0; i < 100; i++) {
            SiteUser paidMember = new SiteUser();
            paidMember.setUsername("paid_member_" + i);
            paidMember.setPassword(passwordEncoder.encode("password"));
            paidMember.setPaidMember(true);
            userService.saveUser(paidMember);
        }
    }

    private void generatePaidPosts() {
        List<SiteUser> paidMembers = userService.getPaidMemberships();

        for (int i = 0; i < 100; i++) {
            Write paidPost = new Write();
            paidPost.setSubject("Paid Post " + i);
            paidPost.setContent("This is a paid post content.");
            paidPost.setCreateDate(LocalDateTime.now());
            paidPost.setAuthor(paidMembers.get(i % paidMembers.size()));
            writeRepository.save(paidPost);
        }
    }
}