package com.mishyn.system.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TICKETS")
public class TicketEntity {

    public enum TicketStatus {
        NEW, IN_PROGRESS, ON_HOLD, ON_REVIEW, DONE, CANCELED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long id;

    private String title;

    @Column(columnDefinition="TEXT")
    private String body;

    @ManyToOne
    @JoinColumn(name = "workspace_id", nullable = false)
    private WorkSpaceEntity workspace;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity createdBy;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private UserEntity assignee;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    private LocalDateTime dueDate;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "ticket", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<CommentEntity> comments = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime modifiedAt;

}
