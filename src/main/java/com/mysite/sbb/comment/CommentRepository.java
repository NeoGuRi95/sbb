package com.mysite.sbb.comment;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("select c "
        + "from Comment c "
        + "join SiteUser u on c.author=u "
        + "where u.username = :username "
        + "order by c.createDate desc ")
    List<Comment> findCurrentComment(@Param("username") String username,
        Pageable pageable);
}
