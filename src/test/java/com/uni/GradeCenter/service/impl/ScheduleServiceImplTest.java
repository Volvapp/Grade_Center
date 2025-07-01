package com.uni.GradeCenter.service.impl;

import com.uni.GradeCenter.model.*;
import com.uni.GradeCenter.model.dto.AvailableSlotDTO;
import com.uni.GradeCenter.model.dto.bindingDTOs.CreateScheduleBindingDTO;
import com.uni.GradeCenter.model.dto.viewDTOs.ScheduleViewDTO;
import com.uni.GradeCenter.repository.ParentRepository;
import com.uni.GradeCenter.repository.ScheduleRepository;
import com.uni.GradeCenter.repository.TeacherRepository;
import com.uni.GradeCenter.service.*;
import com.uni.GradeCenter.service.Impl.ParentServiceImpl;
import com.uni.GradeCenter.service.Impl.ScheduleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceImplTest {

    @Mock private ScheduleRepository scheduleRepository;
    @Mock private TeacherService teacherService;
    @Mock private ClassroomService classroomService;
    @Mock private SubjectService subjectService;
    @Mock private TeacherRepository teacherRepository;

    @InjectMocks private ScheduleServiceImpl scheduleService;

    private Teacher teacher;
    private Subject subject;
    private Classroom classroom;
    private Schedule schedule;

    @BeforeEach
    void setUp() {
        subject = new Subject();
        subject.setId(1L);
        subject.setName("Math");

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");

        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setUser(user);
        teacher.setQualifiedSubjects(List.of(subject));
        teacher.setSchedules(new ArrayList<>());

        classroom = new Classroom();
        classroom.setId(1L);
        classroom.setName("A101");
        classroom.setSchedules(new ArrayList<>());

        schedule = new Schedule();
        schedule.setId(1L);
        schedule.setDayOfWeek(DayOfWeek.MONDAY);
        schedule.setStartTime(LocalTime.of(8, 0));
        schedule.setEndTime(LocalTime.of(8, 45));
        schedule.setTeacher(teacher);
        schedule.setSubject(subject);
        schedule.setClassroom(classroom);
    }

    @Test
    void testCreateSchedule() {
        when(scheduleRepository.save(schedule)).thenReturn(schedule);
        Schedule saved = scheduleService.createSchedule(schedule);
        assertEquals(schedule, saved);
    }

    @Test
    void testGetScheduleById_found() {
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        Schedule found = scheduleService.getScheduleById(1L);
        assertEquals(DayOfWeek.MONDAY, found.getDayOfWeek());
    }

    @Test
    void testGetScheduleById_notFound() {
        when(scheduleRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> scheduleService.getScheduleById(999L));
    }

    @Test
    void testUpdateSchedule_found() {
        when(scheduleRepository.findById(schedule.getId())).thenReturn(Optional.of(schedule));
        when(scheduleRepository.save(schedule)).thenReturn(schedule);
        Schedule updated = scheduleService.updateSchedule(schedule);
        assertEquals(schedule, updated);
    }

    @Test
    void testDeleteScheduleById() {
        scheduleService.deleteScheduleById(1L);
        verify(scheduleRepository).deleteById(1L);
    }

    @Test
    void testGetAllSchedules() {
        when(scheduleRepository.findAll()).thenReturn(List.of(schedule));
        List<Schedule> result = scheduleService.getAllSchedules();
        assertEquals(1, result.size());
    }

    @Test
    void testInitializeSchedules_createsSchedule() {
        when(scheduleRepository.count()).thenReturn(0L);
        when(teacherService.getAllTeachers()).thenReturn(List.of(teacher));
        when(classroomService.getAllClassrooms()).thenReturn(List.of(classroom));
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);

        scheduleService.initializeSchedules();

        verify(scheduleRepository).save(any(Schedule.class));
    }

    @Test
    void testInitializeSchedules_skipsIfAlreadyInitialized() {
        when(scheduleRepository.count()).thenReturn(1L);
        scheduleService.initializeSchedules();
        verify(scheduleRepository, never()).save(any());
    }

    @Test
    void testGetAllByIds() {
        List<Long> ids = List.of(1L, 2L);
        when(scheduleRepository.findAllById(ids)).thenReturn(List.of(schedule));
        List<Schedule> result = scheduleService.getAllByIds(ids);
        assertEquals(1, result.size());
    }

    @Test
    void testGetDailyLessonSlots_correctSlotCount() {
        List<LocalTime> slots = scheduleService.getDailyLessonSlots();
        assertEquals(7, slots.size());
        assertEquals(LocalTime.of(8, 0), slots.get(0));
    }

    @Test
    void testGetAvailableStartTimesWithTeachers_returnsAvailableSlots() {
        when(teacherRepository.findBySchoolAndQualifiedSubjectsContaining(any(), any()))
                .thenReturn(List.of(teacher));
        when(scheduleRepository.findByClassroom_SchoolAndDayOfWeek(any(), any()))
                .thenReturn(new ArrayList<>());

        List<AvailableSlotDTO> result = scheduleService.getAvailableStartTimesWithTeachers(classroom, subject, DayOfWeek.MONDAY);
        assertFalse(result.isEmpty());
    }

    @Test
    void testCreateScheduleFrontend_success() {
        CreateScheduleBindingDTO dto = new CreateScheduleBindingDTO();
        dto.setClassroomId(1L);
        dto.setSubjectId(1L);
        dto.setTeacherName("John Doe");
        dto.setStartTime(LocalTime.of(9, 0));
        dto.setDayOfWeek(DayOfWeek.TUESDAY);

        when(classroomService.getClassroomById(1L)).thenReturn(classroom);
        when(subjectService.getSubjectById(1L)).thenReturn(subject);
        when(teacherRepository.findBySchoolAndQualifiedSubjectsContaining(any(), eq(subject)))
                .thenReturn(List.of(teacher));
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);

        scheduleService.createScheduleFrontend(dto);

        verify(scheduleRepository).save(any(Schedule.class));
        verify(classroomService).updateClassroom(classroom);
        verify(teacherService).updateTeacher(teacher);
    }

    @Test
    void testCreateScheduleFrontend_teacherNotFound_throws() {
        CreateScheduleBindingDTO dto = new CreateScheduleBindingDTO();
        dto.setClassroomId(1L);
        dto.setSubjectId(1L);
        dto.setTeacherName("Invalid Name");

        when(classroomService.getClassroomById(1L)).thenReturn(classroom);
        when(subjectService.getSubjectById(1L)).thenReturn(subject);
        when(teacherRepository.findBySchoolAndQualifiedSubjectsContaining(any(), eq(subject)))
                .thenReturn(List.of(teacher)); // John Doe

        assertThrows(RuntimeException.class, () -> scheduleService.createScheduleFrontend(dto));
    }

    @Test
    void testFindByTeacherAndSubject() {
        when(scheduleRepository.findByTeacherAndSubject(teacher, subject)).thenReturn(List.of(schedule));
        List<Schedule> result = scheduleService.findByTeacherAndSubject(teacher, subject);
        assertEquals(1, result.size());
    }

    @Test
    void testMapToView() {
        List<ScheduleViewDTO> views = scheduleService.mapToView(List.of(schedule));
        assertEquals(1, views.size());
        ScheduleViewDTO dto = views.get(0);
        assertEquals("Math", dto.getName());
        assertEquals("John Doe", dto.getTeacherName());
    }
}

