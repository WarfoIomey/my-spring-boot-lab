package ru.arkhipov.MySecondTestAppSpringBoot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.arkhipov.MySecondTestAppSpringBoot.exception.UnsupportedCodeException;
import ru.arkhipov.MySecondTestAppSpringBoot.exception.ValidationFailedException;
import ru.arkhipov.MySecondTestAppSpringBoot.model.*;
import ru.arkhipov.MySecondTestAppSpringBoot.service.ModifySourceRequestService;
import ru.arkhipov.MySecondTestAppSpringBoot.service.ModifyResponseService;
import ru.arkhipov.MySecondTestAppSpringBoot.service.ValidationService;

import java.util.Date;

import jakarta.validation.Valid;
import ru.arkhipov.MySecondTestAppSpringBoot.util.DateTimeUtil;

@Slf4j
@RestController
public class MyController {

    private final ValidationService validationService;
    private final ModifyResponseService modifyResponseService;
    private final ModifySourceRequestService modifySystemNameRequestService;
    private final ModifySourceRequestService modifySourceRequestService;

    @Autowired
    public MyController(ValidationService validationService,
                        @Qualifier("ModifySystemTimeResponseService") ModifyResponseService modifyResponseService,
                        @Qualifier("ModifySystemNameRequestService") ModifySourceRequestService modifySystemNameRequestService,
                        @Qualifier("ModifySourceRequestService") ModifySourceRequestService modifySourceRequestService) {
        this.validationService = validationService;
        this.modifyResponseService = modifyResponseService;
        this.modifySystemNameRequestService = modifySystemNameRequestService;
        this.modifySourceRequestService = modifySourceRequestService;
    }

    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request,
                                             BindingResult bindingResult) {

        long requestReceivedTime = System.currentTimeMillis();
        log.info("Получен request в Сервисе 1. Время получения: {}", requestReceivedTime);
        log.info("Request: {}", request);

        Response response = Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat().format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();

        log.info("Создан первоначальный response: {}", response);

        try {
            log.info("Начало валидации request");
            validationService.isValid(bindingResult);
            log.info("Валидация request успешно пройдена");

            if ("123".equals(request.getUid())) {
                log.warn("Обнаружен неподдерживаемый UID: {}", request.getUid());
                throw new UnsupportedCodeException("UID 123 не поддерживается");
            }

            // Модификация source
            log.info("Начало модификации поля source");
            modifySourceRequestService.modify(request);

            // Модификация systemName и отправка в Сервис 2
            log.info("Начало модификации systemName и отправки в Сервис 2");
            modifySystemNameRequestService.modify(request);

            log.info("Обработка request завершена успешно");

        } catch (UnsupportedCodeException e) {
            log.error("UnsupportedCodeException: {}", e.getMessage(), e);

            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNSUPPORTED_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNSUPPORTED);

            log.info("Response обновлен после UnsupportedCodeException: {}", response);

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } catch (ValidationFailedException e) {
            log.error("ValidationFailedException: {}", e.getMessage(), e);

            if (bindingResult.hasErrors()) {
                bindingResult.getFieldErrors().forEach(error ->
                        log.error("Ошибка валидации поля '{}': {}", error.getField(), error.getDefaultMessage())
                );
                bindingResult.getGlobalErrors().forEach(error ->
                        log.error("Глобальная ошибка валидации: {}", error.getDefaultMessage())
                );
            }

            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.VALIDATION_EXCEPTION);
            response.setErrorMessage(ErrorMessages.VALIDATION);

            log.info("Response обновлен после ValidationFailedException: {}", response);

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            log.error("Неизвестная ошибка: {}", e.getMessage(), e);

            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNKNOWN_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNKNOWN);

            log.info("Response обновлен после неизвестной ошибки: {}", response);

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (modifyResponseService != null) {
            log.info("Начало модификации response");
            modifyResponseService.modify(response);
            log.info("Response модифицирован: {}", response);
        }

        long processingTime = System.currentTimeMillis() - requestReceivedTime;
        log.info("Завершение обработки в Сервисе 1. Общее время обработки: {} мс. Final response: {}", processingTime, response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}