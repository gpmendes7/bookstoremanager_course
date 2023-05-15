package com.rodrigopeleias.bookstoremanager.author.service;

import com.rodrigopeleias.bookstoremanager.author.builder.AuthorDTOBuilder;
import com.rodrigopeleias.bookstoremanager.author.dto.AuthorDTO;
import com.rodrigopeleias.bookstoremanager.author.entity.Author;
import com.rodrigopeleias.bookstoremanager.author.exception.AuthorAlreadyExistsException;
import com.rodrigopeleias.bookstoremanager.author.exception.AuthorNotFoundException;
import com.rodrigopeleias.bookstoremanager.author.mapper.AuthorMapper;
import com.rodrigopeleias.bookstoremanager.author.repository.AuthorRepository;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    private final AuthorMapper authorMapper = AuthorMapper.INSTANCE;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    private AuthorDTOBuilder authorDTOBuilder;

    @BeforeEach
    void setUp() {
        authorDTOBuilder = AuthorDTOBuilder.builder().build();
    }

    @Test
    void whenNewAuthorIsInformedThenItShouldBeCreated() {
        // given
        AuthorDTO expectedAuthorToCreateDTO = authorDTOBuilder.buildAuthorDTO();
        Author expectedCreatedAuthor = authorMapper.toModel(expectedAuthorToCreateDTO);

        //when
        Mockito.when(authorRepository.save(expectedCreatedAuthor)).thenReturn(expectedCreatedAuthor);
        Mockito.when(authorRepository.findByName(expectedAuthorToCreateDTO.getName())).thenReturn(Optional.empty());

        AuthorDTO createdAuthorDTO = authorService.create(expectedAuthorToCreateDTO);

        // then
        MatcherAssert.assertThat(createdAuthorDTO, Is.is(IsEqual.equalTo(expectedAuthorToCreateDTO)));
    }

    @Test
    void whenExistingAuthorIsInformedThenAnExceptionShouldBeThrown() {
        AuthorDTO expectedAuthorToCreateDTO = authorDTOBuilder.buildAuthorDTO();
        Author expectedCreatedAuthor = authorMapper.toModel(expectedAuthorToCreateDTO);

        Mockito.when(authorRepository.findByName(expectedAuthorToCreateDTO.getName())).thenReturn(Optional.of(expectedCreatedAuthor));

        Assertions.assertThrows(AuthorAlreadyExistsException.class, ()-> authorService.create(expectedAuthorToCreateDTO));
    }

    @Test
    void whenValidIdIsGivenThenAnAuthorShouldBeReturned() {
        AuthorDTO expectedFoundAuthorDTO = authorDTOBuilder.buildAuthorDTO();
        Author expectedFoundAuthor = authorMapper.toModel(expectedFoundAuthorDTO);

        Mockito.when(authorRepository.findById(expectedFoundAuthorDTO.getId())).thenReturn(Optional.of(expectedFoundAuthor));

        AuthorDTO foundAuthorDTO = authorService.findById(expectedFoundAuthorDTO.getId());

        MatcherAssert.assertThat(foundAuthorDTO, Is.is(IsEqual.equalTo(expectedFoundAuthorDTO)));
    }

    @Test
    void whenInvalidIdIsGivenThenAnExceptionShouldBeThrown() {
        AuthorDTO expectedFoundAuthorDTO = authorDTOBuilder.buildAuthorDTO();

        Mockito.when(authorRepository.findById(expectedFoundAuthorDTO.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(AuthorNotFoundException.class, ()-> authorService.findById(expectedFoundAuthorDTO.getId()));
    }

    @Test
    void whenListAuthorIsCalledThenItShouldBeReturned() {
        AuthorDTO expectedFoundAuthorDTO = authorDTOBuilder.buildAuthorDTO();
        Author expectedFoundAuthor = authorMapper.toModel(expectedFoundAuthorDTO);

        Mockito.when(authorRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundAuthor));

        List<AuthorDTO> foundAuthorsDTO = authorService.findAll();

        MatcherAssert.assertThat(foundAuthorsDTO.size(), Is.is(1));
        MatcherAssert.assertThat(foundAuthorsDTO.get(0), Is.is(IsEqual.equalTo(expectedFoundAuthorDTO)));
    }

    @Test
    void whenListAuthorsIsCalledThenAnEmptyListShouldBeReturned() {
        Mockito.when(authorRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

        List<AuthorDTO> foundAuthorsDTO = authorService.findAll();

        MatcherAssert.assertThat(foundAuthorsDTO.size(), Is.is(0));
    }

    @Test
    void whenValidAuthorIdIsGivenThenItShouldBeDeleted() {
        AuthorDTO expectedDeletedAuthorDTO = authorDTOBuilder.buildAuthorDTO();
        Author expectedDeletedAuthor = authorMapper.toModel(expectedDeletedAuthorDTO);

        Long expectedDeletedAuthorId = expectedDeletedAuthorDTO.getId();
        Mockito.doNothing().when(authorRepository).deleteById(expectedDeletedAuthorId);
        Mockito.when(authorRepository.findById(expectedDeletedAuthorId)).thenReturn(Optional.of(expectedDeletedAuthor));

        authorService.delete(expectedDeletedAuthorId);

        Mockito.verify(authorRepository, Mockito.times(1)).deleteById(expectedDeletedAuthorId);
        Mockito.verify(authorRepository, Mockito.times(1)).findById(expectedDeletedAuthorId);
    }

    @Test
    void whenInvalidAuthorIdIsGivenThenAnExceptionShouldBeThrown() {
        var expectedInvalidAuthorId = 2L;

        Mockito.when(authorRepository.findById(expectedInvalidAuthorId)).thenReturn(Optional.empty());

        Assertions.assertThrows(AuthorNotFoundException.class, ()-> authorService.delete(expectedInvalidAuthorId));
    }
}
