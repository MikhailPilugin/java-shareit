package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    public static Map<Integer, User> userMap = new HashMap<>();
    static int idUser = 1;

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
            ++idUser;
            user.setId(idUser);
            userMap.putIfAbsent(idUser, user);
        } else {
            user.setId(idUser);
            userMap.put(idUser, user);
        }
        return user;
    }

    @Override
    public User update(User user, Integer userId) {
        User newUser = userMap.get(userId);

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

    @Override
    public void delete(Integer userId) {
        Map<Integer, User> newUserMap = new HashMap<>();

        for (Map.Entry<Integer, User> userEntry : userMap.entrySet()) {
            if (userEntry.getValue().getId() != userId) {
                newUserMap.put((int) userEntry.getValue().getId(), userEntry.getValue());
            }
        }

        userMap.clear();
        userMap = newUserMap;
    }
}
