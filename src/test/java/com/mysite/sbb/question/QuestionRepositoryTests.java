package com.mysite.sbb.question;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserRepository;
import jakarta.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class QuestionRepositoryTests {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Question 생성")
    @Transactional
    void CreateQuestion() {
        // given
        Question question = new Question();
        question.setSubject("subject");
        question.setContent("content");

        // when
        Question saveQuestion = questionRepository.save(question);
        Optional<Question> findQuestion =
            questionRepository.findById(saveQuestion.getId());

        // then
        assertTrue(findQuestion.isPresent());
        assertThat(findQuestion.get().getId()).isEqualTo(question.getId());
    }

    @Test
    @DisplayName("Question 조회")
    @Transactional
    void SelectQuestion() {
        // given
        Question question = new Question();
        question.setSubject("subject");
        question.setContent("content");

        Question question2 = new Question();
        question2.setSubject("subject2");
        question2.setContent("content2");

        // when
        questionRepository.save(question);
        questionRepository.save(question2);
        List<Question> findQuestionList = questionRepository.findAll();

        // then
        assertThat(findQuestionList).isNotEmpty();
    }

    @Test
    @DisplayName("Question 삭제")
    @Transactional
    void DeleteQuestion() {
        // given
        Question question = new Question();
        question.setSubject("subject");
        question.setContent("content");

        // when
        Question saveQuestion = questionRepository.save(question);
        questionRepository.deleteById(saveQuestion.getId());
        Optional<Question> findQuestion =
            questionRepository.findById(question.getId());

        // then
        assertFalse(findQuestion.isPresent());
    }

    @Test
    @DisplayName("Question 갱신")
    @Transactional
    void UpdateQuestion() {
        // given
        Question question = new Question();
        question.setSubject("subject");
        question.setContent("content");

        // when
        Question saveQuestion = questionRepository.save(question);
        questionRepository.deleteById(saveQuestion.getId());
        Optional<Question> findQuestion =
            questionRepository.findById(saveQuestion.getId());

        // then
        assertFalse(findQuestion.isPresent());
    }

    @Test
    @DisplayName("특정 유저의 최근 Question 조회")
    @Transactional
    void SelectCurrentQuestion() {
        // given
        SiteUser user = new SiteUser();
        user.setUsername("user1");
        userRepository.save(user);

        for (int i = 0; i < 10; i++) {
            Question question = new Question();
            question.setSubject("subject" + i);
            question.setContent("content" + i);
            question.setCreateDate(LocalDateTime.now());
            question.setAuthor(user);
            questionRepository.save(question);
        }

        Pageable pageable = PageRequest.of(0, 5);
        String username = "user1";

        // when
        List<Question> questionList =
            questionRepository.findCurrentQuestion(username, pageable);

        // then
        System.out.println("======================================");
        for (int i = 0; i < questionList.size(); i++) {
            System.out.println(questionList.get(i).getCreateDate());
        }
        System.out.println("======================================");
        assertThat(questionList).isNotEmpty();
    }
}
