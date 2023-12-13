package com.mysite.medium.write;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WriteRepository extends JpaRepository <Write, Integer> {
    Write findBySubject(String subject);
    Write findBySubjectAndContent(String subject, String content);
    List<Write> findBySubjectLike(String subject);
    Page<Write> findAll(Pageable pageable);
}
