package com.back;

import java.util.Scanner;

public class App {
    private final Scanner sc;

    public App(Scanner sc) {
        this.sc = sc;
    }

    public void run() {
        WiseSayingRepository wiseSayingRepository = new WiseSayingRepository();
        WiseSayingService wiseSayingService = new WiseSayingService(wiseSayingRepository);
        WiseSayingController wiseSayingController = new WiseSayingController(sc, wiseSayingService);
        SystemController systemController = new SystemController();

        System.out.println("== 명언 앱 ==");

        while (true) {
            System.out.print("명령) ");
            String command = sc.nextLine();
            Rq rq = new Rq(command);

            String commandName = rq.getCommandName();

            if (commandName.equals("종료")) {
                systemController.exit();
                break;
            } else if (commandName.equals("등록")) {
                wiseSayingController.write();
            } else if (commandName.equals("목록")) {
                wiseSayingController.list(rq);
            } else if (commandName.equals("삭제")) {
                wiseSayingController.delete(rq);
            } else if (commandName.equals("수정")) {
                wiseSayingController.modify(rq);
            } else if (commandName.equals("빌드")) {
                wiseSayingController.build(rq);
            }
        }
    }
}
