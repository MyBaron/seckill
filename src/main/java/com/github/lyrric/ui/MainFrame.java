package com.github.lyrric.ui;

import com.github.lyrric.conf.Config;
import com.github.lyrric.model.*;
import com.github.lyrric.service.SecKillService;
import com.github.lyrric.util.ParseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created on 2020-07-21.
 *
 * @author wangxiaodong
 */
@Slf4j
public class MainFrame extends JFrame {

    SecKillService service = new SecKillService();
    /**
     * 疫苗列表
     */
    private List<Vaccine> vaccines;

    JButton startBtn;

    JButton setCookieBtn;

    JButton setMemberBtn;

    JTable vaccinesTable;

    JButton refreshBtn;

    DefaultTableModel tableModel;

    JTextArea note;

    JComboBox<Area> provinceBox;

    JComboBox<Area> cityBox;


    public MainFrame() {
        setLayout(null);
        setTitle("给我丶鼓励");
        setBounds(700, 700, 880, 540);
        init();
        setLocationRelativeTo(null);
        setVisible(true);
        this.setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void init() {
        startBtn = new JButton("开始");
        startBtn.setEnabled(false);
        startBtn.addActionListener(e -> start());

        setCookieBtn = new JButton("设置Cookie");
        setCookieBtn.addActionListener((e) -> {
            ConfigDialog dialog = new ConfigDialog(this);
            dialog.setModal(true);
            dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
            dialog.setVisible(true);
            if (dialog.success()) {
                setMemberBtn.setEnabled(true);
//                startBtn.setEnabled(true);
//                refreshBtn.setEnabled(true);
                appendMsg("设置Cookie成功");
            }

        });
        setMemberBtn = new JButton("获取成员信息");
        setMemberBtn.setEnabled(false);
        setMemberBtn.addActionListener((e) -> {
            MemberDialog dialog = new MemberDialog(this);
            dialog.setModal(true);
            dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
            dialog.setVisible(true);
            if (dialog.success()) {
                appendMsg("已设置成员：" + Config.memberName);
                refreshBtn.setEnabled(true);
            }
        });

        refreshBtn = new JButton("获取疫苗列表");
        refreshBtn.setEnabled(false);
        refreshBtn.addActionListener((e) -> {
            refreshVaccines();
            startBtn.setEnabled(true);
        });

        note = new JTextArea();
        note.append("日记记录：\r\n");
        note.setEditable(false);
        note.setAutoscrolls(true);
        note.setLineWrap(true);
        JScrollPane scroll = new JScrollPane(note);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        String[] columnNames = {"疫苗编码", "疫苗名称", "诊所编码", "诊所名称", "预约时间", "是否可以预约"};
        tableModel = new TableModel(new String[0][], columnNames);
        vaccinesTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(vaccinesTable);

        List<Area> areas = ParseUtil.getAreas();
        provinceBox = new JComboBox<>(areas.toArray(new Area[0]));
        //itemListener
        ItemListener itemListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent arg0) {
                if (ItemEvent.SELECTED == arg0.getStateChange()) {
                    Area selectedItem = (Area) arg0.getItem();
                    cityBox.removeAllItems();
                    List<Area> children = ParseUtil.getChildren(selectedItem.getName());
                    for (Area child : children) {
                        cityBox.addItem(child);
                    }
                }

            }
        };
        provinceBox.addItemListener(itemListener);
        cityBox = new JComboBox<>(ParseUtil.getChildren("广东省").toArray(new Area[0]));

        provinceBox.setBounds(20, 275, 100, 20);
        cityBox.setBounds(130, 275, 80, 20);

        JButton setAreaBtn = new JButton("确定");
        setAreaBtn.addActionListener(e -> {
            Area selectedItem = (Area) cityBox.getSelectedItem();
            Config.regionCode = selectedItem.getValue();
            appendMsg("已选择地区:" + selectedItem.getName());
        });
        setAreaBtn.setBounds(220, 270, 80, 30);

        scrollPane.setBounds(10, 10, 460, 200);

        startBtn.setBounds(370, 230, 100, 30);

        setCookieBtn.setBounds(20, 230, 100, 30);
        setMemberBtn.setBounds(130, 230, 100, 30);
        refreshBtn.setBounds(240, 230, 120, 30);

        scroll.setBounds(480, 10, 380, 480);

        add(scrollPane);
        add(scroll);
        add(startBtn);
        add(setCookieBtn);
        add(setMemberBtn);
        add(refreshBtn);
        add(provinceBox);
        add(cityBox);
        add(setAreaBtn);
    }


    private void refreshVaccines() {
        try {
            //获取疫苗
            vaccines = service.getVaccines();
            //清除表格数据
            //通知模型更新
            ((DefaultTableModel) vaccinesTable.getModel()).getDataVector().clear();
            ((DefaultTableModel) vaccinesTable.getModel()).fireTableDataChanged();
            vaccinesTable.updateUI();//刷新表
            if (vaccines != null && !vaccines.isEmpty()) {
                for (Vaccine t : vaccines) {
                    String[] item = {t.getId().toString(), t.getVaccineName(), t.getClinicId().toString(), t.getCname(), t.getOrderTime(), t.isEnableOrder() ? "是" : "否"};
                    tableModel.addRow(item);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            appendMsg("未知错误");
        } catch (BusinessException e) {
            appendMsg("错误：" + e.getErrMsg() + "，errCode" + e.getCode());
        }
    }

    private void start() {
        if (StringUtils.isEmpty(Config.cookie)) {
            appendMsg("请配置Cookie!!!");
            return;
        }
        if (vaccinesTable.getSelectedRow() < 0) {
            appendMsg("请选择要抢购的疫苗");
            return;
        }

        int[] selectedRows = vaccinesTable.getSelectedRows();

        appendMsg("要抢的疫苗数量：" + selectedRows.length);

        for (int index : selectedRows) {
            Vaccine vaccine = vaccines.get(index);
            appendMsg("正在开始预约：" + "诊所：" + vaccine.getCname() + "的" + vaccine.getVaccineName());
            // 每个疫苗开2个线程获取日期并进行预约

            new Thread(() -> {
                try {
                    // 先获取可以预约的时间

                    List<SubDate> subDate = service.getSubDate(vaccine.getId(), vaccine.getClinicId(), "202102");
                    log.info("疫苗：{},可以预约的时间：{}", vaccine.getVaccineName(), subDate);
                    // 选择最近的时间进行预约
                    subDate.forEach(k -> {
                        if (k.isEnableSub()){
                            log.info("疫苗:{}，选取{}进行预约",vaccine.getVaccineName(),k.getDate());
                            appendMsg( "疫苗:" + vaccine.getCname() + "尝试预约：" + k.getDate());

                            //获取当天预约时间 和记数


                        }
                    });
                } catch (Exception e) {
                    log.error("预约疫苗，发生错误,{}",vaccine,e);
                }
            }).start();
        }

//        int selectedRow = vaccinesTable.getSelectedRow();
//        Integer id = vaccines.get(selectedRow).getId();
////        String startTime = vaccines.get(selectedRow).getStartTime();
//        String startTime = null;
//        new Thread(() -> {
//            try {
//                setCookieBtn.setEnabled(false);
//                startBtn.setEnabled(false);
//                setMemberBtn.setEnabled(false);
//                service.startSecKill(id, startTime, this);
//            } catch (ParseException | InterruptedException e) {
//                appendMsg("解析开始时间失败");
//                e.printStackTrace();
//            } finally {
//                setCookieBtn.setEnabled(true);
//                startBtn.setEnabled(true);
//                setMemberBtn.setEnabled(true);
//            }
//        }).start();

    }


    public void appendMsg(String message) {
        note.append(message);
        note.append("\r\n");
    }

    public void setStartBtnEnable() {
        startBtn.setEnabled(true);
        setCookieBtn.setEnabled(true);
        startBtn.setEnabled(true);
    }
}
