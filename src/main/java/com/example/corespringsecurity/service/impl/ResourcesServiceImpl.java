package com.example.corespringsecurity.service.impl;

import com.example.corespringsecurity.domain.entity.Resources;
import com.example.corespringsecurity.repository.ResourcesRepository;
import com.example.corespringsecurity.service.ResourcesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @Transactional
@Slf4j
@RequiredArgsConstructor
public class ResourcesServiceImpl implements ResourcesService {

    private final ResourcesRepository resourcesRepository;

    @Override
    public Resources getResources(Long id) {
        return resourcesRepository.findById(id)
                .orElse(new Resources());
    }

    @Override
    public List<Resources> getResources() {
        return resourcesRepository.findAll(
                Sort.by(
                        Sort.Order.asc("orderNum")
                )
        );
    }

    @Override
    public void createResources(Resources resources) {
        resourcesRepository.save(resources);
    }

    @Override
    public void deleteResources(Long id) {
        resourcesRepository.deleteById(id);
    }
}
