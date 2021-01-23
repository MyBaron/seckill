package com.github.lyrric.ui;

import com.github.lyrric.service.HttpService;
import com.github.lyrric.service.SecKillService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created on 2020-08-14.
 * 控制台模式
 *
 * @author wangxiaodong
 */
public class ConsoleMode {

    private final Logger log = LogManager.getLogger(ConsoleMode.class);

    private ExecutorService service = Executors.newFixedThreadPool(100);

    private HttpService httpService = new HttpService();

    private SecKillService secKillService = new SecKillService();

    public void start() throws IOException, ParseException, InterruptedException {
//        Scanner sc = new Scanner(System.in);
//        log.info("请输入sessionId：");
//        Config.cookie = sc.nextLine().trim();
////        log.info("请输入Cookie：");
////        Config.cookies = sc.nextLine().trim();
//        log.info("获取接种人员......");
//        Member members = httpService.getMembers();
//        log.info("{}-{}-{}", 0, members.getCname(), members.getIdcard());
//        log.info("请输入接种人员序号：");
//        int no = Integer.parseInt(sc.nextLine());
//        Config.idCard = members.getCname();
//
//        log.info("获取疫苗列表......");
//        List<VaccineList> vaccineList = httpService.getVaccineList();
//        for (int i = 0; i < vaccineList.size(); i++) {
//            VaccineList item = vaccineList.get(i);
//            log.info("{}-{}-{}-{}-{}", i, item.getCname(), item.getVaccineName(), item.getAddr(), item.getStartTime());
//        }
//        log.info("请输入疫苗序号：");
//        no = Integer.parseInt(sc.nextLine());
//        int code = vaccineList.get(no).getId();
//        String startTime = vaccineList.get(no).getStartTime();
//        log.info("按回车键开始秒杀：");
//        sc.nextLine();
//        secKillService.startSecKill(code, startTime, null);
    }

}
