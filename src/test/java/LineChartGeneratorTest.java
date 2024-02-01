import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.junit.jupiter.api.Test;
import org.project.util.LineChartGenerator;


import static org.junit.jupiter.api.Assertions.*;

public class LineChartGeneratorTest {

    @Test
    public void testCreateLineChart() {
//        LineChart<Number, Number> lineChart = null;
//        try {
//            // Create a line chart for a hypothetical company named "XYZ Company"
//            lineChart = LineChartGenerator.createLineChart("XYZ Company");
//        }catch(ExceptionInInitializerError e){}
//        // Verify that the line chart is not null
//        assertNotNull(lineChart);
        LineChart<Number, Number> lineChart = LineChartGenerator.createLineChart("XYZ Company");

        // Verify that the X-Axis label is set correctly
        NumberAxis xAxis = (NumberAxis) lineChart.getXAxis();
        assertEquals("Market time", xAxis.getLabel());

        // Verify that the line chart title is set correctly
        assertEquals("XYZ Company", lineChart.getTitle());

        // Additional tests for the X and Y axes
        assertTrue(xAxis.isAutoRanging()); // Verify that auto-ranging is enabled for the X-Axis


        NumberAxis yAxis = (NumberAxis) lineChart.getYAxis();
        assertTrue(yAxis.isAutoRanging()); // Verify that auto-ranging is enabled for the Y-Axis


        // Additional tests for the line chart series (uncomment and customize based on your actual data structure)
         XYChart.Series<Number, Number> dataSeries = new XYChart.Series<>();
         dataSeries.getData().add(new XYChart.Data<>(1, 50));
         lineChart.getData().add(dataSeries);
         assertTrue(lineChart.getData().contains(dataSeries)); // Verify that the data series is added to the chart
    }
}
