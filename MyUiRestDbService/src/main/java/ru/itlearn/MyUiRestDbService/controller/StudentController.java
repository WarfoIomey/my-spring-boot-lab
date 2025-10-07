package ru.itlearn.MyUiRestDbService.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import ru.itlearn.MyUiRestDbService.dao.StudentRepository;
import ru.itlearn.MyUiRestDbService.entity.Student;

import java.util.Optional;

@Slf4j
@RestController
public class StudentController {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentController(StudentRepository studentRepository) {this.studentRepository = studentRepository;}
    @GetMapping({"/list","/"})
    public ModelAndView getAllStudents() {
        ModelAndView mav = new ModelAndView("list-Students");
        mav.addObject("students", studentRepository.findAll());
        return mav;
    }
    @GetMapping("/addStudentForm")
    public ModelAndView addStudentForm() {
        ModelAndView mav = new ModelAndView("add-student-form");
        Student student = new Student();
        mav.addObject("student", student);
        return mav;
    }
    @PostMapping("/saveStudent")
    public RedirectView saveStudent(@ModelAttribute Student student) {
        studentRepository.save(student);
        return new RedirectView("/list");
    }
    @GetMapping("/showUpdateForm")
    public ModelAndView showUpdateForm(@RequestParam Long studentId) {
        ModelAndView mav = new ModelAndView("add-student-form");
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        Student student = new Student();
        if (optionalStudent.isPresent()) {
            student = optionalStudent.get();
        }
        mav.addObject("student", student);
        return mav;
    }
    @GetMapping("/deleteStudent")
    public RedirectView deleteStudent(@RequestParam Long studentId, ModelAndView model) {
        studentRepository.deleteById(studentId);
        return new RedirectView("/list");
    }
}
