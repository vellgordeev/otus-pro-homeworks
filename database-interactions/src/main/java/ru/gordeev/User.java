package ru.gordeev;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@RepositoryTable(title = "users")
public class User {

    @RepositoryIdField
    @RepositoryField(name = "id")
    private Long id;

    @RepositoryField(name = "login")
    private String login;

    @RepositoryField(name = "password")
    private String password;

    @RepositoryField(name = "nickname")
    private String nickname;

    public User(String login, String password, String nickname) {
        this.login = login;
        this.password = password;
        this.nickname = nickname;
    }

    public User(Long id, String login, String password, String nickname) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
