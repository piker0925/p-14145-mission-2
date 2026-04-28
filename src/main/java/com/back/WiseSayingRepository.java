package com.back;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class WiseSayingRepository {
    private int lastId = 0;
    private final List<WiseSaying> wiseSayings = new ArrayList<>();
    private final String DB_PATH = "db/wiseSaying/";

    public WiseSayingRepository() {
        load();
    }

    private void load() {
        wiseSayings.clear(); // 여기서 한 번만 비워주면 생성자든 어디든 안전합니다.

        String lastIdStr = Util.readFromFile(DB_PATH + "lastId.txt");
        if (lastIdStr != null) {
            this.lastId = Integer.parseInt(lastIdStr.trim());
        }

        File dir = new File(DB_PATH);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> name.endsWith(".json"));
            if (files != null) {
                for (File file : files) {
                    String json = Util.readFromFile(file.getPath());
                    if (json != null) {
                        wiseSayings.add(parseJson(json));
                    }
                }
            }
        }
        
        wiseSayings.sort(Comparator.comparing(WiseSaying::getId).reversed());
    }

    private WiseSaying parseJson(String json) {
        int id = Integer.parseInt(json.split("\"id\":")[1].split(",")[0].trim());
        String content = json.split("\"content\": \"")[1].split("\",")[0];
        String author = json.split("\"author\": \"")[1].split("\"")[0];
        
        return new WiseSaying(id, content, author);
    }

    public WiseSaying save(String content, String author) {
        lastId++;
        WiseSaying ws = new WiseSaying(lastId, content, author);
        wiseSayings.add(0, ws);

        saveToFile(ws);
        Util.saveToFile(DB_PATH + "lastId.txt", String.valueOf(lastId));

        return ws;
    }

    public void saveToFile(WiseSaying ws) {
        Util.saveToFile(DB_PATH + ws.getId() + ".json", ws.toJson());
    }

    public List<WiseSaying> findAll() {
        return wiseSayings;
    }

    public WiseSaying findById(int id) {
        for (WiseSaying ws : wiseSayings) {
            if (ws.getId() == id) return ws;
        }
        return null;
    }

    public void remove(WiseSaying ws) {
        wiseSayings.remove(ws);
        Util.deleteFile(DB_PATH + ws.getId() + ".json");
    }
}
