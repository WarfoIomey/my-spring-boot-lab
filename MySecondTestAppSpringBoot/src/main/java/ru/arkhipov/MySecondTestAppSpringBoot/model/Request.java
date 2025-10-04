package ru.arkhipov.MySecondTestAppSpringBoot.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    /**
     * Уникальный идентификатор сообщения
     */
    @NotBlank(message = "UID обязателен")
    @Size(max = 32, message = "UID не должен превышать 32 символа")
    private String uid;

    /**
     * Уникальный идентификатор операции
     */
    @NotBlank(message = "OperationUid обязателен")
    @Size(max = 32, message = "OperationUid не должен превышать 32 символа")
    private String operationUid;

    /**
     * Имя системы отправителя
     */
    private Systems systemName;

    /**
     * Время создания сообщения в системе отправителя
     */
    @NotBlank(message = "SystemTime обязателен")
    private String systemTime;

    /**
     * Наименование ресурса
     */
    private String source;

    /**
     * Должность сотрудника
     */
    private Positions positions;

    /**
     * Зарплата сотрудника
     */
    private Double salary;

    /**
     * Бонусный коэффициент
     */
    private Double bonus;

    /**
     * Количество рабочих дней
     */
    private Integer workDays;

    /**
     * Идентификатор коммуникации
     */
    @NotNull(message = "CommunicationId обязателен")
    @Min(value = 1, message = "CommunicationId должен быть не менее 1")
    @Max(value = 100000, message = "CommunicationId должен быть не более 100000")
    private Integer communicationId;

    /**
     * Идентификатор шаблона
     */
    private Integer templateId;

    /**
     * Код продукта
     */
    private Integer productCode;

    /**
     * Код СМС
     */
    private Integer smsCode;

    @Override
    public String toString() {
        return "{" +
                "uid='" + uid + '\'' +
                ", operationUid='" + operationUid + '\'' +
                ", systemName=" + systemName +
                ", systemTime='" + systemTime + '\'' +
                ", source='" + source + '\'' +
                ", communicationId=" + communicationId +
                ", templateId=" + templateId +
                ", productCode=" + productCode +
                ", smsCode=" + smsCode +
                '}';
    }
}