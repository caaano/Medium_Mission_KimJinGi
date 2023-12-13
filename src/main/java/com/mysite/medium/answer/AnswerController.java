package com.mysite.medium.answer;

import com.mysite.medium.write.Write;
import com.mysite.medium.write.WriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {

    private final WriteService writeService;
    private final AnswerService answerService;

    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Integer id,
                               @Valid AnswerForm answerForm, BindingResult bindingResult) {
        Write write = this.writeService.getWrite(id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("write", write);
            return "write_detail";
        }
        this.answerService.create(write, answerForm.getContent());
        return String.format("redirect:/write/detail/%s", id);
    }
}
