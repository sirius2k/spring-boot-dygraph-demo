package kr.co.redbrush.webapp.controller;

import kr.co.redbrush.webapp.domain.RandomData;
import kr.co.redbrush.webapp.service.RandomDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class RandomDataHistoryController {
    @Value("${time.range.default:120}")
    private Integer defaultTimeRange;

    @Autowired
    private RandomDataService randomDataService;

    @RequestMapping("/")
    public String index(Integer timeRangeInMinutes, Map<String, Object> model) {
        if (timeRangeInMinutes == null || timeRangeInMinutes == 0) {
            timeRangeInMinutes = defaultTimeRange;
        }
        Date currentDate = new Date();
        Date startDate = DateUtils.addMinutes(currentDate, -timeRangeInMinutes);

        RandomData randomData = randomDataService.getLastOne();
        List<RandomData> randomDataHistory = randomDataService.getListBetween(startDate, currentDate);

        model.put("timeRangeInMinutes", timeRangeInMinutes);
        model.put("randomData", randomData);
        model.put("randomDataHistory", randomDataHistory);

        LOGGER.debug("randomData : {}", randomData);
        LOGGER.info("random data history size : {}", randomDataHistory!=null ? randomDataHistory.size() : 0);

        return "index";
    }
}