#include "verilated.h"
#include "Vdft_top_top.h"

#include "DFT.h"

#if VM_TRACE 
#include "verilated_vcd_c.h"
VerilatedVcdC* tfp = new VerilatedVcdC;
#endif 

Vdft_top_top* top;

void evalModel() {
    top->eval();
#if VM_TRACE
    tfp->dump(main_time);
#endif
}

void toggleClock() {
    top->wb_clk_i = ~top->wb_clk_i;
}

void waitForACK() {
    while(top->wb_ack_o == 0) {
        runForClockCycles(1);
    }
}

uint32_t readFromAddress(uint32_t pAddress) {
    top->wb_adr_i = pAddress;
    top->wb_dat_i = 0x00000000;
    top->wb_we_i = 0;
    top->wb_stb_i = 1;
    runForClockCycles(10);
    waitForACK();
    uint32_t data = top->wb_dat_o;
    top->wb_stb_i = 0;
    runForClockCycles(10);
    
    return data;
}

void writeToAddress(uint32_t pAddress, uint32_t pData) {
    top->wb_adr_i = pAddress;
    top->wb_dat_i = pData;
    top->wb_we_i = 1;
    top->wb_stb_i = 1;
    runForClockCycles(10);
    waitForACK();
    top->wb_stb_i = 0;
    top->wb_we_i = 0;
    runForClockCycles(10);
}

bool readyValid(void) {
    return (readFromAddress(DES3_START+0x14) != 0) ? true : false;
}

void start(void) {
    writeToAddress(DES3_START, 0x1);
    writeToAddress(DES3_START, 0x0);
}

void setX(uint16_t i, uint16_t pX0, uint16_t pX1, uint16_t pX2, uint16_t pX3){
    writeToAddress(DES3_START+0xC, pX1<<16|pX0); //Write data
    writeToAddress(DES3_START+0x8, i);           //Write addr
    writeToAddress(DES3_START+0x4, 0x1);         //Load
    writeToAddress(DES3_START+0x4, 0x0);         //Stop
    writeToAddress(DES3_START+0x10, pX3<<16|pX2); //Write data
    writeToAddress(DES3_START+0x8, i);           //Write addr
    writeToAddress(DES3_START+0x4, 0x1);         //Load
    writeToAddress(DES3_START+0x4, 0x0);         //Stop
}

void getY(uint16_t i, uint16_t *pY0, uint16_t *pY1, uint16_t *pY2, uint16_t *pY3) {
    uint32_t temp;
    writeToAddress(DES3_START+0x14, i);    //000101 00

    temp=readFromAddress(DES3_START+0x20); //001000 00
    *pY0=temp;
    *pY1=temp>>16;

    temp=readFromAddress(DES3_START+0x24); //001001 00
    *pY2=temp;
    *pY3=temp>>16;
}

void init(){
    // Initialize Inputs
    top->wb_clk_i = 0;
    top->wb_dat_i = 0;
    top->wb_we_i  = 0;
    top->wb_adr_i = 0;
    top->wb_stb_i = 0;
    runForClockCycles(100);

    printf("Reset complete\r\n");
}

int main(int argc, char **argv, char **env) {
    int test=2, j=0, i=0;
    uint16_t dout[4]={0x0000, 0x0000, 0x0000, 0x0000};
    bool success=true;

    Verilated::commandArgs(argc, argv);
#if VM_TRACE
    Verilated::traceEverOn(true);
#endif
    top = new Vdft_top_top;

#if VM_TRACE
    printf("Initializing traces and opening VCD file\r\n");
    top->trace (tfp, 99);//VCD file gen 
    tfp->open("./obj_dir/Vdft_top_top.vcd");//VCD file gen
#endif

    printf("Initializing interface and resetting core\r\n");

    // Initialize Inputs
    init();
    
	printf("\r\n");
	printf("*********************************************************\r\n");
	printf("* DFT core simulation started ...                       *\r\n");
	printf("*********************************************************\r\n");
	printf("\r\n");

    //Set Inputs
    for(i=0; i<test; i++){
        //start();
        // The 64 complex data points enter the system over 32 cycles
        for (j=0; j < 32; j++){
            switch(i){
            case 0: setX(j, j*4, j*4+1, j*4+2, j*4+3); break;// Input: 2 complex words per cycle
            case 1: setX(j, 128+j*4, 128+j*4+1, 128+j*4+2, 128+j*4+3); break;// Input 4 words per cycle
            }
        }
       
        start();

    //Check Outputs
        waitForValidOutput();
        runForClockCycles(1);
        printf("--- begin output %02d---\r\n", i+1);
        for(j=0;j<32;j++){
            getY(j, &dout[0], &dout[1], &dout[2], &dout[3]);
            switch(i){
            case 0: success=success&assertEquals(j, exp_1[j], dout); break;
            case 1: success=success&assertEquals(j, exp_2[j], dout); break;
            }
        }
    }
    
	printf("\r\n");
    assertSuccess(success);
	printf("\r\n");
	printf("**************************************\r\n");
	printf("* DFT Test done ...                  *\r\n");
	printf("**************************************\r\n");
	printf("\r\n");
    
    printf("Completed\r\n");

#if VM_TRACE    
    tfp->close();
#endif
    top->final();
    delete top;
    exit(0);
}

