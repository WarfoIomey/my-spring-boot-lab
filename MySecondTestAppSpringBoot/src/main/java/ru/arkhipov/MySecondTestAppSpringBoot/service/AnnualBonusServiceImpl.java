package ru.arkhipov.MySecondTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import ru.arkhipov.MySecondTestAppSpringBoot.model.Positions;

import java.time.Year;

@Service
public class AnnualBonusServiceImpl implements AnnualBonusService {

    @Override
    public double calculate(Positions positions, double salary, double bonus, int workDays) {
        int daysInYear = isLeapYear() ? 366 : 365;
        return salary * bonus * daysInYear * positions.getPositionCoefficient() / workDays;
    }

    /**
     * Проверяет, является ли текущий год високосным
     * @return true если год високосный, false в противном случае
     */
    private boolean isLeapYear() {
        return Year.now().isLeap();
    }

    /**
     * Вычисляет количество дней в текущем году
     * @return 366 для високосного года, 365 для обычного
     */
    private int getDaysInYear() {
        return Year.now().length();
    }

    /**
     * Вычисляет квартальную премию для менеджеров
     * @param positions должность сотрудника
     * @param salary зарплата сотрудника
     * @param bonus бонусный коэффициент
     * @param workDays количество рабочих дней
     * @return размер квартальной премии
     * @throws IllegalArgumentException если сотрудник не является менеджером
     */
    public double calculateQuarterlyBonus(Positions positions, double salary, double bonus, int workDays) {
        if (!positions.isManager()) {
            throw new IllegalArgumentException("Квартальная премия доступна только для менеджеров и управленцев");
        }

        return salary * bonus * positions.getPositionCoefficient() * 90.0 / workDays;
    }
}