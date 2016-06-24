package com.csw.soundcomf;

import android.util.Log;

/**
 * 一些获取方波buffer方法的公用类
 * @author json_data
 *
 */
public class GetFangBufferUtil {

	
	private short max_iAmp = Short.MAX_VALUE;//Short.MAX_VALUE 方波幅度<16 ~ 32767>
	private short min_iAmp = 0;
	private static String TAG="GetFangBufferUtil";
	public GetFangBufferUtil() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * 获取4Khz方波5个波长buffer,2
	 * bo
	 */
	public short[]  get5K5FangBuffer(){	
//		short[] m_bitDateF = getShortDate(2,"0101010101");	
//		short[] m_bitDateF = getShortDate(11,"0101010101");	//双声道16bit4Khz		
		short[] m_bitDateF = getShortDate(22,"0101010101");	//双声道16bit2Khz	
		return m_bitDateF;
	}
	
	
	/**
	 * 获取单个1Khz方波1波长buffer,0.5
	 */
	public short [] getSingleBuffer_1KHz(){	
//		short[] m_bitDateF = getShortDate(8,"0101010101");	
//		short[] m_bitDateF = getShortDate(44,"01");//双声道16bit1Khz
		short[] m_bitDateF = getShortDate(88,"01");//双声道16bit0.5Khz
		return m_bitDateF;		
	}
	
	/**
	 * 获取单个2Khz方波1波长buffer,1
	 */
	public short [] getSingleBuffer_2KHz(){	
//		short[] m_bitDateF = getShortDate(4,"0101010101");	
//		short[] m_bitDateF = getShortDate(22,"01");//双声道16bit2Khz
		short[] m_bitDateF = getShortDate(44,"01");//双声道16bit1Khz
		return m_bitDateF;		
	}

	/**
	 * 
	 * 根据传入的数据获取数据方波buffer
	 * @param dataHexStr  需要传入的数据，字符串格式，如：0x1234换成字符串为“1234”
	 * @param singleSinByteBuffer_1KHz
	 * @param singleSinByteBuffer_2KHz
	 * @return
	 */
	public short[] getDataBuffer(String dataHexStr,short [] singleSinByteBuffer_1KHz,short [] singleSinByteBuffer_2KHz){
		
		short[] shortBufferTemp = new short[32767];
		String hex0xdata_binary=hexString2binaryString(dataHexStr);
		Log.d(TAG, "0x"+dataHexStr+"=" + hex0xdata_binary);
		
		char item;
		for (int i = 0; i < hex0xdata_binary.length(); i++) {
			if (i == 0) {
				item = hex0xdata_binary.charAt(i);

				if (item == '0') {
					shortBufferTemp = singleSinByteBuffer_1KHz;
				} else if (item == '1') {
					shortBufferTemp = singleSinByteBuffer_2KHz;
				}
				System.out.println("shortBufferTemp="
						+ shortBufferTemp.length);
				continue;
			}
			item = hex0xdata_binary.charAt(i);

			if (item == '0') {
				shortBufferTemp = getAppendBuffer(shortBufferTemp,
						singleSinByteBuffer_1KHz);
			} else if (item == '1') {
				shortBufferTemp = getAppendBuffer(shortBufferTemp,
						singleSinByteBuffer_2KHz);
			}
			System.out.println("shortBufferTemp="
					+ shortBufferTemp.length);
		}
		return shortBufferTemp;
		
		
		
	}
	
	
	
	
	
	
	
	/**
	 * 拼接buffer
	 * 
	 * @author lgj
	 * @param shortBuffer1
	 *            ,shortBuffer2要拼接的buffer
	 * @return shortBufferTemp,合成后的buffer
	 */

	public short[] getAppendBuffer(short[] shortBuffer1, short[] shortBuffer2) {
		
		int shortBuffer1_length = shortBuffer1.length;
		int shortBuffer2_length = shortBuffer2.length;
		
		System.out.println("shortBuffer1_length=" + shortBuffer1_length);
		System.out.println("shortBuffer2_length=" + shortBuffer2_length);
		short[] shortBufferTemp = new short[shortBuffer1_length
				+ shortBuffer2_length];

		System.arraycopy(shortBuffer1, 0, shortBufferTemp, 0, shortBuffer1_length);
		System.arraycopy(shortBuffer2, 0, shortBufferTemp, shortBuffer1_length,
				shortBuffer2_length);

		return shortBufferTemp;

	}

	
	    /**
	     * 获取多少个方波
	     * @param m_iFangBo，方波波长
	     * @param binaryConur，用来判断需要几个方波，01需要一个，0101需要两个.....
	     * @return
	     */
		private short[] getShortDate(int m_iFangBo,String binaryConut) {
			int j = 0;
//			String strBinary = getstrBinary(this.m_date, this.m_lenght);
			String strBinary = binaryConut;
			int len = strBinary.length();
			int m_bitDateSize = len * m_iFangBo;
			short[] m_pRightDate = new short[m_bitDateSize];
			for (int i = 0; i < len; i++) {
				int ct = m_iFangBo;
				if (strBinary.charAt(i) == '0') {
					while (ct-- > 0) {
						m_pRightDate[j++] = min_iAmp;
//						System.out.println(j+"  "+min_iAmp);
					}
				} else {
					while (ct-- > 0) {
						m_pRightDate[j++] = max_iAmp;
//						System.out.println(j+"   "+max_iAmp);
					}
				}
			}
			return m_pRightDate;
		}
		/**
		 * 16进制字符串转成2进制字符串
		 * 
		 * @param hexString
		 * @return
		 */

		public  String hexString2binaryString(String hexString) {
			if (hexString == null || hexString.length() % 2 != 0)
				return null;
			String bString = "", tmp;
			for (int i = 0; i < hexString.length(); i++) {
				tmp = "0000"
						+ Integer.toBinaryString(Integer.parseInt(
								hexString.substring(i, i + 1), 16));
				bString += tmp.substring(tmp.length() - 4);
			}
			return bString;
		}
}
