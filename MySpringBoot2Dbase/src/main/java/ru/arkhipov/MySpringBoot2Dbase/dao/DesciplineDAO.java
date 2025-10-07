package ru.arkhipov.MySpringBoot2Dbase.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.arkhipov.MySpringBoot2Dbase.entity.Descipline;

public interface DesciplineDAO extends JpaRepository<Descipline, Integer> {
}
