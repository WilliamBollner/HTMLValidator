package service;

import java.io.*;
import estrutura.listaEncadeada.ListaEncadeada;
import estrutura.listaEncadeada.NoLista;
import estrutura.pilha.PilhaCheiaException;
import estrutura.pilha.PilhaVaziaException;
import estrutura.pilha.PilhaVetor;
import view.AppUI;

public class HtmlValidator {

    private PilhaVetor<String> tagsStack = new PilhaVetor<String>(100);
    private ListaEncadeada<String> tagsCount = new ListaEncadeada<>();
    private ListaEncadeada<String> singleton = new ListaEncadeada<>();
    private AppUI ui;

    public HtmlValidator(AppUI ui) {
        this.ui = ui;
    }

    public boolean validateFile(String filePath) {
        preencherSingletons();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean insideTag = false;
            StringBuilder currentTag = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                for (char c : line.toCharArray()) {
                    if (c == '<') {
                        insideTag = true;
                        currentTag = new StringBuilder();
                    }

                    if (insideTag) {
                        currentTag.append(c);
                        if (c == '>') {
                            insideTag = false;
                            processTag(currentTag.toString());
                        }
                    }
                }
            }

            while (!tagsStack.estaVazia()) {
                String unclosedTag = tagsStack.pop();
                ui.preencherTextArea("Faltam tags finais para a seguinte tag de início: " + unclosedTag);
            }

        } catch (IOException e) {
            ui.preencherTextArea("Erro ao ler o arquivo: " + e.getMessage());
            return false;
        } catch (PilhaVaziaException e) {
            throw new RuntimeException(e);
        } catch (PilhaCheiaException e) {
            throw new RuntimeException(e);
        }

        // Se chegamos até aqui, o arquivo está bem formatado
        printTagsCount();
        return true;
    }

    private void processTag(String tag) throws PilhaVaziaException, PilhaCheiaException {
        if (tag.startsWith("</")) {
            // Tag de fechamento
            String closingTag = tag.substring(2, tag.length() - 1);
            if (!tagsStack.estaVazia() && tagsStack.peek().equals(closingTag)) {
                tagsStack.pop();
            } else {
                ui.preencherTextArea("Foi encontrada uma tag final inesperada: " + closingTag);
            }
        } else if (tag.startsWith("<")) {
            // Tag de início
            String openingTag = tag.substring(1, tag.indexOf('>'));
            if (singleton.buscar(openingTag) == null) {
                tagsStack.push(openingTag);
            }
            tagsCount.inserir(openingTag);
        }
    }

    private void printTagsCount() {
        ui.preencherTextArea("Tags encontradas:");
        String info = tagsCount.getPrimeiro().getInfo();
        int n = contarTags(tagsCount, info);
        NoLista<String> atual = tagsCount.getPrimeiro();
        do {
            atual = atual.getProximo();
            if (atual != null) {
                info = atual.getInfo();
                n = contarTags(tagsCount, info);
                System.out.println(info + ": " + n);
                ui.preencherTable(info, Integer.toString(n));
            }
        } while (atual != null);
    }

    public <T> int contarTags(ListaEncadeada<T> lista, T info) {
        NoLista<T> atual = lista.getPrimeiro();
        int contador = 0;

        while (atual != null) {
            if (atual.getInfo().equals(info)) {
                contador++;
            }
            atual = atual.getProximo();
        }
        return contador;
    }

    private void preencherSingletons() {
        singleton.inserir("meta");
        singleton.inserir("base");
        singleton.inserir("br");
        singleton.inserir("col");
        singleton.inserir("command");
        singleton.inserir("embed");
        singleton.inserir("hr");
        singleton.inserir("img");
        singleton.inserir("input");
        singleton.inserir("link");
        singleton.inserir("param");
        singleton.inserir("source");
        singleton.inserir("!DOCTYPE");
    }
}
