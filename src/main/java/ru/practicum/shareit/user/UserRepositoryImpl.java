package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final List<User> userList;

    @Override
    public List<User> findAll() {
        return userList;
    }

    @Override
    public User save(User user) {

        if (userList.size() > 0) {
            for (User user1 : userList) {
                if (user1.getEmail().equals(user.getEmail())) {
                    throw new IllegalArgumentException("Duplicate email");
                }
            }
        }

        if (userList.size() > 0) {
            user.setId(userList.size() + 1);
            userList.add(user);
        } else {
            user.setId(1);
            userList.add(user);
        }

        return user;
    }
}
