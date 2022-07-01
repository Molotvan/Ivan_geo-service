import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;
import ru.netology.sender.MessageSender;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class MessageSenderTest {
    public final static String RUS_IP_HEADER = "172.132.32.3";
    public final static String USA_IP_HEADER = "96.143.32.3";
    public final static String OTHER_IP_HEADER = "123.143.32.3";

    @ParameterizedTest
    @ValueSource(strings = {"172.132.32.3/Добро пожаловать", "96.143.32.3/Welcome", "123.143.32.3/Welcome"})
    public void messageSender_Mock_Test(String arguments) {
        GeoService geoService = Mockito.mock(GeoService.class);
        when(geoService.byIp(RUS_IP_HEADER))
                .thenReturn(new Location(null, Country.RUSSIA, null, 0));
        when(geoService.byIp(USA_IP_HEADER))
                .thenReturn(new Location(null, Country.USA, null, 0));
        when(geoService.byIp(OTHER_IP_HEADER))
                .thenReturn(new Location(null, null, null, 0));

        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        when(localizationService.locale(geoService.byIp(RUS_IP_HEADER).getCountry()))
                .thenReturn("Добро пожаловать");
        when(localizationService.locale(geoService.byIp(USA_IP_HEADER).getCountry()))
                .thenReturn("Welcome");
        when(localizationService.locale(geoService.byIp(OTHER_IP_HEADER).getCountry()))
                .thenReturn("Welcome");

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);
        Map<String, String> headers = new HashMap<>();
        String[] args = arguments.split("/");
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, args[0]);

        String message = messageSender.send(headers);
        String expectad = args[1];

        Assertions.assertEquals(message, expectad);
    }

    @ParameterizedTest
    @ArgumentsSource(LocationArgProvider.class)
    public void location_by_Ip_test(String ip, Location location) {

        GeoService testGS = Mockito.spy(GeoServiceImpl.class);
        Location result = testGS.byIp(ip);
        Assertions.assertEquals(location, result);
    }


    @Test
    public void locale_test() {
        LocalizationService localizationService = Mockito.spy(LocalizationServiceImpl.class);
        Assertions.assertEquals("Добро пожаловать", localizationService.locale(Country.RUSSIA));
        Assertions.assertEquals("Welcome", localizationService.locale(Country.GERMANY));

    }


}




