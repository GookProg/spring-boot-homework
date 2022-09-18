package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class Storage {
    //todo создать хранилище в котором будут содержаться данные
    // сделать абстракции через которые можно будет производить операции с хранилищем
    // продумать логику поиска и сохранения
    // продумать возможные ошибки
    // учесть, что при сохранеии юзера или книги, должен генерироваться идентификатор
    // продумать что у узера может быть много книг и нужно создать эту связь
    // так же учесть, что методы хранилища принимают друго тип данных - учесть это в абстракции

    private Map<Long, User> userStorage = new HashMap<>();
    private Map<Long, Book> bookStorage = new HashMap<>();
    private AtomicLong userCounter = new AtomicLong();
    private AtomicLong bookCounter = new AtomicLong();

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
}
