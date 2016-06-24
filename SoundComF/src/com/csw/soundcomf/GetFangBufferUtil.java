package com.csw.soundcomf;

import android.util.Log;

/**
 * һЩ��ȡ����buffer�����Ĺ�����
 * @author json_data
 *
 */
public class GetFangBufferUtil {

	
	private short max_iAmp = Short.MAX_VALUE;//Short.MAX_VALUE ��������<16 ~ 32767>
	private short min_iAmp = 0;
	private static String TAG="GetFangBufferUtil";
	public GetFangBufferUtil() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * ��ȡ4Khz����5������buffer,2
	 * bo
	 */
	public short[]  get5K5FangBuffer(){	
//		short[] m_bitDateF = getShortDate(2,"0101010101");	
//		short[] m_bitDateF = getShortDate(11,"0101010101");	//˫����16bit4Khz		
		short[] m_bitDateF = getShortDate(22,"0101010101");	//˫����16bit2Khz	
		return m_bitDateF;
	}
	
	
	/**
	 * ��ȡ����1Khz����1����buffer,0.5
	 */
	public short [] getSingleBuffer_1KHz(){	
//		short[] m_bitDateF = getShortDate(8,"0101010101");	
//		short[] m_bitDateF = getShortDate(44,"01");//˫����16bit1Khz
		short[] m_bitDateF = getShortDate(88,"01");//˫����16bit0.5Khz
		return m_bitDateF;		
	}
	
	/**
	 * ��ȡ����2Khz����1����buffer,1
	 */
	public short [] getSingleBuffer_2KHz(){	
//		short[] m_bitDateF = getShortDate(4,"0101010101");	
//		short[] m_bitDateF = getShortDate(22,"01");//˫����16bit2Khz
		short[] m_bitDateF = getShortDate(44,"01");//˫����16bit1Khz
		return m_bitDateF;		
	}

	/**
	 * 
	 * ���ݴ�������ݻ�ȡ���ݷ���buffer
	 * @param dataHexStr  ��Ҫ��������ݣ��ַ�����ʽ���磺0x1234�����ַ���Ϊ��1234��
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
	 * ƴ��buffer
	 * 
	 * @author lgj
	 * @param shortBuffer1
	 *            ,shortBuffer2Ҫƴ�ӵ�buffer
	 * @return shortBufferTemp,�ϳɺ��buffer
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
	     * ��ȡ���ٸ�����
	     * @param m_iFangBo����������
	     * @param binaryConur�������ж���Ҫ����������01��Ҫһ����0101��Ҫ����.....
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
		 * 16�����ַ���ת��2�����ַ���
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
