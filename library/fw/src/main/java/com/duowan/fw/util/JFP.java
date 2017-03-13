package com.duowan.fw.util;

import android.util.Pair;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A functional programming style utility
 */
public class JFP {

    /*
     * Generic function object
     */

    public static interface UnaryFunc<R, A> {
        R apply(A x);
    }

    public static interface BinaryFunc<R, A, B> {
        R apply(A a, B b);
    }

    /* Predicate */
    public static abstract class Pred<A> implements UnaryFunc<Boolean, A> {
        @Override
        public Boolean apply(A x) { return pred(x); }

        public abstract boolean pred(A x);
    }

    /* Equality */
    public static abstract class Eq<A> implements BinaryFunc<Boolean, A, A> {
        @Override
        public Boolean apply(A x, A y) {
            return eq(x, y);
        }

        public abstract boolean eq(A x, A y);
    }

    public static class Tuple<A, B, C> {
        public A a;
        public B b;
        public C c;
        
        public Tuple(A x, B y, C z) {
            a = x;
            b = y;
            c = z;
        }
    }

    public static <A, B, C> Tuple<A, B, C> makeTuple(A a, B b, C c) {
        return new Tuple<A, B, C>(a, b, c);
    }

    public static <E> Pred<E> negate(final Pred<E> p) {
        return new Pred<E>() {
            @Override
            public boolean pred(E x) {
                return !p.pred(x);
            }
        };
    }

    public static int limit(int x, int low, int high) {
        return Math.min(Math.max(low, x), high);
    }

    /*
     * Functions over abstract list
     */

    /**
     * Find among a list by using predicate function p
     */
    public static <E> E find(Pred<E> p, List<E> xs) {
        if (!empty(xs) )
            for (E x : xs)
                if (p.pred(x) )
                    return x;
        return null;
    }

    /**
     * Find among a list for an element
     */
    public static <E> E find(final E x, List<E> xs) {
        return find(new Pred<E>() {
                @Override
                public boolean pred(E y) {
                    return y.equals(x);
                }
            }, xs);
    }

    public static <E> int findIndex(Pred<E> p, List<E> xs) {
        int i, n = length(xs);
        for (i = 0; i < n && !p.pred(xs.get(i)); ++i);
        return i == n ? -1 : i;
    }

    /**
     * looks up a key in an association list (which is realized by pair)
     */
    public static <K, V> V lookup(K k, List<Pair<K, V> > xs) {
        if ( !empty(xs) )
            for (Pair<K, V> x : xs)
                if ( k == x.first )
                    return x.second;
        return null;
    }

    public static <E> E lookup(int k, SparseArray<E> xs) {
        return JFP.empty(xs) ? null : xs.get(k);
    }

    /**
     * Remove duplicated items by using a compare function, O(N^2) time bound
     */
    public static <E> List<E> nubBy(final Eq<E> cmp, List<E> xs) {
        List<E> ys = new ArrayList<E>();
        if ( !empty(xs) ) 
            for (final E x : xs) {
                if (find(new Pred<E>() {
                            @Override
                            public boolean pred(E y) { return cmp.eq(x, y); }
                        }, ys) == null)
                    ys.add(x);
            }
        return ys;
    }

    /**
     * Remove duplicate items by using Object.equals(). O(N^2) time bound
     * The name nub means `essence'.
     */
    public static <E> List<E> nub(List<E> xs) {
        return nubBy(new Eq<E>() {
                @Override
                public boolean eq(E x, E y) {
                    return y.equals(x);
                }
            }, xs);
    }

    /*
     * Auxiliary functions
     */

    /**
     * Test if a collection is either NIL or empty
     */
    public static boolean empty(Collection<?> xs){
        return xs == null || xs.isEmpty();
    }

    /**
     * Test if an array is either NIL or empty
     */
    public static <T> boolean empty(T[] xs) {
        return xs == null || xs.length == 0;
    }

    public static boolean empty(SparseArray<?> xs) {
        return xs == null || xs.size() == 0;
    }

    public static boolean empty(int[] xs) {
        return xs == null || xs.length == 0;
    }

    /**
     * Test if a abstract string is either NIL or empty
     */
    public static boolean empty(CharSequence s) {
        return s == null || s.length() == 0;
    }

    public static boolean empty(Map<?, ?> m) {
        return m == null || m.isEmpty();
    }


    /**
     * Safe return the size of a collection even it's NIL
     */
    public static int size(Collection<?> xs) {
        return xs == null ? 0 : xs.size();
    }

    public static int size(CharSequence s) {
        return s == null ? 0 : s.length();
    }

    public static <T> int size(T[] xs) {
        return xs == null ? 0 : xs.length;
    }

    public static int size(int[]  xs) {
        return xs == null ? 0 : xs.length;
    }

    /**
     * Defined just as alias of 'size'.
     */
    public static int length(Collection<?> xs) { return size(xs); }

    public static int length(CharSequence s) { return size(s); }

    public static <T> int length(T[] xs) { return size(xs); }

    public static int length(int[] xs) { return size(xs); }

    /**
     * Safe add element to a list even the list is empty
     */
    public static <E> List<E> add(List<E> xs, E x) {
        if (xs == null)
            xs = new ArrayList<E>();
        xs.add(x);
        return xs;
    }

    /**
     * span, applied to a predicate p and a list xs, returns a tuple where first 
     * element is longest prefix (possibly empty) of xs of elements that satisfy 
     * p and second element is the remainder of the list.
     */
    public static <E> Pair<List<E>, List<E> > span(Pred<E> p, List<E> xs) {
        return Pair.create(takeWhile(p, xs), dropWhile(p, xs));
    }

    /**
     * Safe take the first n elements from a list
     */
    public static <E> List<E> take(int n, List<E> xs) {
        List<E> ys = new ArrayList<E>();
        if ( empty(xs) || n <= 0)
            return ys;
        ys.addAll(xs.subList(0, Math.min(n, length(xs))));
        return ys;
    }

    public static String take(int n, String s) {
        return s.substring(0, limit(n, 0, JFP.length(s)));
    }

    public static <K, V> Map<K, V> take(int n, Map<K, V> xs) {
        Map<K, V> ys = new HashMap<K, V>();
        for (K k : xs.keySet())
            if (n-- > 0)
                ys.put(k, xs.get(k));
        return ys;
    }

    public static <E> List<E> takeWhile(Pred<E> p, List<E> xs) {
        int i, n = length(xs);
        for (i = 0; i < n && p.pred(xs.get(i)); ++i);
        return take(i, xs);
    }

    /** Safe drop the first n elements from a list */
    public static <E> List<E> drop(int n, List<E> xs) {
        List<E> ys = new ArrayList<E>();
        if ( xs == null || n > length(xs))
            return ys;
        ys.addAll(xs.subList(Math.max(0, n), length(xs)));
        return ys;
    }

    public static String drop(int n, String s) {
        if ( s == null || n > length(s))
            return "";
        return s.substring(Math.max(0, n));
    }

    public static <E> List<E> dropWhile(Pred<E> p, List<E> xs) {
        int i, n = length(xs);
        for (i = 0; i < n && p.pred(xs.get(i)); ++i);
        return drop(n, xs);
    }

    public static <E> E first(List<E> xs) {
        return JFP.empty(xs) ? null : xs.get(0);
    }

    /**
     * Access the last element of a list
     */
    public static <E> E last(List<E> xs) {
        return JFP.empty(xs) ? null : xs.get(JFP.lastIndex(xs));
    }

    /**
     * Safe return the index of the last element even if the list is NIL
     * @return -1 if the list is NIL
     */
    public static int lastIndex(List<?> xs) {
        return JFP.empty(xs) ? -1 : xs.size() - 1;
    }

    public static <E> E first(Collection<E> xs) {
        if (empty(xs))
            return null;
        return xs.iterator().next();
    }

    /** 
     *Safe convert a collection to list even it's NIL 
     */
    public static <E> List<E> toList(Collection<? extends E> xs) {
        return empty(xs) ? new ArrayList<E>() : new ArrayList<E>(xs);
    }
    
    /**
     * Safe convert an array to array list even it's NIL
     */
	public static <T> List<T> toList(T...xs) {
		List<T> ys = new ArrayList<T>();
		if (!empty(xs))
			for (T x : xs)
				ys.add(x);
		return ys;
	}

    public static List<Integer> toList(int[] xs) {
        List<Integer> ys = new ArrayList<Integer>();
        if (!empty(xs))
            for (int x : xs)
                ys.add(x);
        return ys;
    }

    public static <E> List<Pair<Integer, E> > toList(SparseArray<E> xs) {
        List<Pair<Integer, E> > ys = new ArrayList<Pair<Integer, E> >();
        if (!empty(xs) )
            for (int i = 0; i < xs.size(); ++i)
                ys.add(Pair.create(xs.keyAt(i), xs.valueAt(i)));
        return ys;
    }

    public static int[] toArray(List<Integer> xs) {
        int n = length(xs);
        int[] ys = new int[n];
        for (int i = 0; i < n; ++i)
            ys[i] = xs.get(i);
        return ys;
    }

    /**
     * Safe reference to a list even it's NIL
     * @return reference to the list if it's not NIL, or an empty list.
     */
    public static <E> List<E> ref(List<E> xs) {
        return xs == null ? new ArrayList<E>(): xs;
    }

    public static String ref(String s) {
        return s == null ? "" : s;
    }

    /** Safe zipper */
    public static <A, B> List<Pair<A, B> > zip(List<A> as, List<B> bs) {
        List<Pair<A, B> > xs = new ArrayList<Pair<A, B> >();
        if ( !empty(as) && !empty(bs) ) {
            Iterator<A> a = as.iterator();
            Iterator<B> b = bs.iterator();
            while ( a.hasNext() && b.hasNext() )
                xs.add(Pair.create(a.next(), b.next()));
        }
        return xs;
    }

    /** Safe equal predicate */
    public static boolean eq(Object a, Object b) {
        if (a == null && b == null)
            return true;
        else if (a == null)
            return false;
        else
            return a.equals(b);
    }
    
    @SuppressWarnings("unchecked")
    public static <T> void convert(T[] dst, Object[] src) {
        for (int i = 0; i < src.length; i++) {
            dst[i] = (T) src;
        }
    }

    /** 
     * Safe concatenation 
     * Note that the first list is MUTATED if it isn't empty.
     */
    public static <T> List<T> concat(List<T> xs, List<T> ys) {
        List<T> zs = ref(xs);
        zs.addAll(ref(ys));
        return zs;
    }

    /** 
     * Safe union, O(N^2) time bound 
     * Note that 
     *   1. the first list is mutated if it isn't empty.
     *   2. Only the duplicated elements in the second list is removed, those duplicated ones in first is kept.
     * If the order needn't be kept, we strongly recommend to use set instead!
     */
    public static <T> List<T> unionBy(final Eq<T> cmp, List<T> xs, List<T> ys) {
        ys = ref(ys);
        if (empty(xs))
            return ys;
        for (T y : ys) {
            boolean e = false;
            for (T x : xs)
                if (cmp.eq(x, y)) {
                    e = true;
                    break;
                }
            if (!e)
                xs.add(y);
        }
        return xs;
    }

    public static <T> List<T> union(List<T> xs, List<T> ys) {
        return unionBy(new Eq<T>() {
                @Override
                public boolean eq(T x, T y) { return JFP.eq(x, y); }
            }, xs, ys);
    }

    /** mapping */
    public static <A, B> List<B> map(UnaryFunc<B, A> f, List<A> xs) {
        List<B> ys = new ArrayList<B>();
        for(A x : ref(xs))
            ys.add(f.apply(x));
        return ys;
    }

    /** filtering */
    public static <E> List<E> filter(Pred<E> p, List<E> xs) {
        List<E> ys = new ArrayList<E>();
        for (E x : xs)
            if (p.pred(x))
                ys.add(x);
        return ys;
    }

    /** 
     * ordered insertion.
     * O(\lg N) algorithm as it uses binary search
     * Please ensure the list support random access, so that the binary search make sense.
     */
    public static <E> List<E> insert(Comparator<E> cmp, E x, List<E> xs) {
        int pos = Collections.binarySearch(xs, x, cmp);
        pos = (pos < 0) ? -pos - 1 : pos;
        xs.add(- pos - 1, x);
        return xs;
    }

    /** 
     * A wrapper to java.util.Collections.sort(). 
     * for chained style usage.
     */
    public static <E> List<E> sort(Comparator<E> cmp, List<E> xs) {
        xs = ref(xs);
        Collections.sort(xs, cmp);
        return xs;
    }

    public static int sum(Integer[] xs) {
        int n = 0;
        for (int x : xs)
            n += x;
        return n;
    }

    public static long sum(Long[] xs) {
        long n = 0;
        for (long x : xs)
            n += x;
        return n;
    }

    public static int sum(List<Integer> xs) {
        int n = 0;
        for (int x : xs)
            n += x;
        return n;
    }

    public static long sum(List<Long> xs, Long nomame) {
        long n = 0;
        for (long x : xs)
            n += x;
        return n;
    }

    public static int ord(boolean x) { return x ? 1 : 0; }

    /**
     * Tree based Map (ordable)
     */
    public static class M {
        public static <K, V> List<Pair<K, V> > toList(Map<K, V> m) {
            List<Pair<K, V> > xs = new ArrayList<Pair<K, V> >();
            if (!empty(m))
                for (Map.Entry<K, V> e : m.entrySet())
                    xs.add(Pair.create(e.getKey(), e.getValue()));
            return xs;
        }


        public static <K extends Comparable<?>, V> Map<K, V> fromList(List<Pair<K, V> > xs) {
            Map<K, V> m = new TreeMap<K, V>();
            if (!empty(xs))
                for (Pair<K, V> p : xs)
                    m.put(p.first, p.second);
            return m;
        }

        public static <V> Map<Integer, V> fromList(SparseArray<V> xs) {
            Map<Integer, V> m = new TreeMap<Integer, V>();
            if (!empty(xs))
                for(int i=0; i < xs.size(); ++i)
                    m.put(xs.keyAt(i), xs.valueAt(i));
            return m;
        }
    }
}