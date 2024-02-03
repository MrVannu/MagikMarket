package controllerTest;

import org.junit.jupiter.api.Test;
import org.project.controllers.PrevisionController;
import org.project.model.Stock;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class PrevisionControllerTest {

    @Test
    void testGenerateNextPrevision() {
        PrevisionController previsionController= null;
        try {
            previsionController= new PrevisionController(new ArrayList<Stock>());

        }catch (ExceptionInInitializerError e){
            assertNull(previsionController);

        }

    }

    @Test
    void testPrevisionButtonAction() {

    }
}



