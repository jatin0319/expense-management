package com.service.expensemanagement.service.expense;

import com.service.expensemanagement.dto.CreateExpenseRequestDto;
import com.service.expensemanagement.dto.CreateExpenseResponseDto;
import com.service.expensemanagement.dto.ExpenseListResponseDto;

import java.util.List;

public interface ExpenseService {
    CreateExpenseResponseDto createExpenseDetails(CreateExpenseRequestDto createExpenseRequest, String operation);

    List<ExpenseListResponseDto> getUserSpecificExpenseList(Integer pageNumber, Integer pageSize, String username, String operation);
}
