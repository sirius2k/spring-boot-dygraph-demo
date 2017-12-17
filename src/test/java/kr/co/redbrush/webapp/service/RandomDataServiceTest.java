package kr.co.redbrush.webapp.service;

import kr.co.redbrush.webapp.domain.RandomData;
import kr.co.redbrush.webapp.domain.RandomValue;
import kr.co.redbrush.webapp.repository.RandomDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo;
import static org.hamcrest.number.OrderingComparison.lessThanOrEqualTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
public class RandomDataServiceTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @InjectMocks
    public RandomDataService randomDataService = new RandomDataService();

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RandomDataRepository repository;

    private String serverUrl = "http://localhost";;
    private String createRandomUrl = "/random/create";
    private RandomValue randomValue = new RandomValue(1);

    @Before
    public void before() {
        ReflectionTestUtils.setField(randomDataService, "serverHost", serverUrl);
        ReflectionTestUtils.setField(randomDataService, "createRandomUrl", createRandomUrl);
        ReflectionTestUtils.invokeMethod(randomDataService, "init");
    }

    @Test
    public void testCollect() {
        when(restTemplate.getForObject(serverUrl + createRandomUrl, RandomValue.class)).thenReturn(this.randomValue);

        RandomData collectedData = randomDataService.collect();

        verify(repository).save(collectedData);

        assertThat("Unexpected randomData", collectedData, is(collectedData));
        assertThat("Unexpected created date", collectedData.getCreatedDate(), notNullValue());
    }

    @Test
    public void testGetListBetween() {
        Date startDate = new Date();
        Date endDate = new Date();
        Iterable<RandomData> randomDataHistory = randomDataService.getListBetween(startDate, endDate);

        verify(repository).findAllByCreatedDateBetween(startDate, endDate);
    }

    @Test
    public void testGetLastOne() {
        RandomData randomData = randomDataService.getLastOne();

        verify(repository).findFirst1ByOrderByCreatedDateDesc();
    }

    @Test
    public void testGenerateValue() {
        RandomValue randomValue = randomDataService.generateRandom(0, 10);

        assertThat("Random value should not be null.", randomValue, notNullValue());
        assertThat("Unexpected randomValue", randomValue.getValue(), greaterThanOrEqualTo(-10));
        assertThat("Unexpected randomValue", randomValue.getValue(), lessThanOrEqualTo(10));
    }
}