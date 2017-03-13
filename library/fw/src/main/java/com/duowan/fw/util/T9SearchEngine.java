package com.duowan.fw.util;

public class T9SearchEngine {
	static {
		System.loadLibrary("t9search");
	}

	private int m_handle = 0;

	public boolean create() {
		if (m_handle != 0)
			return false;

		m_handle = jniCreateInst();
		return (m_handle != 0);
	}
	public void close() {
		if (m_handle != 0) {
			jniDestroyInst(m_handle);
			m_handle = 0;
		}
	}

	//sentenceFlags: nick/remark时使�?x0400，yyid时使�?x0600
	public void addSentence(String sentence, int sentenceFlags, int token) {
		jniAddSentence(m_handle, sentence, sentenceFlags, token);
	}
	
	public void removeAllSentences() {
		jniRemoveAllSentences(m_handle);
	}
	public void removeSentencesByToken(int token) {
		jniRemoveSentencesByToken(m_handle, token);
	}

	//搜索：searchingFlags使用0x03
	public int[] search(String t9key, int searchingFlags) {
		return jniSearch(m_handle, t9key, searchingFlags);
	}

	//assist: get pinyin lead char of hanzi//////////////////////////////////////////////////////////////
	public static native String getPinyinLeadCharsOfHanzi(String hanzi); //如果没有拼音，则置为空格字符
	//assist: get pinyin list of hanzi char//////////////////////////////////////////////////////////////
	public static native String getPinyinListOfHanziChar(char hanziChar);  //逗号分隔多音字的各拼�?
	//assist: get pinyin sort-key of hanzi string//////////////////////////////////////////////////////////////
	//example: "王a二小B" ==> "wang_王` a`er_二`xiao_小` B`"
	public static native String getPinyinSortKeyOfHanziString(String hanziString); //对于多音字，仅使用第�?��拼音


	////////////////////////////////////////////////////////////////
	
	protected void finalize() {
		close();
	}

	////////////////////////////////////////////////////////////////
	//jni 
	private static native int jniCreateInst();
	private static native void jniDestroyInst(int h);

	private static native void jniAddSentence(int h, String sentence, int sentenceFlags, int token);

	private static native void jniRemoveAllSentences(int h);
	private static native void jniRemoveSentencesByToken(int h, int token);

	private static native int[] jniSearch(int h, String t9key, int searchingFlags);
}
