package kr.co.redbrush.webapp.controller;

import kr.co.redbrush.webapp.domain.RandomData;
import kr.co.redbrush.webapp.service.RandomDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class RandomDataHistoryController {
    public static final Integer DEFAULT_TIME_BEFORE_IN_MINUTES = 120;

    @Autowired
    private RandomDataService randomDataService;

    @RequestMapping("/")
    public String index(Integer timeBeforeInMinutes, Map<String, Object> model) {
        if (timeBeforeInMinutes == null || timeBeforeInMinutes == 0) {
            timeBeforeInMinutes = DEFAULT_TIME_BEFORE_IN_MINUTES;
        }
        Date currentDate = new Date();
        Date startDate = DateUtils.addMinutes(currentDate, -timeBeforeInMinutes);

        RandomData randomData = randomDataService.getLastOne();
        List<RandomData> randomDataHistory = randomDataService.getListBetween(startDate, currentDate);

        model.put("timeBeforeInMinutes", timeBeforeInMinutes);
        model.put("randomData", randomData);
        model.put("randomDataHistory", randomDataHistory);

        LOGGER.debug("randomData : {}", randomData);
        LOGGER.info("random data history size : {}", randomDataHistory!=null ? randomDataHistory.size() : 0);

        return "index";
    }
}