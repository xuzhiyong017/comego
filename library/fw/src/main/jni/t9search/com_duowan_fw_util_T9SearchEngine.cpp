#include "com_duowan_fw_util_T9SearchEngine.h"
#include "base.h"
#include "T9SearchEngine.h"
#include "PinyinTable.h"


#ifdef __cplusplus
extern "C" {
#endif


JNIEXPORT jint JNICALL Java_com_duowan_fw_util_T9SearchEngine_jniCreateInst
  (JNIEnv *env, jclass)
{
	CT9SearchEngine* pInst = new CT9SearchEngine;
	return reinterpret_cast<int>(pInst);
}

JNIEXPORT void JNICALL Java_com_duowan_fw_util_T9SearchEngine_jniDestroyInst
  (JNIEnv *env, jclass, jint h)
{
	CT9SearchEngine* pInst = reinterpret_cast<CT9SearchEngine*>(h);
	delete pInst;
}


JNIEXPORT void JNICALL Java_com_duowan_fw_util_T9SearchEngine_jniAddSentence
  (JNIEnv *env, jclass, jint h, jstring sentence, jint sentenceFlags, jint token)
{
	CT9SearchEngine* pInst = reinterpret_cast<CT9SearchEngine*>(h);
	if (pInst == NULL)
		return;

	//sentence
	const jchar* c_sentence = env->GetStringChars(sentence, NULL);
	if (c_sentence == NULL)
		return;

	std::wstring c_str_sentence(c_sentence, env->GetStringLength(sentence));
	env->ReleaseStringChars(sentence, c_sentence);

	//exec
	return pInst->addSentence(c_str_sentence.c_str(), sentenceFlags, token);
}


JNIEXPORT void JNICALL Java_com_duowan_fw_util_T9SearchEngine_jniRemoveAllSentences
  (JNIEnv *env, jclass, jint h)
{
	CT9SearchEngine* pInst = reinterpret_cast<CT9SearchEngine*>(h);
	if (pInst == NULL)
		return;

	return pInst->removeAllSentences();
}

JNIEXPORT void JNICALL Java_com_duowan_fw_util_T9SearchEngine_jniRemoveSentencesByToken
  (JNIEnv *env, jclass, jint h, jint token)
{
	CT9SearchEngine* pInst = reinterpret_cast<CT9SearchEngine*>(h);
	if (pInst == NULL)
		return;

	return pInst->removeSentencesByToken(token);
}


JNIEXPORT jintArray JNICALL Java_com_duowan_fw_util_T9SearchEngine_jniSearch
  (JNIEnv *env, jclass, jint h, jstring t9key, jint searchingFlags)
{
	CT9SearchEngine* pInst = reinterpret_cast<CT9SearchEngine*>(h);
	if (pInst == NULL)
		return NULL;

	//t9key
	const jchar* c_t9key = env->GetStringChars(t9key, NULL);
	if (c_t9key == NULL)
		return NULL;

	std::wstring c_str_t9key(c_t9key, env->GetStringLength(t9key));
	env->ReleaseStringChars(t9key, c_t9key);

	//exec
	CT9SearchEngine::TOKEN_LIST c_resultTokenList;
	pInst->search(c_str_t9key.c_str(), searchingFlags, &c_resultTokenList);

	//result
	jintArray j_resultTokenArray = env->NewIntArray(c_resultTokenList.size());
	if (j_resultTokenArray == NULL)
		return NULL;

	std::basic_string<CT9SearchEngine::TOKEN_TYPE> c_resultTokenArray(c_resultTokenList.begin(), c_resultTokenList.end());
	env->SetIntArrayRegion(j_resultTokenArray, 0, c_resultTokenArray.size(), c_resultTokenArray.data());

	return j_resultTokenArray;
}



JNIEXPORT jstring JNICALL Java_com_duowan_fw_util_T9SearchEngine_getPinyinLeadCharsOfHanzi
  (JNIEnv *env, jclass, jstring hanzi)
{
	const jchar* c_hanzi = env->GetStringChars(hanzi, NULL);
	if (c_hanzi == NULL)
		return NULL;

	size_t hanzi_len = env->GetStringLength(hanzi);


	std::string pinyinLeadChars;
	pinyinLeadChars.reserve(hanzi_len);

	for (size_t i = 0; i < hanzi_len; ++i)
	{
		char lc = CPinyinTable::getPinyinLeadCharOf(c_hanzi[i]);
		if (lc == 0)
			lc = ' ';
		pinyinLeadChars.append(1, lc);
	}


	env->ReleaseStringChars(hanzi, c_hanzi);

	//result
	jstring j_result = env->NewStringUTF(pinyinLeadChars.c_str());
	if (j_result == NULL)
		return NULL;

	return j_result;
}


JNIEXPORT jstring JNICALL Java_com_duowan_fw_util_T9SearchEngine_getPinyinListOfHanziChar
  (JNIEnv *env, jclass, jchar hanziChar)
{
	CPinyinTable::LPCSTR_LIST pinyinList;
	CPinyinTable::getPinyinListOf(hanziChar, &pinyinList);

	jstring j_result;
	if (pinyinList.empty())
	{
		j_result = env->NewStringUTF("");
	}
	else if (pinyinList.size() == 1)
	{
		j_result = env->NewStringUTF(pinyinList.front());
	}
	else
	{
		std::string pinyinListStr;
		pinyinListStr.reserve(20);

		//first 
		CPinyinTable::LPCSTR_LIST::iterator it = pinyinList.begin(); 
		pinyinListStr.append(*it);
		//followings 
		for (++it; it != pinyinList.end(); ++it)
		{
			pinyinListStr.append(1, ',');
			pinyinListStr.append(*it);
		}

		j_result = env->NewStringUTF(pinyinListStr.c_str());
	}

	if (j_result == NULL)
		return NULL;

	return j_result;
}


JNIEXPORT jstring JNICALL Java_com_duowan_fw_util_T9SearchEngine_getPinyinSortKeyOfHanziString
  (JNIEnv *env, jclass, jstring hanziString)
{
	//example: "王a二小B" ==> "wang_王` a`er_二`xiao_小` B`"

	const jchar* c_hanziString = env->GetStringChars(hanziString, NULL);
	if (c_hanziString == NULL)
		return NULL;

	size_t hanziString_len = env->GetStringLength(hanziString);


	std::wstring sortKey;
	sortKey.reserve(hanziString_len * 4);

	for (size_t i = 0; i < hanziString_len; ++i)
	{
		const WCHAR hanzi = c_hanziString[i];
		LPCSTR pinyin = CPinyinTable::getFirstPinyinOf(hanzi);

		if (pinyin != NULL)
		{
			sortKey.append(pinyin, pinyin + strlen(pinyin));
			sortKey.append(1, L'_');
			sortKey.append(1, hanzi);
		}
		else
		{
			sortKey.append(1, L' ');
			sortKey.append(1, hanzi);
		}

		sortKey.append(1, L'`');
	}


	env->ReleaseStringChars(hanziString, c_hanziString);


	jstring j_result = env->NewString(sortKey.c_str(), sortKey.length());
	if (j_result == NULL)
		return NULL;

	return j_result;
}




#ifdef __cplusplus
}
#endif
