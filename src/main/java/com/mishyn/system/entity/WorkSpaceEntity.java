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
@Table(name = "WORKSPACES")
public class WorkSpaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workspace_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "workspace", fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<TicketEntity> ticketEntities = new HashSet<>();

    public WorkSpaceEntity(String name) {
        this.name = name;
    }
}
