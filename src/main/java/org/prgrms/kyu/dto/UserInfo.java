package org.prgrms.kyu.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.kyu.entity.User;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class UserInfo {
    private Long id;
    private String email;
    private String name;
    private String nickname;
    private Long balance;
    private String address;
    private String type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserInfo(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.nickname = user.getNickname();
        this.balance = user.getBalance();
        this.address = user.getAddress();
        this.type = user.getType().name();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }
}
