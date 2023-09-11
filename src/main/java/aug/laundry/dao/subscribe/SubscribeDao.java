package aug.laundry.dao.subscribe;

import aug.laundry.dto.SubscriptionPayDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class SubscribeDao {

    private final SubscribeMapper subscribeMapper;

    public int insertJoinSubscribe(SubscriptionPayDto subData){
        return subscribeMapper.insertJoinSubscribe(subData);
    }

    public int updateMemberSubscribe(int selectMonth, Long memberId){
        return subscribeMapper.updateMemberSubscribe(selectMonth, memberId);
    }

    public int updateNextMerchantId(String merchant_uid, String merchant_uid_r){
        return subscribeMapper.updateNextMerchantId(merchant_uid, merchant_uid_r);
    }
}
