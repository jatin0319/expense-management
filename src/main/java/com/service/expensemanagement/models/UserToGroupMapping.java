package com.service.expensemanagement.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "user_to_group_mapping")
public class UserToGroupMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "expense_amount_per_user", scale = 2)
    private BigDecimal expenseAmountPerUser;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "group_id")
    private Group group;

}
