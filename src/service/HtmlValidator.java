package service;

import java.io.*;

import estrutura.listaEncadeada.ListaEncadeada;
import estrutura.listaEncadeada.NoLista;
import estrutura.ordenacao.OrdenacaoMergeSort;
import estrutura.pilha.PilhaCheiaException;
import estrutura.pilha.PilhaVaziaException;
import estrutura.pilha.PilhaVetor;
import view.AppUI;

public class HtmlValidator {

    private PilhaVetor<String> tagsStack = new PilhaVetor<String>(100);
    private ListaEncadeada<String> tagsCount = new ListaEncadeada<>();
    private ListaEncadeada<String> tagsSingleton = new ListaEncadeada<>();
    private ListaEncadeada<String> singleton = new ListaEncadeada<>();
    private ListaEncadeada<String> listToIgnore = new ListaEncadeada<>();
    private ListaEncadeada<String> openedTags = new ListaEncadeada<>();
    private ListaEncadeada<String> closedTags = new ListaEncadeada<>();
    private AppUI ui;
    private boolean isError = false;
    boolean unexpectedTagFound = false;

    public HtmlValidator(AppUI ui) {
        this.ui = ui;
    }

    public boolean validateFile(String filePath) {
        preencherSingletons();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean insideTag = false;
            boolean atribute = false; //variavel que vai ser true se houver um espaço depois da tag
            StringBuilder currentTag = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                for (char c : line.toCharArray()) {
                    if (c == '<') {
                        insideTag = true;
                        currentTag = new StringBuilder();
                    }

                    if (insideTag) {
                        if (c != ' ' && !atribute && c != '>') { //se não for um espaço e a variavel for falsa
                            currentTag.append(c);
                        } else {
                            atribute = true;

                        }
                        if (c == '>') {
                            currentTag.append(c);
                            insideTag = false;
                            atribute = false;
                            processTag(currentTag.toString());
                        }
                    }
                }
            }
            checkTags();
        } catch (IOException e) {
            ui.fillTextArea("Erro ao ler o arquivo: " + e.getMessage());
            return false;
        } catch (PilhaVaziaException e) {
            throw new RuntimeException(e);
        } catch (PilhaCheiaException e) {
            throw new RuntimeException(e);
        }

        if (!isError) {
            printTagsCount();
        }
        return true;
    }

    private void processTag(String tag) throws PilhaVaziaException, PilhaCheiaException {
        if (tag.startsWith("</")) {
            // Tag de fechamento
            String closingTag = tag.substring(2, tag.length() - 1).toLowerCase();
            if (!tagsStack.estaVazia() && tagsStack.peek().equals(closingTag)) {
                tagsStack.pop();
            } else if (!unexpectedTagFound) {
                ui.fillTextArea("Foi encontrada uma tag final inesperada: " + closingTag + ". O esperado era: " + tagsStack.peek());
                unexpectedTagFound = true;
                isError = true;
            }
            closedTags.inserir(closingTag);
        } else if (tag.startsWith("<")) {
            // Tag de início
            String openingTag = tag.substring(1, tag.indexOf('>')).toLowerCase();
            if (singleton.buscar(openingTag) == null) {
                tagsStack.push(openingTag);
                openedTags.inserir(openingTag);
            } else {
                tagsSingleton.inserir(openingTag);
            }
            tagsCount.inserir(openingTag);
        }
    }
    
    private void sortTags(ListaEncadeada<String> lista) {
    	String[] vetor = new String[lista.getComprimento()];
    	NoLista<String> atual = lista.getPrimeiro();
    	int comprimentoLista = lista.getComprimento();
    	
    	for (int i = 0 ; i < comprimentoLista ; i++) {
    		vetor[i] = atual.getInfo();
    		NoLista<String> anterior = atual;
    		atual = atual.getProximo();
    		lista.retirar(anterior.getInfo());
    		
    	}
    	
    	OrdenacaoMergeSort<String> ord = new OrdenacaoMergeSort<String>();
    	ord.setInfo(vetor);
    	ord.ordenar();
    	vetor = ord.getInfo();
    	for (int i = comprimentoLista-1 ; i >= 0 ; i--) {
    		lista.inserir(vetor[i]);
    	}
    	
    	//Testes
    	ord.display();
    	if(lista.getPrimeiro().getInfo() == vetor[0]) {
			System.out.println("Ordenação feita!");
		}
    }

    private void printTagsCount() {
    	sortTags(tagsCount);
        ui.fillTextArea("Tags encontradas:");
        String info = tagsCount.getPrimeiro().getInfo();
        NoLista<String> atual = tagsCount.getPrimeiro();
        do {
            if (atual != null) {
                if (listToIgnore.buscar(atual.getInfo()) == null) {
                    info = atual.getInfo();
                    int n = contarTags(tagsCount, info);
                    listToIgnore.inserir(info);
                    ui.fillTable(info, Integer.toString(n));
                }
            }
            atual = atual.getProximo();
        } while (atual != null);
    }

    public <T> int contarTags(ListaEncadeada<T> list, T info) {
        NoLista<T> atual = list.getPrimeiro();
        int contador = 0;

        while (atual != null) {
            if (atual.getInfo().equals(info)) {
                contador++;
            }
            atual = atual.getProximo();
        }
        return contador;
    }

    private void checkTags() {
        NoLista<String> atualOpen = openedTags.getPrimeiro();
        if (!openedTags.estaVazia()) {
            while (atualOpen != null) {
                if (closedTags.buscar(atualOpen.getInfo()) != null) {
                    openedTags.retirar(atualOpen.getInfo());
                    closedTags.retirar(atualOpen.getInfo());
                }
                atualOpen = atualOpen.getProximo();
            }
            printTagErrors();
        }
    }

    private void printTagErrors() {
        if (!openedTags.estaVazia()) {
            NoLista<String> atual = openedTags.getPrimeiro();
            while (atual != null) {
                ui.fillTextArea("Faltam tags finais para a seguinte tag de início: " + atual.getInfo());
                isError = true;
                atual = atual.getProximo();
            }
        }
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
        singleton.inserir("!doctype");
    }
}