package org.github.json.flattener;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TestApp {
    public static void main(String[] arg) throws Exception {
        TestApp app = new TestApp();
        String content = app.readJsonFile("D:\\Projects\\json\\3b774348-9579-48ae-8798-d47fe225f684.json");
        List<String> jsonList = JsonArrayFlattener.getFlattenedJson(content);
        app.print(jsonList);
    }

    private void print(List<String> list) throws IOException, Exception {
        int i = 0;
        FileWriter writer = new FileWriter("D:\\Projects\\json\\out.json");
        for(String value: list) {
            writer.write(value+"\n");
        }
        writer.flush();
        writer.close();
    }

    private String readJsonFile (String file) throws IOException {
        List<String> content = Files.readAllLines(Paths.get(file));
        return content.get(0);
    }
}
