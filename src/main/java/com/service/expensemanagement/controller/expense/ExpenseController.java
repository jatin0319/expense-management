package com.service.expensemanagement.controller.expense;

import com.service.expensemanagement.dto.CreateExpenseRequestDto;
import com.service.expensemanagement.dto.CreateExpenseResponseDto;
import com.service.expensemanagement.dto.ExpenseListResponseDto;
import com.service.expensemanagement.helper.JwtUtil;
import com.service.expensemanagement.service.expense.ExpenseService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/expense")
public class ExpenseController {

    private static final String CREATE_EXPENSE_DETAILS = "create-expense-details";
    private static final String FETCH_USER_EXPENSE_LIST = "fetch-user-expense-list" ;
    private final ExpenseService expenseService;
    private final JwtUtil jwtUtil;

    @Autowired
    public ExpenseController(ExpenseService expenseService, JwtUtil jwtUtil) {
        this.expenseService = expenseService;
        this.jwtUtil = jwtUtil;
    }

    @ApiOperation(value = "Api to add expense")
    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<CreateExpenseResponseDto> createExpenseDetails(@Valid @RequestBody CreateExpenseRequestDto createExpenseRequest) {
        return new ResponseEntity<>(expenseService.createExpenseDetails(createExpenseRequest, CREATE_EXPENSE_DETAILS), HttpStatus.OK);
    }

    @ApiOperation(value = "Api to get list of user specific expenses")
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<ExpenseListResponseDto>> getUserSpecificExpenseList(@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                                                   @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                                                   HttpServletRequest request){
        String username = jwtUtil.extractUsername(request.getHeader("Authorization").split("Bearer ")[1]);
        List<ExpenseListResponseDto> expenseListResponse =
                expenseService.getUserSpecificExpenseList(pageNumber, pageSize, username, FETCH_USER_EXPENSE_LIST);
        return new ResponseEntity<>(expenseListResponse, HttpStatus.OK);
    }
}
