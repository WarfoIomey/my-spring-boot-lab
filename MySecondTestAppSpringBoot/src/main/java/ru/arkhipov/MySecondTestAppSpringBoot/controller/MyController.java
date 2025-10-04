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

        long startTime = System.currentTimeMillis();
        logRequestReceived(request, startTime);

        Response response = createInitialResponse(request);

        try {
            processRequest(request, bindingResult);
        } catch (UnsupportedCodeException e) {
            return handleUnsupportedCodeException(response, e);
        } catch (ValidationFailedException e) {
            return handleValidationException(response, bindingResult, e);
        } catch (Exception e) {
            return handleGenericException(response, e);
        }

        modifyResponseIfNeeded(response);
        logProcessingCompletion(startTime, response);

        return ResponseEntity.ok(response);
    }

    private void logRequestReceived(Request request, long timestamp) {
        log.info("Получен request в Сервисе 1. Время получения: {}", timestamp);
        log.info("Request: {}", request);
    }

    private Response createInitialResponse(Request request) {
        Response response = Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat().format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();
        log.info("Создан первоначальный response: {}", response);
        return response;
    }

    private void processRequest(Request request, BindingResult bindingResult) throws UnsupportedCodeException, ValidationFailedException {
        validateRequest(bindingResult);
        checkForUnsupportedUid(request.getUid());
        modifyRequestFields(request);
    }

    private void validateRequest(BindingResult bindingResult) throws ValidationFailedException {
        log.info("Начало валидации request");
        validationService.isValid(bindingResult);
        log.info("Валидация request успешно пройдена");
    }

    private void checkForUnsupportedUid(String uid) throws UnsupportedCodeException {
        if ("123".equals(uid)) {
            log.warn("Обнаружен неподдерживаемый UID: {}", uid);
            throw new UnsupportedCodeException("UID 123 не поддерживается");
        }
    }

    private void modifyRequestFields(Request request) {
        log.info("Начало модификации поля source");
        modifySourceRequestService.modify(request);

        log.info("Начало модификации systemName и отправки в Сервис 2");
        modifySystemNameRequestService.modify(request);

        log.info("Обработка request завершена успешно");
    }

    private ResponseEntity<Response> handleUnsupportedCodeException(Response response, UnsupportedCodeException e) {
        log.error("UnsupportedCodeException: {}", e.getMessage(), e);
        updateErrorResponse(response, Codes.FAILED, ErrorCodes.UNSUPPORTED_EXCEPTION, ErrorMessages.UNSUPPORTED);
        log.info("Response обновлен после UnsupportedCodeException: {}", response);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Response> handleValidationException(Response response, BindingResult bindingResult,
                                                               ValidationFailedException e) {
        log.error("ValidationFailedException: {}", e.getMessage(), e);
        logValidationErrors(bindingResult);
        updateErrorResponse(response, Codes.FAILED, ErrorCodes.VALIDATION_EXCEPTION, ErrorMessages.VALIDATION);
        log.info("Response обновлен после ValidationFailedException: {}", response);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Response> handleGenericException(Response response, Exception e) {
        log.error("Неизвестная ошибка: {}", e.getMessage(), e);
        updateErrorResponse(response, Codes.FAILED, ErrorCodes.UNKNOWN_EXCEPTION, ErrorMessages.UNKNOWN);
        log.info("Response обновлен после неизвестной ошибки: {}", response);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void logValidationErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error ->
                    log.error("Ошибка валидации поля '{}': {}", error.getField(), error.getDefaultMessage())
            );
            bindingResult.getGlobalErrors().forEach(error ->
                    log.error("Глобальная ошибка валидации: {}", error.getDefaultMessage())
            );
        }
    }

    private void updateErrorResponse(Response response, Codes code, ErrorCodes errorCode, ErrorMessages errorMessage) {
        response.setCode(code);
        response.setErrorCode(errorCode);
        response.setErrorMessage(errorMessage);
    }

    private void modifyResponseIfNeeded(Response response) {
        if (modifyResponseService != null) {
            log.info("Начало модификации response");
            modifyResponseService.modify(response);
            log.info("Response модифицирован: {}", response);
        }
    }

    private void logProcessingCompletion(long startTime, Response response) {
        long processingTime = System.currentTimeMillis() - startTime;
        log.info("Завершение обработки в Сервисе 1. Общее время обработки: {} мс. Final response: {}",
                processingTime, response);
    }
}