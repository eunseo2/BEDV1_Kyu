package org.prgrms.kyu.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.kyu.dto.JoinRequest;

import javax.persistence.*;

@Entity
@Table(name = "Users")
@Getter
@NoArgsConstructor
public class User extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private Long balance;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private UserType type;

    public User(JoinRequest form) {
        this.email = form.getEmail();
        this.password = form.getPassword();
        this.name = form.getName();
        this.nickname = form.getNickname();
        this.balance = 1000000L;
        this.address = form.getAddress();
        this.type = UserType.valueOf(form.getType());
    }
}
