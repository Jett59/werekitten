open module com.mycodefu.werekitten {
    requires javafx.graphics;
    requires javafx.swing;
    requires javafx.controls;
	requires java.desktop;
	requires javax.inject;
	requires guice;
	requires io.netty.all;
    requires com.fasterxml.jackson.databind;

    exports com.mycodefu.werekitten to guice, java.base;
}

