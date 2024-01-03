package com.mysite.medium.write;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WriteRepository extends JpaRepository <Write, Integer> {
    Write findBySubject(String subject);
    Write findBySubjectAndContent(String subject, String content);
    List<Write> findBySubjectLike(String subject);
    Page<Write> findAll(Pageable pageable);
    Page<Write> findAll(Specification<Write> spec, Pageable pageable);

    @Query("select "
            + "distinct q "
            + "from Write q "
            + "left outer join SiteUser u1 on q.author=u1 "
            + "left outer join Answer a on a.write=q "
            + "left outer join SiteUser u2 on a.author=u2 "
            + "where "
            + "   q.subject like %:kw% "
            + "   or q.content like %:kw% "
            + "   or u1.username like %:kw% "
            + "   or a.content like %:kw% "
            + "   or u2.username like %:kw% ")
    Page<Write> findAllByKeyword(@Param("kw") String kw, Pageable pageable);
}