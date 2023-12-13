package com.mysite.medium.write;

import com.mysite.medium.answer.AnswerForm;
import com.mysite.medium.user.SiteUser;
import com.mysite.medium.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequestMapping("/write")
@RequiredArgsConstructor
@Controller
public class WriteController {

    private final WriteService writeService;
    private final UserService userService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue = "0") int page) {
        Page<Write> paging = this.writeService.getList(page);
        model.addAttribute("paging", paging);
        return "write_list";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
        Write write = this.writeService.getWrite(id);
        model.addAttribute("write", write);
        return "write_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String writeCreate(WriteForm writeForm) {
        return "write_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String writeCreate(@Valid WriteForm writeForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "write_form";
        }

        SiteUser siteUser = this.userService.getUser(principal.getName());

        this.writeService.create(writeForm.getSubject(), writeForm.getContent(), siteUser);
        return "redirect:/write/list"; // 질문 저장후 질문목록으로 이동
    }
}