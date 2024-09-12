package com.cydeo.repository;

import com.cydeo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserName(String username);

    @Transactional
    void deleteByUserName(String username);

    @Query("SELECT u FROM User u JOIN Role r ON u.role.id = r.id WHERE r.description = 'Manager'")
    List<User> fetchManagers();

    List<User> findAllByRole_DescriptionIgnoreCase(String description);

}
