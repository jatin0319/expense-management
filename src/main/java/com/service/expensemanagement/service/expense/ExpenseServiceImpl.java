package com.service.expensemanagement.service.expense;

import com.service.expensemanagement.dao.GroupRepository;
import com.service.expensemanagement.dao.ExpenseRepository;
import com.service.expensemanagement.dao.UserRepository;
import com.service.expensemanagement.dao.UserToGroupMappingRepository;
import com.service.expensemanagement.dto.CreateExpenseRequestDto;
import com.service.expensemanagement.dto.CreateExpenseResponseDto;
import com.service.expensemanagement.dto.ExpenseListResponseDto;
import com.service.expensemanagement.models.Expense;
import com.service.expensemanagement.models.Group;
import com.service.expensemanagement.models.User;
import com.service.expensemanagement.models.UserToGroupMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;
    private final GroupRepository groupRepository;
    private final UserToGroupMappingRepository userToGroupMappingRepository;

    @Autowired
    public ExpenseServiceImpl(UserRepository userRepository, ExpenseRepository expenseRepository,
                              GroupRepository groupRepository, UserToGroupMappingRepository userToGroupMappingRepository) {
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
        this.groupRepository = groupRepository;
        this.userToGroupMappingRepository = userToGroupMappingRepository;
    }

    @Override
    public CreateExpenseResponseDto createExpenseDetails(CreateExpenseRequestDto createExpenseRequest, String operation) {
        this.validateRequestParameter(createExpenseRequest);

        Group group = groupRepository.findById(createExpenseRequest.getGroupId())
                .orElseThrow(() -> new RuntimeException("Group Not Found"));

        List<UserToGroupMapping> userToGroupMappingList = userToGroupMappingRepository.findByGroup(group);
        if (userToGroupMappingList.isEmpty())
            throw new RuntimeException("No user is present in the group");

        BigDecimal amountPerPerson = createExpenseRequest.getTotalAmount().divide(BigDecimal.valueOf(userToGroupMappingList.size()))
                .setScale(2, RoundingMode.HALF_UP);
        userToGroupMappingList = userToGroupMappingList.stream()
                .map(mapping -> mapping.toBuilder().expenseAmountPerUser(amountPerPerson).build())
                .collect(Collectors.toList());

        Expense expense = Expense.builder()
                .totalExpenseAmount(createExpenseRequest.getTotalAmount())
                .group(group)
                .createdOn(new Date())
                .build();

        expenseRepository.save(expense);
        userToGroupMappingRepository.saveAll(userToGroupMappingList);

        return CreateExpenseResponseDto.builder()
                .message("Expense created successfully")
                .build();
    }

    private void validateRequestParameter(CreateExpenseRequestDto createExpenseRequest) {
        if (createExpenseRequest.getGroupId() == null)
            throw new RuntimeException("User Ids cannot be null");

        if (createExpenseRequest.getTotalAmount() == null)
            throw new RuntimeException("Total amount cannot null");
    }

    @Override
    public List<ExpenseListResponseDto> getUserSpecificExpenseList(Integer pageNumber, Integer pageSize,
                                                                   String username, String operation) {
        List<ExpenseListResponseDto> expenseListResponse = new ArrayList<>();
        this.validateQueryParameter(pageNumber, pageSize);
        Pageable pagination = PageRequest.of(pageNumber, pageSize);

        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("User not found"));

        List<UserToGroupMapping> groupMappingRepositoryByUser = userToGroupMappingRepository.findByUser(user, pagination);
        groupMappingRepositoryByUser.forEach( entry -> expenseListResponse.add(ExpenseListResponseDto.builder()
                        .amount(entry.getExpenseAmountPerUser().toEngineeringString())
                        .groupId(entry.getGroup().getId())
                        .groupName(entry.getGroup().getGroupName())
                .build()));

        return expenseListResponse;
    }

    private void validateQueryParameter(Integer pageNumber, Integer pageSize){
        if (pageNumber <0 || pageNumber >999)
            throw new RuntimeException("Invalid page number");

        if (pageSize <0 || pageSize >999)
            throw new RuntimeException("Invalid page size");
    }
}
