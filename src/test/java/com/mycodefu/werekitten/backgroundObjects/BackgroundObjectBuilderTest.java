package com.mycodefu.werekitten.backgroundObjects;

import com.mycodefu.werekitten.animation.AnimationCompiler;
import com.mycodefu.werekitten.level.data.Color;
import com.mycodefu.werekitten.level.data.Element;
import com.mycodefu.werekitten.level.data.Location;
import com.mycodefu.werekitten.level.data.Size;
import javafx.scene.shape.Shape;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static com.mycodefu.werekitten.level.data.ElementType.Rectangle;
import static javafx.scene.paint.Color.TRANSPARENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BackgroundObjectBuilderTest {

    @Test
    void build_rectangle_valid() {
    	AnimationCompiler mockedAnimationCompiler = Mockito.mock(AnimationCompiler.class);
    	BackgroundObjectBuilder backgroundObjectBuilder = new BackgroundObjectBuilder(mockedAnimationCompiler);

        Element element = new Element();
        element.setName("Yellow Square");
        element.setType(Rectangle);
        element.setFillColor(new Color(1d, 1d, 0d, 1d)); //yellow
        element.setLocation(new Location(10, 10));
        element.setSize(new Size(100, 100));

        NodeObject nodeObject = backgroundObjectBuilder.build(element);
        assertEquals(BackgroundShapeObject.class, nodeObject.getClass());
        BackgroundShapeObject backgroundShapeObject = (BackgroundShapeObject) nodeObject;
        assertEquals(javafx.scene.shape.Rectangle.class, backgroundShapeObject.getShape().getClass());
    }

    @Test
    void createBackgroundShapeFromElement_valid() {
    	AnimationCompiler mockedAnimationCompiler = Mockito.mock(AnimationCompiler.class);
    	BackgroundObjectBuilder backgroundObjectBuilder = new BackgroundObjectBuilder(mockedAnimationCompiler);

        Element element = new Element();
        element.setName("Yellow Square with Red Border");
        element.setType(Rectangle);
        element.setFillColor(new Color(1d, 1d, 0d, 1d)); //yellow
        element.setStrokeColor(new Color(1d, 0d, 0d, 0.5d)); //red half opacity
        element.setStrokeWidth(5d); //5 pixels
        element.setLocation(new Location(10, 20));
        element.setSize(new Size(100, 300));

        BackgroundShapeObject backgroundShapeObject = backgroundObjectBuilder.createBackgroundShapeFromElement(element);
        Shape shape = backgroundShapeObject.getShape();
        assertEquals(javafx.scene.shape.Rectangle.class, shape.getClass());

        javafx.scene.shape.Rectangle rectangle = (javafx.scene.shape.Rectangle)shape;
        assertEquals(rectangle, backgroundShapeObject.getNode());

        assertEquals("Yellow Square with Red Border", backgroundShapeObject.getName());
        assertEquals(javafx.scene.paint.Color.YELLOW, rectangle.getFill());
        assertEquals(new javafx.scene.paint.Color(1d, 0d, 0d, 0.5d), rectangle.getStroke());
        assertEquals(5d, rectangle.getStrokeWidth());

        assertEquals(10, rectangle.getX());
        assertEquals(20, rectangle.getY());

        assertEquals(100, rectangle.getWidth());
        assertEquals(300, rectangle.getHeight());
    }

    @Test
    void createBackgroundShapeFromElement_valid_noborder() {
    	AnimationCompiler mockedAnimationCompiler = Mockito.mock(AnimationCompiler.class);
    	BackgroundObjectBuilder backgroundObjectBuilder = new BackgroundObjectBuilder(mockedAnimationCompiler);

        Element element = new Element();
        element.setName("Yellow Square with No Border");
        element.setType(Rectangle);
        element.setFillColor(new Color(1d, 1d, 0d, 1d)); //yellow
        element.setLocation(new Location(10, 20));
        element.setSize(new Size(100, 300));

        BackgroundShapeObject backgroundShapeObject = backgroundObjectBuilder.createBackgroundShapeFromElement(element);
        Shape shape = backgroundShapeObject.getShape();
        assertEquals(javafx.scene.shape.Rectangle.class, shape.getClass());

        javafx.scene.shape.Rectangle rectangle = (javafx.scene.shape.Rectangle)shape;
        assertEquals(rectangle, backgroundShapeObject.getNode());

        assertEquals("Yellow Square with No Border", backgroundShapeObject.getName());
        assertEquals(javafx.scene.paint.Color.YELLOW, rectangle.getFill());
        assertEquals(javafx.scene.paint.Color.BLACK, rectangle.getStroke());
        assertEquals(0d, rectangle.getStrokeWidth());

        assertEquals(10, rectangle.getX());
        assertEquals(20, rectangle.getY());

        assertEquals(100, rectangle.getWidth());
        assertEquals(300, rectangle.getHeight());
    }

    @Test
    void createBackgroundShapeFromElement_valid_border_nofill() {
    	AnimationCompiler mockedAnimationCompiler = Mockito.mock(AnimationCompiler.class);
    	BackgroundObjectBuilder backgroundObjectBuilder = new BackgroundObjectBuilder(mockedAnimationCompiler);

        Element element = new Element();
        element.setName("Blue Square with Border and No Fill");
        element.setType(Rectangle);
        element.setStrokeColor(new Color(0d, 0d, 1d, 1d)); //blue half opacity
        element.setStrokeWidth(1d); //1 pixel width stroke
        element.setLocation(new Location(10, 20));
        element.setSize(new Size(100, 300));

        BackgroundShapeObject backgroundShapeObject = backgroundObjectBuilder.createBackgroundShapeFromElement(element);
        Shape shape = backgroundShapeObject.getShape();
        assertEquals(javafx.scene.shape.Rectangle.class, shape.getClass());

        javafx.scene.shape.Rectangle rectangle = (javafx.scene.shape.Rectangle)shape;
        assertEquals(rectangle, backgroundShapeObject.getNode());

        assertEquals("Blue Square with Border and No Fill", backgroundShapeObject.getName());
        assertEquals(TRANSPARENT, rectangle.getFill());
        assertEquals(javafx.scene.paint.Color.BLUE, rectangle.getStroke());
        assertEquals(1d, rectangle.getStrokeWidth());

        assertEquals(10, rectangle.getX());
        assertEquals(20, rectangle.getY());

        assertEquals(100, rectangle.getWidth());
        assertEquals(300, rectangle.getHeight());
    }

    @Test
    void build_rectangle_nosize() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> {
                	AnimationCompiler mockedAnimationCompiler = Mockito.mock(AnimationCompiler.class);
                	BackgroundObjectBuilder backgroundObjectBuilder = new BackgroundObjectBuilder(mockedAnimationCompiler);

                    Element element = new Element();
                    element.setName("Yellow Square");
                    element.setType(Rectangle);
                    element.setFillColor(new Color(1d, 1d, 0d, 1d)); //yellow
                    element.setLocation(new Location(10, 10));

                    backgroundObjectBuilder.build(element);
                });

        assertEquals("No size specified on shape element named 'Yellow Square'.", exception.getMessage());
    }


    @Test()
    void build_rectangle_nolocation() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> {
                	AnimationCompiler mockedAnimationCompiler = Mockito.mock(AnimationCompiler.class);
                	BackgroundObjectBuilder backgroundObjectBuilder = new BackgroundObjectBuilder(mockedAnimationCompiler);

                    Element element = new Element();
                    element.setName("Yellow Square");
                    element.setType(Rectangle);
                    element.setFillColor(new Color(1d, 1d, 0d, 1d)); //yellow
                    element.setSize(new Size(100, 100));

                    backgroundObjectBuilder.build(element);
                });

        assertEquals("No location specified on shape element named 'Yellow Square'.", exception.getMessage());
    }

}