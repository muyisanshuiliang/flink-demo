package serializable.test;

import lombok.Data;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;

/**
 * @description:
 * @Author: yangLang
 * @CreateDate: 2021/1/18 11:53
 */
@Data
public class OtherPerson implements Externalizable {

    private static final long serialVersionUID = -2681454076312701441L;

    private Integer id;

    private String name;

    private Date date;

    private String address;

    /*被transient修饰的变量，通过继承Externalizable，自定义可以被序列化和反序列化*/
    private transient String book = "《JAVA-从入门到放弃》(卷一)";
    /*静态变量无论怎么都不会被序列话*/
    private static String book1 = "《JAVA-从入门到放弃》(卷二)";

    @Override
    public void writeExternal(ObjectOutput out) {
        try {
            /*自定义需要系列化的数据*/
            out.writeInt(id);
            out.writeObject(date);
            out.writeObject(name);
            out.writeObject(book);
            out.writeObject(book1);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("序列化失败");
        }
    }

    @Override
    public void readExternal(ObjectInput in) {
        try {
            /*自定义需要反序列化的数据*/
            id = in.readInt();
            date = (Date) in.readObject();
            name = (String) in.readObject();
            book = (String) in.readObject();
            book1 = (String) in.readObject();
            // 如果序列化时一个字段没有序列化，那反序列化是要注意别给为序列化的字段反序列化了
            /*java.io.OptionalDataException*/
//            address = (String) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("反序列化失败");
        }
    }
}
