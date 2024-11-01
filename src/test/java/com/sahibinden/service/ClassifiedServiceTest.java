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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClassifiedServiceTest {

    @Mock
    private ClassifiedStateMapper classifiedStateMapper;

    @Mock
    private BadWordsFilter badWordsFilter;

    @Mock
    private ClassifiedRepository classifiedRepository;

    @InjectMocks
    private ClassifiedService classifiedService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateClassified_ShouldThrowBadWordsException_WhenTitleContainsBadWords() {
        ClassifiedDto classifiedDto = new ClassifiedDto();
        classifiedDto.setTitle("opsiyonlu");

        when(badWordsFilter.containsBadWords(classifiedDto.getTitle())).thenReturn(true);

        assertThrows(BadWordsException.class, () -> classifiedService.createClassified(classifiedDto));
        verify(badWordsFilter, times(1)).containsBadWords(classifiedDto.getTitle());
    }

    @Test
    void testCreateClassified_ShouldSetStatusMukerrer_WhenClassifiedExists() throws BadWordsException {
        ClassifiedDto classifiedDto = new ClassifiedDto();
        classifiedDto.setCategory(Category.EMLAK);
        classifiedDto.setTitle("title");
        classifiedDto.setDetail("detail");
        classifiedDto.setCreateDate(LocalDateTime.now());

        when(badWordsFilter.containsBadWords(classifiedDto.getTitle())).thenReturn(false);
        when(classifiedRepository.existsByCategoryAndTitleAndDetail(classifiedDto.getCategory(), classifiedDto.getTitle(), classifiedDto.getDetail())).thenReturn(true);

        classifiedService.createClassified(classifiedDto);

        ArgumentCaptor<Classified> classifiedCaptor = ArgumentCaptor.forClass(Classified.class);
        verify(classifiedRepository, times(1)).save(classifiedCaptor.capture());
        assertEquals(Status.MUKERRER, classifiedCaptor.getValue().getStatus());
    }

    @Test
    void testCreateClassified_ShouldSetStatusOnayBekliyor_WhenCategoryIsEmlak() throws BadWordsException {
        ClassifiedDto classifiedDto = new ClassifiedDto();
        classifiedDto.setCategory(Category.EMLAK);
        classifiedDto.setTitle("title");
        classifiedDto.setDetail("detail");

        when(badWordsFilter.containsBadWords(classifiedDto.getTitle())).thenReturn(false);
        when(classifiedRepository.existsByCategoryAndTitleAndDetail(classifiedDto.getCategory(), classifiedDto.getTitle(), classifiedDto.getDetail())).thenReturn(false);

        classifiedService.createClassified(classifiedDto);

        ArgumentCaptor<Classified> classifiedCaptor = ArgumentCaptor.forClass(Classified.class);
        verify(classifiedRepository, times(1)).save(classifiedCaptor.capture());
        assertEquals(Status.ONAY_BEKLIYOR, classifiedCaptor.getValue().getStatus());
    }

    @Test
    void testChangeClassifiedStatus_ShouldThrowNotFoundException_WhenClassifiedNotFound() {
        Long id = 1L;
        Status newStatus = Status.AKTIF;

        when(classifiedRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> classifiedService.changeClassifiedStatus(id, newStatus));
    }

    @Test
    void testChangeClassifiedStatus_ShouldUpdateStatus_WhenClassifiedFound() {
        Long id = 1L;
        Status newStatus = Status.AKTIF;
        Classified classified = new Classified();
        classified.setStatus(Status.ONAY_BEKLIYOR);

        when(classifiedRepository.findById(id)).thenReturn(Optional.of(classified));

        classifiedService.changeClassifiedStatus(id, newStatus);

        assertEquals(newStatus, classified.getStatus());
        verify(classifiedRepository, times(1)).save(classified);
    }

    @Test
    void testGetClassifiedStatistics_WhenMethodCalls_ShouldReturnCorrectClassifiedStatistics() {
        Classified classified1 = new Classified();
        classified1.setStatus(Status.AKTIF);
        Classified classified2 = new Classified();
        classified2.setStatus(Status.DEAKTIF);
        Classified classified3 = new Classified();
        classified3.setStatus(Status.AKTIF);

        List<Classified> classifieds = Arrays.asList(classified1, classified2, classified3);
        when(classifiedRepository.findAll()).thenReturn(classifieds);

        List<StatusCountDto> result = classifiedService.getClassifiedStatistics();

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(dto -> dto.getStatus() == Status.AKTIF && dto.getCount() == 2));
        assertTrue(result.stream().anyMatch(dto -> dto.getStatus() == Status.DEAKTIF && dto.getCount() == 1));
    }

    @Test
    void testGetAllClassifiedStatusChanges_whenMethodCallsById_shouldReturnAllClassifiedStatusChanges() {
        Long classifiedId = 1L;
        Classified classified = new Classified();
        classified.setId(classifiedId);
        ClassifiedState state1 = new ClassifiedState();
        ClassifiedState state2 = new ClassifiedState();
        List<ClassifiedState> states = Arrays.asList(state1, state2);
        classified.setStatusChanges(states);

        when(classifiedRepository.findById(classifiedId)).thenReturn(Optional.of(classified));
        when(classifiedStateMapper.convertEntityListToDtoList(states)).thenReturn(Arrays.asList(new ClassifiedStateDto(), new ClassifiedStateDto()));

        List<ClassifiedStateDto> result = classifiedService.getAllClassifiedStatusChanges(classifiedId);

        assertEquals(2, result.size());
    }

    @Test
    void testGetAllClassifiedStatusChanges_whenMethodCallsById_shouldThrowNotFoundException() {
        Long classifiedId = 1L;
        when(classifiedRepository.findById(classifiedId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> classifiedService.getAllClassifiedStatusChanges(classifiedId));
    }
}
