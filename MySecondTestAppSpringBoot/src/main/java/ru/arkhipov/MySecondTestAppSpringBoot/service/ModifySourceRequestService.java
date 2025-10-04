package ru.arkhipov.MySecondTestAppSpringBoot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.arkhipov.MySecondTestAppSpringBoot.model.Request;

@Slf4j
@Service
public class ModifySourceRequestService implements ModifyRequestService {

    @Override
    public void modify(Request request) {
        log.info("Изменение поля source с '{}' на '{}'", request.getSource(), "Service 1");
        request.setSource("Service 1");
        log.info("Поле source изменено: {}", request.getSource());
    }
}