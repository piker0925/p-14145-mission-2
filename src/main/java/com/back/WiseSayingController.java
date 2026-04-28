package com.back;

import java.util.List;
import java.util.Scanner;

public class WiseSayingController {
    private final Scanner sc;
    private final WiseSayingService wiseSayingService;

    public WiseSayingController(Scanner sc, WiseSayingService wiseSayingService) {
        this.sc = sc;
        this.wiseSayingService = wiseSayingService;
    }

    public void write() {
        System.out.print("명언 : ");
        String content = sc.nextLine();
        System.out.print("작가 : ");
        String author = sc.nextLine();

        WiseSaying ws = wiseSayingService.write(content, author);
        System.out.println(ws.getId() + "번 명언이 등록되었습니다.");
    }

    public void list(Rq rq) {
        String keywordType = rq.getParam("keywordType", "all");
        String keyword = rq.getParam("keyword", "");
        int page = rq.getParamAsInt("page", 1);
        int pageSize = rq.getParamAsInt("pageSize", 5);

        List<WiseSaying> wiseSayings = wiseSayingService.findAll(keywordType, keyword, page, pageSize);
        int totalItems = wiseSayingService.getTotalCount(keywordType, keyword);
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        if (!keyword.isEmpty()) {
            System.out.println("----------------------");
            System.out.println("검색타입 : " + keywordType);
            System.out.println("검색어 : " + keyword);
            System.out.println("----------------------");
        }

        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");
        for (WiseSaying ws : wiseSayings)
            System.out.printf("%d / %s / %s\n", ws.getId(), ws.getAuthor(), ws.getContent());
        System.out.println("----------------------");

        System.out.print("페이지 : ");
        if (totalPages == 0) {
            System.out.println("1");
        } else {
            for (int i = 1; i <= totalPages; i++) {
                if (i == page) {
                    System.out.print("[" + i + "]");
                } else {
                    System.out.print(i);
                }

                if (i < totalPages) {
                    System.out.print(" / ");
                }
            }
            System.out.println();
        }
    }

    public void delete(Rq rq) {
        int id = rq.getParamAsInt("id", -1);
        if (id == -1) {
            System.out.println("번호를 정확히 입력해주세요.");
            return;
        }

        WiseSaying ws = wiseSayingService.findById(id);

        if (ws != null) {
            wiseSayingService.remove(ws);
            System.out.println(id + "번 명언이 삭제되었습니다.");
        } else {
            System.out.println(id + "번 명언은 존재하지 않습니다.");
        }
    }

    public void modify(Rq rq) {
        int id = rq.getParamAsInt("id", -1);
        if (id == -1) {
            System.out.println("번호를 정확히 입력해주세요.");
            return;
        }

        WiseSaying ws = wiseSayingService.findById(id);

        if (ws != null) {
            System.out.println("명언(기존) : " + ws.getContent());
            System.out.print("명언 : ");
            String content = sc.nextLine();
            System.out.println("작가(기존) : " + ws.getAuthor());
            System.out.print("작가 : ");
            String author = sc.nextLine();

            wiseSayingService.modify(ws, content, author);
        } else {
            System.out.println(id + "번 명언은 존재하지 않습니다.");
        }
    }

    public void build(Rq rq) {
        wiseSayingService.build();
        System.out.println("data.json 파일의 내용이 갱신되었습니다.");
    }
}
