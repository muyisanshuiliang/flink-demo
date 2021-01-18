package serializable.test;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @Author: yangLang
 * @CreateDate: 2021/1/18 11:39
 */
@Data
@AllArgsConstructor
public class Brother implements Serializable {
    private static final long serialVersionUID = -3073098026735763451L;
    private Integer id;
    private String name;
}
