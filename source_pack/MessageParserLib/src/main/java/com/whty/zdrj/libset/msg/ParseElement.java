package com.whty.zdrj.libset.msg;

import java.util.ArrayList;
import java.util.List;

public class ParseElement {

	private String id;
	private String tag;
	private String len;
	private String value;
	private String lllvar;
	private String lllcode;
	private String code;
	private String comments;
	private String targetClass;
	private String parseMethod;
	private String buildMethod;
	private String parseMethodParam;
	private String buildMethodParam;
	private String valueLength;

	public String getParseMethodParam() {
		return parseMethodParam;
	}

	public void setParseMethodParam(String parseMethodParam) {
		this.parseMethodParam = parseMethodParam;
	}

	public String getBuildMethodParam() {
		return buildMethodParam;
	}

	public void setBuildMethodParam(String buildMethodParam) {
		this.buildMethodParam = buildMethodParam;
	}

	private List<ParseElement> children = new ArrayList<ParseElement>();

	public String getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(String targetClass) {
		this.targetClass = targetClass;
	}

	public String getParseMethod() {
		return parseMethod;
	}

	public void setParseMethod(String parseMethod) {
		this.parseMethod = parseMethod;
	}

	public String getBuildMethod() {
		return buildMethod;
	}

	public void setBuildMethod(String buildMethod) {
		this.buildMethod = buildMethod;
	}

	public List<ParseElement> getChildren() {
		return children;
	}

	public void setChildren(List<ParseElement> children) {
		this.children = children;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLllvar() {
		return lllvar;
	}

	public void setLllvar(String lllvar) {
		this.lllvar = lllvar;
	}

	public String getLllcode() {
		return lllcode;
	}

	public void setLllcode(String lllcode) {
		this.lllcode = lllcode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getLen() {
		return len;
	}

	public void setLen(String len) {
		this.len = len;
	}

	public String getValueLength() {
		return valueLength;
	}

	public void setValueLength(String valueLength) {
		String lllvar = this.getLllvar();
		String lllcode = this.getLllcode();
		String code = this.getCode();
		if (lllvar != null) {
			int varLen = 0;
			try {
				varLen = (Integer.valueOf(lllvar) + 1) / 2 * 2;
			} catch (Exception e) {
				return;
			}
			if (lllcode != null) {
				code = lllcode;
			}
			if (code != null) {
				while (valueLength.length() < varLen) {
					valueLength = "0" + valueLength;
				}
			}
		}
		this.valueLength = valueLength;
	}

	public ParseElement clone() {
		ParseElement clone = new ParseElement();
		clone.setCode(code);
		clone.setComments(comments);
		clone.setId(id);
		clone.setLen(len);
		clone.setLllvar(lllvar);
		clone.setLllcode(lllcode);
		clone.setTag(tag);
		clone.setValue(value);
		clone.setBuildMethod(buildMethod);
		clone.setParseMethod(parseMethod);
		clone.setChildren(children);
		clone.setTargetClass(targetClass);
		clone.setParseMethodParam(parseMethodParam);
		clone.setBuildMethodParam(buildMethodParam);
		if (valueLength != null)
			clone.setValueLength(valueLength);
		return clone;
	}

	@Override
	public String toString() {
		return "ParseElement [id=" + id + ", tag=" + tag + ", len=" + len + ", value=" + value + ", lllvar=" + lllvar
				+ ", code=" + code + ", comments=" + comments + ", targetClass=" + targetClass + ", parseMethod="
				+ parseMethod + ", buildMethod=" + buildMethod + ", parseMethodParam=" + parseMethodParam
				+ ", buildMethodParam=" + buildMethodParam + ", children=" + children + "]";
	}

}
