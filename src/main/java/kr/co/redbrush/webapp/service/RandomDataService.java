package kr.co.redbrush.webapp.service;

import kr.co.redbrush.webapp.domain.RandomData;
import kr.co.redbrush.webapp.domain.RandomValue;
import kr.co.redbrush.webapp.repository.RandomDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class RandomDataService {
    @Value("${api.server.host}")
    private String serverHost;

    @Value("${api.create.random.url}")
    private String createRandomUrl;

    @Value("${enableCollect:true}")
    private boolean enableCollect;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RandomDataRepository repository;

    private String requestUrl;

    private boolean collecting;
    private boolean requestCompleted = true;

    @PostConstruct
    private void init() {
        requestUrl = serverHost + createRandomUrl;

        collecting = true;

        LOGGER.info("serverHost : {}, createRandomUrl : {}, requestUrl : {}, collecting", serverHost, createRandomUrl, requestUrl, collecting);
    }

    @Scheduled(fixedRate = 1000)
    public void collect() {
        if (collecting && requestCompleted) {
            RandomData lastData = repository.findFirst1ByOrderByCreatedDateDesc();
            RandomValue randomValue = restTemplate.getForObject(requestUrl, RandomValue.class);

            RandomData randomData = new RandomData();
            randomData.setValue(randomValue.getValue());
            randomData.setCreatedDate(new Date());

            if (lastData != null && lastData.getSum() != null) {
                randomData.setSum(lastData.getSum() + randomValue.getValue());
            } else {
                randomData.setSum(new Long(randomValue.getValue()));
            }

            LOGGER.info("New randomData to save : {}", randomData);

            repository.save(randomData);
        }
    }

    public List<RandomData> getListBetween(Date startDate, Date endDate) {
        return repository.findAllByCreatedDateBetween(startDate, endDate);
    }

    public RandomData getLastOne() {
        return repository.findFirst1ByOrderByCreatedDateDesc();
    }

    public RandomValue generateRandom(int startInclusive, int endInclusive) {
        int nextRandom = RandomUtils.nextInt(startInclusive, endInclusive) * (RandomUtils.nextBoolean() ? 1 : -1);

        RandomValue randomValue = new RandomValue();
        randomValue.setValue(nextRandom);

        return randomValue;
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public List<RandomData> getDelta(long lastId, Date fromDate) {
        return repository.findAllByIdGreaterThanAndCreatedDateGreaterThanOrderByIdAsc(lastId, fromDate);
    }

    public void enableCollect() {
        this.collecting = true;
    }

    public void disableCollect() {
        this.collecting = false;
    }

    public boolean isCollecting() {
        return collecting;
    }
}