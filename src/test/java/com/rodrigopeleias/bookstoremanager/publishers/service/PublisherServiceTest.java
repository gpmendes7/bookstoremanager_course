package com.rodrigopeleias.bookstoremanager.publishers.service;

import com.rodrigopeleias.bookstoremanager.publishers.builder.PublisherDTOBuilder;
import com.rodrigopeleias.bookstoremanager.publishers.dto.PublisherDTO;
import com.rodrigopeleias.bookstoremanager.publishers.entity.Publisher;
import com.rodrigopeleias.bookstoremanager.publishers.exception.PublisherAlreadyExistsException;
import com.rodrigopeleias.bookstoremanager.publishers.exception.PublisherNotFoundException;
import com.rodrigopeleias.bookstoremanager.publishers.mappers.PublisherMapper;
import com.rodrigopeleias.bookstoremanager.publishers.repository.PublisherRepository;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PublisherServiceTest {

    private final static PublisherMapper publisherMapper = PublisherMapper.INSTANCE;

    @Mock
    private PublisherRepository publisherRepository;

    @InjectMocks
    private PublisherService publisherService;

    private PublisherDTOBuilder publisherDTOBuilder;

    @BeforeEach
    void setUp() {
        publisherDTOBuilder = PublisherDTOBuilder.builder().build();
    }

    @Test
    void whenNewPublisherIsInformedThenItShouldBeCreated() {
        PublisherDTO expectedPublisherToCreateDTO = publisherDTOBuilder.buildPublisherDTO();
        Publisher expectedPublisherCreated = publisherMapper.toModel(expectedPublisherToCreateDTO);

        Mockito.when(publisherRepository.findByNameOrCode(expectedPublisherToCreateDTO.getName(), expectedPublisherToCreateDTO.getCode()))
                        .thenReturn(Optional.empty());
        Mockito.when(publisherRepository.save(expectedPublisherCreated)).thenReturn(expectedPublisherCreated);

        PublisherDTO createdPublisherDTO = publisherService.create(expectedPublisherToCreateDTO);

        MatcherAssert.assertThat(createdPublisherDTO, Matchers.is(Matchers.equalTo(expectedPublisherToCreateDTO)));
    }

    @Test
    void whenExistingPublisherIsInformedThenAnExceptionShouldBeThrown() {
        PublisherDTO expectedPublisherToCreateDTO = publisherDTOBuilder.buildPublisherDTO();
        Publisher expectedPublisherDuplicated = publisherMapper.toModel(expectedPublisherToCreateDTO);

        Mockito.when(publisherRepository.findByNameOrCode(expectedPublisherToCreateDTO.getName(), expectedPublisherToCreateDTO.getCode()))
                .thenReturn(Optional.of(expectedPublisherDuplicated));

        Assertions.assertThrows(PublisherAlreadyExistsException.class, () -> publisherService.create(expectedPublisherToCreateDTO));
    }

    @Test
    void whenValidIdIsGivenThenAPublisherShouldBeReturned() {
        PublisherDTO expectedPublisherFoundDTO = publisherDTOBuilder.buildPublisherDTO();
        Publisher expectedPublisherFound = publisherMapper.toModel(expectedPublisherFoundDTO);
        var expectedPublisherFoundId = expectedPublisherFoundDTO.getId();

        Mockito.when(publisherRepository.findById(expectedPublisherFoundId)).thenReturn(Optional.of(expectedPublisherFound));

        PublisherDTO foundPublisherDTO = publisherService.findById(expectedPublisherFoundId);

        MatcherAssert.assertThat(foundPublisherDTO, Matchers.is(Matchers.equalTo(expectedPublisherFoundDTO)));
    }

    @Test
    void whenInvalidIdIsGivenThenAnExceptionShouldBeThrown() {
        PublisherDTO expectedPublisherFoundDTO = publisherDTOBuilder.buildPublisherDTO();

        var expectedPublisherFoundId = expectedPublisherFoundDTO.getId();

        Mockito.when(publisherRepository.findById(expectedPublisherFoundId)).thenReturn(Optional.empty());

        Assertions.assertThrows(PublisherNotFoundException.class, () -> publisherService.findById(expectedPublisherFoundId));
    }
}
