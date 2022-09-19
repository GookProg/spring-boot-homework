package com.edu.ulab.app.service;


import com.edu.ulab.app.dto.BookDto;

import java.util.List;

public interface BookService {
    BookDto createBook(BookDto userDto);

    List<Long> getBooksByUserId(Long userId);

    void deleteBooksByUserId(Long userId);
}
