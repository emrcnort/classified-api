package com.sahibinden.service;

import com.sahibinden.dto.ClassifiedDto;
import com.sahibinden.dto.ClassifiedStateDto;
import com.sahibinden.dto.StatusCountDto;
import com.sahibinden.enums.Category;
import com.sahibinden.enums.Status;
import com.sahibinden.exceptions.BadWordsException;
import com.sahibinden.exceptions.NotFoundException;
import com.sahibinden.mapper.ClassifiedStateMapper;
import com.sahibinden.model.Classified;
import com.sahibinden.model.ClassifiedState;
import com.sahibinden.repository.ClassifiedRepository;
import com.sahibinden.utils.BadWordsFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassifiedService {

    private final BadWordsFilter badWordsFilter;
    private final ClassifiedRepository classifiedRepository;
    private final ClassifiedStateMapper classifiedStateMapper;

    @CacheEvict(value = "classifiedCache", allEntries = true)
    public void createClassified(ClassifiedDto classifiedDto) throws BadWordsException {
        validateClassified(classifiedDto);

        Classified classified = buildClassified(classifiedDto);
        setClassifiedStatus(classified,classifiedDto);

        saveClassifiedWithState(classified);
    }

    private ClassifiedState createClassifiedState(Status oldStatus, Status newStatus) {
        ClassifiedState classifiedState = new ClassifiedState();
        classifiedState.setChangeDate(LocalDateTime.now());
        classifiedState.setOldStatus(oldStatus);
        classifiedState.setNewStatus(newStatus);
        return classifiedState;
    }

    private void saveClassifiedWithState(Classified classified) {
        ClassifiedState classifiedState = createClassifiedState(null, classified.getStatus());
        classified.getStatusChanges().add(classifiedState);
        classifiedRepository.save(classified);
    }

    private void setClassifiedStatus(Classified classified, ClassifiedDto classifiedDto) {
        if (classifiedRepository.existsByCategoryAndTitleAndDetail(classifiedDto.getCategory(), classifiedDto.getTitle(), classifiedDto.getDetail())) {
            classified.setStatus(Status.MUKERRER);
        } else {
            classified.setStatus(
                    Category.EMLAK.equals(classifiedDto.getCategory()) ||
                            Category.VASITA.equals(classifiedDto.getCategory()) ||
                            Category.DIGER.equals(classifiedDto.getCategory())
                            ? Status.ONAY_BEKLIYOR
                            : Status.AKTIF
            );
        }
    }

    private void validateClassified(ClassifiedDto classifiedDto) throws BadWordsException {
        if (badWordsFilter.containsBadWords(classifiedDto.getTitle())) {
            throw new BadWordsException();
        }
    }

    private Classified buildClassified(ClassifiedDto classifiedDto) {
        Classified classified = new Classified();
        classified.setCategory(classifiedDto.getCategory());
        classified.setDetail(classifiedDto.getDetail());
        classified.setEndDate(classifiedDto.calculateEndDate(classifiedDto.getCategory()));
        classified.setTitle(classifiedDto.getTitle());
        classified.setCreateDate(LocalDateTime.now());
        return classified;
    }

    @CachePut(value = "classifiedCache", key = "#id")
    public void changeClassifiedStatus(Long id, Status newStatus) {
     Classified classified = classifiedRepository.findById(id).orElseThrow(NotFoundException::new);
     Status oldStatus = classified.getStatus();
     classified.setStatus(newStatus);

     ClassifiedState classifiedState = new ClassifiedState();
     classifiedState.setChangeDate(LocalDateTime.now());
     classifiedState.setOldStatus(oldStatus);
     classifiedState.setNewStatus(newStatus);
     classifiedState.setClassified(classified);

     classified.getStatusChanges().add(classifiedState);
     classifiedRepository.save(classified);

    }

   @Cacheable(value = "classifiedCache", key = "'statistics'")
    public List<StatusCountDto> getClassifiedStatistics() {
        Map<Status, Long> allClassifiedsByStatus = classifiedRepository.findAll().stream()
                .collect(Collectors.groupingBy(Classified::getStatus, Collectors.counting()));
        return convertMapToStatusCountDtoList(allClassifiedsByStatus);
    }

    private List<StatusCountDto> convertMapToStatusCountDtoList (Map<Status, Long> allClassifiedsByStatusMap){
        return allClassifiedsByStatusMap.entrySet().stream()
                .map(entry -> new StatusCountDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "classifiedCache", key = "#id")
    public List<ClassifiedStateDto> getAllClassifiedStatusChanges(Long id) {
        Classified classified = classifiedRepository.findById(id).orElseThrow(NotFoundException::new);
        List<ClassifiedState> classifiedStateList = classified.getStatusChanges();
        return classifiedStateMapper.convertEntityListToDtoList(classifiedStateList);
    }
}
