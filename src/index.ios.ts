/*

generate meta data in demo dir:
TNS_DEBUG_METADATA_PATH="$(pwd)/metadata" tns build ios

Generate typings in demo dir:
TNS_TYPESCRIPT_DECLARATIONS_PATH="$(pwd)/typings" tns build ios

then copy objc!nsswiftsupport.d.ts to src/platforms/ios/typings
and add the line:
/// <reference path="./platforms/ios/typings/objc!nsswiftsupport.d.ts"/>
to the file: references.d.ts

*/



export class PrintClient {
    constructor(printType?: number){
        var self = this;
        if (!printType) {
            printType = 0;
        }
    }

    public connect(servername: string, port: number, data: string): number {
        var printer = new IOSPrinter()
        printer.setupNetworkCommunicationWithServernamePort(servername, port)
        printer.sendMessageWithMessage(data)
        printer.closePrinter()
        return 0
    }

    public close(): number {
        return 0;
    }

    public send(data: Array<number>): number {
        return 0;
    }

    public receive(): number {
        console.log("==receive======not implemented =============")
        return 0
    }
}
