package com.mishyn.system.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USERS")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    private String password;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "users_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<RoleEntity> roles = new HashSet<>();

    @OneToMany(mappedBy = "createdBy")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<TicketEntity> createdTickets = new HashSet<>();

    @OneToMany(mappedBy = "assignee")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<TicketEntity> assignedTickets = new HashSet<>();

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<CommentEntity> comments = new HashSet<>();

}
