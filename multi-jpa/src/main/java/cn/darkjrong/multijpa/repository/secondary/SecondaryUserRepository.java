package cn.darkjrong.multijpa.repository.secondary;

import cn.darkjrong.multijpa.entity.secondary.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SecondaryUserRepository extends JpaRepository<Users, Long>, JpaSpecificationExecutor<Users> {





}
