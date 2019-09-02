/**
 * 
 */
package com.whty.zdrj.libset.data.pattern.reponsibilitychain;

import java.io.InputStream;

import com.whty.zdrj.libset.msg.IDataHandler;
import com.whty.zdrj.libset.msg.ProtocolParseFactory;

/**
 * <p>
 * Title:ResponsibilityChainDemo
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Date:2018年3月14日 上午10:55:24
 * </p>
 * <p>
 * 
 * @author wangwei01
 *         </p>
 */
public class ResponsibilityChainDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String signMsg = "08000020000000c00012000016393033363137343839323330313137353339393136393500110000000100400003303120";

		String signResMsg = "0810003800010ac000140000861328050418080009450031383034313831333238303530303332393031323430333239303132343030303030303031001100000001005001045065A84226DD429258F81675C5E9D77239F98870683517B8F96E2D87B5BCB3386662CED50EC5CD14680EC351F93428F39A283A3F42962FC81050CC3D25F27069D7AF252B6AD444FCBBA9CC0E8B95DF0C85DBCD9526931E6A96C040C794988D9BCC57E773B05AD52B";

		String consumeMsg = "0200703C06C020C09A11196217857600008050804000000000000000000100010516124302092308071000010012376217857600008050804D23082201000082300039323333363738373834373130303035333939303030323135367D6A7408DFE2E9E5260000000000000001459F26089C0A057796D091E39F2701809F1013070F0103A00000010A010000005959BAF31C349F37042AE9F9B49F36020FCD950500000000009A031802099C01009F02060000000000015F2A02015682027C009F1A0201569F03060000000000009F330390C8C09F34030000009F3501229F1E0831323334353637388408A0000003330101019F090200209F41040000000200122200000100064433374345464536";

		InputStream is = ResponsibilityChainDemo.class.getClassLoader()
				.getResourceAsStream("template/8583-standard-brief.json");

		IDataHandler dataHandler = ProtocolParseFactory.getMSG8583ParserByJsonModel(is, true);
		dataHandler.parseStandard8583MSG(new StringBuffer(signMsg));
		dataHandler.parseStandard8583MSG(new StringBuffer(signResMsg));
		dataHandler.parseStandard8583MSG(new StringBuffer(consumeMsg));
		dataHandler.parseStandard8583MSG(new StringBuffer(signMsg));

		// FieldChainManager.getSingletonChainManager().loadDataModelFile(is);
		//
		// AbstructHandler msgHeaderParseHandler = new FieldParseHandler(
		// FieldChainManager.getSingletonChainManager().getDataFormatList().get("MSGTYPE"));
		// AbstructHandler bitmapParseHandler = new FieldParseHandler(
		// FieldChainManager.getSingletonChainManager().getDataFormatList().get("BITMAP"));
		//
		// FieldChainManager.getSingletonChainManager().addHander(msgHeaderParseHandler);
		// FieldChainManager.getSingletonChainManager().addHander(bitmapParseHandler);
		//
		// FieldChainManager.getSingletonChainManager().startDataParseChain(
		// "0200703C06C020C09A11196217857600008050804000000000000000000100010516124302092308071000010012376217857600008050804D23082201000082300039323333363738373834373130303035333939303030323135367D6A7408DFE2E9E5260000000000000001459F26089C0A057796D091E39F2701809F1013070F0103A00000010A010000005959BAF31C349F37042AE9F9B49F36020FCD950500000000009A031802099C01009F02060000000000015F2A02015682027C009F1A0201569F03060000000000009F330390C8C09F34030000009F3501229F1E0831323334353637388408A0000003330101019F090200209F41040000000200122200000100064433374345464536");
		// MessageParseResult dataParseResult =
		// FieldChainManager.getSingletonChainManager().getDataParseResult();
		// System.out.println("success:" + dataParseResult.isSuccess());
		// System.out.println("remarks:" + dataParseResult.getResultRemarks());
		// @SuppressWarnings("unchecked")
		// List<SingleFieldParseResult> singleParseResultList =
		// (List<SingleFieldParseResult>) dataParseResult
		// .getResultList();
		// if (singleParseResultList != null && !singleParseResultList.isEmpty()) {
		// for (SingleFieldParseResult result : singleParseResultList) {
		// System.out.println(result.getSingleFieldData().getDataFormat().getComments());
		// System.out.println(result.isParseSuccess());
		// System.out.println(result.getRemarks());
		// System.out.println(result.getSingleFieldData().getOrignalLenStr());
		// System.out.println(result.getSingleFieldData().getParsedLen());
		// System.out.println(result.getSingleFieldData().getParsedData());
		// System.out.println(result.getSingleFieldData().getOriginalData());
		// }
		// }

		// Map<String, Object> dataMap = new HashMap<>();
		// dataMap.put("MSGTYPE", "0200");
		// // dataMap.put("BITMAP", "703C06C020C09A11");
		// dataMap.put("2", "6217857600008050804");
		// dataMap.put("3", "000000");
		// dataMap.put("4", "000000000001");
		// dataMap.put("11", "000105");
		// dataMap.put("12", "161243");
		// dataMap.put("13", "0209");
		// dataMap.put("14", "2308");
		// dataMap.put("22", "071");
		// dataMap.put("23", "001");
		// dataMap.put("25", "00");
		// dataMap.put("26", "12");
		// dataMap.put("35", "6217857600008050804D23082201000082300");
		// dataMap.put("41", "92336787");
		// dataMap.put("42", "847100053990002");
		// dataMap.put("49", "156");
		// dataMap.put("52", "7D6A7408DFE2E9E5");
		// dataMap.put("53", "2600000000000000");
		// dataMap.put("55",
		// "9F26089C0A057796D091E39F2701809F1013070F0103A00000010A010000005959BAF31C349F37042AE9F9B49F36020FCD950500000000009A031802099C01009F02060000000000015F2A02015682027C009F1A0201569F03060000000000009F330390C8C09F34030000009F3501229F1E0831323334353637388408A0000003330101019F090200209F410400000002");
		// dataMap.put("60", "220000010006");
		// dataMap.put("64", "4433374345464536");
		//
		// String data8583 = dataHandler.buildStandard8583MSG(dataMap);
		// System.out.println(data8583);
	}

}
