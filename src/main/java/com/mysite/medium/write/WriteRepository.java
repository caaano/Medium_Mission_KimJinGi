package com.mysite.medium.write;

import com.mysite.medium.write.Write;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WriteRepository extends JpaRepository <Write, Integer> {
}
