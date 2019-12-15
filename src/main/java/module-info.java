open module com.mycodefu.werekitten {
    requires javafx.graphics;
    requires javafx.swing;
    requires com.fasterxml.jackson.databind;
	requires java.desktop;
	requires javax.inject;
	requires guice;
	

    exports com.mycodefu.werekitten to guice, java.base, java.lang;
}

