package com.duowan.fw.util;

import java.util.HashSet;
import java.util.Set;


// 封装集合操作: 交，叉，并，补
public class JSetUtil {
	
	/**
	 * 交 A & B, 属于A且属于B
	 * */
	public static <T> T[] intersect(T[] ret, T[] setl, T[] setr){
		Set<T> hs = new HashSet<T>();
        for(T obj:setl){
            hs.add(obj);
        }
        for(T obj:setr){
            hs.add(obj);
        }
        return hs.toArray(ret);
	}
	
	/**
	 * 差 A - B, 属于A不属于B
	 * */
	public static <T> T[] substract(T[] ret, T[] setl, T[] setr){
		Set<T> hs = new HashSet<T>();
        for (T obj : setl) {   
            if(!hs.contains(obj)) {   
                hs.add(obj);   
            }   
        }   
        for (T obj : setr) {   
            if (hs.contains(obj)) {   
                hs.remove(obj);   
            } 
        }   
        return hs.toArray(ret);
	}
	
	/**
	 * 并 A | B， 属于A或者属于B
	 * */
	public static <T> T[] union(T[] ret, T[] setl, T[] setr){
		Set<T> hs = new HashSet<T>();
        for(T str:setl){
            hs.add(str);
        }
        for(T str:setr){
            hs.add(str);
        }
        return hs.toArray(ret);
	}
	
	/**
	 * 补 : (A-B)|(B-A)
	 * */
	
	
	
}
