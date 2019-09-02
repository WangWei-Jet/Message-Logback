/**
 * 
 */
package com.whty.zdrj.libset.data.parse;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Title:DataParseResult
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Date:2018年4月9日 下午1:34:01
 * </p>
 * <p>
 * 
 * @author wangwei01
 *         </p>
 */
public class DataParseResult {
	private List<SingleFieldParseResult> signleFieldParseResultList = new ArrayList<>();

	private boolean isSuccess;

	private String remarks;

	public List<SingleFieldParseResult> getSignleFieldParseResultList() {
		return signleFieldParseResultList;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
