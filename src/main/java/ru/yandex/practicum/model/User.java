package ru.yandex.practicum.model;

import lombok.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class User {

    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
   // private Set<Long> friendsId; //!!!

//    public void setAndCheckFriendsId(long friendsId) {
//        if (this.friendsId == null) {
//            this.friendsId = new HashSet<>();
//            this.friendsId.add(friendsId);
//        } else {
//            this.friendsId.add(friendsId);
//        }
//    }
}
