package TBEx

// ------- Imports START -- DO NOT MODIFY BELOW
import Chisel.{Complex => _, Mux => _, Reg => _, RegNext => _, RegInit => _, Pipe => _, Mem => _, SeqMem => _,
               Module => _, ModuleOverride => _, when => _, switch => _, is => _, unless => _, Round => _,  _}
import ChiselDSP._
// ------- Imports END -- OK TO MODIFY BELOW

class CountIO extends IOBundle {
  val upDown = DSPBool(INPUT)
  val addVal = DSPUInt(INPUT,255)
  val count = DSPUInt(OUTPUT,255)
}

class TestMod extends DSPModule{
  override val io = new IOBundle{
    val in = UInt(INPUT,width=4)
    val out = UInt(OUTPUT,width=4)
  }
  io.out := io.in - UInt(1)
}

/** Module that supports both fixed and floating point testing */
class TBEx[T <: DSPQnm[T]](gen : => T) extends GenDSPModule (gen) {

  override val io = new CountIO
  val io2 = new CountIO
  val io3 = new CountIO

  val o = new IOBundle{
    val count2 = UInt(OUTPUT,width=8)
    val count = DSPUInt(OUTPUT,255)
    val count3 = SInt(OUTPUT,width=8)
    val count4 = DSPSInt(OUTPUT,(-128,127))
    val fix = gen.asOutput
    val out = UInt(OUTPUT,width=5)
  }

  val i = new IOBundle{
    val fix = gen.asInput
    val in = UInt(INPUT,width=5)
  }

  val testMod = DSPModule(new TestMod)

  val outTemp = UInt(width=5)
  testMod.io.in := i.in
  outTemp := testMod.io.out
  o.out := outTemp - UInt(1,width=5)

  val newCount = Mux(io.upDown,io.count +% io.addVal, io.count - io.addVal)
  io.count := Mux(DSPBool(reset),DSPUInt(0),newCount).reg()

  io2.count := io.count
  o.count2 := io.count.toUInt
  o.count := io.count
  io3.count := io.count
  o.count3 := io.count.toSInt
  o.count4 := DSPSInt(o.count3,(-128,127))

  o.fix := i.fix.reg()

}
