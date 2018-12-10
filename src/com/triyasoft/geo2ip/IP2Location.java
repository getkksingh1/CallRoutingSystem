package com.triyasoft.geo2ip;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

public class IP2Location {
	public static final Pattern pattern = Pattern
			.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
	static final BigInteger MAX_IPv4_RANGE = new BigInteger("2").pow(32)
			.subtract(BigInteger.ONE);
	static final BigInteger MAX_IPv6_RANGE = new BigInteger("2").pow(128)
			.subtract(BigInteger.ONE);
	static final int[] COUNTRY_POSITION = { 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 };
	static final int[] REGION_POSITION = { 0, 0, 0, 3, 3, 3, 3, 3, 3, 3, 3, 3,
			3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 };
	static final int[] CITY_POSITION = { 0, 0, 0, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
			4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4 };
	static final int[] ISP_POSITION = { 0, 0, 3, 0, 5, 0, 7, 5, 7, 0, 8, 0, 9,
			0, 9, 0, 9, 0, 9, 7, 9, 0, 9, 7, 9 };
	static final int[] LATITUDE_POSITION = { 0, 0, 0, 0, 0, 5, 5, 0, 5, 5, 5,
			5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 };
	static final int[] LONGITUDE_POSITION = { 0, 0, 0, 0, 0, 6, 6, 0, 6, 6, 6,
			6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6 };
	static final int[] DOMAIN_POSITION = { 0, 0, 0, 0, 0, 0, 0, 6, 8, 0, 9, 0,
			10, 0, 10, 0, 10, 0, 10, 8, 10, 0, 10, 8, 10 };
	static final int[] ZIPCODE_POSITION = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 7, 7,
			7, 0, 7, 7, 7, 0, 7, 0, 7, 7, 7, 0, 7 };
	static final int[] TIMEZONE_POSITION = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			8, 8, 7, 8, 8, 8, 7, 8, 0, 8, 8, 8, 0, 8 };
	static final int[] NETSPEED_POSITION = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 8, 11, 0, 11, 8, 11, 0, 11, 0, 11, 0, 11 };
	static final int[] IDDCODE_POSITION = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 9, 12, 0, 12, 0, 12, 9, 12, 0, 12 };
	static final int[] AREACODE_POSITION = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 10, 13, 0, 13, 0, 13, 10, 13, 0, 13 };
	static final int[] WEATHERSTATIONCODE_POSITION = { 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 14, 0, 14, 0, 14, 0, 14 };
	static final int[] WEATHERSTATIONNAME_POSITION = { 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 15, 0, 15, 0, 15, 0, 15 };
	static final int[] MCC_POSITION = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 9, 16, 0, 16, 9, 16 };
	static final int[] MNC_POSITION = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 10, 17, 0, 17, 10, 17 };
	static final int[] MOBILEBRAND_POSITION = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 18, 0, 18, 11, 18 };
	static final int[] ELEVATION_POSITION = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 19, 0, 19 };
	static final int[] USAGETYPE_POSITION = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 12, 20 };
	public String IPDatabasePath;
	public String IPDatabasePathIPv6;
	public String IPLicensePath;
	static boolean gotdelay = false;
	static boolean isdelayed = false;
	static boolean _alreadyCheckedKey = false;

	private MetaData open(RandomAccessFile paramRandomAccessFile, int paramInt)
			throws FileNotFoundException, IOException {
		MetaData localMetaData = new MetaData();
		if (paramInt == 4) {
			localMetaData.setDBType(read8(1L, paramRandomAccessFile));
			localMetaData.setDBColumn(read8(2L, paramRandomAccessFile));
			localMetaData.setDBYear(read8(3L, paramRandomAccessFile));
			localMetaData.setDBMonth(read8(4L, paramRandomAccessFile));
			localMetaData.setDBDay(read8(5L, paramRandomAccessFile));
			localMetaData.setDBCount(read32(6L, paramRandomAccessFile)
					.intValue());
			localMetaData.setBaseAddr(read32(10L, paramRandomAccessFile)
					.intValue());
		} else if (paramInt == 6) {
			localMetaData.setDBTypeIPv6(read8(1L, paramRandomAccessFile));
			localMetaData.setDBColumnIPv6(read8(2L, paramRandomAccessFile));
			localMetaData.setDBYearIPv6(read8(3L, paramRandomAccessFile));
			localMetaData.setDBMonthIPv6(read8(4L, paramRandomAccessFile));
			localMetaData.setDBDayIPv6(read8(5L, paramRandomAccessFile));
			localMetaData.setDBCountIPv6(read32(6L, paramRandomAccessFile)
					.intValue());
			localMetaData.setBaseAddrIPv6(read32(10L, paramRandomAccessFile)
					.intValue());
		}
		return localMetaData;
	}

	/**
	 * @deprecated
	 */
	protected void finalize() throws Throwable {
		super.finalize();
	}

	public IPResult IPQuery(String paramString) throws Exception {
		paramString = paramString.trim();
		IPResult localIPResult1 = new IPResult(paramString);
		RandomAccessFile localRandomAccessFile = null;
		try {
			Object localObject1;
			if ((paramString == null) || (paramString == "")) {
				localIPResult1.status = "EMPTY_IP_ADDRESS";
				return localIPResult1;
			}
			int i = 0;
			int j = 0;
			int k = 0;
			int m = 0;
			BigInteger localBigInteger1 = BigInteger.ZERO;
			long l1 = 0L;
			long l2 = 0L;
			try {
				BigInteger[] arrayOfBigInteger = ip2no(paramString);
				i = arrayOfBigInteger[0].intValue();
				localObject1 = arrayOfBigInteger[1];
			} catch (UnknownHostException localUnknownHostException) {
				localIPResult1.status = "INVALID_IP_ADDRESS";
				return localIPResult1;
			}
			checkLicense();

			localIPResult1.delay = isdelayed;

			long l3 = 0L;
			long l4 = 0L;
			long l5 = 0L;
			BigInteger localBigInteger2 = BigInteger.ZERO;
			BigInteger localBigInteger3 = BigInteger.ZERO;
			MetaData localMetaData;
			if (i == 4) {
				if (this.IPDatabasePath != "") {
					localRandomAccessFile = new RandomAccessFile(
							this.IPDatabasePath, "r");
					localMetaData = open(localRandomAccessFile, i);
					localBigInteger1 = MAX_IPv4_RANGE;
					l4 = localMetaData.getDBCount();
					j = localMetaData.getDBType();
					k = localMetaData.getBaseAddr();
					m = localMetaData.getDBColumn();
				}
			} else if ((i == 6) && (this.IPDatabasePathIPv6 != "")) {
				localRandomAccessFile = new RandomAccessFile(
						this.IPDatabasePathIPv6, "r");
				localMetaData = open(localRandomAccessFile, i);
				localBigInteger1 = MAX_IPv6_RANGE;
				l4 = localMetaData.getDBCountIPv6();
				j = localMetaData.getDBTypeIPv6();
				k = localMetaData.getBaseAddrIPv6();
				m = localMetaData.getDBColumnIPv6();
			}
			if (localRandomAccessFile == null) {
				localIPResult1.status = "MISSING_FILE";
				return localIPResult1;
			}
			if (((BigInteger) localObject1).compareTo(localBigInteger1) == 0) {
				localObject1 = ((BigInteger) localObject1)
						.subtract(BigInteger.ONE);
			}
			while (l3 <= l4) {
				l5 = (l3 + l4) / 2L;
				if (i == 4) {
					l1 = k + l5 * m * 4L;
					l2 = k + (l5 + 1L) * m * 4L;
				} else if (i == 6) {
					l1 = k + l5 * (16 + (m - 1) * 4);
					l2 = k + (l5 + 1L) * (16 + (m - 1) * 4);
				}
				localBigInteger2 = read32or128(l1, i, localRandomAccessFile);
				localBigInteger3 = read32or128(l2, i, localRandomAccessFile);
				if ((((BigInteger) localObject1).compareTo(localBigInteger2) >= 0)
						&& (((BigInteger) localObject1)
								.compareTo(localBigInteger3) < 0)) {
					if (i == 6) {
						l1 += 12L;
					}
					long l6;
					if (COUNTRY_POSITION[j] != 0) {
						l6 = read32(l1 + 4 * (COUNTRY_POSITION[j] - 1),
								localRandomAccessFile).longValue();
						localIPResult1.country_short = readStr(l6,
								localRandomAccessFile);
						l6 += 3L;
						localIPResult1.country_long = readStr(l6,
								localRandomAccessFile);
					} else {
						localIPResult1.country_short = "Not_Supported";
						localIPResult1.country_long = "Not_Supported";
					}
					if (REGION_POSITION[j] != 0) {
						l6 = read32(l1 + 4 * (REGION_POSITION[j] - 1),
								localRandomAccessFile).longValue();
						localIPResult1.region = readStr(l6,
								localRandomAccessFile);
					} else {
						localIPResult1.region = "Not_Supported";
					}
					if (CITY_POSITION[j] != 0) {
						l6 = read32(l1 + 4 * (CITY_POSITION[j] - 1),
								localRandomAccessFile).longValue();
						localIPResult1.city = readStr(l6, localRandomAccessFile);
					} else {
						localIPResult1.city = "Not_Supported";
					}
					if (ISP_POSITION[j] != 0) {
						l6 = read32(l1 + 4 * (ISP_POSITION[j] - 1),
								localRandomAccessFile).longValue();
						localIPResult1.isp = readStr(l6, localRandomAccessFile);
					} else {
						localIPResult1.isp = "Not_Supported";
					}
					if (LATITUDE_POSITION[j] != 0) {
						l6 = l1 + 4 * (LATITUDE_POSITION[j] - 1);
						localIPResult1.latitude = readFloat(l6,
								localRandomAccessFile);
					} else {
						localIPResult1.latitude = 0.0F;
					}
					if (LONGITUDE_POSITION[j] != 0) {
						l6 = l1 + 4 * (LONGITUDE_POSITION[j] - 1);
						localIPResult1.longitude = readFloat(l6,
								localRandomAccessFile);
					} else {
						localIPResult1.longitude = 0.0F;
					}
					if (DOMAIN_POSITION[j] != 0) {
						l6 = read32(l1 + 4 * (DOMAIN_POSITION[j] - 1),
								localRandomAccessFile).longValue();
						localIPResult1.domain = readStr(l6,
								localRandomAccessFile);
					} else {
						localIPResult1.domain = "Not_Supported";
					}
					if (ZIPCODE_POSITION[j] != 0) {
						l6 = read32(l1 + 4 * (ZIPCODE_POSITION[j] - 1),
								localRandomAccessFile).longValue();
						localIPResult1.zipcode = readStr(l6,
								localRandomAccessFile);
					} else {
						localIPResult1.zipcode = "Not_Supported";
					}
					if (TIMEZONE_POSITION[j] != 0) {
						l6 = read32(l1 + 4 * (TIMEZONE_POSITION[j] - 1),
								localRandomAccessFile).longValue();
						localIPResult1.timezone = readStr(l6,
								localRandomAccessFile);
					} else {
						localIPResult1.timezone = "Not_Supported";
					}
					if (NETSPEED_POSITION[j] != 0) {
						l6 = read32(l1 + 4 * (NETSPEED_POSITION[j] - 1),
								localRandomAccessFile).longValue();
						localIPResult1.netspeed = readStr(l6,
								localRandomAccessFile);
					} else {
						localIPResult1.netspeed = "Not_Supported";
					}
					if (IDDCODE_POSITION[j] != 0) {
						l6 = read32(l1 + 4 * (IDDCODE_POSITION[j] - 1),
								localRandomAccessFile).longValue();
						localIPResult1.iddcode = readStr(l6,
								localRandomAccessFile);
					} else {
						localIPResult1.iddcode = "Not_Supported";
					}
					if (AREACODE_POSITION[j] != 0) {
						l6 = read32(l1 + 4 * (AREACODE_POSITION[j] - 1),
								localRandomAccessFile).longValue();
						localIPResult1.areacode = readStr(l6,
								localRandomAccessFile);
					} else {
						localIPResult1.areacode = "Not_Supported";
					}
					if (WEATHERSTATIONCODE_POSITION[j] != 0) {
						l6 = read32(
								l1 + 4 * (WEATHERSTATIONCODE_POSITION[j] - 1),
								localRandomAccessFile).longValue();
						localIPResult1.weatherstationcode = readStr(l6,
								localRandomAccessFile);
					} else {
						localIPResult1.weatherstationcode = "Not_Supported";
					}
					if (WEATHERSTATIONNAME_POSITION[j] != 0) {
						l6 = read32(
								l1 + 4 * (WEATHERSTATIONNAME_POSITION[j] - 1),
								localRandomAccessFile).longValue();
						localIPResult1.weatherstationname = readStr(l6,
								localRandomAccessFile);
					} else {
						localIPResult1.weatherstationname = "Not_Supported";
					}
					if (MCC_POSITION[j] != 0) {
						l6 = read32(l1 + 4 * (MCC_POSITION[j] - 1),
								localRandomAccessFile).longValue();
						localIPResult1.mcc = readStr(l6, localRandomAccessFile);
					} else {
						localIPResult1.mcc = "Not_Supported";
					}
					if (MNC_POSITION[j] != 0) {
						l6 = read32(l1 + 4 * (MNC_POSITION[j] - 1),
								localRandomAccessFile).longValue();
						localIPResult1.mnc = readStr(l6, localRandomAccessFile);
					} else {
						localIPResult1.mnc = "Not_Supported";
					}
					if (MOBILEBRAND_POSITION[j] != 0) {
						l6 = read32(l1 + 4 * (MOBILEBRAND_POSITION[j] - 1),
								localRandomAccessFile).longValue();
						localIPResult1.mobilebrand = readStr(l6,
								localRandomAccessFile);
					} else {
						localIPResult1.mobilebrand = "Not_Supported";
					}
					if (ELEVATION_POSITION[j] != 0) {
						l6 = read32(l1 + 4 * (ELEVATION_POSITION[j] - 1),
								localRandomAccessFile).longValue();
						localIPResult1.elevation = convertFloat(readStr(l6,
								localRandomAccessFile));
					} else {
						localIPResult1.elevation = 0.0F;
					}
					if (USAGETYPE_POSITION[j] != 0) {
						l6 = read32(l1 + 4 * (USAGETYPE_POSITION[j] - 1),
								localRandomAccessFile).longValue();
						localIPResult1.usagetype = readStr(l6,
								localRandomAccessFile);
					} else {
						localIPResult1.usagetype = "Not_Supported";
					}
					localIPResult1.status = "OK";
					break;
				}
				if (((BigInteger) localObject1).compareTo(localBigInteger2) < 0) {
					l4 = l5 - 1L;
				} else {
					l3 = l5 + 1L;
				}
			}
			return localIPResult1;
		} catch (Exception localException1) {
			throw localException1;
		} finally {
			try {
				if (localRandomAccessFile != null) {
					localRandomAccessFile.close();
				}
			} catch (Exception localException6) {
			}
			localRandomAccessFile = null;
		}
	}

	private float convertFloat(String paramString) {
		try {
			return Float.parseFloat(paramString);
		} catch (NumberFormatException localNumberFormatException) {
		}
		return 0.0F;
	}

	public static void reverse(byte[] paramArrayOfByte) {
		if (paramArrayOfByte == null) {
			return;
		}
		int i = 0;
		int j = paramArrayOfByte.length - 1;
		while (j > i) {
			int k = paramArrayOfByte[j];
			paramArrayOfByte[j] = paramArrayOfByte[i];
			paramArrayOfByte[i] = (byte) k;
			j--;
			i++;
		}
	}

	private BigInteger read32or128(long paramLong, int paramInt,
			RandomAccessFile paramRandomAccessFile) throws IOException {
		if (paramInt == 4) {
			return read32(paramLong, paramRandomAccessFile);
		}
		if (paramInt == 6) {
			return read128(paramLong, paramRandomAccessFile);
		}
		return BigInteger.ZERO;
	}

	private BigInteger read128(long paramLong,
			RandomAccessFile paramRandomAccessFile) throws IOException {
		if (paramRandomAccessFile != null) {
			paramRandomAccessFile.seek(paramLong - 1L);
			byte[] arrayOfByte = new byte[16];
			for (int i = 0; i < arrayOfByte.length; i++) {
				arrayOfByte[i] = paramRandomAccessFile.readByte();
			}
			reverse(arrayOfByte);
			BigInteger localBigInteger = new BigInteger(1, arrayOfByte);

			return localBigInteger;
		}
		return BigInteger.ZERO;
	}

	private BigInteger read32(long paramLong,
			RandomAccessFile paramRandomAccessFile) throws IOException {
		if (paramRandomAccessFile != null) {
			paramRandomAccessFile.seek(paramLong - 1L);
			byte[] arrayOfByte = new byte[4];
			for (int i = 0; i < arrayOfByte.length; i++) {
				arrayOfByte[i] = paramRandomAccessFile.readByte();
			}
			reverse(arrayOfByte);
			BigInteger localBigInteger = new BigInteger(1, arrayOfByte);

			return localBigInteger;
		}
		return BigInteger.ZERO;
	}

	private int read8(long paramLong, RandomAccessFile paramRandomAccessFile)
			throws IOException {
		if (paramRandomAccessFile != null) {
			paramRandomAccessFile.seek(paramLong - 1L);

			return paramRandomAccessFile.read();
		}
		return 0;
	}

	private String readStr(long paramLong,
			RandomAccessFile paramRandomAccessFile) throws IOException {
		if (paramRandomAccessFile != null) {
			paramRandomAccessFile.seek(paramLong);

			int i = paramRandomAccessFile.read();
			char[] arrayOfChar = null;
			try {
				arrayOfChar = new char[i];
				for (int j = 0; j < i; j++) {
					arrayOfChar[j] = ((char) paramRandomAccessFile.read());
				}
			} catch (NegativeArraySizeException localNegativeArraySizeException) {
				return null;
			}
			return String.copyValueOf(arrayOfChar);
		}
		return "";
	}

	private float readFloat(long paramLong,
			RandomAccessFile paramRandomAccessFile) throws IOException {
		if (paramRandomAccessFile != null) {
			paramRandomAccessFile.seek(paramLong - 1L);
			int[] arrayOfInt = new int[4];
			for (int i = 0; i < arrayOfInt.length; i++) {
				arrayOfInt[i] = paramRandomAccessFile.read();
			}
			return Float.intBitsToFloat(arrayOfInt[3] << 24
					| arrayOfInt[2] << 16 | arrayOfInt[1] << 8 | arrayOfInt[0]);
		}
		return 0.0F;
	}

	private static BigInteger[] ip2no(String paramString)
			throws UnknownHostException {
		BigInteger localBigInteger1 = BigInteger.ZERO;
		BigInteger localBigInteger2 = BigInteger.ZERO;
		if (pattern.matcher(paramString).matches()) {
			localBigInteger1 = new BigInteger("4");
			localBigInteger2 = new BigInteger(
					String.valueOf(ipv4no(paramString)));
		} else {
			InetAddress localObject = InetAddress.getByName(paramString);
			byte[] arrayOfByte = ((InetAddress) localObject).getAddress();

			String str = "0";
			if ((localObject instanceof Inet6Address)) {
				str = "6";
			} else if ((localObject instanceof Inet4Address)) {
				str = "4";
			}
			localBigInteger1 = new BigInteger(str);
			localBigInteger2 = new BigInteger(1, arrayOfByte);
		}
		BigInteger[] localObject = { localBigInteger1, localBigInteger2 };

		return localObject;
	}

	private static long ipv4no(String paramString) {
		String[] arrayOfString = paramString.split("\\.");
		long l = 0L;
		for (int i = 0; i < arrayOfString.length; i++) {
			int j = 3 - i;
			l = (l + Integer.parseInt(arrayOfString[i]) % 256 * pow(256, j));
		}
		return l;
	}

	private static long pow(int i, int j) {
		long returnValue = 1;

		for (int k = 0; k < j; k++)
			returnValue *= i;
		return returnValue;
	}

	private void checkLicense() {
		if (!_alreadyCheckedKey) {
			if ((this.IPLicensePath == null) || (this.IPLicensePath == "")) {
				this.IPLicensePath = "license.key";
			}
			String str1 = "";
			String str2 = "";
			String str3 = "";
			try {
				BufferedReader localBufferedReader = new BufferedReader(
						new FileReader(this.IPLicensePath));
				if (!localBufferedReader.ready()) {
					throw new IOException();
				}
				if (((str1 = localBufferedReader.readLine()) != null)
						&& ((str2 = localBufferedReader.readLine()) != null)) {
					str3 = localBufferedReader.readLine();
				}
				localBufferedReader.close();
				if ((str3 == null) || (str3 == "")) {
					if (!str2.trim().equals(generateKey(str1))) {
						gotdelay = true;
					}
				} else if (!str3.trim().equals(generateKey(str1 + str2))) {
					gotdelay = true;
				}
				_alreadyCheckedKey = true;
			} catch (IOException localIOException) {
				gotdelay = true;
			}
		}
	}

	private static void delay() {
		if (gotdelay) {
			int i = 1 + (int) (Math.random() * 10.0D);
			try {
				if (i == 10) {
					isdelayed = true;
					Thread.sleep(5000L);
				} else {
					isdelayed = false;
				}
			} catch (Exception localException) {
				System.out.println(localException);
			}
		}
	}

	private static String generateKey(String paramString) {
		int i = 200;
		String[] arrayOfString1 = new String[2];
		String[] arrayOfString2 = new String[62];
		String[] arrayOfString3 = new String[i];

		String str = "error";
		try {
			if (paramString.length() > 20) {
				arrayOfString1[0] = paramString.substring(1, 20);
			} else {
				arrayOfString1[0] = paramString;
			}
			arrayOfString1[1] = "Hexasoft";
			int j = 0;
			for (int k = 48; k <= 57; k++) {
				arrayOfString2[j] = new Character((char) k).toString();
				j += 1;
			}
			for (int k = 65; k <= 90; k++) {
				arrayOfString2[j] = new Character((char) k).toString();
				j += 1;
			}
			for (int k = 97; k <= 122; k++) {
				arrayOfString2[j] = new Character((char) k).toString();
				j += 1;
			}
			for (int k = 0; k < i; k++) {
				if (k % 15 == 1) {
					arrayOfString3[k] = String.valueOf(Asc("0"));
				}
				if (k % 15 == 2) {
					arrayOfString3[k] = String.valueOf(Asc("a"));
				}
				if (k % 15 == 3) {
					arrayOfString3[k] = String.valueOf(Asc("1"));
				}
				if (k % 15 == 4) {
					arrayOfString3[k] = String.valueOf(Asc("b"));
				}
				if (k % 15 == 5) {
					arrayOfString3[k] = String.valueOf(Asc("2"));
				}
				if (k % 15 == 6) {
					arrayOfString3[k] = String.valueOf(Asc("c"));
				}
				if (k % 15 == 7) {
					arrayOfString3[k] = String.valueOf(Asc("3"));
				}
				if (k % 15 == 8) {
					arrayOfString3[k] = String.valueOf(Asc("d"));
				}
				if (k % 15 == 9) {
					arrayOfString3[k] = String.valueOf(Asc("4"));
				}
				if (k % 15 == 10) {
					arrayOfString3[k] = String.valueOf(Asc("e"));
				}
				if (k % 15 == 11) {
					arrayOfString3[k] = String.valueOf(Asc("5"));
				}
				if (k % 15 == 12) {
					arrayOfString3[k] = String.valueOf(Asc("f"));
				}
				if (k % 15 == 13) {
					arrayOfString3[k] = String.valueOf(Asc("6"));
				}
				if (k % 15 == 14) {
					arrayOfString3[k] = String.valueOf(Asc("g"));
				}
				if (k % 15 == 0) {
					arrayOfString3[k] = String.valueOf(Asc("7"));
				}
			}
			j = 0;
			for (int m = 0; m <= 1; m++) {
				for (int n = 0; n <= 11; n++) {
					for (int k = 0; k < arrayOfString1[m].length(); k++) {
						int tmp582_581 = (j % i);
						String[] tmp582_576 = arrayOfString3;
						tmp582_576[tmp582_581] = (tmp582_576[tmp582_581] + String
								.valueOf(Asc(arrayOfString1[m].substring(k,
										k + 1))));
						j += 1;
					}
					arrayOfString3[(j % i)] = String.valueOf(Long
							.parseLong(arrayOfString3[(j % i)]) * 2L);
					j += 1;
				}
			}
			str = "";
			for (int m = 0; m <= 15; m++) {
				str = str
						+ arrayOfString2[(Integer.parseInt(arrayOfString3[m]) % 62)];
			}
		} catch (Exception localException) {
			System.out.println(localException);
		}
		str = str.toUpperCase();
		str = str.substring(0, 4) + "-" + str.substring(4, 8) + "-"
				+ str.substring(8, 12) + "-" + str.substring(12, 16);
		return str;
	}

	private static int Asc(String paramString) {
		try {
			String str1 = " !\"#$%&'()*+'-./0123456789:;<=>?@";
			String str2 = "abcdefghijklmnopqrstuvwxyz";
			str1 = str1 + str2.toUpperCase();
			str1 = str1 + "[\\]^_`";
			str1 = str1 + str2;
			str1 = str1 + "{|}~";

			int i = str1.indexOf(paramString);
			if (i > -1) {
				return i + 32;
			}
		} catch (Exception localException) {
			System.out.println(localException);
		}
		return 0;
	}
}
