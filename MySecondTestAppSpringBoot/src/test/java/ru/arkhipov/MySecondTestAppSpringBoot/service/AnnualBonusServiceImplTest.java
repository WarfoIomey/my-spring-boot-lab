package ru.arkhipov.MySecondTestAppSpringBoot.service;

import org.junit.jupiter.api.Test;
import ru.arkhipov.MySecondTestAppSpringBoot.model.Positions;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AnnualBonusServiceImplTest {

    @Test
    void calculate() {
        Positions positions = Positions.HR;
        double bonus = 2.0;
        int workDays = 243;
        double salary = 100000.00;

        double result = new AnnualBonusServiceImpl().calculate(positions, salary, bonus, workDays);
        double expected = 360493.8271604938;
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void calculateQuarterlyBonus_ForManager_ShouldReturnCorrectValue() {
        AnnualBonusServiceImpl service = new AnnualBonusServiceImpl();
        Positions managerPosition = Positions.TEAM_LEAD;
        double salary = 100000.0;
        double bonus = 0.1;
        int workDays = 60;

        double result = service.calculateQuarterlyBonus(managerPosition, salary, bonus, workDays);
        double expected = 100000.0 * 0.1 * 2.8 * 90.0 / 60.0;

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void calculateQuarterlyBonus_ForNonManager_ShouldThrowException() {
        AnnualBonusServiceImpl service = new AnnualBonusServiceImpl();
        Positions nonManagerPosition = Positions.DEV;
        double salary = 100000.0;
        double bonus = 0.1;
        int workDays = 60;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.calculateQuarterlyBonus(nonManagerPosition, salary, bonus, workDays));

        assertThat(exception.getMessage()).isEqualTo("Квартальная премия доступна только для менеджеров и управленцев");
    }

    @Test
    void calculateQuarterlyBonus_ForAllManagerPositions_ShouldWork() {
        AnnualBonusServiceImpl service = new AnnualBonusServiceImpl();

        Positions[] managerPositions = {
                Positions.TEAM_LEAD,
                Positions.PROJECT_MANAGER,
                Positions.DEPARTMENT_HEAD
        };

        for (Positions position : managerPositions) {
            assertDoesNotThrow(() -> {
                double result = service.calculateQuarterlyBonus(position, 100000.0, 0.1, 60);
                assertThat(result).isGreaterThan(0);
            });
        }
    }

    @Test
    void calculate_WithDifferentYears_ShouldUseCorrectDaysCount() {
        AnnualBonusServiceImpl service = new AnnualBonusServiceImpl();
        Positions position = Positions.DEV;
        double salary = 100000.0;
        double bonus = 2.0;
        int workDays = 243;

        double result = service.calculate(position, salary, bonus, workDays);

        assertThat(result).isPositive();
        assertThat(result).isGreaterThan(100000);
    }
}