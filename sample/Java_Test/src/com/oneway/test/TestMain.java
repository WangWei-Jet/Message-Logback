package com.oneway.test;

import java.awt.Dimension;
import java.awt.FlowLayout;
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

import com.oneway.libset.ParserFactory;
import com.oneway.libset.msg.IDataHandler;
import com.oneway.libset.msg.KeyFactory;
import com.oneway.libset.msg.MsgTest;
import com.oneway.libset.msg.PatternTables;
import com.oneway.libset.msg.ProtocolParseFactory;
import com.oneway.libset.tlv.TLVParseResult;
import com.oneway.libset.tlv.TlvParser;
import com.oneway.libset.tlv.entity.TLV;
import com.oneway.utils.CustomLogger;

public class TestMain {
	private static CustomLogger logger = CustomLogger.getLogger(TestMain.class.getSimpleName());
	// private static String testData =
	// "FF90000401102101FF900206000000008806FF900303000025FF90080102FF9009011FFF9011083030313231303431FF90120F303031323130333830313031323334FF901B03313536FF902203000001FF9001084218690009159094FF9006021309FF900B0112FF900D124218690009159094D13D1C9EE73AE229800FFF900E28994218690009159094D1561560000000000000003000000034343413090DDDBBCCDC146A7EE03500FF901C08FB7CB09192533670FF901D012FFF902302000FFF9024016FFF9025010FFF8015011FFF8017016FFF9026011FFF9161023032FF916218303030303234303235313631373730303130303030303031FF916306313539303934FF9164084138453542333336FF9165083430323434332020FF9145026000ff9145820123FF90000401102101FF900206000000008806FF900303000025FF90080102FF9009011FFF9011083030313231303431FF90120F303031323130333830313031323334FF901B03313536FF902203000001FF9001084218690009159094FF9006021309FF900B0112FF900D124218690009159094D13D1C9EE73AE229800FFF900E28994218690009159094D1561560000000000000003000000034343413090DDDBBCCDC146A7EE03500FF901C08FB7CB09192533670FF901D012FFF902302000FFF9024016FFF9025010FFF8015011FFF8017016FFF9026011FFF9161023032FF916218303030303234303235313631373730303130303030303031FF916306313539303934FF9164084138453542333336FF9165083430323434332020FF9145026000ff91458180FF90000401102101FF900206000000008806FF900303000025FF90080102FF9009011FFF9011083030313231303431FF90120F303031323130333830313031323334FF901B03313536FF902203000001FF9001084218690009159094FF9006021309FF900B0112FF900D124218690009159094D13D1C9EE73AE229800FFF900E9f2608baa102f98ccf955c9f2701809f101307010103a00002010a01000007197766b5e5e39f3704290000009f3602000d950500200400009a031706309c01009f02060000000000055f2a02015682027c009f1a0201569f03060000000000009f3303e0e9c09f34030203009f3501229f1e0830303132313034308408a0000003330101029f090200209f41040000004f";

	public static void main(String[] args) {
		// System.out.println("Hello World!");
		// int lenLong = Integer.parseInt("010203", 16);
		// System.out.println(lenLong + "");
		// testStandardTlvParser_Parse();
		// commonTest();
		testFrame();
		// testMessage();
	}

	private static void testMessage() {
		IDataHandler dataHandler = ProtocolParseFactory.getMSG8583DataParserHandler(KeyFactory.MSG_8583_KEY);
		try {
			PatternTables.loadPattern(KeyFactory.MSG_8583_KEY,
					MsgTest.class.getClassLoader().getResourceAsStream("template/iso8583-common.xml"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		dataHandler.setLogPrint(true);
		dataHandler.parseStandard8583MSG(new StringBuffer(
				"08202000000000c0000c180000323135353535353531333239353435363135313731313700324001882842e4da705696e686ff98dad50016c59100001f2800180019600735084e45"));
	}

	private static TLVParseResult testStandardTlvParser_Parse(String data) {
		TlvParser tlvParser = ParserFactory.getStandardTlvParser();
		long start = System.currentTimeMillis();
		// testData += "154154";
		TLVParseResult result = tlvParser.parse(data);
		long end = System.currentTimeMillis();
		if (result != null) {
			logger.debug("解析结果:" + result.getParseResult() + "\n耗时:" + (end - start) + " ms");
		}
		return result;
	}

	// private static void commonTest() {
	// System.out.println((0xff - 0x0f) >> 4);
	// }

	private static void testFrame() {
		// 创建窗体对象
		final Frame f = new Frame("TLV数据解析");
		Dimension s = Toolkit.getDefaultToolkit().getScreenSize();
		// System.out.println("长:" + s.height + "\t宽:" + s.width);
		logger.debug("屏幕信息:长:" + s.height + "\t宽:" + s.width);
		// 设置属性
		f.setBounds(s.width / 4, s.height / 4, s.width / 2, s.height / 2);
		f.setResizable(false);
		// // 设置布局为流式布局
		f.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));
		// f.setLayout(new GridLayout(3, 1));

		// 创建文本
		final JLabel lable = new JLabel("TLV格式数据:", JLabel.CENTER);

		Panel titlePanel = new Panel();
		titlePanel.add(lable);
		// titlePanel.setSize(500, 100);
		// 创建输入对象
		final TextField textField = new TextField(50);
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
		clearTable(resultTable);

		// 把按钮添加到窗体
		f.add(titlePanel);
		// f.add(textField);
		// f.add(bu);
		f.add(dataPanel);
		JScrollPane resultPanel = new JScrollPane(resultTable);
		// resultPanel.setSize(500, 100);
		f.add(resultPanel);

		// 设置窗体可以关闭
		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);// 退出JVM
			}
		});

		bu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 清空结果区域
				clearTable(resultTable);

				String data = textField.getText();
				TLVParseResult result = testStandardTlvParser_Parse(data);
				if (result != null && result.getTlvList() != null && result.getTlvList().size() > 0) {
					setUserTableData(resultTable, result.getTlvList());
					if (!result.getParseResult()) {
						// 解析出错
						// 打成jar包的时候不能使用此种方式加载配置文件，会出错
						// String description =
						// TLVParseResult.ResultCodeMatcher.getDescription(result.getResultCode(),
						// this.getClass().getClassLoader().getResource("ResultModel.xml").getPath());
						// 打成jar包的时候采用此种方式加载配置文件
						String description = TLVParseResult.ResultCodeMatcher.getDescription(result.getResultCode());
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

	private static void setUserTableData(JTable table, List<TLV> tlvList) {
		// 设置usertable数据；
		((DefaultTableModel) table.getModel()).getDataVector().clear();
		// 清空table的数据，重新写入；
		Vector<String> row = null;
		Vector<Object> targetData = new Vector<Object>();
		for (int i = 0; i < tlvList.size(); i++) {
			row = new Vector<String>();
			row.add(tlvList.get(i).getTagStr().toUpperCase(Locale.getDefault()));
			row.add(tlvList.get(i).getLengthStr().toUpperCase(Locale.getDefault()));
			row.add(tlvList.get(i).getValueStr() == null ? null
					: tlvList.get(i).getValueStr().toUpperCase(Locale.getDefault()));
			targetData.add(row); // 添加数据到userdata中
		}
		Vector<String> title = new Vector<String>();
		title.add("TAG");
		title.add("LEN");
		title.add("VALUE");

		DefaultTableModel dtmView = new DefaultTableModel(targetData, title); // 添加userdata到JTable中
		table.setModel(dtmView);
	}

	private static void clearTable(JTable table) {
		((DefaultTableModel) table.getModel()).getDataVector().clear();
		Vector<String> title = new Vector<String>();
		title.add("TAG");
		title.add("LEN");
		title.add("VALUE");
		DefaultTableModel dtmView = new DefaultTableModel(null, title); // 添加userdata到JTable中
		table.setModel(dtmView);
	}
}
