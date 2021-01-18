package serializable.test;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description:
 * @Author: yangLang
 * @CreateDate: 2021/1/18 11:19
 */
@Data
public class Person implements Serializable {
    private static final long serialVersionUID = -7815525045275881255L;
    /**
     * 必须实现 Serializable 接口，否则会报以下错
     * java.io.NotSerializableException: serializable.test.Person
     */
    private Integer id;
    private String name;
    private Date date;

    /*一个对象要进行序列化，如果该对象成员变量是引用类型的，那这个引用类型也一定要是可序列化的，否则会报错*/
    private Brother brother;

    /*对于不想序列化的字段可以再字段类型之前加上transient关键字修饰（反序列化时会被赋予默认值）*/
//    private transient String address;
    private String address;
}
