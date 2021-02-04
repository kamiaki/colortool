package org.example;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ColorTool {
    public static void main(String[] args) {
        Color color1 = new Color(205, 20, 20);
        Color color2 = new Color(160, 155, 57);
        Color color3 = new Color(23, 81, 208);
        Color[] colors = new Color[]{color1, color2, color3};
        Map colorStrArray = new ColorTool().createColorStrArray(colors, 20, 100, 0.5);
        System.out.println(colorStrArray);
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
            int redE = colors[i + 1].getRed();
            int greenE = colors[i + 1].getGreen();
            int blueE = colors[i + 1].getBlue();
            double redStep = (redE - redS) / colorIntervalStepLen;
            double greenStep = (greenE - greenS) / colorIntervalStepLen;
            double blueStep = (blueE - blueS) / colorIntervalStepLen;
            int redTmp;
            int greenTmp;
            int blueTmp;
            for (int j = 0; j < colorIntervalStepLen; j++) {
                redTmp = (int) Math.round(redS + redStep * j);
                greenTmp = (int) Math.round(greenS + greenStep * j);
                blueTmp = (int) Math.round(blueS + blueStep * j);
                Color color = new Color(redTmp, greenTmp, blueTmp);
                colorList.add(color);
            }
        }
        List<ColorInfo> colorInfos = new LinkedList<>();
        for (int i = 0; i < colorList.size(); i++) {
            double v1 = minValue;
            double v2 = i >= (colorList.size() - 1) ? maxMaxValue : (minValue += step);
            ColorInfo colorInfo = new ColorInfo();
            colorInfo.setR(colorList.get(i).getRed());
            colorInfo.setG(colorList.get(i).getGreen());
            colorInfo.setB(colorList.get(i).getBlue());
            colorInfo.setValueMin(v1);
            colorInfo.setValueMax(v2);
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
        private int R;
        private int G;
        private int B;
        private double valueMin;
        private double valueMax;
    }
}
