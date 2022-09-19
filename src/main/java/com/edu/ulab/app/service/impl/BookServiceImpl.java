package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.storage.Storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BookServiceImpl implements BookService {
    private final Storage storage;
    private final BookMapper bookMapper;

    public BookServiceImpl(Storage storage, BookMapper bookMapper) {
        this.storage = storage;
        this.bookMapper = bookMapper;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        Book book = bookMapper.bookDtoToBook(bookDto);
        bookDto.setId(storage.save(book));

        return bookDto;
    }

    @Override
    public List<Long> getBooksByUserId(Long userId) {
        User user = storage.findUserById(userId);
        List<Book> bookIdList = user.getBooks();

        return bookIdList
                .stream()
                .map(Book::getId)
                .toList();
    }

    @Override
    public void deleteBooksByUserId(Long userId) {
        List<Long> bookIdList = getBooksByUserId(userId);

        bookIdList.forEach(storage::deleteBookById);
    }
}
