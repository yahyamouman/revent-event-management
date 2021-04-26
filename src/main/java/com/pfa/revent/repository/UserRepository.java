package com.pfa.revent.repository;

import com.pfa.revent.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
/*    @Query("SELECT u FROM User u WHERE u.username LIKE %?2%")
    List<User> queryFindByUsername(String username);
    Optional<User> findByUsername(String username);
    @Query("SELECT u FROM User u WHERE u.roles LIKE %?1%")
    List<User> findByRoles(String roles);
    Optional<User> findByEmail(String username);
    @Query("SELECT u.userId FROM User u WHERE u.username=?1")
    long getUserIdByUsernameExact(String Username);*/

    Optional<User> findByUsername(String username);

    @Query("SELECT u.parentModerator FROM User u WHERE u.userId=?1")
    User getParentByUserId(long userId);

    @Query("SELECT u FROM User u WHERE u.parentModerator.userId=?1")
    Page<User> findChildUsersByParentId(long parentId, Pageable pageable);
}