package com.service.expensemanagement.dao;

import com.service.expensemanagement.models.Group;
import com.service.expensemanagement.models.User;
import com.service.expensemanagement.models.UserToGroupMapping;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserToGroupMappingRepository extends JpaRepository<UserToGroupMapping, Integer> {
    List<UserToGroupMapping> findByGroup(Group group);
    List<UserToGroupMapping> findByUser(User group, Pageable pageable);
}
