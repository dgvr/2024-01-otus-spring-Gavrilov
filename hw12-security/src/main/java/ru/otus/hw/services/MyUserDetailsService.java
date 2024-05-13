package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Authority;
import ru.otus.hw.models.User;
import ru.otus.hw.repositories.AuthorityRepository;
import ru.otus.hw.repositories.UserRepository;
import ru.otus.hw.security.MyUserPrincipal;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private final AuthorityRepository authorityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        List<Authority> authorities = authorityRepository.findAllByUsername(username);
        return new MyUserPrincipal(user, authorities);
    }
}
