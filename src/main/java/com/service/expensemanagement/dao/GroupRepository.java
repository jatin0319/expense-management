package com.service.expensemanagement.dao;

import com.service.expensemanagement.models.Expense;
import com.service.expensemanagement.models.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
}
