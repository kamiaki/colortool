package org.example;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ColorTool {
    public static void main(String[] args) throws FileNotFoundException {
        Color color1 = new Color(255, 0, 0, 0);
        Color color2 = new Color(208, 0, 255, 0);
        Color color3 = new Color(0, 34, 255, 64);
        Color color4 = new Color(0, 255, 217, 128);
        Color color5 = new Color(17, 255, 0, 191);
        Color color6 = new Color(255, 230, 0, 255);
        Color color7 = new Color(255, 0, 0, 255);
        Color[] colors = new Color[]{color1, color2, color3, color4, color5, color6, color7};
        Map colorStrArray = new ColorTool().createColorStrArray(colors, 0, 80, 10);
        System.out.println(colorStrArray);
        List<ColorInfo> colorInfos = (List<ColorInfo>) colorStrArray.get("colorInfos");
        File file = new File("d:\\color_options.xlsx");
        FileOutputStream outputStream = new FileOutputStream(file);
        EasyExcel.write(outputStream, ColorInfo.class).sheet("站点数据").doWrite(colorInfos);
    }

    /**
     * 获取渐变颜色 中间的颜色值
     *
     * @param colors   关键点颜色
     * @param minValue 最小值
     * @param maxValue 最大值
     * @param step     步长
     * @return
     */
    public Map createColorStrArray(Color[] colors, double minValue, double maxValue, double step) {
        final double maxMaxValue = 99999D;
        if (step <= 0) return null;
        Map map = new LinkedHashMap();
        int colorLen = colors.length;// 颜色总数
        if (colorLen < 2) return null;
        int colorInterval = colorLen - 1; //    颜色间隔
        double diff = maxValue - minValue; //   最大值最小值差值
        if (diff <= 0) return null;
        double stepLen = diff / step;   //  一共取多少颜色
        double colorIntervalStepLen = stepLen / colorInterval; //   两个颜色间隔多少颜色
        List<Color> colorList = new LinkedList<>();
        for (int i = 0; i < colorLen - 1; i++) {
            int redS = colors[i].getRed();
            int greenS = colors[i].getGreen();
            int blueS = colors[i].getBlue();
            int alphaS = colors[i].getAlpha();
            int redE = colors[i + 1].getRed();
            int greenE = colors[i + 1].getGreen();
            int blueE = colors[i + 1].getBlue();
            int alphaE = colors[i + 1].getAlpha();
            double redStep = (redE - redS) / colorIntervalStepLen;
            double greenStep = (greenE - greenS) / colorIntervalStepLen;
            double blueStep = (blueE - blueS) / colorIntervalStepLen;
            double alphaStep = (alphaE - alphaS) / colorIntervalStepLen;
            int redTmp;
            int greenTmp;
            int blueTmp;
            int alphaTmp;
            for (int j = 0; j < Math.floor(colorIntervalStepLen); j++) {
                redTmp = (int) Math.round(redS + redStep * j);
                greenTmp = (int) Math.round(greenS + greenStep * j);
                blueTmp = (int) Math.round(blueS + blueStep * j);
                alphaTmp = (int) Math.round(alphaS + alphaStep * j);
                Color color = new Color(redTmp, greenTmp, blueTmp, alphaTmp);
                colorList.add(color);
            }
        }
        colorList.add(colors[colorLen - 1]);// 因为循环最后取不到最后一个值，所以最后要写上最后一个值
        // 输出excel用
        List<ColorInfo> colorInfos = new LinkedList<>();
        for (int i = 0; i < colorList.size(); i++) {
            double v1 = minValue;
            double v2 = i >= (colorList.size() - 1) ? maxMaxValue : (minValue += step);
            ColorInfo colorInfo = new ColorInfo();
            colorInfo.setR(colorList.get(i).getRed());
            colorInfo.setG(colorList.get(i).getGreen());
            colorInfo.setB(colorList.get(i).getBlue());
            colorInfo.setA(colorList.get(i).getAlpha());
            colorInfo.setValueMin(v1);
            colorInfo.setValueMax(v2);
            colorInfo.setId(0);
            colorInfo.setType("AQI");
            colorInfos.add(colorInfo);
        }
        map.put("colorList", colorList);
        map.put("colorInfos", colorInfos);
        return map;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ColorInfo {
        @ExcelProperty(index = 0,value = "ID")
        private int id;
        @ExcelProperty(index = 1,value = "TYPE")
        private String type;
        @ExcelProperty(index = 2,value = "VALUE_MIN")
        private double valueMin;
        @ExcelProperty(index = 3,value = "VALUE_MAX")
        private double valueMax;
        @ExcelProperty(index = 4,value = "R")
        private int r;
        @ExcelProperty(index = 5,value = "G")
        private int g;
        @ExcelProperty(index = 6,value = "B")
        private int b;
        @ExcelProperty(index = 7,value = "A")
        private int a;
    }
}
