package cn.darkjrong.multijpa.entity.primary;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Rong.Jia
 * @since 2022-04-01
 */
@Data
@Table(name = "users")
@Entity
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *  主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

}
