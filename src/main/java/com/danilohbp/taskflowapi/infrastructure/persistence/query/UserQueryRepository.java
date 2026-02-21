package com.danilohbp.taskflowapi.infrastructure.persistence.query;

import com.danilohbp.taskflowapi.application.query.user.dto.UserView;
import com.danilohbp.taskflowapi.domain.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface UserQueryRepository extends Repository<User, Long> {

    @Query("""
        select new com.danilohbp.taskflowapi.application.query.user.dto.UserView(
            u.id, u.name, u.email, u.role, u.active, u.createdAt, u.updatedAt
        )
        from User u
        order by u.name asc
    """)
    List<UserView> listUsers();

    @Query("""
        select new com.danilohbp.taskflowapi.application.query.user.dto.UserView(
            u.id, u.name, u.email, u.role, u.active, u.createdAt, u.updatedAt
        )
        from User u
        where u.id = :id
    """)
    Optional<UserView> findUserById(Long id);
}
