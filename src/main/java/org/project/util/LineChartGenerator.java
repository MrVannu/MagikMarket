package org.project.util;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;

public  class LineChartGenerator {
    public static LineChart<Number, Number> createLineChart(String nameOfCompany) {
        // Create X and Y axes for the line chart
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();

        //Customize the X-Axis
        xAxis.setLabel("Market time");

        // Create the line chart with axes
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(nameOfCompany);

        //lineChart.getData().addAll(checkBoxSeriesMap.values());

        return lineChart;
    }
}
