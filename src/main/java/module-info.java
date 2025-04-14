module com.pesquisaordenacao.animacaosort {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.pesquisaordenacao.animacaosort to javafx.fxml;
    exports com.pesquisaordenacao.animacaosort;
}