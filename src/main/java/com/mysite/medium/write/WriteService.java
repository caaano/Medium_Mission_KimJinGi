package com.mysite.medium.write;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class WriteService {

    private final WriteRepository writeRepository;

    public List<Write> getList() {
        return this.writeRepository.findAll();
    }
}
