package com.mysite.medium.write;

import com.mysite.medium.answer.AnswerForm;
import com.mysite.medium.user.SiteUser;
import com.mysite.medium.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequestMapping("/write")
@RequiredArgsConstructor
@Controller
public class WriteController {

    private final WriteService writeService;
    private final UserService userService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Write> paging = this.writeService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String writeModify(WriteForm writeForm, @PathVariable("id") Integer id, Principal principal) {
        Write write = this.writeService.getWrite(id);
        if(!write.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        writeForm.setSubject(write.getSubject());
        writeForm.setContent(write.getContent());
        return "write_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String writeModify(@Valid WriteForm writeForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "write_form";
        }
        Write write = this.writeService.getWrite(id);
        if (!write.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.writeService.modify(write, writeForm.getSubject(), writeForm.getContent());
        return String.format("redirect:/write/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String writeDelete(Principal principal, @PathVariable("id") Integer id) {
        Write write = this.writeService.getWrite(id);
        if (!write.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.writeService.delete(write);
        return "redirect:/";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String writeVote(Principal principal, @PathVariable("id") Integer id) {
        Write write = this.writeService.getWrite(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.writeService.vote(write, siteUser);
        return String.format("redirect:/write/detail/%s", id);
    }

    @GetMapping("/generateSampleData")
    public String generateSampleData() {
        writeService.generateSampleData();
        return "redirect:/write/list";
    }
}