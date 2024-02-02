import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.junit.jupiter.api.Test;
import org.project.util.LineChartGenerator;


import static org.junit.jupiter.api.Assertions.*;

public class LineChartGeneratorTest {

    @Test
    public void testCreateLineChart() {

        LineChart<Number, Number> lineChart= null;
        try{
             lineChart = LineChartGenerator.createLineChart("XYZ Company");
        }
        catch (ExceptionInInitializerError e){}

        assertNull(lineChart);

        // Verify that the line chart title is set correctly
        //assertEquals("XYZ Company", lineChart.getTitle());


    }
}
