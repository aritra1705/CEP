// See LICENSE for license details.
package sifive.freedom.unleashed

import freechips.rocketchip.config._
import freechips.rocketchip.subsystem._
import freechips.rocketchip.devices.debug._
import freechips.rocketchip.devices.tilelink._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.system._
import freechips.rocketchip.tile._

import sifive.blocks.devices.gpio._
import sifive.blocks.devices.spi._
import sifive.blocks.devices.uart._

import mitllBlocks.cep_addresses._
import mitllBlocks.cep_registers._

import mitllBlocks.aes._
import mitllBlocks.rsa._
import mitllBlocks.sha256._
import mitllBlocks.fir._
import mitllBlocks.iir._
import mitllBlocks.idft._
import mitllBlocks.des3._
import mitllBlocks.md5._
import mitllBlocks.gps._
import mitllBlocks.dft._
import mitllBlocks.srot._
import mitllBlocks.scratchpad._

// Default FreedomU500Config
class FreedomU500Config extends Config(
  new WithJtagDTM            ++
  new WithNMemoryChannels(1) ++
  new WithNBigCores(4)       ++
  new WithoutTLMonitors      ++
  new WithDTS("mit-ll,rocketchip-cep", Nil) ++
  new BaseConfig
)

// Freedom U500 Dev Kit Peripherals
class U500DevKitPeripherals extends Config((site, here, up) => {
  case PeripheryUARTKey => List(
    UARTParams(address = BigInt(0x64000000L)))
  case PeripherySPIKey => List(
    SPIParams(rAddress = BigInt(0x64001000L)))
  case PeripheryGPIOKey => List(
    GPIOParams(address = BigInt(0x64002000L), width = 8))
  case PeripheryMaskROMKey => List(
    MaskROMParams(address = 0x10000, name = "BootROM", depth = 0x2000)) // 8K x 32-bits  words
  case PeripheryDES3Key => List(
    COREParams(
      slave_base_addr     = BigInt(CEPBaseAddresses.des3_base_addr),
      slave_depth         = BigInt(CEPBaseAddresses.des3_depth),
      llki_base_addr      = BigInt(CEPBaseAddresses.des3_llki_base_addr),
      llki_depth          = BigInt(CEPBaseAddresses.des3_llki_depth),
      llki_ctrlsts_addr   = BigInt(CEPBaseAddresses.des3_llki_ctrlsts_addr),
      llki_sendrecv_addr  = BigInt(CEPBaseAddresses.des3_llki_sendrecv_addr),
      dev_name            = s"des3"
    ))
  case PeripheryAESKey => List(
    COREParams(
      slave_base_addr     = BigInt(CEPBaseAddresses.aes_base_addr),
      slave_depth         = BigInt(CEPBaseAddresses.aes_depth),
      llki_base_addr      = BigInt(CEPBaseAddresses.aes_llki_base_addr),
      llki_depth          = BigInt(CEPBaseAddresses.aes_llki_depth),
      llki_ctrlsts_addr   = BigInt(CEPBaseAddresses.aes_llki_ctrlsts_addr),
      llki_sendrecv_addr  = BigInt(CEPBaseAddresses.aes_llki_sendrecv_addr),
      dev_name            = s"aes"
    ))
  case PeripheryIIRKey => List(
    COREParams(
      slave_base_addr     = BigInt(CEPBaseAddresses.iir_base_addr),
      slave_depth         = BigInt(CEPBaseAddresses.iir_depth),
      llki_base_addr      = BigInt(CEPBaseAddresses.iir_llki_base_addr),
      llki_depth          = BigInt(CEPBaseAddresses.iir_llki_depth),
      llki_ctrlsts_addr   = BigInt(CEPBaseAddresses.iir_llki_ctrlsts_addr),
      llki_sendrecv_addr  = BigInt(CEPBaseAddresses.iir_llki_sendrecv_addr),
      dev_name            = s"iir"
    ))
  case PeripheryIDFTKey => List(
    COREParams(
      slave_base_addr     = BigInt(CEPBaseAddresses.idft_base_addr),
      slave_depth         = BigInt(CEPBaseAddresses.idft_depth),
      llki_base_addr      = BigInt(CEPBaseAddresses.idft_llki_base_addr),
      llki_depth          = BigInt(CEPBaseAddresses.idft_llki_depth),
      llki_ctrlsts_addr   = BigInt(CEPBaseAddresses.idft_llki_ctrlsts_addr),
      llki_sendrecv_addr  = BigInt(CEPBaseAddresses.idft_llki_sendrecv_addr),
      dev_name            = s"idft"
    ))
  case PeripheryGPSKey => List(
    COREParams(
      slave_base_addr     = BigInt(CEPBaseAddresses.gps_0_base_addr),
      slave_depth         = BigInt(CEPBaseAddresses.gps_0_depth),
      llki_base_addr      = BigInt(CEPBaseAddresses.gps_0_llki_base_addr),
      llki_depth          = BigInt(CEPBaseAddresses.gps_0_llki_depth),
      llki_ctrlsts_addr   = BigInt(CEPBaseAddresses.gps_0_llki_ctrlsts_addr),
      llki_sendrecv_addr  = BigInt(CEPBaseAddresses.gps_0_llki_sendrecv_addr),
      dev_name            = s"gps_0",
      verilog_module_name = Some(s"gps_mock_tss")
    ),
    COREParams(
      slave_base_addr     = BigInt(CEPBaseAddresses.gps_1_base_addr),
      slave_depth         = BigInt(CEPBaseAddresses.gps_1_depth),
      llki_base_addr      = BigInt(CEPBaseAddresses.gps_1_llki_base_addr),
      llki_depth          = BigInt(CEPBaseAddresses.gps_1_llki_depth),
      llki_ctrlsts_addr   = BigInt(CEPBaseAddresses.gps_1_llki_ctrlsts_addr),
      llki_sendrecv_addr  = BigInt(CEPBaseAddresses.gps_1_llki_sendrecv_addr),
      dev_name            = s"gps_1",
      verilog_module_name = Some(s"gps_mock_tss")
    ),
    COREParams(
      slave_base_addr     = BigInt(CEPBaseAddresses.gps_2_base_addr),
      slave_depth         = BigInt(CEPBaseAddresses.gps_2_depth),
      llki_base_addr      = BigInt(CEPBaseAddresses.gps_2_llki_base_addr),
      llki_depth          = BigInt(CEPBaseAddresses.gps_2_llki_depth),
      llki_ctrlsts_addr   = BigInt(CEPBaseAddresses.gps_2_llki_ctrlsts_addr),
      llki_sendrecv_addr  = BigInt(CEPBaseAddresses.gps_2_llki_sendrecv_addr),
      dev_name            = s"gps_2",
      verilog_module_name = Some(s"gps_mock_tss")
    ),
    COREParams(
      slave_base_addr     = BigInt(CEPBaseAddresses.gps_3_base_addr),
      slave_depth         = BigInt(CEPBaseAddresses.gps_3_depth),
      llki_base_addr      = BigInt(CEPBaseAddresses.gps_3_llki_base_addr),
      llki_depth          = BigInt(CEPBaseAddresses.gps_3_llki_depth),
      llki_ctrlsts_addr   = BigInt(CEPBaseAddresses.gps_3_llki_ctrlsts_addr),
      llki_sendrecv_addr  = BigInt(CEPBaseAddresses.gps_3_llki_sendrecv_addr),
      dev_name            = s"gps_3",
      verilog_module_name = Some(s"gps_mock_tss")
    ))
  case PeripheryMD5Key => List(
    COREParams(
      slave_base_addr     = BigInt(CEPBaseAddresses.md5_base_addr),
      slave_depth         = BigInt(CEPBaseAddresses.md5_depth),
      llki_base_addr      = BigInt(CEPBaseAddresses.md5_llki_base_addr),
      llki_depth          = BigInt(CEPBaseAddresses.md5_llki_depth),
      llki_ctrlsts_addr   = BigInt(CEPBaseAddresses.md5_llki_ctrlsts_addr),
      llki_sendrecv_addr  = BigInt(CEPBaseAddresses.md5_llki_sendrecv_addr),
      dev_name            = s"md5"
    ))
  case PeripheryDFTKey => List(
    COREParams(
      slave_base_addr     = BigInt(CEPBaseAddresses.dft_base_addr),
      slave_depth         = BigInt(CEPBaseAddresses.dft_depth),
      llki_base_addr      = BigInt(CEPBaseAddresses.dft_llki_base_addr),
      llki_depth          = BigInt(CEPBaseAddresses.dft_llki_depth),
      llki_ctrlsts_addr   = BigInt(CEPBaseAddresses.dft_llki_ctrlsts_addr),
      llki_sendrecv_addr  = BigInt(CEPBaseAddresses.dft_llki_sendrecv_addr),
      dev_name            = s"dft"
    ))
  case PeripheryFIRKey => List(
    COREParams(
      slave_base_addr     = BigInt(CEPBaseAddresses.fir_base_addr),
      slave_depth         = BigInt(CEPBaseAddresses.fir_depth),
      llki_base_addr      = BigInt(CEPBaseAddresses.fir_llki_base_addr),
      llki_depth          = BigInt(CEPBaseAddresses.fir_llki_depth),
      llki_ctrlsts_addr   = BigInt(CEPBaseAddresses.fir_llki_ctrlsts_addr),
      llki_sendrecv_addr  = BigInt(CEPBaseAddresses.fir_llki_sendrecv_addr),
      dev_name            = s"fir"
    ))
  case PeripherySHA256Key => List(
    COREParams(
      slave_base_addr     = BigInt(CEPBaseAddresses.sha256_0_base_addr),
      slave_depth         = BigInt(CEPBaseAddresses.sha256_0_depth),
      llki_base_addr      = BigInt(CEPBaseAddresses.sha256_0_llki_base_addr),
      llki_depth          = BigInt(CEPBaseAddresses.sha256_0_llki_depth),
      llki_ctrlsts_addr   = BigInt(CEPBaseAddresses.sha256_0_llki_ctrlsts_addr),
      llki_sendrecv_addr  = BigInt(CEPBaseAddresses.sha256_0_llki_sendrecv_addr),
      dev_name            = s"sha256_0",
      verilog_module_name = Some(s"sha256_mock_tss")
    ),
    COREParams(
      slave_base_addr     = BigInt(CEPBaseAddresses.sha256_1_base_addr),
      slave_depth         = BigInt(CEPBaseAddresses.sha256_1_depth),
      llki_base_addr      = BigInt(CEPBaseAddresses.sha256_1_llki_base_addr),
      llki_depth          = BigInt(CEPBaseAddresses.sha256_1_llki_depth),
      llki_ctrlsts_addr   = BigInt(CEPBaseAddresses.sha256_1_llki_ctrlsts_addr),
      llki_sendrecv_addr  = BigInt(CEPBaseAddresses.sha256_1_llki_sendrecv_addr),
      dev_name            = s"sha256_1",
      verilog_module_name = Some(s"sha256_mock_tss")
    ),
    COREParams(
      slave_base_addr     = BigInt(CEPBaseAddresses.sha256_2_base_addr),
      slave_depth         = BigInt(CEPBaseAddresses.sha256_2_depth),
      llki_base_addr      = BigInt(CEPBaseAddresses.sha256_2_llki_base_addr),
      llki_depth          = BigInt(CEPBaseAddresses.sha256_2_llki_depth),
      llki_ctrlsts_addr   = BigInt(CEPBaseAddresses.sha256_2_llki_ctrlsts_addr),
      llki_sendrecv_addr  = BigInt(CEPBaseAddresses.sha256_2_llki_sendrecv_addr),
      dev_name            = s"sha256_2",
      verilog_module_name = Some(s"sha256_mock_tss")
    ),
    COREParams(
      slave_base_addr     = BigInt(CEPBaseAddresses.sha256_3_base_addr),
      slave_depth         = BigInt(CEPBaseAddresses.sha256_3_depth),
      llki_base_addr      = BigInt(CEPBaseAddresses.sha256_3_llki_base_addr),
      llki_depth          = BigInt(CEPBaseAddresses.sha256_3_llki_depth),
      llki_ctrlsts_addr   = BigInt(CEPBaseAddresses.sha256_3_llki_ctrlsts_addr),
      llki_sendrecv_addr  = BigInt(CEPBaseAddresses.sha256_3_llki_sendrecv_addr),
      dev_name            = s"sha256_3",
      verilog_module_name = Some(s"sha256_mock_tss")
    ))
  case PeripheryRSAKey => List(
    COREParams(
      slave_base_addr     = BigInt(CEPBaseAddresses.rsa_base_addr),
      slave_depth         = BigInt(CEPBaseAddresses.rsa_depth),
      llki_base_addr      = BigInt(CEPBaseAddresses.rsa_llki_base_addr),
      llki_depth          = BigInt(CEPBaseAddresses.rsa_llki_depth),
      llki_ctrlsts_addr   = BigInt(CEPBaseAddresses.rsa_llki_ctrlsts_addr),
      llki_sendrecv_addr  = BigInt(CEPBaseAddresses.rsa_llki_sendrecv_addr),
      dev_name            = s"rsa"
    ))
  case PeripheryCEPRegistersKey => List(
    CEPREGSParams(
      slave_base_addr     = BigInt(CEPBaseAddresses.cepregs_base_addr),
      slave_depth         = BigInt(CEPBaseAddresses.cepregs_base_depth),
      dev_name            = s"cepregs"
    ))
  case ScratchpadKey => List(
    ScratchpadParams(
      slave_address       = BigInt(CEPBaseAddresses.scratchpad_base_addr),
      slave_depth         = BigInt(CEPBaseAddresses.scratchpad_depth),
      dev_name            = s"scratchpad"
    ))
  case SROTKey => List(
    SROTParams(
      slave_address       = BigInt(CEPBaseAddresses.srot_base_addr),
      slave_depth         = BigInt(CEPBaseAddresses.srot_base_depth),
      cep_cores_base_addr = BigInt(CEPBaseAddresses.cep_cores_base_addr),
      cep_cores_depth     = BigInt(CEPBaseAddresses.cep_cores_depth),
      // The following array results in the creation of LLKI_CORE_INDEX_ARRAY in srot_wrapper.sv
      // The SRoT uses these indicies for routing keys to the appropriate core
      llki_cores_array    = Array(
        CEPBaseAddresses.aes_llki_base_addr,      // Core Index 0 
        CEPBaseAddresses.md5_llki_base_addr,      // Core Index 1 
        CEPBaseAddresses.sha256_0_llki_base_addr, // Core Index 2 
        CEPBaseAddresses.sha256_1_llki_base_addr, // Core Index 3 
        CEPBaseAddresses.sha256_2_llki_base_addr, // Core Index 4 
        CEPBaseAddresses.sha256_3_llki_base_addr, // Core Index 5 
        CEPBaseAddresses.rsa_llki_base_addr,      // Core Index 6 
        CEPBaseAddresses.des3_llki_base_addr,     // Core Index 7 
        CEPBaseAddresses.dft_llki_base_addr,      // Core Index 8 
        CEPBaseAddresses.idft_llki_base_addr,     // Core Index 9 
        CEPBaseAddresses.fir_llki_base_addr,      // Core Index 10
        CEPBaseAddresses.iir_llki_base_addr,      // Core Index 11
        CEPBaseAddresses.gps_0_llki_base_addr,    // Core Index 12
        CEPBaseAddresses.gps_1_llki_base_addr,    // Core Index 13
        CEPBaseAddresses.gps_2_llki_base_addr,    // Core Index 14
        CEPBaseAddresses.gps_3_llki_base_addr     // Core Index 15
      )
    ))
})

// Freedom U500 Dev Kit
class U500DevKitConfig extends Config(
  new WithNExtTopInterrupts(0)   ++
  new U500DevKitPeripherals ++
  new FreedomU500Config().alter((site,here,up) => {
    case SystemBusKey => up(SystemBusKey).copy()
    case PeripheryBusKey => up(PeripheryBusKey, site).copy(dtsFrequency =
    Some(BigDecimal(site(DevKitFPGAFrequencyKey)*1000000).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt))  
    case DTSTimebase => BigInt(1000000)
    case JtagDTMKey => new JtagDTMConfig (
      idcodeVersion = 2,      // 1 was legacy (FE310-G000, Acai).
      idcodePartNum = 0x0000, // Decided to simplify.
      idcodeManufId = 0x489,  // As Assigned by JEDEC to SiFive. Only used in wrappers / test harnesses.
      debugIdleCycles = 5)    // Reasonable guess for synchronization
  })
)
