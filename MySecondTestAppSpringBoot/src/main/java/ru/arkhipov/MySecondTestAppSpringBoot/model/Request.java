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

    @NotBlank(message = "UID обязателен")
    @Size(max = 32, message = "UID не должен превышать 32 символа")
    private String uid;

    @NotBlank(message = "OperationUid обязателен")
    @Size(max = 32, message = "OperationUid не должен превышать 32 символа")
    private String operationUid;

    private String systemName;

    @NotBlank(message = "SystemTime обязателен")
    private String systemTime;

    private String source;

    @NotNull(message = "CommunicationId обязателен")
    @Min(value = 1, message = "CommunicationId должен быть не менее 1")
    @Max(value = 100000, message = "CommunicationId должен быть не более 100000")
    private Integer communicationId;

    private Integer templateId;
    private Integer productCode;
    private Integer smsCode;
}
