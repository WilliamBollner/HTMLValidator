package service;

/**
 *
 * @author willi
 */
import java.io.*;
import java.util.*;
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

    public boolean validateFile(String filePath) {
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

            while (!tagsStack.isEmpty()) {
                String unclosedTag = tagsStack.pop();
                ui.preencherTextArea("Faltam tags finais para a seguinte tag de início: " + unclosedTag);
            }

        } catch (IOException e) {
            ui.preencherTextArea("Erro ao ler o arquivo: " + e.getMessage());
            return false;
        }

        // Se chegamos até aqui, o arquivo está bem formatado
        printTagsCount();
        return true;
    }

    private void processTag(String tag) {
        if (tag.startsWith("</")) {
            // Tag de fechamento
            String closingTag = tag.substring(2, tag.length() - 1);
            if (!tagsStack.isEmpty() && tagsStack.peek().equals(closingTag)) {
                tagsStack.pop();
            } else {
                ui.preencherTextArea("Foi encontrada uma tag final inesperada: " + closingTag);
            }
        } else if (tag.startsWith("<")) {
            // Tag de início
            String openingTag = tag.substring(1, tag.indexOf('>'));
            if (!SINGLETON_TAGS.contains(openingTag)) {
                tagsStack.push(openingTag);
            }
            tagsCount.put(openingTag, tagsCount.getOrDefault(openingTag, 0) + 1);
        }
    }

    private void printTagsCount() {
        ui.preencherTextArea("Tags encontradas:");
        tagsCount.forEach((tag, count) -> {
            System.out.println(tag + ": " + count);
            ui.preencherTable(tag, count);
        });
    }
}
