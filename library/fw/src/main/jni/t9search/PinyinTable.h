#pragma once


class CPinyinTable
{
public:
	const static WCHAR c_min_chinese_char = 0x4E00;
	const static WCHAR c_max_chinese_char = 0x9FBF;

	//typedef std::deque<LPCSTR>	LPCSTR_LIST;
	class LPCSTR_LIST : public std::vector<LPCSTR>
	{
		public: LPCSTR_LIST() {reserve(3);} 
	};

	static void getPinyinListOf(WCHAR ch, LPCSTR_LIST* pPinyinList);
	static char getPinyinLeadCharOf(WCHAR ch);
	static LPCSTR getFirstPinyinOf(WCHAR ch);

	static bool hasPinyinOf(WCHAR ch);
};
