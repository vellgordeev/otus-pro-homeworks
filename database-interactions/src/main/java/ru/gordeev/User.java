package ru.gordeev;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@RepositoryTable(title = "users")
public class User {
    @RepositoryIdField
    @RepositoryField
    private Long id;

    @RepositoryField
    private String login;

    @RepositoryField
    private String password;

    @RepositoryField
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
