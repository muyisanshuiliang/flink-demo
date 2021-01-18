package serializable.test;

import org.apache.commons.compress.utils.Lists;

import java.io.*;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @Author: yangLang
 * @CreateDate: 2021/1/18 11:21
 */
public class Main {
    public static void main(String[] args) {
        serializableFun();
        deserializableFun();
        serializableOtherPersonFun();
        deserializableOtherPersonFun();

        serializableListFun();
    }

    private static void serializableOtherPersonFun() {
        OtherPerson person = new OtherPerson();
        person.setId(1);
        person.setName("yangLang");
        person.setDate(new Date());
        person.setAddress("四川省成都市");
        try (FileOutputStream fileOutputStream = new FileOutputStream("src\\main\\java\\serializable\\test\\otherPerson.txt");
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(person);
            /*  特别注意：
            如果序列化一个可变对象，序列化之后，修改对象属性值，再次序列化，只会保存上次序列化的编号*/
            person.setId(2);
            objectOutputStream.writeObject(person);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void deserializableOtherPersonFun() {
        try (FileInputStream fileInputStream = new FileInputStream("src\\main\\java\\serializable\\test\\otherPerson.txt");
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            OtherPerson person = (OtherPerson) objectInputStream.readObject();
            System.out.println(person);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void serializableFun() {
        Person person = new Person();
        person.setId(1);
        person.setName("yangLang");
        person.setDate(new Date());
        person.setAddress("四川省成都市");
        person.setBrother(new Brother(3, "yangLang's brother"));
        try (FileOutputStream fileOutputStream = new FileOutputStream("src\\main\\java\\serializable\\test\\Person.txt");
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(person);
            /*  特别注意：
            如果序列化一个可变对象，序列化之后，修改对象属性值，再次序列化，只会保存上次序列化的编号*/
            person.setId(2);
            objectOutputStream.writeObject(person);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Person deserializableFun() {
        try (FileInputStream fileInputStream = new FileInputStream("src\\main\\java\\serializable\\test\\Person.txt");
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            Person person = (Person) objectInputStream.readObject();
            System.out.println(person);
            return person;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void serializableListFun() {
        List<Brother> brothers = Lists.newArrayList();
        for (int i = 0; i < 100; i++) {
            brothers.add(new Brother(i, "yangLang's brother =====> " + i));
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream("src\\main\\java\\serializable\\test\\Person.txt");
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {

            /*第一种写法：将整个list数据直接写入*/
//            objectOutputStream.writeObject(brothers);

            /*第二种写法：逐条数据写入，写完之后在最后写入一个空对象*/
            /*第三种写法：逐条数据写入*/
            brothers.forEach(item -> {
                try {
                    objectOutputStream.writeObject(item);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
//            objectOutputStream.writeObject(null);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileInputStream fileInputStream = new FileInputStream("src\\main\\java\\serializable\\test\\Person.txt");
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

            /*第一种读法：将整个list数据直接读出*/
//            List<Object> brothers1 = (List<Object>) objectInputStream.readObject();
//            brothers1.forEach(System.out::println);

            /*第二种读法：判断所读的对象是否为空作为是否读取完毕的条件*/
            Brother brother;
//            while((brother=(Brother) objectInputStream.readObject())!=null) {
//                System.out.println(brother);
//            }

            /*第三种读法：判断是否到达文件末尾作为读取完毕的条件*/
            while (fileInputStream.available() > 0) {
                brother = (Brother) objectInputStream.readObject();
                System.out.println(brother);
            }


        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

}
