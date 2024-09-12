package com.cydeo.entity;

import com.cydeo.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "projects")
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_deleted = false")
public class Project extends BaseEntity {

    private String projectName;

    @Column(nullable = false, unique = true)
    private String projectCode;

    @ManyToOne
    private User assignedManager;

    @Column(columnDefinition = "DATE")
    private LocalDate startDate;

    @Column(columnDefinition = "DATE")
    private LocalDate endDate;

    private String projectDetail;

    @Enumerated(EnumType.STRING)
    private Status projectStatus;

    private int completeTaskCounts;

    private int unfinishedTaskCounts;


}
