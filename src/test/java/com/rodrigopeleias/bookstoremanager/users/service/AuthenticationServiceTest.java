package com.rodrigopeleias.bookstoremanager.users.service;

import com.rodrigopeleias.bookstoremanager.users.builder.UserDTOBuilder;
import com.rodrigopeleias.bookstoremanager.users.dto.UserDTO;
import com.rodrigopeleias.bookstoremanager.users.entity.User;
import com.rodrigopeleias.bookstoremanager.users.mapper.UserMapper;
import com.rodrigopeleias.bookstoremanager.users.repository.UserRepository;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    private UserDTOBuilder userDTOBuilder;

    @BeforeEach
    void setUp() {
        userDTOBuilder = UserDTOBuilder.builder().build();
    }

    @Test
    void whenUsernameIsInformedThenUserShouldBeReturned() {
        UserDTO expectedFoundUserDTO = userDTOBuilder.buildUserDTO();
        User expectedFoundUser = userMapper.toModel(expectedFoundUserDTO);
        SimpleGrantedAuthority expectedUserRole =  new SimpleGrantedAuthority("ROLE_" + expectedFoundUserDTO.getRole().getDescription().toUpperCase());
        String expectedUsername = expectedFoundUserDTO.getUsername();

        Mockito.when(userRepository.findByUsername(expectedUsername)).thenReturn(Optional.of(expectedFoundUser));

        UserDetails userDetails = authenticationService.loadUserByUsername(expectedUsername);

        MatcherAssert.assertThat(userDetails.getUsername(), Matchers.is(Matchers.equalTo(expectedFoundUser.getUsername())));
        MatcherAssert.assertThat(userDetails.getPassword(), Matchers.is(Matchers.equalTo(expectedFoundUser.getPassword())));
        Assertions.assertTrue(userDetails.getAuthorities().contains(expectedUserRole));
    }

    @Test
    void whenInvalidUsernameIsInformedThenAnExceptionShouldBeThrown() {
        UserDTO expectedFoundUserDTO = userDTOBuilder.buildUserDTO();
        String expectedUsername = expectedFoundUserDTO.getUsername();

        Mockito.when(userRepository.findByUsername(expectedUsername)).thenReturn(Optional.empty());

        Assertions.assertThrows(UsernameNotFoundException.class, () -> authenticationService.loadUserByUsername(expectedUsername));
    }
}
