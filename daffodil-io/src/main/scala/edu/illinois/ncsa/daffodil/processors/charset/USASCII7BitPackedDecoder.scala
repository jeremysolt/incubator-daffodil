/* Copyright (c) 2012-2015 Tresys Technology, LLC. All rights reserved.
 *
 * Developed by: Tresys Technology, LLC
 *               http://www.tresys.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal with
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 *  1. Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimers.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimers in the
 *     documentation and/or other materials provided with the distribution.
 *
 *  3. Neither the names of Tresys Technology, nor the names of its contributors
 *     may be used to endorse or promote products derived from this Software
 *     without specific prior written permission.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS WITH THE
 * SOFTWARE.
 */

package edu.illinois.ncsa.daffodil.processors.charset

import java.nio.CharBuffer
import java.nio.charset.Charset
import java.nio.charset.CharsetDecoder
import java.nio.charset.CharsetEncoder

import edu.illinois.ncsa.daffodil.io.NonByteSizeCharset
import edu.illinois.ncsa.daffodil.io.NonByteSizeCharsetDecoder
import edu.illinois.ncsa.daffodil.io.NonByteSizeCharsetEncoder
import edu.illinois.ncsa.daffodil.schema.annotation.props.gen.BitOrder
import edu.illinois.ncsa.daffodil.util.MaybeInt

/**
 * Some encodings are not byte-oriented.
 *
 * X-DFDL-US-ASCII-7-BIT-PACKED occupies only 7 bits with each
 * code unit.
 *
 * There are 6 bit and 5 bit encodings in use as well. (One can even think of hexadecimal as
 * a 4-bit encoding of 16 possible characters - might be a cool way to
 * implement packed decimals of various sorts.)
 */

trait USASCII7BitPackedCharsetMixin
  extends NonByteSizeCharset {

  val bitWidthOfACodeUnit = 7 // in units of bits
  val requiredBitOrder = BitOrder.LeastSignificantBitFirst
}

object USASCII7BitPackedCharset
  extends java.nio.charset.Charset("X-DFDL-US-ASCII-7-BIT-PACKED", Array("US-ASCII-7-BIT-PACKED"))
  with USASCII7BitPackedCharsetMixin {

  def contains(cs: Charset): Boolean = false

  def newDecoder(): CharsetDecoder = new USASCII7BitPackedDecoder

  def newEncoder(): CharsetEncoder = new USASCII7BitPackedEncoder

  private[charset] def charsPerByte = 8.0F / 7.0F
  private[charset] def bytesPerChar = 1.0F // can't use 7/8 here because CharsetEncoder base class requires it to be 1 or greater.
}

/**
 * You have to initialize one of these for a specific ByteBuffer because
 * the encoding is 7-bits wide, so we need additional state beyond just
 * the byte position and limit that a ByteBuffer provides in order to
 * properly sequence through the data.
 */
class USASCII7BitPackedDecoder
  extends java.nio.charset.CharsetDecoder(USASCII7BitPackedCharset,
    USASCII7BitPackedCharset.charsPerByte, // average
    USASCII7BitPackedCharset.charsPerByte) // maximum
  with NonByteSizeCharsetDecoder
  with USASCII7BitPackedCharsetMixin {

  def output(charCode: Int, out: CharBuffer) {
    val char = charCode.toChar
    out.put(char)
  }

}

class USASCII7BitPackedEncoder
  extends java.nio.charset.CharsetEncoder(USASCII7BitPackedCharset,
    USASCII7BitPackedCharset.bytesPerChar, // average
    USASCII7BitPackedCharset.bytesPerChar) // maximum
  with NonByteSizeCharsetEncoder
  with USASCII7BitPackedCharsetMixin {

  val replacementChar = 0x3F

  def charToCharCode(char: Char): MaybeInt = {
    val charAsInt = char.toInt
    if (charAsInt <= 127) MaybeInt(charAsInt)
    else MaybeInt.Nope
  }

}
