package cn.darkjrong.multimp.entity.secondary;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Rong.Jia
 * @since 2022-04-01
 */
@TableName("users")
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *  主键ID
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Users{" +
            "id=" + id +
            ", name=" + name +
            ", age=" + age +
        "}";
    }
}
