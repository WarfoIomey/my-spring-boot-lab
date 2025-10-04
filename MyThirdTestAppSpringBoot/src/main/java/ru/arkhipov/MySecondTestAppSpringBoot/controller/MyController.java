package ru.arkhipov.MySecondTestAppSpringBoot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.arkhipov.MySecondTestAppSpringBoot.exception.UnsupportedCodeException;
import ru.arkhipov.MySecondTestAppSpringBoot.exception.ValidationFailedException;
import ru.arkhipov.MySecondTestAppSpringBoot.model.*;
import ru.arkhipov.MySecondTestAppSpringBoot.service.ValidationService;

import java.util.Date;
import java.text.SimpleDateFormat;

import jakarta.validation.Valid;

@Slf4j
@RestController
public class MyController {

    private final ValidationService validationService;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    @Autowired
    public MyController(ValidationService validationService) {
        this.validationService = validationService;
    }

    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request,
                                             BindingResult bindingResult) {

        long service2ReceivedTime = System.currentTimeMillis();
        log.info("Получен request в Сервисе 2. Время получения: {}", service2ReceivedTime);
        log.info("Request в Сервисе 2: {}", request);

        try {
            Date requestTime = dateFormat.parse(request.getSystemTime());
            long requestOriginalTime = requestTime.getTime();

            long timeDifference = service2ReceivedTime - requestOriginalTime;

            log.info("ИЗМЕРЕНИЕ ВРЕМЕНИ");
            log.info("Время создания запроса (из systemTime): {}", requestOriginalTime);
            log.info("Время получения в Сервисе 2: {}", service2ReceivedTime);
            log.info("Общее время от создания до получения в Сервисе 2: {} мс", timeDifference);
            log.info("КОНЕЦ ИЗМЕРЕНИЯ");

        } catch (Exception e) {
            log.error("Ошибка при парсинге времени: {}", e.getMessage());
        }

        Response response = Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(dateFormat.format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();

        log.info("Создан response в Сервисе 2: {}", response);

        try {
            log.info("Начало валидации request в Сервисе 2");
            validationService.isValid(bindingResult);
            log.info("Валидация request в Сервисе 2 успешно пройдена");

            if ("123".equals(request.getUid())) {
                log.warn("Обнаружен неподдерживаемый UID в Сервисе 2: {}", request.getUid());
                throw new UnsupportedCodeException("UID 123 не поддерживается");
            }

            log.info("Обработка request в Сервисе 2 завершена успешно");

        } catch (UnsupportedCodeException e) {
            log.error("UnsupportedCodeException в Сервисе 2: {}", e.getMessage(), e);
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNSUPPORTED_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNSUPPORTED);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } catch (ValidationFailedException e) {
            log.error("ValidationFailedException в Сервисе 2: {}", e.getMessage(), e);
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.VALIDATION_EXCEPTION);
            response.setErrorMessage(ErrorMessages.VALIDATION);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            log.error("Неизвестная ошибка в Сервисе 2: {}", e.getMessage(), e);
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNKNOWN_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNKNOWN);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.info("Завершение обработки в Сервисе 2. Final response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}