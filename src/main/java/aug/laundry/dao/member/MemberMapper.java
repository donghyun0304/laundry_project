package aug.laundry.dao.member;

import aug.laundry.domain.Member;
import aug.laundry.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberMapper {
    public MemberDto selectOne(Long memberId);
    public int checkId(String memberAccount);
    public Integer registerUser(MemberDto memberDto);
    public int inviteCodeCheck(String inviteCode);







}
