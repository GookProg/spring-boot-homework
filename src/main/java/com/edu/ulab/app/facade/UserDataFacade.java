package com.edu.ulab.app.facade;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.web.request.UserBookRequest;
import com.edu.ulab.app.web.response.UserBookResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class UserDataFacade {
    private final UserService userService;
    private final BookService bookService;
    private final UserMapper userMapper;
    private final BookMapper bookMapper;

    public UserDataFacade(UserService userService,
                          BookService bookService,
                          UserMapper userMapper,
                          BookMapper bookMapper) {
        this.userService = userService;
        this.bookService = bookService;
        this.userMapper = userMapper;
        this.bookMapper = bookMapper;
    }

    public UserBookResponse createUserWithBooks(UserBookRequest userBookRequest) {
        log.info("Got user book create request: {}", userBookRequest);
        UserDto userDto = userMapper.userRequestToUserDto(userBookRequest.getUserRequest());
        log.info("Mapped user request: {}", userDto);

        UserDto createdUser = userService.createUser(userDto);
        log.info("Created user: {}", createdUser);

        List<Long> bookIdList = collectBookIds(userBookRequest, createdUser);
        log.info("Collected book ids: {}", bookIdList);

        return UserBookResponse.builder()
                .userId(createdUser.getId())
                .fullName(createdUser.getFullName())
                .title(createdUser.getTitle())
                .age(createdUser.getAge())
                .booksIdList(bookIdList)
                .build();
    }

    public List<Long> collectBookIds(UserBookRequest userBookRequest, UserDto userDto) {
        return userBookRequest.getBookRequests()
                .stream()
                .filter(Objects::nonNull)
                .map(bookMapper::bookRequestToBookDto)
                .peek(bookDto -> bookDto.setUserId(userDto.getId()))
                .peek(mappedBookDto -> log.info("mapped book: {}", mappedBookDto))
                .map(bookService::createBook)
                .peek(createdBook -> log.info("Created book: {}", createdBook))
                .map(BookDto::getId)
                .toList();
    }

    public UserBookResponse updateUserWithBooks(UserBookRequest userBookRequest, Long userId) {
        log.info("Got user book create request: {}", userBookRequest);
        UserDto userDto = userMapper.userRequestToUserDto(userBookRequest.getUserRequest());
        log.info("Mapped user request: {}", userDto);

        UserDto updatedUser = userService.updateUser(userDto, userId);
        log.info("Updated user: {}", updatedUser);

        List<Long> bookIdList = collectBookIds(userBookRequest, updatedUser);
        log.info("Collected book ids: {}", bookIdList);

        return UserBookResponse.builder()
                .userId(updatedUser.getId())
                .fullName(updatedUser.getFullName())
                .title(updatedUser.getTitle())
                .age(updatedUser.getAge())
                .booksIdList(bookIdList)
                .build();
    }

    public UserBookResponse getUserWithBooks(Long userId) {
        log.info("Got user id: {}", userId);
        UserDto userDto = userService.getUserById(userId);
        log.info("Got user from storage: {}", userDto);

        List<Long> bookIdList = bookService.getBooksByUserId(userId);
        log.info("Collected book ids: {}", bookIdList);

        return UserBookResponse.builder()
                .userId(userId)
                .fullName(userDto.getFullName())
                .title(userDto.getTitle())
                .age(userDto.getAge())
                .booksIdList(bookIdList)
                .build();
    }

    public void deleteUserWithBooks(Long userId) {
        log.info("Got user id: {}", userId);
        bookService.deleteBooksByUserId(userId);
        log.info("Deleted books from user: {}", userId);
        userService.deleteUserById(userId);
        log.info("User with id {} deleted", userId);
    }
}
