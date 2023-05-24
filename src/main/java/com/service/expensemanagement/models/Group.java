package com.service.expensemanagement.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "group")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "description")
    private String description;

    @Transient
    @OneToMany(mappedBy = "group")
    private List<UserToGroupMapping> userToGroupMappingList = new ArrayList<>();

    @Transient
    @OneToOne(mappedBy = "group")
    private Expense expense;
}
