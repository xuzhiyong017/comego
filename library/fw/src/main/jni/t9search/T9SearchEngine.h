#pragma once

//[经典用法]
	//名称项：SENTENCE_SUPPRESS_T9_FLAG  (==0x0400)
	//数字项：SENTENCE_SUPPRESS_ABC_FLAG | SENTENCE_SUPPRESS_T9_FLAG  (==0x0600)
	//搜索：SEARCHING_STR_FLAG | SEARCHING_ABC_FLAG  (==0x03)


class CT9SearchEngine 
{
public:
	CT9SearchEngine(void);
	~CT9SearchEngine(void);

public:
	const static unsigned SENTENCE_PASSBY_WORD_FLAG		= 0x01;
	const static unsigned SENTENCE_ISOLATE_CHAR_FLAG	= 0x02;
	const static unsigned SENTENCE_SUPPRESS_STR_FLAG	= 0x0100; 
	const static unsigned SENTENCE_SUPPRESS_ABC_FLAG	= 0x0200;
	const static unsigned SENTENCE_SUPPRESS_T9_FLAG		= 0x0400;

	const static unsigned SEARCHING_STR_FLAG	= 0x01;
	const static unsigned SEARCHING_ABC_FLAG	= 0x02;
	const static unsigned SEARCHING_T9_FLAG		= 0x04;


	typedef int	TOKEN_TYPE;
	typedef std::deque<TOKEN_TYPE>	TOKEN_LIST;
	typedef std::less<TOKEN_TYPE>	TOKEN_LESS_OP;

	void addSentence(LPCWSTR sentence, unsigned sentenceFlags, const TOKEN_TYPE& token);

	void removeAllSentences();
	void removeSentencesByToken(const TOKEN_TYPE& token);

	void search(LPCWSTR t9key, unsigned searchingFlags, TOKEN_LIST* pResult);
	void search(LPCWSTR t9key, unsigned searchingFlags, const TOKEN_LIST* pWhiteTokenList, const TOKEN_LIST* pBlackTokenList, TOKEN_LIST* pResult);

private:
	CT9SearchEngine(const CT9SearchEngine&); //disabled
	void operator =(const CT9SearchEngine&); //disabled

private:
	typedef std::set<TOKEN_TYPE, TOKEN_LESS_OP>	_TOKEN_SET;
	typedef std::deque<std::string>	_ANSI_STRING_LIST;
	typedef std::deque<std::string>	_LPCSTR_LIST;

	template <class T>
	struct _comptr //smart-ptr of _SENTENCE_ITEM and _WORD_ITEM
	{
		explicit _comptr(T* p) {p->_ref=0; _doAssignP(p);}
		_comptr(const _comptr& r) {_doAssignP(r._p);}
		~_comptr() {_doUnAssignP();}
		_comptr& operator =(const _comptr& r) {if (_p != r._p) {_doUnAssignP(); _doAssignP(r._p);} return *this;}
		bool operator ==(T* p) const {return _p == p;}
		bool operator !=(T* p) const {return _p != p;}
		T* get() const {return _p;}
		T* operator ->() const {return _p;}
		T& operator *() const {return *_p;}
		private: void _doAssignP(T* p) {ASSERT(p); ++p->_ref; _p = p;}
		private: void _doUnAssignP() {if (--_p->_ref==0) {delete _p;} }
		private: T* _p;
	};


	struct _FIRST_CHAR_MATCH_FLAGS
	{
		unsigned short	number_flags;
		unsigned long	alpha_flags;

		void reset() {number_flags = 0; alpha_flags = 0;}
		void orAssign(const _FIRST_CHAR_MATCH_FLAGS& r) {number_flags |= r.number_flags; alpha_flags |= r.alpha_flags;}

		bool hasBits() const {return number_flags != 0 || alpha_flags != 0;}

		bool isBit(size_t pos) const
		{
			return pos < 10 ? (number_flags & (1<<pos)) != 0 
				 : pos < 36 ? (alpha_flags & (1<<(pos-10))) != 0 
				 : false;
		}
		void setBit(size_t pos) 
		{
			if (pos < 10)
				number_flags |= (1<<pos);
			else if (pos < 36)
				alpha_flags |= (1<<(pos-10));
			else
				ASSERT(false);
		}
	};

	enum _enumWordType {_wordtype_dummyOther, _wordtype_number, _wordtype_alpha, _wordtype_chinese};
	struct _WORD_ITEM
	{
		unsigned int _ref;
#ifdef _DEBUG
		std::wstring ccWord;
		_enumWordType wordType;
#endif
		//for abc and t9
		_ANSI_STRING_LIST abcWordList;
		_FIRST_CHAR_MATCH_FLAGS firstCharMatchFlagsAbc; //optimization
		_FIRST_CHAR_MATCH_FLAGS firstCharMatchFlagsT9; //optimization

#ifdef _DEBUG
		_WORD_ITEM() {}
		_WORD_ITEM(const _WORD_ITEM&);
		void operator =(const _WORD_ITEM&);
#endif
	};

	typedef std::deque<_comptr<_WORD_ITEM> >	_WORD_LIST;
	struct _SENTENCE_ITEM
	{
		unsigned int _ref;
		std::wstring ccSentence;
		unsigned sentenceFlags;
		TOKEN_TYPE theToken;
		//for abc and t9
		_WORD_LIST wordList;
		_FIRST_CHAR_MATCH_FLAGS firstCharMatchFlagsAbc; //optimization
		_FIRST_CHAR_MATCH_FLAGS firstCharMatchFlagsT9; //optimization
		bool isAbcAndT9Same;

#ifdef _DEBUG
		_SENTENCE_ITEM() {}
		_SENTENCE_ITEM(const _SENTENCE_ITEM&);
		void operator =(const _SENTENCE_ITEM&);
#endif
	};

	typedef std::deque<_comptr<_SENTENCE_ITEM> >	_SENTENCE_LIST;
	_SENTENCE_LIST m_sentenceList;

private:
	static void _makeSentenceItem(LPCWSTR sentence, unsigned sentenceFlags, const TOKEN_TYPE& token, _SENTENCE_ITEM* pSentenceItem);
	static void _adjustSentence(std::wstring* pSentence);

	static void _makeWordItem(LPCWSTR word, size_t wordLen, _enumWordType wordType, _WORD_ITEM* pWordItem);

	static size_t _takeFirstWord(LPCWSTR sentence, unsigned sentenceFlags, size_t sentencePos, std::wstring* pWord, _enumWordType* pWordType);
	static size_t _doFindFirstWordEndPos_Number(LPCWSTR sentence, unsigned sentenceFlags, size_t sentencePos);
	static size_t _doFindFirstWordEndPos_Alpha(LPCWSTR sentence, unsigned sentenceFlags, size_t sentencePos);
	static size_t _doFindFirstWordEndPos_Chinese(LPCWSTR sentence, unsigned sentenceFlags, size_t sentencePos);

	static void _transWordToAbc(LPCWSTR word, size_t wordLen, _enumWordType wordType, _ANSI_STRING_LIST* pAbcWordList);
	static void _doTransWordToAbc_Number(LPCWSTR word, size_t wordLen, _ANSI_STRING_LIST* pAbcWordList);
	static void _doTransWordToAbc_Alpha(LPCWSTR word, size_t wordLen, _ANSI_STRING_LIST* pAbcWordList);
	static void _doTransWordToAbc_Chinese(LPCWSTR word, size_t wordLen, _ANSI_STRING_LIST* pAbcWordList);

	/*template <class CH>
	static void _convertAlphaWordToT9(const CH* pFirst, const CH* pLast, std::string* pT9Word);
	static void _doConvertAndPushAlphaWordCharToT9(char ch, std::string* pT9Word);
	*/
	static void _convertHanziCharToPinyinList(WCHAR ch, _ANSI_STRING_LIST* pPinyinList);

	static void _removeSentencesByTokenFrom(_SENTENCE_LIST* pSentenceList, const TOKEN_TYPE& token);

	void _execSearch(LPCWSTR t9key, unsigned searchingFlags, const _TOKEN_SET* pWhiteTokenSet, const _TOKEN_SET* pBlackTokenSet, TOKEN_LIST* pResult) const;

	static void _adjustKeyAndSearchingFlags(std::wstring* pKey, unsigned* pSearchingFlags);

	static void _collectAllTokens(const _SENTENCE_LIST& sentenceList, const _TOKEN_SET* pWhiteTokenSet, const _TOKEN_SET* pBlackTokenSet, TOKEN_LIST* pTokenList);

	static void _searchMatchedTokens(const _SENTENCE_LIST& sentenceList, LPCWSTR t9key, size_t t9keyLen, unsigned searchingFlags, const _TOKEN_SET* pWhiteTokenSet, const _TOKEN_SET* pBlackTokenSet, TOKEN_LIST* pTokenList);
	static bool _isAbcOrT9MatchSentence(LPCSTR t9key, size_t t9keyLen, const _SENTENCE_ITEM& sentenceItem, bool isT9);
	static bool _doAbcOrT9MatchFollowingWords(LPCSTR t9key, size_t t9keyLen, size_t t9keyPos, const _SENTENCE_ITEM& sentenceItem, _WORD_LIST::const_iterator nextWordIter, bool isT9);

	static _enumWordType _judgeWordTypeByChar(WCHAR ch);

	static size_t _calcAbcCharFlagPos(char ch);
	static char _convAbcCharToT9(char ch);

	static bool _wcs_isContain(LPCWSTR sz, size_t szLen, LPCWSTR sub, size_t subLen);
	static bool _chr_isAbcOrT9Equal(char c, char k, bool isT9);
	static bool _str_isAbcOrT9Equal(LPCSTR cz, LPCSTR kz, size_t len, bool isT9);
};
