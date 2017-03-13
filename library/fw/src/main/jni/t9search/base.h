#pragma once

typedef const char*	LPCSTR;

typedef unsigned short	WCHAR;
typedef const WCHAR*	LPCWSTR;

#include <string>
#define wstring	basic_string<WCHAR>

#include <vector>
#include <deque>
#include <set>

#include <algorithm>

#include <assert.h>
#define ASSERT	assert

