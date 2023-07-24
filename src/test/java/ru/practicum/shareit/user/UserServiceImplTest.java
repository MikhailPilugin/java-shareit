package ru.practicum.shareit.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.EmailExistingException;
import ru.practicum.shareit.exceptions.UserNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;
    @Mock
    UserRepository userRepository;
    User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("User Name");
        user.setEmail("user@email.com");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void add_whenAddUser_thenSaveUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User returnedUser = userService.add(user);

        assertThat(returnedUser, equalTo(user));
        verify(userRepository).save(user);
    }

    @Test
    void update_whenAddUser_thenSaveUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userRepository.findByEmailContainingIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User returnedUser = userService.update(user);

        assertThat(returnedUser, equalTo(user));
        verify(userRepository).save(user);
        verify(userRepository).findByEmailContainingIgnoreCase(user.getEmail());
        verify(userRepository).findById(user.getId());
    }

    @Test
    void update_whenAddUserWithExistingEmail_thenThrowEmailExistingException() {
        User existingUser = new User(user.getId() + 1, "Existing user", user.getEmail());
        when(userRepository.findByEmailContainingIgnoreCase(anyString())).thenReturn(Optional.of(existingUser));

        EmailExistingException exception = assertThrows(EmailExistingException.class, () ->
                userService.update(user));

        assertThat(exception.getMessage(), equalTo(String.format(
                "User with email: %s is already registered", user.getEmail())));
        verify(userRepository, never()).save(user);
        verify(userRepository).findByEmailContainingIgnoreCase(user.getEmail());
    }

    @Test
    void update_whenAddUserWithIncorrectId_thenThrowUserNotFoundException() {
        when(userRepository.findByEmailContainingIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () ->
                userService.update(user));

        assertThat(exception.getMessage(), equalTo(String.format("User with id=%d not found", user.getId())));
        verify(userRepository, never()).save(user);
        verify(userRepository).findByEmailContainingIgnoreCase(user.getEmail());
        verify(userRepository).findById(user.getId());
    }

    @Test
    void update_whenAddUserWithUpdatedFields_thenSaveUserWithNewFields() {
        User updatedUser = new User(user.getId(), "Updated user", "updated@email.com");
        when(userRepository.save(any(User.class))).thenAnswer(invocationOnMock ->
                invocationOnMock.getArgument(0, User.class));
        when(userRepository.findByEmailContainingIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User returnedUser = userService.update(updatedUser);

        assertThat(returnedUser, equalTo(updatedUser));
        verify(userRepository).save(updatedUser);
        verify(userRepository).findByEmailContainingIgnoreCase(updatedUser.getEmail());
        verify(userRepository).findById(updatedUser.getId());
    }

    @Test
    void getById_whenIdPresented_thenReturnUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User returnedUser = userService.getById(user.getId());

        assertThat(returnedUser, equalTo(user));
        verify(userRepository).findById(user.getId());
    }

    @Test
    void getById_whenIdNotPresented_thenThrowUserNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () ->
                userService.getById(user.getId()));

        assertThat(exception.getMessage(), equalTo(String.format("User with id=%d not found", user.getId())));
        verify(userRepository).findById(user.getId());
    }

    @Test
    void getAll_whenInvoke_thenReturnAllUsers() {
        User secondUser = new User(user.getId() + 1, "Second user", "second@email.com");
        List<User> userList = List.of(user, secondUser);
        when(userRepository.findAll()).thenReturn(userList);

        List<User> result = userService.getAll();

        assertThat(result, allOf(notNullValue(), hasSize(2)));
        assertThat(result, allOf(hasItem(user), hasItem(secondUser)));
        verify(userRepository).findAll();
    }

    @Test
    void deleteById_whenUserIdPresented_thenDeleteUser() {
        when(userRepository.existsById(anyLong())).thenReturn(Boolean.TRUE);
        doNothing().when(userRepository).deleteById(anyLong());

        userService.delete(user.getId());

        verify(userRepository).existsById(user.getId());
        verify(userRepository).deleteById(user.getId());
    }

    @Test
    void deleteById_whenUserIdPresented_thenThrowUserNotFoundException() {
        when(userRepository.existsById(anyLong())).thenReturn(Boolean.FALSE);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () ->
                userService.delete(user.getId()));

        assertThat(exception.getMessage(), equalTo(String.format("User with id=%d not found", user.getId())));
        verify(userRepository).existsById(user.getId());
        verify(userRepository, never()).deleteById(user.getId());
    }

    @Test
    void checkUserExistence_whenUserIdPresented_thenDoNothing() {
        when(userRepository.existsById(anyLong())).thenReturn(Boolean.TRUE);

        userService.checkUser(user.getId());

        verify(userRepository).existsById(user.getId());
    }

    @Test
    void checkUserExistence_whenUserIdNotPresented_thenThrowUserNotFoundException() {
        when(userRepository.existsById(anyLong())).thenReturn(Boolean.FALSE);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () ->
                userService.checkUser(user.getId()));

        assertThat(exception.getMessage(), equalTo(String.format("User with id=%d not found", user.getId())));
        verify(userRepository).existsById(user.getId());
    }
}