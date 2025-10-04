package ru.arkhipov.MySecondTestAppSpringBoot.model;

import lombok.Getter;

@Getter
public enum Positions {
    DEV(2.2, false, "Разработчик"),
    HR(1.7, false, "Специалист по персоналу"),
    QA(1.9, false, "Тестировщик"),
    TEAM_LEAD(2.8, true, "Тимлид"),
    PROJECT_MANAGER(2.5, true, "Менеджер проекта"),
    DEPARTMENT_HEAD(3.0, true, "Начальник отдела");

    private final double positionCoefficient;
    private final boolean isManager;
    private final String description;

    Positions(double positionCoefficient, boolean isManager, String description) {
        this.positionCoefficient = positionCoefficient;
        this.isManager = isManager;
        this.description = description;
    }
}