package ru.arkhipov.MySpringBoot2Dbase.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.arkhipov.MySpringBoot2Dbase.dao.DesciplineDAO;
import ru.arkhipov.MySpringBoot2Dbase.entity.Descipline;

import java.util.List;
import java.util.Optional;

@Service
public class DesciplineServiceImpl implements DesciplineService {

    @Autowired
    private DesciplineDAO desciplineDAO;

    @Override
    public List<Descipline> getAllDesciplines() {
        return desciplineDAO.findAll();
    }

    @Override
    public Descipline getDescipline(int id) {
        Optional<Descipline> optional = desciplineDAO.findById(id);
        return optional.orElse(null);
    }

    @Override
    public Descipline saveDescipline(Descipline descipline) {
        return desciplineDAO.save(descipline);
    }

    @Override
    public void deleteDescipline(int id) {
        desciplineDAO.deleteById(id);
    }

    @Override
    public List<Descipline> findBySemester(int semester) {
        return desciplineDAO.findAll().stream()
                .filter(subject -> subject.getSemester() == semester)
                .toList();
    }

    @Override
    public List<Descipline> findOptionalDesciplines() {
        return desciplineDAO.findAll().stream()
                .filter(Descipline::isOptional)
                .toList();
    }
}
