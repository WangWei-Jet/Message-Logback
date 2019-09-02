package com.whty.zdrj.libset.msg;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.whty.zdrj.libset.msg.utils.CustomLogger;

public class MsgTest {
	private static CustomLogger logger = CustomLogger.getLogger(MsgTest.class);

	public static void main(String[] args) {
		/*IDataHandler dataHandler = ProtocolParseFactory.getMSG8583DataParserHandler(KeyFactory.MSG_8583_KEY);
		try {
			PatternTables.loadPattern(KeyFactory.MSG_8583_KEY,
					MsgTest.class.getClassLoader().getResourceAsStream("template/iso8583-common.xml"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		dataHandler.setLogPrint(true);
		dataHandler.parseStandard8583MSG(new StringBuffer(
				"0200702404c020c09811166221268888888888000000000000000001000031622102200012336221268888888888563a0491f85118428f3930333631373438393233303131373533393931363935313536d68011b8b7c73fae061000000000000000122200000200063238323331314235"));
		// logger.debug(resultList.toString());
		*/
		testFrame();
	}

	private static MessageParseResult testStandardMessageParse(String data) {
		IDataHandler dataHandler = ProtocolParseFactory.getMSG8583DataParserHandler(KeyFactory.MSG_8583_KEY);
		try {
			PatternTables.loadPattern(KeyFactory.MSG_8583_KEY,
					MsgTest.class.getClassLoader().getResourceAsStream("template/iso8583-common.xml"));

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		dataHandler.setLogPrint(true);
		// return dataHandler.parseStandard8583MSG(new StringBuffer(
		// "08202000000000c0000c180000323135353535353531333239353435363135313731313700324001882842e4da705696e686ff98dad50016c59100001f2800180019600735084e45"));

		return dataHandler.parseStandard8583MSG(new StringBuffer(data));
	}

	private static void testFrame() {
		// 创建窗体对象
		final Frame f = new Frame("8583报文数据解析");
		Dimension s = Toolkit.getDefaultToolkit().getScreenSize();
		// System.out.println("长:" + s.height + "\t宽:" + s.width);
		logger.debug("屏幕信息:长:" + s.height + "\t宽:" + s.width);
		// 设置属性
		f.setBounds(s.width / 6, s.height / 6, s.width / 3 * 2, s.height / 3 * 2);
		f.setResizable(false);
		// // 设置布局为流式布局
		f.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));
		// f.setLayout(new GridLayout(3, 1));

		// 创建文本
		final JLabel lable = new JLabel("数据:", JLabel.CENTER);

		Panel titlePanel = new Panel();
		titlePanel.add(lable);
		// titlePanel.setSize(500, 100);
		// 创建输入对象
		final TextField textField = new TextField(100);
		// 创建按钮对象
		JButton bu = new JButton("解析");
		// bu.setSize(20, 10);
		// table
		// Object[][] data = { { "082520", "张平", "03A01", 80, 90, 95, (80 + 90 +
		// 95) },
		// { "082521", "李红", "03A02", 88, 90, 90, (88 + 90 + 90) } };
		// String[] rowName = { "TAG", "LEN", "VALUE" };
		Panel dataPanel = new Panel();
		// dataPanel.setSize(500, 100);
		dataPanel.add(textField);
		dataPanel.add(bu);

		final JTable resultTable = new JTable();
		// resultTable.setSize(1500, 100);
		// resultTable.setPreferredSize(new Dimension(1500, 300));
		resultTable.setFont(new Font("Menu.font", Font.PLAIN, 15));
		clearTable(resultTable);

		// 把按钮添加到窗体
		f.add(titlePanel);
		// f.add(textField);
		// f.add(bu);
		f.add(dataPanel);
		JScrollPane resultPanel = new JScrollPane(resultTable);
		// resultPanel.setSize(1500, 100);
		resultPanel.setPreferredSize(new Dimension(800, 500));
		f.add(resultPanel);

		// 设置窗体可以关闭
		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);// 退出JVM
			}
		});

		bu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 清空结果区域
				clearTable(resultTable);

				String data = textField.getText();
				MessageParseResult messageParseResult = testStandardMessageParse(data);
				List<?> result = messageParseResult.getResultList();
				// TLVParseResult result = testCommonTlvParser_Parse(data);
				if (result != null) {
					if (result.size() > 0) {
						setUserTableData(resultTable, result);
					} else {
						// 解析出错
						// String description = "解析失败";
						String description = MessageParseResult.ResultCodeMatcher
								.getDescription(messageParseResult.getResultCode());
						logger.debug("description:" + description);
						JOptionPane.showMessageDialog(f, description);
					}
					if (!messageParseResult.isSuccess()) {
						String description = MessageParseResult.ResultCodeMatcher
								.getDescription(messageParseResult.getResultCode());
						logger.debug("description:" + description);
						JOptionPane.showMessageDialog(f, description);
					}
				} else {
					// 解析失败
					JOptionPane.showMessageDialog(f, "解析失败");
				}
			}
		});

		// 窗体显示
		f.setVisible(true);
	}

	private static void setUserTableData(JTable table, List<?> fieldList) {
		// 设置usertable数据；
		((DefaultTableModel) table.getModel()).getDataVector().clear();
		// 清空table的数据，重新写入；
		Vector<String> row = null;
		Vector<Object> targetData = new Vector<Object>();
		for (int i = 0; i < fieldList.size(); i++) {
			ParseElement element = (ParseElement) fieldList.get(i);
			row = new Vector<String>();
			row.add(element.getId() == null ? element.getTag().toUpperCase(Locale.getDefault())
					: element.getId().toUpperCase(Locale.getDefault()));
			row.add(element.getComments().toUpperCase(Locale.getDefault()));
			row.add(element.getLen().toUpperCase(Locale.getDefault()));
			row.add(element.getValue() == null ? null : element.getValue().toUpperCase(Locale.getDefault()));
			row.add(element.getCode().toUpperCase(Locale.getDefault()));
			targetData.add(row); // 添加数据到userdata中
		}
		Vector<String> title = new Vector<String>();
		title.add("域");
		title.add("说明");
		title.add("长度");
		title.add("值");
		title.add("编码");
		DefaultTableModel dtmView = new DefaultTableModel(targetData, title); // 添加userdata到JTable中
		table.setModel(dtmView);
		table.setRowHeight(25);
		// for (int i = 0; i < table.getRowCount(); i++) {
		// table.setRowHeight(30);
		// }
	}

	private static void clearTable(JTable table) {
		((DefaultTableModel) table.getModel()).getDataVector().clear();
		Vector<String> title = new Vector<String>();
		title.add("域");
		title.add("说明");
		title.add("长度");
		title.add("值");
		title.add("编码");
		DefaultTableModel dtmView = new DefaultTableModel(null, title); // 添加userdata到JTable中
		table.setModel(dtmView);
	}
}
