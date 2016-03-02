/** DSPSInt is a special case of DSPFixed */

package ChiselDSP
import Chisel._

object DSPSInt {

  /** Convert SInt to a DSPSInt by reinterpreting the Bits */
  def apply(s:SInt, range: => (Int,Int)): DSPFixed = apply(s,(BigInt(range._1),BigInt(range._2)))
  def apply(s:SInt, range:(BigInt,BigInt)): DSPFixed = DSPFixed(s,0,range)

  /** Creates a DSPSInt object from a constant BigInt (or Int casted to BigInt) */
  def apply(x: BigInt): DSPFixed = DSPFixed(x,(x.bitLength,0))
  def apply(x: BigInt, range: => (Int,Int)): DSPFixed = DSPSInt(x,(BigInt(range._1),BigInt(range._2)))
  def apply(x: BigInt, range: (BigInt,BigInt)) = {
    val out = DSPFixed(x,(DSPFixed.rangeToWidth(range)-1,0))
    out.updateLimits(range)
    out
  }

  /** Create a DSPSInt object with a specified IODirection and range */
  def apply(dir:IODirection, range: => (Int,Int)): DSPFixed = apply(dir,(BigInt(range._1),BigInt(range._2)))
  def apply(dir: IODirection, range: (BigInt,BigInt)): DSPFixed = DSPFixed(dir,0,range)

  // TODO: Add shorten equivalent for Fixed to solve SInt problem

}