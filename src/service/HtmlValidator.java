package service;

/**
 *
 * @author willi
 */
import java.io.*;
import java.util.*;
import javax.swing.JFrame;
import view.AppUI;

public class HtmlValidator {
    private Stack<String> tagsStack = new Stack<>();
    private HashMap<String, Integer> tagsCount = new HashMap<>();
    private static final Set<String> SINGLETON_TAGS = 
        new HashSet<>(Arrays.asList("meta", "base", "br", "col", "command", "embed", "hr", "img", "input", "link", "param", "source", "!DOCTYPE"));
    private AppUI ui;

    public HtmlValidator(AppUI ui) {
        this.ui = ui;
    }
    
    
    /**
     *
     * @param filePath
     * @return
     */
    public boolean validateFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                processLine(line.trim().toLowerCase());
            }
            
            // Verificar se todas as tags foram fechadas
            if (!tagsStack.isEmpty()) {
                System.out.println("Faltam tags finais para as seguintes tags de início: " + tagsStack);
                ui.preencherTextArea("Faltam tags finais para as seguintes tags de início:" + tagsStack);
                return false;
            }
            
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
            ui.preencherTextArea("Erro ao ler o arquivo: " + e.getMessage());
            return false;
        }
        
        // Se chegamos até aqui, o arquivo está bem formatado
        printTagsCount();
        return true;
    }
    
    private void processLine(String line) {
        // Processamento básico para extrair e validar tags, pode ser expandido
        if (line.startsWith("<")) {
            if (line.startsWith("</")) {
                // Tag final
                String closingTag = line.substring(2, line.indexOf('>'));
                if (!tagsStack.isEmpty() && tagsStack.peek().equals(closingTag)) {
                    tagsStack.pop();
                } else {
                    System.out.println("Foi encontrada uma tag final inesperada: " + closingTag);
                    ui.preencherTextArea("Foi encontrada uma tag final inesperada: " + closingTag);
                }
            } else {
                // Tag de início
                String openingTag = line.substring(1, line.indexOf(' ') == -1 ? line.indexOf('>') : line.indexOf(' '));
                if (!SINGLETON_TAGS.contains(openingTag)) {
                    tagsStack.push(openingTag);
                }
                tagsCount.put(openingTag, tagsCount.getOrDefault(openingTag, 0) + 1);
            }
        }
    }
    
    private void printTagsCount() {
        System.out.println("Tags encontradas:");
        ui.preencherTextArea("Tags encontradas:");
        tagsCount.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
        tagsCount.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> ui.preencherTextArea(entry.getKey() + ": " + entry.getValue()));
    }
}

