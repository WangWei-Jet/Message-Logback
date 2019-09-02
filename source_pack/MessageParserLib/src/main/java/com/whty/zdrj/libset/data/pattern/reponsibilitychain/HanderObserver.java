/**
 * 
 */
package com.whty.zdrj.libset.data.pattern.reponsibilitychain;

import com.whty.zdrj.libset.data.model.DataFormat;
import com.whty.zdrj.libset.data.parse.SingleFieldParseResult;

/**
 * <p>
 * Title:HanderObserver
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Date:2018年3月14日 上午11:27:13
 * </p>
 * <p>
 * 
 * @author wangwei01
 *         </p>
 */
public abstract class HanderObserver {

	abstract void singleFinishNotified(SingleFieldParseResult singleFieldParseResult);

	abstract void finishNotified(boolean result, String remarks);

	abstract void addNextHandlerNotified(String dataFormatTag);

	abstract DataFormat getDataFormat(String fieldTag);

}
