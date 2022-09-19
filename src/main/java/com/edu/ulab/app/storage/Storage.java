package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.exception.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class Storage {
    private final Map<Long, User> userStorage = new HashMap<>();
    private final Map<Long, Book> bookStorage = new HashMap<>();
    private final AtomicLong userCounter = new AtomicLong();
    private final AtomicLong bookCounter = new AtomicLong();

    public Long save(User user) {
        Long id = userCounter.incrementAndGet();
        user.setId(id);
        userStorage.put(id, user);
        return id;
    }

    public Long save(Book book) {
        Long id = bookCounter.incrementAndGet();
        book.setId(id);
        bookStorage.put(id, book);
        User user = userStorage.get(book.getUserId());
        user.addBook(book);

        return id;
    }

    public Long update(User user, Long id) {
        findUserById(id);
        userStorage.replace(id, user);

        return id;
    }

    public User findUserById(Long id) {
        if (userStorage.containsKey(id))
            return userStorage.get(id);
        else
            throw new NotFoundException("Cannot find user with id: " + id);
    }

    public void deleteUserById(Long id) {
        findUserById(id);
        userStorage.remove(id);
    }

    public Book findBookById(Long id) {
        if (bookStorage.containsKey(id))
            return bookStorage.get(id);
        else
            throw new NotFoundException("Cannot find book with id: " + id);
    }

    public void deleteBookById(Long id) {
        findBookById(id);
        bookStorage.remove(id);
    }
}
