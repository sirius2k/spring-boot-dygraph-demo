package kr.co.redbrush.webapp.controller;

import kr.co.redbrush.webapp.domain.RandomData;
import kr.co.redbrush.webapp.domain.RandomValue;
import kr.co.redbrush.webapp.service.RandomDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@Slf4j
public class RandomGeneratorController {
    @Autowired
    private RandomDataService randomDataService;

    @Value("${generator.startInclusive:0}")
    private int startInclusive;

    @Value("${generator.endInclusive:100}")
    private int endInclusive;

    @GetMapping(value = "/random/generate")
    public RandomValue generateRandom() {
        return randomDataService.generateRandom(startInclusive, endInclusive);
    }

    @PostMapping(value = "/start")
    public Boolean start() {
        if (!randomDataService.isCollecting()) {
            randomDataService.enableCollect();
        }

        return randomDataService.isCollecting();
    }

    @PostMapping(value = "/stop")
    public Boolean stop() {
        if (randomDataService.isCollecting()) {
            randomDataService.disableCollect();
        }

        return randomDataService.isCollecting();
    }

    @GetMapping(value = "/status")
    public Boolean status() {
        return randomDataService.isCollecting();
    }

    @PostMapping(value = "/reset")
    public Boolean reset() {
        randomDataService.deleteAll();

        return true;
    }

    @GetMapping(value = "/delta")
    public List<RandomData> delta(long lastId, int timeRangeInMinutes) {
        Date fromDate = DateUtils.addMinutes(new Date(), -timeRangeInMinutes);
        LOGGER.debug("lastId : {}, timeRangeInMinutes : {}", lastId, timeRangeInMinutes );

        List<RandomData> randomDataList = randomDataService.getDelta(lastId, fromDate);

        LOGGER.debug("delta : {}", randomDataList);

        return randomDataList;
    }
}
