module qcontrol {
    requires javafx.controls;
    requires transitive javafx.graphics;
    requires javafx.fxml;
    requires java.desktop;
    requires java.prefs;
    opens qcontrol to javafx.fxml;
    exports qcontrol;
}