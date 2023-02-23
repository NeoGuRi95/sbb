package com.mysite.sbb.comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.mysite.sbb.exception.ErrorCode;
import com.mysite.sbb.exception.DataNotFoundException;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {

	private final CommentRepository commentRepository;

	public Comment create(Question question, SiteUser author, String content) {
		Comment c = new Comment();
		c.setQuestion(question);
		c.setAuthor(author);
		c.setContent(content);
		c.setCreateDate(LocalDateTime.now());
		c = this.commentRepository.save(c);
		return c;
	}

	public Comment getComment(Integer id) {
		Optional<Comment> comment = this.commentRepository.findById(id);
		if (comment.isPresent()) {
			return comment.get();
		} else {
			throw new DataNotFoundException(ErrorCode.RESOURCE_NOT_FOUND);
		}
	}

	public Comment modify(Comment c, String content) {
		c.setContent(content);
		c.setModifyDate(LocalDateTime.now());
		c = this.commentRepository.save(c);
		return c;
	}

	public void delete(Comment c) {
		this.commentRepository.delete(c);
	}

	public List<Comment> getCurrentListByUser(String username, int num) {
		Pageable pageable = PageRequest.of(0, num);
		return commentRepository.findCurrentComment(username, pageable);
	}
}
