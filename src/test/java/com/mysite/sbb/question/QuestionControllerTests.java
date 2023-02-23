package com.mysite.sbb.question;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import com.mysite.sbb.answer.AnswerService;
import com.mysite.sbb.category.CategoryService;
import com.mysite.sbb.user.UserService;

@WebMvcTest(QuestionController.class)
public class QuestionControllerTests {

    @Autowired
    MockMvc mvc;
    
    @MockBean
    private QuestionService questionService;
    @MockBean
    private UserService userService;
    @MockBean
    private AnswerService answerService;
    @MockBean
    private CategoryService categoryService;

    @Test
    @DisplayName("질문글 전체 조회")
    void getQuestionList() throws Exception {
        //when
        mvc.perform(get("/question/list")
            .with(csrf()))
            .andDo(print());
    }

}
