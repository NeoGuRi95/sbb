package com.mysite.sbb.question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.category.Category;
import com.mysite.sbb.exception.ErrorCode;
import com.mysite.sbb.exception.DataNotFoundException;
import com.mysite.sbb.user.SiteUser;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    private Specification<Question> search(String kw, String categoryName) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Question> q,
                CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true); // 중복을 제거
                Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                Join<Question, Category> c = q.join("category", JoinType.LEFT);
                Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
                return cb.and(cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
                    cb.like(q.get("content"), "%" + kw + "%"), // 내용
                    cb.like(u1.get("username"), "%" + kw + "%"), // 질문 작성자
                    cb.like(a.get("content"), "%" + kw + "%"), // 답변 내용
                    cb.like(u2.get("username"), "%" + kw + "%")), // 답변 작성자
                    // and
                    cb.like(c.get("name"), "%" + categoryName + "%")); // 카테고리
                                                                       // 이름
            }
        };
    }

    public Page<Question> getList(int page, String kw, String categoryName) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Question> spec = search(kw, categoryName);
        return this.questionRepository.findAll(spec, pageable);
    }

    public List<Question> getCurrentListByUser(String username, int num) {
        Pageable pageable = PageRequest.of(0, num);
        return questionRepository.findCurrentQuestion(username, pageable);
    }

    public Question getQuestion(Integer id) {
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            return question.get();
        } else {
            throw new DataNotFoundException(ErrorCode.RESOURCE_NOT_FOUND);
        }
    }

    @Transactional
    public Question hitQuestion(Integer id) {
        Optional<Question> oquestion = this.questionRepository.findById(id);
        if (oquestion.isPresent()) {
            Question question = oquestion.get();
            question.setHit(question.getHit() + 1);
            return question;
        } else {
            throw new DataNotFoundException(ErrorCode.RESOURCE_NOT_FOUND);
        }
    }

    public void create(String subject, String content, SiteUser user,
        Category category) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        q.setAuthor(user);
        q.setCategory(category);
        this.questionRepository.save(q);
    }

    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        this.questionRepository.save(question);
    }

    public void delete(Question question) {
        this.questionRepository.delete(question);
    }

    public void vote(Question question, SiteUser siteUser) {
        question.getVoter().add(siteUser);
        this.questionRepository.save(question);
    }
}
