package daffodil.dsom

import org.scalatest.junit.JUnitSuite
import org.junit.Test
import daffodil.tdml.DFDLTestSuite
import daffodil.util.Misc
import daffodil.processors.InStreamFromByteChannel
import daffodil.processors._
import daffodil.compiler._

// Do no harm number 16 of 626 fail in regression, 154 in total of 797

class TestBinaryInput_01 extends JUnitSuite {

  var runner = {
    val testDir = "/test-suite/tresys-contributed/"
    val aa = testDir + "BinaryInput_01.tdml"
    lazy val runner = new DFDLTestSuite(Misc.getRequiredResource(aa))
    runner
  }

  /*** DFDL-334 ***/
  // Verify Bit Extraction
  @Test
  def testBufferBitExtraction() {
    var in = Compiler.stringToReadableByteChannel("3")
    val inStream = new InStreamFromByteChannel(null, in, 1)
    assert(inStream.getPartialByte(1, 3) == 3)
  }

  @Test
  def testBufferBitExtractionShift() {
    var in = Compiler.stringToReadableByteChannel("3")
    val inStream = new InStreamFromByteChannel(null, in, 1)
    assert(inStream.getPartialByte(1, 3, 2) == 12)
  }

  @Test
  def testBufferLeastSignificantBitExtractionShift() {
    var in = Compiler.stringToReadableByteChannel("4")
    val inStream = new InStreamFromByteChannel(null, in, 1)
    assert(inStream.getPartialByte(5, 3, 2) == 16)
  }

  // Verify aligned byte/short/int/long/bigint extraction
  @Test
  def testBufferByteBigEndianExtraction() {
    var in = Compiler.stringToReadableByteChannel("3")
    val inStream = new InStreamFromByteChannel(null, in, 1)
    assert(inStream.getBitSequence(0, 8, java.nio.ByteOrder.BIG_ENDIAN) == 51)
  }

  @Test
  def testBufferByteLittleEndianExtraction() {
    var in = Compiler.stringToReadableByteChannel("3")
    val inStream = new InStreamFromByteChannel(null, in, 1)
    assert(inStream.getBitSequence(0, 8, java.nio.ByteOrder.LITTLE_ENDIAN) == 51)
  }

  @Test
  def testBufferShortBigEndianExtraction() {
    var in = Compiler.stringToReadableByteChannel("Om")
    val inStream = new InStreamFromByteChannel(null, in, 2)
    assert(inStream.getBitSequence(0, 16, java.nio.ByteOrder.BIG_ENDIAN) == 20333)
  }

  @Test
  def testBufferShortLittleEndianExtraction() {
    var in = Compiler.stringToReadableByteChannel("Om")
    val inStream = new InStreamFromByteChannel(null, in, 2)
    assert(inStream.getBitSequence(0, 16, java.nio.ByteOrder.LITTLE_ENDIAN) == 27983)
  }

  @Test
  def testBufferIntBigEndianExtraction() {
    var in = Compiler.stringToReadableByteChannel("Help")
    val inStream = new InStreamFromByteChannel(null, in, 4)
    assert(inStream.getBitSequence(0, 32, java.nio.ByteOrder.BIG_ENDIAN) == 1214606448)
  }

  @Test
  def testBufferIntLittleEndianExtraction() {
    var in = Compiler.stringToReadableByteChannel("Help")
    val inStream = new InStreamFromByteChannel(null, in, 4)
    assert(inStream.getBitSequence(0, 32, java.nio.ByteOrder.LITTLE_ENDIAN) == 1886152008)
  }

  @Test
  def testBufferLongBigEndianExtraction() {
    var in = Compiler.stringToReadableByteChannel("Harrison")
    val inStream = new InStreamFromByteChannel(null, in, 8)
    assert(inStream.getBitSequence(0, 64, java.nio.ByteOrder.BIG_ENDIAN).toString == "5215575679192756078")
  }

  @Test
  def testBufferLongLittleEndianExtraction() {
    var in = Compiler.stringToReadableByteChannel("Harrison")
    val inStream = new InStreamFromByteChannel(null, in, 8)
    assert(inStream.getBitSequence(0, 64, java.nio.ByteOrder.LITTLE_ENDIAN).toString == "7957705963315814728")
  }

  @Test
  def testBufferBigIntBigEndianExtraction() {
    var in = Compiler.stringToReadableByteChannel("Something in the way she moves, ")
    val inStream = new InStreamFromByteChannel(null, in, 32)
    assert(inStream.getBitSequence(0, 256, java.nio.ByteOrder.BIG_ENDIAN).toString ==
      "37738841482167102822784581157237036764884875846207476558974346160344516471840")
  }

  @Test
  def testBufferBigIntLittleEndianExtraction() {
    var in = Compiler.stringToReadableByteChannel("Something in the way she moves, ")
    val inStream = new InStreamFromByteChannel(null, in, 32)
    assert(inStream.getBitSequence(0, 256, java.nio.ByteOrder.LITTLE_ENDIAN).toString ==
      "14552548861771956163454220823873430243364312915206513831353612029437431082835")
  }

  // Aligned but not full string
  @Test
  def testBufferPartialIntBigEndianExtraction() {
    var in = Compiler.stringToReadableByteChannel("SBT")
    val inStream = new InStreamFromByteChannel(null, in, 3)
    assert(inStream.getBitSequence(0, 24, java.nio.ByteOrder.BIG_ENDIAN) == 5456468)
  }

  @Test
  def testBufferPartialIntLittleEndianExtraction() {
    var in = Compiler.stringToReadableByteChannel("SBT")
    val inStream = new InStreamFromByteChannel(null, in, 3)
    assert(inStream.getBitSequence(0, 24, java.nio.ByteOrder.LITTLE_ENDIAN) == 5522003)
  }

  // Non-Aligned 1 Byte or less
  @Test
  def testBufferBitNumberBigEndianExtraction() {
    var in = Compiler.stringToReadableByteChannel("3")
    val inStream = new InStreamFromByteChannel(null, in, 1)
    assert(inStream.getBitSequence(1, 3, java.nio.ByteOrder.BIG_ENDIAN) == 3)
  }

  @Test
  def testBufferBitNumberLittleEndianExtraction() {
    var in = Compiler.stringToReadableByteChannel("3")
    val inStream = new InStreamFromByteChannel(null, in, 1)
    assert(inStream.getBitSequence(1, 3, java.nio.ByteOrder.LITTLE_ENDIAN) == 3)
  }

  @Test
  def testBufferBitByteBigEndianExtraction() {
    var in = Compiler.stringToReadableByteChannel("3>")
    val inStream = new InStreamFromByteChannel(null, in, 2)
    assert(inStream.getBitSequence(2, 8, java.nio.ByteOrder.BIG_ENDIAN) == 204)
  }

  @Test
  def testBufferBitByteLittleEndianExtraction() {
    var in = Compiler.stringToReadableByteChannel("3>")
    val inStream = new InStreamFromByteChannel(null, in, 2)
    assert(inStream.getBitSequence(2, 8, java.nio.ByteOrder.LITTLE_ENDIAN) == 204)
  }

  // Non-Aligned multi-byte
  @Test
  def testBufferPartialInt22At0BigEndianExtraction() {
    var in = Compiler.stringToReadableByteChannel("SBT")
    val inStream = new InStreamFromByteChannel(null, in, 3)
    assert(inStream.getBitSequence(0, 22, java.nio.ByteOrder.BIG_ENDIAN) == 1364117)
  }

  @Test
  def testBufferPartialInt22At0LittleEndianExtraction() {
    var in = Compiler.stringToReadableByteChannel("SBT")
    val inStream = new InStreamFromByteChannel(null, in, 3)
    assert(inStream.getBitSequence(0, 22, java.nio.ByteOrder.LITTLE_ENDIAN) == 1393235)
  }


  @Test
  def testBufferPartialInt22At2BigEndianExtraction() {
    var in = Compiler.stringToReadableByteChannel("SBT")
    val inStream = new InStreamFromByteChannel(null, in, 3)
    assert(inStream.getBitSequence(2, 22, java.nio.ByteOrder.BIG_ENDIAN) == 1262164)
  }

  @Test
  def testBufferPartialInt22At2LittleEndianExtraction() {
    var in = Compiler.stringToReadableByteChannel("SBT")
    val inStream = new InStreamFromByteChannel(null, in, 3)
    assert(inStream.getBitSequence(2, 22, java.nio.ByteOrder.LITTLE_ENDIAN) == 1313101)
  }

  /*** DFDL-307 ***/
  @Test
  def test_one_octet() {
    runner.runOneTest("OneOctetBinaryParse")
  }
}