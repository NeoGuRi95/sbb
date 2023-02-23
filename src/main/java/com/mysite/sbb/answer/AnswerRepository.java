package com.mysite.sbb.answer;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.mysite.sbb.question.Question;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
	Page<Answer> findAllByQuestion(Question question, Pageable pagealbe);

	@Query("select a "
		+ "from Answer a "
		+ "join SiteUser u on a.author=u "
		+ "where u.username = :username "
		+ "order by a.createDate desc ")
	List<Answer> findCurrentAnswer(@Param("username") String username,
		Pageable pageable);
}
