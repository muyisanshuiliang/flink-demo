package kml.test;


import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @Author: yangLang
 * @CreateDate: 2021/1/13 10:16
 */
@Data
public class KmlPoint {
    private  String color;
    private List<Coordinate> points;
    private String name;
}
