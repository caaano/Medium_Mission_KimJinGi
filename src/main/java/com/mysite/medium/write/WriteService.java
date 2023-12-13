package com.mysite.medium.write;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.mysite.medium.DataNotFoundException;

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
}
