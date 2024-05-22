package ru.otus.hw.dto;

import lombok.Getter;
import lombok.Setter;
import ru.otus.hw.models.Authority;

import java.util.List;

@Getter
@Setter
public class UserDto {

    private long id;

    private String username;

    private String password;

    private Boolean enabled;

    private List<Authority> authorities;
}
