package ru.arkhipov.MySpringBoot2Dbase.service;

import org.springframework.stereotype.Service;
import ru.arkhipov.MySpringBoot2Dbase.entity.Descipline;

import java.util.List;

@Service
public interface DesciplineService {

    List<Descipline> getAllDesciplines();

    Descipline getDescipline(int id);

    Descipline saveDescipline(Descipline subject);

    void deleteDescipline(int id);

    List<Descipline> findBySemester(int semester);

    List<Descipline> findOptionalDesciplines();
}
