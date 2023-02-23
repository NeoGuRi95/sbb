package com.mysite.sbb.user;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import com.mysite.sbb.answer.AnswerService;
import com.mysite.sbb.comment.CommentService;
import com.mysite.sbb.exception.ErrorCode;
import com.mysite.sbb.exception.UserDataIntegrityViolationException;
import com.mysite.sbb.question.QuestionService;

@WebMvcTest(UserController.class)
public class UserControllerTests {
    @Autowired
    MockMvc mvc;

    @MockBean
    private UserService userService;
    @MockBean
    private QuestionService questionService;
    @MockBean
    private AnswerService answerService;
    @MockBean
    private CommentService commentService;
    @MockBean
    private BindingResult bindingResult;

    @Test
    @DisplayName("유저 생성")
    @WithMockUser
    void createUser() throws Exception {
        //given

        //when
        when(userService.create(anyString(), anyString(), anyString()))
            .thenThrow(new UserDataIntegrityViolationException(ErrorCode.SIGN_UP_FAIL));
        when(bindingResult.hasErrors()).thenReturn(false);

        mvc.perform(post("/user/signup")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("email", "jea5158@naver.com")
            .param("username", "jea5158")
            .param("password1", "1234")
            .param("password2", "1234")
            .with(csrf()))
        //then
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("SIGN_UP_FAIL"))
            .andExpect(jsonPath("$.message").value("이미 등록된 사용자입니다"));
    }

}
