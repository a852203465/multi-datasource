package cn.darkjrong.multijpa;

import cn.darkjrong.multijpa.repository.primary.PrimaryUserRepository;
import cn.darkjrong.multijpa.repository.secondary.SecondaryUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MultiTest extends MultiJpaApplicationTests {

    @Autowired
    private PrimaryUserRepository primaryUserRepository;

    @Autowired
    private SecondaryUserRepository secondaryUserRepository;

    @Test
    void save(){

        primaryUserRepository.findAll();
        secondaryUserRepository.findAll();



    }



}
