package com.back;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class WiseSayingService {
    private final WiseSayingRepository wiseSayingRepository;

    public WiseSayingService(WiseSayingRepository wiseSayingRepository) {
        this.wiseSayingRepository = wiseSayingRepository;

        if (wiseSayingRepository.findAll().isEmpty()) {
            for (int i = 1; i <= 10; i++) {
                write("명언 " + i, "작가 " + i);
            }
        }
    }

    public WiseSaying write(String content, String author) {
        return wiseSayingRepository.save(content, author);
    }

    public List<WiseSaying> findAll(String keywordType, String keyword, int page, int pageSize) {
        List<WiseSaying> filtered = wiseSayingRepository.findAll().stream()
                .filter(ws -> {
                    if (keyword.isEmpty()) return true;
                    if (keywordType.equals("all")) return ws.getContent().contains(keyword) || ws.getAuthor().contains(keyword);
                    if (keywordType.equals("content")) return ws.getContent().contains(keyword);
                    if (keywordType.equals("author")) return ws.getAuthor().contains(keyword);
                    return false;
                })
                .toList();

        List<WiseSaying> result = new ArrayList<>();
        int startIndex = (page - 1) * pageSize;

        for (int i = startIndex; i < startIndex + pageSize && i < filtered.size(); i++) {
            result.add(filtered.get(i));
        }
        
        return result;
    }

    public int getTotalCount(String keywordType, String keyword) {
        return (int) wiseSayingRepository.findAll().stream()
                .filter(ws -> {
                    if (keyword.isEmpty()) return true;
                    if (keywordType.equals("all")) return ws.getContent().contains(keyword) || ws.getAuthor().contains(keyword);
                    if (keywordType.equals("content")) return ws.getContent().contains(keyword);
                    if (keywordType.equals("author")) return ws.getAuthor().contains(keyword);
                    return false;
                }).count();
    }

    public WiseSaying findById(int id) {
        return wiseSayingRepository.findById(id);
    }

    public void remove(WiseSaying ws) {
        wiseSayingRepository.remove(ws);
    }

    public void modify(WiseSaying ws, String content, String author) {
        ws.setContent(content);
        ws.setAuthor(author);
        wiseSayingRepository.saveToFile(ws);
    }

    public void build() {
        List<WiseSaying> wiseSayings = wiseSayingRepository.findAll().stream()
                .sorted(Comparator.comparing(WiseSaying::getId))
                .toList();

        StringBuilder sb = new StringBuilder();
        sb.append("[\n");

        for(int i=0; i<wiseSayings.size(); i++) {
            WiseSaying ws = wiseSayings.get(i);

            String json = ws.toJson().indent(2).stripTrailing();
            sb.append(json);

            if(i<wiseSayings.size() -1) {
                sb.append(",");
            }
            sb.append("\n");
        }

        sb.append("]");
        Util.saveToFile("db/wiseSaying/data.json", sb.toString());
    }
}
