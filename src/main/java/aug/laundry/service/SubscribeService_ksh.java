package aug.laundry.service;

import aug.laundry.dto.SubscriptionPayDto;
import org.springframework.stereotype.Service;

@Service
public interface SubscribeService_ksh {

    int saveSubscribe(SubscriptionPayDto subData);

    String getAccessToken();
}
