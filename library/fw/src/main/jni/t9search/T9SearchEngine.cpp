#include "base.h"
#include "T9SearchEngine.h"
#include "PinyinTable.h"


CT9SearchEngine::CT9SearchEngine(void) 
{
}

CT9SearchEngine::~CT9SearchEngine(void)
{
}


void CT9SearchEngine::addSentence(LPCWSTR sentence, unsigned sentenceFlags, const TOKEN_TYPE& token)
{
	_comptr<_SENTENCE_ITEM> spSentenceItem(new _SENTENCE_ITEM);
	_makeSentenceItem(sentence, sentenceFlags, token, spSentenceItem.get());

	m_sentenceList.push_back(spSentenceItem);

	return;
}


void CT9SearchEngine::removeAllSentences()
{
	m_sentenceList.clear();
	return;
}

void CT9SearchEngine::removeSentencesByToken(const TOKEN_TYPE& token)
{
	_removeSentencesByTokenFrom(&m_sentenceList, token);
}


void CT9SearchEngine::search(LPCWSTR t9key, unsigned searchingFlags, TOKEN_LIST* pResult)
{
	_execSearch(t9key, searchingFlags, NULL, NULL, pResult);
	return;
}

void CT9SearchEngine::search(LPCWSTR t9key, unsigned searchingFlags, const TOKEN_LIST* pWhiteTokenList, const TOKEN_LIST* pBlackTokenList, TOKEN_LIST* pResult)
{
	_TOKEN_SET whiteTokenSet, blackTokenSet;
	if (pWhiteTokenList != NULL)
		whiteTokenSet.insert(pWhiteTokenList->begin(), pWhiteTokenList->end());
	if (pBlackTokenList != NULL)
		blackTokenSet.insert(pBlackTokenList->begin(), pBlackTokenList->end());

	_execSearch(t9key, searchingFlags, 
		pWhiteTokenList != NULL ? &whiteTokenSet : NULL, 
		pBlackTokenList != NULL ? &blackTokenSet : NULL, 
		pResult);
	return;
}


void CT9SearchEngine::_makeSentenceItem(LPCWSTR sentence, unsigned sentenceFlags, const TOKEN_TYPE& token, _SENTENCE_ITEM* pSentenceItem)
{
	pSentenceItem->ccSentence = sentence;
	pSentenceItem->sentenceFlags = sentenceFlags;
	pSentenceItem->theToken = token;
	pSentenceItem->firstCharMatchFlagsAbc.reset();
	pSentenceItem->firstCharMatchFlagsT9.reset();
	pSentenceItem->isAbcAndT9Same = true;

	_adjustSentence(&pSentenceItem->ccSentence);

	if ((sentenceFlags & (SENTENCE_SUPPRESS_ABC_FLAG|SENTENCE_SUPPRESS_T9_FLAG)) != (SENTENCE_SUPPRESS_ABC_FLAG|SENTENCE_SUPPRESS_T9_FLAG))
	{
		size_t sentencePos = 0;
		while (sentencePos != (size_t)-1)
		{
			std::wstring word;
			_enumWordType wordType;
			sentencePos = _takeFirstWord(sentence, sentenceFlags, sentencePos, &word, &wordType);

			if (! word.empty())
			{
				_comptr<_WORD_ITEM> spWordItem(new _WORD_ITEM);
				_makeWordItem(word.c_str(), word.length(), wordType, spWordItem.get());

				ASSERT(! spWordItem->abcWordList.empty());
				if (! spWordItem->abcWordList.empty())
				{
					pSentenceItem->wordList.push_back(spWordItem);
					pSentenceItem->firstCharMatchFlagsAbc.orAssign(spWordItem->firstCharMatchFlagsAbc);
					pSentenceItem->firstCharMatchFlagsT9.orAssign(spWordItem->firstCharMatchFlagsT9);
					if (wordType != _wordtype_number)
						pSentenceItem->isAbcAndT9Same = false;
				}
			}
		}
	}

	return;
}

void CT9SearchEngine::_adjustSentence(std::wstring* pSentence)
{
	size_t sentenceLen = pSentence->length();
	for (size_t i = 0; i < sentenceLen; ++i)
	{
		WCHAR ch = (*pSentence)[i];
		if (ch >= 'A' && ch <= 'Z')
			(*pSentence)[i] = 'a' + (ch - 'A');
	}

}

void CT9SearchEngine::_makeWordItem(LPCWSTR word, size_t wordLen, _enumWordType wordType, _WORD_ITEM* pWordItem)
{
#ifdef _DEBUG
	pWordItem->ccWord = word;
	pWordItem->wordType = wordType;
#endif
	pWordItem->firstCharMatchFlagsAbc.reset();
	pWordItem->firstCharMatchFlagsT9.reset();

	_transWordToAbc(word, wordLen, wordType, &pWordItem->abcWordList);

	for (_ANSI_STRING_LIST::const_iterator it = pWordItem->abcWordList.begin(); it != pWordItem->abcWordList.end(); ++it)
	{
		ASSERT(! (*it).empty());
		char firstAbcChar = (*it)[0];
		pWordItem->firstCharMatchFlagsAbc.setBit(_calcAbcCharFlagPos(firstAbcChar));
		pWordItem->firstCharMatchFlagsT9.setBit(_calcAbcCharFlagPos(_convAbcCharToT9(firstAbcChar)));
	}

	return;
}

size_t CT9SearchEngine::_takeFirstWord(LPCWSTR sentence, unsigned sentenceFlags, size_t sentencePos, std::wstring* pWord, _enumWordType* pWordType)
{
	//skip space chars
	_enumWordType firstCharType;
	while (true)
	{
		WCHAR ch = sentence[sentencePos];
		if (ch == 0)
			return (size_t)-1;

		firstCharType = _judgeWordTypeByChar(ch);
		if (firstCharType != _wordtype_dummyOther)
			break;

		++ sentencePos;
	}

	//find word end pos
	size_t wordEndPos;
	if ((sentenceFlags & SENTENCE_ISOLATE_CHAR_FLAG) != 0)
	{
		wordEndPos = sentencePos + 1;
	}
	else
	{
		switch (firstCharType)
		{
		case _wordtype_number:
			wordEndPos = _doFindFirstWordEndPos_Number(sentence, sentenceFlags, sentencePos);
			break;
		case _wordtype_alpha:
			wordEndPos = _doFindFirstWordEndPos_Alpha(sentence, sentenceFlags, sentencePos);
			break;
		case _wordtype_chinese:
			wordEndPos = _doFindFirstWordEndPos_Chinese(sentence, sentenceFlags, sentencePos);
			break;
		default:
			ASSERT(false);
			return (size_t)-1;
		}
	}

	//take word
	pWord->assign(sentence + sentencePos, wordEndPos - sentencePos);
	*pWordType = firstCharType;
	return wordEndPos;
}

inline 
size_t CT9SearchEngine::_doFindFirstWordEndPos_Number(LPCWSTR sentence, unsigned sentenceFlags, size_t sentencePos)
{
	ASSERT(_judgeWordTypeByChar(sentence[sentencePos]) == _wordtype_number);

	size_t wordEndPos = sentencePos + 1;
	while (true)
	{
		WCHAR ch = sentence[wordEndPos];
		if (! (ch >= '0' && ch <= '9'))
		{
			ASSERT(_judgeWordTypeByChar(ch) != _wordtype_number);
			break;
		}

		ASSERT(_judgeWordTypeByChar(ch) == _wordtype_number);
		++ wordEndPos;
	}

	return wordEndPos;
}

inline 
size_t CT9SearchEngine::_doFindFirstWordEndPos_Alpha(LPCWSTR sentence, unsigned sentenceFlags, size_t sentencePos)
{
	ASSERT(_judgeWordTypeByChar(sentence[sentencePos]) == _wordtype_alpha);

	size_t wordEndPos = sentencePos + 1;
	if ((sentenceFlags & SENTENCE_PASSBY_WORD_FLAG) != 0)
	{//跳词时，大写字母作为分隔符
		while (true)
		{
			WCHAR ch = sentence[wordEndPos];
			if (! (ch >= 'a' && ch <= 'z'))
			{
				ASSERT((ch >= 'A' && ch <= 'Z') || _judgeWordTypeByChar(ch) != _wordtype_alpha);
				break;
			}

			++ wordEndPos;
		}
	}
	else
	{//连续字母作为一个词，不区分大小写
		while (true)
		{
			WCHAR ch = sentence[wordEndPos];
			if (! ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')))
			{
				ASSERT(_judgeWordTypeByChar(ch) != _wordtype_alpha);
				break;
			}

			++ wordEndPos;
		}
	}

	return wordEndPos;
}

inline 
size_t CT9SearchEngine::_doFindFirstWordEndPos_Chinese(LPCWSTR sentence, unsigned sentenceFlags, size_t sentencePos)
{
	ASSERT(_judgeWordTypeByChar(sentence[sentencePos]) == _wordtype_chinese);
	return sentencePos + 1;
}


void CT9SearchEngine::_transWordToAbc(LPCWSTR word, size_t wordLen, _enumWordType wordType, _ANSI_STRING_LIST* pAbcWordList)
{
	switch (wordType)
	{
	case _wordtype_number:
		_doTransWordToAbc_Number(word, wordLen, pAbcWordList);
		return;
	case _wordtype_alpha:
		_doTransWordToAbc_Alpha(word, wordLen, pAbcWordList);
		return;
	case _wordtype_chinese:
		_doTransWordToAbc_Chinese(word, wordLen, pAbcWordList);
		return;
	default:
		ASSERT(false);
		return;
	}
}

inline 
void CT9SearchEngine::_doTransWordToAbc_Number(LPCWSTR word, size_t wordLen, _ANSI_STRING_LIST* pAbcWordList)
{
	pAbcWordList->push_back(std::string());
	pAbcWordList->back().assign(word, word + wordLen);
	return;
}

inline 
void CT9SearchEngine::_doTransWordToAbc_Alpha(LPCWSTR word, size_t wordLen, _ANSI_STRING_LIST* pAbcWordList)
{
	std::string abcWord(word, word + wordLen);
	for (size_t i = 0; i < abcWord.length(); ++i)
	{
		char ch = abcWord[i];
		if (ch >= 'A' && ch <= 'Z')
			abcWord[i] = 'a' + (ch - 'A');
	}

	if (! abcWord.empty())
	{
		pAbcWordList->push_back(std::string());
		pAbcWordList->back().swap(abcWord);
	}

	return;
}

inline 
void CT9SearchEngine::_doTransWordToAbc_Chinese(LPCWSTR word, size_t wordLen, _ANSI_STRING_LIST* pAbcWordList)
{
	ASSERT(wordLen == 1);
	_convertHanziCharToPinyinList(word[0], pAbcWordList);
	return;
}

inline 
void CT9SearchEngine::_convertHanziCharToPinyinList(WCHAR ch, _ANSI_STRING_LIST* pPinyinList)
{
	CPinyinTable::LPCSTR_LIST pinyinList;
	CPinyinTable::getPinyinListOf(ch, &pinyinList);

	for (CPinyinTable::LPCSTR_LIST::const_iterator it = pinyinList.begin(); it != pinyinList.end(); ++it)
	{
		LPCSTR pinyin = *it;
		ASSERT(pinyin != NULL);
		ASSERT(pinyin[0] != 0);
		ASSERT(pinyin[strspn(pinyin, "abcdefghijklmnopqrstuvwxyz")] == 0);

		if (pinyin[0] != 0)
		{
			pPinyinList->push_back(std::string());
			pPinyinList->back().assign(pinyin);
		}
	}

	return;
}


void CT9SearchEngine::_removeSentencesByTokenFrom(_SENTENCE_LIST* pSentenceList, const TOKEN_TYPE& token)
{
	_SENTENCE_LIST sentenceList2;
	for (_SENTENCE_LIST::const_iterator it = pSentenceList->begin(); it != pSentenceList->end(); ++it)
	{
		const _comptr<_SENTENCE_ITEM>& spSentenceItem = *it;
		if (spSentenceItem->theToken != token)
			sentenceList2.push_back(spSentenceItem);
	}

	pSentenceList->swap(sentenceList2);
	return;
}

void CT9SearchEngine::_execSearch(LPCWSTR t9key, unsigned searchingFlags, const _TOKEN_SET* pWhiteTokenSet, const _TOKEN_SET* pBlackTokenSet, TOKEN_LIST* pResult) const
{
	if (m_sentenceList.empty())
		return;
	if (pWhiteTokenSet != NULL && pWhiteTokenSet->empty())
		return;

	std::wstring strKey(t9key);
	_adjustKeyAndSearchingFlags(&strKey, &searchingFlags);
	if (searchingFlags == 0)
		return;


	//key is empty
	if (strKey.empty())
	{
		_collectAllTokens(m_sentenceList, pWhiteTokenSet, pBlackTokenSet, pResult);
		return;
	}

	_searchMatchedTokens(m_sentenceList, strKey.c_str(), strKey.length(), searchingFlags, pWhiteTokenSet, pBlackTokenSet, pResult);
	return;
}

void CT9SearchEngine::_adjustKeyAndSearchingFlags(std::wstring* pKey, unsigned* pSearchingFlags)
{
	size_t keyLen = pKey->length();
	for (size_t i = 0; i < keyLen; ++i)
	{
		WCHAR c = (*pKey)[i];

		if ((*pSearchingFlags & SEARCHING_T9_FLAG) != 0)
		{
			if (c >= '2' && c <= '9')
				;
			else
				*pSearchingFlags &= ~SEARCHING_T9_FLAG;
		}
		if ((*pSearchingFlags & SEARCHING_ABC_FLAG) != 0)
		{
			if (c >= '0' && c <= '9')
				;
			else if (c >= 'a' && c <= 'z')
				;
			else if (c >= 'A' && c <= 'Z')
				(*pKey)[i] = 'a' + (c - 'A');
			else
				*pSearchingFlags &= ~SEARCHING_ABC_FLAG;
		}
	}
}

void CT9SearchEngine::_collectAllTokens(const _SENTENCE_LIST& sentenceList, const _TOKEN_SET* pWhiteTokenSet, const _TOKEN_SET* pBlackTokenSet, TOKEN_LIST* pTokenList)
{
	//保证返回token集无重复
	_TOKEN_SET metTokenSet;

	for (_SENTENCE_LIST::const_iterator it = sentenceList.begin(); it != sentenceList.end(); ++it)
	{
		const _SENTENCE_ITEM& sentenceItem = *(*it);

		if (pWhiteTokenSet != NULL && pWhiteTokenSet->find(sentenceItem.theToken) == pWhiteTokenSet->end())
			continue;
		if (pBlackTokenSet != NULL && pBlackTokenSet->find(sentenceItem.theToken) != pBlackTokenSet->end())
			continue;

		if (! metTokenSet.insert(sentenceItem.theToken).second)
			continue;

		pTokenList->push_back(sentenceItem.theToken);
	}

	return;
}


void CT9SearchEngine::_searchMatchedTokens(const _SENTENCE_LIST& sentenceList, LPCWSTR t9key, size_t t9keyLen, unsigned searchingFlags, const _TOKEN_SET* pWhiteTokenSet, const _TOKEN_SET* pBlackTokenSet, TOKEN_LIST* pTokenList)
{
	ASSERT(t9keyLen > 0);

	//保证返回token集无重复
	_TOKEN_SET metTokenSet;


	std::string t9keyAbcOrT9;
	if ((searchingFlags & (SEARCHING_ABC_FLAG|SEARCHING_T9_FLAG)) != 0)
		t9keyAbcOrT9.assign(t9key, t9key + t9keyLen);

	for (_SENTENCE_LIST::const_iterator it = sentenceList.begin(); it != sentenceList.end(); ++it)
	{
		const _SENTENCE_ITEM& sentenceItem = *(*it);

		unsigned sentenceSearchingFlags = searchingFlags & ~((sentenceItem.sentenceFlags & 0x0000FF00) >> 8); //suppress flags
		if (sentenceSearchingFlags == 0)
			continue;

		if (pWhiteTokenSet != NULL && pWhiteTokenSet->find(sentenceItem.theToken) == pWhiteTokenSet->end())
			continue;
		if (pBlackTokenSet != NULL && pBlackTokenSet->find(sentenceItem.theToken) != pBlackTokenSet->end())
			continue;

		if (metTokenSet.find(sentenceItem.theToken) != metTokenSet.end())
			continue;

		//check
		bool fMatched = false;

		if ((sentenceSearchingFlags & SEARCHING_STR_FLAG) != 0)
		{
			if (_wcs_isContain(sentenceItem.ccSentence.c_str(), sentenceItem.ccSentence.length(), t9key, t9keyLen))
				fMatched = true;
		}

		bool fWantRunT9 = true;
		if (!fMatched && (sentenceSearchingFlags & SEARCHING_ABC_FLAG) != 0)
		{
			if (_isAbcOrT9MatchSentence(t9keyAbcOrT9.c_str(), t9keyLen, sentenceItem, false))
				fMatched = true;

			if (sentenceItem.isAbcAndT9Same)
				fWantRunT9 = false;
		}

		if (!fMatched && fWantRunT9 && (sentenceSearchingFlags & SEARCHING_T9_FLAG) != 0)
		{
			if (_isAbcOrT9MatchSentence(t9keyAbcOrT9.c_str(), t9keyLen, sentenceItem, true))
				fMatched = true;
		}

		//push
		if (fMatched)
		{
			metTokenSet.insert(sentenceItem.theToken);
			pTokenList->push_back(sentenceItem.theToken);
		}
	}

	return;
}

inline 
bool CT9SearchEngine::_isAbcOrT9MatchSentence(LPCSTR t9key, size_t t9keyLen, const _SENTENCE_ITEM& sentenceItem, bool isT9)
{
	ASSERT(t9keyLen > 0);

	size_t firstKeyCharFlagPos = _calcAbcCharFlagPos(t9key[0]);
	if (! isT9)
	{
		if (!sentenceItem.firstCharMatchFlagsAbc.hasBits()
			|| !sentenceItem.firstCharMatchFlagsAbc.isBit(firstKeyCharFlagPos))
			return false;
	}
	else
	{
		if (!sentenceItem.firstCharMatchFlagsT9.hasBits()
			|| !sentenceItem.firstCharMatchFlagsT9.isBit(firstKeyCharFlagPos))
			return false;
	}

	if ((sentenceItem.sentenceFlags & SENTENCE_PASSBY_WORD_FLAG) != 0)
	{
		return _doAbcOrT9MatchFollowingWords(t9key, t9keyLen, 0, sentenceItem, sentenceItem.wordList.begin(), isT9);
	}
	else
	{
		for (_WORD_LIST::const_iterator it = sentenceItem.wordList.begin(); it != sentenceItem.wordList.end(); ++it)
		{
			if (_doAbcOrT9MatchFollowingWords(t9key, t9keyLen, 0, sentenceItem, it, isT9))
				return true;
		}

		return false;
	}
}

bool CT9SearchEngine::_doAbcOrT9MatchFollowingWords(LPCSTR t9key, size_t t9keyLen, size_t t9keyPos, const _SENTENCE_ITEM& sentenceItem, _WORD_LIST::const_iterator nextWordIter, bool isT9)
{
	const size_t t9keyRemainLen = t9keyLen - t9keyPos;
	ASSERT(t9keyRemainLen > 0);

	if (nextWordIter == sentenceItem.wordList.end())
		return false;

	const _WORD_ITEM& nextWord = *(*nextWordIter);
	bool fRunCheck = true;

	size_t nextKeyCharFlagPos = _calcAbcCharFlagPos(static_cast<char>(t9key[t9keyPos]));
	if (! isT9)
	{
		if (! nextWord.firstCharMatchFlagsAbc.isBit(nextKeyCharFlagPos))
			fRunCheck = false;
	}
	else 
	{
		if (! nextWord.firstCharMatchFlagsT9.isBit(nextKeyCharFlagPos))
			fRunCheck = false;
	}

	//check
	if (fRunCheck)
	{
		if (t9keyRemainLen == 1)
			return true;

		_FIRST_CHAR_MATCH_FLAGS metLeadCharFlags;
		metLeadCharFlags.reset();

		for (_ANSI_STRING_LIST::const_iterator it = nextWord.abcWordList.begin(); it != nextWord.abcWordList.end(); ++it)
		{
			const std::string& abcWord = *it;
			ASSERT(! abcWord.empty());

			char firstChar = abcWord[0];
			if (! _chr_isAbcOrT9Equal(firstChar, t9key[t9keyPos], isT9))
				continue;

			//check: whole/head word
			if (abcWord.length() > 1) //如果t9Word长度为1，直接跳过whole，直接进行lead判定
			{
				size_t cmpLen = t9keyRemainLen < abcWord.length() ? t9keyRemainLen : abcWord.length();
				if (_str_isAbcOrT9Equal(abcWord.c_str(), t9key + t9keyPos, cmpLen, isT9))
				{
					if (t9keyRemainLen <= abcWord.length())
						return true;
					if (_doAbcOrT9MatchFollowingWords(t9key, t9keyLen, t9keyPos + abcWord.length(), sentenceItem, nextWordIter + 1, isT9))
						return true;
				}
			}

			//check: lead char
			size_t firstCharFlagPos = _calcAbcCharFlagPos(!isT9 ? firstChar : _convAbcCharToT9(firstChar));
			if (! metLeadCharFlags.isBit(firstCharFlagPos))
			{
				metLeadCharFlags.setBit(firstCharFlagPos);
				if (_doAbcOrT9MatchFollowingWords(t9key, t9keyLen, t9keyPos + 1, sentenceItem, nextWordIter + 1, isT9))
					return true;
			}
		}
	}

	if ((sentenceItem.sentenceFlags & SENTENCE_PASSBY_WORD_FLAG) != 0)
	{
		return _doAbcOrT9MatchFollowingWords(t9key, t9keyLen, t9keyPos, sentenceItem, nextWordIter + 1, isT9);
	}
	else
	{
		return false; 
	}
}

inline 
CT9SearchEngine::_enumWordType CT9SearchEngine::_judgeWordTypeByChar(WCHAR ch)
{
	if (ch >= '0' && ch <= '9')
		return _wordtype_number;

	if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))
		return _wordtype_alpha;

	if (ch >= CPinyinTable::c_min_chinese_char && ch <= CPinyinTable::c_max_chinese_char)
		return  CPinyinTable::hasPinyinOf(ch) ? _wordtype_chinese : _wordtype_dummyOther;

	return _wordtype_dummyOther;
}

inline 
size_t CT9SearchEngine::_calcAbcCharFlagPos(char ch)
{
	if (ch >= 'a' && ch <= 'z')
		return 10 + (ch - 'a');
	else if (ch >= 'A' && ch <= 'Z')
		return 10 + (ch - 'A');
	else if (ch >= '0' && ch <= '9')
		return ch - '0';
	else
	{
		ASSERT(false);
		return (size_t)-1;
	}
}

inline 
char CT9SearchEngine::_convAbcCharToT9(char ch)
{
	const static char c_alpha_t9_table[26+1] = "22233344455566677778889999";

	if (ch >= '0' && ch <= '9')
		return ch;
	else if (ch >= 'a' && ch <= 'z')
		return c_alpha_t9_table[ch - 'a'];
	else if (ch >= 'A' && ch <= 'Z')
		return c_alpha_t9_table[ch - 'A'];
	else
	{
		ASSERT(false);
		return 0;
	}
}


inline 
bool CT9SearchEngine::_wcs_isContain(LPCWSTR sz, size_t szLen, LPCWSTR sub, size_t subLen)
{
	LPCWSTR szFound = std::search(sz, sz + szLen, sub, sub + subLen);
	return szFound != sz + szLen;
}

inline 
bool CT9SearchEngine::_chr_isAbcOrT9Equal(char c, char k, bool isT9)
{
	if (k == c)
		return true;
	if (isT9 && k == _convAbcCharToT9(c))
		return true;
	return false;
}

inline 
bool CT9SearchEngine::_str_isAbcOrT9Equal(LPCSTR cz, LPCSTR kz, size_t len, bool isT9)
{
	for (size_t i = 0; i < len; ++i)
		if (! _chr_isAbcOrT9Equal(cz[i], kz[i], isT9))
			return false;
	return true;
}
