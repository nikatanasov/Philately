package Philately.user.model;

import Philately.stamp.model.Stamp;
import Philately.stamp.model.WishedStamp;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "owner")
    private List<Stamp> stamps = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "owner")
    private List<WishedStamp> wishedStamps = new ArrayList<>();
}
