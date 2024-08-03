module com.example.walletappvis {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires org.jsoup;

    opens com.example.walletappvis to javafx.fxml;
    exports com.example.walletappvis;
}