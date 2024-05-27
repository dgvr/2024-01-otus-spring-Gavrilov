package ru.otus.hw.dto;

import ru.otus.hw.models.Authority;
import ru.otus.hw.models.User;

import java.util.ArrayList;
import java.util.List;


public class UserDtoConverter {

    public static UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        userDto.setEnabled(user.getEnabled());

        if (user.getAuthorities() != null) {
            List<Authority> authorityList = new ArrayList<>(user.getAuthorities().size());
            user.getAuthorities().forEach(authority -> {
                authorityList.add(new Authority(authority.getId(), authority.getAuthority()));
            });
            userDto.setAuthorities(authorityList);
        }
        return userDto;
    }
}
