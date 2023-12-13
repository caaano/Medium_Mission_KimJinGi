package com.mysite.medium.write;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class WriteController {

    private final WriteRepository writeRepository;

    @GetMapping("/write/list")
    public String list(Model model) {
        List<Write> writeList = this.writeRepository.findAll();
        return "write_list";
    }
}
