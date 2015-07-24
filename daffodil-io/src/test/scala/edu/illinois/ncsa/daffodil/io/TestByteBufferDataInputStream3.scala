package edu.illinois.ncsa.daffodil.io

import org.junit.Test
import org.junit.Assert._
import edu.illinois.ncsa.daffodil.util.Misc

class TestByteBufferDataInputStream3 {

  val Dump = new DataDumper

  @Test def dumpVisible1 {
    val bytes = "Date 年月日=2003年08月27日".getBytes("utf-8")
    val lengthInBits = bytes.length * 8
    val dis = ByteBufferDataInputStream(bytes)
    dis.setDebugging(true)
    val fb = dis.futureData(48)

    val dumpString =
      Dump.dump(Dump.MixedHexLTR(Some("utf-8")), 0, lengthInBits, fb,
        includeHeadingLine = true).mkString("\n")
    val expected = """
87654321  0011 2233 4455 6677 8899 aabb ccdd eeff  0~1~2~3~4~5~6~7~8~9~a~b~c~d~e~f~
00000000: 4461 7465 20e5 b9b4 e69c 88e6 97a5 3d32  D~a~t~e~␣~年~~~~月~~~~日~~~~=~2~
00000010: 3030 33e5 b9b4 3038 e69c 8832 37e6 97a5  0~0~3~年~~~~0~8~月~~~~2~7~日~~~~
"""
    assertEquals(expected, "\n" + dumpString + "\n")
  }

  @Test def dumpVisible2 {
    val bytes = "datadatadatadataDate 年月日=2003年08月27日".getBytes("utf-8")
    val lengthInBits = bytes.length * 8 - (16 * 8)
    val dis = ByteBufferDataInputStream(bytes)
    dis.setDebugging(true)
    val fb = dis.futureData(48)

    val dumpString =
      Dump.dump(Dump.MixedHexLTR(Some("utf-8")), 16 * 8, lengthInBits, fb,
        includeHeadingLine = true).mkString("\n")
    val expected = """
87654321  0011 2233 4455 6677 8899 aabb ccdd eeff  0~1~2~3~4~5~6~7~8~9~a~b~c~d~e~f~
00000010: 4461 7465 20e5 b9b4 e69c 88e6 97a5 3d32  D~a~t~e~␣~年~~~~月~~~~日~~~~=~2~
00000020: 3030 33e5 b9b4 3038 e69c 8832 37e6 97a5  0~0~3~年~~~~0~8~月~~~~2~7~日~~~~
"""
    assertEquals(expected, "\n" + dumpString + "\n")
  }

  @Test def dumpVisible3 {
    val bytes = "datadatadatadatadataDate 年月日=2003年08月27日".getBytes("utf-8")
    val lengthInBits = bytes.length * 8 - (20 * 8)
    val dis = ByteBufferDataInputStream(bytes)
    dis.setDebugging(true)
    val fb = dis.futureData(bytes.length)

    val dumpString =
      Dump.dump(Dump.MixedHexLTR(Some("utf-8")), 20 * 8, lengthInBits, fb,
        includeHeadingLine = true).mkString("\n")
    val expected = """
87654321  0011 2233 4455 6677 8899 aabb ccdd eeff  0~1~2~3~4~5~6~7~8~9~a~b~c~d~e~f~
00000010:           4461 7465 20e5 b9b4 e69c 88e6          D~a~t~e~␣~年~~~~月~~~~日
00000020: 97a5 3d32 3030 33e5 b9b4 3038 e69c 8832  ~~~~=~2~0~0~3~年~~~~0~8~月~~~~2~
00000030: 37e6 97a5                                7~日~~~~                        
"""
    assertEquals(expected, "\n" + dumpString + "\n")
  }
}