package cn.darkjrong.multijpa.repository.primary;

import cn.darkjrong.multijpa.entity.primary.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PrimaryUserRepository extends JpaRepository<Users, Long>, JpaSpecificationExecutor<Users> {
}
