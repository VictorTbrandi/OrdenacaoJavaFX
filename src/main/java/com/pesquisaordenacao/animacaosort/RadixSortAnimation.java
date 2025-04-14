package com.pesquisaordenacao.animacaosort;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class RadixSortAnimation extends Application {

    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 800;
    private static final int VARS_MAX_COLS = 8;
    private static final int DELAY = 800;

    Font minecraftFont = Font.loadFont(
            getClass().getResourceAsStream("/com/pesquisaordenacao/animacaosort/fonts/Minecraft.ttf"),
            18);
    Font codeFont = Font.loadFont(
            getClass().getResourceAsStream("/com/pesquisaordenacao/animacaosort/fonts/DroidSansMono.ttf"),
            18);

    private BorderPane root;

    private VBox topBottomContainer;
    private HBox containerTop;
    private VBox containerVetores;
    private HBox bottomBottomContainer;
    private GridPane gridVariaveis;
    private VBox bottomContainer;
    private Button initRadixInterno;
    private Button voltarTelaInicial;
    private Button refazerRadixBtn;
    private VBox vectorLabelsContainer;
    private HBox buttonsContainer;
    private VBox containerCodigo;
    private VBox containerCodigoLinhas;

    private HashMap<String, VBox> variaveisMap = new HashMap<>();
    private HashMap<String, HBox> vectorContainerMap = new HashMap<>();
    private HashMap<String, HBox> vectorMap = new HashMap<>();
    private HashMap<String, Label> labelsMap = new HashMap<>();
    private HashMap<Integer, StackPane> linhasCodigoMap = new HashMap<>();
    private int[] vet;
    private int tl;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Radix e Comb Sort");
        configurarLayoutInicial();
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void cleanRoot() {
        if (root != null) {
            root.setTop(null);
            root.setBottom(null);
            root.setLeft(null);
            root.setRight(null);
            root.setCenter(null);
        }
    }

    private int max(int mx) {
        try {
            Platform.runLater(() -> {
                adicionarLinhaCodigo("    int m = vet[0];", 2);
                adicionarLinhaCodigo("    for (int i = 1; i < vet.length; i++) {", 3);
                adicionarLinhaCodigo("        if (vet[i] > m)", 4);
                adicionarLinhaCodigo("            m = vet[i];", 5);
                adicionarLinhaCodigo("    }", 6);
                adicionarLinhaCodigo("    return m;", 7);
            });
            final int[][] m = new int[1][1];
            m[0][0] = vet[0];
            Platform.runLater(() -> {
                destacarLinha(2);
                animacaoAuxUsoDaPosicaoDoVetor("vet", 0);
                criaAtualizaVariavel("m", m[0][0]);
            });
            Thread.sleep(DELAY);
            Platform.runLater(() -> {
                retirarDestaqueDeLinha(2);
                destacarLinha(3);
                criaAtualizaVariavel("i", 1);
            });
            Thread.sleep(DELAY);
            for (int i = 1; i < vet.length; i++) {
                final int index = i;
                Platform.runLater(() -> {
                    retirarDestaqueDeLinha(3);
                    destacarLinha(4);
                    animacaoAuxUsoDaPosicaoDoVetor("vet", index);
                });
                Thread.sleep(DELAY);
                if (vet[i] > m[0][0]) {
                    m[0][0] = vet[i];
                    Platform.runLater(() -> {
                        destacarLinha(5);
                        retirarDestaqueDeLinha(4);
                        criaAtualizaVariavel("m", m[0][0]);
                    });
                    Thread.sleep(DELAY);
                }
                Platform.runLater(() -> {
                    criaAtualizaVariavel("i", index + 1);
                    destacarLinha(3);
                    retirarDestaqueDeLinha(4);
                    retirarDestaqueDeLinha(5);
                });
                Thread.sleep(DELAY);
            }
            // Platform.runLater(() -> {
            // retirarDestaqueDeLinha(5);
            // retirarDestaqueDeLinha(4);
            // destacarLinha(3);
            // });
            // Thread.sleep(DELAY);
            Platform.runLater(() -> {
                retirarDestaqueDeLinha(3);
                destacarLinha(7);
            });
            Thread.sleep(DELAY);
            Platform.runLater(() -> {
                removerLinhaCodigo(7);
                removerLinhaCodigo(6);
                removerLinhaCodigo(5);
                removerLinhaCodigo(4);
                removerLinhaCodigo(3);
                removerLinhaCodigo(2);
                deletarVariavel("m");
                deletarVariavel("i");
            });
            Thread.sleep(1);
            return m[0][0];
        } catch (Exception e) {
            return -1;
        }
    }

    private int maxAux() {
        int m = vet[0];
        for (int i = 0; i < vet.length; i++) {
            if (vet[i] > m)
                m = vet[i];
        }
        return m;
    }

    private void configRandomValores() {
        tl = 0;
        Random rand = new Random();
        HashSet<Integer> valores = new HashSet<>();
        while (valores.size() < 10) {
            int num = rand.nextInt(10) + 1;
            if (valores.add(num))
                vet[tl++] = num;
        }
    }

    private void radixSort() {
        configRandomValores();
        for (int i = 0; i < 10; i++) {
            atualizaVetorPorPosRadix("vet", i, vet[i]);
        }
        bottomContainer.getChildren().remove(initRadixInterno);
        initRadixInterno = null;

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(DELAY);
                Platform.runLater(() -> {
                    adicionarLinhaCodigo("int max = max();", 0);
                    adicionarLinhaCodigo("-----------------", 1);
                    destacarLinha(0);
                });
                Thread.sleep(DELAY);
                int maxAux = maxAux();
                int max = max(maxAux);
                Platform.runLater(() -> {
                    criaAtualizaVariavel("max", max);
                    retirarDestaqueDeLinha(0);
                    removerLinhaCodigo(1);
                });
                Thread.sleep(DELAY);
                final int[][] vetOrdenadoHolder = new int[1][];
                vetOrdenadoHolder[0] = new int[tl];

                Platform.runLater(() -> {
                    adicionarLinhaCodigo("int[] vet_ordenado = new int[tl];", 8);
                    adicionarLinhaCodigo("for (int dgt = 1; dgt <= max; dgt *= 10) {", 9);
                });
                Thread.sleep(DELAY);

                Platform.runLater(() -> {
                    destacarLinha(8);
                    criarVetorNaTela("vet_ordenado", vetOrdenadoHolder[0]);
                });
                Thread.sleep(DELAY);
                Platform.runLater(() -> {
                    retirarDestaqueDeLinha(8);
                });
                Thread.sleep(DELAY);
                Platform.runLater(() -> {
                    removerLinhaCodigo(0);
                    removerLinhaCodigo(8);
                    adicionarLinhaCodigo("    int[] counting = new int[10];", 10);
                    adicionarLinhaCodigo("    for (int i = 0; i < tl; i++)", 11);
                    adicionarLinhaCodigo("        counting[(vet[i] / dgt) % 10]++;", 12);
                    adicionarLinhaCodigo("    for (int i = 1; i < 10; i++)", 13);
                    adicionarLinhaCodigo("        counting[i] += counting[i - 1];", 14);
                    adicionarLinhaCodigo("    for (int i = tl - 1; i >= 0; i--) {", 15);
                    adicionarLinhaCodigo("        int pos = (vet[i] / dgt) % 10;", 16);
                    adicionarLinhaCodigo("        vet_ordenado[counting[pos] - 1] = vet[i];", 17);
                    adicionarLinhaCodigo("        counting[pos]--;", 18);
                    adicionarLinhaCodigo("    int[] aux = vet;", 19);
                    adicionarLinhaCodigo("    vet = vet_ordenado;", 20);
                    adicionarLinhaCodigo("    vet_ordenado = aux;", 21);
                    adicionarLinhaCodigo("}", 22);
                    criaAtualizaVariavel("dgt", 1);
                    destacarLinha(9);
                });
                Thread.sleep(DELAY);
                for (int digit = 1; digit <= max; digit *= 10) {
                    final int currentDigit = digit;
                    int[] countingArray = new int[10];
                    Thread.sleep(DELAY);
                    Platform.runLater(() -> {
                        retirarDestaqueDeLinha(9);
                        destacarLinha(10);
                        removerVetorDaTela("counting");
                        criarVetorNaTela("counting", countingArray);
                    });
                    Thread.sleep(DELAY);
                    Platform.runLater(() -> {
                        retirarDestaqueDeLinha(10);
                        criaAtualizaVariavel("i", 0);
                        destacarLinha(11);
                    });
                    Thread.sleep(DELAY);
                    for (int i = 0; i < tl; i++) {
                        final int index = i;
                        int pos = (vet[index] / currentDigit) % 10;
                        final int posFinal = pos;
                        int newValue = countingArray[pos] + 1;
                        countingArray[pos]++;
                        Platform.runLater(() -> {
                            retirarDestaqueDeLinha(11);
                            destacarLinha(12);
                            atualizaVetorPorPosRadix("counting", posFinal, newValue);
                            animacaoAuxUsoDaPosicaoDoVetor("vet", index);
                        });
                        Thread.sleep(DELAY);
                        Platform.runLater(() -> {
                            retirarDestaqueDeLinha(12);
                            destacarLinha(11);
                            criaAtualizaVariavel("i", index + 1);
                        });
                        Thread.sleep(DELAY);
                    }
                    Platform.runLater(() -> {
                        retirarDestaqueDeLinha(12);
                        retirarDestaqueDeLinha(11);
                        criaAtualizaVariavel("i", 1);
                        destacarLinha(13);
                    });
                    Thread.sleep(DELAY);
                    for (int i = 1; i < 10; i++) {
                        final int index = i;
                        Platform.runLater(() -> {
                            atualizaVetorPorPosRadix("counting", index,
                                    countingArray[index] + countingArray[index - 1]);
                            retirarDestaqueDeLinha(13);
                            destacarLinha(14);
                        });
                        Thread.sleep(DELAY);
                        countingArray[index] += countingArray[index - 1];
                        Platform.runLater(() -> {
                            retirarDestaqueDeLinha(14);
                            destacarLinha(13);
                            criaAtualizaVariavel("i", index + 1);
                        });
                        Thread.sleep(DELAY);
                    }

                    Platform.runLater(() -> {
                        destacarLinha(15);
                        retirarDestaqueDeLinha(14);
                        retirarDestaqueDeLinha(13);
                        criaAtualizaVariavel("i", tl - 1);
                    });
                    Thread.sleep(DELAY);
                    for (int i = tl - 1; i >= 0; i--) {
                        final int index = i;
                        int pos = (vet[index] / currentDigit) % 10;
                        int pos2 = countingArray[pos] - 1;
                        Platform.runLater(() -> {
                            criaAtualizaVariavel("pos", pos);
                            animacaoAuxUsoDaPosicaoDoVetor("vet", index);
                            destacarLinha(16);
                            retirarDestaqueDeLinha(15);
                        });
                        Thread.sleep(DELAY);
                        Platform.runLater(() -> {
                            retirarDestaqueDeLinha(16);
                            destacarLinha(17);
                            animacaoAuxUsoDaPosicaoDoVetor("counting", pos);
                            atualizaVetorPorPosRadix("vet_ordenado", pos2,
                                    vet[index]);
                        });
                        Thread.sleep(DELAY + 50);
                        vetOrdenadoHolder[0][pos2] = vet[index];
                        Platform.runLater(() -> {
                            atualizaVetorPorPosRadix("counting", pos,
                                    pos2);
                            destacarLinha(18);
                            retirarDestaqueDeLinha(17);
                        });
                        Thread.sleep(DELAY);
                        countingArray[pos]--;
                        Platform.runLater(() -> {
                            criaAtualizaVariavel("i", index - 1);
                            destacarLinha(15);
                            retirarDestaqueDeLinha(18);
                        });
                        Thread.sleep(DELAY);
                    }
                    Platform.runLater(() -> {
                        // removerLinhaCodigo(18);
                        // removerLinhaCodigo(17);
                        // removerLinhaCodigo(16);
                        // removerLinhaCodigo(15);
                        retirarDestaqueDeLinha(15);
                        deletarVariavel("i");
                        deletarVariavel("pos");
                        destacarLinha(19);
                        destacarLinha(20);
                        destacarLinha(21);
                    });
                    Thread.sleep(DELAY);
                    int[] aux = vet;
                    vet = vetOrdenadoHolder[0];
                    vetOrdenadoHolder[0] = aux;
                    Platform.runLater(() -> {
                        HBox vetContainer = vectorMap.get("vet");
                        HBox vetOrdenadoContainer = vectorMap.get("vet_ordenado");
                        if (vetContainer != null && vetOrdenadoContainer != null) {
                            double distanciaY = vetOrdenadoContainer.getBoundsInParent().getMinY() -
                                    vetContainer.getBoundsInParent().getMinY();
                            TranslateTransition descerVet = new TranslateTransition(Duration.millis(800), vetContainer);
                            descerVet.setByY(distanciaY);
                            TranslateTransition subirVetOrdenado = new TranslateTransition(Duration.millis(800),
                                    vetOrdenadoContainer);
                            subirVetOrdenado.setByY(-distanciaY);
                            SequentialTransition seqTransition = new SequentialTransition(
                                    new ParallelTransition(descerVet, subirVetOrdenado));
                            seqTransition.setOnFinished(e -> {
                                HBox containerVet = vectorMap.get("vet");
                                HBox containerVetOr = vectorMap.get("vet_ordenado");
                                vectorMap.remove("vet");
                                vectorMap.remove("vet_ordenado");
                                vectorMap.put("vet", containerVetOr);
                                vectorMap.put("vet_ordenado", containerVet);
                            });
                            seqTransition.play();
                        }
                        retirarDestaqueDeLinha(21);
                        retirarDestaqueDeLinha(20);
                        retirarDestaqueDeLinha(19);
                        criaAtualizaVariavel("dgt", currentDigit * 10);
                        destacarLinha(9);
                    });
                    Thread.sleep(DELAY + 800);
                }
                Platform.runLater(() -> {
                    configurarBotoesFinal(true);
                    retirarDestaqueDeLinha(9);
                    // retirarDestaqueDeLinha(20);
                    // retirarDestaqueDeLinha(19);
                    // removerLinhaCodigo(9);
                    deletarVariavel("max");
                    deletarVariavel("dgt");
                    removerVetorDaTela("counting");
                    removerVetorDaTela("vet_ordenado");
                });
                Thread.sleep(1);
                return null;
            }
        };
        new Thread(task).start();
    }

    private void configurarBotoesFinal(boolean radix) {
        buttonsContainer = new HBox(20);
        voltarTelaInicial = new Button("Voltar");
        voltarTelaInicial.setFont(new Font("Minecraft", 16));
        voltarTelaInicial.setStyle("-fx-background-color: #444444; -fx-border: 1px solid white; -fx-text-fill: white;");
        voltarTelaInicial.setOnAction(e -> voltarParaTelaInicial());
        buttonsContainer.getChildren().add(voltarTelaInicial);
        refazerRadixBtn = new Button("Refazer");
        refazerRadixBtn.setFont(new Font("Minecraft", 16));
        refazerRadixBtn.setStyle("-fx-background-color: #444444; -fx-border: 1px solid white; -fx-text-fill: white;");
        if (radix) {
            refazerRadixBtn.setOnAction(e -> refazerRadix());
        } else {
            refazerRadixBtn.setOnAction(e -> refazerComb());
        }
        buttonsContainer.getChildren().add(refazerRadixBtn);
        buttonsContainer.setAlignment(Pos.CENTER);
        bottomContainer.getChildren().add(buttonsContainer);
    }

    private void refazerComb() {
        bottomContainer.getChildren().remove(buttonsContainer);
        for (int i = 0; i < 16; i++) {
            removerLinhaCodigo(i);
        }
        combSort();
    }

    private void voltarParaTelaInicial() {
        vectorMap.clear();
        vectorContainerMap.clear();
        variaveisMap.clear();
        labelsMap.clear();
        vet = null;
        topBottomContainer = null;
        containerTop = null;
        containerVetores = null;
        bottomBottomContainer = null;
        gridVariaveis = null;
        bottomContainer = null;
        initRadixInterno = null;
        voltarTelaInicial = null;
        refazerRadixBtn = null;
        vectorLabelsContainer = null;
        buttonsContainer = null;
        configurarLayoutInicial();
    }

    private void combSort() {
        configRandomValores();
        for (int i = 0; i < 10; i++) {
            atualizaVetorPorPosRadix("vet", i, vet[i]);
        }
        bottomContainer.getChildren().remove(initRadixInterno);
        initRadixInterno = null;
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> {
                    adicionarLinhaCodigo("int intervalo = (int) (tl / 1.3);", 0);
                    adicionarLinhaCodigo("boolean flag = true;", 1);
                    adicionarLinhaCodigo("while (intervalo > 1 || flag) {", 2);
                    adicionarLinhaCodigo("    flag = false;", 3);
                    adicionarLinhaCodigo("    for (int i = intervalo; i < tl; i++)", 4);
                    adicionarLinhaCodigo("        if (vet[i] < vet[i - intervalo]) {", 5);
                    adicionarLinhaCodigo("            int aux = vet[i];", 6);
                    adicionarLinhaCodigo("            vet[i] = vet[i - intervalo];", 7);
                    adicionarLinhaCodigo("            vet[i - intervalo] = aux;", 8);
                    adicionarLinhaCodigo("            flag = true;", 9);
                    adicionarLinhaCodigo("        }", 10);
                    adicionarLinhaCodigo("    if (intervalo > 1){", 11);
                    adicionarLinhaCodigo("        intervalo = (int) (intervalo / 1.3);", 12);
                    adicionarLinhaCodigo("        flag = true;", 13);
                    adicionarLinhaCodigo("    }", 14);
                    adicionarLinhaCodigo("}", 15);
                    criaAtualizaVariavel("tl", 10);
                });
                Thread.sleep(DELAY);
                final int[][] inter = new int[1][1];
                inter[0][0] = (int) (tl / 1.3);
                Platform.runLater(() -> {
                    criaAtualizaVariavel("intervalo", inter[0][0]);
                    destacarLinha(0);
                });
                Thread.sleep(DELAY);
                boolean flag = true;
                Platform.runLater(() -> {
                    criaAtualizaVariavel("flag", "true");
                    retirarDestaqueDeLinha(0);
                    destacarLinha(1);
                });
                Thread.sleep(DELAY);
                Platform.runLater(() -> {
                    retirarDestaqueDeLinha(1);
                    destacarLinha(2);
                });
                Thread.sleep(DELAY);
                while (inter[0][0] > 1 || flag) {
                    flag = false;
                    Platform.runLater(() -> {
                        retirarDestaqueDeLinha(2);
                        destacarLinha(3);
                        criaAtualizaVariavel("flag", "false");
                    });
                    Thread.sleep(DELAY);
                    Platform.runLater(() -> {
                        retirarDestaqueDeLinha(3);
                        destacarLinha(4);
                        criaAtualizaVariavel("i", inter[0][0]);
                    });
                    Thread.sleep(DELAY);
                    for (int i = inter[0][0]; i < tl; i++) {
                        final int index = i;
                        Platform.runLater(() -> {
                            retirarDestaqueDeLinha(4);
                            destacarLinha(5);
                            animacaoAuxUsoDaPosicaoDoVetor("vet", index);
                            animacaoAuxUsoDaPosicaoDoVetor("vet", index - inter[0][0]);
                        });
                        Thread.sleep(DELAY);
                        if (vet[i] < vet[i - inter[0][0]]) {
                            Platform.runLater(() -> {
                                retirarDestaqueDeLinha(5);
                                destacarLinha(6);
                                destacarLinha(7);
                                destacarLinha(8);
                                animarTrocaPosVet("vet", index, index - inter[0][0]);
                                criaAtualizaVariavel("aux", vet[index]);
                            });
                            Thread.sleep(DELAY);
                            int aux = vet[i];
                            vet[i] = vet[i - inter[0][0]];
                            vet[i - inter[0][0]] = aux;
                            flag = true;
                            Platform.runLater(() -> {
                                destacarLinha(9);
                                deletarVariavel("aux");
                                retirarDestaqueDeLinha(6);
                                retirarDestaqueDeLinha(7);
                                retirarDestaqueDeLinha(8);
                                criaAtualizaVariavel("flag", "true");
                            });
                            Thread.sleep(DELAY);
                        }
                        Platform.runLater(() -> {
                            destacarLinha(4);
                            retirarDestaqueDeLinha(9);
                            retirarDestaqueDeLinha(5);
                            criaAtualizaVariavel("i", index + 1);
                        });
                        Thread.sleep(DELAY);
                    }
                    Platform.runLater(() -> {
                        destacarLinha(11);
                        retirarDestaqueDeLinha(4);
                        deletarVariavel("i");
                    });
                    Thread.sleep(DELAY);
                    if (inter[0][0] > 1) {
                        inter[0][0] = (int) (inter[0][0] / 1.3);
                        flag = true;
                        Platform.runLater(() -> {
                            destacarLinha(12);
                            retirarDestaqueDeLinha(11);
                            criaAtualizaVariavel("intervalo", inter[0][0]);
                        });
                        Thread.sleep(DELAY);
                        Platform.runLater(() -> {
                            destacarLinha(13);
                            retirarDestaqueDeLinha(12);
                            criaAtualizaVariavel("flag", "true");
                        });
                        Thread.sleep(DELAY);
                    }
                    Platform.runLater(() -> {
                        retirarDestaqueDeLinha(11);
                        retirarDestaqueDeLinha(12);
                        retirarDestaqueDeLinha(13);
                        retirarDestaqueDeLinha(4);
                        destacarLinha(2);
                    });
                    Thread.sleep(DELAY);
                }
                Platform.runLater(() -> {
                    retirarDestaqueDeLinha(2);
                });
                Thread.sleep(DELAY);
                Platform.runLater(() -> {
                    configurarBotoesFinal(false);
                    deletarVariavel("tl");
                    deletarVariavel("intervalo");
                    deletarVariavel("flag");
                    removerVetorDaTela("counting");
                });
                Thread.sleep(1);
                return null;
            }
        };
        new Thread(task).start();
    }

    private void animarTrocaPosVet(String vectorName, int pos1, int pos2) {
        HBox boxesContainer = vectorMap.get(vectorName);
        if (boxesContainer != null && pos1 >= 0 && pos1 < boxesContainer.getChildren().size()
                && pos2 >= 0 && pos2 < boxesContainer.getChildren().size()) {

            StackPane box1 = (StackPane) boxesContainer.getChildren().get(pos1);
            StackPane box2 = (StackPane) boxesContainer.getChildren().get(pos2);
            double distanciaX = box2.getBoundsInParent().getMinX() - box1.getBoundsInParent().getMinX();
            TranslateTransition subirBox1 = new TranslateTransition(Duration.millis(300), box1);
            subirBox1.setByY(-60);

            TranslateTransition moverHorizontalBox1 = new TranslateTransition(Duration.millis(400), box1);
            moverHorizontalBox1.setByX(distanciaX);

            TranslateTransition descerBox1 = new TranslateTransition(Duration.millis(300), box1);
            descerBox1.setByY(60);

            TranslateTransition descerBox2 = new TranslateTransition(Duration.millis(300), box2);
            descerBox2.setByY(60);

            TranslateTransition moverHorizontalBox2 = new TranslateTransition(Duration.millis(400), box2);
            moverHorizontalBox2.setByX(-distanciaX);

            TranslateTransition subirBox2 = new TranslateTransition(Duration.millis(300), box2);
            subirBox2.setByY(-60);

            SequentialTransition seqBox1 = new SequentialTransition(subirBox1, moverHorizontalBox1, descerBox1);
            SequentialTransition seqBox2 = new SequentialTransition(descerBox2, moverHorizontalBox2, subirBox2);
            ParallelTransition animacaoParalela = new ParallelTransition(seqBox1, seqBox2);
            animacaoParalela.setOnFinished(e -> {
                box1.setTranslateX(0);
                box1.setTranslateY(0);
                box2.setTranslateX(0);
                box2.setTranslateY(0);
                boxesContainer.getChildren().remove(box1);
                boxesContainer.getChildren().remove(box2);
                boxesContainer.getChildren().add(pos2, box1);
                boxesContainer.getChildren().add(pos1, box2);
            });
            animacaoParalela.play();
        }
    }

    private void configurarLayoutInicial() {
        cleanRoot();
        System.out.println("CONFIGURANDO LAYOUT PRINCIPAL");
        if (root == null) {
            root = new BorderPane();
            root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        HBox container = new HBox(20);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(20));
        Button initRadix = new Button("Radix");
        initRadix.setFont(new Font("Minecraft", 16));
        initRadix.setOnAction(e -> configurarRadix());
        Button initComb = new Button("Comb");
        initComb.setFont(new Font("Minecraft", 16));
        initComb.setOnAction(e -> configurarComb());
        initRadix.setMinWidth(150);
        initComb.setMinWidth(150);
        initRadix.setStyle(
                "-fx-background-color: #444444; -fx-border: 1px solid white; -fx-border-radius: 0; -fx-text-fill: white;");
        initComb.setStyle(
                "-fx-background-color: #444444; -fx-border: 1px solid white; -fx-border-radius: 0; -fx-text-fill: white;");
        StackPane centerPane = new StackPane(container);
        centerPane.setAlignment(Pos.CENTER);
        container.getChildren().addAll(initRadix, initComb);
        VBox namesContainer = new VBox(10);
        Label feito = new Label("Feito por:");
        Label g = new Label("Gustavo da Costa e Silva");
        Label v = new Label("Victor Terrengui Brandi");
        feito.setStyle("-fx-text-fill: white;");
        feito.setFont(new Font("Droid Sans Mono", 18));
        feito.setAlignment(Pos.CENTER);
        g.setStyle("-fx-text-fill: white;");
        g.setFont(new Font("Droid Sans Mono", 18));
        g.setAlignment(Pos.CENTER);
        v.setStyle("-fx-text-fill: white;");
        v.setFont(new Font("Droid Sans Mono", 18));
        v.setAlignment(Pos.CENTER);
        namesContainer.getChildren().addAll(feito, g, v);
        root.setCenter(centerPane);
        root.setBottom(namesContainer);
    }

    private void removerVetorDaTela(String nomeDoVetor) {
        HBox container = vectorMap.get(nomeDoVetor);
        if (container != null) {
            containerVetores.getChildren().remove(container);
            vectorMap.remove(nomeDoVetor);
            vectorContainerMap.remove(nomeDoVetor);
        }
        Label label = labelsMap.get(nomeDoVetor);
        if (label != null) {
            vectorLabelsContainer.getChildren().remove(label);
            labelsMap.remove(nomeDoVetor);
        }
    }

    private void deletarVariavel(String nome) {
        VBox container = variaveisMap.get(nome);
        if (container != null) {
            gridVariaveis.getChildren().remove(container);
            variaveisMap.remove(nome);
        }
    }

    private void criaAtualizaVariavel(String nome, int valor) {
        String styleBase = "-fx-border-radius: 0; -fx-border-color: white; -fx-border-width: 1px; -fx-min-width: 50px; -fx-background-color: #333333;";
        if (variaveisMap.containsKey(nome)) {
            VBox container = variaveisMap.get(nome);
            Label valorLabel = (Label) container.getChildren().get(1);
            valorLabel.setText(String.valueOf(valor));
            String originalStyle = container.getStyle();
            container.setStyle(originalStyle + " -fx-background-color: #2ECA23;");
            FadeTransition ft = new FadeTransition(Duration.millis(400), container);
            ft.setFromValue(1.0);
            ft.setToValue(0.3);
            ft.setCycleCount(2);
            ft.setAutoReverse(true);
            ft.setOnFinished(e -> container.setStyle(styleBase));
            ft.play();
        } else {
            VBox container = new VBox();
            container.setAlignment(Pos.CENTER);
            container.setSpacing(5);
            container.setPadding(new Insets(5));
            container.setStyle(styleBase);
            Label nomeLabel = new Label(nome);
            nomeLabel.setFont(new Font("Droid Sans Mono", 18));
            nomeLabel.setAlignment(Pos.CENTER);
            nomeLabel.setStyle("-fx-border-color: white; -fx-border-width: 0 0 1 0; -fx-text-fill: white;");
            Label valorLabel = new Label(String.valueOf(valor));
            valorLabel.setFont(new Font("Droid Sans Mono", 18));
            valorLabel.setAlignment(Pos.CENTER);
            valorLabel.setStyle("-fx-text-fill: white;");
            container.getChildren().addAll(nomeLabel, valorLabel);
            int index = variaveisMap.size();
            int col = index % VARS_MAX_COLS;
            int row = index / VARS_MAX_COLS;
            gridVariaveis.add(container, col, row);
            variaveisMap.put(nome, container);
            container.setStyle(styleBase + " -fx-background-color: #2ECA23;");
            FadeTransition ft = new FadeTransition(Duration.millis(400), container);
            ft.setFromValue(1.0);
            ft.setToValue(0.3);
            ft.setCycleCount(2);
            ft.setAutoReverse(true);
            ft.setOnFinished(e -> container.setStyle(styleBase));
            ft.play();
        }
    }

    private void criaAtualizaVariavel(String nome, String valor) {
        String styleBase = "-fx-border-radius: 0; -fx-border-color: white; -fx-border-width: 1px; -fx-min-width: 50px; -fx-background-color: #333333;";
        if (variaveisMap.containsKey(nome)) {
            VBox container = variaveisMap.get(nome);
            Label valorLabel = (Label) container.getChildren().get(1);
            valorLabel.setText(valor);
            String originalStyle = container.getStyle();
            container.setStyle(originalStyle + " -fx-background-color: #555555;");
            FadeTransition ft = new FadeTransition(Duration.millis(400), container);
            ft.setFromValue(1.0);
            ft.setToValue(0.3);
            ft.setCycleCount(2);
            ft.setAutoReverse(true);
            ft.setOnFinished(e -> container.setStyle(styleBase));
            ft.play();
        } else {
            VBox container = new VBox();
            container.setAlignment(Pos.CENTER);
            container.setSpacing(5);
            container.setPadding(new Insets(5));
            container.setStyle(styleBase);
            Label nomeLabel = new Label(nome);
            nomeLabel.setFont(new Font("Droid Sans Mono", 18));
            nomeLabel.setAlignment(Pos.CENTER);
            nomeLabel.setStyle("-fx-border-color: white; -fx-border-width: 0 0 1 0; -fx-text-fill: white;");
            Label valorLabel = new Label(valor);
            valorLabel.setFont(new Font("Droid Sans Mono", 18));
            valorLabel.setAlignment(Pos.CENTER);
            valorLabel.setStyle("-fx-text-fill: white;");
            container.getChildren().addAll(nomeLabel, valorLabel);
            int index = variaveisMap.size();
            int col = index % VARS_MAX_COLS;
            int row = index / VARS_MAX_COLS;
            gridVariaveis.add(container, col, row);
            variaveisMap.put(nome, container);
            container.setStyle(styleBase + " -fx-background-color: #555555;");
            FadeTransition ft = new FadeTransition(Duration.millis(400), container);
            ft.setFromValue(1.0);
            ft.setToValue(0.3);
            ft.setCycleCount(2);
            ft.setAutoReverse(true);
            ft.setOnFinished(e -> container.setStyle(styleBase));
            ft.play();
        }
    }

    private void criarVetorNaTela(String vectorName, int[] vetor) {
        try {
            Label label = new Label(vectorName);
            label.setFont(new Font("Minecraft", 21));
            label.setStyle("-fx-text-fill: white;");
            label.prefHeightProperty().bind(vectorLabelsContainer.heightProperty().multiply(0.32));
            labelsMap.put(vectorName, label);
            vectorLabelsContainer.getChildren().add(label);
            HBox boxesContainer = new HBox();
            boxesContainer.setSpacing(10);
            boxesContainer.setAlignment(Pos.CENTER);
            SequentialTransition sequentialAppear = new SequentialTransition();
            for (int i = 0; i < vetor.length; i++) {
                StackPane box = new StackPane();
                box.setPrefSize(50, 50);
                box.setMaxHeight(50);
                box.setMaxWidth(50);
                box.setMinSize(50, 50);
                box.setOpacity(0);
                box.setStyle(
                        "-fx-border-color: white; -fx-border-width: 1px; -fx-border-radius: 0; -fx-background-radius: 0;");

                box.setBackground(
                        new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
                Label valueLabel = new Label(String.valueOf(vetor[i]));
                valueLabel.setFont(new Font("Minecraft", 14));
                valueLabel.setStyle("-fx-text-fill: white;");
                box.getChildren().add(valueLabel);
                boxesContainer.getChildren().add(box);
                FadeTransition fadeIn = new FadeTransition(Duration.millis(90), box);
                fadeIn.setFromValue(0);
                fadeIn.setToValue(1.0);
                sequentialAppear.getChildren().add(fadeIn);
            }
            containerVetores.getChildren().add(boxesContainer);
            boxesContainer.prefHeightProperty().bind(containerVetores.heightProperty().multiply(0.32));
            sequentialAppear.play();
            sequentialAppear.setOnFinished(e -> {
                PauseTransition pause = new PauseTransition(Duration.millis(100));
                pause.setOnFinished(event -> {
                    ParallelTransition parallelFade = new ParallelTransition();
                    boxesContainer.getChildren().forEach(node -> {
                        StackPane box = (StackPane) node;
                        FadeTransition fadeToDark = new FadeTransition(Duration.millis(400), box);
                        fadeToDark.setFromValue(1.0);
                        fadeToDark.setToValue(0.7);
                        fadeToDark.setCycleCount(1);
                        fadeToDark.setAutoReverse(false);
                        fadeToDark.setOnFinished(evt -> {
                            box.setBackground(
                                    new Background(
                                            new BackgroundFill(Color.web("#222222"), CornerRadii.EMPTY, Insets.EMPTY)));
                            box.setOpacity(1.0);
                        });
                        parallelFade.getChildren().add(fadeToDark);
                    });
                    parallelFade.play();
                });
                pause.play();
            });
            vectorMap.put(vectorName, boxesContainer);
            vectorContainerMap.put(vectorName, boxesContainer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void criarVetorNaTela(String vectorName, int[] vetor, boolean comb) {
        try {
            Label label = new Label(vectorName);
            label.setFont(new Font("Minecraft", 21));
            label.setStyle("-fx-text-fill: white;");
            label.prefHeightProperty().bind(vectorLabelsContainer.heightProperty().multiply(comb ? 1 : 0.32));
            labelsMap.put(vectorName, label);
            vectorLabelsContainer.getChildren().add(label);
            HBox boxesContainer = new HBox();
            boxesContainer.setSpacing(10);
            boxesContainer.setAlignment(Pos.CENTER);
            SequentialTransition sequentialAppear = new SequentialTransition();
            for (int i = 0; i < vetor.length; i++) {
                StackPane box = new StackPane();
                box.setPrefSize(50, 50);
                box.setMaxHeight(50);
                box.setMaxWidth(50);
                box.setMinSize(50, 50);
                box.setOpacity(0);
                box.setStyle(
                        "-fx-border-color: white; -fx-border-width: 1px; -fx-border-radius: 0; -fx-background-radius: 0;");
                box.setBackground(
                        new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
                Label valueLabel = new Label(String.valueOf(vetor[i]));
                valueLabel.setFont(new Font("Minecraft", 14));
                valueLabel.setStyle("-fx-text-fill: white;");
                box.getChildren().add(valueLabel);
                boxesContainer.getChildren().add(box);
                FadeTransition fadeIn = new FadeTransition(Duration.millis(90), box);
                fadeIn.setFromValue(0);
                fadeIn.setToValue(1.0);
                sequentialAppear.getChildren().add(fadeIn);
            }
            containerVetores.getChildren().add(boxesContainer);
            boxesContainer.prefHeightProperty().bind(containerVetores.heightProperty().multiply(comb ? 1 : 0.32));
            sequentialAppear.play();
            sequentialAppear.setOnFinished(e -> {
                PauseTransition pause = new PauseTransition(Duration.millis(100));
                pause.setOnFinished(event -> {
                    ParallelTransition parallelFade = new ParallelTransition();
                    boxesContainer.getChildren().forEach(node -> {
                        StackPane box = (StackPane) node;
                        FadeTransition fadeToBlack = new FadeTransition(Duration.millis(400), box);
                        fadeToBlack.setFromValue(1.0);
                        fadeToBlack.setToValue(0.7);
                        fadeToBlack.setCycleCount(1);
                        fadeToBlack.setAutoReverse(false);
                        fadeToBlack.setOnFinished(evt -> {
                            box.setBackground(
                                    new Background(
                                            new BackgroundFill(Color.web("#222222"), CornerRadii.EMPTY, Insets.EMPTY)));
                            box.setOpacity(1.0);
                        });
                        parallelFade.getChildren().add(fadeToBlack);
                    });
                    parallelFade.play();
                });
                pause.play();
            });
            vectorMap.put(vectorName, boxesContainer);
            vectorContainerMap.put(vectorName, boxesContainer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void atualizaVetorPorPosRadix(String vectorName, int pos, int newValue) {
        HBox boxesContainer = vectorMap.get(vectorName);
        if (boxesContainer != null && pos >= 0 && pos < boxesContainer.getChildren().size()) {
            StackPane box = (StackPane) boxesContainer.getChildren().get(pos);
            Label valueLabel = (Label) box.getChildren().get(0);
            valueLabel.setText(String.valueOf(newValue));
            box.setStyle(
                    "-fx-border-color: white; -fx-border-width: 1px; -fx-border-radius: 0; -fx-background-radius: 0; -fx-background-color: #2ECA23;");
            FadeTransition ft = new FadeTransition(Duration.millis(400), box);
            ft.setFromValue(1.0);
            ft.setToValue(0.3);
            ft.setCycleCount(2);
            ft.setAutoReverse(true);
            ft.setOnFinished(e -> box.setStyle(
                    "-fx-border-color: white; -fx-border-width: 1px; -fx-border-radius: 0; -fx-background-radius: 0;"));
            ft.play();
        }
    }

    private void animacaoAuxUsoDaPosicaoDoVetor(String vectorName, int pos) {
        HBox boxesContainer = vectorMap.get(vectorName);
        if (boxesContainer != null && pos >= 0 && pos < boxesContainer.getChildren().size()) {
            StackPane box = (StackPane) boxesContainer.getChildren().get(pos);
            box.setStyle(
                    "-fx-border-color: white; -fx-border-width: 1px; -fx-border-radius: 0; -fx-background-radius: 0; -fx-background-color: deepskyblue;");
            FadeTransition ft = new FadeTransition(Duration.millis(400), box);
            ft.setFromValue(1.0);
            ft.setToValue(0.3);
            ft.setCycleCount(2);
            ft.setAutoReverse(true);
            ft.setOnFinished(e -> box.setStyle(
                    "-fx-border-color: white; -fx-border-width: 1px; -fx-border-radius: 0; -fx-background-radius: 0;"));
            ft.play();
        }
    }

    private void configurarRadix() {
        cleanRoot();
        vet = new int[10];
        tl = 10;
        configurarLayout(true);
        configurarCodigo();
        configurarVetores(true);
        configurarStartBtn(true);
    }

    private void configurarComb() {
        cleanRoot();
        vet = new int[10];
        tl = 10;
        configurarLayout(false);
        configurarCodigo();
        configurarVetores(false);
        configurarStartBtn(false);
    }

    public void configurarStartBtn(boolean radix) {
        initRadixInterno = new Button("Iniciar");
        initRadixInterno.setFont(new Font("Minecraft", 16));
        initRadixInterno.setStyle(
                "-fx-background-color: #444444; -fx-border: 1px solid white; -fx-border-radius: 0; -fx-text-fill: white;");
        if (radix) {
            initRadixInterno.setOnAction(e -> radixSort());
        } else {
            initRadixInterno.setOnAction(e -> combSort());
        }
        bottomContainer.getChildren().add(initRadixInterno);
    }

    public void refazerRadix() {
        for (int i = 0; i < 23; i++) {
            removerLinhaCodigo(i);
        }
        removerVetorDaTela("vet");
        removerVetorDaTela("vet_ordenado");
        removerVetorDaTela("counting");
        deletarVariavel("max");
        deletarVariavel("dgt");
        bottomContainer.getChildren().remove(buttonsContainer);
        criarVetorNaTela("vet", vet);
        radixSort();
    }

    private void configurarVetores(boolean radix) {
        vectorLabelsContainer = new VBox();
        vectorLabelsContainer.prefWidthProperty().bind(bottomBottomContainer.widthProperty().multiply(0.20));
        containerVetores = new VBox();
        containerVetores.prefWidthProperty().bind(bottomBottomContainer.widthProperty().multiply(0.79));
        bottomBottomContainer.getChildren().addAll(vectorLabelsContainer, containerVetores);
        vectorMap.clear();
        vectorContainerMap.clear();
        if (radix) {
            criarVetorNaTela("vet", vet);
        } else {
            criarVetorNaTela("vet", vet, true);
        }
    }

    private void configurarLayout(boolean radix) {
        topBottomContainer = new VBox();
        topBottomContainer.setPadding(new Insets(10));
        double spacing = 20;
        topBottomContainer.setSpacing(spacing);
        containerTop = new HBox();
        containerTop.setSpacing(10);
        VBox boxLeft = new VBox();
        boxLeft.setAlignment(Pos.TOP_CENTER);
        boxLeft.setPadding(new Insets(10));
        boxLeft.setSpacing(10);
        boxLeft.setStyle(
                "-fx-border-color: white; -fx-border-width: 1px; -fx-border-radius: 0; -fx-background-radius: 0; -fx-background-color: #222222;");
        Label variaveisLabel = new Label("Variáveis");
        variaveisLabel.setFont(new Font("Minecraft", 21));
        variaveisLabel.setStyle("-fx-text-fill: white;");
        gridVariaveis = new GridPane();
        gridVariaveis.setHgap(10);
        gridVariaveis.setVgap(8);
        gridVariaveis.setAlignment(Pos.TOP_LEFT);
        boxLeft.getChildren().addAll(variaveisLabel, gridVariaveis);
        containerCodigo = new VBox();
        containerCodigo.setAlignment(Pos.TOP_CENTER);
        containerCodigo.setPadding(new Insets(10));
        containerCodigo.setStyle(
                "-fx-border-color: white; -fx-border-width: 1px; -fx-border-radius: 0; -fx-background-radius: 0; -fx-background-color: #222222;");
        HBox.setHgrow(boxLeft, Priority.ALWAYS);
        HBox.setHgrow(containerCodigo, Priority.ALWAYS);
        boxLeft.prefWidthProperty().bind(containerTop.widthProperty().multiply(0.5));
        containerCodigo.prefWidthProperty().bind(containerTop.widthProperty().multiply(0.5));
        containerTop.getChildren().addAll(boxLeft, containerCodigo);
        bottomContainer = new VBox(10);
        bottomContainer.setAlignment(Pos.TOP_CENTER);
        bottomContainer.setStyle(
                "-fx-border-color: white; -fx-border-width: 1px; -fx-border-radius: 0; -fx-background-radius: 0; -fx-background-color: #222222;");
        bottomContainer.setPadding(new Insets(10));
        bottomBottomContainer = new HBox();
        bottomBottomContainer.setSpacing(10);
        Label labelVetores = new Label("Vetores");
        labelVetores.setFont(new Font("Minecraft", 21));
        labelVetores.setStyle("-fx-text-fill: white;");
        bottomContainer.getChildren().addAll(labelVetores, bottomBottomContainer);
        topBottomContainer.getChildren().addAll(containerTop, bottomContainer);
        containerTop.prefHeightProperty()
                .bind(topBottomContainer.heightProperty().subtract(spacing).multiply(0.5));
        bottomContainer.prefHeightProperty()
                .bind(topBottomContainer.heightProperty().subtract(spacing).multiply(0.45));
        bottomBottomContainer.prefHeightProperty().bind(bottomContainer.heightProperty().multiply(0.90));
        root.setCenter(topBottomContainer);
    }

    private void configurarCodigo() {
        Label label = new Label("Código");
        label.setFont(new Font("Minecraft", 21));
        label.setStyle("-fx-text-fill: white;");
        containerCodigoLinhas = new VBox();
        containerCodigoLinhas.setPadding(new Insets(10));
        containerCodigo.getChildren().addAll(label, containerCodigoLinhas);
    }

    private void adicionarLinhaCodigo(String linha, int num) {
        StackPane box = new StackPane();
        box.setAlignment(Pos.TOP_LEFT);
        box.setMaxHeight(25);
        Label valueLabel = new Label(linha);
        valueLabel.setFont(Font.font("Droid Sans Mono", 19));
        valueLabel.setStyle("-fx-text-fill: white;");
        box.getChildren().add(valueLabel);
        linhasCodigoMap.put(num, box);
        containerCodigoLinhas.getChildren().add(box);
    }

    private void destacarLinha(int numLinha) {
        StackPane box = linhasCodigoMap.get(numLinha);
        if (box != null) {
            box.setStyle(box.getStyle() + " -fx-background-color: #2ECA23;");
        }
    }

    private void retirarDestaqueDeLinha(int numLinha) {
        StackPane box = linhasCodigoMap.get(numLinha);
        if (box != null) {
            box.setStyle(box.getStyle() + " -fx-background-color: #222222;");
        }
    }

    private void removerLinhaCodigo(int num) {
        StackPane box = linhasCodigoMap.get(num);
        if (box != null) {
            containerCodigoLinhas.getChildren().remove(box);
            linhasCodigoMap.remove(num);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
