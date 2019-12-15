package com.mycodefu.werekitten.backgroundObjects;

import com.mycodefu.werekitten.level.data.*;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.Test;

import static com.mycodefu.werekitten.level.data.ElementType.*;
import static org.junit.jupiter.api.Assertions.*;

class BackgroundObjectBuilderTest {

    @Test
    void build_rectangle_valid() {
        Element element = new Element();
        element.setName("Yellow Square");
        element.setType(Rectangle);
        element.setFillColor(new Color(1d, 1d, 0d, 1d)); //yellow
        element.setLocation(new Location(10, 10));
        element.setSize(new Size(100, 100));

        NodeObject nodeObject = BackgroundObjectBuilder.build(element);
        assertEquals(BackgroundShapeObject.class, nodeObject.getClass());
        BackgroundShapeObject backgroundShapeObject = (BackgroundShapeObject) nodeObject;
        assertEquals(javafx.scene.shape.Rectangle.class, backgroundShapeObject.getShape().getClass());
    }

    @Test()
    void build_rectangle_nosize() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    Element element = new Element();
                    element.setName("Yellow Square");
                    element.setType(Rectangle);
                    element.setFillColor(new Color(1d, 1d, 0d, 1d)); //yellow
                    element.setLocation(new Location(10, 10));

                    NodeObject nodeObject = BackgroundObjectBuilder.build(element);
                    assertEquals(BackgroundShapeObject.class, nodeObject.getClass());
                    BackgroundShapeObject backgroundShapeObject = (BackgroundShapeObject) nodeObject;
                    assertEquals(javafx.scene.shape.Rectangle.class, backgroundShapeObject.getShape().getClass());
                });

        assertEquals("No size specified on shape element named 'Yellow Square'.", exception.getMessage());
    }

}