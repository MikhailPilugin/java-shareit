package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final Map<Integer,User> userMap;

    @Override
    public Map<Integer, User> findAll() {
        return userMap;
    }

    @Override
    public User get(Integer userId) {
        User user = new User();

        for (Map.Entry<Integer, User> integerUserEntry : userMap.entrySet()) {
            if (integerUserEntry.getValue().getId() == userId) {
                user = integerUserEntry.getValue();
                break;
            }
        }

        return user;
    }

    @Override
    public User add(User user) {
        Set<String> email = new HashSet<>();
        for (Map.Entry<Integer, User> integerUserEntry : userMap.entrySet()) {
            email.add(integerUserEntry.getValue().getEmail());
        }

        if (email.contains(user.getEmail())) {
            throw new IllegalArgumentException("Email is exist");
        }

        if (userMap.size() > 0) {
            int index = userMap.size() + 1;
            user.setId(index);
            userMap.putIfAbsent(index, user);
        } else {
            user.setId(1);
            userMap.put(1, user);
        }

        return user;
    }

    @Override
    public User update(User user, Integer userId) {
        User newUser = new User();

        newUser = userMap.get(userId);

        if (user.getName() != null) {
            newUser.setName(user.getName());
        }

        if (user.getEmail() != null) {
            for (Map.Entry<Integer, User> userEntry : userMap.entrySet()) {
                if (userEntry.getValue().getEmail().equals(user.getEmail()) && userEntry.getValue().getId() != userId) {
                    throw new IllegalArgumentException("Email is exist already");
                }
            }

            newUser.setEmail(user.getEmail());
        }

        userMap.replace(userId, newUser);

        return newUser;
    }
}
