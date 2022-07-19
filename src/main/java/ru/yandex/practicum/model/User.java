package ru.yandex.practicum.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Value;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {

    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Long> friendsId;

    public void setAndCheckFriendsId(long friendsId) {
        if (this.friendsId == null) {
            this.friendsId = new HashSet<>();
            this.friendsId.add(friendsId);
        } else {
            this.friendsId.add(friendsId);
        }
    }
}
