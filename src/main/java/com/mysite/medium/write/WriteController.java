package com.mysite.medium.write;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@RequestMapping("/write")
@RequiredArgsConstructor
@Controller
public class WriteController {

    private final WriteService writeService;

    @GetMapping("/list")
    public String list(Model model) {
        List<Write> writeList = this.writeService.getList();
        model.addAttribute("writeList", writeList);
        return "write_list";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id) {
        Write write = this.writeService.getWrite(id);
        model.addAttribute("write", write);
        return "write_detail";
    }
}