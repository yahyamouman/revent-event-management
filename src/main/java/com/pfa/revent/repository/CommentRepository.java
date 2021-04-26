package com.pfa.revent.repository;

import com.pfa.revent.entity.Comment;
import com.pfa.revent.entity.Event;
import com.pfa.revent.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findCommentByCommentedEvent(Event commentedEvent);
    List<Comment> findCommentByCommentOwner(User commentOwner);
}
