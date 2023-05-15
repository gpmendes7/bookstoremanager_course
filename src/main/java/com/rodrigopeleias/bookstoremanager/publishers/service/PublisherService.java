package com.rodrigopeleias.bookstoremanager.publishers.service;

import com.rodrigopeleias.bookstoremanager.publishers.mappers.PublisherMapper;
import com.rodrigopeleias.bookstoremanager.publishers.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublisherService {

    private final static PublisherMapper publisherMapper = PublisherMapper.INSTANCEE;

    private PublisherRepository publisherRepository;

    @Autowired
    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

}
