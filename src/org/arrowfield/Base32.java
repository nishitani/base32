/**
 *
 */
package org.arrowfield;

import java.util.Arrays;

/**
 * BASE32エンコーダー。BASE32は、「a-z」「A-Z」「0-9」のうち、アルファベットの大文字小文字を区別せず、
 * 読み間違いしやすい「0」「O」「1」「I」を除いた32文字でエンコードする。5バイトが8文字になる。
 * 
 * @author m-nishitani@nri.co.jp
 * 
 */
public class Base32 {
	/**
	 * エンコードに使う文字リスト。
	 */
	protected static final char[] chars32 = { 'A', 'B', 'C', 'D', 'E', 'F',
			'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U',
			'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9' };

	/**
	 * エンコード後の1文字から整数に変換する。
	 * 
	 * @param in
	 *            エンコード後の文字列に含まれる文字
	 * @return 変換後の値を int 型で返す。
	 */
	private static int char2int(char in) {
		in = Character.toLowerCase(in);
		switch (in) {
		case 'a':
			return 0;
		case 'b':
			return 1;
		case 'c':
			return 2;
		case 'd':
			return 3;
		case 'e':
			return 4;
		case 'f':
			return 5;
		case 'g':
			return 6;
		case 'h':
			return 7;
		case 'j':
			return 8;
		case 'k':
			return 9;
		case 'l':
			return 10;
		case 'm':
			return 11;
		case 'n':
			return 12;
		case 'p':
			return 13;
		case 'q':
			return 14;
		case 'r':
			return 15;
		case 's':
			return 16;
		case 't':
			return 17;
		case 'u':
			return 18;
		case 'v':
			return 19;
		case 'w':
			return 20;
		case 'x':
			return 21;
		case 'y':
			return 22;
		case 'z':
			return 23;
		case '2':
			return 24;
		case '3':
			return 25;
		case '4':
			return 26;
		case '5':
			return 27;
		case '6':
			return 28;
		case '7':
			return 29;
		case '8':
			return 30;
		case '9':
			return 31;
		case ':':
		case ';':
		case '<':
		case '=':
		case '>':
		case '?':
		case '@':
		case 'A':
		case 'B':
		case 'C':
		case 'D':
		case 'E':
		case 'F':
		case 'G':
		case 'H':
		case 'I':
		case 'J':
		case 'K':
		case 'L':
		case 'M':
		case 'N':
		case 'O':
		case 'P':
		case 'Q':
		case 'R':
		case 'S':
		case 'T':
		case 'U':
		case 'V':
		case 'W':
		case 'X':
		case 'Y':
		case 'Z':
		case '[':
		case '\\':
		case ']':
		case '^':
		case '_':
		case '`':
		case 'i':
		case 'o':
		}
		return -1;
	}

	/**
	 * BASE32にエンコードする
	 * 
	 * @param エンコードする平文
	 * @return エンコードされたBase32文字列
	 */
	public static String encode(byte[] input) {
		byte[] binary = input.length % 5 == 0 ? input : Arrays.copyOf(input,
				input.length + 5 - input.length % 5);
		int cursor = 0;

		char[] result = new char[binary.length / 5 * 8];
		while ((cursor + 1) * 5 <= binary.length) {
			long pack = (((long) binary[cursor * 5] << 32) & 0x000000ff00000000l)
					| (((long) binary[cursor * 5 + 1] << 24) & 0x00000000ff000000l)
					| (((long) binary[cursor * 5 + 2] << 16) & 0x0000000000ff0000l)
					| (((long) binary[cursor * 5 + 3] << 8) & 0x000000000000ff00l)
					| ((long) binary[cursor * 5 + 4] & 0x00000000000000ffl);
			result[cursor * 8] = chars32[(int) (pack >>> 35) & 0x0000001f];
			result[cursor * 8 + 1] = chars32[(int) (pack >>> 30) & 0x0000001f];
			result[cursor * 8 + 2] = chars32[(int) (pack >>> 25) & 0x0000001f];
			result[cursor * 8 + 3] = chars32[(int) (pack >>> 20) & 0x0000001f];
			result[cursor * 8 + 4] = chars32[(int) (pack >>> 15) & 0x0000001f];
			result[cursor * 8 + 5] = chars32[(int) (pack >>> 10) & 0x0000001f];
			result[cursor * 8 + 6] = chars32[(int) (pack >>> 5) & 0x0000001f];
			result[cursor * 8 + 7] = chars32[(int) pack & 0x0000001f];
			cursor++;
		}

		switch (input.length % 5) {
		case 0:
			return new String(result);
		case 1:
			return new String(Arrays.copyOf(result, result.length - 6));
		case 2:
			return new String(Arrays.copyOf(result, result.length - 5));
		case 3:
			return new String(Arrays.copyOf(result, result.length - 3));
		case 4:
			return new String(Arrays.copyOf(result, result.length - 1));
		}
		return null;
	}

	/**
	 * BASE32をデコードする。パディングはあってはならない。認識できない文字は「0」となる。引数の文字長が不正の場合はnullが帰る。
	 * 
	 * @param エンコードされた文字列
	 * @return デコードした平文
	 */
	public static byte[] decode(String encoded) {
		String input = encoded.length() % 8 == 0 ? encoded : encoded
				+ "0000000".substring(0, 8 - encoded.length() % 8);
		int cursor = 0;
		byte[] result = new byte[input.length() / 8 * 5];
		while (input.length() > cursor * 8) {
			result[cursor * 5] = (byte) ((char2int(input.charAt(cursor * 8)) << 3 & 0x000000f8) | (char2int(input
					.charAt(cursor * 8 + 1)) >>> 2 & 0x00000007));
			result[cursor * 5 + 1] = (byte) ((char2int(input
					.charAt(cursor * 8 + 1)) << 6 & 0x000000c0)
					| (char2int(input.charAt(cursor * 8 + 2)) << 1 & 0x00000003e) | (char2int(input
					.charAt(cursor * 8 + 3)) >>> 4 & 0x00000001));
			result[cursor * 5 + 2] = (byte) ((char2int(input
					.charAt(cursor * 8 + 3)) << 4 & 0x000000f0) | (char2int(input
					.charAt(cursor * 8 + 4)) >>> 1 & 0x0000000f));
			result[cursor * 5 + 3] = (byte) ((char2int(input
					.charAt(cursor * 8 + 4)) << 7 & 0x00000080)
					| (char2int(input.charAt(cursor * 8 + 5)) << 2 & 0x00000007c) | (char2int(input
					.charAt(cursor * 8 + 6)) >>> 3 & 0x00000003));
			result[cursor * 5 + 4] = (byte) ((char2int(input
					.charAt(cursor * 8 + 6)) << 5 & 0x000000e0) | (char2int(input
					.charAt(cursor * 8 + 7)) & 0x0000001f));
			cursor++;
		}
		switch (encoded.length() % 8) {
		case 0:
			return result;
		case 1:
			return null;
		case 2:
			return Arrays.copyOf(result, result.length - 4);
		case 3:
			return Arrays.copyOf(result, result.length - 3);
		case 4:
			return null;
		case 5:
			return Arrays.copyOf(result, result.length - 2);
		case 6:
			return null;
		case 7:
			return Arrays.copyOf(result, result.length - 1);
		}
		return null;

	}

	public static void main(String[] args) {
		{
			String[] in = { "ああああああああああ", "01", "012", "0123", "01234",
					"012345", "0123456", "01234567", "012345678", "0123456789",
					"01234567890" };
			for (String p : in) {
				System.out.println("P:" + p);
				String e = encode(p.getBytes());
				System.out.println("E:" + e);
				System.out.println("R:" + new String(decode(e)));
				System.out.println("----------");
			}
		}

	}
}
