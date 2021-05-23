package com.mishyn.system.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "COMMENTS")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(columnDefinition="TEXT")
    private String body;

    @ManyToOne
    @JoinColumn(name="ticket_id", nullable=false)
    private TicketEntity ticket;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private UserEntity createdBy;
}
