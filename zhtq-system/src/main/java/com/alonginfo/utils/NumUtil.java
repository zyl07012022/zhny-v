package com.alonginfo.utils;

import java.math.BigDecimal;

/**
 * @author YR:
 * @version 创建时间：2021-2-18 下午5:24:37
 * 类说明
 */
public class NumUtil {
	/**
	 * @param a 单数  32
	 * @param b 总数  145
	 * a / b    计算百分比32/145
	 * @return 22.07%
	 */
	public static String CalculateUtil(BigDecimal a, BigDecimal b){
		String percent =
				b == null ? "0.00" :b.compareTo(new BigDecimal(0)) == 0 ? "0.00":
					a == null ? "0.00" :a.multiply(new BigDecimal(100)).divide(b,0,BigDecimal.ROUND_HALF_UP) + "";
		return percent;
	}


	public static double deciMal(int top, int below) {
		double result;
		if(top == 0 || below == 0){
			result = 0d;
		} else {
			result = new BigDecimal((float)top / below).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		return result;
	}
	
	/**
	 * @param a 单数  32
	 * @param b 总数  145
	 * a / b    计算百分比32/145
	 * @return 22.07%
	 */
	public static String deciMal(BigDecimal a, BigDecimal b){
		String percent =
				b == null ? "0.00%" :b.compareTo(new BigDecimal(0)) == 0 ? "0.00%":
					a == null ? "0.00%" :a.multiply(new BigDecimal(100)).divide(b,1,BigDecimal.ROUND_HALF_UP) + "%";
		return percent;
	}
	
	
	/**
	 * double保留两位小数
	 * @return double
	 */
	public static double doubleRound(double a){
		BigDecimal b = new BigDecimal(a);
		return b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	public static void main(String[] args) {
//		System.out.println(doubleRound(0));
		System.out.println(deciMal(new BigDecimal(13d),new BigDecimal(109d)));
	}

}
