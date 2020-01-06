open module com.mycodefu.werekitten {
    requires java.desktop;
    requires javafx.graphics;
    requires javafx.swing;
    requires javafx.controls;
    requires javafx.base;
    requires com.fasterxml.jackson.databind;
    requires io.netty.common;
    requires io.netty.handler;
    requires io.netty.codec.http;
    requires io.netty.codec;
    requires io.netty.transport;
    requires io.netty.buffer;
    requires jdk.unsupported;

    exports com.mycodefu.werekitten to java.base;
}

