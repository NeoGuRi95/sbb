package com.mysite.sbb.tennis;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysite.sbb.WebDriverUtil;
import lombok.RequiredArgsConstructor;

@RequestMapping("/tennis")
@Controller
@RequiredArgsConstructor
public class TennisController {

    private final CoatRepository coatRepository;
    private final WebDriverUtil webDriverUtil;
    private final ObjectMapper objectMapper;

    @GetMapping("")
    public String coatInfo(Model model) {
        model.addAttribute("coatInfoList", coatRepository.findAll());
        return "tennis";
    }

    @ResponseBody
    @GetMapping("/boramae")
    public String boramaeCoatInfo() throws InterruptedException, JsonProcessingException {
        return objectMapper.writeValueAsString(webDriverUtil.getBoramaeCoatInfo());
    }
}
