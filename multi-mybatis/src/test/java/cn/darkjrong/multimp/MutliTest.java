package cn.darkjrong.multimp;

import cn.darkjrong.multimp.mapper.primary.PrimaryUsersMapper;
import cn.darkjrong.multimp.mapper.secondary.SecondaryUsersMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MutliTest extends MultiMybatisApplicationTests{

    @Autowired
    private PrimaryUsersMapper primaryUsersMapper;

    @Autowired
    private SecondaryUsersMapper secondaryUsersMapper;

    @Test
    void save() {
        primaryUsersMapper.selectList(Wrappers.emptyWrapper());
        secondaryUsersMapper.selectList(Wrappers.emptyWrapper());


    }

}
