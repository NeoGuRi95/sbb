package com.mysite.sbb.tennis;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;

@RequestMapping("/tennis")
@Controller
@RequiredArgsConstructor
public class TennisController {

    private final CoatRepository coatRepository;

    @GetMapping("")
    public String coatInfo(Model model) {
        model.addAttribute("coatInfoList", coatRepository.findAll());
        return "tennis";
    }

}
