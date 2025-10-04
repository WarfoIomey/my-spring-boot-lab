package ru.arkhipov.MySecondTestAppSpringBoot.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response {
    /**
     * Уникальный идентификатор сообщения
     */
    private String uid;

    /**
     * Уникальный идентификатор операции
     */
    private String operationUid;

    /**
     * Время создания сообщения в системе
     */
    private String systemTime;

    /**
     * Код выполнения операции
     */
    private Codes code;

    /**
     * Годовой бонус сотрудника
     */
    private Double annualBonus;

    /**
     * Код ошибки
     */
    private ErrorCodes errorCode;

    /**
     * Сообщение об ошибке
     */
    private ErrorMessages errorMessage;
}