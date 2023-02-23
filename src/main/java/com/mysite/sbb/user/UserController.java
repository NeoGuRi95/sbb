package com.mysite.sbb.user;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.mysite.sbb.answer.AnswerService;
import com.mysite.sbb.comment.CommentService;

import com.mysite.sbb.question.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final CommentService commentService;

    private static final String SIGNUP_FORM = "signup_form";
    private static final String TEMP_PASSWORD_FORM = "temp_password_form";

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return SIGNUP_FORM;
    }

    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return SIGNUP_FORM;
        }

        if (!userCreateForm.getPassword1()
            .equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                "2개의 패스워드가 일치하지 않습니다.");
            return SIGNUP_FORM;
        }

        userService.create(userCreateForm.getUsername(),
            userCreateForm.getEmail(), userCreateForm.getPassword1());
        
        // try {
        //     userService.create(userCreateForm.getUsername(),
        //         userCreateForm.getEmail(), userCreateForm.getPassword1());
        // } catch (DataIntegrityViolationException e) {
        //     e.printStackTrace();
        //     bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
        //     return SIGNUP_FORM;
        // } catch (Exception e) {
        //     e.printStackTrace();
        //     bindingResult.reject("signupFailed", e.getMessage());
        //     return SIGNUP_FORM;
        // }
        
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login_form";
    }

    @GetMapping("/tempPassword")
    public String tempPassword(TempPasswordForm tempPasswordForm) {
        return TEMP_PASSWORD_FORM;
    }

    @PostMapping("/tempPassword")
    public String sendTempPassword(@Valid TempPasswordForm tempPasswordForm,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return TEMP_PASSWORD_FORM;
        }

        userService.modifyPassword(tempPasswordForm.getEmail());

        // try {
        //     userService.modifyPassword(tempPasswordForm.getEmail());
        // } catch (DataNotFoundException e) {
        //     e.printStackTrace();
        //     bindingResult.reject("emailNotFound", e.getMessage());
        //     return TEMP_PASSWORD_FORM;
        // } catch (EmailException e) {
        //     e.printStackTrace();
        //     bindingResult.reject("sendEmailFail", e.getMessage());
        //     return TEMP_PASSWORD_FORM;
        // }
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String profile(Model model,
        @AuthenticationPrincipal PrincipalDetails principal) {
        String username = principal.getUsername();
        model.addAttribute("username", username);
        model.addAttribute("questionList",
            questionService.getCurrentListByUser(username, 5));
        model.addAttribute("answerList",
            answerService.getCurrentListByUser(username, 5));
        model.addAttribute("commentList",
            commentService.getCurrentListByUser(username, 5));
        return "profile";
    }
}
