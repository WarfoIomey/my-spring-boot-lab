package ru.arkhipov.MySecondTestAppSpringBoot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.arkhipov.MySecondTestAppSpringBoot.model.Request;

@Slf4j
@Service
public class ModifySystemNameRequestService implements ModifyRequestService {

    @Override
    public void modify(Request request) {
        long startTime = System.currentTimeMillis();
        log.info("Начало модификации systemName в Сервисе 1. Время начала: {}", startTime);
        String originalSystemTime = request.getSystemTime();
        request.setSystemName(ru.arkhipov.MySecondTestAppSpringBoot.model.Systems.ERP);
        log.info("Поле systemName изменено на: {}", request.getSystemName());
        HttpEntity<Request> httpEntity = new HttpEntity<>(request);
        try {
            log.info("Отправка запроса в Сервис 2...");
            new RestTemplate().exchange("http://localhost:8084/feedback",
                    HttpMethod.POST,
                    httpEntity,
                    new ParameterizedTypeReference<Object>() {});

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            log.info("Запрос отправлен в Сервис 2. Время обработки в Сервисе 1: {} мс", duration);
        } catch (Exception e) {
            log.error("Ошибка при отправке запроса в Сервис 2: {}", e.getMessage());
        }
    }
}